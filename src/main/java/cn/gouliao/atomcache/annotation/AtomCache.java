package cn.gouliao.atomcache.annotation;

import cn.gouliao.atomcache.common.ATOM_CACHE_LEVEL;
import cn.gouliao.atomcache.common.ATOM_CACHE_TYPE;
import com.sun.istack.internal.NotNull;

import java.lang.annotation.*;

/**
 * AtomCache的注解, 需要Cache的时候放一个此注解到方法之上
 *
 * 1. 此方法必须是public的(cglib是extends目标类进行代理);
 * 2. 此方法必须有返回值(非void)
 *
 * @author Shawn
 * @since 2017/12/8
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomCache {

    /**
     * 执行的缓存操作
     *
     * @return 操作方法
     */
    @NotNull
    ATOM_CACHE_TYPE cacheMethod();

    /**
     * 缓存的类型 默认使用redis和guava缓存,可以根据需求自定义
     *
     * @return 缓存的类型
     */
    ATOM_CACHE_LEVEL cacheType() default ATOM_CACHE_LEVEL.BOTH_REDIS_AND_GUAVA;


    /**
     * 缓存时间 针对redis
     *
     * @return 默认永久保存
     */
    long expire() default 0L;



}
