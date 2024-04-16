package com.ruoyi.system.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.system.scheduled.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class BiShengUtils {

    @Autowired
    private RestTemplate restTemplate;

    public Object sendRequest(String url, String type, Map body, HttpHeaders headers){
        headers.set("Cookie", "access_token_cookie=" + ScheduledTask.accessTokenCookie + ";refresh_token_cookie=" + ScheduledTask.refreshTokenCookie);
        HttpEntity requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = null;
        if(type.equals("POST")){
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        }else if(type.equals("DELETE")){
            responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
        } else if(type.equals("GET")){
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } else if(type.equals("PATCH")){
            responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
        }
        String res = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(res);
        Integer status_code = jsonObject.getInteger("status_code");
        if(status_code != 200){
            throw new GlobalException(jsonObject.getString("status_message"));
        } else {
            if(jsonObject.get("data") instanceof JSONObject){
                return jsonObject.getJSONObject("data");
            }else if(jsonObject.get("data") instanceof String){
                return jsonObject.getString("data");
            } else if(jsonObject.get("data") instanceof Long){
                return jsonObject.getLong("data");
            } else {
                return jsonObject.getJSONArray("data");
            }
        }
    }
}
