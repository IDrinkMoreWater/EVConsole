/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           BluetoothMsg.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ������        
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.view;

public class BluetoothMsg {
	 /** 
     * ������������ 
     * @author Andy 
     * 
     */  
    public enum ServerOrCilent{  
        NONE,  
        SERVICE,  
        CILENT  
    };  
    //�������ӷ�ʽ  
    public static ServerOrCilent serviceOrCilent = ServerOrCilent.NONE;  
    //����������ַ  
    public static String BlueToothAddress = null,lastblueToothAddress=null;  
    //ͨ���߳��Ƿ���  
    public static boolean isOpen = false;  
}
