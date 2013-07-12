package com.appkefu.appkehu_3;

import android.app.Application;

public class AppApplication extends Application {
	
	private boolean mIsConnected = false;

	public AppApplication(){
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public boolean isConnected() {
		return mIsConnected;
	}
	
	public void setConnected(boolean isConnected) {
		mIsConnected = isConnected;
	}
}
