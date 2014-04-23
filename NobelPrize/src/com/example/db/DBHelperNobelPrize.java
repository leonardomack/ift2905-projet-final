package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperNobelPrize extends SQLiteOpenHelper{

	static final int VERSION=2;

	static final String TABLE = "statistic";
	
	static final String C_USERNAME = "username";
	static final String C_SCOREGAME1 = "score1";
	static final String C_GAMEPLAYED1 = "total1";	
	static final String C_SCOREGAME2 = "score2";
	static final String C_GAMEPLAYED2 = "total2";
	static final String C_SCOREGAME3 = "score3";
	static final String C_GAMEPLAYED3 = "total3";
	static final String TAG = "DBHelperNobelPrize";

	Context context;
	
	public DBHelperNobelPrize(Context context) {
		super(context, "statistic.db", null, VERSION);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Création BDD");
		String sql = "create table " + TABLE + " ("
				+ C_USERNAME +" text primary key, "
				+ C_SCOREGAME1 + " integer DEFAULT 0,"
				+ C_GAMEPLAYED1 + " integer DEFAULT 0,"
				+ C_SCOREGAME2 + " integer DEFAULT 0,"
				+ C_GAMEPLAYED2 + " integer DEFAULT 0,"
				+ C_SCOREGAME3 + " integer DEFAULT 0,"
				+ C_GAMEPLAYED3 + " integer DEFAULT 0 )";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Mise à jour BDD");
		db.execSQL("drop table if exists "+TABLE);
		onCreate(db);
	}
	
	/*
	 * Write methods
	 */
	public void createNewPlayer(String username){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.clear();
		val.put(C_USERNAME,username);
		try {
			db.insertOrThrow(TABLE, null,val);
		} catch ( SQLException e ) {
			Log.d(TAG, "Erreur BDD: " + e.getMessage());
		}
		db.close();		
	}
	public void modifySingleItemFromDB(String username, String column, int value){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.clear();
		val.put(column, value);
		db.update(TABLE, val, C_USERNAME+"='"+username+"'", null);
		db.close();		
	}
	public void modifyScoreTrueFalseGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME1, score);
	}
	public void modifyTotalTrueFalseGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED1, total);
	}
	public void modifyScoreQCMGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME2, score);
	}
	public void modifyTotalQCMGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED2, total);
	}
	public void modifyScorePictureGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME3, score);
	}
	public void modifyTotalPictureGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED3, total);
	}
	
	/*
	 * Read methods
	 */
	public int getSingleItemFromDB(String username, String column){
		int itemRetrieved = -1;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE, new String[] {column}, C_USERNAME+"='"+username+"'", null, null, null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			itemRetrieved=c.getInt(0);
		}
		db.close();
		return itemRetrieved;
	}
	public int getScoreOfTrueFalseGame(String username){
		int score = -1;
		score = getSingleItemFromDB(username, C_SCOREGAME1);
		return score;
	}
	public int getTotalOfTrueFalseGame(String username){
		int total = -1;
		total = getSingleItemFromDB(username, C_GAMEPLAYED1);
		return total;
	}
	public int getScoreOfQCMGame(String username){
		int score = -1;
		score = getSingleItemFromDB(username, C_SCOREGAME2);
		return score;
	}
	public int getTotalOfQCMGame(String username){
		int total = -1;
		total = getSingleItemFromDB(username, C_GAMEPLAYED2);
		return total;
	}
	public int getScoreOfPictureGame(String username){
		int score = -1;
		score = getSingleItemFromDB(username, C_SCOREGAME3);
		return score;
	}
	public int getTotalOfPictureGame(String username){
		int total = -1;
		total = getSingleItemFromDB(username, C_GAMEPLAYED3);
		return total;
	}
	public boolean playerExists(String username){
		boolean exists = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE, new String[] {C_USERNAME}, C_USERNAME+"='"+username+"'", null, null, null, null);
		exists = c.getCount()>0;
		db.close();
		return exists;
	}
}
