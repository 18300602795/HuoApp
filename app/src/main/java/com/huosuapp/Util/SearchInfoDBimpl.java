package com.huosuapp.Util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.huosuapp.db.DBOpenHelper;
import com.huosuapp.download.SearchInfo;

/**
 * author janecer 2014年5月27日上午10:36:23
 */
public class SearchInfoDBimpl {

	private DBOpenHelper dbhepler;
	private static SearchInfoDBimpl searchinfoimpl;

	private SearchInfoDBimpl(Context context) {
		if (null == dbhepler) {
			dbhepler = new DBOpenHelper(context);
		}
	}

	public static SearchInfoDBimpl getInstance(Context context) {
		if (null == searchinfoimpl) {
			searchinfoimpl = new SearchInfoDBimpl(context);
		}
		return searchinfoimpl;
	}

	/**
	 * 保存searchinfo信息到数据库中
	 * 
	 * @param
	 */
	public void savaSearchInfo(String gamename) {
		if (null == gamename) {
			return;
		}
		if(isNameExits(gamename)){
			return;
		}
		SQLiteDatabase db = dbhepler.getWritableDatabase();
		if (db.isOpen()) {
			try {
				db.beginTransaction();
				db.execSQL("insert into " + SearchInfo.TABLE_NAME + "("
						+ SearchInfo.COLUMN_GAME_NAME + ")  values(?)",
						new String[] { gamename });
				db.setTransactionSuccessful();
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				db = null;
			}
		}
	}

	/**
	 * 获取所有的searchinfo集合
	 * 
	 * @return
	 */
	public List<String> getSearchinfos() {
		List<String> searchinfos = new ArrayList<String>();
		SQLiteDatabase db = dbhepler.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = null;
			try {
				cursor = db.rawQuery("select * from " + SearchInfo.TABLE_NAME,
						null);
				// SearchInfo searchinfo=null;
				cursor.moveToLast();
				searchinfos.add(cursor.getString(cursor
						.getColumnIndex(SearchInfo.COLUMN_GAME_NAME)));
				while (cursor.moveToPrevious()) {
					// searchinfo=new SearchInfo();
					// searchinfo.game_name=cursor.getString(cursor.getColumnIndex(SearchInfo.COLUMN_GAME_NAME));
					// searchinfo.gameid=cursor.getString(cursor.getColumnIndex(SearchInfo.COLUMN_GAMEID));
					searchinfos.add(cursor.getString(cursor
							.getColumnIndex(SearchInfo.COLUMN_GAME_NAME)));
					// searchinfo=null;
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} catch (CursorIndexOutOfBoundsException e) {
				e.printStackTrace();
			} finally {
				if (null != cursor) {
					cursor.close();
					cursor = null;
				}
				db.close();
				db = null;
			}
		}
		return searchinfos;
	}

	/**
	 * 根据gameid删除一条信息
	 *
	 *            返回true表示删除成功，false表示删除失败
	 */
	public boolean deleteSearchinfoByGameid(String game_name) {
		boolean flag = false;
		if (game_name == null) {
			return flag;
		}
		SQLiteDatabase db = dbhepler.getWritableDatabase();
		if (db.isOpen()) {
			try {
				db.beginTransaction();
				db.execSQL("delete from " + SearchInfo.TABLE_NAME + " where "
						+ SearchInfo.COLUMN_GAME_NAME + "=?",
						new String[] { game_name });
				db.setTransactionSuccessful();
				flag = true;
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				db = null;
			}
		}
		return flag;
	}

	public boolean isNameExits(String name) {
		boolean flag = false;
		SQLiteDatabase db = dbhepler.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = null;
			try {
				cursor = db.rawQuery("select * from " + SearchInfo.TABLE_NAME
						+ " where " + SearchInfo.COLUMN_GAME_NAME + " =?",
						new String[] { name });
				if (cursor.moveToNext()) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(null!=cursor){
					cursor.close();
				}
			}
		}
		return flag;
	}
}
