package com.example.android.navigationdrawerexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//给当前activity去掉标题
		
		setContentView(R.layout.activity_welcome);
		
		//ImageView iv = (ImageView)this.findViewById(R.id.weatherWelcome);
		
		welcome();
	}

	public void welcome(){
		//重点：线程机制。
		//第一步，创建线程对象。
		new Thread(
				new Runnable(){//匿名的线程实现类。

					@Override
					public void run() {//第二个线程的内容代码。
						// TODO Auto-generated method stub
						try {
							Thread.sleep(3000);
							Message m=new Message();//这个是android机制当中的一个对象。作用是给线程机制发消息。
							//如何发消息了，线程机制有一个类，Handler，作用是收发message消息对象。
							//重点：通过使用handler类向主线程发送消息。主线程由handleMessage方法来接收消息。
							lhandler.sendMessage(m);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}//run.
					
				}//runnable.
				).start();
	}
	
	//创建一个操作消息机制的handler类。
		Handler lhandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {//接收另一个线程发过来Message消息，
				// TODO Auto-generated method stub
				begin();
			}
			
		};//handler.
		
		public void begin(){
			Intent in=new Intent(WelcomeActivity.this,MainActivity.class);
			//in.setClass(MainActivity.this, MainActivity.this);
			startActivity(in);
			WelcomeActivity.this.finish();
		}//begin.

}
