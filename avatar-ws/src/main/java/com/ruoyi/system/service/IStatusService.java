package com.ruoyi.system.service;

import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.domain.entiy.Status;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IStatusService {

    int binding(Status status);

    int cancelBinding(Status status);

    Status getCurrnetStatus();
}
