package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.entiy.File;
import com.ruoyi.system.domain.entiy.Separation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 领域管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-01
 */
public interface SeparationMapper
{
    public List<Separation> selectSeparationList(@Param("separation") Separation separation);

    Separation selectSeparationById(@Param("separationId") Long separationId);

    int insertSeparation(Separation separation);

    int deleteSeparationById(@Param("separationId") Long separationId);

    int batchInsertAreaFiles(@Param("separationId") Long separationId, @Param("files") List<File> areaFiles);

    int updateSeparation(Separation separation);

    int updateSeparationAvatar(@Param("separationId") Long separationId, @Param("avatar") String avatar);

    File selectFileById(String id);

    int deleteFileById(String id);

    Separation selectSeparationByUserId(@Param("userId") Long userId);

    List<File> selectFiles(File f);

    void updateFileVectorStatusByBsId(File file);
}
