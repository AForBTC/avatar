package com.ruoyi.system.domain.entiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Chathistory {
    private Long id;
    private Integer type;  //0大模型 1私有领域 2分身
    private String text;
    private Integer chatType;  //0文本和表格 1卡片工单
    private Long chatboxId;
    private String question;
    private String answer;
    private String effect;
    private String beginIP;
    private String endIP;
    private String port;
    private String imgUrl;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
