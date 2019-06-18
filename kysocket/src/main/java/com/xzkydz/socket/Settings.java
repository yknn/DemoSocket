package com.xzkydz.socket;

public class Settings {
    private int mWritePackageBytes = 1024;
    private int mReadPackageBytes = 256;
    private int mConnectTimeoutSecond = 10;
    private int mMaxReadDataMB = 2;

    public int getmWritePackageBytes() {
        return mWritePackageBytes;
    }

    public void setmWritePackageBytes(int mWritePackageBytes) {
        this.mWritePackageBytes = mWritePackageBytes;
    }

    public int getmReadPackageBytes() {
        return mReadPackageBytes;
    }

    public void setmReadPackageBytes(int mReadPackageBytes) {
        this.mReadPackageBytes = mReadPackageBytes;
    }

    public int getmConnectTimeoutSecond() {
        return mConnectTimeoutSecond;
    }

    public void setmConnectTimeoutSecond(int mConnectTimeoutSecond) {
        this.mConnectTimeoutSecond = mConnectTimeoutSecond;
    }

    public int getmMaxReadDataMB() {
        return mMaxReadDataMB;
    }

    public void setmMaxReadDataMB(int mMaxReadDataMB) {
        this.mMaxReadDataMB = mMaxReadDataMB;
    }
}
