package com.exina.android.dbhelper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	public static final int VERSION=1;
	public static final String TABLE_NAME="plans";
	public static final String DATABASE_NAME="scu.db";
	public DBHelper(Context context){
    	super(context,DATABASE_NAME,null,VERSION);
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		 String str_sql="create table "+TABLE_NAME
				   +"(uid integer primary key autoincrement," +
				   "title varchar," +
				   "level varchar," +
				   "site varchar," +
				   "plan_date varchar," +
				   "remind_time varchar,"+
				   "current_date varchar)";
		   db.execSQL(str_sql);
		
	}
	public ArrayList getAllPlans(SQLiteDatabase sdb,String date){
		ArrayList list=new ArrayList();
		Cursor cursor = null;
		String str = "Select * from "+TABLE_NAME+" where "+TABLE_NAME+ ".current_date='"+date+"'";
		cursor = sdb.rawQuery(str, null);
		 while(cursor.moveToNext()){
				
				HashMap item = new HashMap();
				item.put("uid", cursor.getInt(cursor.getColumnIndex("uid")));
				item.put("title", cursor.getString(cursor.getColumnIndex("title")));
				item.put("level", cursor.getString(cursor.getColumnIndex("level")));
				item.put("site", cursor.getString(cursor.getColumnIndex("site")));
				item.put("plan_date", cursor.getString(cursor.getColumnIndex("plan_date")));
				item.put("remind_time", cursor.getString(cursor.getColumnIndex("remind_time")));
				item.put("current_date", cursor.getString(cursor.getColumnIndex("current_date")));
				list.add(item);
			  }
		return list;
	}
	/*删除选中的对象
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	 public void deleteMarked(ArrayList<Integer> deleteId,SQLiteDatabase sdb){
		 StringBuffer  strDeleteId = new StringBuffer();
		
		 strDeleteId.append("uid=");
		 for(int i=0;i<deleteId.size();i++) {
				if(i!=deleteId.size()-1) {
					strDeleteId.append(deleteId.get(i) + " or uid=");
				} else {
					strDeleteId.append(deleteId.get(i));
				}
			}
		 sdb.delete(TABLE_NAME, strDeleteId.toString(), null);
		
	 }//deleteMarked
	 /*查找某对象
	  * 
	  */
	 public ArrayList getPlans(String condition,SQLiteDatabase sdb,String date){
		 ArrayList list=new ArrayList();
		 String sql;
		 Cursor cursor;
	
		  sql = "select * from " + TABLE_NAME +  " where "+TABLE_NAME+".title like '%" + condition + "%' " +
					"or "+TABLE_NAME+ ".level like '%"+condition+"%'" + " and current_date='"+date+"'" ;
		 cursor =sdb.rawQuery(sql, null);
		 while(cursor.moveToNext()){
				
			 HashMap item = new HashMap();
				item.put("uid", cursor.getInt(cursor.getColumnIndex("uid")));
				item.put("title", cursor.getString(cursor.getColumnIndex("title")));
				item.put("level", cursor.getString(cursor.getColumnIndex("level")));
				item.put("site", cursor.getString(cursor.getColumnIndex("site")));
				item.put("plan_date", cursor.getString(cursor.getColumnIndex("plan_date")));
				item.put("remind_time", cursor.getString(cursor.getColumnIndex("remind_time")));
				item.put("current_date", cursor.getString(cursor.getColumnIndex("current_date")));
				list.add(item);
		 }
		
			return list;
			
		 
	 }//getPlans
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
