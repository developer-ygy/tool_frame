package com.bozong.model.dto;

import lombok.Data;

/**
 * @author yinguoyu
 * @date 2022/9/13
 * @des 
**/
@Data
public class HotelDTO {

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

}