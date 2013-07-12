package com.appkefu.appkehu_3;

import com.appkefu.lib.service.AppService;
import com.appkefu.lib.service.LoginAsyncTask;
import com.appkefu.lib.service.XmppFacade;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private XmppFacade mXmppFacade;
	private AsyncTask<XmppFacade,Void,Boolean> mLoginTask;
	private boolean mBinded = false;

	/*
	 * 此为用户唯一标识，用于与您的用户体系相对接，比如可以设为 用户的用户名或ID
	 * 默认为空
	 */
	private String selfDefinedUserId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");

		if(mLoginTask == null)
			mLoginTask = new LoginTask(selfDefinedUserId);
		
		if (!mBinded) {
			Intent intent = new Intent(LoginActivity.this, AppService.class);
			bindService(intent, conn, Context.BIND_AUTO_CREATE);
		}
	} 
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		if (mBinded) {
			unbindService(conn);
			mBinded = false;
		}
		
		mXmppFacade = null;
		//newChat = null;
		Log.d(TAG, "onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		
	}
	
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.d(TAG, "MainActivity.onServiceDisconnected");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.d(TAG, "MainActivity.onServiceConnected");

			mXmppFacade = (XmppFacade) service;
			mLoginTask.execute(mXmppFacade);
			mBinded = true;
		}
	};
	
	private class LoginTask extends LoginAsyncTask {

		private String mVisitorName;
		
		public LoginTask(String visitorName) {			
			mVisitorName = visitorName;
			Log.d(TAG, "LoginTask.Construction");
		}
		
		@Override
		protected Boolean doInBackground(XmppFacade... params) {
			
			boolean result = false;
			XmppFacade facade = params[0];
			
			result = facade.login(mVisitorName);
			
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.d(TAG, "LoginTask.onPostExecute");
			
			if (result == null || !result) { // Task cancelled or exception
				if (!result) {
					Intent i = new Intent();
					i.putExtra("message", getErrorMessage());
					LoginActivity.this.setResult(Activity.RESULT_CANCELED, i);
				} else
					LoginActivity.this.setResult(Activity.RESULT_CANCELED);
				LoginActivity.this.finish();
			}
			else
			{
				//login succeed
				Log.d(TAG, "LoginTask.onPostExecute.true ");

				LoginActivity.this.startService(new Intent(LoginActivity.this, AppService.class));
				LoginActivity.this.setResult(RESULT_OK);
				LoginActivity.this.finish();
				
			}
			
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Log.d(TAG, "LoginTask.onCancelled");
			
			Intent intent = new Intent(LoginActivity.this, AppService.class);
			LoginActivity.this.stopService(intent);
		}
	}

}
