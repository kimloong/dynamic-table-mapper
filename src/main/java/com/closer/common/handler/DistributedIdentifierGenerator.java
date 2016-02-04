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

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 支持分布式
 * 规则：
 * Long类型，总计占53位(考虑JavaScript仅能表示53位整形)
 * 1.时间毫秒数/{PER_TIME}，占 {TIME_BITS} bit，可表示[21]年
 * 2.集群间计数器 占 {COUNTER_BITS} bit，可表示[2048]个数，这里使用redis来处理，每过{PER_TIME}毫秒进行清0
 * 3.实例内计数器 占 {SEQUENCE_BITS} bit，可表示[65536]个数,每过{PER_TIME}毫秒进行清0
 * 注意：这里的{PER_TIME}不宜设置过大，过大之后，当redis宕掉恢复后，如果计数又重新开始，且又
 * 在同一个{PER_TIME}时间窗口内，则会引起主键重复。同时又不宜设置过小，会导致频繁的读写redis。
 * 这里主要考虑的是一个{PER_TIME}时间窗口内，redis宕掉之后也无法恢复。
 * 实现参考：http://www.oschina.net/code/snippet_147955_25122
 * <p/>
 * Created by closer on 2016/2/2.
 */
public class DistributedIdentifierGenerator implements IdentifierGenerator, Configurable {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedIdentifierGenerator.class);

    /**
     * 项目起始纪元(此处取2016-01-01:00:00:00.000)
     */
    private static final long _PROJECT_EPOCH = 1451577600000L;

    /**
     * id中被排除的时间位数
     */
    private static final int PER_TIME = 1000 * 10;
    /**
     * 根据 PER_TIME 计算后的项目起始纪元
     */
    private static final long PROJECT_EPOCH = _PROJECT_EPOCH / PER_TIME;
    /**
     * 时间所占位数
     */
    private static final int TIME_BITS = 26;

    /**
     * 集群间计数所占位数
     */
    private static final int COUNTER_BITS = 11;
    /**
     * 实例内计数所占位数
     */
    private static final int SEQUENCE_BITS = 16;

    /**
     * 时间位移数
     */
    private static final int TIME_SHIFT = COUNTER_BITS + SEQUENCE_BITS;

    /**
     * 集群间计数位移数
     */
    private static final int COUNTER_SHIFT = SEQUENCE_BITS;

    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private RedisTemplate<String, String> redisTemplate;

    private long lastTimestamp = -1L;

    private long sequence = 0L;

    @Override
    public void configure(Type type, Properties params, Dialect d) throws MappingException {
        redisTemplate = ContextHelper.applicationContext()
                .getBean("redisTemplate", StringRedisTemplate.class);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        String key = "idg:" + object.getClass().getSimpleName();
        getDistributedCounter(key);
        long nowFromEpoch = System.currentTimeMillis() / PER_TIME - PROJECT_EPOCH;
        return null;
    }

    /**
     * 获取集群间计数
     *
     * @param key key
     * @return 集群间计数
     */
    private long getDistributedCounter(String key) {
        long i = redisTemplate.opsForValue().increment(key, 1);
        if (i == 1) {
            redisTemplate.expire(key, PER_TIME, TimeUnit.MILLISECONDS);
        }
        return i;
    }

//    public synchronized long nextId(long timestamp) {
//        if (timestamp < lastTimestamp) {
//            LOG.error(String.format("时间回退了. 拒绝直到%d的请求", lastTimestamp * PER_TIME));
//            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
//        }
//
//        if (lastTimestamp == timestamp) {
//            sequence = (sequence + 1) & SEQUENCE_MASK;
//            if (sequence == 0) {
//                timestamp = tilNextMillis(lastTimestamp);
//            }
//        } else {
//            sequence = 0L;
//        }
//
//        lastTimestamp = timestamp;
//
//        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
//    }

    public static void main(String[] args) {
        long add = (1L << 26) * PER_TIME;

        System.out.println(new Date(_PROJECT_EPOCH + add));
        System.out.println((1L << 11));
        System.out.println((1L << 16));
        System.out.println(26 + 11 + 16);

    }
}
