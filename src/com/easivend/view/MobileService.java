package com.easivend.view;

import com.easivend.common.ToolClass;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class MobileService extends Service 
{
	TelephonyManager        Tel;
    MyPhoneStateListener    MyListener;
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) 
	{
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //����
        NetworkInfo networkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if(networkInfo.isConnected())
        {
        	ToolClass.setNetType(1);
        }
        //wifi
        else
        {
        	State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        	if(wifi.toString().equals("CONNECTED"))
        	{
        		ToolClass.setNetType(2);
        	}
        	//mobile
        	else
        	{
        		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        		if(mobile.toString().equals("CONNECTED"))
            	{
            		ToolClass.setNetType(3);
            	}
        		else
        		{
            		ToolClass.setNetType(4);
            	}	
        	}
        }
        ToolClass.Log(ToolClass.INFO,"EV_JNI","�������������="+ToolClass.getNetType(),"jni.txt");
		
        
        //4g���
        MyListener   = new MyPhoneStateListener();        
        Tel       = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE); 
        Tel.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	
		
	private class MyPhoneStateListener extends PhoneStateListener
	{ 
      /* Get the Signal strength from the provider, each tiome there is an update  �ӵõ����ź�ǿ��,ÿ��tiome��Ӧ���и���*/
 
      @Override 
      public void onSignalStrengthsChanged(SignalStrength signalStrength)
      {
          super.onSignalStrengthsChanged(signalStrength);
          ToolClass.Log(ToolClass.INFO,"EV_JNI","GSM �ź� = "
                  + String.valueOf(signalStrength.getGsmSignalStrength()),"jni.txt");
          ToolClass.setNetStr("GSM �ź� = "
             + String.valueOf(signalStrength.getGsmSignalStrength()));
      } 
    };/* End of private Class */

}
