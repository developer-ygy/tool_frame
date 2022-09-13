package com.bozong.es.repository;

import com.bozong.model.entity.HotelDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author yinguoyu
 * @date 2022/9/13
 * @des 
**/
public interface HotelRepository extends ElasticsearchRepository<HotelDO,String> {
}