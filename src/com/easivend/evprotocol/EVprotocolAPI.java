/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocolAPI.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ��JNI�ӿ�ͨ��API��          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.evprotocol;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVprotocolAPI 
{
	EVprotocol ev=null;
	//ʵ����hand���䣬���ҽ���pend
	private Handler EVProhand=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			Log.i("EV_thread",msg.obj.toString());
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case 1://���߳̽������߳���Ϣ
					Log.i("EV_thread",msg.obj.toString());
					break;
			}	
		}
		
	};	
	
	
	/*********************************************************************************************************
	** Function name:     	vmcStart
	** Descriptions:	    VMC���ذ忪���ӿ�
	** input parameters:    portName ���ں� ����/dev/tty0
	** output parameters:   ��
	** Returned value:      1:�����ɹ�      -1:����ʧ��  ��ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public  int vmcStart(String portName)
	{
		ev=new EVprotocol(EVProhand);
		return ev.vmcStart(portName);
	}
			
	/*********************************************************************************************************
	** Function name:     	vmcStop
	** Descriptions:	    VMC���ذ�Ͽ��ӿ�
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      �ޣ�ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public void vmcStop()
	{
		ev.vmcStop();
	}
	
	/*********************************************************************************************************
	** Function name:     	trade
	** Descriptions:	    VMC�����ӿ�  
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	**						�ص�JSON�� ��ʽ 
	** input parameters:    cabinet:���  column:������   type:֧����ʽ  type= 0 :�ֽ�  type = 1  ���ֽ�    
	**					    cost:�ۿ���(��λ:�� ;���type=1���ֵ����Ϊ0)
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��  
	*********************************************************************************************************/
	public int trade(int cabinet,int column,int type,int cost)
	{
		return ev.trade(cabinet,column,type,cost);
	}
	
}
