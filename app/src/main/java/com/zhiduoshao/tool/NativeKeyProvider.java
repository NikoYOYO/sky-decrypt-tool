package com.zhiduoshao.tool;

public class NativeKeyProvider {
    
    static {
        System.loadLibrary("keyprovider");
    }
    
    public static native String getRAGUS();
    public static native String getTLAS();
}
