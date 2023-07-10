package com.example.weather_api_call;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Util {
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    public String getNowDay(){
        Calendar cal = Calendar.getInstance();
        String getDay = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateTime = new SimpleDateFormat("HHmm");
        int dTime = Integer.parseInt(dateTime.format(date));
        if(0 < dTime && dTime < 210){
            cal.add(Calendar.DATE, -1);
            getDay = dateFormat.format(cal.getTime());
        }else{
            getDay = dateFormat.format(date);
        }
        return getDay;
    }
    public String getFcstTime(){
        Calendar nCal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        nCal.add(Calendar.HOUR_OF_DAY,1);
        String NowTime = dateFormat.format(nCal.getTime());
        return NowTime;
    }


    public String getbaseTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        int nTime = Integer.parseInt(dateFormat.format(date));
        String getTime = "";
        if(210 < nTime && nTime < 500){
         getTime = "0200";
        }else if(510 < nTime && nTime < 800){
            getTime = "0500";
        }else if(810 < nTime && nTime < 1100){
            getTime = "0800";
        }else if(1110 < nTime && nTime < 1400){
            getTime = "1100";
        }else if(1410 < nTime && nTime < 1700){
            getTime = "1400";
        }else if(1710 < nTime && nTime < 2000){
            getTime = "1700";
        }else if(2010 < nTime && nTime < 2300){
            getTime = "2000";
        }else if(2310 < nTime && nTime < 2400){
            getTime = "2300";
        }else if(0 < nTime && nTime <210){
            getTime = "2300";
        }
        return getTime;
    }

    public String getSkyState(String state){
        String resultSky = "";
        if(state.equals("1")){
            resultSky = "맑음";
        }else if(state.equals("3")){
            resultSky = "구름많음";
        }else if(state.equals("4")){
            resultSky = "흐림";
        }else{
            resultSky = "불분명";
        }
        return resultSky;
    }

    public String getWSDstate(String WsdState){
        int n = Integer.parseInt(WsdState);
        String resultWsd = "";
        if(n < 4){
            resultWsd = "약한바람";
        }else if(n >= 4 && n <9){
            resultWsd = "약간 강한바람";
        }else if(n >= 9 && n <14){
            resultWsd = "강한바람";
        }else if(n >= 14){
            resultWsd = "매우강한 바람";
        }
        return resultWsd;
    }

    public String getClothes(String rsTmp){
        int rs = Integer.parseInt(rsTmp);
        String rsClothes = "";
        if(rs >= 28){
            rsClothes = "민소매, 반발, 반바지, 짧은 치마, 린넨 옷";
        }else if(rs >= 23 && rs<= 27){
            rsClothes = "반팔, 얅은 셔트, 반바지, 면바지";
        }else if(rs >= 20 && rs <= 22){
            rsClothes = "블라우스, 긴팔 티, 면바지, 슬랙스";
        }else if(rs >= 17 && rs <= 19){
            rsClothes = "얇은 가디건, 니트, 맨투맨, 후드, 긴바지";
        }else if(rs >= 12 && rs <= 16){
            rsClothes = "자켓, 가디건, 청자켓, 니트, 스타킹, 청바지";
        }else if(rs >= 9 && rs <= 11){
            rsClothes = "트렌치 코트, 야상, 점퍼, 스타킹, 기모바지";
        }else if(rs >= 5 && rs <= 8){
            rsClothes = "울 코트, 가죽 옷, 기모";
        }else if(rs <= 4){
            rsClothes = "패딩, 두꺼운 코트, 누빔 옷, 기모, 목도리";
        }

        return rsClothes;
    }
}
