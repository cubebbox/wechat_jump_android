# 微信跳一跳android辅助

### Example

![IMG a](https://github.com/cubebbox/wechat_jump_android/blob/master/b.png)
![GIF a](https://github.com/cubebbox/wechat_jump_android/blob/master/a.gif)

辅助跳跃，根据不同的机型可以调节不同的敏感度，如果有差别比较大的同学可以直接改源码，因为懒并没有做适配那些，所以需要小伙伴自己到应用设置界面
赋予权限（悬浮窗等）,而且由于需要模拟点击所以需要root权限，手机必须root才能使用

### Customization

由于实力有限，找了很久也没有找到合适的算法去适配跳跃距离，所以干脆以25为一个阶段直接给固定值，所以不同手机可能会不同，自己根据自己的情况调节
计算方法是：屏幕显示距离-730 = 结果（如果是负数修改对应小于范围的区间值（远了减小值，近了增大值），反之则取大于区间内的值（远了增大值近了减小值））

本来想做成自动调节的，但是太懒了不想做了，就直接开源了，需要的自己改吧
```groovy
  public double getDistance() {
         double dis = Math.sqrt(((rectA.centerX() - rectB.centerX()) * (rectA.centerX() - rectB.centerX())) + ((rectA.centerY() - rectB.centerY()) * (rectA.centerY() - rectB.centerY())));
         double cen = 730f;
         if (dis < cen) {//小于
             double tmp = cen - dis;
             int tmp2 = (int) (tmp / 25);
 //                dis += (tmp * (tmp2 * 0.01 + 0.06) * (((1 - tmp) / cen)));
             if (tmp >= 0 && tmp < 25) {
                 dis += (tmp * 0.06f);
             } else if (tmp >= 25 && tmp < 50) {
                 dis += (tmp * 0.07f);
             } else if (tmp >= 50 && tmp < 75) {
                 dis += (tmp * 0.08f);
             } else if (tmp >= 75 && tmp < 100) {
                 dis += (tmp * 0.09f);
             } else if (tmp >= 100 && tmp < 125) {
                 dis += (tmp * 0.10f);
             } else if (tmp >= 125 && tmp < 150) {
                 dis += (tmp * 0.11f);
             } else if (tmp >= 150 && tmp < 175) {
                 dis += (tmp * 0.115f);
             } else if (tmp >= 175 && tmp < 200) {
                 dis += (tmp * 0.12f);
             } else if (tmp >= 200 && tmp < 225) {
                 dis += (tmp * 0.125f);
             } else if (tmp >= 225 && tmp < 250) {
                 dis += (tmp * 0.127f);//待定
             } else if (tmp >= 250 && tmp < 275) {
                 dis += (tmp * 0.13f);
             } else if (tmp >= 275 && tmp < 300) {
                 dis += (tmp * 0.14);
             } else if (tmp >= 300 && tmp < 325) {
                 dis += (tmp * 0.16f);
             } else if (tmp >= 325 && tmp < 350) {
                 dis += (tmp * 0.175f);
             } else if (tmp >= 350 && tmp < 375) {
                 dis += (tmp * 0.18f);
             } else if (tmp >= 375 && tmp < 400) {
                 dis += (tmp * 0.165f);
             } else if (tmp >= 400 && tmp < 425) {
                 dis += (tmp * 0.14f);
             } else if (tmp >= 425 && tmp < 450) {
                 dis += (tmp * 0.13f);
             } else if (tmp >= 450 && tmp < 475) {
                 dis += (tmp * 0.12f);
             } else if (tmp >= 475 && tmp < 500) {
                 dis += (tmp * 0.11f);
             } else {
                 dis += (tmp * 0.10f);
             }
         } else {//大于
             double tmp = dis - cen;
             int tmp2 = (int) (tmp / 25);
             //dis -= (tmp * (tmp2 * 0.025 + 0.05));
             if (tmp >= 0 && tmp < 25) {
                 dis -= (tmp * 0.05f);
             } else if (tmp >= 25 && tmp < 50) {
                 dis -= (tmp * 0.06f);
             } else if (tmp >= 50 && tmp < 75) {
                 dis -= (tmp * 0.07f);
             } else if (tmp >= 75 && tmp < 100) {
                 dis -= (tmp * 0.075f);
             } else if (tmp >= 100 && tmp < 125) {
                 dis -= (tmp * 0.09f);
             } else if (tmp >= 125 && tmp < 150) {
                 dis -= (tmp * 0.10f);
             } else if (tmp >= 150 && tmp < 175) {
                 dis -= (tmp * 0.11f);
             } else if (tmp >= 175 && tmp < 200) {
                 dis -= (tmp * 0.12f);
             } else if (tmp >= 200 && tmp < 225) {
                 dis -= (tmp * 0.13f);
             } else if (tmp >= 225 && tmp < 250) {
                 dis -= (tmp * 0.14f);
             } else if (tmp >= 250 && tmp < 275) {
                 dis -= (tmp * 0.15f);
             } else if (tmp >= 275 && tmp < 300) {
                 dis -= (tmp * 0.16f);
             } else if (tmp >= 300 && tmp < 325) {
                 dis -= (tmp * 0.17f);
             } else {
                 dis -= (tmp * 0.19f);
             }
         }
         return dis;
     }
```
###PS
    如果以后有空的话再做成动态调节



