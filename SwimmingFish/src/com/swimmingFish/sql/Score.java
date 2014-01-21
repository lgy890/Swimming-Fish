package com.swimmingFish.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Record the score in the database
 */
public class Score {

	private final Context context;
	SQLiteDatabase Db;
	DbHelper dbHelper;
	public static final String key_id = "_id";
	public static final String key_name = "name";
	public static final String key_score = "score";
	public static final String key_distance = "distance";
	private static final String DATABASE_NAME = "db_UserInfo";
	private static final String DATATABLE_NAME = "scoreData";
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_CREATE = "create table scoreData (_id integer primary key autoincrement,"
			+ "name text not null,score integer not null,distance integer not null);";

	/**
	 * Inner class to realize the help database class
	 */
	private static class DbHelper extends SQLiteOpenHelper {
		DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS scoreData");
			onCreate(db);
		}
	}

	public Score(Context context) {
		this.context = context;
	}

	public Score open() throws SQLException {
		dbHelper = new DbHelper(context);
		Db = dbHelper.getWritableDatabase();
		return this;
	}

	public void closeDB() {
		dbHelper.close();
	}

	public void deleteTable() {
		Db.execSQL("DROP TABLE scoreData");
	}

	// Use ContentValues and insert method to add data in the database
	public long createRecord(String name, int score, int distance) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(key_name, name);
		contentValues.put(key_score, score);
		contentValues.put(key_distance, distance);
		return Db.insert(DATATABLE_NAME, null, contentValues);
	}

	public boolean updateDiary(String name, int score, int distance, long rowId) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(key_name, name);
		contentValues.put(key_score, score);
		contentValues.put(key_distance, distance);
		return Db.update(DATATABLE_NAME, contentValues, key_id + "=" + rowId,
				null) > 0;

	}

	public boolean deleteRecord(long rowId) {
		return Db.delete(DATATABLE_NAME, key_id + "=" + rowId, null) > 0;
	}

	public Cursor getAllRecord() {
		return Db.query(DATATABLE_NAME, new String[] { key_id, key_name,
				key_score, key_distance }, null, null, null, null, key_score
				+ " desc");
	}
}
