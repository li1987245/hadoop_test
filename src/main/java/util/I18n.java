package util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@SuppressWarnings("all")
public class I18n {
    public static  final  String basename = "language.language";
    private static I18n instance = new I18n();
    private ResourceBundle bundle;
    private I18n(){
        bundle = ResourceBundle.getBundle(basename);
    }
    public static I18n getInstance(){
        return instance;
    }

    public String getMessage(String key,Object ...obj){
        String str = bundle.getString(key);
        MessageFormat format = new MessageFormat(str);
        String result = format.format(obj);
        return result;
    }

}
