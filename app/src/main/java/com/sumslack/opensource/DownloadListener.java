package com.sumslack.opensource;

/**
 * Created by Administrator on 2018/2/23/023.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onParsed();
    void onCanceled();
}
