package com.swindle.geupdater;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.util.Log;

public class Parser {
	
	public static final String CAPTION = "caption";
	public static final String ICON = "icon";
	public static final String HEADER = "header";
	public static final String PERCENT = "percent";
	
	private static final Pattern offerPattern = Pattern.compile("^(selling|buying) ([0-9]+) ([a-z0-9\\s]+) at ([0-9]+), ([0-9]{1,3})% complete, (earning|costing) ([0-9]+) gp so far$", Pattern.CASE_INSENSITIVE);

	
	public static List<Map<String, String>> parseXML(String xml) {
		if(xml == null)
			return null;
		
		List<Map<String,String>> menuItems = new ArrayList<Map<String,String>>();
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		
		try {
		    builder = builderFactory.newDocumentBuilder();
		    InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
		    document = builder.parse(is);
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		} catch (SAXException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		if(document == null){
			Log.e("Document", "Error Parsing document");
			return null;
		}
		
		NodeList nl = document.getElementsByTagName("DEFAULT_BUTTON_TEXT");
		if(nl != null){
			Map<String,String> menuItem = new HashMap<String,String>();
			menuItem.put(HEADER, nl.item(0).getTextContent());
			menuItems.add(menuItem);
		}
		
		
		nl = document.getElementsByTagName("MENU_ITEM");
		if(nl == null){
			Log.e("NodeList", "NodeList is empty");
			return null;
		}
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			
			Map<String,String> menuItem = new HashMap<String,String>();
			
			String caption = getValue(e, "CAPTION");
			
			menuItem.put(CAPTION, caption);
			menuItem.put(ICON, getValue(e, "ICON_URL"));
			Matcher offerMatch = offerPattern.matcher(caption);
			if(offerMatch.matches()){
				menuItem.put(PERCENT, offerMatch.group(5));
			}
			
			menuItems.add(menuItem);
		}
		
		return menuItems;
	}
	
	public static byte[] convertBitmap(Bitmap bitmap) {
		int size = bitmap.getWidth() * bitmap.getHeight();
		ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] x = out.toByteArray();
		
		return x;
	}
	
	private static String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
	    return getElementValue(n.item(0));
	}
	
	private static String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.CDATA_SECTION_NODE || child.getNodeType() == Node.TEXT_NODE ){
                        return child.getNodeValue();
                    }
                }
            } else {
            	return elem.getNodeValue();
            }
        }
        return "";
	} 


}
