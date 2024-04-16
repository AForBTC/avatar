package com.ruoyi.system.domain.entiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;

import java.util.Date;

@Data
public class Separation {
    private Long id;
    private String avatarUrl;
    private String bsId;
    private Long userId;
    private String type;
    private String isArea;
    private String separationName;
    private String bsName;
    private String bsFloweId;
    private String describe;
    private String url;
    private String isOpen;
    private String delFlag;
    private SysUser sysUser;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String isActivate;
    private SysUser user;
}
