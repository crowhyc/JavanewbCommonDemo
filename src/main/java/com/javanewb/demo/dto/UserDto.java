package com.javanewb.demo.dto;

import com.javanewb.demo.entity.DemoUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * <p>
 * Description: com.javanewb.demo.dto
 * </p>
 * date：2017/11/20
 * email:crowhyc@163.com
 *
 * @author Dean.Hwang
 */
@Data
@ApiModel
public class UserDto {
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "年龄")
    @Range(min = 0, max = 100)
    private int age;
    @ApiModelProperty(value = "性别")
    private String gender;

    public DemoUser convertModel() {
        DemoUser demoUser = new DemoUser();
        demoUser.setAddress(this.address);
        demoUser.setAge(this.age);
        demoUser.setGender(this.gender);
        demoUser.setPassword(this.password);
        demoUser.setUsername(this.username);
        return demoUser;
    }
}
