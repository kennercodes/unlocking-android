/*
 * JobListHandler.java
 * Unlocking Android
 * http://manning.com/ableson
 * Author: W. F. Ableson
 * fableson@msiservices.com
 */


package com.msi.manning.UnlockingAndroid;

import android.os.Handler;
import android.os.Message;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import android.util.Log;
import android.content.Context;

public class JobListHandler extends DefaultHandler 
{
	Handler phandler = null;
	JobList _list;
	JobEntry _job;
	String _lastElementName = "";
	StringBuilder sb = null;
	Context _context;
	
	JobListHandler(Context c,Handler progresshandler)
	{
		_context = c;
		if (progresshandler != null)
		{
			phandler = progresshandler;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = (Object)("Processing List");
			phandler.sendMessage(msg);
		}
	}
	
	public JobList getList()
	{
		Message msg = new Message();
		msg.what = 0;
		msg.obj = (Object)("Fetching List");
		if (phandler != null) phandler.sendMessage(msg);
		return _list;
	}
	
	public void startDocument() throws SAXException
	{
		Message msg = new Message();
		msg.what = 0;
		msg.obj = (Object)("Starting Document");
		if (phandler != null) phandler.sendMessage(msg);		
		
		// initialize our JobLIst object - this will hold our parsed contents
		_list = new JobList(_context);
		
		// initialize the JobEntry object 
		_job = new JobEntry();

	}
	public void endDocument() throws SAXException
	{
		Message msg = new Message();
		msg.what = 0;
		msg.obj = (Object)("End of Document");
		if (phandler != null) phandler.sendMessage(msg);		

	}
	
	
	public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException
	{
		try
		{
			sb = new StringBuilder("");
	
			if (localName.equals("job"))
			{
				// create a new item
				
				Message msg = new Message();
				msg.what = 0;
				msg.obj = (Object)(localName);
				if (phandler != null) phandler.sendMessage(msg);		
				
				
				_job = new JobEntry();

			}
		}
		catch (Exception ee)
		{
			Log.d("CH12:startElement",ee.getStackTrace().toString());
		}
	}
	
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{

		if (localName.equals("job"))
		{
			// add our job to the list!
			_list.addJob(_job);
			Message msg = new Message();
			msg.what = 0;
			msg.obj = (Object)("Storing Job # " + _job.get_jobid());
			if (phandler != null) phandler.sendMessage(msg);		

			return;
		}
		
		if (localName.equals("id"))
		{
			_job.set_jobid(sb.toString());
			return;
		}
		if (localName.equals("status"))
		{
			_job.set_status(sb.toString());
			return;
		}
		if (localName.equals("customer"))
		{
			_job.set_customer(sb.toString());
			return;
		}
		if (localName.equals("address"))
		{
			_job.set_address(sb.toString());
			return;
		}
		if (localName.equals("city"))
		{
			_job.set_city(sb.toString());
			return;
		}
		if (localName.equals("state"))
		{
			_job.set_state(sb.toString());
			return;
		}
		if (localName.equals("zip"))
		{
			_job.set_zip(sb.toString());
			return;
		}
		if (localName.equals("product"))
		{
			_job.set_product(sb.toString());
			return;
		}
		if (localName.equals("producturl"))
		{
			_job.set_producturl(sb.toString());
			return;
		}
		if (localName.equals("comments"))
		{
			_job.set_comments(sb.toString());
			return;
		}		
		
		
	}
	
	public void characters(char ch[], int start, int length)
	{
		String theString = new String(ch,start,length);
		Log.d("CH12","characters[" + theString + "]");
		sb.append(theString);
	}
}
