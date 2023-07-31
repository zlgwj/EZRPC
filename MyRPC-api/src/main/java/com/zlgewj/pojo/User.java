package com.zlgewj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 17:56
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private Integer age;
    private List<User> friends;
}
