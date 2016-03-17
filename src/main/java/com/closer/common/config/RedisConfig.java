package com.closer.common.config;

import com.closer.tenant.domain.Tenant;
import com.fasterxml.jackson.databind.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Redis配置
 * Created by Zhang Jinlong(150429) on 2016/1/25.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private WebConfig webConfig;

    private static final String host = "localhost";

    private static final int port = 6379;

    private static final int db = 0;

    private static final int maxIdle = 10;

    private static final int minIdle = 2;

    private static final int maxWait = 3000;

    private static final boolean testOnBorrow = true;

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
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisScript<String> idgScript() {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("idg.lua")));
        redisScript.setResultType(String.class);
        return redisScript;
    }

    @Bean
    public CacheManager cacheManager() {
        Set<CacheManager> cacheManagers = new HashSet<>();
        cacheManagers.add(getCacheManager(Tenant.class, "tenants"));
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        cacheManager.setCacheManagers(cacheManagers);
        cacheManager.afterPropertiesSet();
        return cacheManager;
    }

    private CacheManager getCacheManager(Class type, String cacheName) {
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(type);
        return _getCacheManager(cacheName, redisSerializer);
    }

    private CacheManager getCacheManager(JavaType type, String cacheName) {
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(type);
        return _getCacheManager(cacheName, redisSerializer);
    }

    private CacheManager _getCacheManager(String cacheName, Jackson2JsonRedisSerializer redisSerializer) {
        redisSerializer.setObjectMapper(webConfig.objectMapper());
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setDefaultSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();

        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(cacheName);
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate, cacheNames);
        cacheManager.setUsePrefix(true);
        //12 hours
        cacheManager.setDefaultExpiration(60 * 60 * 12);
        cacheManager.setTransactionAware(true);
        cacheManager.afterPropertiesSet();
        return cacheManager;
    }

}
