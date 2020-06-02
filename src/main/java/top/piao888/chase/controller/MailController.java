package top.piao888.chase.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.piao888.chase.service.imp.ChaseServiceImpl;
import top.piao888.chase.service.imp.HttpClient;
import top.piao888.chase.service.imp.IMailServiceImpl;
import top.piao888.chase.vo.ContentText;
import top.piao888.chase.vo.Date;
import top.piao888.chase.vo.Joke;
import top.piao888.chase.vo.Weather;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/Mail")
public class MailController {

    private static final String SUCC_MAIL = "邮件发送成功！";
    private static final String FAIL_MAIL = "邮件发送失败！";

    // 图片路径
    private static final String IMG_PATH = "C:/Users/zjj/Desktop/github/materials/blog/img/WC-GZH.jpg";
    // 发送对象
    private static final String MAIL_TO = "419117883@qq.com";
//    private static final String MAIL_TO = "847118663@qq.com";
    //日志监控
    private static final String GOVERN = "847118663@qq.com";

    @Autowired
    private IMailServiceImpl mailService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ChaseServiceImpl chaseService;
    @RequestMapping("/encrypt")
    public void encrypt(@RequestParam("aseKey") String key, @RequestParam("encrypt")String encrypt){
        try {
            mailService.sendSimpleMail(GOVERN,"小妖精猜对了","key:"+key+" content:"+encrypt);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @RequestMapping("/decode")
    public void decode(@RequestParam("aseKey") String key, @RequestParam("encrypt")String encrypt){
        try {
            mailService.sendSimpleMail(GOVERN,"小妖精开始加密了","key:"+key+" content:"+encrypt);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @RequestMapping("/htmlEmail")
//    @Scheduled(cron = "0/4 * * * * ?")
    public String htmlEmail(){
        try {
            mailService.sendHtmlMail(MAIL_TO,"这是一HTML的邮件","<body>\n" +
                    "<div id=\"welcome\">\n" +
                    "    <h3>Welcome To My Friend!</h3>\n" +
                    "\n" +
                    "    GitHub：\n" +
                    "        <a href=\"#\" th:href=\"@{${github_url}}\" target=\"_bank\">\n" +
                    "            <strong>GitHub</strong>\n" +
                    "        </a>\n" +
                    "    <br />\n" +
                    "    <br />\n" +
                    "    个人博客：\n" +
                    "        <a href=\"#\" th:href=\"@{${blog_url}}\" target=\"_bank\">\n" +
                    "            <strong> </strong>\n" +
                    "        </a>\n" +
                    "    <br />\n" +
                    "    <br />\n" +
                    "    <img width=\"258px\" height=\"258px\"\n" +
                    "         src=\"url\">\n" +
                    "    <br />啥也不是\n" +
                    "</div>\n" +
                    "</body>");
        }catch (Exception ex){
            ex.printStackTrace();
            return FAIL_MAIL;
        }
        return SUCC_MAIL;
    }

    @RequestMapping("/attachmentsMail")
    public String attachmentsMail(){
        try {
            mailService.sendAttachmentsMail(MAIL_TO, "这是一封带附件的邮件", "邮件中有附件，请注意查收！", IMG_PATH);
        }catch (Exception ex){
            ex.printStackTrace();
            return FAIL_MAIL;
        }
        return SUCC_MAIL;
    }

    @RequestMapping("/resourceMail")
    public String resourceMail(){
        try {
            String rscId = "DoubleFJ";
            String content = "<html><body>这是有图片的邮件<br/><img src=\'cid:" + rscId + "\' ></body></html>";
            mailService.sendResourceMail(MAIL_TO, "这邮件中含有图片", content, IMG_PATH, rscId);

        }catch (Exception ex){
            ex.printStackTrace();
            return FAIL_MAIL;
        }
        return SUCC_MAIL;
    }

    @RequestMapping("/templateMail")
    @Scheduled(cron = "0 0 7 * * ?")
//    @Scheduled(cron = "0,4 * * * * ?")
//    public String templateMail(@RequestParam(value = "DES",required = false)String DES){
    public String templateMail(){
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        params.add("appid","64329795");
        params.add("appsecret","px3RMfYQ");
        params.add("version","v6");
        params.add("cityid","101010100");
        params.add("city","北京");
       String weatherResult= httpClient.client("https://www.tianqiapi.com/api/?appid=64329795&appsecret=px3RMfYQ&version=v1&cityid=101010100&city=北京",HttpMethod.GET,null);
        System.out.println(weatherResult);
        Weather weather = JSONArray.parseObject(weatherResult, Weather.class);
        System.out.println(weather.getData()[0].getTem1());
        System.out.println(weather.getData()[0].getIndex()[3].getDesc());

       String jokeResult= httpClient.client("https://api.jisuapi.com/xiaohua/text?pagenum=2&pagesize=1&sort=rand&appkey=25818990b8df176d",HttpMethod.GET,null);
        System.out.println(jokeResult);
        Joke joke = JSONArray.parseObject(jokeResult, Joke.class);
        System.out.println(joke.getResult().getList()[0].getContent());
        ContentText contentText=chaseService.routine(weather,joke);
//        ContentText contentText=chaseService.routine(weather,null);
        try {
            Context context = new Context();
            context.setVariable("github_url", "https://github.com/Folgerjun");
//            context.setVariable("weather", contentText.getWeather());
//            context.setVariable("dress", contentText.getDress());
//            context.setVariable("joke",contentText.getJoke());
            context.setVariable("DES","111W+cyXdnWqpb7dh6Dvag==");
            context.setVariable("secret","http://47.104.92.214:8888/cipher/cipher");
            String emailContent = templateEngine.process("mailTemplate", context);
            mailService.sendHtmlMail(MAIL_TO, "早！", emailContent);
            LocalDateTime localDateTime1 = LocalDateTime.now();
            mailService.sendSimpleMail(GOVERN,"发送成功",localDateTime1.toString());
        }catch (Exception ex){
            ex.printStackTrace();
            return FAIL_MAIL;
        }
        return SUCC_MAIL;
    }
}