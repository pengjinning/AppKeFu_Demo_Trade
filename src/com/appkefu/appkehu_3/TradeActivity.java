package com.appkefu.appkehu_3;

import org.jivesoftware.smack.util.StringUtils;

import com.appkefu.lib.interfaces.KFInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFLog;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class TradeActivity extends TabActivity {
	
	/*
	 提示：如果已经运行过旧版的Demo，请先在手机上删除原先的App再重新运行此工程
	 更多使用帮助参见：http://appkefu.com/AppKeFu/tutorial-android.html
	
	 简要使用说明：
	 第1步：到http://appkefu.com/AppKeFu/admin/，注册/创建应用/分配客服，并将获取的appkey填入AnroidManifest.xml
	 		中的com.appkefu.lib.appkey
	 第2步：用真实的客服名初始化mKefuUsername
	 第3步：调用 KFInterfaces.visitorLogin(this); 函数登录
	 第4步：调用chatWithKeFu(mKefuUsername);与客服会话，其中mKefuUsername需要替换为真实客服名
	 第5步：(可选)
     	//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
     	KFInterfaces.setVisitorNickname("访客1", this);
	 */
	
	
	TabHost tabHost; 
	private RadioButton main_tab_home, main_tab_catagory, main_tab_car,
			main_tab_buy, main_tab_more;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTab();
        init();
        ExitManager.getInstance().addActivity(this);

        //设置开发者调试模式，默认为false，如要开启开发者模式，请设置为true
      	KFInterfaces.enableDebugMode(this, true);
		//第一步：登录
		KFInterfaces.visitorLogin(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		KFLog.dd("onStart");
		
		IntentFilter intentFilter = new IntentFilter();
		//监听网络连接变化情况
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //监听消息
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);

        registerReceiver(mXmppreceiver, intentFilter); 
	}


	@Override
	protected void onStop() {
		super.onStop();
		KFLog.dd("onStop");
		
        unregisterReceiver(mXmppreceiver);
	}
	
	//监听：连接状态、即时通讯消息、客服在线状态
		private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() 
		{
	        public void onReceive(Context context, Intent intent) 
	        {
	            String action = intent.getAction();
	            //监听：连接状态
	            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//监听链接状态
	            {
	                updateStatus(intent.getIntExtra("new_state", 0));        
	            }
	            //监听：即时通讯消息
	            else if(action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//监听消息
	            {
	            	String body = intent.getStringExtra("body");
	            	String from = StringUtils.parseName(intent.getStringExtra("from"));
	            	
	            	KFLog.dd("body:"+body+" from:"+from);
	            }
	        }
	    };
	    

	  //根据监听到的连接变化情况更新界面显示
	    private void updateStatus(int status) {

	    	switch (status) {
	            case KFXmppManager.CONNECTED:
	            	KFLog.dd("connected");
	            	//mTitle.setText("微客服(客服Demo)");

	        		//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
	        		//KFInterfaces.setVisitorNickname("访客1", this);

	                break;
	            case KFXmppManager.DISCONNECTED:
	            	KFLog.dd("disconnected");
	            	//mTitle.setText("微客服(客服Demo)(未连接)");
	                break;
	            case KFXmppManager.CONNECTING:
	            	KFLog.dd("connecting");
	            	//mTitle.setText("微客服(客服Demo)(登录中...)");
	            	break;
	            case KFXmppManager.DISCONNECTING:
	            	KFLog.dd("connecting");
	            	//mTitle.setText("微客服(客服Demo)(登出中...)");
	                break;
	            case KFXmppManager.WAITING_TO_CONNECT:
	            case KFXmppManager.WAITING_FOR_NETWORK:
	            	KFLog.dd("waiting to connect");
	            	//mTitle.setText("微客服(客服Demo)(等待中)");
	                break;
	            default:
	                throw new IllegalStateException();
	        }
	    }
	    
	public void init(){
    	main_tab_home=(RadioButton)findViewById(R.id.main_tab_home);
    	main_tab_catagory = (RadioButton) findViewById(R.id.main_tab_catagory);
		main_tab_car = (RadioButton) findViewById(R.id.main_tab_car);
		main_tab_buy = (RadioButton) findViewById(R.id.main_tab_buy);
		main_tab_more = (RadioButton) findViewById(R.id.main_tab_more);
		main_tab_home.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("home");

			}
		});

		main_tab_catagory.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("catagory");

			}
		});
		main_tab_car.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("car");

			}
		});
		main_tab_buy.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("buy");

			}
		});
		main_tab_more.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				tabHost.setCurrentTabByTag("more");

			}
		});
    }
    
    public void initTab(){
    	tabHost=getTabHost();
    	tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(new Intent(this, HomeActivity.class)));
    	tabHost.addTab(tabHost.newTabSpec("catagory").setIndicator("catagory")
				.setContent(new Intent(this, CategoryActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("car").setIndicator("car")
				.setContent(new Intent(this, CarActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("buy").setIndicator("buy")
				.setContent(new Intent(this, BuyActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("more")
				.setContent(new Intent(this, MoreActivity.class)));
    }
    
    /*
    public boolean dispatchKeyEvent( KeyEvent event) {
		int keyCode=event.getKeyCode();
	      if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getRepeatCount() == 0) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						TradeActivity.this);
				alertDialog.setTitle(TradeActivity.this
						.getString(R.string.app_close));
				alertDialog.setPositiveButton(TradeActivity.this
						.getString(R.string.btn_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ExitManager.getInstance().exit();
							}
						});
				alertDialog.setNegativeButton(TradeActivity.this
						.getString(R.string.btn_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialog.show();
			}
		}
		return super.dispatchKeyEvent(event);
	}
     */

}






















