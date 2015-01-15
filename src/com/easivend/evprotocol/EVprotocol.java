/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        java����JNI�ӿڷ�װ��                   
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.evprotocol;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class EVprotocol {
	
	
	public EVprotocol(){} //���캯��
	public EVprotocol(Handler handler){this.handler = handler;}//���캯��
	
	
	static{
		System.loadLibrary("EVprotocol");//����JNI��̬���ӿ�
		
	}
	
	
	/*********************************************************************************************************
	** Function name:     	EV_callBack
	** Descriptions:	    VMC�ص���ڣ����нϳ��Ľ����Ӧ����ͨ���ú����ص���ʾ��
	** input parameters:    json_msg:��Ӧ�����JSON���� 
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	public void EV_callBack(String json_msg)
	{
		Log.i("JSON", json_msg);
		//������JSON�� �ô���ͨ��handler������Ϣ�ķ�ʽ���͵����̴߳������ģ������߿���ѡ���Լ��ķ�ʽ���д���
		//����ǧ��Ҫ�ڴ˺�����������Ĵ�������jni���ó��� ���� �����ٶ�
		if(handler != null)
		{
			Message msg = Message.obtain();
			msg.what = 1;
			msg.obj = json_msg;
			handler.sendMessage(msg);
		}
	}

	
	/*********************************************************************************************************
	** Function name:     	vmcStart
	** Descriptions:	    VMC���ذ忪���ӿ�
	** input parameters:    portName ���ں� ����/dev/tty0
	** output parameters:   ��
	** Returned value:      1:�����ɹ�      -1:����ʧ��  ��ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public native int vmcStart(String portName);
	
	
	
	
	
	/*********************************************************************************************************
	** Function name:     	vmcStop
	** Descriptions:	    VMC���ذ�Ͽ��ӿ�
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      �ޣ�ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public native void vmcStop();
	
	
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
	public native int trade(int cabinet,int column,int type,int cost);
	
	
	
	/*********************************************************************************************************
	** Function name:     	payout
	** Descriptions:	    VMC���ҽӿ�
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	** input parameters:    value:Ҫ���ҵĽ��(��λ:��)
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public native int payout(long value);
	
	
	
	/*********************************************************************************************************
	** Function name:     	getStatus
	** Descriptions:	             ��ȡVMC״̬�ӿ�  PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public native int getStatus();
	
	
	/*********************************************************************************************************
	** Function name:     	getRemainAmount
	** Descriptions:	             ��ȡVMCͶ�����  �����лص� ֱ�ӷ��ص�ǰ��� 
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      ���ص�ǰ��� ��λ:��
	*********************************************************************************************************/
	public native long getRemainAmount();
	
		
	
	/*********************************************************************************************************
	** Function name:     	bentoRegister
	** Descriptions:	             �������ʼ���ӿ�
	** input parameters:    portName ���ں� ����/dev/tty0
	** output parameters:   ��
	** Returned value:      1:��ʼ���ɹ�      -1:��ʼ��ʧ�� (ʧ��ԭ��Ϊ���ڴ�ʧ��)
	*********************************************************************************************************/
	public native int bentoRegister(String portName);
	
	
	
	/*********************************************************************************************************
	** Function name:     	bentoRelease
	** Descriptions:	             �������ͷ���Դ�ӿڣ���bentoRegister���׵��ã�
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      1:�ɹ�      �����ñض��ɹ���
	*********************************************************************************************************/
	public native int bentoRelease();
	

	/*********************************************************************************************************
	** Function name:     	bentoOpen
	** Descriptions:	             �����񿪸��ӽӿ�
	** input parameters:    cabinet:���     box:���Ӻ�
	** output parameters:   ��
	** Returned value:      1:�򿪳ɹ�      0:��ʧ��
	*********************************************************************************************************/
	public native int bentoOpen(int cabinet,int box);
	
	
	
	/*********************************************************************************************************
	** Function name:     	bentoLight
	** Descriptions:	             �������������ƽӿ�
	** input parameters:    cabinet:���     flag:1 ��ʾ������    0:��ʾ������
	** output parameters:   ��
	** Returned value:      1:�򿪳ɹ�      0:��ʧ��
	*********************************************************************************************************/
	public native int bentoLight(int cabinet,int flag);//flag 1����  0�ص�
	
	
	/*********************************************************************************************************
	** Function name:     	bentoCheck
	** Descriptions:	             ������״̬��ѯ�ӿ�
	** input parameters:    cabinet:���     
	** output parameters:   ��
	** Returned value:      ���ص���һ��JSON�� ����{{}}
	*********************************************************************************************************/
	public native String bentoCheck(int cabinet);
	
	
	
	
		
	
	
	
	
	
	
	public Handler handler = null;
	

	//JNI ��̬�ص����� ʾ������ û����
	public static void EV_callBackStatic(int i) 
	{
		Log.i("Java------------->","" +  i);
	}
	
}