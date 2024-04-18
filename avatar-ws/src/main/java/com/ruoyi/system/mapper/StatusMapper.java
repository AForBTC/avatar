package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.entiy.Status;
import org.apache.ibatis.annotations.Param;

/**
 * 文件管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-01
 */
public interface StatusMapper
{

    Status getCurrnetStatus(Long userId);

    int binding(@Param("statusId")Long statusId, @Param("separationId")Long separationId, @Param("isActivate")String isActivate);


    void updateSeparationActivate(@Param("statusId")Long statusId, @Param("separationId")Long separationId, @Param("isActivate")String isActivate);

    void updateTypeActivate(@Param("statusId") Long statusId, @Param("i") int i);

    void deleteSeparation(Long statusId, Long separationId);

    void updateChatboxActivate(Long chatboxId);

    void insertStatus(Status status);
}
