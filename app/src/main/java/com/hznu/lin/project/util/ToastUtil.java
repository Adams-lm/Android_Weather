package com.hznu.lin.project.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 自定义Toast类，避免Toast显示延迟和线程池累加，用context统一管理生命周期，进行替换
 * 使用时注意context以及this的上下文关系对应
 * @author LIN
 * @date 2020/10/13 14:32
 */
public class ToastUtil {
    private static Toast myToast;
    public static void showToast(Context context, String msg, int length) {
        missToast(context);
        myToast = Toast.makeText(context, msg, length);
        myToast.show();
    }
    //利用上下文统一管理Toast生命周期，补充该方法后可在调用后直接miss
    public static void missToast(Context context) {
        if (myToast!=null){
            myToast.cancel();//仅仅为隐藏，如果不调用下面myToast=null在同一界面使用出现首次点击Toast不能正常弹出的问题
            myToast=null;
        }
    }
}
