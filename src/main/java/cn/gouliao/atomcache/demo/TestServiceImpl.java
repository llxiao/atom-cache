package cn.gouliao.atomcache.demo;

import cn.gouliao.atomcache.annotation.AtomCache;
import cn.gouliao.atomcache.annotation.AtomParam;
import cn.gouliao.atomcache.common.ATOM_CACHE_METHOD;
import com.xiaoleilu.hutool.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

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

    @AtomCache(cacheMethod = ATOM_CACHE_METHOD.FIND)
    public Student findByID(@AtomParam Student entity) {
        Friend friend1 = new Friend.Builder().withFriendName("1111").build();
        Friend friend2 = new Friend.Builder().withFriendName("2222").build();
        Friend friend3 = new Friend.Builder().withFriendName("网吧").build();
        Friend friend4 = new Friend.Builder().withFriendName("吃鸡").build();
        Friend friend5 = new Friend.Builder().withFriendName("秋季").build();
        HashSet<Friend> friends = CollectionUtil.newHashSet(friend1, friend2, friend3, friend4, friend5);
        Student student = new Student.Builder()
                .withStudentID("102")
                .withStudentName("李晓")
                .withStudentFriend(friends).build();

        log.info("getFromDB");
        return student;
    }

    @AtomCache(cacheMethod = ATOM_CACHE_METHOD.DELETE)
    public String updateByID(@AtomParam Student entity) {
        log.info("removeCache");
        return "1698741";
    }
}
