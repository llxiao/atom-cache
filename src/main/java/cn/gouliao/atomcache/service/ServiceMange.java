package cn.gouliao.atomcache.service;

import cn.gouliao.atomcache.common.ATOM_CACHE_LEVEL;
import cn.gouliao.atomcache.service.impl.GuavaCacheImpl;
import cn.gouliao.atomcache.service.impl.RedisCacheImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.LinkedList;

/**
 * 类说明
 *
 * @author shawn
 * @since 2017/12/10
 */
@Slf4j
public class ServiceMange {
    private static volatile ServiceMange instance;

    private LinkedList<IAtomNormalCache> serviceList;


    private ServiceMange(ATOM_CACHE_LEVEL level, RedisTemplate<String, Object> redisTemplate) {
        this.serviceList = new LinkedList<>();
        this.initService(level, redisTemplate);
    }

    public static ServiceMange getInstance(ATOM_CACHE_LEVEL level, RedisTemplate<String, Object> redisTemplate) {
        if (instance == null) {
            synchronized (ServiceMange.class) {
                if (instance == null) {
                    instance = new ServiceMange(level, redisTemplate);
                }
            }
        }
        return instance;
    }


    public void put(String key, Object value, long expireTime) {
        for (IAtomNormalCache cache : serviceList) {
            cache.put(key, value, expireTime);
        }
    }


    public Object get(String key) {
        Object returnObj = null;
        boolean needRefreshGuava = false;
        IAtomNormalCache refreshCache = null;
        for (IAtomNormalCache cache : serviceList) {
            returnObj = cache.get(key);
            if (cache instanceof GuavaCacheImpl && returnObj == null) {
                needRefreshGuava = true;
                refreshCache = cache;
            }
            if (returnObj != null) {
                if (needRefreshGuava) {
                    log.info("get cacheKey={} from redis cache", key);
                }else {
                    log.info("get cacheKey={} from guava cache", key);
                }
                break;
            }
        }
        if (returnObj != null && needRefreshGuava) {
            refreshCache.put(key, returnObj);
        }
        return returnObj;
    }


    public void remove(String key) {
        for (IAtomNormalCache cache : serviceList) {
            cache.remove(key);
        }
    }


    /**
     * 初始化缓存集合
     *
     * @param level         缓存等级
     * @param redisTemplate redisTemplate
     */
    private void initService(ATOM_CACHE_LEVEL level, RedisTemplate<String, Object> redisTemplate) {
        serviceList = new LinkedList<>();
        switch (level) {
            case GUAVA: {
                IAtomNormalCache guavaCache = GuavaCacheImpl.getInstance();
                serviceList.add(guavaCache);
                break;
            }
            case REDIS: {
                if (redisTemplate == null) {
                    log.error("redisTemplate不能为空 添加redis缓存出错");
                    break;
                }
                IAtomNormalCache redisCache = RedisCacheImpl.getInstance(redisTemplate);
                serviceList.add(redisCache);
                break;
            }
            case BOTH_REDIS_AND_GUAVA: {
                IAtomNormalCache guavaCache = GuavaCacheImpl.getInstance();
                serviceList.add(guavaCache);

                if (redisTemplate == null) {
                    log.error("redisTemplate不能为空 添加redis缓存出错");
                } else {
                    IAtomNormalCache redisCache = RedisCacheImpl.getInstance(redisTemplate);
                    serviceList.add(redisCache);
                }
                break;
            }
            default:
                break;
        }
    }
}
