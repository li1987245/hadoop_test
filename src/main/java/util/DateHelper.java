package util;

import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by d on 2016/12/8.
 */
public class DateHelper {

    /**
     * 格式化日期
     * @param date
     * @param formate
     * @return
     */
    public static String format(Date date, String formate) {
        Assert.notNull(date);
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        return sdf.format(date);
    }

    public static String simpleFormat(Date date){
        return format(date,"yyyy-mm-dd");
    }
}
