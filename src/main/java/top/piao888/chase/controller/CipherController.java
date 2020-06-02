package top.piao888.chase.controller;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.piao888.chase.utill.DESEncryptUtil;
import top.piao888.chase.utill.DESUtil;

@Controller
    @RequestMapping("/cipher")
public class CipherController {
    @RequestMapping("/cipher")
    public String cipher(){
        return "cipher.html";
    }

    /**
     * 解密
     * @param key
     * @param encrypt
     * @return
     */
    @RequestMapping("decode")
    @ResponseBody
    public String decode(@RequestParam("aseKey") String key, @RequestParam("encrypt")String encrypt){
        String con= DESEncryptUtil.decrypt(encrypt,key);
//        String con= DESUtil.decrypt(key,old);
        System.out.println(con);
        return con;
    }

    /**
     * 加密
     * @param key
     * @param encrypt
     * @return
     */
    @RequestMapping("des")
    @ResponseBody
    public String des(@RequestParam("aseKey") String key, @RequestParam("encrypt")String encrypt){
        String con= DESEncryptUtil.encrypt(encrypt,key);
//        String con= DESUtil.encrypt(key,old);
        System.out.println(con);
        return con;
    }

}
