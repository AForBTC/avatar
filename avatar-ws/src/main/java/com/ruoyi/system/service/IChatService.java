package com.ruoyi.system.service;

import com.ruoyi.system.domain.entiy.Chatbox;
import com.ruoyi.system.domain.entiy.Chathistory;
import com.ruoyi.system.domain.entiy.Separation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IChatService {

    int chat(Chathistory chathistory);

    List<Chatbox> getChatBoxs(Chatbox chatbox);

    List<Chathistory> getHistoryByChatboxId(Long chatboxId);

    int delChatbox(Long id);
}
