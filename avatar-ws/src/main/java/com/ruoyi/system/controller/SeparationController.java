package com.ruoyi.system.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.service.ISeparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/separation")
public class SeparationController extends BaseController {

    @Autowired
    private ISeparationService separationService;


    @RequestMapping("getSeparationList")
    public TableDataInfo getSeparationInfo(@RequestParam Separation separation){
        startPage();
        List<Separation> list = separationService.selectSeparationList(separation);
        return getDataTable(list);
    }

    @PostMapping("updateSparationupdateAvatar")
    public AjaxResult separationupdateAvatar(@RequestParam("separationId") Long separationId, @RequestParam("avatarImage") MultipartFile avatarImage) throws Exception{
        if (!avatarImage.isEmpty())
        {
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), avatarImage, MimeTypeUtils.IMAGE_EXTENSION);
            if (separationService.updateSeparationAvatar(separationId, avatar))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }

    @RequestMapping("getSeparationInfo")
    public AjaxResult getSeparationInfo(@RequestParam Long separationId){
        return success(separationService.getSeparationById(separationId));
    }

    @DeleteMapping("/delFiles")
    public AjaxResult delFiles(@RequestBody(required = true) Map map)
    {
        String[] ids = (String[]) map.get("ids");
        return toAjax(separationService.deleteFileByIds(ids));
    }

    @PostMapping("addSeparation")
    public AjaxResult addSeparation(@RequestBody Separation separation){
        return toAjax(separationService.addSeparation(separation));
    }

    @PostMapping("updateSeparation")
    public AjaxResult updateSeparation(@RequestBody Separation separation){
        return toAjax(separationService.updateSeparation(separation));
    }

    @PostMapping("delSeparation")
    public AjaxResult delSeparation(Long separationId){
        return toAjax(separationService.deleteSeparationById(separationId));
    }

    @PostMapping("switchSeparationAreaOrUrl")
    public AjaxResult switchSeparationAreaOrUrl(@RequestParam("separationId") Long separationId){
        return toAjax(separationService.switchSeparationAreaOrUrl(separationId));
    }
}
