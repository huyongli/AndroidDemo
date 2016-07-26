package cn.ittiger.demo.util;

import android.os.Environment;

/**
 * @author: laohu on 2016/7/24
 * @site: http://ittiger.cn
 */
public class Util {

    public static String getSdCardPath() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return Environment.getDataDirectory().getAbsolutePath();
    }
}
