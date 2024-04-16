package com.ruoyi.system.domain.entiy;

import lombok.Data;

import java.util.List;

@Data
public class Status {
    private Long id;
    private Long userId;
    private Long separationId;
    private Integer type;    // 0大模型 1私有领域 2分身

    private List<Separation> separationList;
    private Chatbox chatbox;
}
