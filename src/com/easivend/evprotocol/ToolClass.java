/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           ToolClass.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        �����࣬�������ŵ���Ҫ��static������static��Ա��ͳһ��Ϊȫ�ֱ�����ȫ�ֺ�����       
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/


package com.easivend.evprotocol;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

public class ToolClass 
{
	public final static int VERBOSE=0;
	public final static int DEBUG=1;
	public final static int INFO=2;
	public final static int WARN=3;
	public final static int ERROR=4;
	
	//����Map����<String,Object>�����ݼ���
	public static Map<String, Object> getMapListgson(String jsonStr)
	{
		Map<String, Object> list=new HashMap<String,Object>();
		try {
			JSONObject object=new JSONObject(jsonStr);//{"EV_json":{"EV_type":"EV_STATE_RPT","state":2}}
			JSONObject perobj=object.getJSONObject("EV_json");//{"EV_type":"EV_STATE_RPT","state":2}
			Gson gson=new Gson();
			list=gson.fromJson(perobj.toString(), new TypeToken<Map<String, Object>>(){}.getType());
			//Log.i("EV_JNI",perobj.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	//Log���������ڴ�ӡlog�������ı��ļ��д�ӡ������־
	public static void Log(int info,String tag,String str)
	{
		String infotype="";
		switch(info)
		{
			case VERBOSE:
				infotype="VERBOSE";
				Log.v(tag,str);
				break;
			case DEBUG:
				infotype="DEBUG";
				Log.d(tag,str);
				break;
			case INFO:
				infotype="INFO";
				Log.i(tag,str);
				break;
			case WARN:
				infotype="WARN";
				Log.w(tag,str);
				break;
			case ERROR:
				infotype="ERROR";
				Log.e(tag,str);
				break;	
		}	
		infotype="("+infotype+"),["+tag+"] "+str;
		AppendLogFile(infotype);
	}
	
	/**
     * ׷���ļ���ʹ��FileWriter
     */
    public static void AppendLogFile(String content) 
    {
    	final String SDCARD_DIR=File.separator+"sdcard"+File.separator+"logs";
    	final String NOSDCARD_DIR=File.separator+"logs";
    	String  sDir =null;
    	File fileName=null;
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  
                + "hh:mm:ss:SSS"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString();  
        String cont=datetime+content+"\n";
    	
        try {
        	  //�����ж�sdcard�Ƿ����
        	  String status = Environment.getExternalStorageState();
        	  if (status.equals(Environment.MEDIA_MOUNTED)) 
        	  {
        		 sDir = SDCARD_DIR;;
        	  } 
        	  else
        	  {
        		  sDir = NOSDCARD_DIR;
        	  }
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	 
        	 fileName=new File(sDir+File.separator+"log.txt");         	
        	//��������ڣ��򴴽��ļ�
        	if(!fileName.exists())
        	{  
    	      fileName.createNewFile(); 
    	    }  
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(cont);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//���ͽ����������Ԫ,תΪ�Է�Ϊ��λ���͵�����
	public static long MoneySend(float sendMoney)
	{
		long values=(long)(sendMoney*100);
		return values;
	}
	
	//���ս��ת�����������յķ�,תΪ�����Ԫ
	public static float MoneyRec(long Money)
	{
		float amount = (float)(Money/100);
		return amount;
	}
	
	/**
	 * �������Uri��������ļ�ϵͳ�е�·��
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri )
	{
	    if ( null == uri ) 
	    	return "";
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}
	
}
