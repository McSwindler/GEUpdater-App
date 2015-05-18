package com.swindle.geupdater;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class OptionsDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String OPTIONS_TABLE_NAME = "geu_options";
	private static final String OPTION_ID = "ID";
	private static final String OPTION_NAME = "Name";
	private static final String OPTION_VALUE = "Value";
    private static final String TOOLBAR_TABLE_CREATE =
                "CREATE TABLE " + OPTIONS_TABLE_NAME + " (" +
                	OPTION_ID + " INTEGER PRIMARY KEY, " +
                	OPTION_NAME + " TEXT, " +
                	OPTION_VALUE + " TEXT);";
    
    private static Map<String,String> toolbarCode = null;
    private SQLiteDatabase database = null;
    

    OptionsDatabaseHandler(Context context) {
        super(context, OPTIONS_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TOOLBAR_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	private void openDB() {
		if(database == null){
			database = getWritableDatabase();
		}
	}
	
	private void closeDB() {
		if(database != null){
			database.close();
		}
		
		database = null;
	}
	
	public void setToolbarCode(String toolbarCode, String toolbarHash) {
		Map<String,String> codes = getToolbarCode();
		ContentValues codeVals = new ContentValues(2);
		ContentValues hashVals = new ContentValues(2);
		
		codeVals.put(OPTION_VALUE, toolbarCode);
		hashVals.put(OPTION_VALUE, toolbarHash);
		
		
		if(!codes.isEmpty()){
			openDB();
			database.update(OPTIONS_TABLE_NAME, codeVals, OPTION_NAME + " = 'toolbarCode'", null);
			database.update(OPTIONS_TABLE_NAME, hashVals, OPTION_NAME + " = 'toolbarHash'", null);
			
		} else {
			openDB();
			codeVals.put(OPTION_NAME, "toolbarCode");
			hashVals.put(OPTION_NAME, "toolbarHash");
			
			
			database.insert(OPTIONS_TABLE_NAME, null, codeVals);
			database.insert(OPTIONS_TABLE_NAME, null, hashVals);
		}
		toolbarCode = null;
		
		closeDB();
	}
	
	public Map<String,String> getToolbarCode() {
		if(toolbarCode != null) {
			return toolbarCode;
		}
		
		HashMap<String,String> results = new HashMap<String,String>();
		openDB();
		String select = "SELECT " + OPTION_VALUE + 
				" FROM " + OPTIONS_TABLE_NAME +
				" WHERE " + OPTION_NAME + " = 'toolbarCode' OR " +
				OPTION_NAME + " = 'toolbarHash'";
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(select, null);
			
			if(cursor.getCount() < 2) {
				return results;
			}
			
			if(cursor.moveToFirst()){
				results.put("toolbarCode", cursor.getString(cursor.getColumnIndex(OPTION_VALUE)));
				if(cursor.moveToNext()){
					results.put("toolbarHash", cursor.getString(cursor.getColumnIndex(OPTION_VALUE)));
				}
			}
		} catch (Exception e) {
			Log.e("Cursor", e.toString());
			
		} finally {
			if(cursor != null)
				cursor.close();
			closeDB();
		}
		
		return results;
	}
}