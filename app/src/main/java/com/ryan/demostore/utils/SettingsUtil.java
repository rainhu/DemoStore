package com.ryan.demostore.utils;

import com.ryan.demostore.APP;

/**
 * Created by Ryan on 2018/2/10.
 */

public class SettingsUtil {
    public static final String THEME = "theme_color";


    public static void setTheme(int themeIndex){
        SPUtil.put(APP.getGlobalContext(),THEME, themeIndex);
    }

    public static int getTheme(){
        return (int)SPUtil.get(APP.getGlobalContext(),THEME, 0);
    }


}
