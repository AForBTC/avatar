package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.entiy.Chatbox;
import com.ruoyi.system.domain.entiy.Chathistory;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Autowired
    private IChatService chatService;

    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody Chathistory chathistory) throws IOException {
        return success(chatService.chat(chathistory));
    }

    @GetMapping("/getHistoryByChatboxId")
    public AjaxResult getHistoryByChatboxId(){
//        Long chatboxId = (Long) map.get("chatboxId");
        return success(chatService.getHistoryByChatboxId(1l));
    }

    @GetMapping("/getChatboxs")
    public AjaxResult getChatboxs(){
        ArrayList<Chatbox> chatboxes = new ArrayList<>();
        Chatbox chatbox1 = new Chatbox();
        chatbox1.setChatboxName("会话一");
        chatbox1.setIsActivate("Y");
        chatboxes.add(chatbox1);
        return success(chatboxes);
    }

    @PostMapping("/delChatboxs")
    public AjaxResult delChatboxs(@RequestBody(required = true) Map map){
        Long id = (Long) map.get("id");
        return toAjax(chatService.delChatbox(id));
    }
}
