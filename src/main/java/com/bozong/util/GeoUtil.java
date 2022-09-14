package com.bozong.util;

import com.alibaba.fastjson.JSONObject;
import com.bozong.config.GeoConfig;
import com.bozong.model.entity.ReturnLocationBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author yinguoyu
 * @date 2022/9/14
 * @des 
**/
@Slf4j
@Component
public class GeoUtil {

    @Autowired
    private GeoConfig geoConfig;

    /**
     * 根据地址获取经纬度信息
     * @param address
     * @return
     */
    public ReturnLocationBean addressToLngLat(String address) {

        if (!StringUtils.hasLength(address)) {
            log.info("address is null");
            return null;
        } else {
            String url = geoConfig.getAddress_to_logintudea_url() + "&ak=" + geoConfig.getAk() + "&address=" + address;
            // 创建默认http连接
            HttpClient client = HttpClients.createDefault();

            //创建一个post请求
            HttpPost post = new HttpPost(url);

            try {
                // 用http连接去执行get请求并且获得http响应
                HttpResponse response = client.execute(post);

                // 从response中取到响实体
                HttpEntity entity = response.getEntity();

                // 把响应实体转成文本
                String text = EntityUtils.toString(entity);

                //转Json实体
                JSONObject jsonObject = JSONObject.parseObject(text);

                //地理信息实体封装
                ReturnLocationBean locationBean = new ReturnLocationBean();
                locationBean.setLng(jsonObject.getJSONObject("result").getJSONObject("location").getDouble("lng"));
                locationBean.setLat(jsonObject.getJSONObject("result").getJSONObject("location").getDouble("lat"));
                locationBean.setLevel(jsonObject.getJSONObject("result").getString("level"));
                locationBean.setFormattedAddress(address);

                return locationBean;
            } catch (Exception e) {
                return null;
            }
        }
    }

}