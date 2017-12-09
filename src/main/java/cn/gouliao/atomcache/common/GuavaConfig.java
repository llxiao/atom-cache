package cn.gouliao.atomcache.common;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
public interface GuavaConfig {

    /**
     * 最大存储100,000数据
     */
    long MAX_CACHE_SIZE = 1000000L;

    /**
     * 缓存的Access过期时间，默认 3600s
     */
    long ACCESS_EXPIRE_SECONDS = 3600L;

    /**
     * 缓存的Write过期时间，默认 3600s
     */
    long WRITE_EXPIRE_SECONDS = 3600L;
}
