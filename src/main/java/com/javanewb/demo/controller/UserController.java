package com.javanewb.demo.controller;

import com.javanewb.demo.dto.UserDto;
import com.javanewb.demo.entity.DemoUser;
import com.javanewb.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * Description: com.javanewb.demo.controller
 * </p>
 * date：2017/11/20
 * email:crowhyc@163.com
 *
 * @author Dean.Hwang
 */
@RestController
@Api("UserDemoController")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "添加用户", notes = "添加用户")
    @RequestMapping(value = "user/add", method = RequestMethod.POST)
    public long addUser(@Valid @RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @ApiOperation(value = "获取用户", notes = "获取用户")
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public DemoUser getUser(@PathVariable(value = "id") Long id) {
        return userService.getUser(id);
    }

    @ApiOperation(value = "查找年龄和性别符合的用户", notes = "添加用户")
    @RequestMapping(value = "user/find", method = RequestMethod.GET)
    public List<DemoUser> findUser(int age, String gender) {
        return userService.getUserByAgeAndGender(age, gender);
    }

    @ApiOperation(value = "添加用户", notes = "添加用户")
    @RequestMapping(value = "user/mongo/add", method = RequestMethod.POST)
    public long addUserMongo(@Valid @RequestBody UserDto userDto) {
        return userService.saveUserByMongo(userDto);
    }

    @ApiOperation(value = "获取用户", notes = "获取用户")
    @RequestMapping(value = "user/mongo/{id}", method = RequestMethod.GET)
    public DemoUser getUserMongo(@PathVariable(value = "id") long id) {
        return userService.getUserByMongo(id);
    }

    @ApiOperation(value = "查找年龄和性别符合的用户", notes = "添加用户")
    @RequestMapping(value = "user/mongo/find", method = RequestMethod.GET)
    public List<DemoUser> findUserMongo(int age, String gender) {
        return userService.findUserByMongo(age, gender);
    }
    @ApiOperation(value = "添加用户", notes = "添加用户")
    @RequestMapping(value = "user/redis/add", method = RequestMethod.POST)
    public long addUserRedis(@Valid @RequestBody String data) {
        return userService.saveDataByRedis(data);
    }

    @ApiOperation(value = "获取用户", notes = "获取用户")
    @RequestMapping(value = "user/redis/{id}", method = RequestMethod.GET)
    public String getUserRedis(@PathVariable(value = "id") long id) {
        return userService.getDataByRedis(id);
    }

}
