package com.bozong.util;

import com.bozong.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class EsUtil {

    /**
     * ES 分页辅助方法
     * 此分页方法最多可以并发处理 500 个请求，因为 es 默认同一时间只能存在 500 个 scroll 滚动上下文
     * @param elasticsearchRestTemplate ：操作模板类
     * @param pageNumber ： 页码
     * @param pageSize ：页大小
     * @param nativeSearchQuery ：筛选条件
     * @param objectClass ：结果实体类
     * @param indexName ：索引名称
     * @param <T> ：结果类型
     * @return ：实体结果集
     */
    public static <T> List<T> pageAssist(ElasticsearchRestTemplate elasticsearchRestTemplate
            , Integer pageNumber
            , Integer pageSize
            , NativeSearchQuery nativeSearchQuery
            , Class<T> objectClass
            , String indexName){

        //保存 scrollId ,用于后续清除
        List<String> scrollIds = new ArrayList<>();
        try{
            if (pageNumber == null){
                pageNumber = 1;
            }
            // 设置每页数据量
            nativeSearchQuery.setMaxResults(pageSize);
            // 1、缓存第一页符合搜索条件的数据
            SearchScrollHits<T> searchScrollHits = elasticsearchRestTemplate.searchScrollStart(Constants.scrollTimeInMillis, nativeSearchQuery, objectClass, IndexCoordinates.of(indexName));
            String scrollId = searchScrollHits.getScrollId();
            //统计下耗时
            long start = System.currentTimeMillis();
            //记录翻页次数
            int scrollTime=1;
            scrollIds.add(scrollId);
            if (searchScrollHits == null){
                log.info("searchScrollHits 为null");
                return new ArrayList<>();
            }

            List<T> resultList = new ArrayList<>();
            //2、判断searchScrollHits中是否有命中数据，如果为空，则表示已将符合查询条件的数据全部遍历完毕
            while (searchScrollHits.hasSearchHits()) {
                if (scrollTime >= pageNumber){
                    break;
                }
                //4、根据上次搜索结果scroll_id进入下一页数据搜索,该方法执行后将重新刷新快照保留时间
                searchScrollHits = elasticsearchRestTemplate.searchScrollContinue(scrollId, Constants.scrollTimeInMillis, objectClass, IndexCoordinates.of(indexName));
                scrollId = searchScrollHits.getScrollId();
                scrollIds.add(scrollId);
                scrollTime=scrollTime+1;
            }

            //3.从缓存中读取数据
            for (SearchHit<T> searchHit : searchScrollHits.getSearchHits()) {
                T obj = searchHit.getContent();
                resultList.add(obj);
            }
            log.info("耗时：" + (System.currentTimeMillis() - start) + " ms");
            return resultList;
        } finally {
            if (!scrollIds.isEmpty()){
                // 清除 scroll
                elasticsearchRestTemplate.searchScrollClear(scrollIds);
            }
        }

    }

    /**
     * 用于查询所有数据使用，由于每次查询最多查询 10000 条数据，所以，采用分页的方式，每次最多查询 10000 条数据来完成全量查询
     * @param elasticsearchRestTemplate
     * @param nativeSearchQuery
     * @param objectClass
     * @param indexName
     * @param <T>
     * @return
     */
    public static <T> List<T> getAllData(ElasticsearchRestTemplate elasticsearchRestTemplate
            , NativeSearchQuery nativeSearchQuery
            , Class<T> objectClass
            , String indexName){

        List<String> scrollIds = new ArrayList<>();
        try{
            // 设置每页数据量
            nativeSearchQuery.setMaxResults(10000);
            // 缓存第一页符合搜索条件的数据
            SearchScrollHits<T> searchScrollHits = elasticsearchRestTemplate.searchScrollStart(Constants.scrollTimeInMillis, nativeSearchQuery, objectClass, IndexCoordinates.of(indexName));
            String scrollId = searchScrollHits.getScrollId();
            //统计下耗时
            long start = System.currentTimeMillis();
            scrollIds.add(scrollId);
            if (searchScrollHits == null){
                log.info("searchScrollHits 为null");
                return new ArrayList<>();
            }
            List<T> resultList = new ArrayList<>();
            //将第一页的数据放入结果集
            for (SearchHit<T> demo : searchScrollHits.getSearchHits()){
                resultList.add(demo.getContent());
            }
            //判断searchScrollHits中是否有命中数据，如果为空，则表示已将符合查询条件的数据全部遍历完毕
            while (searchScrollHits.hasSearchHits()) {
                //4、根据上次搜索结果scroll_id进入下一页数据搜索,该方法执行后将重新刷新快照保留时间
                searchScrollHits = elasticsearchRestTemplate.searchScrollContinue(scrollId, Constants.scrollTimeInMillis, objectClass, IndexCoordinates.of(indexName));
                scrollId = searchScrollHits.getScrollId();
                scrollIds.add(scrollId);
                //将每一页的数据放入结果集
                for (SearchHit<T> demo : searchScrollHits.getSearchHits()){
                    resultList.add(demo.getContent());
                }
            }
            log.info("耗时：{} ms, 数据总数：{}",(System.currentTimeMillis() - start) ,resultList.size());
            return resultList;
        } finally {
            if (!scrollIds.isEmpty()){
                // 清除 scroll
                elasticsearchRestTemplate.searchScrollClear(scrollIds);
            }
        }
    }

}
