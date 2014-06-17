package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class AlarmReceiver extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent=this.getIntent();
	    String title=intent.getStringExtra("msg");
		
	    new AlertDialog.Builder(AlarmReceiver.this)
	    .setTitle("提醒事项：")
	    .setMessage(title)
	    .setPositiveButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				finish();
			}})
			.create()
			.show();
		
	}

		
	
	

}
