package com.bozong.es.service.impl;

import com.bozong.constant.Constants;
import com.bozong.es.service.HotelEsService;
import com.bozong.model.dto.HotelDTO;
import com.bozong.model.entity.HotelDO;
import com.bozong.util.EsUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * @author yinguoyu
 * @date 2022/9/13
 * @des
 **/
@Service
public class HotelEsServiceImpl implements HotelEsService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public List<HotelDO> queryByCondition(HotelDTO hotelDTO) {
        List<HotelDO> list;
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //创建条件
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        //组装查询条件
        if (StringUtils.hasLength(hotelDTO.getTitle())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", hotelDTO.getTitle()));
        }
        if (StringUtils.hasLength(hotelDTO.getCity())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("city", hotelDTO.getCity()));
        }
        if (hotelDTO.getPrice() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("price", hotelDTO.getPrice()));
        }
        //放入条件
        builder.withQuery(boolQueryBuilder);

        //排序
        builder.withSorts(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));

        //分页查询
        list = EsUtil.pageAssist(elasticsearchRestTemplate,
                1,
                10,
                builder.build(),
                HotelDO.class,
                Constants.HOTEL);

        return list;
    }

}