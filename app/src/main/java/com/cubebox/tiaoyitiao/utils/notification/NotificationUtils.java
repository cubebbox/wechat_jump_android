package com.cubebox.tiaoyitiao.utils.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by luozi on 2016/11/18.
 */

public class NotificationUtils implements NotifiyID {
    private volatile static NotificationUtils instance;
    private Context context;
    /**
     * Notification管理
     */
    private NotificationManager mNotificationManager;
    /**
     * Notification构造器
     */
    private NotificationCompat.Builder mBuilder;

    public NotificationUtils(Context context) {
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 单例模式
     */
    public static NotificationUtils getInstance(Context context) {
        synchronized (NotificationUtils.class) {
            if (instance == null) instance = new NotificationUtils(context);
            return instance;
        }

    }

    /**
     * 生成一个正常的通知,不带动作行为的通知
     * * @param title         标题
     *
     * @param content  内容文本
     * @param ticker   通知闪烁文本
     * @param icon     通知图标
     * @param notifyId 通知id编号
     * @param title    通知标题     * @param isNotify   是否显示通知
     */
    public void showNormalNotification(int notifyId, String title, String content, String ticker, int icon, boolean isNotify, int progress) {
        showDefaultNotification(null, notifyId, title, content, ticker, 0, Notification.PRIORITY_DEFAULT, true, false,
                Notification.DEFAULT_VIBRATE, icon, null, null, isNotify, progress);
    }

    /**
     * 生成一个带行为动作得到通知
     * * @param title         标题
     *
     * @param content  内容文本
     * @param ticker   通知闪烁文本
     * @param icon     通知图标
     * @param intent   动作意图
     * @param notifyId 通知id编号
     * @param title    通知标题     * @param isNotify   是否显示通知
     */
    public void showIntentNotification(int notifyId, String title,
                                       String content, String ticker, int icon, Intent intent, Intent deleteIntent, boolean isNotify, int progress) {
        showDefaultNotification(null, notifyId, title, content, ticker, 0, Notification.PRIORITY_DEFAULT, true, false,
                Notification.DEFAULT_VIBRATE, icon, intent, deleteIntent, isNotify, progress);
    }

    /**
     * 生成一个自定义view 的通知,不带动作意图
     * * @param title         标题
     *
     * @param content  内容文本
     * @param ticker   通知闪烁文本
     * @param icon     通知图标
     * @param notifyId 通知id编号
     * @param title    通知标题
     * @param views    自定义view     * @param isNotify   是否显示通知
     */
    public void showCustomViewNotification(RemoteViews views, int notifyId,
                                           String title, String content, String ticker, int icon, boolean isNotify, int progress) {
        showDefaultNotification(views, notifyId, title, content, ticker, 0, Notification.PRIORITY_DEFAULT, true, false,
                Notification.DEFAULT_VIBRATE, icon, null, null, isNotify, progress);
    }

    /**
     * 生成一个自定义view 带动作意图的通知
     * * @param title         标题
     *
     * @param content  内容文本
     * @param ticker   通知闪烁文本
     * @param icon     通知图标
     * @param notifyId 通知id编号
     * @param title    通知标题
     * @param views    自定义view     * @param isNotify   是否显示通知
     */
    public void showCustomViewNotification(RemoteViews views, int notifyId,
                                           String title, String content, String ticker, int icon, Intent intent, Intent deleteIntent, boolean isNotify, int progress) {
        showDefaultNotification(views, notifyId, title, content, ticker, 0, Notification.PRIORITY_DEFAULT, true, false,
                Notification.DEFAULT_VIBRATE, icon, intent, deleteIntent, isNotify, progress);
    }

    /**
     * 生成一个通知栏
     *
     * @param title       标题
     * @param content     内容文本
     * @param ticker      通知闪烁文本
     * @param number      通知数量
     * @param pri         通知优先级
     * @param autoCancel  是否可以自动取消
     * @param ongoing     设置他为一个正在进行的通知，当设置后通知栏为常驻通知栏。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐
     *                    )或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
     * @param defaults    向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
     * @param clickIntent 意图，动作可以添加行为动作：开启activity、打开某个文件等等
     * @param icon        通知图标
     * @param isNotify    是否显示通知
     */
    public Notification showDefaultNotification(RemoteViews views, int notifyId, String title, String content, String ticker, int number,
                                                int pri, boolean autoCancel, boolean ongoing, int defaults, int icon,
                                                Intent clickIntent, Intent deleteIntent, boolean isNotify, int progress) {

        mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContent(views)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker)//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(pri)//设置该通知优先级
                .setAutoCancel(autoCancel)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(ongoing)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(icon);
        if (defaults >= 0)
            mBuilder.setDefaults(defaults);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
        if (number >= 0)
            mBuilder.setNumber(number);//显示数量
        if (clickIntent != null)
            mBuilder.setContentIntent(getDefalutIntent(context, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT));
        if (deleteIntent != null)
            mBuilder.setDeleteIntent(getDefalutIntent(context, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        if (progress >= 0)
            mBuilder.setProgress(100, progress, false);

        Notification notification = mBuilder.build();

        if (isNotify)
            mNotificationManager.notify(notifyId, notification);

        return notification;
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        if (mNotificationManager != null)
            mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        if (mNotificationManager != null)
            mNotificationManager.cancelAll();// 删除你发的所有通知
    }

    /**
     * 获取默认的pendingIntent,为了防止2.3及以下版本报错
     *
     * @param context
     * @param intent  行为动作意图，如果为空的话将会自动生成一个空intent
     * @param flags   动作标记 点击去除：Notification.FLAG_AUTO_CANCEL，在顶部常驻:Notification.FLAG_ONGOING_EVENT
     */
    public PendingIntent getDefalutIntent(Context context, Intent intent, int flags) {
        //可以添加intent的启动路径
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(NotifyRegularActivity.class);
//        stackBuilder.addNextIntent(notifyIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) SystemClock.uptimeMillis(),
                intent == null ? new Intent() : intent, flags);

        return pendingIntent;
    }
}
