package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.entiy.File;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.domain.entiy.Status;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-01
 */
public interface StatusMapper
{

    Status getCurrnetStatus(Long userId);

    int binding(Long separationId, Long userId, String isActivate);


    void updateSeparationActivate(Long statusId, Long separationId, String isActivate);

    void updateTypeActivate(Long userId,int i);

    void deleteSeparation(Long statusId, Long separationId);

    void updateChatboxActivate(Long chatboxId);
}
