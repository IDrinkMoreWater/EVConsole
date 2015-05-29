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
	//static EVprotocol ev=null;
	private static JNIInterface callBack=null;//��activity����ע��ص�
	private static Call_json callJson = new Call_json();//��JNI����ע��ص�	
	private static Map<String,Object> allSet = new LinkedHashMap<String,Object>() ;
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
	
	public static final int EV_NONE 		= 0;	//��������
	public static final int EV_REGISTER 	= 1;	//����ע��
	public static final int EV_RELEASE 	= 2;	//�����ͷ�
	
	//=====================��ݹ�����==============================================================================
	public static final int EV_BENTO_OPEN 	= 11;	//��ݹ���
	public static final int EV_BENTO_CHECK = 12;	//��ݹ��ѯ
	public static final int EV_BENTO_LIGHT = 13;	//��ݹ�����
	public static final int EV_BENTO_COOL 	= 14;	//��ݹ�����
	public static final int EV_BENTO_HOT 	= 15;	//��ݹ����
	
	//=====================MDB�ֽ�ģ������==============================================================================
	public static final int EV_MDB_INIT 	= 21;	//MDB�豸��ʼ��
	public static final int EV_MDB_ENABLE 	= 22;	//MDB�豸ʹ��
	public static final int EV_MDB_HEART 	= 23;	//MDB�豸����
	public static final int EV_MDB_B_INFO 	= 24;	//MDBֽ������Ϣ
	public static final int EV_MDB_C_INFO 	= 25;	//MDBӲ������Ϣ
	public static final int EV_MDB_COST 	= 26;	//MDB�豸�ۿ�
	public static final int EV_MDB_PAYBACK = 27;	//MDB�豸�˱�
	public static final int EV_MDB_PAYOUT 	= 28;	//MDB�豸�ұ�
	
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
						int str_evType =  ev_head.getInt("EV_type");					
					    ToolClass.Log(ToolClass.INFO,"EV_JNI",String.valueOf(str_evType));
					    switch(str_evType)
					    {
						    case EV_REGISTER://����ע��
						    	if(ev_head.getInt("port_id")>=0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_REGISTER);
									allSet.put("port_com", ev_head.getString("port"));
									allSet.put("port_id", ev_head.getInt("port_id"));
									callBack.jniCallback(allSet);
						    	}
						    	break;
						    case EV_BENTO_OPEN://��ݹ���
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_BENTO_OPEN);
									allSet.put("addr", ev_head.getInt("addr"));//���ӵ�ַ
									allSet.put("box", ev_head.getInt("box"));//���ӵ�ַ
									allSet.put("result", ev_head.getInt("result"));
									callBack.jniCallback(allSet);
						    	}
						    	break;
						    case EV_BENTO_CHECK://��ݹ��ѯ
								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����״̬�ϱ�");
								if(ev_head.getInt("is_success")>0)
								{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_BENTO_CHECK);
									allSet.put("cool", ev_head.getInt("cool"));
									allSet.put("hot", ev_head.getInt("hot"));
									allSet.put("light", ev_head.getInt("light"));
									JSONArray arr=ev_head.getJSONArray("column");//����json����
									//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
									for(int i=0;i<arr.length();i++)
									{
										JSONObject object2=arr.getJSONObject(i);
										allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
									}
									//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����3:"+allSet.toString());
									callBack.jniCallback(allSet);
								}
						    	break;
						    case EV_BENTO_LIGHT://��ݹ�����
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_BENTO_LIGHT);
									allSet.put("addr", ev_head.getInt("addr"));//���ӵ�ַ
									allSet.put("opt", ev_head.getInt("opt"));//�����ǹ�
									allSet.put("result", ev_head.getInt("result"));
									callBack.jniCallback(allSet);
						    	}
						    	break;
					    }
//					    if(str_evType.equals("EV_INITING"))//���ڳ�ʼ��
//						{
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<���ڳ�ʼ��");
//					    	EV_TYPE=EV_INITING;
//						}
//						else if(str_evType.equals("EV_ONLINE"))//str_evType.equals("EV_PAYOUT_RPT")
//						{
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�ɹ�����");
//							EV_TYPE=EV_ONLINE;
//							//��Activity�̷߳�����Ϣ
////							Message childmsg=mainHandler.obtainMessage();
////							childmsg.what=EV_ONLINE;
////							mainHandler.sendMessage(childmsg);
//							//���ӿڻص���Ϣ
//							allSet.clear();
//							allSet.put("EV_TYPE", EV_ONLINE);
//							callBack.jniCallback(allSet);
//						}
//						else if(str_evType.equals("EV_OFFLINE"))
//						{
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�Ͽ�����");
//							EV_TYPE=EV_OFFLINE;
//						}
//						else if(str_evType.equals("EV_RESTART"))
//						{
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<���ذ������Ķ�");
//							EV_TYPE=EV_RESTART;
//						}
//						else if(str_evType.equals("EV_STATE_RPT"))
//						{
//							int state = ev_head.getInt("vmcState");
//							if(state == 0)
//								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�Ͽ�����");
//							else if(state == 1)
//								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<���ڳ�ʼ��");
//							else if(state == 2)		
//							{
//								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����");	
//								//���ӿڻص���Ϣ
//								allSet.clear();
//								allSet.put("EV_TYPE", EV_ONLINE);
//								callBack.jniCallback(allSet);
//							}
//							else if(state == 3)
//								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����");
//							else if(state == 4)
//								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<ά��");
//						}
//						else if(str_evType.equals("EV_ENTER_MANTAIN"))
//						{
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<ά��");
//						}
//						else if(str_evType.equals("EV_EXIT_MANTAIN"))
//						{
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<�˳�ά��");
//						}
//					    //����
//						else if(str_evType.equals("EV_TRADE_RPT"))
//						{
//							EV_TYPE=EV_TRADE_RPT;
//							//�õ������ش�����
//							allSet.clear();	
//							allSet.put("EV_TYPE", EV_TRADE_RPT);
//							allSet.put("device", ev_head.getInt("cabinet"));//�������							
//							allSet.put("status", ev_head.getInt("result"));//�������
//							allSet.put("hdid", ev_head.getInt("column"));//����id
//							allSet.put("type", ev_head.getInt("type"));//��������
//							allSet.put("cost", ev_head.getInt("cost"));//��Ǯ
//							allSet.put("totalvalue", ev_head.getInt("remainAmount"));//ʣ����
//							allSet.put("huodao", ev_head.getInt("remainCount"));//ʣ��������				
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<��������");	
//							//��Activity�̷߳�����Ϣ
////							Message childmsg=mainHandler.obtainMessage();
////							childmsg.what=EV_TRADE_RPT;
////							childmsg.obj=allSet;
////							mainHandler.sendMessage(childmsg);		
//							//���ӿڻص���Ϣ
//							callBack.jniCallback(allSet);
//						}
//					    //����״̬�ϱ�
//						else if(str_evType.equals("EV_COLUMN_RPT"))
//						{
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����״̬�ϱ�");
//							EV_TYPE=EV_COLUMN_RPT;
//							JSONArray arr=ev_head.getJSONArray("column");//����json����
//							//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
//							//���ӿڻص���Ϣ
//							allSet.clear();	
//							allSet.put("EV_TYPE", EV_COLUMN_RPT);
//							for(int i=0;i<arr.length();i++)
//							{
//								JSONObject object2=arr.getJSONObject(i);
//								allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
//							}
//							//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����3:"+allSet.toString());
//							callBack.jniCallback(allSet);
//						}
//					    //Ͷ���ϱ�
//						else if(str_evType.equals("EV_PAYIN_RPT"))//Ͷ���ϱ�
//						{
//							int payin_amount= ev_head.getInt("payin_amount");
//							int reamin_amount = ev_head.getInt("reamin_amount");
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<Ͷ��:"+Integer.toString(payin_amount)
//									+"�ܹ�:"+Integer.toString(reamin_amount));							
//							
//							//��Activity�̷߳�����Ϣ
////							Message childmsg=mainHandler.obtainMessage();
////							childmsg.what=EV_PAYIN_RPT;
////							childmsg.obj=amount;
////							mainHandler.sendMessage(childmsg);	
//							//���ӿڻص���Ϣ
//							allSet.clear();	
//							allSet.put("EV_TYPE", EV_PAYIN_RPT);
//							allSet.put("payin_amount", payin_amount);
//							allSet.put("reamin_amount", reamin_amount);
//							callBack.jniCallback(allSet);
//						}
//					    //�������ϱ�
//						else if(str_evType.equals("EV_PAYOUT_RPT"))
//						{
//							int payout_amount= ev_head.getInt("payout_amount");
//							int reamin_amount = ev_head.getInt("reamin_amount");
//							ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����:"+Integer.toString(payout_amount)
//									+"ʣ��:"+Integer.toString(reamin_amount));	//��Activity�̷߳�����Ϣ
//							
////							Message childmsg=mainHandler.obtainMessage();
////							childmsg.what=EV_PAYOUT_RPT;
////							childmsg.obj=amount;
////							mainHandler.sendMessage(childmsg);
//							//���ӿڻص���Ϣ
//							allSet.clear();	
//							allSet.put("EV_TYPE", EV_PAYOUT_RPT);
//							allSet.put("payout_amount", payout_amount);
//							allSet.put("reamin_amount", reamin_amount);
//							callBack.jniCallback(allSet);
//						}
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
	** Descriptions:	    ���������ӿ�
	** input parameters:    
	** output parameters:   ��
	** Returned value:      
	*********************************************************************************************************/
	public static void vmcEVStart()
	{
		EVprotocol.addListener(callJson);//ע����jni�ļ����ӿ�
	}
	/*********************************************************************************************************
	** Function name:     	vmcEVStop
	** Descriptions:	    �����ر�
	** input parameters:    
	** output parameters:   ��
	** Returned value:      
	*********************************************************************************************************/
	public static void vmcEVStop()
	{
		EVprotocol.removeListener(callJson);
	}
	
	/*********************************************************************************************************
	** Function name:     	bentoRegister
	** Descriptions		:		����ע��ӿ� 	[�첽]
	** input parameters	:       portName ���ں� ����"COM1"
	** output parameters:		��
	** Returned value	:		1��ָ��ͳɹ�  0��ָ���ʧ��
	*  ����Ľ��ͨ���ص�����json�� 		 ���磺 EV_JSON={"EV_json":{"EV_type":1,"port":"/dev/ttymxc2","port_id":0}}
	*  							"EV_type" = EV_REGISTER = 1;��ʾ����ע�������
	*                           "port"    ��ʾ���صĴ��ں�,����portName��ֵ
	*							"port_id":��ʾ���صĴ��ڱ�ţ����ʧ���򷵻� -1
	*********************************************************************************************************/
	public  static int bentoRegister(String portName)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenopen>>]"+portName);		
		return EVprotocol.EV_portRegister(portName);		
	}
	
	
	
	/*********************************************************************************************************
	** Function name:     	bentoRelease
	** Descriptions		:		�����ͷŽӿ�  [�첽]
	** input parameters	:       port_id ���ڱ��
	** output parameters:		��
	** Returned value	:		1��ָ��ͳɹ�  0��ָ���ʧ��
	*	����Ľ��ͨ���ص�����json��	  ���磺 EV_JSON={"EV_json":{"EV_type":2,"result":1}}
	*							"EV_type"= EV_RELEASE = 2; ��ʾ�����ͷŰ�����
	*							"result":��ʾ�������    1:��ʾ�ɹ��ͷ�   0:��ʾ�ͷ�ʧ��
	*********************************************************************************************************/
	public  static int bentoRelease(int port_id)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenclose>>]"+port_id);		
		return EVprotocol.EV_portRelease(port_id);		
	}
	
	
	
	
	//��ͨ����
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
		//return ev.getColumn(cabinet);		
		return 0;
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
		//return ev.trade(cabinet,column,type,(int)cost);
		return 0;
	}
	
		

	
	//��ݹ�
	/*********************************************************************************************************
	** Function name:     	bentoOpen
	** Descriptions		:		��ݹ��Žӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��, addr:���ӵ�ַ 00-15,box:���ŵĸ��Ӻ� 1-88
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":11,"port_id":0,"addr":0,"box":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_OPEN = 11; ���Ž����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": 	��ʾ������	1:���ųɹ�   0:����ʧ��
	*********************************************************************************************************/
	public static int bentoOpen(int port_id,int addr,int box)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[bentoCheck>>port_id=]"+port_id+"[addr]="+addr+"[box]="+box);
		return EVprotocol.EV_bentoOpen(port_id,addr,box);
	}
	
	
	/*********************************************************************************************************
	** Function name:     	bentoLight
	** Descriptions		:		��ݹ��������ƽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16,opt:���������� 1:��  0:��
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":13,"port_id":0,"addr":0,"opt":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_LIGHT = 13: ���������ƽ����Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ,"opt":ԭ�����ز���.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": ��ʾ������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  static int bentoLight(int port_id,int addr,int opt)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[bentoCheck>>port_id=]"+port_id+"[addr]="+addr+"[opt]="+opt);
		return EVprotocol.EV_bentoLight(port_id,addr,opt);
	}
	
	/*********************************************************************************************************
	** Function name:     	bentoCheck
	** Descriptions		:		��ݹ��ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":12,"port_id":0,"addr":0,"is_success":1,"ID":"xxxxxxxxx1",
	*								"cool":0,"hot":0,"light":1,"sum":88,[]}}
	*							"EV_type"= EV_BENTO_CHECK = 12: ���ѯ�����Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"ID":�������ID��
	*							"cool":�Ƿ�֧������ 	 	1:֧�� 0:��֧��
	*							"hot":�Ƿ�֧�ּ���  		1:֧�� 0:��֧��
	*							"light":�Ƿ�֧������  	1:֧�� 0:��֧��
	*							"sum":��������	�����������88 ��Ĭ�� ������� 1-88
	*********************************************************************************************************/
	public  static int bentoCheck(int port_id,int addr)
	{				
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[bentoCheck>>port_id=]"+port_id+"[addr]="+addr);
		return EVprotocol.EV_bentoCheck(port_id,addr);
	}
	
	
	//�ֽ�֧���豸			
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
		//return ev.payout((int)value);		
		return 0;
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
		//return ev.payback();	
		return 0;
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
		//return ev.cashControl(flag);		
		return 0;
	}
			
}
