package com.swindle.geupdater;

import java.util.regex.Pattern;

import android.util.SparseArray;

public class OffersParser extends Parser {
	private Pattern offerPattern = Pattern.compile("^(selling|buying) ([0-9]+) ([a-z0-9\\s]+) at ([0-9]+), ([0-9]){1,3}% complete, earning ([0-9]+) gp so far$", Pattern.CASE_INSENSITIVE);
	private Pattern iconPattern = Pattern.compile("id=([0-9]+)", Pattern.CASE_INSENSITIVE);
	private SparseArray<String> offerValues = new SparseArray<String>();
	
	public OffersParser(String toolbarCode, String toolbarHash) {

		
		offerValues.put(1, OffersDatabaseHandler.OFFER_TYPE);
		offerValues.put(2, OffersDatabaseHandler.OFFER_AMOUNT);
		offerValues.put(3, OffersDatabaseHandler.ITEM_NAME);
		offerValues.put(4, OffersDatabaseHandler.OFFER_PRICE);
		offerValues.put(5, OffersDatabaseHandler.OFFER_PERCENT);
		offerValues.put(6, OffersDatabaseHandler.OFFER_EARN);
	}
	
	/*public List<Map<String,Object>> getOffers() {
		List<Map<String,Object>> offers = new ArrayList<Map<String,Object>>();
		List<Map<String,String>> items = getItems();
		for(Map<String,String> item : items) {
			offers.add(parseOffer(item));
		}
		return offers;
	}
	
	private Map<String,Object> parseOffer(Map<String,String> item) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		String iconUrl = item.get(ICON);
		Matcher iconMatcher = iconPattern.matcher(iconUrl);
		if(iconMatcher.find()){
			byte[] byteMap = convertBitmap(CustomViewBinder.loadBitmap(iconUrl));
			result.put(OffersDatabaseHandler.ITEM_ICON, byteMap);
			result.put(OffersDatabaseHandler.ITEM_ID, iconMatcher.group(1));
		}
		

		Matcher offerMatcher = offerPattern.matcher(item.get(CAPTION));
		if(offerMatcher.matches()){
			for(int i = 1; i <= offerMatcher.groupCount(); i++){
				result.put(offerValues.get(i), offerMatcher.group(i));
			}
		}
		
		return result;
	}*/
	
}
