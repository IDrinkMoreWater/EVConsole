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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.R.integer;
import android.os.Handler;
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
		switch(info)
		{
			case VERBOSE:
				Log.v(tag,str);
				break;
			case DEBUG:
				Log.d(tag,str);
				break;
			case INFO:
				Log.i(tag,str);
				break;
			case WARN:
				Log.w(tag,str);
				break;
			case ERROR:
				Log.e(tag,str);
				break;	
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
	
}
