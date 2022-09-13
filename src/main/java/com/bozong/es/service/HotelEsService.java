package com.bozong.es.service;

import com.bozong.model.dto.HotelDTO;
import com.bozong.model.entity.HotelDO;

import java.util.List;

/**
 * @author yinguoyu
 * @date 2022/9/13
 * @des
 **/
public interface HotelEsService {

    /**
     * 根据条件查询酒店信息
     * @param hotelDTO
     * @return
     */
    List<HotelDO> queryByCondition(HotelDTO hotelDTO);

}
