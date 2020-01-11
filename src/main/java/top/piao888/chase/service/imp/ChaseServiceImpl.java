package top.piao888.chase.service.imp;


import org.springframework.stereotype.Service;
import top.piao888.chase.vo.ContentText;
import top.piao888.chase.vo.Joke;
import top.piao888.chase.vo.Weather;

@Service
public class ChaseServiceImpl {
    public ContentText routine(Weather weather, Joke joke){
        ContentText contentText=new ContentText();
        contentText.setWeather("今天最高温度："+weather.getData()[0].getTem1()+"今天最低温度"+weather.getData()[0].getTem2());
        contentText.setDress(weather.getData()[0].getIndex()[3].getDesc());
        if(null!=joke){
            contentText.setJoke(joke.getResult().getList()[0].getContent());
        }else {
            contentText.setJoke("占位");
        }
        return contentText;
    }
}
