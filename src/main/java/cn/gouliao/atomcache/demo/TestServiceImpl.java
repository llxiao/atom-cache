package cn.gouliao.atomcache.demo;

import cn.gouliao.atomcache.annotation.AtomCache;
import cn.gouliao.atomcache.annotation.AtomParam;
import cn.gouliao.atomcache.common.ATOM_CACHE_METHOD;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
@Slf4j
public class TestServiceImpl {

    @AtomCache(cacheKey = "client", targetClass = String.class, cacheMethod = ATOM_CACHE_METHOD.FIND)
    public String findByID(@AtomParam String ID) {
        System.out.println("getFromDB");
        return "1698741";
    }

    @AtomCache(cacheKey = "client", targetClass = String.class, cacheMethod = ATOM_CACHE_METHOD.DELETE)
    public String updateByID(@AtomParam String ID) {
        System.out.println("removeCache");
        return "1698741";
    }
}
