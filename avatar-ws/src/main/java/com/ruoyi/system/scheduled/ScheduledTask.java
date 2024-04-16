package com.ruoyi.system.scheduled;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.system.domain.entiy.Separation;
import com.ruoyi.system.mapper.SeparationMapper;
import com.ruoyi.system.service.ISeparationService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;


@Component
public class ScheduledTask {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private QueueWithConsumer queueWithConsumer;
    @Autowired
    private SeparationMapper separationMapper;
    @Autowired
    private ISeparationService separationService;

    public static String accessTokenCookie = "";
    public static String refreshTokenCookie = "";

    @Value("${bisheng.baseurl}")
    private String bishengBaseURL;
    @Value("${bisheng.api.loginurl}")
    private String loginurl;
    @Value("${bisheng.api.refresh}")
    private String refreshurl;

    @Value("${bisheng.userName}")
    private String userName;
    @Value("${bisheng.password}")
    private String password;
    @Value("${bisheng.captcha_key}")
    private String captcha_key;
    @Value("${bisheng.captcha}")
    private String captcha;

    @PostConstruct
    public void executeAtStartup() {
        loginbishegn();
        createUserSeparation();
        List<Separation> separations = separationMapper.selectSeparationList(new Separation());
        for (Separation separation : separations) {
            queueWithConsumer.synBSToMysql(Long.parseLong(separation.getBsId()));
        }
        Thread consumerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Long areaBsId = queueWithConsumer.consume();
                queueWithConsumer.synBSToMysql(areaBsId);
            }
        });
        consumerThread.start();
    }

    @Scheduled(fixedRate = 600000) // 300000毫秒 = 5分钟
    public void executeTask() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity<>(null, headers);
        restTemplate.exchange(bishengBaseURL + refreshurl, HttpMethod.GET, requestEntity, String.class);
    }

    private void loginbishegn() {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_name", userName);
        body.put("password", password);
        body.put("captcha", captcha);
        body.put("captcha_key", captcha_key);
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(bishengBaseURL + loginurl, HttpMethod.POST, requestEntity, String.class);
        List<String> setCookieHeaders = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
        // 输出Cookie信息
        if (setCookieHeaders != null) {
            for (String setCookieHeader : setCookieHeaders) {
                if (setCookieHeader.startsWith("access_token_cookie=")) {
                    accessTokenCookie = setCookieHeader.substring(20);
                } else if (setCookieHeader.startsWith("refresh_token_cookie=")) {
                    refreshTokenCookie = setCookieHeader.substring(21);
                } else {
                    throw new GlobalException("登录bisheng失败");
                }
            }
        } else {
            throw new GlobalException("登录bisheng失败");
        }
    }

    private void createUserSeparation() {
        List<SysUser> sysUsers = userService.selectUserList(new SysUser());
        Separation separation = new Separation();
        for(SysUser sysUser : sysUsers){
            separation.setUserId(sysUser.getUserId());
            List<Separation> separations = separationMapper.selectSeparationList(separation);
            if(sysUser.getUserId() != 1){
                if(separations == null || separations.size() == 0){
                    separationService.addSeparation(new Separation());
                }
            }
        }
    }
}