package com.markerhub.controller;
import com.rpc.example.IUserService;
import com.rpc.example.annotation.RemoteReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RpcController {
//    @RemoteReference
//    private IUserService userService;
//
//    @GetMapping("/un/say")
//    public String say() {
//        System.out.println("say");
//        return userService.saveUser("cc11");
//    }

    @GetMapping("/un/say1")
    public String say1() {
        System.out.println("say1");
        return ("cc---11");
    }
}
