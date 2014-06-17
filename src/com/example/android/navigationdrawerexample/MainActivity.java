/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawerexample;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.android.DB.mydbhelper;
import android.R.array;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.ActionBar.OnNavigationListener;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	static String mainCityCode;
	static String city;
	static String weather1,weather2,weather3,weather4;
	static String wind1;
	static String temp1,temp2,temp3,temp4,index_d,st1,fchh;
	static String changeCityName;
	static String index_uv;
	static String img_title1,img_title2,img_title3,img_title4;
	static String higher_temp1,lower_temp1;
	static String[] s;
	static String xianshi;
	
	static String higher_temp2,lower_temp2;
	static String[] s2;
	static String xianshi2;
	
	static String higher_temp3,lower_temp3;
	static String[] s3;
	static String xianshi3;
	static String higher_temp4,lower_temp4;
	static String[] s4;
	static String xianshi4;
	
	TextView tv_city_name,tv_city_temp1,tv_city_wind1,tv_city_weather1;
	TextView tv_city_ziwaixian,tv_city_index_d;
	ImageButton btn_nextpage;
	static ArrayList<HashMap<String,Object>> citysearch;
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	static CalendarView mView = null;
	static TextView mHit;
	static Handler mHandler = new Handler();
	static  TextView cen_text;
	static Rect ecBounds;
	private long exitTime = 0;
	
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if(System.currentTimeMillis() - exitTime > 2000){
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}else
			{
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources resources = getBaseContext().getResources();
		//�����һ��Ĭ�ϳ���
		mydbhelper dbh_default = new mydbhelper(MainActivity.this);
		SQLiteDatabase sdb_default = dbh_default.getReadableDatabase();
		String[] table_default = new String[]{"_id","citycode"};
		Cursor cs_default = sdb_default.query(mydbhelper.TABLE_NAME_CITYCODE, table_default, null, null, null, null, null);
		if(cs_default != null)
		{
			if(cs_default.getCount() == 0)
			{
				ContentValues cv_default = new ContentValues();
				String citycode_default = "101320101";
				cv_default.put("citycode", citycode_default);
				sdb_default.insert(mydbhelper.TABLE_NAME_CITYCODE, null, cv_default);
			}
		}
		
		setContentView(R.layout.activity_main);
		tv_city_name = (TextView)
				this.findViewById(R.id.tv_city_name);
		tv_city_temp1 = (TextView) this
				.findViewById(R.id.tv_city_temp1);
		tv_city_wind1 = (TextView) this
				.findViewById(R.id.tv_city_wind1);
		tv_city_weather1 = (TextView) this
				.findViewById(R.id.tv_city_weather1);
		
		// ��city��cityidд�����ݿ�
		String[][] Array = new String[][] { { "101010100", "����" },
				{ "101010200", "����" }, { "101020100", "�Ϻ�" },
				{ "101030100", "���" }, { "101040100", "����" },
				{ "101050101", "������" }, { "101060101", "����" },
				{ "101070101", "����" }, { "101070201", "����" },
				{ "101070205", "��˳" }, { "101070301", "��ɽ" },
				{ "101080101", "���ͺ���" }, { "101090101", "ʯ��ׯ" },
				{ "101090206", "����" }, { "101100101", "̫ԭ" },
				{ "101310101", "����" }, { "101310201", "����" },
				{ "101310202", "����" }, { "101310217", "��ɳ" },
				{ "101310222", "��ָɽ" }, { "101320101", "���" },
				{ "101320102", "����" }, { "101320103", "�½�" },
				{ "101320104", "�л�" },// 24
		};
		mydbhelper dbh = new mydbhelper(MainActivity.this);
		SQLiteDatabase sdb = dbh.getReadableDatabase();
		ContentValues cv = new ContentValues();
		String[] table = new String[] { "_id", "city", "cityid" };
		Cursor cs = sdb.query(mydbhelper.TABLE_NAME_CITY, table, null, null,
				null, null, null);
		if (cs.getCount() == 0) {
			for (int i = 0; i < 24; i++) {
				cv.put("city", Array[i][1]);
				cv.put("cityid", Array[i][0]);
				sdb.insert(mydbhelper.TABLE_NAME_CITY, null, cv);
			}
		}

		System.out.println("����¼��ɹ�QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");

		mTitle = getTitle();
		mDrawerTitle = "����Ԥ��";
		mPlanetTitles = new String[] { "����", "����", "���ó���","���й���","�˳�" };
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		
		//��drawer�򿪵�ʱ������һ����Ӱ
		// ��
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerLayout.setBackgroundColor(R.drawable.light);
		// set up the drawer's list view with items and click listener
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles);
		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().hide();//����actionbar
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}// �������Ͻǵİ���

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}
	
	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new WeatherFragment();
		Bundle args = new Bundle();
		args.putInt(WeatherFragment.ARG_WEAHTER_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static class WeatherFragment extends Fragment implements CalendarView.OnCellTouchListener{
		public static final String ARG_WEAHTER_NUMBER = "planet_number";
		String msggmainfo;
		List<CWeather> clist;
		TextView tv_city_name,tv_city_temp1,tv_city_temp2,tv_city_temp3,tv_city_temp4,tv_city_wind1,tv_city_weather1;
		TextView tv_city_ziwaixian,tv_city_fchh,tv_city_index_d,tv_city_temp1_lower;
		ImageButton btn_nextpage,btn_share,btn_refresh;
		LinearLayout temp;
		ImageView img_city_temp1;
		
		public WeatherFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;

			// ///////////////��������//////////////////

			if (getArguments().getInt(ARG_WEAHTER_NUMBER) == 0) {
				System.out.println(getArguments().getInt(ARG_WEAHTER_NUMBER)  + "qqqqqqqqqqqqqq");
				rootView = inflater.inflate(R.layout.activity_scroll_view,
						container, false);
				subThread();
				
				tv_city_name = (TextView) rootView
						.findViewById(R.id.tv_city_name);
				tv_city_temp1 = (TextView) rootView
						.findViewById(R.id.tv_city_temp1);
				tv_city_temp2 = (TextView) rootView.findViewById(R.id.tv_city_temp2);
				tv_city_temp3 = (TextView) rootView.findViewById(R.id.tv_city_temp3);
				tv_city_temp4 = (TextView) rootView.findViewById(R.id.tv_city_temp4);
				tv_city_wind1 = (TextView) rootView
						.findViewById(R.id.tv_city_wind1);
				tv_city_weather1 = (TextView) rootView
						.findViewById(R.id.tv_city_weather1);
				tv_city_fchh = (TextView) rootView.findViewById(R.id.tv_city_fchh);
				tv_city_index_d = (TextView) rootView.findViewById(R.id.tv_city_index_d);
				tv_city_temp1_lower = (TextView) rootView.findViewById(R.id.tv_city_temp1_lower);
				
			//	btn_nextpage = (ImageButton) rootView.findViewById(R.id.btn_nextpage);
				btn_share = (ImageButton) rootView.findViewById(R.id.btn_share);
				btn_refresh = (ImageButton) rootView.findViewById(R.id.btn_refresh);
				
				tv_city_ziwaixian = (TextView) rootView.findViewById(R.id.tv_city_ziwaixian);	
				temp=(LinearLayout)rootView.findViewById(R.id.weather_showlayout);
				img_city_temp1 = (ImageView) rootView.findViewById(R.id.img_city_temp1);
				
				btn_share.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("������ȡ������Ϣ" + weather1);
						String msgContent = "DouBi  ������ܰ��ʾ:��������:" + weather1 + ",����:" + temp1 + "," + wind1 + 
								",������ǿ��:" + index_uv+"." + "���н���:" + index_d + ".DouBiף��ÿ������飡";
						sendmsg(msgContent);
					}

					private void sendmsg(String msgContent) {
						// TODO Auto-generated method stub
						Uri smsToUri = Uri.parse("smsto:");  
						  
						Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
						  
						intent.putExtra("sms_body", msgContent);  
						  
						startActivity(intent);
					}});//����button
				
				btn_refresh.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						subThread();
					}});//ˢ�°�ť
				/*btn_nextpage.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						System.out.println("Ҫ��ҳ�ˣ���");
						Intent in = new Intent(getActivity(),Weather_nextPageActivity.class);
						startActivity(in);
					}});//��һҳ��ť
				
				
				btn_share.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("������ȡ������Ϣ" + weather1);
						String msgContent = "DouBi  ������ܰ��ʾ:��������:" + weather1 + ",����:" + temp1 + "," + wind1 + 
								",������ǿ��:" + index_uv+"." + "���н���:" + index_d + ".DouBiף��ÿ������飡";
						sendmsg(msgContent);
					}

					private void sendmsg(String msgContent) {
						// TODO Auto-generated method stub
						Uri smsToUri = Uri.parse("smsto:");  
						  
						Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
						  
						intent.putExtra("sms_body", msgContent);  
						  
						startActivity(intent);
					}});//����button
				
				btn_refresh.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						subThread();
					}});//ˢ�°�ť
*/			}

/////////////////////����///////////////////////////////////
			if (getArguments().getInt(ARG_WEAHTER_NUMBER) == 1) {
				rootView = inflater.inflate(R.layout.main,
						container, false);
				mView = (CalendarView) rootView.findViewById(R.id.calendar);
		        mView.setOnCellTouchListener(this);
		        cen_text = (TextView) rootView.findViewById(R.id.Top_Date);
		        cen_text.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
		        Button btLeft = (Button) rootView.findViewById(R.id.btnLeft);
		        Button btRight = (Button) rootView.findViewById(R.id.btRight);
		        btLeft.setOnClickListener(new OnClickListener() { 
					public void onClick(View v) {
						mView.previousMonth(); 
						cen_text.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
					}
				});
		        btRight.setOnClickListener(new OnClickListener() { 
					public void onClick(View v) {
						mView.nextMonth(); 
						cen_text.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
					}
				});
			}
			
			if (getArguments().getInt(ARG_WEAHTER_NUMBER) == 2) {
					rootView = inflater.inflate(R.layout.ofenusecity,
							container, false);
				//���ó���
				System.out.println(getArguments().getInt(ARG_WEAHTER_NUMBER) + "����2222222222");
				ListView lv;
				lv = (ListView) rootView.findViewById(R.id.lv_chooseOftenUseCity);
				ArrayList<HashMap<String,String>> oftenCityList = new ArrayList<HashMap<String,String>>();
				String oftenUseCity_name = "";
				String[] ofenUCity = new String[]{"_id","oftenusecity"};
				mydbhelper dbh_ofenusecity = new mydbhelper(getActivity());
				SQLiteDatabase sdb = dbh_ofenusecity.getReadableDatabase();
				Cursor cr = sdb.query(mydbhelper.TABLE_NAME_OFTENUSECITY, ofenUCity, null, null, null, null, null);
				if(cr != null)
				{
					if(cr.getCount() == 0)
					{
						oftenUseCity_name = "null";
					}
				}
				while(cr.moveToNext())
				{
					oftenUseCity_name = cr.getString(1);
					HashMap<String,String> map_2 = new HashMap<String,String>();
					map_2.put("ofenusecity", oftenUseCity_name);
					oftenCityList.add(map_2);
					System.out.println(oftenUseCity_name + "QQQQQQQQQQQQQQQQQqq");
					
				}
				SimpleAdapter oftenUseCityAdapter = new SimpleAdapter(getActivity(), oftenCityList, R.layout.citymger_ousecity,
						new String[]{"ofenusecity"}, new int[]{R.id.tv_citymger_oftenusecity});
				lv.setAdapter(oftenUseCityAdapter);
				
				//////����л�����///////
				AdapterView.OnItemClickListener changeCityWeather = new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position,
							long arg3) {
						// TODO Auto-generated method stub
						TextView tv_showNoteTitle =(TextView) view.findViewById(R.id.tv_citymger_oftenusecity);
						changeCityName = tv_showNoteTitle.getText().toString();
						System.out.println(changeCityName);
						String changeCityCode = "";
						mydbhelper dbh = new mydbhelper(getActivity());
						SQLiteDatabase sdb = dbh.getReadableDatabase();
						String[] table = new String[]{"_id", "city","cityid"};
						Cursor cr = sdb.query(mydbhelper.TABLE_NAME_CITY, table, "city=?", new String[]{changeCityName}, null, null, null);
						if(cr != null)
						{
							if(cr.getCount() == 0)
							{
								changeCityCode="null";
							}
						}
						while(cr.moveToNext())
						{
							changeCityCode = cr.getString(2);
						}
					
						System.out.println(changeCityCode + "changeCityCodeQQQQQQQQQQQQQQQQQQqq");
						Toast.makeText(getActivity(), "������ӳɹ�", 1).show();
						mainCityCode = changeCityCode;
						ContentValues cv2 = new ContentValues();
						cv2.put("citycode", mainCityCode);
						sdb.update(mydbhelper.TABLE_NAME_CITYCODE, cv2, "_id=?", new String[]{"1"});
					}
				};
				lv.setOnItemClickListener(changeCityWeather); //���ó�������������
			}
			
			
			/////////////////////���й���///////////////////////////////////
			
			if (getArguments().getInt(ARG_WEAHTER_NUMBER) == 3) {
				System.out.println("333333333333333333333");
				rootView = inflater.inflate(R.layout.citymanager,
						container, false);
				mydbhelper dbh = new mydbhelper(getActivity());
				SQLiteDatabase sdb = dbh.getReadableDatabase();
				////���ų���
				ListView lv;
				ListView lv_search;
				lv_search = (ListView) rootView.findViewById(R.id.lv_showSearchCity);
				lv = (ListView) rootView.findViewById(R.id.lv_HotCity);
				final List<String> list;
				list = new ArrayList<String>();
				list.add("����");
				list.add("�Ϻ�");
				list.add("����");
				list.add("����");
				list.add("ʯ��ׯ");
				list.add("����");
				list.add("����");
				list.add("���");
				list.add("����");
				list.add("�½�");
				ArrayAdapter<String> adapter1 = 
		        		new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
				lv.setAdapter(adapter1);
				//�����ӳ���
				
				AdapterView.OnItemClickListener addCityWeather = new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position,
							long arg3) {
						// TODO Auto-generated method stub
						String cityname = (String)list.get(position);
						mydbhelper dbh = new mydbhelper(getActivity());
						SQLiteDatabase sdb = dbh.getReadableDatabase();
						String[] table = new String[]{"_id","oftenusecity"};
						Cursor cr_search = sdb.query(mydbhelper.TABLE_NAME_OFTENUSECITY, table, "oftenusecity=?", new String[]{cityname}, null, null, null);
						if(cr_search.getCount()==0)
						{
							ContentValues cv = new ContentValues();
							cv.put("oftenusecity", cityname);
							sdb.insert(mydbhelper.TABLE_NAME_OFTENUSECITY, null, cv);
							Toast.makeText(getActivity(), "���ų�����ӳɹ�", 1).show();
						}
						else
						{
							Toast.makeText(getActivity(), "�����Ѵ���", 1).show();
						}
						
					}
				};
				lv.setOnItemClickListener(addCityWeather);
				
				//��ѯ����
				final EditText et_search = (EditText) rootView.findViewById(R.id.et_searchCity);
				final ListView hotCity = (ListView) rootView.findViewById(R.id.lv_HotCity);
				
				final ListView showSearchCity = (ListView) rootView.findViewById(R.id.lv_showSearchCity);
				final TextView tv_hotCity = (TextView) rootView.findViewById(R.id.tv_allCity);
				et_search.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						temp = arg0;
					}
					
					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
							int arg3) {
						// TODO Auto-generated method stub
						
					}
					private CharSequence temp; 
					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
						citysearch = new ArrayList<HashMap<String,Object>>();
						if ( temp.length()!=0){
							mydbhelper dbh_city=new mydbhelper(getActivity());
							SQLiteDatabase sdb_city=dbh_city.getReadableDatabase();
							
							String[]  project_city = new String[]{"_id","city","cityid"};
							Cursor cur_city1 = sdb_city.query(mydbhelper.TABLE_NAME_CITY, project_city, "city like ?", new String[]{"%"+temp+"%"}, null, null, null);							
							while (cur_city1.moveToNext()){
								HashMap<String,Object> map = new HashMap<String,Object>();
					        	map.put("city", cur_city1.getString(1));
					        	citysearch.add(map);
							}
							//����list tv �ؼ�
							hotCity.setVisibility(View.INVISIBLE);
						
							tv_hotCity.setVisibility(View.INVISIBLE);
							
							
						}else{
							hotCity.setVisibility(View.VISIBLE);
							
							tv_hotCity.setVisibility(View.VISIBLE);
							
						}	
						 SimpleAdapter adapter_search = new SimpleAdapter(getActivity(),citysearch,R.layout.citysearchlistitem,
					        		new String[]{"city"},new int[]{R.id.tv_searchCityListItem});
					        	
						 showSearchCity.setAdapter(adapter_search);
					}
				});
				
				//������������ĳ��У���ӵ����ó�����
				AdapterView.OnItemClickListener addSearchCity = new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position,
							long arg3) {
						// TODO Auto-generated method stub
						String searchCityName = "";
						TextView tv1_1 = (TextView) view.findViewById(R.id.tv_searchCityListItem);
						searchCityName = tv1_1.getText().toString();
						mydbhelper dbh_search = new mydbhelper(getActivity());
						SQLiteDatabase sdb_search = dbh_search.getReadableDatabase();
						ContentValues cv_search = new ContentValues();
						cv_search.put("oftenusecity", searchCityName);
						sdb_search.insert(mydbhelper.TABLE_NAME_OFTENUSECITY, null, cv_search);
						Toast.makeText(getActivity(), "������ӳɹ�", 1).show();
					}};
					lv_search.setOnItemClickListener(addSearchCity);
				
			}
			if (getArguments().getInt(ARG_WEAHTER_NUMBER) == 4) {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return rootView;

		}//onCreateView
		
		///////////////////��������󴥷��¼�/////////////////////////////////////
		
		public void onTouch(Cell cell) {
			
			
			if(cell.mPaint.getColor() == Color.GRAY) {
				// �������µ�
				mView.previousMonth(); 
				cen_text.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
			} else if(cell.mPaint.getColor() == Color.LTGRAY) {
				// ���µ�
				mView.nextMonth(); 
				cen_text.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
			} else {  //  ���µ�
				
					Intent ret = new Intent(getActivity(),OutPlan.class);
					Bundle bu = new Bundle();
					String date = mView.getYear() +"-"+(mView.getMonth()+1)+"-"+ cell.getDayOfMonth();
					bu.putString("date", date);
					bu.putInt("Year",mView.getYear());
					bu.putInt("Month",mView.getMonth()+1);
					bu.putInt("Day",cell.getDayOfMonth());
					ret.putExtras(bu);
					 
					System.out.println("��ǰ���ڣ�" + mView.getYear() + "-" + (mView.getMonth()+1) + "-" + cell.getDayOfMonth());
					// �ڴ��õ�ǰ��View �ػ�һ��
					ecBounds = cell.getBound();
					mView.getDate();
					mView.mDecoraClick.setBounds(ecBounds);

					startActivity(ret);
					return;
				}
		}//onTouch
		
		private void subThread() {
			// TODO Auto-generated method stub
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//���ȴ�TABLE_NAME_CITYCODE���л�ȡĬ�ϳ���(idΪ1)���
					mydbhelper dbh = new mydbhelper(getActivity());
					SQLiteDatabase sdb = dbh.getReadableDatabase();
					String cityCode = "";
					String[] table = new String[]{"_id","citycode"};
					Cursor cr = sdb.query(mydbhelper.TABLE_NAME_CITYCODE, table, "_id=?", new String[]{"1"}, null, null, null);
					while(cr.moveToNext()){
						String mainCityCode = cr.getString(1);
						cityCode = mainCityCode;
						System.out.println("mainCityCode    " + mainCityCode);
					}
					//�õ���ѯapi
					String weatherUrl = "http://m.weather.com.cn/data/"+cityCode+".html";
					//���������message��ֵ
					String wgsoninfo = connWeb(weatherUrl);

					Message msg = new Message();
					msg.obj = wgsoninfo;
					whandler.sendMessage(msg);
				}// run.
			}// Runnable.
			).start();
		}// method.

		
		Handler whandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				System.out.println("handleMessage..");
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				msggmainfo = (String) msg.obj;
				System.out.println("main Thread:" + msggmainfo);

				clist = getGsonList(msggmainfo);

				//����ȡֵ
				Iterator it = clist.iterator();
				if (it.hasNext()) { 
					CWeather cw = (CWeather) it.next();
					city = cw.getCity();
					temp1 = cw.getTemp1();
					temp2 = cw.getTemp2();
					temp3 = cw.getTemp3();
					temp4 = cw.getTemp4();
					weather1 = cw.getWeather1();
					weather2 = cw.getWeather2();
					weather3 = cw.getWeather3();
					weather4 = cw.getWeather4();
					img_title1 = cw.getImg_title1();
					img_title2 = cw.getImg_title2();
					img_title3 = cw.getImg_title3();
					img_title4 = cw.getImg_title4();
					wind1 = cw.getWind1();
					index_uv = cw.getIndex_uv();
					index_d = cw.getIndex_d();
					fchh = cw.getFchh();
					 
				}// if.
				//��ȡ�õ�ֵ��ʾ��ҳ��
				//Ϊ������������
				Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"fonts/1.ttf");   
				tv_city_name.setTypeface(face);
				tv_city_name.setText(city);
				//Ϊ�¶���������
				Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/jetmix.ttf");   
				tv_city_temp1.setTypeface(face1);
				tv_city_temp2.setTypeface(face1);
				tv_city_temp3.setTypeface(face1);
				tv_city_temp4.setTypeface(face1);
				tv_city_temp1_lower.setTypeface(face1);
				//����õ��������ַ���
				s = temp1.split("~");
				higher_temp1 = s[0];
				lower_temp1 = s[1];
				xianshi = higher_temp1+"/"+lower_temp1;
				tv_city_temp1.setText(higher_temp1);
				tv_city_temp1_lower.setText(lower_temp1);
				
				s2 = temp2.split("~");
				higher_temp2 = s2[0];
				lower_temp2 = s2[1];
				xianshi2 = higher_temp2+"/"+lower_temp2;
				tv_city_temp2.setText(xianshi2);
				
				s3 = temp3.split("~");
				higher_temp3 = s3[0];
				lower_temp3 = s3[1];
				xianshi3 = higher_temp3+"/"+lower_temp3;
				tv_city_temp3.setText(xianshi3);
				
				s4 = temp4.split("~");
				higher_temp4 = s4[0];
				lower_temp4 = s4[1];
				xianshi4 = higher_temp4+"/"+lower_temp4;
				tv_city_temp4.setText(xianshi4);
				//��������(��ǰֻ��������������)
				tv_city_wind1.setTypeface(face);
				tv_city_wind1.setText(wind1);
				tv_city_weather1.setTypeface(face);
				tv_city_weather1.setText(weather1);
				tv_city_fchh.setTypeface(face);
				tv_city_fchh.setText(fchh+"ʱ");
				tv_city_ziwaixian.setTypeface(face);
				tv_city_ziwaixian.setText(index_uv);
				tv_city_index_d.setTypeface(face);
				tv_city_index_d.setText(index_d);
				
				//��ʾ����ͼ��(�˴���������ͼ��title����)
				System.out.println("�õ���������ʾ��img_title1��" + img_title1);
				if("��".equals(img_title1)){
					img_city_temp1.setImageResource(R.drawable.sunny);
				}
				if("����".equals(img_title1)){
					img_city_temp1.setImageResource(R.drawable.yin);
				}
				if("��".equals(img_title1)){
					img_city_temp1.setImageResource(R.drawable.yin);
				}
				if("С��".equals(img_title1)){
					img_city_temp1.setImageResource(R.drawable.rainy);
				}
				//���������ı䱳��(�д�����)
				if("��".equals(weather1)){
					Resources resources = getActivity().getBaseContext().getResources(); 
					Drawable d = resources.getDrawable(R.drawable.red); 
					temp.setBackgroundDrawable(d);
				}
				if("С��ת����".equals(weather1)){
					Resources resources = getActivity().getBaseContext().getResources(); 
					Drawable d = resources.getDrawable(R.drawable.xiaoyuzhuanduoyun); 
					temp.setBackgroundDrawable(d);
				}
				if("����".equals(weather1)){
					Resources resources = getActivity().getBaseContext().getResources(); 
					Drawable d = resources.getDrawable(R.drawable.weatherbackgroud); 
					temp.setBackgroundDrawable(d);
				}
				if("С��".equals(weather1)){
					Resources resources = getActivity().getBaseContext().getResources(); 
					Drawable d = resources.getDrawable(R.drawable.xiaoyu); 
					temp.setBackgroundDrawable(d);
				}
				if("����ת��".equals(weather1)){
					Resources resources = getActivity().getBaseContext().getResources(); 
					Drawable d = resources.getDrawable(R.drawable.duoyunzhuanqing); 
					temp.setBackgroundDrawable(d);
				}
			}// handlerMessage.
		};// whandler.

		
		private String connWeb(String url) {
			String str = "";
			try {
				System.out.println("connWeb...");
				HttpGet request = new HttpGet(url);
				//�������磬���������
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(request);
				//��״̬��Ϊ200ʱ�õ���Ӧֵ
				if (response.getStatusLine().getStatusCode() == 200) {
					str = EntityUtils.toString(response.getEntity());
					System.out.println("response str:" + str);
				}// if.
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str;
		}// connWeb.

		//��ȡgson�б�ֵ
		public List<CWeather> getGsonList(String msg) {
			List<CWeather> mlist = new ArrayList<CWeather>();

			try {
				JSONObject job = new JSONObject(msg);
				JSONObject wjob = job.getJSONObject("weatherinfo");
				CWeather cw = new CWeather();

				//��ȡ����ֵ���������CWeather������
				cw.setCityid(wjob.getString("cityid"));
				cw.setCity(wjob.getString("city"));
				cw.setTemp1(wjob.getString("temp1"));
				cw.setTemp2(wjob.getString("temp2"));
				cw.setTemp3(wjob.getString("temp3"));
				cw.setTemp4(wjob.getString("temp4"));
				cw.setWeather1(wjob.getString("weather1"));
				cw.setWeather2(wjob.getString("weather2"));
				cw.setWeather3(wjob.getString("weather3"));
				cw.setWeather4(wjob.getString("weather4"));
				cw.setImg_title1(wjob.getString("img_title1"));
				cw.setImg_title2(wjob.getString("img_title2"));
				cw.setImg_title3(wjob.getString("img_title3"));
				cw.setImg_title4(wjob.getString("img_title4"));
				cw.setWind1(wjob.getString("wind1"));
				cw.setWind2(wjob.getString("wind2"));
				cw.setWind3(wjob.getString("wind3"));
				cw.setWind4(wjob.getString("wind4"));
				cw.setDate_y(wjob.getString("date_y"));
				cw.setIndex_uv(wjob.getString("index_uv"));
				cw.setIndex_d(wjob.getString("index_d"));
				cw.setSt2(wjob.getString("st2"));
				cw.setSt3(wjob.getString("st3"));
				cw.setSt4(wjob.getString("st4"));
				cw.setFl1(wjob.getString("fl1"));
				cw.setFchh(wjob.getString("fchh"));

				mlist.add(cw);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return mlist;
		}// method
		
	}
}