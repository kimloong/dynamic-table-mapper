package com.closer.common.config;

import com.closer.company.domain.Company;
import com.closer.department.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/25.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private WebConfig webConfig;

    private final String host = "localhost";

    private final int port = 6379;

    private final int db = 0;

    private final int maxIdle = 10;

    private final int minIdle = 2;

    private final int maxWait = 3000;

    private boolean testOnBorrow = true;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setDatabase(db);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(webConfig.objectMapper());
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setDefaultSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        Set<CacheManager> cacheManagers = new HashSet<>();
        cacheManagers.add(getCacheManager(Company.class, "companies"));
        cacheManagers.add(getCacheManager(Department.class, "departments"));
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        cacheManager.setCacheManagers(cacheManagers);
        cacheManager.afterPropertiesSet();
        return cacheManager;
    }

    private CacheManager getCacheManager(Class type, String cacheName) {
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(type);
        redisSerializer.setObjectMapper(webConfig.objectMapper());
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setDefaultSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();

        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(cacheName);
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate, cacheNames);
        cacheManager.setUsePrefix(true);
        //1 hours
        cacheManager.setDefaultExpiration(60 * 60 * 12);
        cacheManager.afterPropertiesSet();
        return cacheManager;
    }
}
