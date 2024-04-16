package com.ruoyi.system.domain.entiy;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;

/**
 * 文件管理对象 my_file
 * 
 * @author ruoyi
 * @date 2024-02-01
 */
@Data
public class File extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件id */
    private Long id;

    /** 分身d */
    private Long separationId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    private String bsName;
    private Long bsId;

    private String fileUrl;

    /** 向量库状态  ‘Y’已同步  'N'未同步 */
    @Excel(name = "向量库状态  ‘Y’已同步  'N'未同步")
    private Integer vectorStatus;
    private String delFlag;
}
