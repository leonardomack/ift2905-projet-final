package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperNobelPrize extends SQLiteOpenHelper{

	static final int VERSION=3;
	
	static final String TAG = "DBHelperNobelPrize";

	//Table statistic
	static final String TABLE = "statistic";
	
	static final String C_USERNAME_STAT = "username";
	static final String C_SCOREGAME1_STAT = "score1";
	static final String C_GAMEPLAYED1_STAT = "total1";	
	static final String C_SCOREGAME2_STAT = "score2";
	static final String C_GAMEPLAYED2_STAT = "total2";
	static final String C_SCOREGAME3_STAT = "score3";
	static final String C_GAMEPLAYED3_STAT = "total3";
	
	//Table trophy
	static final String TABLE2 = "trophy";
	
	static final String C_USERNAME_TROP = "username";
	static final String C_TROPHY_NOTIPS_TROP = "trophy1";
	static final String C_TROPHY_3CONSECUTIVE_TROP = "trophy2";
	static final String C_TROPHY_USEATIP_TROP = "trophy3";
	static final String C_TROPHY_5TRUEFROMEVERYGAME_TROP = "trophy4";
	static final String C_TROPHY_MYNOBELPRIZE_TROP = "trophy5";
	
	
	Context context;
	
	public DBHelperNobelPrize(Context context) {
		super(context, "statistic.db", null, VERSION);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Création BDD");
		
		String sql = "create table " + TABLE + " ("
				+ C_USERNAME_STAT +" text primary key, "
				+ C_SCOREGAME1_STAT + " integer DEFAULT 0,"
				+ C_GAMEPLAYED1_STAT + " integer DEFAULT 0,"
				+ C_SCOREGAME2_STAT + " integer DEFAULT 0,"
				+ C_GAMEPLAYED2_STAT + " integer DEFAULT 0,"
				+ C_SCOREGAME3_STAT + " integer DEFAULT 0,"
				+ C_GAMEPLAYED3_STAT + " integer DEFAULT 0 )";
		db.execSQL(sql);
		
		String sql2 = "create table " + TABLE2 + " ("
				+ C_USERNAME_TROP +" text primary key, "
				+ C_TROPHY_USEATIP_TROP + " integer DEFAULT 0,"
				+ C_TROPHY_NOTIPS_TROP + " integer DEFAULT 0,"
				+ C_TROPHY_3CONSECUTIVE_TROP + " integer DEFAULT 0,"
				+ C_TROPHY_5TRUEFROMEVERYGAME_TROP + " integer DEFAULT 0,"
				+ C_TROPHY_MYNOBELPRIZE_TROP + " integer DEFAULT 0 )";
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Mise à jour BDD");
		db.execSQL("drop table if exists "+TABLE);
		db.execSQL("drop table if exists "+TABLE2);
		onCreate(db);
	}
	
	/*
	 * Write methods
	 */
	public void createNewPlayer(String username){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.clear();
		val.put(C_USERNAME_STAT,username);
		try {
			db.insertOrThrow(TABLE, null,val);
			db.insertOrThrow(TABLE2, null, val);
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
		db.update(TABLE, val, C_USERNAME_STAT+"='"+username+"'", null);
		db.close();		
	}
	public void modifyScoreTrueFalseGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME1_STAT, score);
	}
	public void modifyTotalTrueFalseGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED1_STAT, total);
	}
	public void modifyScoreQCMGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME2_STAT, score);
	}
	public void modifyTotalQCMGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED2_STAT, total);
	}
	public void modifyScorePictureGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME3_STAT, score);
	}
	public void modifyTotalPictureGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED3_STAT, total);
	}
	public void activateTrophyNoTips(String username){
		modifySingleItemFromDB(username, C_TROPHY_NOTIPS_TROP, 1);
	}
	public void activateTrophyUseATips(String username){
		modifySingleItemFromDB(username, C_TROPHY_USEATIP_TROP, 1);
	}
	public void activateTrophy3Consecutive(String username){
		modifySingleItemFromDB(username, C_TROPHY_3CONSECUTIVE_TROP, 1);
	}
	public void activateTrophy5TrueFromEveryGame(String username){
		modifySingleItemFromDB(username, C_TROPHY_5TRUEFROMEVERYGAME_TROP, 1);
	}
	public void activateTrophyMyNobelPrize(String username){
		modifySingleItemFromDB(username, C_TROPHY_MYNOBELPRIZE_TROP, 1);
	}
	
	/*
	 * Read methods
	 */
	public int getSingleItemFromDB(String username, String column, String table){
		int itemRetrieved = -1;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(table, new String[] {column}, C_USERNAME_STAT+"='"+username+"'", null, null, null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			itemRetrieved=c.getInt(0);
		}
		db.close();
		return itemRetrieved;
	}
	public int getScoreOfTrueFalseGame(String username){
		int score = -1;
		score = getSingleItemFromDB(username, C_SCOREGAME1_STAT, TABLE);
		return score;
	}
	public int getTotalOfTrueFalseGame(String username){
		int total = -1;
		total = getSingleItemFromDB(username, C_GAMEPLAYED1_STAT, TABLE);
		return total;
	}
	public int getScoreOfQCMGame(String username){
		int score = -1;
		score = getSingleItemFromDB(username, C_SCOREGAME2_STAT, TABLE);
		return score;
	}
	public int getTotalOfQCMGame(String username){
		int total = -1;
		total = getSingleItemFromDB(username, C_GAMEPLAYED2_STAT, TABLE);
		return total;
	}
	public int getScoreOfPictureGame(String username){
		int score = -1;
		score = getSingleItemFromDB(username, C_SCOREGAME3_STAT, TABLE);
		return score;
	}
	public int getTotalOfPictureGame(String username){
		int total = -1;
		total = getSingleItemFromDB(username, C_GAMEPLAYED3_STAT, TABLE);
		return total;
	}
	public boolean playerExists(String username){
		boolean exists = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE, new String[] {C_USERNAME_STAT}, C_USERNAME_STAT+"='"+username+"'", null, null, null, null);
		exists = c.getCount()>0;
		db.close();
		return exists;
	}
	public boolean hasTrophyNoTips(String username){
		boolean hasIt = false;
		int dbValue = 0;
		dbValue = getSingleItemFromDB(username, C_TROPHY_NOTIPS_TROP, TABLE2);
		if(dbValue==1)
			hasIt=true;
		return hasIt;
	}
	public boolean hasTrophyUseTips(String username){
		boolean hasIt = false;
		int dbValue = 0;
		dbValue = getSingleItemFromDB(username, C_TROPHY_USEATIP_TROP, TABLE2);
		if(dbValue==1)
			hasIt=true;
		return hasIt;
	}
	public boolean hasTrophy3Consecutive(String username){
		boolean hasIt = false;
		int dbValue = 0;
		dbValue = getSingleItemFromDB(username, C_TROPHY_3CONSECUTIVE_TROP, TABLE2);
		if(dbValue==1)
			hasIt=true;
		return hasIt;
	}
	public boolean hasTrophy5TrueFromEveryGame(String username){
		boolean hasIt = false;
		int dbValue = 0;
		dbValue = getSingleItemFromDB(username, C_TROPHY_5TRUEFROMEVERYGAME_TROP, TABLE2);
		if(dbValue==1)
			hasIt=true;
		return hasIt;
	}
	public boolean hasTrophyMyNobelPrize(String username){
		boolean hasIt = false;
		int dbValue = 0;
		dbValue = getSingleItemFromDB(username, C_TROPHY_MYNOBELPRIZE_TROP, TABLE2);
		if(dbValue==1)
			hasIt=true;
		return hasIt;
	}
}
