package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.domain.entiy.File;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.mapper.SeparationMapper;
import com.ruoyi.system.service.ISeparationService;
import com.ruoyi.system.utils.BiShengUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeparationServiceImpl implements ISeparationService {

    @Autowired
    private SeparationMapper separationMapper;
    @Autowired
    private BiShengUtils biShengUtils;

    @Value("${bisheng.baseurl}")
    private String bishengBaseURL;
    @Value("${bisheng.api.createAreaurl}")
    private String createAreaURL;
    @Value("${bisheng.api.deleteApiKey}")
    private String deleteApiKeyURL;
    @Value("${bisheng.api.deleteFile}")
    private String deleteFileURL;
    @Value("${bisheng.templateId}")
    private String templateId;
    @Value("${bisheng.modelName}")
    private String modelName;
    @Value("${default.avatarurl}")
    private String defaultAvatarUrl;

    @Override
    public List<Separation> selectSeparationList(Separation separation) {
        List<Separation> separations = separationMapper.selectSeparationList(separation);
        Long userId = SecurityUtils.getUserId();
        List<Separation> filteredList = separations.stream()
                .filter(obj -> obj.getId() != userId)
                .collect(Collectors.toList());
        return filteredList;
    }

    @Override
    public Separation getSeparationById(Long separationId) {
        Separation separation = separationMapper.selectSeparationById(separationId);
        return separation;
    }

    @Override
    public int addSeparation(Separation separation) {
        Long userId = SecurityUtils.getUserId();
        separation.setUserId(userId);
        separation.setCreateTime(new Date());
        separation.setDelFlag("N");
        if(userId == 1){
            separation.setType("public");
        } else {
            separation.setType("private");
        }
        separation.setIsArea("Y");
        separation.setIsOpen("N");
        String bsName = IdUtils.fastSimpleUUID();
        separation.setBsName(bsName);
        separation.setAvatarUrl(defaultAvatarUrl);
        //毕昇创建
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", bsName);
        body.put("model", modelName);
        body.put("description", separation.getDescribe());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject dataJsonObject = (JSONObject) biShengUtils.sendRequest(bishengBaseURL + createAreaURL, "POST", body, headers);
        separation.setBsId(dataJsonObject.getLong("id") + "");
        //TODO 毕昇开启技能
        openGrapharea(separation);
        int rows = separationMapper.insertSeparation(separation);
        return rows;
    }

    @Override
    public int updateSeparation(Separation separation) {
        int i = separationMapper.updateSeparation(separation);
        return i;
    }

    @Transactional
    @Override
    public int deleteSeparationById(Long separationId) {
        Separation separation = separationMapper.selectSeparationById(separationId);
        int row = separationMapper.deleteSeparationById(separationId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        biShengUtils.sendRequest(bishengBaseURL + deleteApiKeyURL + separation.getBsFloweId(), "DELETE", null, headers);
        return row;
    }

    @Override
    public boolean updateSeparationAvatar(Long separationId, String avatar) {
        if(separationId == null){
            return separationMapper.updateSeparationAvatar(SecurityUtils.getUserId(), avatar) > 0;
        } else {
            return separationMapper.updateSeparationAvatar(separationId, avatar) > 0;
        }
    }

    @Override
    public int deleteFileByIds(String[] ids) {
        for(String id : ids){
            File file = separationMapper.selectFileById(id);
            separationMapper.deleteFileById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            biShengUtils.sendRequest(bishengBaseURL + deleteFileURL + file.getBsId(), "DELETE", null, headers);
        }
        return 1;
    }

    @Override
    public int switchSeparationAreaOrUrl(Long separationId) {
        Separation separationN = new Separation();
        if(separationId == null){
            separationN.setUserId(SecurityUtils.getUserId());
            Separation separation = separationMapper.selectSeparationByUserId(SecurityUtils.getUserId());
            if(separation.getIsArea().equals("Y")){
                separationN.setIsArea("N");
            } else {
                separationN.setIsArea("Y");
            }
            return separationMapper.updateSeparation(separation);
        } else {
            Separation separation = separationMapper.selectSeparationById(separationId);
            if(separation.getIsArea().equals("Y")){
                separationN.setIsArea("N");
            } else {
                separationN.setIsArea("Y");
            }
            return separationMapper.updateSeparation(separation);
        }
    }

    private void openGrapharea(Separation separation){
        HashMap<String, Object> mapBody = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONArray temBodyarr = (JSONArray) biShengUtils.sendRequest(bishengBaseURL + "/api/v1/skill/template?id=" + templateId, "GET", mapBody, headers);
        JSONObject temObj = temBodyarr.getJSONObject(0);
        String flow_id = temObj.getString("flow_id");
        JSONObject edgesAnodesAviewport = temObj.getJSONObject("data");
        mapBody.put("data", edgesAnodesAviewport);
        mapBody.put("flow_id", flow_id);
        mapBody.put("name", separation.getBsName());
        JSONObject flowBody = (JSONObject) biShengUtils.sendRequest(bishengBaseURL + "/api/v1/flows/", "POST", mapBody, headers);
        String floweId = flowBody.getString("id");
        mapBody.clear();
        JSONObject body = (JSONObject) biShengUtils.sendRequest(bishengBaseURL + "/api/v1/flows/" + floweId, "GET", null, headers);
        JSONObject data = body.getJSONObject("data");
        JSONArray nodes = data.getJSONArray("nodes");
        JSONObject cyr5w_collection_name = nodes.getJSONObject(2).getJSONObject("data").getJSONObject("node").getJSONObject("template").getJSONObject("collection_name");
        cyr5w_collection_name.put("collection_id",separation.getBsId());
        cyr5w_collection_name.put("value", separation.getBsName());
        JSONObject h31et9_index_name = nodes.getJSONObject(3).getJSONObject("data").getJSONObject("node").getJSONObject("template").getJSONObject("index_name");
        h31et9_index_name.put("collection_id",separation.getBsId());
        h31et9_index_name.put("value", separation.getBsName());
        mapBody.clear();
        mapBody.put("data", data);
        mapBody.put("name", separation.getBsName());
        biShengUtils.sendRequest(bishengBaseURL + "/api/v1/flows/" + floweId, "PATCH", mapBody, headers);
        separation.setBsFloweId(floweId);
    }
}
