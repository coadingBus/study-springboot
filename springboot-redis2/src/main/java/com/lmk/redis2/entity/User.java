package com.lmk.redis2.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Study)实体类
 *
 * @author lmk
 * @since 2020-09-10 14:44:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 701066726681770595L;

    private String name;

    private Long id;

    private String sex;

    private String describes;
    
}