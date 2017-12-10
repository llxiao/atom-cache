package cn.gouliao.atomcache.test;

import cn.gouliao.atomcache.demo.Student;
import cn.gouliao.atomcache.demo.TestServiceImpl;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author Shawn
 * @since 2017/12/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"}) //加载配置文件
@Slf4j
public class CacheTest {
    @Resource
    private TestServiceImpl testService;

    @Test
    public void testFind() {
        Student entity = new Student.Builder().withStudentID("102").build();
        RateLimiter rateLimiter = RateLimiter.create(2);
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
            Student byID = testService.findByID(entity);
            log.info("findByID{}={}",i,byID);
        }


    }

    @Test
    public void testRemove() {
        Student entity = new Student.Builder().withStudentID("102").build();
        testService.updateByID(entity);


    }


}
