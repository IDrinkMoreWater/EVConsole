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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVprotocolAPI 
{
	static EVprotocol ev=null;
	static Handler mainHandler=null;
	private static int EV_TYPE=0;	
	public static final int EV_INITING=1;//���ڳ�ʼ��
	public static final int EV_ONLINE=2;//�ɹ�����
	public static final int EV_OFFLINE=3;//�Ͽ�����
	public static final int EV_RESTART=4;//���ذ������Ķ�
	public static final int EV_TRADE_RPT=5;//��������	
	public static final int EV_PAYIN_RPT=6;//Ͷ��ֽ��
	public static final int EV_PAYOUT_RPT=7;//������
	
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
//					Map<String, Object> map=JsonToolUnpack.getMapListgson(msg.obj.toString());
//					//Log.i("EV_JNI",list.toString());
//					/*
//					 //����Map���
//					Set<Entry<String, Object>> allset=map.entrySet();  //ʵ����
//			        Iterator<Entry<String, Object>> iter=allset.iterator();
//			        while(iter.hasNext())
//			        {
//			            Entry<String, Object> me=iter.next();
//			            Log.i("EV_JNI",me.getKey()+"-->"+me.getValue());
//			        } 
//			        */
					try {
						String text = msg.obj.toString();
						JSONObject jsonObject = new JSONObject(text); 
						//����keyȡ������
						JSONObject ev_head = (JSONObject) jsonObject.getJSONObject("EV_json");
						String str_evType =  ev_head.getString("EV_type");					
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
							//��Activity�̷߳�����Ϣ
							Message childmsg=mainHandler.obtainMessage();
							childmsg.what=EV_ONLINE;
							mainHandler.sendMessage(childmsg);
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
						else if(str_evType.equals("EV_STATE_RPT"))
						{
							int state = ev_head.getInt("state");
							if(state == 0)
								Log.i("EV_JNI","�Ͽ�����");
							else if(state == 1)
								Log.i("EV_JNI","���ڳ�ʼ��");
							else if(state == 2)
								Log.i("EV_JNI","����");
							else if(state == 3)
								Log.i("EV_JNI","����");
							else if(state == 4)
								Log.i("EV_JNI","ά��");
						}
						else if(str_evType.equals("EV_ENTER_MANTAIN"))
						{
							Log.i("EV_JNI","ά��");
						}
						else if(str_evType.equals("EV_EXIT_MANTAIN"))
						{
							Log.i("EV_JNI","�˳�ά��");
						}
					    //����
						else if(str_evType.equals("EV_TRADE_RPT"))
						{
							//textView_VMCState.setText("���ذ������Ķ�");
							EV_TYPE=EV_TRADE_RPT;
							//�õ������ش�����
							Map<String,Integer> allSet = new HashMap<String,Integer>() ;							
							allSet.put("device", ev_head.getInt("cabinet"));//�������							
							allSet.put("status", ev_head.getInt("result"));//�������
							allSet.put("hdid", ev_head.getInt("column"));//����id
							allSet.put("type", ev_head.getInt("type"));//��������
							allSet.put("cost", ev_head.getInt("cost"));//��Ǯ
							allSet.put("totalvalue", ev_head.getInt("remainAmount"));//ʣ����
							allSet.put("huodao", ev_head.getInt("remainCount"));//ʣ��������				
							Log.i("EV_JNI","��������");	
							//��Activity�̷߳�����Ϣ
							Message childmsg=mainHandler.obtainMessage();
							childmsg.what=EV_TRADE_RPT;
							childmsg.obj=allSet;
							mainHandler.sendMessage(childmsg);							
						}
					    //Ͷ���ϱ�
						else if(str_evType.equals("EV_PAYIN_RPT"))//Ͷ���ϱ�
						{
							int amount = ev_head.getInt("remainAmount");
							Log.i("EV_JNI","Ͷ��:"+Integer.toString(amount));
							//��Activity�̷߳�����Ϣ
							Message childmsg=mainHandler.obtainMessage();
							childmsg.what=EV_PAYIN_RPT;
							childmsg.obj=amount;
							mainHandler.sendMessage(childmsg);	
						}
					    //����
						else if(str_evType.equals("EV_PAYOUT_RPT"))
						{
							int amount = ev_head.getInt("remainAmount");
							Log.i("EV_JNI","����:"+Integer.toString(amount));
							//��Activity�̷߳�����Ϣ
							Message childmsg=mainHandler.obtainMessage();
							childmsg.what=EV_PAYOUT_RPT;
							childmsg.obj=amount;
							mainHandler.sendMessage(childmsg);	
						}
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					break;
			}	
		}
		
	};	
	
	public static void setHandler(Handler hand)
	{
		mainHandler=hand;
	}
	
	
	/*********************************************************************************************************
	** Function name:     	vmcStart
	** Descriptions:	    VMC���ذ忪���ӿ�
	** input parameters:    portName ���ں� ����/dev/tty0
	** output parameters:   ��
	** Returned value:      1:�����ɹ�      -1:����ʧ��  ��ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public static int vmcStart(String portName)
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
	public static int trade(int cabinet,int column,int type,float cost)
	{
		return ev.trade(cabinet,column,type,(int)cost*100);
	}
	
	/*********************************************************************************************************
	** Function name:     	payout
	** Descriptions:	    VMC���ҽӿ�
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	** input parameters:    value:Ҫ���ҵĽ��(��λ:��)
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public static int payout(float value)
	{
		return payout((long)value*100);
	}
			
}
