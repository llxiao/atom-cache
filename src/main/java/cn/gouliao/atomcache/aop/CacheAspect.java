package cn.gouliao.atomcache.aop;

import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cn.gouliao.atomcache.annotation.AtomCache;
import cn.gouliao.atomcache.annotation.AtomParam;
import cn.gouliao.atomcache.common.ATOM_CACHE_LEVEL;
import cn.gouliao.atomcache.common.ATOM_CACHE_METHOD;
import cn.gouliao.atomcache.common.ATOM_CACHE_RETURN_TYPE;
import cn.gouliao.atomcache.service.IAtomNormalCache;
import cn.gouliao.atomcache.service.impl.GuavaCacheImpl;
import cn.gouliao.atomcache.service.impl.RedisCacheImpl;
import cn.gouliao.atomcache.util.SerializeUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
@Aspect
@Slf4j
@Component
public class CacheAspect {

    private RedisTemplate<String, Object> redisTemplate;

    private LinkedList<IAtomNormalCache> serviceList;

    //设置切片位置
    @Pointcut("@annotation(cn.gouliao.atomcache.annotation.AtomCache)")
    public void setJoinPoint() {
    }

    //环绕通知
    @Around(value = "setJoinPoint()")
    @SuppressWarnings("unchecked")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        Object[] args = joinPoint.getArgs();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        //获取到此方法上,此注解传递的参数
        AtomCache cacheAnnotation = method.getAnnotation(AtomCache.class);
        String preCacheKey = cacheAnnotation.cacheKey();
        ATOM_CACHE_METHOD cacheMethod = cacheAnnotation.cacheMethod();
        ATOM_CACHE_LEVEL cacheLevel = cacheAnnotation.cacheType();
        long expireTime = cacheAnnotation.expire();
        Class classz = cacheAnnotation.targetClass();
        ATOM_CACHE_RETURN_TYPE returnType = cacheAnnotation.returnType();
        String cacheKey = getCacheKey(method, preCacheKey, args);
        Object cacheGetObj = null;
        if (StrUtil.isNotBlank(cacheKey)) {
            this.initService(cacheLevel, redisTemplate);
            if (cacheMethod == ATOM_CACHE_METHOD.FIND) {

                if (returnType == ATOM_CACHE_RETURN_TYPE.SET_COLLECTION) {
                    Set findSet = this.sGet(serviceList, cacheKey);
                    Set realSet = new HashSet();
                    if (findSet != null) {
                        for (Object object : findSet) {
                            Object deserialize = SerializeUtils.deserialize(object, classz);
                            realSet.add(deserialize);
                        }
                    }
                    cacheGetObj = realSet;
                } else {
                    Object findObj = this.get(serviceList, cacheKey);
                    cacheGetObj = SerializeUtils.deserialize(findObj, classz);
                }

            }
        } else {
            log.warn("没有在方法参数中添加@AtomParam注解,缓存方法未生效");
        }
        if (null != cacheGetObj) {

            return cacheGetObj;
        }
        //没有命中缓存 继续执行
        result = joinPoint.proceed();
        //如果是删除的 就去删除缓存
        if (StrUtil.isNotBlank(cacheKey)) {
            if (cacheMethod == ATOM_CACHE_METHOD.DELETE) {
                this.remove(serviceList, cacheKey);
            } else {
                if (result != null) {
                    if (returnType == ATOM_CACHE_RETURN_TYPE.SET_COLLECTION) {
                        this.sPut(serviceList, cacheKey, result, expireTime);
                    } else {
                        this.put(serviceList, cacheKey, result, expireTime);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取cacheKey
     *
     * @param method      方法
     * @param preCacheKey 前缀
     * @return 缓存key
     */
    private String getCacheKey(Method method, String preCacheKey, Object[] args) {
        Annotation[][] annotations = method.getParameterAnnotations();
        //如果此方法的所有参数有参数注解,才正常执行
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == AtomParam.class) {
                    String realCacheKey = (String) args[i];
                    return StrUtil.format("{}_{}", preCacheKey, realCacheKey);
                }
            }
        }
        return null;
    }
    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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
                IAtomNormalCache redisCache = RedisCacheImpl.getInstance(redisTemplate);
                serviceList.add(redisCache);
                break;
            }
            case BOTH_REDIS_AND_GUAVA: {
                IAtomNormalCache guavaCache = GuavaCacheImpl.getInstance();
                serviceList.add(guavaCache);
                IAtomNormalCache redisCache = RedisCacheImpl.getInstance(redisTemplate);
                serviceList.add(redisCache);
                break;
            }
            default:
                break;
        }

    }

    private void put(LinkedList<IAtomNormalCache> serviceList, String key, Object value, long expireTime) {
        for (IAtomNormalCache cache : serviceList) {
            cache.put(key, value, expireTime);
        }
    }

    private void sPut(LinkedList<IAtomNormalCache> serviceList, String key, Object value, long expireTime) {
        Set set = (Set) value;
        for (IAtomNormalCache cache : serviceList) {
            cache.put(key, set.toArray(), expireTime);
        }
    }

    private Object get(LinkedList<IAtomNormalCache> serviceList, String key) {
        Object returnObj = null;
        for (IAtomNormalCache cache : serviceList) {
            returnObj = cache.get(key);
            if (returnObj != null) {
                break;
            }
        }
        return returnObj;
    }

    private Set<Object> sGet(LinkedList<IAtomNormalCache> serviceList, String key) {
        Set<Object> returnObj = null;
        for (IAtomNormalCache cache : serviceList) {
            returnObj = cache.setGet(key);
            if (CollectionUtil.isNotEmpty(returnObj)) {
                break;
            }
        }
        return returnObj;
    }

    private void remove(LinkedList<IAtomNormalCache> serviceList, String key) {
        for (IAtomNormalCache cache : serviceList) {
            cache.remove(key);
        }
    }
}
