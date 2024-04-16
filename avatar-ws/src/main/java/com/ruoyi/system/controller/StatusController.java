package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.domain.entiy.Status;
import com.ruoyi.system.service.IStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class StatusController extends BaseController {

    @Autowired
    private IStatusService statusService;

    @GetMapping("/getCurrnetStatus")
    public AjaxResult getCurrnetStatus(){
        return success(statusService.getCurrnetStatus());
    }

    @PostMapping("/binding")
    public AjaxResult binding(@RequestBody Status status){
        return toAjax(statusService.binding(status));
    }

    @PostMapping("/cancelBinding")
    public AjaxResult cancelBinding(@RequestBody Status status){
        return toAjax(statusService.cancelBinding(status));
    }
}
