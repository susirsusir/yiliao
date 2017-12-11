package com.yl.yiliao.qrcode;

import com.google.zxing.Result;


public interface DecodeImgCallback {
    public void onImageDecodeSuccess(Result result);

    public void onImageDecodeFailed();
}
