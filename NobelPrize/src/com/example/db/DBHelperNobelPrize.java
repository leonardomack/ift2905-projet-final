package com.example.db;

import com.example.nobelobjects.Achievements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperNobelPrize extends SQLiteOpenHelper{

	static final int VERSION=4;

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

	// pour savoir s'il a débloqué de nouveaux trophées recemment qu'il n'a pas consulté
	static final String C_HASNEWTROPHIES = "has";

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
				+ C_GAMEPLAYED3_STAT + " integer DEFAULT 0,"
				+ C_HASNEWTROPHIES + " integer DEFAULT 0 )";
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
		} catch ( SQLException e ) {
			Log.d(TAG, "Erreur BDD: " + e.getMessage());
		}
		db.close();
		createTrophies(username);
	}
	private void createTrophies(String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.clear();
		val.put(C_USERNAME_TROP,username);
		try {
			db.insertOrThrow(TABLE2, null, val);
		} catch ( SQLException e ) {
			Log.d(TAG, "Erreur BDD: " + e.getMessage());
		}
		db.close();	
	}
	public void modifySingleItemFromDB(String username, String column, int value, String table){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.clear();
		val.put(column, value);
		db.update(table, val, C_USERNAME_STAT+"='"+username+"'", null);
		db.close();		
	}
	public void modifyScoreTrueFalseGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME1_STAT, score, TABLE);
	}
	public void modifyTotalTrueFalseGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED1_STAT, total, TABLE);
	}
	public void modifyScoreQCMGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME2_STAT, score, TABLE);
	}
	public void modifyTotalQCMGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED2_STAT, total, TABLE);
	}
	public void modifyScorePictureGame(String username, int score){
		modifySingleItemFromDB(username, C_SCOREGAME3_STAT, score, TABLE);
	}
	public void modifyTotalPictureGame(String username, int total){
		modifySingleItemFromDB(username, C_GAMEPLAYED3_STAT, total, TABLE);
	}
	//mettre à 0 = hasnot ou 1 = has
	public void modifyHasNewTrophies(String username, int has){
		modifySingleItemFromDB(username, C_HASNEWTROPHIES, has, TABLE);
	}
	
	public void activateTrophyNoTips(String username){
		modifySingleItemFromDB(username, C_TROPHY_NOTIPS_TROP, 1, TABLE2);
	}
	public void activateTrophyUseATips(String username){
		modifySingleItemFromDB(username, C_TROPHY_USEATIP_TROP, 1, TABLE2);
	}
	public void activateTrophy3Consecutive(String username){
		modifySingleItemFromDB(username, C_TROPHY_3CONSECUTIVE_TROP, 1, TABLE2);
	}
	public void activateTrophy5TrueFromEveryGame(String username){
		modifySingleItemFromDB(username, C_TROPHY_5TRUEFROMEVERYGAME_TROP, 1, TABLE2);
	}
	public void activateTrophyMyNobelPrize(String username){
		modifySingleItemFromDB(username, C_TROPHY_MYNOBELPRIZE_TROP, 1, TABLE2);
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


	//mettre à 0 = hasnot ou 1 = has
	public int getHasNewTrophiesFromDB(String username){
		int has = 0;
		has = getSingleItemFromDB(username, C_HASNEWTROPHIES, TABLE);
		return has;
	}

	public boolean playerExists(String username){
		boolean exists = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE, new String[] {C_USERNAME_STAT}, C_USERNAME_STAT+"='"+username+"'", null, null, null, null);
		exists = c.getCount()>0;
		db.close();
		if(!trophiesExists(username))
			createTrophies(username);
		return exists;
	}

	public boolean trophiesExists(String username){
		boolean exists = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE2, new String[] {C_USERNAME_TROP}, C_USERNAME_TROP+"='"+username+"'", null, null, null, null);
		exists = c.getCount()>0;
		db.close();
		return exists;
	}
	public Achievements getTrophies(String username){
		int[] trophies = {0,0,0,0,0};
		Achievements achievements = null; 
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE2, null, C_USERNAME_STAT+"='"+username+"'", null, null, null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			trophies[0]=c.getInt(1);
			trophies[1]=c.getInt(2);
			trophies[2]=c.getInt(3);
			trophies[3]=c.getInt(4);
			trophies[4]=c.getInt(5);
			achievements = new Achievements(trophies);
		}
		db.close();
		return achievements;
	}
}
