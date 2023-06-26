package com.panther.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片实体类
 *
 * @author Gin 琴酒
 * @data 2023/6/18 16:31
 */
@Data
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String url;

}
