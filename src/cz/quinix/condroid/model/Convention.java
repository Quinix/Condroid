package cz.quinix.condroid.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import cz.quinix.condroid.abstracts.DBInsertable;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Convention implements Serializable, DBInsertable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2754843728933013769L;
	private String name = "";
	private String iconUrl = "";
	private String date = "";
	private int cid = 0;
	private String dataUrl;
	private String message;
	
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message.trim();
	}
	public int getCid() {
		return cid;
	}
	public String getDate() {
		return date;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public String getName() {
		return name;
	}
	
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public void setDate(String date) {
		this.date = date.trim();
	}
	
	public void setName(String name) {
		this.name = name.trim();
	}
	
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl.trim();
	}
	
	
	
	public Bitmap getImage() {
		Bitmap bitmap = null;
		if(iconUrl != null) {
			InputStream io;
			try {
				io = (InputStream) new URL(this.iconUrl).getContent();
				bitmap = BitmapFactory.decodeStream(io);
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return bitmap;
	}
	public String getDataUrl() {
		return this.dataUrl;
	}	
	
	public void setDataUrl(String url) {
		this.dataUrl = url;
	}
	public ContentValues getContentValues() {
		ContentValues ret = new ContentValues();
		ret.put("id", this.cid);
		ret.put("date", date);
		ret.put("iconUrl", iconUrl);
		ret.put("name", name);
		ret.put("dataUrl", dataUrl);
		ret.put("message", message);
		
		return ret;
	}
}