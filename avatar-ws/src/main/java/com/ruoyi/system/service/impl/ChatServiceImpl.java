package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.entiy.Chatbox;
import com.ruoyi.system.domain.entiy.Chathistory;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.domain.entiy.Status;
import com.ruoyi.system.mapper.ChatMapper;
import com.ruoyi.system.mapper.StatusMapper;
import com.ruoyi.system.service.IChatService;
import com.ruoyi.system.service.ISeparationService;
import com.ruoyi.system.service.IStatusService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ISeparationService separationService;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private IStatusService statusService;
    @Autowired
    private StatusMapper statusMapper;

    @Value("${bisheng.api.modelChaturl}")
    private String modelChaturl;
    @Value("${bisheng.api.areaChaturl}")
    private String areaChaturl;
    @Value("${bisheng.chatModelFlowId}")
    private String chatModelFlowId;

    @Override
    public Chathistory chat(Chathistory chathistory1) throws IOException {
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1; // 生成1到3之间的随机数
        try {
            Thread.sleep(randomNumber * 1000); // 将随机数乘以1000转换为毫秒，并使当前线程睡眠
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String text = chathistory1.getText();
        Chathistory chathistory = new Chathistory();

        //bomc工单生成：
        if(text.equals("帮我生成防火墙策略工单")){
            chathistory.setText("请对该网络策略进行描述");
            chathistory.setChatType(0);
        }else if(text.equals("aiops平台访问mysql服务")){
            chathistory.setText("请指定源ip和端口号、目标ip和端口号信息以及到期时间");
            chathistory.setChatType(0);
        }else if(text.equals("134.84.148.14到服务器134.84.148.15")){
            chathistory.setText("请指定端口号信息和到期时间");
            chathistory.setChatType(0);
        }else if(text.equals("3306端口，永久")){
            chathistory.setBeginIP("134.84.148.14");
            chathistory.setEndIP("134.84.148.15");
            chathistory.setPort("3306");
            chathistory.setChatType(1);
        }else if(text == null || text.equals("")){
            chathistory.setText("工单已进入审批阶段，工单编号：SD-090-231012-65138-1，请登录您的bomc平台查看详细信息");
            chathistory.setChatType(0);
        }

        //虚拟机系统告警查询：
        if(text.equals("查询所有虚拟机系统")){
            chathistory.setText("以下是所有操作系统信息：\n" +
                    "\t\t虚拟机名称：c6h493 ip：134.80.162.242\n" +
                    "\t\t虚拟机名称：c6h494 ip：134.80.162.243\n" +
                    "\t\t虚拟机名称：c6a121 ip：134.84.84.2\n" +
                    "\t\t虚拟机名称：c6a122 ip：134.84.84.3");
            chathistory.setChatType(0);
        }else if(text.equals("帮我查询虚拟机c6h493的告警信息")){
            chathistory.setText("为了帮你查询到准确的信息，请指定时间范围");
            chathistory.setChatType(0);
        }else if(text.equals("昨天")){
            //TODO EXCEL
            Path path = Paths.get("/generated.xlsx");
            byte[] fileBytes = null;
            try {
                // 读取文件的所有字节
                fileBytes = Files.readAllBytes(path);

                // 打印文件字节数组的长度
                // 可以在这里对文件字节数组进行进一步的处理，比如输出到控制台或者写入另一个文件等
            } catch (IOException e) {
                // 处理文件读取错误
                e.printStackTrace();
            }
            String s = convertExcelToHtml(fileBytes);
            chathistory.setText(s);
            chathistory.setChatType(0);
        }

        //营销话
        if(text.startsWith("产品")){
            chathistory.setText("尊敬的客户，您好！我是中国移动的营销客服，很高兴为您服务。您咨询的是云手机4C8G版，每月返还5元消费券，返24个月，到期后不再返还消费券的套餐。这款套餐的预存金额为0元，赠送金币或者消费券合计为120元，协议期为24个月。产品权益包括云手机(CPU4核，内存8G+64G）和移动云盘空间包（个人1T+家庭1T）。此外，您还可以使用消费券兑换铂金会员权益年包、流量年包（120GB）、移动云盘白银会员年包、7天云储存年包、5G视频通话年包、移动高清铂金会员年包（5选1）、咪咕视频钻石会员两年包、室外摄像头安装调测服务、爱家健康年包（尊享版）和其他等价组合档次。每个档次任选其一，您可以根据自己的需求选择合适的档次。如果您有任何疑问，欢迎随时咨询我。");
            chathistory.setChatType(0);
        }

        //运维申告
        if(text.startsWith("如何判断")){
            chathistory.setText("要判断用户是否拥有4G服务，可以按以下步骤进行：\n" +
                    "\t1.查询用户是否符合4G服务的开通条件：\n" +
                    "\t\t是否有超低价套餐资费（无）\n" +
                    "\t\t是否是USIM卡（有）\n" +
                    "\t\t是否有关闭GPRS服务（无）\n" +
                    "\t\t是否已经存在4G功能\n" +
                    "\t2.使用相应的数据库查询语句进行检查，例如：\n" +
                    "\t\t-- 是否有超低价套餐资费（无）\n" +
                    "\t\tselect * from tbcs.subs_rateplan \n" +
                    "\t\twhere region = 539 and subsid = 5398084765456 \n" +
                    "\t\tand rateplan in (select dictid from tbcs.dict_item where groupid = '394GrltpMtxNew') \n" +
                    "\t\tand nvl(enddate, sysdate + 1) >= sysdate \n" +
                    "\t\tand nvl(enddate, startdate + 1) >= startdate\n" +
                    "\n" +
                    "\t\t-- 是否是USIM卡（有）\n" +
                    "\t\tselect * from tbcs.dict_item \n" +
                    "\t\twhere groupid = '394GUSIMCardType' \n" +
                    "\t\tand dictid in (select res_type_id \n" +
                    "\t\t\t\t\t   from im_inv_IMSI_use@lnk_zy1a \n" +
                    "\t\t\t\t\t   where inv_id in (select imsi from TBCS.V_HW_SUBSCRIBER where subsid = 5398084765456))\n" +
                    "\n" +
                    "\t\t-- 是否有关闭GPRS服务（无）\n" +
                    "\t\tselect * from tbcs.subs_product \n" +
                    "\t\twhere region = 539 and subsid = 5398084765456 \n" +
                    "\t\tand prodid in (select dictid \n" +
                    "\t\t\t\t\t   from tbcs.dict_item \n" +
                    "\t\t\t\t\t   where groupid = 'MutexProdIDFor4GAutoProc') \n" +
                    "\t\tand nvl(enddate, sysdate + 1) >= sysdate \n" +
                    "\t\tand nvl(enddate, startdate + 1) >= startdate\n" +
                    "\n" +
                    "\t\t-- 是否已经存在4G功能\n" +
                    "\t\tselect * from tbcs.subs_product \n" +
                    "\t\twhere region = 539 and subsid = 5398084765456 \n" +
                    "\t\tand prodid = 'LTE_4G' \n" +
                    "\t\tand nvl(enddate, sysdate + 1) >= sysdate \n" +
                    "\t\tand nvl(enddate, startdate + 1) >= startdate");
            chathistory.setChatType(0);
        }
        if(text.startsWith("积分相关表字段及其含义如下")){
            chathistory.setText("积分相关表字段及其含义如下：\n" +
                    "\t\t1..am_score_balance（积分余额表）：\n" +
                    "\t\t\tscorecycle: 用户的账期\n" +
                    "\t\t\tentityid: 用户的标识\n" +
                    "\t\t\tscoretypeid: 积分类型\n" +
                    "\t\t\tBalance: 用户本月的积分余额\n" +
                    "\t\t\textendbalance: 用户上月的积分余额\n" +
                    "\t\t\tinamt: 用户本月的积分流入\n" +
                    "\t\t\toutamt: 用户本月的积分流出\n" +
                    "\t\t2.account.am_score_flow_log（积分流动表）：\n" +
                    "\t\t\trecid: 积分流动流水号\n" +
                    "\t\t\tscorecycle: 用户的账期\n" +
                    "\t\t\tscoretypeid: 积分类型\n" +
                    "\t\t\tentityid: 用户的标识\n" +
                    "\t\t\tscoreamt: 用户的积分流动值\n" +
                    "\t\t3.account.subsscore_pre_process（用户预处理表）：\n" +
                    "\t\t\tstatday: 用户的销账日期\n" +
                    "\t\t\tbillcycle: 用户的账期\n" +
                    "\t\t\tflag: 用户积分处理结果\n" +
                    "\t\t4.account.am_score_reception（积分日志表）：\n" +
                    "\t\t\trecid: 用户的积分流水号");
            chathistory.setChatType(0);
        }

        chathistory.setType(1);
        chathistory.setCreateTime(new Date());

        return chathistory;
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
//        statusMapper.updateChatboxActivate(chatboxId);
        Status currnetStatus = statusMapper.getCurrnetStatus(110l);
        List<Separation> separationList = currnetStatus.getSeparationList();
        Optional<Separation> separation = separationList.stream()
                .filter(obj -> obj.getIsActivate().equals("Y"))
                .findFirst();
        String firstText = "您好,当前数字分身为：" + separation.get().getSeparationName() + ",欢迎问答";
        ArrayList<Chathistory> chathistories = new ArrayList<>();
        Chathistory chathistory = new Chathistory();
        chathistory.setText(firstText);
        chathistory.setType(1);
        chathistory.setChatType(0);
        chathistory.setCreateTime(new Date());
        chathistories.add(chathistory);
        return chathistories;
    }

    @Override
    public int delChatbox(Long id) {
        return chatMapper.deleteChatboxById(id);
    }

    public String convertExcelToHtml(byte[] excelBytes) throws IOException {
        InputStream excelInputStream = new ByteArrayInputStream(excelBytes);
        Workbook workbook = null;

        if (excelInputStream.markSupported()) {
            workbook = WorkbookFactory.create(excelInputStream);
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(excelBytes);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            workbook = WorkbookFactory.create(byteArrayInputStream);
        }

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head></head><body><table>");

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (Row row : sheet) {
                htmlBuilder.append("<tr>");
                for (Cell cell : row) {
                    htmlBuilder.append("<td>");
                    switch (cell.getCellType()) {
                        case STRING:
                            htmlBuilder.append(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            htmlBuilder.append(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            htmlBuilder.append(cell.getBooleanCellValue());
                            break;
                        default:
                            htmlBuilder.append("");
                            break;
                    }
                    htmlBuilder.append("</td>");
                }
                htmlBuilder.append("</tr>");
            }
        }
        return htmlBuilder.toString();
    }
}
