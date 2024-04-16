package com.ruoyi.system.domain.entiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Chatbox {
    private Long id;
    private Integer type;
    private Long userId;
    private String sessionId;
    private Long separationId;
    private String chatboxName;
    private String isActivate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
