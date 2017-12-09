package cn.gouliao.atomcache.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.gouliao.atomcache.common.GuavaConfig;
import cn.gouliao.atomcache.service.IAtomNormalCache;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * guava缓存的实现类
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
@Slf4j
public class GuavaCacheImpl implements IAtomNormalCache {

    private static volatile GuavaCacheImpl instance;
    /**
     * 本地的获取cache   http://ifeve.com/google-guava-cachesexplained/
     */
    private Cache<String, Object> cache;

    private GuavaCacheImpl() {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(GuavaConfig.MAX_CACHE_SIZE)
                .expireAfterAccess(GuavaConfig.ACCESS_EXPIRE_SECONDS, TimeUnit.SECONDS)
                .expireAfterWrite(GuavaConfig.WRITE_EXPIRE_SECONDS, TimeUnit.SECONDS)
                .softValues()
                .build();
    }

    public static GuavaCacheImpl getInstance() {
        if (instance == null) {
            synchronized (GuavaCacheImpl.class) {
                if (instance == null) {
                    instance = new GuavaCacheImpl();
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
        return cache.getIfPresent(cacheKey) != null;
    }

    @Override
    public boolean put(String cacheKey, Object cacheValue) {
        if (StrUtil.isBlank(cacheKey)) {
            // 直接返回设置不成功，避免导致业务逻辑出错
            log.warn("cacheKey is null or empty");
            return false;
        }
        if (null == cacheValue) {
            log.warn("value is null");
            return false;
        }
        cache.put(cacheKey, cacheValue);
        return true;
    }

    @Override
    public boolean put(String cacheKey, Object cacheValue, long second) {
        if (StrUtil.isBlank(cacheKey)) {
            // 直接返回设置不成功，避免导致业务逻辑出错
            log.warn("cacheKey is null or empty");
            return false;
        }
        if (null == cacheValue) {
            log.warn("value is null");
            return false;
        }
        cache.put(cacheKey, cacheValue);
        return true;
    }

    @Override
    public Object get(String cacheKey) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return null;
        }
        Object object = cache.getIfPresent(cacheKey);
        if (null == object){
            log.warn("cacheKey={} 在guavaCache不存在");
        }
        return object;
    }

    @Override
    public boolean remove(String cacheKey) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return true;
        }
        cache.invalidate(cacheKey);
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
        cache.put(cacheKey, CollectionUtil.newHashSet(objects));
        return true;
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
        cache.put(cacheKey, CollectionUtil.newHashSet(objects));
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Object> setGet(String cacheKey) {
        if (StrUtil.isBlank(cacheKey)) {
            log.warn("cacheKey is null or empty");
            return null;
        }
        Set<Object> returnSet = null;
        Object object = cache.getIfPresent(cacheKey);
        if (null != object) {
            returnSet = (Set<Object>) object;
        }
        return returnSet;
    }
}
