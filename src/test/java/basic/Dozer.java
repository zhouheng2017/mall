package basic;

import com.google.common.collect.Lists;
import com.mall.test.TargetUSer;
import com.mall.test.USerDO;
import com.mall.test.User;
import com.mall.test.UserSource;
import com.mall.util.BeanCopyUtil;
import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 14:21
 */
public class Dozer extends ApplicationTest {
    @Autowired
    private Mapper mapper;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setMail("Email@163.com");
        UserSource userSource = new UserSource();
        userSource.setHah("hahah");
        List<Integer> list = Lists.newArrayList();
        list.add(1);
        list.add(12);
        list.add(13);
        list.add(14);
        list.add(15);
        userSource.setList(list);
        userSource.setId("id");

        USerDO map = mapper.map(user, USerDO.class);
        System.out.println(map);

        BeanCopyUtil.copyProperties(userSource, map);
        System.out.println(map);
        USerDO map1 = mapper.map(userSource, USerDO.class);
        System.out.println(map1);
        USerDO uSerDO = new USerDO();
//
//        BeanUtils.copyProperties(userSource, map);
//        System.out.println(map);
//
//        BeanUtils.copyProperties(user, uSerDO);
//        System.out.println(uSerDO);

    }
}
