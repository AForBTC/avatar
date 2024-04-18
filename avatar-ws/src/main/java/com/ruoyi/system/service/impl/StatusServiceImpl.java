package com.ruoyi.system.service.impl;


import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.domain.entiy.Status;
import com.ruoyi.system.mapper.StatusMapper;
import com.ruoyi.system.service.IStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class StatusServiceImpl implements IStatusService {

    @Autowired
    private StatusMapper statusMapper;


    @Transactional
    @Override
    public Status getCurrnetStatus() {
        Status currnetStatus = statusMapper.getCurrnetStatus(110l);
        if(currnetStatus == null){
            Status status = new Status();
            status.setUserId(110l);
            status.setType(0);
            statusMapper.insertStatus(status);
        }
        Status status = statusMapper.getCurrnetStatus(110l);
        return status;
    }

    @Override
    public int binding(Status status) {
        Status currnetStatus = statusMapper.getCurrnetStatus(110l);
        List<Separation> separations = currnetStatus.getSeparationList();
        // 使用流查找 id 为 1 的对象
        Optional<Separation> separation = separations.stream()
                .filter(obj -> obj.getIsActivate().equals("Y"))
                .findFirst();
        // 输出结果
        if (separation.isPresent()) {
            Separation obj = separation.get();
            statusMapper.updateSeparationActivate(currnetStatus.getId(), obj.getId(), "N");
        }
        if(status.getType() == 0){
            statusMapper.updateTypeActivate(currnetStatus.getId(), 0);
        } else if(status.getType() == 1){
            statusMapper.updateTypeActivate(currnetStatus.getId(), 1);
        } else {
            Optional<Separation> separationC = separations.stream()
                    .filter(obj -> obj.getId().equals(status.getSeparationId()))
                    .findFirst();
            if (!separationC.isPresent()) {
                statusMapper.updateTypeActivate(currnetStatus.getId(), 2);
                statusMapper.binding(currnetStatus.getId(), status.getSeparationId(), "Y");
                return 1;
            }
            statusMapper.updateTypeActivate(currnetStatus.getId(), 2);
            statusMapper.updateSeparationActivate(currnetStatus.getId(), status.getSeparationId(), "Y");
        }
        return 1;
    }

    @Override
    public int cancelBinding(Status status) {
        Status currnetStatus = statusMapper.getCurrnetStatus(SecurityUtils.getUserId());
        List<Separation> separationList = currnetStatus.getSeparationList();
        Optional<Separation> separation = separationList.stream()
                .filter(obj -> obj.getId() == status.getId())
                .findFirst();
        Separation obj = separation.get();
        Long separationId = currnetStatus.getSeparationId();
        if(obj.getIsActivate().equals("Y")){
            statusMapper.updateTypeActivate(currnetStatus.getId(), 0);
            statusMapper.deleteSeparation(currnetStatus.getId(), separationId);
        } else {
            statusMapper.deleteSeparation(currnetStatus.getId(), separationId);
        }
        return 1;
    }
}
