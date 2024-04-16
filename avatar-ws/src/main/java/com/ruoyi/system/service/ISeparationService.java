package com.ruoyi.system.service;

import com.ruoyi.system.domain.entiy.Separation;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISeparationService {

    List<Separation> selectSeparationList(Separation separation);

    Separation getSeparationById(Long separationId);

    int addSeparation(Separation separation);

    int updateSeparation(Separation separation);

    int deleteSeparationById(Long separationId);

    boolean updateSeparationAvatar(Long separationId, String avatar);

    int deleteFileByIds(String[] ids);

    int switchSeparationAreaOrUrl(Long separationId);
}
