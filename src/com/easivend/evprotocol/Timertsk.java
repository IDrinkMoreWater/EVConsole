package com.easivend.evprotocol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import android.util.Log;

public class Timertsk extends TimerTask {

	@Override
	public void run() {
	    //SimpleDateFormat sdf=null;//��ӡʱ��ĸ�ʽ
	    //sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    //Log.i("EV_JNI","ʱ�䣺"+sdf.format(new Date()).toString());
		if(EVprotocolAPI.EVproTimer>0)
			EVprotocolAPI.EVproTimer--;
	  }

}
