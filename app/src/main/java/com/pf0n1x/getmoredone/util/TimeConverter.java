package com.pf0n1x.getmoredone.util;

import androidx.room.TypeConverter;

import java.sql.Time;

public class TimeConverter {
    @TypeConverter
    public static Time toTime(Long timeLong){
        return timeLong == null ? null: new Time(timeLong);
    }

    @TypeConverter
    public static Long fromDate(Time time){
        return time == null ? null : time.getTime();
    }
}
