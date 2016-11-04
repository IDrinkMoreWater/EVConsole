package com.easivend.view;

import com.easivend.common.ToolClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;  
import android.net.wifi.WifiManager; 
import android.util.Log;

public class WifiChangeBroadcastReceiver extends BroadcastReceiver {
	private Context mContext;
	private BRInteraction brInteraction;
	 @Override  
     public void onReceive(Context context, Intent intent) {  
         mContext=context;  
         ToolClass.Log(ToolClass.INFO,"EV_JNI","Wifi�����仯","jni.txt");
         getWifiInfo();  
     }  
       
     private void getWifiInfo() {  
         WifiManager wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);  
         WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
         if (wifiInfo.getBSSID() != null) {  
             //wifi����  
             String ssid = wifiInfo.getSSID();  
             //wifi�ź�ǿ��  
             int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);  
             //wifi�ٶ�  
             int speed = wifiInfo.getLinkSpeed();  
             //wifi�ٶȵ�λ  
             String units = WifiInfo.LINK_SPEED_UNITS;  
             ToolClass.Log(ToolClass.INFO,"EV_JNI","ssid="+ssid+",signalLevel="+signalLevel+",speed="+speed,"jni.txt");
             brInteraction.setText("ssid="+ssid+",signalLevel="+signalLevel+",speed="+speed);  
         }  
    } 
     public interface BRInteraction {
         public void setText(String content);
     }

     public void setBRInteractionListener(BRInteraction brInteraction) {
         this.brInteraction = brInteraction;
     } 

}
