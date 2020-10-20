package com.site.lmk.entity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

import lombok.Data;

/**
 * (Study)实体类
 *
 * @author lmk
 * @since 2020-09-25 14:35:37
 */
@Data

public class Study implements Serializable {
    private static final long serialVersionUID = 449895107855241402L;

    private String name;

    private Integer id;


    private String sex;

    private String wordid;

}