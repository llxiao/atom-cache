package cn.gouliao.atomcache.common;


import java.util.HashMap;
import java.util.Map;

/**
 * cache类型
 *
 * @author Shawn
 * @since 2017/12/9
 */

public enum ATOM_CACHE_TYPE {
    /**
     * 查找 找到直接返回
     */
    FIND(0),
    /**
     * 删除 保存 更新 删除 时 都去把缓存给清除了
     */
    DELETE(1);


    private static Map map = new HashMap();

    static {
        for (ATOM_CACHE_TYPE pageType : ATOM_CACHE_TYPE.values()) {
            map.put(pageType.value, pageType);
        }
    }

    private int value;

    private ATOM_CACHE_TYPE(int value) {
        this.value = value;
    }

    public static ATOM_CACHE_TYPE valueOf(int value) {
        return (ATOM_CACHE_TYPE) map.get(value);
    }

    public int getValue() {
        return value;
    }


}
