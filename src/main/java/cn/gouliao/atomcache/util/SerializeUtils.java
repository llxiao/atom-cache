package cn.gouliao.atomcache.util;


import com.google.gson.Gson;

/***
 * <p>
 * gsonStr to object
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
public class SerializeUtils {


    /**
     * 反序列化为具体对象
     */
    public static <T> T deserialize(Object cacheValue, Class<T> clazz) {
        Gson gson = new Gson();
        String gsonStr = gson.toJson(cacheValue);
        return gson.fromJson(gsonStr, clazz);
    }
}
