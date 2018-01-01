package com.cubebox.tiaoyitiao.utils;

import android.content.Context;

public class DensityUtil {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(Context var0, float var1) {
		float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
		return (int)(var1 * var2 + 0.5F);
	}

	public static int px2sp(Context var0, float var1) {
		float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
		return (int)(var1 / var2 + 0.5F);
	}

}
