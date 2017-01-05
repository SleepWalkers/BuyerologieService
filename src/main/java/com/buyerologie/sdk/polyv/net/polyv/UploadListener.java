package com.buyerologie.sdk.polyv.net.polyv;

public interface UploadListener {
    public void fail(Exception ex);

    public void success(String body);
}
