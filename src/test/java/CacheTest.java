
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import cn.gouliao.atomcache.demo.TestServiceImpl;

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
public class CacheTest {
    @Resource
    private TestServiceImpl testService;
    @Test
    public void testFind() {
        testService.findByID("123");

        testService.findByID("123");
    }


}
