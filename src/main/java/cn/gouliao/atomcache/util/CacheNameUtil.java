package cn.gouliao.atomcache.util;

import com.xiaoleilu.hutool.lang.StrFormatter;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 类说明
 *
 * @author shawn
 * @since 2017/12/10
 */
@Slf4j
public class CacheNameUtil {

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String getCacheName(CharSequence template, Object... params) {
        if (null == template) {
            return null;
        } else {
            String s = !ArrayUtil.isEmpty(params) && !StrUtil.isBlank(template) ? StrFormatter.format(template.toString(), params) : template.toString();
            String returnStr = StrUtil.format("ATOM_CACHE_{}", s);
            log.info("cacheKey=>:{}", returnStr);
            return returnStr;
        }
    }


}
