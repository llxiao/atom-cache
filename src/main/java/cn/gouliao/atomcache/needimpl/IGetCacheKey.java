package cn.gouliao.atomcache.needimpl;

/**
 * <p>
 * 生成cacheKey前缀的接口
 * 调用cache 自定义缓存前缀，需要实现此接口或者传入前缀
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
public interface IGetCacheKey {


    /**
     * 获取cacheKey的前缀
     *
     * @return 根据业务需求
     */
    String getCacheKey();
}
