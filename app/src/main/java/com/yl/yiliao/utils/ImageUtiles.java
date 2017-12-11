package com.yl.yiliao.utils;


import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.yl.yiliao.R;



public class ImageUtiles {

    public static void load(SimpleDraweeView view, String url) {
        String imgPath = Constants.BASE_URL + "/" + url;
        Log.e("Load Avater:", imgPath);
        Phoenix.with(view).load(imgPath);
    }

    public static void loadFile(SimpleDraweeView view, String filePath) {
        Phoenix.with(view).load(filePath);
    }

    public static void loadGlideHead(ImageView imageView, String url) {
        String imgPath = Constants.BASE_URL + "/" + url;
        Log.e("Load Avater:", imgPath);
        Glide.with(imageView.getContext()).load(imgPath).placeholder(R.drawable.default_head_icon).error(R.drawable.default_head_icon).dontAnimate().into(imageView);

    }

    public static void loadGlideHeadPath(ImageView imageView, String path) {
        Log.e("Load Avater:", path);
        Glide.with(imageView.getContext()).load(path).placeholder(R.drawable.default_head_icon).error(R.drawable.default_head_icon).dontAnimate().into(imageView);

    }

    public static void loadGlideHeadRes(ImageView imageView, int res) {
        Glide.with(imageView.getContext()).load(res).placeholder(R.drawable.default_head_icon).error(R.drawable.default_head_icon).dontAnimate().into(imageView);

    }

    public static void loadRes(SimpleDraweeView view, int res) {
        Phoenix.with(view).load(res);
    }
}
