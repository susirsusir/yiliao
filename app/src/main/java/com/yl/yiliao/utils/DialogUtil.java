package com.yl.yiliao.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import com.yl.yiliao.R;

import java.util.List;

public class DialogUtil {


    public static  void showDialogNoTitle(Context context,String content, String positiveText,MaterialDialog.SingleButtonCallback positiveListener, String negativeText,MaterialDialog.SingleButtonCallback negativeListener){
        showDialogBase(context,null,content,positiveText,positiveListener,negativeText,negativeListener);
    }

    public static MaterialDialog showDialogBase(Context context,String title,String content, String positiveText,MaterialDialog.SingleButtonCallback positiveListener, String negativeText,MaterialDialog.SingleButtonCallback negativeListener){
        MaterialDialog dialog=new MaterialDialog.Builder(context)
                .title(title)
                .theme(Theme.LIGHT)
                .content(content)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .positiveColorRes(R.color.cursor_color)
                .negativeColorRes(R.color.cursor_color)
                .canceledOnTouchOutside(false)
                .onPositive(positiveListener)
                .onNegative(negativeListener)
                .show();
        return dialog;
    }
    public static MaterialDialog.Builder getDialogBuild(Context context,String title,String content, String positiveText,MaterialDialog.SingleButtonCallback positiveListener, String negativeText,MaterialDialog.SingleButtonCallback negativeListener){
        MaterialDialog.Builder builder=new MaterialDialog.Builder(context);
        builder.title(title)
                .content(content)
                .theme(Theme.LIGHT)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .positiveColorRes(R.color.cursor_color)
                .negativeColorRes(R.color.cursor_color)
                .canceledOnTouchOutside(false)
                .onPositive(positiveListener)
                .onNegative(negativeListener);
                return builder;
    }
    // 弹出只有一个返回按钮的提示框
    public static void showCustomBackDialog(Context context,String contentMsg, final boolean isclose) {
        showCustomSimpleDialog(context,contentMsg, "确认", isclose);
    }

    // 弹出只有一个确认按钮的提示框
    public static void showCustomConfirmDialog(Context context, String contentMsg, final boolean isclose) {
        showCustomSimpleDialog(context,contentMsg,"返回",isclose);
    }

    // 弹出只有一个按钮和title的提示框
    public static void showSimpleTitleDialog(final Context context, String title, String contentMsg, int
            stringResId, final boolean isClose) {
        DialogUtil.showDialogBase(context, title, contentMsg, context.getString(stringResId), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                if (isClose && context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        }, null, null);
    }

    // 弹出只有一个按钮的提示框
    public static void showCustomSimpleDialog(final Context context,String contentMsg, int
            stringResId,final boolean isclose) {
        DialogUtil.showDialogBase(context,null, contentMsg, context.getString(stringResId), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                if(isclose){
                    ((Activity)context).finish();
                }
            }
        },null,null);
    }

    // 弹出只有一个按钮的提示框
    public static void showCustomSimpleDialog(final Context context,String contentMsg, String
            stringResId,final boolean isclose) {
        DialogUtil.showDialogBase(context,null, contentMsg, stringResId, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                if(isclose){
                    ((Activity)context).finish();
                }
            }
        },null,null);
    }
    public static MaterialDialog showListDialogSimple(Context context, List<String> list, MaterialDialog.ListCallback listCallback){
        return showListDialogBase(context,null,null,list,listCallback);
    }
    public static MaterialDialog showListDialogBase(Context context, String title, String content,List<String> list, MaterialDialog.ListCallback listCallback) {
        MaterialDialog dialog=new MaterialDialog.Builder(context)
                .title(title)
                .items(list)
                .content(content)
                .itemsCallback(listCallback)
                .positiveColorRes(R.color.cursor_color)
                .negativeColorRes(R.color.cursor_color)
                .canceledOnTouchOutside(true).show();
        return dialog;
    }
    public static MaterialDialog showListDialogSimple(Context context, String[] array, MaterialDialog.ListCallback listCallback){
        return showListDialogBase(context,null,null,array,listCallback);
    }
    public static MaterialDialog showListDialogBase(Context context, String title, String content,String[] array, MaterialDialog.ListCallback listCallback) {
        MaterialDialog dialog=new MaterialDialog.Builder(context)
                .title(title)
                .items(array)
                .content(content)
                .itemsCallback(listCallback)
                .positiveColorRes(R.color.cursor_color)
                .negativeColorRes(R.color.cursor_color)
                .canceledOnTouchOutside(true).show();
        return dialog;
    }
}