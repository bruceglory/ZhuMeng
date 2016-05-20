package com.example.bruce.zhumeng.utils;

import android.content.Context;
import android.content.res.Resources;

import com.example.bruce.zhumeng.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhang on 2016/5/12.
 */
public class TimeUtils {

    private static final long ONE_MIU = 60*1000;
    private static final long ONE_HOUR = 60*ONE_MIU;
    private static final long ONE_DAY = 24*ONE_HOUR;

    public static String TimeDiff(Context context,Date date){
        Resources resources = context.getResources();
        long currentTimeMillis = System.currentTimeMillis();
        long timeDiff = currentTimeMillis - date.getTime();
        if(timeDiff < ONE_MIU) {
            return String.format(resources.getString(R.string.minute_time),0);
        } else if( timeDiff >= ONE_MIU && timeDiff < ONE_HOUR) {
            int timeDiffInt = (int)(timeDiff/ONE_MIU);
            return String.format(resources.getString(R.string.minute_time),timeDiffInt);
        } else if( timeDiff >= ONE_HOUR && timeDiff < ONE_DAY) {
            int timeDiffInt = (int)(timeDiff/ONE_HOUR);
            return String.format(resources.getString(R.string.day_time),timeDiffInt);
        } else if(timeDiff >= ONE_DAY ) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            Date yesterday = new Date(currentTimeMillis-ONE_DAY);
            cal.setTime(yesterday);
            int yesterDay = cal.get(Calendar.DAY_OF_MONTH);
            if(yesterDay == day) {
                return resources.getString(R.string.yesterday);
            } else {
                return String.format(resources.getString(R.string.year_month_day), year, month, day);
            }
        }
        return resources.getString(R.string.unKnow);
    }
}
