package com.petroleumsoft.stimopti.algos;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Navin
 */
public class TimeAddition {
    
    public static String timeAdd(String initialTime, String duration){
        String time = initialTime;
        String timeAnu = "00:00:00";
//        System.out.println(duration);
        long ttime = 0;
        if(duration.contains(".")){
        int i = duration.indexOf(".");
        String min1 = duration.substring(0, i);
        String sec = duration.substring(i+1);
//        System.out.println(min1+" "+sec);
        long sec1 = (Long.parseLong(min1))*60;
        long sec2 = Long.parseLong(sec)*(60/10);
        ttime = sec1+sec2;
        }
        else{
            ttime = Long.parseLong(duration)*60;
        }
        long diffti = dateconversoion(timeAnu, time);
        diffti+=ttime;
        return convertTime(diffti);
        
    }

    public static long dateconversoion(String date1, String date2) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        long diff = 0L;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
            diff = d2.getTime() - d1.getTime();
            diff = diff / 1000;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diff;
    }

    private static String convertTime(long time) {
        String finalTime = "";
        long hour = time/60;
        long minutes = hour % 60;
        long seconds = time % 60;
        hour = hour/60;
        finalTime = String.format("%02d:%02d:%02d",
                TimeUnit.HOURS.toHours(hour),
                TimeUnit.MINUTES.toMinutes(minutes),
                TimeUnit.SECONDS.toSeconds(seconds));
//        System.out.println(finalTime);
        return finalTime;
    }
}
