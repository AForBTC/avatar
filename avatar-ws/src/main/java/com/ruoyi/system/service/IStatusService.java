package com.ruoyi.system.service;

import com.ruoyi.system.domain.entiy.Status;


public interface IStatusService {

    int binding(Status status);

    int cancelBinding(Status status);

    Status getCurrnetStatus();
}
