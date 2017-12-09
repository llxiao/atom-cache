package cn.gouliao.atomcache.service;

import java.util.Set;

/**
 * <p>
 * 通用缓存接口
 * </p>
 *
 * @author Shawn
 * @since 2017/12/8
 */
public interface IAtomNormalCache {

    /**
     * 判断cacheKey是否存在
     *
     * @param cacheKey cacheKey
     * @return 是否存在
     */
    boolean cacheKeyExist(String cacheKey);

    /**
     * 存放对象
     *
     * @param cacheKey   cacheKey
     * @param cacheValue 需要存取的对象
     * @return 存放是否成功
     */
    boolean put(String cacheKey, Object cacheValue);

    /**
     * 存放对象
     *
     * @param cacheKey   cacheKey
     * @param second     缓存时间 仅对redis生效
     * @param cacheValue 缓存对象
     * @return 缓存是否成功
     */
    boolean put(String cacheKey, Object cacheValue, long second);

    /**
     * 根据cacheKey 获取具体的对象
     *
     * @param cacheKey cacheKey
     * @return 对象
     */
    Object get(String cacheKey);

    /**
     * 手动删除缓存
     *
     * @param cacheKey cacheKey
     * @return 缓存对象
     */
    boolean remove(String cacheKey);


    /**
     * set存放数据 针对的是redis,guava存取的依然为具体的类型
     *
     * @param cacheKey cacheKey
     * @param objects  存放的对象数组
     * @return 是否成功
     */
    boolean setPut(String cacheKey, Object[] objects);

    /**
     * set存放数据 针对的是redis,guava存取的依然为具体的类型
     *
     * @param cacheKey cacheKey
     * @param objects  存放的对象数组
     * @param second   缓存时间 仅对redis生效
     * @return 是否成功
     */
    boolean setPut(String cacheKey, Object[] objects, long second);

    /**
     * 获取set对象 guava的话 获取完强制转化下类型
     *
     * @param cacheKey cacheKey
     * @return 返回set对象集合
     */
    Set<Object> setGet(String cacheKey);

}
