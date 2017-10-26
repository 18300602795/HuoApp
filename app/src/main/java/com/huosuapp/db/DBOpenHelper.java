package com.huosuapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.huosuapp.download.Download;
import com.huosuapp.download.SearchInfo;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "ttw.db";
	private static final int VERSION = 1;
	
	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
	    //图片导航信息
//		db.execSQL("CREATE TABLE IF NOT EXISTS "+ImgNav.TABLENAME+"(id integer primary key autoincrement,"+ImgNav.IMAGEURL+" varchar(100), "+ImgNav.GAMEID+" INTEGER)");
//	    //首页item信息
//		db.execSQL("CREATE TABLE IF NOT EXISTS "+GameItem.TABLENAME+"(id integer primary key autoincrement, "+
//	    GameItem.ANDROIDURL+" varchar(100),"+GameItem.COUNT+" integer,"
//		+ GameItem.GAMEID +" integer,"+GameItem.IMAGE+" varchar(100),"+GameItem.NAME+" varchar(30),"
//	    + GameItem.SIZE+" varchar(40),"+GameItem.TYPE+" integer)");

		db.execSQL("CREATE TABLE IF NOT EXISTS "+ Download.TABLE_NAME+"(id integer primary key autoincrement, "+
				Download.BM_URL+" varchar(100),"+Download.CURRENT_SIZE+" integer,"
		+ Download.TOTAL_SIZE +" integer,"+Download.SPEED+" varchar(30),"+Download.BAOMING+" varchar(30),"
	    + Download.ULR+" varchar(40),"+Download.ISDOWNCOMPLETE+" boolean,"+Download.APKNAME+" varchar(50),"+Download.NAME+" varchar(50) )");
	
		//搜索关键字保存
	    db.execSQL("CREATE TABLE IF NOT EXISTS "+ SearchInfo.TABLE_NAME+"(id integer primary key autoincrement,"+SearchInfo.COLUMN_GAME_NAME+" varchar(50) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS filedownlog");
		db.execSQL("DROP TABLE IF EXISTS "+Download.TABLE_NAME);
		onCreate(db);
	}

}
