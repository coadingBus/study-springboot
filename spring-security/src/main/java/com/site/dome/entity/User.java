package com.site.dome.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author lmk
 * @since 2020-10-20 20:56:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -39990381162862913L;

    private String name;

    private Integer id;

    private String sex;

    private String describes;

}