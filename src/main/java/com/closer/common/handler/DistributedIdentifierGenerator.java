package com.closer.common.handler;

import com.closer.common.helper.ContextHelper;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

/**
 * 支持分布式
 * 规则：
 * Long类型，总计占53位(考虑JavaScript仅能表示53位整形)
 * 1.时间秒数，占 30 bit，可表示[34]年
 * 2.集群间计数器 占 {COUNTER_BITS} bit，可表示[1024]个数，这里使用redis来处理，每过{PER_TIME}毫秒进行清0
 * 3.实例内计数器 占 {SEQUENCE_BITS} bit，可表示[8096]个数,每过{COUNTER_EXPIRE_TIME}毫秒进行清0
 * 注意：这里的{COUNTER_EXPIRE_TIME}不宜设置过大，过大之后，当redis宕掉恢复后，如果计数又重新开始，且又
 * 在同一个{COUNTER_EXPIRE_TIME}时间窗口内，则会引起主键重复。同时又不宜设置过小，会导致频繁的读写redis。
 * 这里主要考虑的是一个{PER_TIME}时间窗口内，redis宕掉之后也无法恢复。
 * 实现参考：http://www.oschina.net/code/snippet_147955_25122
 * Created by closer on 2016/2/2.
 * @since 1.0
 */
//TODO 还需要做性能测试
public class DistributedIdentifierGenerator implements IdentifierGenerator, Configurable {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedIdentifierGenerator.class);

    /**
     * 项目起始纪元(此处取2016-01-01:00:00:00.000)
     */
    private static final long PROJECT_EPOCH = 1451577600L;

    /**
     * 集群间计数过期时间，单位秒
     */
    private static final long COUNTER_EXPIRE_TIME = 60 * 5L;

    /**
     * 集群间计数所占位数
     */
    private static final int COUNTER_BITS = 10;
    /**
     * 实例内计数所占位数
     */
    private static final int SEQUENCE_BITS = 13;

    /**
     * 时间位移数
     */
    private static final int TIME_SHIFT = COUNTER_BITS + SEQUENCE_BITS;

    /**
     * 集群间计数位移数
     */
    private static final int COUNTER_SHIFT = SEQUENCE_BITS;

    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final long COUNTER_MASK = ~(-1L << COUNTER_BITS);

    private RedisTemplate<String, String> redisTemplate;
    private RedisScript<String> script;

    private long lastTimestamp = -1L;

    private long sequence = 0L;

    private long counter = 0L;

    private long counterRefreshTimestamp = -1L;

    private String key;

    @Override
    public void configure(Type type, Properties params, Dialect d) throws MappingException {
        String jpaEntityName = params.getProperty(IdentifierGenerator.JPA_ENTITY_NAME);
        key = "idg:" + jpaEntityName;
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return nextId();
    }

    /**
     * 获取集群间计数
     *
     * @return 集群间计数
     */
    private long getDistributedCounter(long timestamp) {
        if (redisTemplate == null) {
            synchronized (this) {
                if (redisTemplate == null) {
                    redisTemplate = ContextHelper.applicationContext()
                            .getBean("redisTemplate", StringRedisTemplate.class);
                    script = (RedisScript<String>) ContextHelper.applicationContext().getBean("idgScript");
                }
            }
        }

        String resultStr = redisTemplate.execute(script, Collections.EMPTY_LIST,
                key, String.valueOf(COUNTER_EXPIRE_TIME), String.valueOf(COUNTER_MASK),
                String.valueOf(COUNTER_BITS));
        String[] result = resultStr.split(",");
        counterRefreshTimestamp = timestamp + Long.parseLong(result[0]);
        return Long.parseLong(result[1]) & COUNTER_MASK;
    }

    private synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            LOG.error(String.format("时间回退了. 拒绝直到%d的请求", lastTimestamp));
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextSecond(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        if (timestamp > counterRefreshTimestamp) {
            counter = getDistributedCounter(timestamp);
        }

        lastTimestamp = timestamp;

        return ((timestamp - PROJECT_EPOCH) << TIME_SHIFT) | (counter << COUNTER_SHIFT) | sequence;
    }

    private long tilNextSecond(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp == lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis() / 1000;
    }

    public static void main(String[] args) {
        long add = (1L << 30) * 1000;
        System.out.println(new Date(PROJECT_EPOCH * 1000 + add));

        long id = 30784723361792L;
        long time = ((id >> TIME_SHIFT) + PROJECT_EPOCH) * 1000;
        System.out.println(new Date(time));

    }
}
