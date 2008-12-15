/*
 * prefs.java
 * Unlocking Android
 * http://manning.com/ableson
 * Author: W. F. Ableson
 * fableson@msiservices.com
 */

package com.msi.manning.UnlockingAndroid;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences.Editor;



public class Prefs 
{
	private SharedPreferences _prefs = null;
	private Editor _editor = null;
	private String _useremailaddress = "Unknown";
	private String _serverurl = "http://android13.msi-wireless.com/getjoblist.php";
	
	
	public Prefs(Context context) 
	{
		_prefs = context.getSharedPreferences("PREFS_PRIVATE",Context.MODE_PRIVATE);
		_editor = _prefs.edit();
	}
	
	public String getValue(String key,String defaultvalue)
	{
		if (_prefs == null) return "Unknown";
		
		return _prefs.getString(key,defaultvalue);
	}
	
	public void setValue(String key,String value)
	{
		if (_editor == null) return;
		
		_editor.putString(key,value);
		
	}
	
	public String getEmail()
	{
		if (_prefs == null) return "Unknown";
		
		_useremailaddress = _prefs.getString("emailaddress","Unknown");
		return _useremailaddress;
	}
	public String getServer()
	{
		if (_prefs == null) return "http://android13.msi-wireless.com/";
		
		_serverurl = _prefs.getString("serverurl","http://android13.msi-wireless.com/");
		return _serverurl;
	}
	
	public void setEmail(String newemail)
	{
		if (_editor == null) return;
		
		_editor.putString("emailaddress",newemail);
	}
	public void setServer(String serverurl)
	{
		if (_editor == null) return;
		_editor.putString("serverurl",serverurl);
	}
	public void save()
	{
		if (_editor == null) return;
		_editor.commit();
	}
}
