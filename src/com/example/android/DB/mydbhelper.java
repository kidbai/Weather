package com.example.android.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class mydbhelper extends SQLiteOpenHelper{


	public static final int VERSION=24;
	public static final String TABLE_NAME_CITY="t_city";
	public static final String TABLE_NAME_OFTENUSECITY="t_oftenusecity";
	public static final String TABLE_NAME_CITYCODE="t_citycode";


	public static final String DATABASE_NAME="city.db";
	
	public mydbhelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String str_sql_t_city="create table "+TABLE_NAME_CITY
				+"(_id integer primary key autoincrement,city varchar,cityid varchar)";
		String str_sql_t_oftenusecity="create table "+TABLE_NAME_OFTENUSECITY
				+"(_id integer primary key autoincrement,oftenusecity varchar)";
		String str_sql_t_citycode="create table "+TABLE_NAME_CITYCODE
				+"(_id integer primary key autoincrement,citycode varchar)";
		db.execSQL(str_sql_t_city);
		db.execSQL(str_sql_t_oftenusecity);
		db.execSQL(str_sql_t_citycode);
			}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVerson, int newVerson) {
		// TODO Auto-generated method stub
		System.out.println("onUpgrade·½·¨Ö´ÐÐ");
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_CITY);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_OFTENUSECITY);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_CITYCODE);
		onCreate(db);
	}

}
