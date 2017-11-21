package com.javanewb.demo.mapper;

import com.javanewb.demo.entity.DemoUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * <p>
 * Description: com.javanewb.demo.mapper
 * </p>
 * date：2017/11/21
 * email:crowhyc@163.com
 *
 * @author Dean.Hwang
 */
public interface UserDao extends MongoRepository<DemoUser, String> {
    List<DemoUser> findDemoUserByGenderAndAge(String gender, int age);

    DemoUser findDemoUserById(Long id);
}
