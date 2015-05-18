package com.swindle.geupdater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class OffersDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String OFFERS_TABLE_NAME = "geu_offers";
	private static final String OFFER_ID = "ID";
	public static final String ITEM_ID = "itemID";
	public static final String ITEM_ICON = "icon";
	public static final String ITEM_NAME = "name";
	public static final String OFFER_TYPE = "type";
	public static final String OFFER_AMOUNT = "amount";
	public static final String OFFER_PRICE = "price";
	public static final String OFFER_PERCENT = "percent";
	public static final String OFFER_EARN = "earned";
	public static final String OFFER_UPDATE = "updated";
    private static final String OFFERS_TABLE_CREATE =
                "CREATE TABLE " + OFFERS_TABLE_NAME + " (" +
                		OFFER_ID + " INTEGER PRIMARY KEY, " +
                		ITEM_ID + " INTEGER, " +
                		ITEM_ICON + " BLOB, " +
                		ITEM_NAME + " TEXT, " +
                		OFFER_TYPE + " TEXT, " +
                		OFFER_AMOUNT + " INTEGER, " +
                		OFFER_PRICE + " INTEGER, " +
                		OFFER_PERCENT + " DOUBLE, " +
                		OFFER_EARN + " INTEGER, " +
                		OFFER_UPDATE + " DATETIME);";
    
    private SQLiteDatabase database = null;
    

    OffersDatabaseHandler(Context context) {
        super(context, OFFERS_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OFFERS_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	private void openDB(boolean isWrite) {
		if(database == null){
			if(isWrite)
				database = getWritableDatabase();
			else
				database = getReadableDatabase();
		}
	}
	
	private void closeDB() {
		if(database != null){
			database.close();
		}
		
		database = null;
	}
	
	public void updateOffer(Map<String,Object> offerData) {
		List<Integer> found = findOffer(offerData, 1);
		if(found.isEmpty()){
			//TODO addOffer(offerData);
		} else {
			int offerId = found.get(0);
			ContentValues offerVals = new ContentValues(2);
			
			offerVals.put(OFFER_PERCENT, (String) offerData.get(OFFER_PERCENT));
			offerVals.put(OFFER_EARN, (String) offerData.get(OFFER_EARN));
			
			
			openDB(true);
			database.update(OFFERS_TABLE_NAME, offerVals, OFFER_ID + " = " + offerId, null);
			
			closeDB();
		}
	}
	
	private List<Integer> findOffer(Map<String,Object> offerData, int limit) {
		String sql = "SELECT " + OFFER_ID + 
				" FROM " + OFFERS_TABLE_NAME +
				" WHERE ";
		
		String where = "";
		Iterator<String> keys = offerData.keySet().iterator();
		while(keys.hasNext()) {
			String field = keys.next();
			if(field == ITEM_ICON || field == OFFER_UPDATE) {
				continue;
			} else if(field == ITEM_ID || field == OFFER_AMOUNT || field == OFFER_PRICE){
				int value = (Integer) offerData.get(field);
				where += field + " = " + value;
			} else {
				String value = (String) offerData.get(field);
				where += field + " = '" + value + "'";
			}
			
			if(keys.hasNext())
				where += " AND ";
		}
		if(where.length() < 1)
			return null;
		
		openDB(false);
		Cursor cursor = null;
		String select = sql + where + " ORDER BY " + OFFER_ID;
		List<Integer> results = new ArrayList<Integer>();
		if(limit > 0)
			select += " LIMIT " + limit + ";";
		else
			select += ";";
		
		try {
			cursor = database.rawQuery(select, null);
			
			if(cursor.getCount() < 1) {
				return null;
			}
			
			while(cursor.moveToNext()){
				int id = cursor.getInt(cursor.getColumnIndex(OFFER_ID));
				if(id < 0)
					continue;
				
				results.add(id);
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