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
		requestWindowFeature(Window.FEATURE_NO_TITLE);//����ǰactivityȥ������
		
		setContentView(R.layout.activity_welcome);
		
		//ImageView iv = (ImageView)this.findViewById(R.id.weatherWelcome);
		
		welcome();
	}

	public void welcome(){
		//�ص㣺�̻߳��ơ�
		//��һ���������̶߳���
		new Thread(
				new Runnable(){//�������߳�ʵ���ࡣ

					@Override
					public void run() {//�ڶ����̵߳����ݴ��롣
						// TODO Auto-generated method stub
						try {
							Thread.sleep(3000);
							Message m=new Message();//�����android���Ƶ��е�һ�����������Ǹ��̻߳��Ʒ���Ϣ��
							//��η���Ϣ�ˣ��̻߳�����һ���࣬Handler���������շ�message��Ϣ����
							//�ص㣺ͨ��ʹ��handler�������̷߳�����Ϣ�����߳���handleMessage������������Ϣ��
							lhandler.sendMessage(m);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}//run.
					
				}//runnable.
				).start();
	}
	
	//����һ��������Ϣ���Ƶ�handler�ࡣ
		Handler lhandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {//������һ���̷߳�����Message��Ϣ��
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
