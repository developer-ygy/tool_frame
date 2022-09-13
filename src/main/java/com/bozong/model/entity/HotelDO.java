package com.bozong.model.entity;

import com.bozong.constant.Constants;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * @author yinguoyu
 * @date 2022/9/13
 * @des 
**/
@Data
/**
 * 映射 es 索引，并设置不自动创建索引（因为自动创建的索引可能存在类型不准确的情况）
 */
@Document(indexName = Constants.HOTEL, createIndex = false)
public class HotelDO {

    /**
     * es 映射对象必须存在 id 属性，7.10.2 中，如果在创建索引的时候不指定 id 类型的话，默认是 String 类型的
     */
    private String id;

    /**
     * 酒店标题
     */
    private String title;

    /**
     * 城市
     */
    private String city;

    /**
     * 价格
     */
    private Double price;

    /**
     * 创建时间
     */
    private Date createTime;

}