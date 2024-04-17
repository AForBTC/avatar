package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.entiy.Chatbox;
import com.ruoyi.system.domain.entiy.Chathistory;
import com.ruoyi.system.domain.entiy.Status;
import com.ruoyi.system.mapper.ChatMapper;
import com.ruoyi.system.mapper.StatusMapper;
import com.ruoyi.system.service.IChatService;
import com.ruoyi.system.service.ISeparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ISeparationService separationService;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private StatusMapper statusMapper;

    @Value("${bisheng.api.modelChaturl}")
    private String modelChaturl;
    @Value("${bisheng.api.areaChaturl}")
    private String areaChaturl;
    @Value("${bisheng.chatModelFlowId}")
    private String chatModelFlowId;

    @Override
    public int chat(Chathistory chathistory) {
        if(chathistory.getChatboxId() == null){
            if(chathistory.getType() == 0){

            } else if( chathistory.getType() == 1){

            } else if( chathistory.getType() == 2){

            } else {
                throw  new GlobalException("请求参数不正确, 无法识别问答类型");
            }
        } else {

        }
        return 0;
    }

    @Override
    public List<Chatbox> getChatBoxs(Chatbox chatbox) {
        chatbox.setUserId(SecurityUtils.getUserId());
        List<Chatbox> chatboxList = chatMapper.selectChatboxs(chatbox);
        Status currnetStatus = statusMapper.getCurrnetStatus(SecurityUtils.getUserId());
        Integer type = chatbox.getType();
        if(type == 0){
            statusMapper.updateTypeActivate(currnetStatus.getId(), 0);
        } else if(type == 1){
            statusMapper.updateTypeActivate(currnetStatus.getId(), 1);
        } else {
            statusMapper.updateTypeActivate(currnetStatus.getId(), 2);
            statusMapper.updateSeparationActivate(currnetStatus.getId(), chatbox.getSeparationId(), "Y");
        }
        return chatboxList;
    }

    @Override
    public List<Chathistory> getHistoryByChatboxId(Long chatboxId) {
        statusMapper.updateChatboxActivate(chatboxId);
        return chatMapper.getHistoryByChatboxId(chatboxId);
    }

    @Override
    public int delChatbox(Long id) {
        return chatMapper.deleteChatboxById(id);
    }
}
