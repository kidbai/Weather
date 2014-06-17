package com.example.android.navigationdrawerexample;

import java.util.ArrayList;
import java.util.HashMap;

import com.exina.android.dbhelper.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat.Action;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


public class OutPlan extends Activity {
	ListView lv;//显示所有数据的ListView
	ArrayList list;//拥有所有数据的Adapter
	SimpleAdapter adapter;//装搜索框的linearlayout,默认情况下visibility=gone
	LinearLayout searchLinearout;
  	LinearLayout mainLinearLayout;
  	LinearLayout listitem;
  	//搜索框
  	EditText et_search;
  	EditText et_enter_file_name;
  //存储标记条目的_id号
  	ArrayList<Integer> deleteId;
  	//显示的条目的名称和等级
  	//TextView ct_name,ct_level;
  	String date;
  	int Year,Month,Day;
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out_plan);
		Intent in =getIntent();
		Bundle bu = in.getExtras();
		date = bu.getString("date");
		Year = bu.getInt("Year");
		Month= bu.getInt("Month");
		Day = bu.getInt("Day");
		
		ActionBar actionBar = getActionBar(); //获得并隐藏操作栏
		Resources r = getResources();  
		Drawable myDrawable = r.getDrawable(R.drawable.actionbar); 
		
		actionBar.setBackgroundDrawable(myDrawable); 
		actionBar.setTitle(date); 
		mainLinearLayout=(LinearLayout)this.findViewById(R.id.list_ll);
		final DBHelper myDBHelper= new DBHelper(this);//获得所有用户的list
        SQLiteDatabase sdb=myDBHelper.getReadableDatabase(); //打开数据库
		list=myDBHelper.getAllPlans(sdb,date);
	    lv=(ListView)this.findViewById(R.id.ct_lv);
	        //将数据整合起来
	    adapter=new SimpleAdapter(this, list, R.layout.listitem, new String[]{"title","plan_date"},
	        		new int[]{R.id.ct_name,R.id.ct_mobilephone});
	        lv.setAdapter(adapter);//将整合好的adapter交给listview，显示给用户看
	        lv.setCacheColorHint(Color.TRANSPARENT); //设置ListView的背景为透明
	        /*
	         * 单击某一选项时，弹出联系人详细信息
	         * 
	         */
	        lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int id,
						long arg3) {
					// TODO Auto-generated method stub
					HashMap item = (HashMap)arg0.getItemAtPosition(id);
					Bundle bu =new Bundle();
					int uid = Integer.parseInt(String.valueOf(item.get("uid")));
					Intent in = new Intent(OutPlan.this,Plans_detail.class);
					bu.putString("title", item.get("title").toString());
					bu.putString("level", item.get("level").toString());
					bu.putString("site", item.get("site").toString());
					bu.putString("current_date", item.get("current_date").toString());
					bu.putString("plan_date", item.get("plan_date").toString());
					bu.putString("remind_time", item.get("remind_time").toString());
					bu.putInt("uid",uid);
					bu.putInt("Year", Year);
					bu.putInt("Month", Month);
					bu.putInt("Day", Day);		
					in.putExtras(bu);
					startActivity(in);
					if(searchLinearout != null && searchLinearout.getVisibility()==View.VISIBLE) {
						searchLinearout.setVisibility(View.GONE);
					}
				}});
	        //长按某一选项时
	        lv.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view,
						int id, long arg3) {
					// TODO Auto-generated method stub
					if(deleteId==null){
						deleteId = new ArrayList<Integer>();
					}
					HashMap item=(HashMap)arg0.getItemAtPosition(id);
					Integer uid=Integer.valueOf(String.valueOf(item.get("uid")));
					RelativeLayout r = (RelativeLayout)view;
					ImageView markedView = (ImageView)r.getChildAt(2);
					if(markedView.getVisibility()==View.VISIBLE){
						markedView.setVisibility(View.GONE);
						deleteId.remove(uid);
					}else{
						markedView.setVisibility(View.VISIBLE);
						deleteId.add(uid);
					}
					
					return true;
				}
				});
	        //为list添加item选择器
	        Drawable bgDrawable = getResources().getDrawable(R.drawable.longclick_selector);
	        lv.setSelector(bgDrawable);
	   
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.top_menu, menu);
		return true;
	}
	/*为上方菜单按钮添加事件
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.menu_add:
			Intent in=new Intent(this,Addnew.class);
			Bundle bu = new Bundle();
			bu.putString("date", date);
			bu.putInt("Year", Year);
			bu.putInt("Month", Month);
			bu.putInt("Day", Day);
			in.putExtras(bu);
			startActivity(in);
			return true;
		case R.id.menu_search:
			loadSearchLinearout();//加载搜索框
			if(searchLinearout.getVisibility()==View.VISIBLE) {
				searchLinearout.setVisibility(View.GONE);
			} else {
				searchLinearout.setVisibility(View.VISIBLE);
				et_search.requestFocus();
				et_search.selectAll();
			}
			return true;
		case R.id.menu_delete:
			//如果搜索框可见，将搜索框关闭
			if(searchLinearout!=null && searchLinearout.getVisibility()==View.VISIBLE){
				searchLinearout.setVisibility(View.GONE );
			}
			if(deleteId == null||deleteId.size() == 0){//表示没有对象被选中
				Toast.makeText(OutPlan.this, "没有任何被选中的记录\n长按可选中删除对象", 1).show();
			}else{
				new AlertDialog.Builder(OutPlan.this)
				.setTitle("确定要删除选中的"+deleteId.size()+"条记录么？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						DBHelper myDBHelper = new DBHelper(OutPlan.this);
						SQLiteDatabase sdb=myDBHelper.getReadableDatabase();
						myDBHelper.deleteMarked(deleteId, sdb);
						//重置试图
						list=myDBHelper.getAllPlans(sdb,date);
						adapter = new SimpleAdapter(OutPlan.this,
								list,
								R.layout.listitem, 
								new String[]{"title","plan_date"}, 
								new int[]{R.id.ct_name,R.id.ct_mobilephone});
						lv.setAdapter(adapter);
						deleteId.clear();
						Toast.makeText(OutPlan.this, "删除记录成功！", 1).show();
					}
				})
				.setNegativeButton("取消", null)
				.create()
				.show();
			}
			return true;
		}
		return true;
	}
	//加载搜索框，从搜索框中获取信息
	private void loadSearchLinearout(){
		if(searchLinearout == null){
			searchLinearout=(LinearLayout)findViewById(R.id.ll_search);
			et_search = (EditText)findViewById(R.id.et_search);
			et_search.setOnKeyListener(new OnKeyListener(){

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					String condition=et_search.getText().toString();
					if(condition.equals("")){
						lv.setAdapter(adapter);
					}
					DBHelper myDBHelper = new DBHelper(OutPlan.this);
					SQLiteDatabase sdb = myDBHelper.getReadableDatabase();
				    list = myDBHelper.getPlans(condition, sdb,date);
				    SimpleAdapter searchAdapter = 
							new SimpleAdapter(
									        OutPlan.this, 
				        					list, 
				        					R.layout.listitem, 
				        					new String[]{"title","plan_date"}, 
				        					new int[]{R.id.ct_name,R.id.ct_mobilephone});
					lv.setAdapter(searchAdapter); 
					return false;
				}});
		}
	}
	



}//class
