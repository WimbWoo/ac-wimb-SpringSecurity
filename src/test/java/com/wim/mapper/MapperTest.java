package com.wim.mapper;

import com.wim.domain.Menu;
import com.wim.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
public class MapperTest {
 
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;
 
    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testMenuMapper() {
        List<String> menus = menuMapper.selectPermsByUserId(1L);
        System.out.println(menus);
    }

    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pd1 = passwordEncoder.encode("111");
        String pd2 = passwordEncoder.encode("1234");
        System.out.println("pd1:" + pd1);
        System.out.println("pd2:"+ pd2);
        String encoderResult = "$2a$10$wg64i0Vt1UuhGzPxCuDgF.xn7VRFjd68f.LsPoV4YTL7iHqmqtGum";
        System.out.println(passwordEncoder.matches("1234", encoderResult));
    }
}
