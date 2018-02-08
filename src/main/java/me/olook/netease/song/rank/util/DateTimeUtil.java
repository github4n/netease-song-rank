package me.olook.netease.song.rank.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author zhaohw
 * @date 2018-01-12 11:23
 */
public class DateTimeUtil {

    /**
     * 获取前一天起始
     */
    public static Date getLastDayStart(){
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取前一天结束
     */
    public static Date getLastDayEnd(){
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取当天开始
     */
    public static Date getThisDayStart(){
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天结束
     */
    public static Date getThisDayEnd(){
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 基于当前时间的上周周一开始
     * @return
     */
    public static Date getLastWeekStart(){
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH,-1);
        cal.set(Calendar.DAY_OF_WEEK,2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 基于当前时间的上周周日结束
     * @return
     */
    public static Date getLastWeekEnd(){
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH,-1);
        cal.set(Calendar.DAY_OF_WEEK,0);
        cal.add(Calendar.DATE,1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取上月第一天开始
     */
    public static Date getLastMonthStart(){
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.MONTH,-1);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取上月最后一天结束
     */
    public static Date getLastMonthEnd(){
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,0);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取去年第一天
     * @return
     */
    public static Date getLastYearStart(){
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.YEAR,-1);
        cal.set(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取去年最后一天
     * @return
     */
    public static Date getLastYearEnd(){
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH,0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取上季度第一天
     * @return
     */
    public static Date getLastSeasonStart(){
        Calendar cal= Calendar.getInstance();
        //获取当前季度
        int season = cal.get(Calendar.MONTH) / 3 + 1;
        switch (season){
            default:
                break;
            case 1 :
                //当前为第一季度 则上季度在去年
                cal.add(Calendar.YEAR,-1);
                cal.set(Calendar.MONTH,Calendar.OCTOBER);
                break;
            case 2 :
                cal.set(Calendar.MONTH,Calendar.JANUARY);
                break;
            case 3 :
                cal.set(Calendar.MONTH,Calendar.APRIL);
                break;
            case 4 :
                cal.set(Calendar.MONTH,Calendar.JULY);
                break;
        }
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取上季度第一天
     * @return
     */
    public static Date getLastSeasonEnd(){
        Calendar cal= Calendar.getInstance();
        //获取当前季度
        int season = cal.get(Calendar.MONTH) / 3 + 1;
        switch (season){
            default:
                break;
            case 1 :
                //当前为第一季度 则上季度在去年
                cal.add(Calendar.YEAR,-1);
                cal.set(Calendar.MONTH,Calendar.DECEMBER);
                break;
            case 2 :
                cal.set(Calendar.MONTH,Calendar.MARCH);
                break;
            case 3 :
                cal.set(Calendar.MONTH,Calendar.JUNE);
                break;
            case 4 :
                cal.set(Calendar.MONTH,Calendar.SEPTEMBER);
                break;
        }
        cal.set(Calendar.DAY_OF_MONTH,0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static void main(String[] args) {
        Calendar cal= Calendar.getInstance();
        System.out.println("当前时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));

        System.out.println("昨天开始:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastDayStart()));
        System.out.println("昨天结束:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastDayEnd()));

        System.out.println("当天开始:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getThisDayStart()));
        System.out.println("当天结束:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getThisDayEnd()));

        System.out.println("上周开始:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastWeekStart()));
        System.out.println("上周结束:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastWeekEnd()));

        System.out.println("上月开始:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastMonthStart()));
        System.out.println("上月结束:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastMonthEnd()));

        System.out.println("上季开始:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastSeasonStart()));
        System.out.println("上季结束:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastSeasonEnd()));

        System.out.println("去年开始:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastYearStart()));
        System.out.println("去年结束:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastYearEnd()));
    }
}
