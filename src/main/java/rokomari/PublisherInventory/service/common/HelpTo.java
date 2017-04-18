package rokomari.PublisherInventory.service.common;


import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class HelpTo {

    public String convertDate(Calendar calendar){

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        String am_pm = String.valueOf(calendar.get(Calendar.AM_PM));
        String time = " ("+hour+":"+minute+":"+second+" "+am_pm;

        if (month.length()<2){
            month = "0"+month;
        }
        if (day.length()<2){
            day = "0"+day;
        }

        return day+"-"+month+"-"+year;
    }

    public String convertDateForPoAndRo(Calendar calendar){

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        String am_pm = String.valueOf(calendar.get(Calendar.AM_PM));
        String time = " ("+hour+":"+minute+":"+second+" "+am_pm;

        if (month.length()<2){
            month = "0"+month;
        }
        if (day.length()<2){
            day = "0"+day;
        }

        return year+month+day;
    }

}