package cn.gouliao.atomcache.common;


import java.util.HashMap;
import java.util.Map;

/**
 * cache类型
 *
 * @author Shawn
 * @since 2017/12/9
 */

public enum ATOM_CACHE_LEVEL {
    /**
     * 使用两种缓存
     */
    BOTH_REDIS_AND_GUAVA(0),
    /**
     * 只使用redis缓存
     */
    REDIS(1),
    /**
     * 只使用guava缓存
     */
    GUAVA(2);

    private static Map map = new HashMap();

    static {
        for (ATOM_CACHE_LEVEL pageType : ATOM_CACHE_LEVEL.values()) {
            map.put(pageType.value, pageType);
        }
    }

    private int value;

    private ATOM_CACHE_LEVEL(int value) {
        this.value = value;
    }

    public static ATOM_CACHE_LEVEL valueOf(int value) {
        return (ATOM_CACHE_LEVEL) map.get(value);
    }

    public int getValue() {
        return value;
    }


}
