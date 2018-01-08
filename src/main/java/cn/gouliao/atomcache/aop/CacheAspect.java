package cn.gouliao.atomcache.aop;

import com.xiaoleilu.hutool.util.StrUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import cn.gouliao.atomcache.annotation.AtomCache;
import cn.gouliao.atomcache.annotation.AtomParam;
import cn.gouliao.atomcache.common.ATOM_CACHE_LEVEL;
import cn.gouliao.atomcache.common.ATOM_CACHE_TYPE;
import cn.gouliao.atomcache.needimpl.IGetCacheKey;
import cn.gouliao.atomcache.service.ServiceMange;
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
public class CacheAspect {

    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 设置切片位置
     */
    @Pointcut("@annotation(cn.gouliao.atomcache.annotation.AtomCache)")
    public void setJoinPoint() {
    }

    /**
     * 环绕通知
     */
    @Around(value = "setJoinPoint()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        Object[] args = joinPoint.getArgs();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        //获取到此方法上,此注解传递的参数
        AtomCache cacheAnnotation = method.getAnnotation(AtomCache.class);
        if (cacheAnnotation == null) {
            result = joinPoint.proceed();
            return result;
        }
        ATOM_CACHE_TYPE cacheMethod = cacheAnnotation.cacheMethod();
        ATOM_CACHE_LEVEL cacheLevel = cacheAnnotation.cacheType();
        long expireTime = cacheAnnotation.expire();

        Annotation[][] annotations = method.getParameterAnnotations();
        String cacheKey = null;
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == AtomParam.class) {
                    IGetCacheKey iGetCacheKey = (IGetCacheKey) args[i];
                    cacheKey = iGetCacheKey.getCacheKey();
                    break;
                }
            }
        }
        ServiceMange mange = ServiceMange.getInstance(cacheLevel, redisTemplate);
        if (StrUtil.isNotBlank(cacheKey)) {
            if (cacheMethod == ATOM_CACHE_TYPE.FIND) {
                result = mange.get(cacheKey);
                if (result != null) {
                    return result;
                }
            } else if (cacheMethod == ATOM_CACHE_TYPE.DELETE) {
                mange.remove(cacheKey);
            }
        }

        //没有命中缓存 继续执行
        result = joinPoint.proceed();

        if (StrUtil.isNotBlank(cacheKey)) {
            //如果是查询 并且查询结果不为null 则更新下缓存
            if (cacheMethod == ATOM_CACHE_TYPE.FIND) {
                if (result != null) {
                    mange.put(cacheKey, result, expireTime);
                }
            } else if (cacheMethod == ATOM_CACHE_TYPE.DELETE) {
                //如果是删除 则删除缓存
                mange.remove(cacheKey);
            }

        }
        return result;
    }


    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}
