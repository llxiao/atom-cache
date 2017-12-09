package cn.gouliao.atomcache.service.impl;

import com.xiaoleilu.hutool.util.StrUtil;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.gouliao.atomcache.service.IAtomNormalCache;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * redisCache缓存实现类
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
@Slf4j
public class RedisCacheImpl implements IAtomNormalCache {

    private static volatile RedisCacheImpl instance;
    private RedisTemplate<String, Object> template;


    private RedisCacheImpl(RedisTemplate<String, Object> redisTemplate) {
        this.template = redisTemplate;
    }

    public static RedisCacheImpl getInstance(RedisTemplate<String, Object> redisTemplate) {
        if (instance == null) {
            synchronized (RedisCacheImpl.class) {
                if (instance == null) {
                    instance = new RedisCacheImpl(redisTemplate);
                }
            }
        }
        return instance;
    }

    @Override
    public boolean cacheKeyExist(String cacheKey) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return false;
        }
        return template.hasKey(cacheKey);
    }

    @Override
    public boolean put(String cacheKey, Object cacheValue) {
        if (StrUtil.isBlank(cacheKey) || null == cacheValue) {
            log.warn("cacheKey or cacheValue is null or empty");
            return false;
        }
        template.opsForValue().set(cacheKey, cacheValue);
        return true;
    }

    @Override
    public boolean put(String cacheKey, Object cacheValue, long second) {
        if (StrUtil.isBlank(cacheKey) || null == cacheValue) {
            log.warn("cacheKey or cacheValue is null or empty");
            return false;
        }
        if (second > 0) {
            template.opsForValue().set(cacheKey, cacheValue, second, TimeUnit.SECONDS);
        } else {
            this.put(cacheKey, cacheValue);
        }
        return true;
    }

    @Override
    public Object get(String cacheKey) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return null;
        }
        if (!cacheKeyExist(cacheKey)) {
            log.warn("cacheKey={}的缓存在redisCache不存在", cacheKey);
            return null;
        }
        Object object = template.opsForValue().get(cacheKey);
        return object;
    }

    @Override
    public boolean remove(String cacheKey) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return false;
        }
        template.delete(cacheKey);
        return true;
    }

    @Override
    public boolean setPut(String cacheKey, Object[] objects) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return false;
        }
        if (objects == null || objects.length == 0) {
            log.warn("what do you want to cache");
            return false;
        }
        Long add = template.opsForSet().add(cacheKey, objects);
        return add > 0;
    }

    @Override
    public boolean setPut(String cacheKey, Object[] objects, long second) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return false;
        }
        if (objects == null || objects.length == 0) {
            log.warn("what do you want to cache");
            return false;
        }
        Long add = template.opsForSet().add(cacheKey, objects);
        if (second > 0) {
            template.expire(cacheKey, second, TimeUnit.SECONDS);
        }
        return add > 0;
    }

    @Override
    public Set<Object> setGet(String cacheKey) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return null;
        }
        return template.opsForSet().members(cacheKey);
    }
}
