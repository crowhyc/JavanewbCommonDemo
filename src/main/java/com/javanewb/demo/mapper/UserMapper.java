package com.javanewb.demo.mapper;

import com.javanewb.common.configuration.mybatis.CommonsMapper;
import com.javanewb.demo.entity.DemoUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Description: com.javanewb.demo.mapper
 * </p>
 * date：2017/11/20
 * email:crowhyc@163.com
 *
 * @author Dean.Hwang
 */
public interface UserMapper extends CommonsMapper<DemoUser> {

    @Select("select * from user where age>#{age} and gender=#{gender}")
    public List<DemoUser> getUserByAgeAndGender(@Param("age") int age, @Param("gender") String gender);
}
