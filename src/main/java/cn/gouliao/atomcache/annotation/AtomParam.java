package cn.gouliao.atomcache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数上的注解,组成实际的cacheKey为preCacheKey+此注解注解的string 参数
 *
 * @author Shawn
 * @since 2017/12/8
 */
@Inherited
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomParam {
}
