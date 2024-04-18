package com.ruoyi.system.service;

import com.ruoyi.system.domain.entiy.Chatbox;
import com.ruoyi.system.domain.entiy.Chathistory;
import com.ruoyi.system.domain.entiy.Separation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface IChatService {

    Chathistory chat(Chathistory chathistory) throws IOException;

    List<Chatbox> getChatBoxs(Chatbox chatbox);

    List<Chathistory> getHistoryByChatboxId(Long chatboxId);

    int delChatbox(Long id);
}
