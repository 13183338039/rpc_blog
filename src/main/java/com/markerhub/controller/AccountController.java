package com.markerhub.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.Kafka.KafkaMessageProducer;
import com.markerhub.common.dto.LoginDto;
import com.markerhub.common.exception.CustomException;
import com.markerhub.common.lang.Result;
import com.markerhub.shiro.JwtToken;
import com.rpc.entity.User;
import com.rpc.example.UserService;
import com.markerhub.util.JwtUtils;
import com.rpc.example.annotation.RemoteReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@Slf4j
public class AccountController {

    @RemoteReference
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer1;

    @PostConstruct
    public void a1(){
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");
        System.out.println("111111111111111111111111");

    }

    @GetMapping("/un/test-error")
    public String testError() {
        throw new CustomException("模拟错误");
    }

    @GetMapping("/un/say2")
    public String say1() {
        String a=userService.test1("sb");
        System.out.println(a);
        return a;
    }

//    @RequiresRoles("admin")
    @GetMapping("/un/authorization1")
    public String test_authorizationinfo1(){
        System.out.println("is admin");
        return "is admin";
    }

    @RequiresRoles("user")
    @GetMapping("/un/authorization2")
    public String test_authorizationinfo2(){
        System.out.println("is user");
        return "is user";
    }

    @GetMapping("/hello")
    public String func1(){
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");
        //SecurityManager 是Shiro内部的底层实现，几乎所有功能都由其实现
        SecurityManager sm = factory.getInstance();
        //SecurityUtils是一个工具，方便用户调用，它封装了SecurityManager
        SecurityUtils.setSecurityManager(sm);
        //生成一个SecurityManager的门面类，即Subject。
        Subject subject = SecurityUtils.getSubject();
        //封装用户的数据
        UsernamePasswordToken token = new UsernamePasswordToken("jay", "123456");
        //Subject接收到的方法参数，最终将会传到SecurityManager中进行验证
        //将用户的数据token 最终传递到Realm中进行对比
        subject.login(token);
        return "hello world;";
    }

    @GetMapping("/doLogin")
    public String func(){
        return "doLogin";
    }

    @GetMapping("/un/doLogin1")
    public String func11(){
        kafkaMessageProducer1.sendAsync(
                "11111111111","22222222",
                (metadata, exception) -> callback("11111111111", metadata, exception));
        return "doLogin11";
    }
    private void callback(String s, RecordMetadata metadata, Exception ex) {
        if (metadata != null) {
            log.info("发送订单消息:" + s + " 偏移量: " + metadata.offset() + " 主题: " + metadata.topic());
        } else {
            log.error("发送订单消息失败: " + ex.getMessage(), ex);
        }
    }
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        System.out.println("login");
        //User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        User user = (User) userService.getAByUsername(loginDto.getUsername());
        Assert.notNull(user, "用户不存在");
        System.out.println(SecureUtil.md5(loginDto.getPassword()));
        if(!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }
        String jwt = JwtUtils.sign(loginDto.getUsername());
        //JwtToken jwt1=new JwtToken(jwt);
        //SecurityUtils.getSubject().login(jwt1);

        response.setHeader("Authorization", jwt);
        System.out.println("生成token:"+jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        kafkaMessageProducer1.sendAsync(
                user.getUsername(),user.getEmail(),
                (metadata, exception) -> callback(user.getUsername(), metadata, exception));
        userService.updateLoginByid(user.getId(), LocalDateTime.now());
        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }

    /**
     * 用户注册（与 frontend 表单一致：username, email, password, 可选 avatar）
     * 当前未接入 MinIO，头像使用默认 URL；若传入 avatar 文件则仅做校验后忽略
     */
    @PostMapping("/register")
    public Result register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile) {
        log.info("用户注册请求 - 用户名: {}, 邮箱: {}", username, email);

        if (!StringUtils.hasText(username)) {
            return Result.fail("用户名不能为空");
        }
        if (!StringUtils.hasText(email)) {
            return Result.fail("邮箱不能为空");
        }
        if (!StringUtils.hasText(password)) {
            return Result.fail("密码不能为空");
        }

        User existUser = userService.getAByUsername(username);
        if (existUser != null) {
            log.warn("用户注册失败 - 用户名: {}, 原因: 用户名已存在", username);
            return Result.fail("用户名已存在");
        }

        existUser = userService.getAByEmail(email);
        if (existUser != null) {
            log.warn("用户注册失败 - 邮箱: {}, 原因: 邮箱已被注册", email);
            return Result.fail("邮箱已被注册");
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String contentType = avatarFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.fail("头像必须是图片文件");
            }
            if (avatarFile.getSize() > 5 * 1024 * 1024) {
                return Result.fail("头像文件大小不能超过 5MB");
            }
        }

        String avatarUrl = "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg";

        User user = new User();
        user.setUsername(username);
        user.setPassword(SecureUtil.md5(password));
        user.setEmail(email);
        user.setStatus(0);
        user.setCreated(LocalDateTime.now());
        user.setAvatar(avatarUrl);

        user = userService.saveUser(user);
        log.info("用户注册成功 - 用户名: {}, 用户ID: {}", user.getUsername(), user.getId());

        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("email", user.getEmail())
                .put("avatar", user.getAvatar())
                .map()
        );
    }

    @RequiresAuthentication
    @RequiresRoles("user")
    @GetMapping("/logout")
    public Result logout() {
        System.out.println("logout");
        System.out.println(SecurityUtils.getSubject().getPrincipal().toString());
        SecurityUtils.getSubject().logout();
        System.out.println("logout-success");
        return Result.succ(null);
    }

}
