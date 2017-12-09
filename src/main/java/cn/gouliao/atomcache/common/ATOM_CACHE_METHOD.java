package cn.gouliao.atomcache.common;


import java.util.HashMap;
import java.util.Map;

/**
 * cache类型
 *
 * @author Shawn
 * @since 2017/12/9
 */

public enum ATOM_CACHE_METHOD {
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
        for (ATOM_CACHE_METHOD pageType : ATOM_CACHE_METHOD.values()) {
            map.put(pageType.value, pageType);
        }
    }

    private int value;

    private ATOM_CACHE_METHOD(int value) {
        this.value = value;
    }

    public static ATOM_CACHE_METHOD valueOf(int value) {
        return (ATOM_CACHE_METHOD) map.get(value);
    }

    public int getValue() {
        return value;
    }


}
