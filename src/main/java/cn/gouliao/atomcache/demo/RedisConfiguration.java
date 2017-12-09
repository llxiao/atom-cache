package cn.gouliao.atomcache.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import cn.gouliao.atomcache.aop.CacheAspect;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
@Configuration
public class RedisConfiguration {
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        String redisServer = "127.0.0.1";
        String redisPort = "2181";
        String redisPwd = "ycc123456";
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setDatabase(6);
        jedisConnectionFactory.setHostName(redisServer);
        jedisConnectionFactory.setPassword(redisPwd);
        jedisConnectionFactory.setPort(Integer.parseInt(redisPort));
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig());
        return jedisConnectionFactory;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        int redisMaxIdle = 300;
        //控制一个pool可分配多少个jedis实例,用来替换上面的redis.maxActive,如果是jedis 2.4以后用该属性
        int redisMaxTotal = 1000;
        //最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
        int redisMaxWaitMillis = 1000;
        //连接的最小空闲时间 默认1800000毫秒(30分钟)
        long redisMinEvictableIdleTimeMillis = 300000L;
        //每次释放连接的最大数目,默认3
        int redisNumTestsPerEvictionRun = 1024;
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        long redisTimeBetweenEvictionRunsMillis = 30000L;
        //是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        boolean redisTestOnBorrow = true;
        //在空闲时检查有效性, 默认false
        boolean redisTestWhileIdle = true;

        jedisPoolConfig.setMaxIdle(redisMaxIdle);
        jedisPoolConfig.setMaxTotal(redisMaxTotal);
        jedisPoolConfig.setMaxWaitMillis(redisMaxWaitMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisMinEvictableIdleTimeMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(redisNumTestsPerEvictionRun);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisTimeBetweenEvictionRunsMillis);
        jedisPoolConfig.setTestOnBorrow(redisTestOnBorrow);
        jedisPoolConfig.setTestWhileIdle(redisTestWhileIdle);
        return jedisPoolConfig;

    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheAspect getAspect() {
        CacheAspect cacheAspect = new CacheAspect();
        cacheAspect.setRedisTemplate(redisTemplate());
        return cacheAspect;
    }
}
