package com.javanewb.demo.entity;

import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * <p>
 * Description: com.javanewb.demo.entity
 * </p>
 * date：2017/11/20
 * email:crowhyc@163.com
 *
 * @author Dean.Hwang
 */
@Data
@Table(name = "user")
@NameStyle(Style.normal)
public class DemoUser implements Serializable {
    @Id
    private long id;
    private String username;
    private String password;
    private String address;
    private int age;
    private String gender;
}
