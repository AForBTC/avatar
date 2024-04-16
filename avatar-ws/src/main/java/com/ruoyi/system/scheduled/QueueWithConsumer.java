package com.ruoyi.system.scheduled;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.entiy.File;
import com.ruoyi.system.mapper.SeparationMapper;
import com.ruoyi.system.utils.BiShengUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class QueueWithConsumer {

    @Autowired
    private BiShengUtils biShengUtils;

    @Value("${bisheng.baseurl}")
    private String bishengBaseURL;
    @Value("${bisheng.api.filesByAreaId}")
    private String filesByAreaIdURL;
    @Value("${bisheng.api.deleteFile}")
    private String deleteFileURL;

    @Autowired
    private SeparationMapper separationMapper;

    private Queue<Long> queue = new LinkedList<>();

    public synchronized void produce(Long areaBsId) {
        queue.offer(areaBsId);
        notify(); // 唤醒可能正在等待的消费者线程
    }

    public synchronized Long consume() {
        while (queue.isEmpty()) {
            try {
                wait(); // 等待直到队列中有数据
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue.poll();
    }

    public void synBSToMysql(Long separationBsId){
        HashMap<Object, Object> body = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = (JSONObject) biShengUtils.sendRequest(bishengBaseURL + filesByAreaIdURL + separationBsId, "GET", body, headers);
        JSONArray data = jsonObject.getJSONArray("data");
        for (Object obj : data) {
            JSONObject areaBsObj = (JSONObject) obj;
            Long fileBsId = areaBsObj.getLong("id");
            Long status = areaBsObj.getLong("status");
            File f = new File();
            f.setBsId(fileBsId);
            List<File> files = separationMapper.selectFiles(f);
            if(files.size() == 0){
                biShengUtils.sendRequest(bishengBaseURL + deleteFileURL + fileBsId, "DELETE", null, headers);
            } else {
                if(status == 1){
                    produce(separationBsId);
                    return;
                } else if(status == 2){
                    File file = new File();
                    file.setBsId(fileBsId);
                    file.setVectorStatus(1);
                    separationMapper.updateFileVectorStatusByBsId(file);
                } else if(status == 3){
                    File file = new File();
                    file.setBsId(fileBsId);
                    file.setVectorStatus(0);
                    separationMapper.updateFileVectorStatusByBsId(file);
                }
            }
        }
    }
}
