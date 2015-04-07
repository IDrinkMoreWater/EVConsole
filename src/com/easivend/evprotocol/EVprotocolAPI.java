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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.easivend.common.ToolClass;
import com.easivend.evprotocol.EVprotocol.EV_listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVprotocolAPI 
{
	static EVprotocol ev=null;
	private static JNIInterface callBack=null;//��activity����ע��ص�
	private static Call_json callJson = new Call_json();//��JNI����ע��ص�	
	private static Map<String,Integer> allSet = new TreeMap<String,Integer>() ;
	private static int EV_TYPE=0;	
	public static final int EV_INITING=1;//���ڳ�ʼ��
	public static final int EV_ONLINE=2;//�ɹ�����
	public static final int EV_OFFLINE=3;//�Ͽ�����
	public static final int EV_RESTART=4;//���ذ������Ķ�
	public static final int EV_TRADE_RPT=5;//��������
	public static final int EV_COLUMN_RPT=6;//����״̬�ϱ�
	public static final int EV_PAYIN_RPT=7;//Ͷ��ֽ��
	public static final int EV_PAYOUT_RPT=8;//������
	public static final int EV_STATE_RPT=9;//��ʼ�����
	
	//ʵ����hand���䣬���ҽ���pend
	private static Handler EVProhand=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			//ToolClass.Log(ToolClass.INFO,"EV_JNI",msg.obj.toString());
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case 1://����JNI���ص���Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<"+msg.obj.toString());
//					Map<String, Object> map=JsonToolUnpack.getMapListgson(msg.obj.toString());
//					//ToolClass.Log(ToolClass.INFO,"EV_JNI",list.toString());
//					/*
//					 //����Map���
//					Set<Entry<String, Object>> allset=map.entrySet();  //ʵ����
//			        Iterator<Entry<String, Object>> iter=allset.iterator();
//			        while(iter.hasNext())
//			        {
//			            Entry<String, Object> me=iter.next();
//			            ToolClass.Log(ToolClass.INFO,"EV_JNI",me.getKey()+"-->"+me.getValue());
//			        } 
//			        */
					try {
						String text = msg.obj.toString();
						JSONObject jsonObject = new JSONObject(text); 
						//����keyȡ������
						JSONObject ev_head = (JSONObject) jsonObject.getJSONObject("EV_json");
						String str_evType =  ev_head.getString("EV_type");					
					    //ToolClass.Log(ToolClass.INFO,"EV_JNI",str_evType);				    
					    if(str_evType.equals("EV_INITING"))//���ڳ�ʼ��
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<���ڳ�ʼ��");
					    	EV_TYPE=EV_INITING;
						}
						else if(str_evType.equals("EV_ONLINE"))//str_evType.equals("EV_PAYOUT_RPT")
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�ɹ�����");
							EV_TYPE=EV_ONLINE;
							//��Activity�̷߳�����Ϣ
//							Message childmsg=mainHandler.obtainMessage();
//							childmsg.what=EV_ONLINE;
//							mainHandler.sendMessage(childmsg);
							//���ӿڻص���Ϣ
							allSet.clear();
							allSet.put("EV_TYPE", EV_ONLINE);
							callBack.jniCallback(allSet);
						}
						else if(str_evType.equals("EV_OFFLINE"))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�Ͽ�����");
							EV_TYPE=EV_OFFLINE;
						}
						else if(str_evType.equals("EV_RESTART"))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<���ذ������Ķ�");
							EV_TYPE=EV_RESTART;
						}
						else if(str_evType.equals("EV_STATE_RPT"))
						{
							int state = ev_head.getInt("vmcState");
							if(state == 0)
								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�Ͽ�����");
							else if(state == 1)
								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<���ڳ�ʼ��");
							else if(state == 2)		
							{
								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����");	
								//���ӿڻص���Ϣ
								allSet.clear();
								allSet.put("EV_TYPE", EV_ONLINE);
								callBack.jniCallback(allSet);
							}
							else if(state == 3)
								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����");
							else if(state == 4)
								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<ά��");
						}
						else if(str_evType.equals("EV_ENTER_MANTAIN"))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<ά��");
						}
						else if(str_evType.equals("EV_EXIT_MANTAIN"))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�˳�ά��");
						}
					    //����
						else if(str_evType.equals("EV_TRADE_RPT"))
						{
							EV_TYPE=EV_TRADE_RPT;
							//�õ������ش�����
							allSet.clear();	
							allSet.put("EV_TYPE", EV_TRADE_RPT);
							allSet.put("device", ev_head.getInt("cabinet"));//�������							
							allSet.put("status", ev_head.getInt("result"));//�������
							allSet.put("hdid", ev_head.getInt("column"));//����id
							allSet.put("type", ev_head.getInt("type"));//��������
							allSet.put("cost", ev_head.getInt("cost"));//��Ǯ
							allSet.put("totalvalue", ev_head.getInt("remainAmount"));//ʣ����
							allSet.put("huodao", ev_head.getInt("remainCount"));//ʣ��������				
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<��������");	
							//��Activity�̷߳�����Ϣ
//							Message childmsg=mainHandler.obtainMessage();
//							childmsg.what=EV_TRADE_RPT;
//							childmsg.obj=allSet;
//							mainHandler.sendMessage(childmsg);		
							//���ӿڻص���Ϣ
							callBack.jniCallback(allSet);
						}
					    //����״̬�ϱ�
						else if(str_evType.equals("EV_COLUMN_RPT"))
						{
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����״̬�ϱ�");
							EV_TYPE=EV_COLUMN_RPT;
							JSONArray arr=ev_head.getJSONArray("column");//����json����
							//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
							//���ӿڻص���Ϣ
							allSet.clear();	
							allSet.put("EV_TYPE", EV_COLUMN_RPT);
							for(int i=0;i<arr.length();i++)
							{
								JSONObject object2=arr.getJSONObject(i);
								allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
							}
							//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����3:"+allSet.toString());
							callBack.jniCallback(allSet);
						}
					    //Ͷ���ϱ�
						else if(str_evType.equals("EV_PAYIN_RPT"))//Ͷ���ϱ�
						{
							int payin_amount= ev_head.getInt("payin_amount");
							int reamin_amount = ev_head.getInt("reamin_amount");
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<Ͷ��:"+Integer.toString(payin_amount)
									+"�ܹ�:"+Integer.toString(reamin_amount));							
							
							//��Activity�̷߳�����Ϣ
//							Message childmsg=mainHandler.obtainMessage();
//							childmsg.what=EV_PAYIN_RPT;
//							childmsg.obj=amount;
//							mainHandler.sendMessage(childmsg);	
							//���ӿڻص���Ϣ
							allSet.clear();	
							allSet.put("EV_TYPE", EV_PAYIN_RPT);
							allSet.put("payin_amount", payin_amount);
							allSet.put("reamin_amount", reamin_amount);
							callBack.jniCallback(allSet);
						}
					    //�������ϱ�
						else if(str_evType.equals("EV_PAYOUT_RPT"))
						{
							int payout_amount= ev_head.getInt("payout_amount");
							int reamin_amount = ev_head.getInt("reamin_amount");
							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����:"+Integer.toString(payout_amount)
									+"ʣ��:"+Integer.toString(reamin_amount));	//��Activity�̷߳�����Ϣ
							
//							Message childmsg=mainHandler.obtainMessage();
//							childmsg.what=EV_PAYOUT_RPT;
//							childmsg.obj=amount;
//							mainHandler.sendMessage(childmsg);
							//���ӿڻص���Ϣ
							allSet.clear();	
							allSet.put("EV_TYPE", EV_PAYOUT_RPT);
							allSet.put("payout_amount", payout_amount);
							allSet.put("reamin_amount", reamin_amount);
							callBack.jniCallback(allSet);
						}
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
			}	
		}
		
	};	
	
		
	//��activity֮��ӿڵĽ���
	public static void setCallBack(JNIInterface call){ 
        callBack = call;
    } 
	//��jni�ӿڵĽ���
	public static class Call_json implements EV_listener
	{
		public void do_json(String json){
			Message msg = Message.obtain();
			msg.what = 1;
			msg.obj = json;
			EVProhand.sendMessage(msg);
		}
	}
	/*********************************************************************************************************
	** Function name:     	vmcEVStart
	** Descriptions:	    VMC���ذ���������ӿ�
	** input parameters:    
	** output parameters:   ��
	** Returned value:      
	*********************************************************************************************************/
	public static void vmcEVStart()
	{
		ev= new EVprotocol();
		ev.addListener(callJson);//ע����jni�ļ����ӿ�
	}
	/*********************************************************************************************************
	** Function name:     	vmcEVStop
	** Descriptions:	    VMC���ذ�����ر�
	** input parameters:    
	** output parameters:   ��
	** Returned value:      
	*********************************************************************************************************/
	public static void vmcEVStop()
	{
		ev.removeListener(callJson);
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
	** Function name:     	getColumn
	** Descriptions:	    VMC��ȡ�����ӿ�
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������صĽ�����н���
	** input parameters:    cabinet:1����,2����
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public  static int getColumn(int cabinet)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIgetColumn>>]"+cabinet);
		return ev.getColumn(cabinet);		
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
	public static int trade(int cabinet,int column,int type,long cost)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIhuo>>]"+cost);
		return ev.trade(cabinet,column,type,(int)cost);
	}
	
	/*********************************************************************************************************
	** Function name:     	bentoRegister
	** Descriptions:	             �������ʼ���ӿ�
	** input parameters:    portName ���ں� ����/dev/tty0
	** output parameters:   ��
	** Returned value:      1:��ʼ���ɹ�      -1:��ʼ��ʧ�� (ʧ��ԭ��Ϊ���ڴ�ʧ��)
	*********************************************************************************************************/
	public  static int bentoRegister(String portName)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenopen>>]"+portName);		
		return ev.bentoRegister(portName);
	}
	
	
	
	/*********************************************************************************************************
	** Function name:     	bentoRelease
	** Descriptions:	             �������ͷ���Դ�ӿڣ���bentoRegister���׵��ã�
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      1:�ɹ�      �����ñض��ɹ���
	*********************************************************************************************************/
	public  static int bentoRelease()
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenclose>>]");		
		return ev.bentoRelease();
	}
	

	/*********************************************************************************************************
	** Function name:     	bentoOpen
	** Descriptions:	             �����񿪸��ӽӿ�
	** input parameters:    cabinet:���     box:���Ӻ�
	** output parameters:   ��
	** Returned value:      1:�򿪳ɹ�      0:��ʧ��
	*********************************************************************************************************/
	public static int bentoOpen(int cabinet,int box)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenopen>>]"+cabinet+box);
		return ev.bentoOpen(cabinet,box);
	}
	
	
	/*********************************************************************************************************
	** Function name:     	bentoLight
	** Descriptions:	             �������������ƽӿ�
	** input parameters:    cabinet:���     flag:1 ��ʾ������    0:��ʾ������
	** output parameters:   ��
	** Returned value:      1:�򿪳ɹ�      0:��ʧ��
	*********************************************************************************************************/
	public  static int bentoLight(int cabinet,int flag)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenopen>>]"+cabinet+" "+flag);
		return ev.bentoLight(cabinet,flag);
	}
	
	/*********************************************************************************************************
	** Function name:     	bentoCheck
	** Descriptions:	             ������״̬��ѯ�ӿ�
	** input parameters:    cabinet:���     
	** output parameters:   ��
	** Returned value:      ���ص���һ��JSON�� ����{{}}
	*********************************************************************************************************/
	public  static Map<String,Integer> bentoCheck(int cabinet) throws Exception
	{
		Map<String,Integer> allSetbent = new LinkedHashMap<String,Integer>() ;
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenopen>>]"+cabinet);
		String ret=ev.bentoCheck(cabinet);
		ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<"+ret);
		
		
		JSONObject jsonObject = new JSONObject(ret); 
		//����keyȡ������
		JSONObject ev_head = (JSONObject) jsonObject.getJSONObject("EV_json");
		JSONArray arr=ev_head.getJSONArray("column");//����json����
		//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<arr="+arr);
		//������Ϣ
		allSetbent.clear();	
		for(int i=0;i<arr.length();i++)
		{
			JSONObject object2=arr.getJSONObject(i);
			allSetbent.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
		}
		//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<bent="+allSetbent);
		return allSetbent;
	}
				
	/*********************************************************************************************************
	** Function name:     	payout
	** Descriptions:	    VMC���ҽӿ�
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	** input parameters:    value:Ҫ���ҵĽ��(��λ:��)
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public static int payout(long value)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIpayout>>]"+value);
		return ev.payout((int)value);		
	}
	
	/*********************************************************************************************************
	** Function name:     	payback
	** Descriptions:	    VMC�˱ҽӿ�
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������صĽ�����н���
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public  static int payback()
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIback>>]");
		return ev.payback();		
	}
	
	/*********************************************************************************************************
	** Function name:     	cashControl
	** Descriptions:	             �����ֽ��豸 ֱ�ӷ��� �����лص�
	** input parameters:    flag 1:�����ֽ��豸  0�ر��ֽ��豸
	** output parameters:   ��
	** Returned value:      1�ɹ�  0ʧ��
	*********************************************************************************************************/
	public  static int cashControl(int flag)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIcashControl>>]"+flag);
		return ev.cashControl(flag);		
	}
			
}
