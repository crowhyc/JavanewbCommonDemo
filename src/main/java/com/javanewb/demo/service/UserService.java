package com.javanewb.demo.service;

import com.javanewb.common.redis.RedisKey;
import com.javanewb.common.redis.RedisKeyFactory;
import com.javanewb.common.redis.operations.CommonRedisTemplate;
import com.javanewb.demo.dto.UserDto;
import com.javanewb.demo.entity.DemoUser;
import com.javanewb.demo.mapper.UserDao;
import com.javanewb.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * Description: com.javanewb.demo.service
 * </p>
 * date：2017/11/20
 * email:crowhyc@163.com
 *
 * @author Dean.Hwang
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommonRedisTemplate<String> commonRedisTemplate;

    private RedisKeyFactory keyFactory = new RedisKeyFactory("javanewb");

    /**
     * Mysql
     */
    public DemoUser getUser(long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public long saveUser(UserDto userDto) {
        DemoUser demoUser = userDto.convertModel();
        userMapper.insertUseGeneratedKeys(demoUser);
        return demoUser.getId();
    }

    public List<DemoUser> getUserByAgeAndGender(int age, String gender) {
        return userMapper.getUserByAgeAndGender(age, gender);
    }

    /**
     * mongo
     */
    public List<DemoUser> findUserByMongo(int age, String gender) {
        return userDao.findDemoUserByGenderAndAge(gender, age);
    }

    public DemoUser getUserByMongo(Long id) {
        return userDao.findDemoUserById(id);
    }

    public long saveUserByMongo(UserDto userDto) {
        DemoUser demoUser = userDto.convertModel();
        demoUser = userDao.insert(demoUser);
        return demoUser.getId();
    }

    /**
     * redis
     */

    public String getDataByRedis(Long id) {
        RedisKey key = keyFactory.getRedisKey(id + "");
        return commonRedisTemplate.opsCommonValue().get(key);
    }

    public long saveDataByRedis(String data) {
        Long id = new Random().nextLong();
        RedisKey redisKey = keyFactory.getRedisKey(id + "");
        commonRedisTemplate.opsCommonValue().set(redisKey, data);
        return id;
    }
}
