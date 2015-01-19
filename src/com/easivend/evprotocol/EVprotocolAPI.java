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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVprotocolAPI 
{
	static int EVproTimer=0;//��ʱ��ʱ��
	static EVprotocol ev=null;
	private static int EV_TYPE=0;
	private static final int EV_INITING=1;//���ڳ�ʼ��
	private static final int EV_ONLINE=2;//�ɹ�����
	private static final int EV_OFFLINE=3;//�Ͽ�����
	private static final int EV_RESTART=4;//���ذ������Ķ�
	private static final int EV_TRADE_RPT=5;//��������
		private static int device=0;//�������		
		private static int status=0;//�������
		private static int hdid=0;//����id
		private static int type=0;//��������
		private static int cost=0;//��Ǯ
		private static int totalvalue=0;//ʣ����
		private static int huodao=0;//ʣ��������
	
	
	//ʵ����hand���䣬���ҽ���pend
	private static Handler EVProhand=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			//Log.i("EV_JNI",msg.obj.toString());
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case 1://����JNI���ص���Ϣ
					Log.i("EV_JNI",msg.obj.toString());
					Map<String, Object> map=JsonToolUnpack.getMapListgson(msg.obj.toString());
					//Log.i("EV_JNI",list.toString());
					/*
					 //����Map���
					Set<Entry<String, Object>> allset=map.entrySet();  //ʵ����
			        Iterator<Entry<String, Object>> iter=allset.iterator();
			        while(iter.hasNext())
			        {
			            Entry<String, Object> me=iter.next();
			            Log.i("EV_JNI",me.getKey()+"-->"+me.getValue());
			        } 
			        */
					//����keyȡ������
				    String str_evType=map.get("EV_type").toString(); 
					//Log.i("EV_JNI",str_evType);				    
				    if(str_evType.equals("EV_INITING"))//���ڳ�ʼ��
					{
						//textView_VMCState.setText("���ڳ�ʼ��");
				    	Log.i("EV_JNI","���ڳ�ʼ��");
				    	EV_TYPE=EV_INITING;
					}
					else if(str_evType.equals("EV_ONLINE"))//str_evType.equals("EV_PAYOUT_RPT")
					{
						//textView_VMCState.setText("�ɹ�����");
						Log.i("EV_JNI","�ɹ�����");
						EV_TYPE=EV_ONLINE;
					}
					else if(str_evType.equals("EV_OFFLINE"))
					{
						//textView_VMCState.setText("�Ͽ�����");
						Log.i("EV_JNI","�Ͽ�����");
						EV_TYPE=EV_OFFLINE;
					}
					else if(str_evType.equals("EV_RESTART"))
					{
						//textView_VMCState.setText("���ذ������Ķ�");
						Log.i("EV_JNI","���ذ������Ķ�");
						EV_TYPE=EV_RESTART;
					}
					else if(str_evType.equals("EV_TRADE_RPT"))
					{
						//textView_VMCState.setText("���ذ������Ķ�");
						Log.i("EV_JNI","��������");
						EV_TYPE=EV_TRADE_RPT;
						//�õ������ش�����
						device=Integer.parseInt(map.get("cabinet").toString());//�������
						status=Integer.parseInt(map.get("result").toString());//�������
						hdid=Integer.parseInt(map.get("column").toString());//����id
						type=Integer.parseInt(map.get("type").toString());//��������
						cost=Integer.parseInt(map.get("cost").toString());//��Ǯ
						totalvalue=Integer.parseInt(map.get("remainAmount").toString());//ʣ����
						huodao=Integer.parseInt(map.get("remainCount").toString());//ʣ��������
					}
				    
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
	public static int vmcStart(String portName)
	{
		int result=0;	
		ev=new EVprotocol(EVProhand);
		result=ev.vmcStart(portName);
		//�����ɹ�
		if(result==1)
		{
			result=2;	
			EVproTimer=20;//��Ϊ20s��ʱ
			while(EVproTimer>0)
			{
				if(EV_TYPE==EV_ONLINE)
				{
					result=1;
					EVproTimer=0;
				}
			}
		}	
		//����ʧ��
		
		
		return result; 
	}
			
	/*********************************************************************************************************
	** Function name:     	vmcStop
	** Descriptions:	    VMC���ذ�Ͽ��ӿ�
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      �ޣ�ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public static void vmcStop()
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
	public static int trade(int cabinet,int column,int type,int cost)
	{
		int result=0;	
		result=ev.trade(cabinet,column,type,cost);
		//�����ͳɹ�
		if(result==1)
		{
			result=0;
			EVproTimer=60;//��Ϊ60s��ʱ
			while(EVproTimer>0)
			{
				if(EV_TYPE==EV_TRADE_RPT)
				{
					result=1;
					EVproTimer=0;
					//��ջش�����
					device=0;//�������		
					status=0;//�������
					hdid=0;//����id
					type=0;//��������
					cost=0;//��Ǯ
					totalvalue=0;//ʣ����
					huodao=0;//ʣ��������
				}
			}
		}
		//������ʧ��
		return result;
	}
	
}
