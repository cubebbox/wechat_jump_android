package com.cubebox.tiaoyitiao.utils.notification;

/**
 * Created by cubebox on 2017/9/13.
 */

public class MsgRemaindUtils {
    private static MsgRemaindUtils instance = null;


    public static MsgRemaindUtils getInstance() {
        synchronized (MsgRemaindUtils.class) {
            if (instance == null)
                instance = new MsgRemaindUtils();
        }
        return instance;
    }

}
