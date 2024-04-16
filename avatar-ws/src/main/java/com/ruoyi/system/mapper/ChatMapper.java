package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.entiy.Chatbox;
import com.ruoyi.system.domain.entiy.Chathistory;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.domain.entiy.Status;

import java.util.List;

/**
 * 文件管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-01
 */
public interface ChatMapper
{

    List<Chatbox> selectChatboxs(Chatbox chatbox);

    int deleteChatboxById(Long id);

    List<Chathistory> getHistoryByChatboxId(Long chatboxId);
}
