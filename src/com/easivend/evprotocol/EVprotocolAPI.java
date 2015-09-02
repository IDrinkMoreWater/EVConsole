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
import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.ToolClass;
import com.easivend.evprotocol.EVprotocol.EV_listener;
import com.easivend.evprotocol.EVprotocol.RequestObject;
import com.easivend.http.EVServerhttp;

import android.content.Intent;
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
	public static final int EV_MDB_B_CON 	= 29;	//MDBֽ��������
	public static final int EV_MDB_C_CON 	= 30;	//MDBӲ��������
	public static final int EV_MDB_HP_PAYOUT 	= 31;	//hopperӲ��������
	
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
					ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<"+msg.obj.toString(),"log.txt");
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
					    //ToolClass.Log(ToolClass.INFO,"EV_JNI",String.valueOf(str_evType));
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
						    	else
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_REGISTER);
									allSet.put("port_com", ev_head.getString("port"));
									allSet.put("port_id", ev_head.getInt("port_id"));
									callBack.jniCallback(allSet);
						    	}
						    	break;
						    	
						    //��ݹ��豸	
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
						    	else
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_BENTO_OPEN);
									allSet.put("addr", 0);//���ӵ�ַ
									allSet.put("box", 0);//���ӵ�ַ
									allSet.put("result", 0);
									callBack.jniCallback(allSet);
						    	}
						    	break;
						    case EV_BENTO_CHECK://��ݹ��ѯ
								ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����״̬�ϱ�","log.txt");
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
								else
								{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_BENTO_CHECK);
									allSet.put("cool", 0);
									allSet.put("hot", 0);
									allSet.put("light", 0);
//									JSONArray arr=ev_head.getJSONArray("column");//����json����
//									//ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<����2:"+arr.toString());
//									for(int i=0;i<arr.length();i++)
//									{
//										JSONObject object2=arr.getJSONObject(i);
//										allSet.put(String.valueOf(object2.getInt("no")), object2.getInt("state"));								
//									}
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
						    	else 
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_BENTO_LIGHT);
									allSet.put("addr", 0);//���ӵ�ַ
									allSet.put("opt", 0);//�����ǹ�
									allSet.put("result", 0);
									callBack.jniCallback(allSet);
						    	}
						    	break;
						    	
						    //�ֽ��豸	
						    case EV_MDB_ENABLE:	//ʹ�ܽӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_ENABLE);
									allSet.put("bill_result", ev_head.getInt("bill_result"));
									allSet.put("coin_result", ev_head.getInt("coin_result"));
									callBack.jniCallback(allSet);
						    	}
						    	else
						    	{
						    		//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_ENABLE);
						    		allSet.put("bill_result", 0);
									allSet.put("coin_result", 0);
									callBack.jniCallback(allSet);
								}
						    	break;
						    case EV_MDB_B_INFO://ֽ������ѯ�ӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_B_INFO);
									allSet.put("acceptor", ev_head.getInt("acceptor"));
									allSet.put("dispenser", ev_head.getInt("dispenser"));
									allSet.put("code", ev_head.getString("code"));
									allSet.put("sn", ev_head.getString("sn"));
									allSet.put("model", ev_head.getString("model"));
									allSet.put("ver", ev_head.getString("ver"));
									allSet.put("capacity", ev_head.getInt("capacity"));
									JSONArray arr1=ev_head.getJSONArray("ch_r");//����json����
									Map<String,Integer> allSet1 = new LinkedHashMap<String,Integer>() ;
									for(int i=0;i<arr1.length();i++)
									{
										JSONObject object2=arr1.getJSONObject(i);
										allSet1.put(String.valueOf(object2.getInt("ch")), object2.getInt("value"));								
									}
									allSet.put("ch_r", allSet1);
									
									JSONArray arr2=ev_head.getJSONArray("ch_d");//����json����
									Map<String,Integer> allSet2 = new LinkedHashMap<String,Integer>() ;
									for(int i=0;i<arr2.length();i++)
									{
										JSONObject object2=arr2.getJSONObject(i);
										allSet2.put(String.valueOf(object2.getInt("ch")), object2.getInt("value"));								
									}
									allSet.put("ch_d", allSet2);
									callBack.jniCallback(allSet);
						    	}
						    	else
						    	{
						    		
								}
						    	break;
						    case EV_MDB_B_CON://ֽ�������ýӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
						    		allSet.clear();
						    	}
						    	else
						    	{
						    		
								}
						    	break;
						    case EV_MDB_C_INFO://Ӳ������ѯ�ӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_C_INFO);
									allSet.put("acceptor", ev_head.getInt("acceptor"));
									allSet.put("dispenser", ev_head.getInt("dispenser"));
									allSet.put("code", ev_head.getString("code"));
									allSet.put("sn", ev_head.getString("sn"));
									allSet.put("model", ev_head.getString("model"));
									allSet.put("ver", ev_head.getString("ver"));
									allSet.put("capacity", ev_head.getInt("capacity"));
									JSONArray arr1=ev_head.getJSONArray("ch_r");//����json����
									Map<String,Integer> allSet1 = new LinkedHashMap<String,Integer>() ;
									for(int i=0;i<arr1.length();i++)
									{
										JSONObject object2=arr1.getJSONObject(i);
										allSet1.put(String.valueOf(object2.getInt("ch")), object2.getInt("value"));								
									}
									allSet.put("ch_r", allSet1);
									
									JSONArray arr2=ev_head.getJSONArray("ch_d");//����json����
									Map<String,Integer> allSet2 = new LinkedHashMap<String,Integer>() ;
									for(int i=0;i<arr2.length();i++)
									{
										JSONObject object2=arr2.getJSONObject(i);
										allSet2.put(String.valueOf(object2.getInt("ch")), object2.getInt("value"));								
									}
									allSet.put("ch_d", allSet2);
									callBack.jniCallback(allSet);
						    	}
						    	else
						    	{
						    		
								}
						    	break;
						    case EV_MDB_C_CON://Ӳ�������ýӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
						    		allSet.clear();
						    	}
						    	else
						    	{
						    		
								}
						    	break;	
						    case EV_MDB_PAYOUT://�ұҽӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_PAYOUT);
									allSet.put("result", ev_head.getInt("result"));
									allSet.put("bill_changed", ev_head.getInt("bill_changed"));
									allSet.put("coin_changed", ev_head.getInt("coin_changed"));
									callBack.jniCallback(allSet);
						    	}	
						    	else
						    	{
						    		//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_PAYOUT);
									allSet.put("result", 0);
						    		allSet.put("bill_changed", 0);
									allSet.put("coin_changed", 0);
									callBack.jniCallback(allSet);
								}
						    	break;
						    case EV_MDB_HP_PAYOUT://Hopper�ұҽӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_HP_PAYOUT);
									allSet.put("result", ev_head.getInt("result"));
									allSet.put("changed", ev_head.getInt("changed"));
									callBack.jniCallback(allSet);
						    	}
						    	else
						    	{
						    		//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_HP_PAYOUT);
									allSet.put("result", 0);
						    		allSet.put("changed", 0);
						    		callBack.jniCallback(allSet);
								}
						    	break;	
						    //����ҳ��ʹ��	
						    case EV_MDB_HEART://�ֽ�������ѯ�ӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_HEART);
									allSet.put("bill_enable", ev_head.getInt("bill_enable"));
									allSet.put("bill_payback", ev_head.getInt("bill_payback"));
									allSet.put("bill_err", ev_head.getInt("bill_err"));
									allSet.put("bill_recv", ev_head.getInt("bill_recv"));
									allSet.put("bill_remain", ev_head.getInt("bill_remain"));
									allSet.put("coin_enable", ev_head.getInt("coin_enable"));
									allSet.put("coin_payback", ev_head.getInt("coin_payback"));
									allSet.put("coin_err", ev_head.getInt("coin_err"));
									allSet.put("coin_recv", ev_head.getInt("coin_recv"));
									allSet.put("coin_remain", ev_head.getInt("coin_remain"));
									JSONObject object2=ev_head.getJSONObject("hopper");//����json����
									allSet.put("hopper1", object2.getInt("hopper1"));
									allSet.put("hopper2", object2.getInt("hopper2"));
									allSet.put("hopper3", object2.getInt("hopper3"));
									allSet.put("hopper4", object2.getInt("hopper4"));
									allSet.put("hopper5", object2.getInt("hopper5"));
									allSet.put("hopper6", object2.getInt("hopper6"));
									allSet.put("hopper7", object2.getInt("hopper7"));
									allSet.put("hopper8", object2.getInt("hopper8"));
									callBack.jniCallback(allSet);
						    	}
						    	else
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_HEART);
									allSet.put("bill_enable", 0);
									allSet.put("bill_payback", 0);
									allSet.put("bill_err", 1);
									allSet.put("bill_recv", 0);
									allSet.put("bill_remain", 0);
									allSet.put("coin_enable", 0);
									allSet.put("coin_payback", 0);
									allSet.put("coin_err", 1);
									allSet.put("coin_recv", 0);
									allSet.put("coin_remain", 0);
									allSet.put("hopper1", 3);
									allSet.put("hopper2", 3);
									allSet.put("hopper3", 3);
									allSet.put("hopper4", 3);
									allSet.put("hopper5", 3);
									allSet.put("hopper6", 3);
									allSet.put("hopper7", 3);
									allSet.put("hopper8", 3);
									callBack.jniCallback(allSet);
						    	}	
						    	break;
						    case EV_MDB_COST:
//						    	if(ev_head.getInt("is_success")>0)
//						    	{
//									//���ӿڻص���Ϣ
//									allSet.clear();
//									allSet.put("EV_TYPE", EV_MDB_COST);
//									allSet.put("result", ev_head.getInt("result"));
//									allSet.put("bill_recv", ev_head.getInt("bill_recv"));
//									allSet.put("coin_recv", ev_head.getInt("coin_recv"));									
//									callBack.jniCallback(allSet);
//						    	}
						    	break;
						    case EV_MDB_PAYBACK://�˱ҽӿ�
						    	if(ev_head.getInt("is_success")>0)
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_PAYBACK);
									allSet.put("result", ev_head.getInt("result"));
									allSet.put("bill_changed", ev_head.getInt("bill_changed"));
									allSet.put("coin_changed", ev_head.getInt("coin_changed"));									
									callBack.jniCallback(allSet);
						    	}
						    	else
						    	{
									//���ӿڻص���Ϣ
									allSet.clear();
									allSet.put("EV_TYPE", EV_MDB_PAYBACK);
									allSet.put("result", 0);
									allSet.put("bill_changed", 0);
									allSet.put("coin_changed", 0);									
									callBack.jniCallback(allSet);
						    	}
						    	break;	
					    }

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
	** Function name	:		EV_portRegister
	** Descriptions		:		����ע��ӿ� 	[�첽]
	** input parameters	:       portName ���ں� ����"COM1"
	** output parameters:		��
	** Returned value	:		1��ָ��ͳɹ�  0��ָ���ʧ��
	*  ����Ľ��ͨ���ص�����json�� 		 ���磺 EV_JSON={"EV_json":{"EV_type":1,"port":"/dev/ttymxc2","port_id":0}}
	*  							"EV_type" = EV_REGISTER = 1;��ʾ����ע�������
	*                           "port"    ��ʾ���صĴ��ں�,����portName��ֵ
	*							"port_id":��ʾ���صĴ��ڱ�ţ����ʧ���򷵻� -1
	*********************************************************************************************************/
	public static int EV_portRegister(String portName)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenopen>>]"+portName,"log.txt");		
		return EVprotocol.EV_portRegister(portName);		
	}
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_portRelease
	** Descriptions		:		�����ͷŽӿ�  [�첽]
	** input parameters	:       port_id ���ڱ��
	** output parameters:		��
	** Returned value	:		1��ָ��ͳɹ�  0��ָ���ʧ��
	*	����Ľ��ͨ���ص�����json��	  ���磺 EV_JSON={"EV_json":{"EV_type":2,"result":1}}
	*							"EV_type"= EV_RELEASE = 2; ��ʾ�����ͷŰ�����
	*							"result":��ʾ�������    1:��ʾ�ɹ��ͷ�   0:��ʾ�ͷ�ʧ��
	*********************************************************************************************************/
	public  static int EV_portRelease(int port_id)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIbenclose>>]"+port_id,"log.txt");		
		return EVprotocol.EV_portRelease(port_id);		
	}
	
	
	
	//��ݹ�
	/*********************************************************************************************************
	** Function name	:		EV_bentoOpen
	** Descriptions		:		��ݹ��Žӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��, addr:���ӵ�ַ 01-16,box:���ŵĸ��Ӻ� 1-88
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":11,"port_id":0,"addr":1,"box":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_OPEN = 11; ���Ž����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": 	��ʾ������	1:���ųɹ�   0:����ʧ��
	*********************************************************************************************************/
	public  static int EV_bentoOpen(int port_id,int addr,int box)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[bentoCheck>>port_id=]"+port_id+"[addr]="+addr+"[box]="+box,"log.txt");
		return EVprotocol.EV_bentoOpen(port_id,addr,box);
	}
	
	/*********************************************************************************************************
	** Function name	:		EV_bentoCheck
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
	*							"sum":��������	�����������88 ��Ĭ�� ������� 1-88,״̬1����,0����
	*********************************************************************************************************/
	public  static int EV_bentoCheck(int port_id,int addr)
	{				
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[bentoCheck>>port_id=]"+port_id+"[addr]="+addr,"log.txt");
		return EVprotocol.EV_bentoCheck(port_id,addr);
	}
	
	/*********************************************************************************************************
	** Function name	:		EV_bentoLight
	** Descriptions		:		��ݹ��������ƽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16,opt:���������� 1:��  0:��
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":13,"port_id":0,"addr":1,"opt":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_LIGHT = 13: ���������ƽ����Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ,"opt":ԭ�����ز���.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": ��ʾ������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  static int EV_bentoLight(int port_id,int addr,int opt)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[bentoCheck>>port_id=]"+port_id+"[addr]="+addr+"[opt]="+opt,"log.txt");
		return EVprotocol.EV_bentoLight(port_id,addr,opt);
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
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIgetColumn>>]"+cabinet,"log.txt");
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
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIhuo>>]"+cost,"log.txt");
		//return ev.trade(cabinet,column,type,(int)cost);
		return 0;
	}
	
		
	
	//�ֽ�֧���豸		
	/*********************************************************************************************************
	** Function name	:		EV_mdbEnable
	** Descriptions		:		�ձ��豸ʹ�ܽ��ܽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��; bill:����ֽ����  1:���� 0:������;coin:����Ӳ����  1:����,0:������ ;opt:���� 1:ʹ�� 0:����
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":22,"port_id":0,"bill":1,"coin":1,"opt":1,"is_success":1,"bill_result":1,"coin_result":1}}
	*							"EV_type"= EV_MDB_ENABLE = 22: ��MDBʹ�ܽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ�����ع��ӵ�ַ,"coin":ԭ�����ز���;"opt":ԭ������
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_result": ֽ����������	1:�ɹ�   0:ʧ��
	*							"coin_result": ֽ����������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  static int EV_mdbEnable(int port_id,int bill,int coin,int opt)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[mdbEnable>>port_id=]"+port_id+"[bill]="+bill+"[coin]="+coin+"[opt]="+opt,"log.txt");
		return EVprotocol.EV_mdbEnable(port_id,bill,coin,opt);
	}
	/*********************************************************************************************************
	** Function name	:		EVmdbBillInfoCheck
	** Descriptions		:		MDBֽ������ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":24,"port_id":0,"is_success":1,
	*							"acceptor":2,"dispenser":2,"code":"ITL","sn":"12312....","model":"NV9",
	*							"ver":"1212","capacity":500,"ch_r":[],"ch_d":[]}}
	*							"EV_type"= EV_MDB_B_INFO = 24: ��ʾMDBֽ������Ϣ��ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"acceptor": ֽ�ҽ�����Э������	0:�������ر�	2:MDBЭ�������
	*							"dispenser": ֽ��������Э������	0:�������ر�	2:MDBЭ��������
	*							"code": ֽ�������̴���			���磺ITL
	*							"sn":ֽ�������к�
	*							"model":ֽ�����ͺ�				����:NV9
	*							ver:ֽ��������汾��
	*							capacity:ֽ����������	
	*							ch_r:ֽ����������ͨ����ֵ 		�Է�Ϊ��λ
	*							ch_d:ֽ����������ͨ����ֵ		�Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbBillInfoCheck(int port_id)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[mdbbillInfo>>port_id=]"+port_id,"log.txt");
		return EVprotocol.EV_mdbBillInfoCheck(port_id);
	}
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbBillConfig
	** Descriptions		:		MDBֽ��������  [�첽]
	** input parameters	:       port_id:���ڱ��;req:���ò��� json��
 								EV_JSON={"EV_json":{"EV_type":29,"port_id":0,"acceptor":2,"dispenser":2,
 								"ch_r":[{"ch":1,"value":100},{"ch":2,"value":500},{"ch":3,"value":1000},{"ch":4,"value":2000},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}],
 								"ch_d":[{"ch":1,"value":0},{"ch":2,"value":0},{"ch":3,"value":0},{"ch":4,"value":0},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}]}}
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	�ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":29,"port_id":0,"acceptor":0,"dispenser":1,
	*							"is_success":1,"result":1}}
	*							"EV_type"= EV_MDB_B_CON = 29: ��ʾMDBֽ�����ý����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":���ؽ��	1:�ɹ�     0:ʧ��			
	*********************************************************************************************************/
	public  static int EV_mdbBillConfig(int port_id,int billtype)
	{
		JSONObject jsonObject = new JSONObject(); 
		JSONObject EV_json = new JSONObject(); 
		JSONArray ch_r=new JSONArray();
		JSONArray ch_d=new JSONArray();
		try {
			jsonObject.put("EV_type", EV_MDB_B_CON);
			jsonObject.put("port_id", port_id);
			jsonObject.put("acceptor", billtype);
			jsonObject.put("dispenser", billtype);
			for(int i=1;i<=16;i++)
			{
				JSONObject ch = new JSONObject(); 
				ch.put("ch", i);
				ch.put("value", 0);
				ch_r.put(ch);
			}
			jsonObject.put("ch_r", ch_r);
			for(int i=1;i<=16;i++)
			{
				JSONObject ch = new JSONObject(); 
				ch.put("ch", i);
				ch.put("value", 0);
				ch_d.put(ch);
			}
			jsonObject.put("ch_d", ch_d);
			EV_json.put("EV_json", jsonObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[BillConfig>>]"+EV_json.toString(),"log.txt");		
		return EVprotocol.EV_mdbBillConfig(EV_json.toString());
	}
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbCoinInfoCheck
	** Descriptions		:		MDBӲ������Ϣ��ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":25,"port_id":0,"is_success":1,
	*							"acceptor":2,"dispenser":2,"code":"MEI","sn":"12312....","model":"***",
	*							"ver":"1212","capacity":500,"ch_r":[],"ch_d":[]}}
	*							"EV_type"= EV_MDB_C_INFO = 25: ��ʾMDBӲ������Ϣ��ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"acceptor": Ӳ�ҽ�����Э������	0:�������ر�	1:����Ӳ������Э��  2:MDBЭ������� 3:����Ӳ������Э��
	*							"dispenser": Ӳ��������Э������	0:�������ر�	1:hopper ����232Э��  2:MDBЭ��������
	*							"code": Ӳ�������̴���			���磺MEI
	*							"sn":Ӳ�������к�
	*							"model":Ӳ�����ͺ�			
	*							ver:Ӳ��������汾��
	*							capacity:Ӳ����������	
	*							ch_r:Ӳ����������ͨ����ֵ 		�Է�Ϊ��λ
	*							ch_d:Ӳ����������ͨ����ֵ		�Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbCoinInfoCheck(int port_id)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[mdbcoinInfo>>port_id=]"+port_id,"log.txt");
		return EVprotocol.EV_mdbCoinInfoCheck(port_id);
	}
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbCoinConfig
	** Descriptions		:		Ӳ��������������  [�첽]
	** input parameters	:       port_id:���ڱ��;req:���ò��� json��
 								EV_JSON={"EV_json":{"EV_type":30,"port_id":0,"acceptor":2,"dispenser":2,"hight_en":0,
 								"ch_r":[{"ch":1,"value":100},{"ch":2,"value":500},{"ch":3,"value":1000},{"ch":4,"value":2000},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}],
 								"ch_d":[{"ch":1,"value":0},{"ch":2,"value":0},{"ch":3,"value":0},{"ch":4,"value":0},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}]}}
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*�ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":30,"port_id":0,"acceptor":0,"dispenser":1,
	*							"is_success":1,"result":1}}
	*							"EV_type"= EV_MDB_C_CON = 30: ��ʾMDBӲ�����ý����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ɹ�     0:ʧ��			
	*********************************************************************************************************/
	public  static int EV_mdbCoinConfig(int port_id,int cointype,int payouttype,Map<Integer, Integer>c_r,Map<Integer, Integer>c_d)
	{
		JSONObject jsonObject = new JSONObject(); 
		JSONObject EV_json = new JSONObject(); 
		JSONArray ch_r=new JSONArray();
		JSONArray ch_d=new JSONArray();
		try {
			jsonObject.put("EV_type", EV_MDB_C_CON);
			jsonObject.put("port_id", port_id);
			jsonObject.put("acceptor", cointype);
			jsonObject.put("dispenser", payouttype);
			jsonObject.put("hight_en", 0);
			//�������
	        Set<Map.Entry<Integer,Integer>> allset=c_r.entrySet();  //ʵ����
	        Iterator<Map.Entry<Integer,Integer>> iter=allset.iterator();
	        while(iter.hasNext())
	        {
	            Map.Entry<Integer,Integer> me=iter.next();
	            //System.out.println(me.getKey()+"--"+me.getValue());
	            JSONObject ch = new JSONObject(); 
				ch.put("ch", me.getKey());
				ch.put("value",me.getValue());
				ch_r.put(ch);
	        } 	        
			jsonObject.put("ch_r", ch_r);
			
			//�������
	        Set<Map.Entry<Integer,Integer>> allset2=c_d.entrySet();  //ʵ����
	        Iterator<Map.Entry<Integer,Integer>> iter2=allset2.iterator();
	        while(iter2.hasNext())
	        {
	            Map.Entry<Integer,Integer> me2=iter2.next();
	            //System.out.println(me.getKey()+"--"+me.getValue());
	            JSONObject ch = new JSONObject(); 
				ch.put("ch", me2.getKey());
				ch.put("value",me2.getValue());
				ch_d.put(ch);
	        } 	   
			jsonObject.put("ch_d", ch_d);
			EV_json.put("EV_json", jsonObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[CoinConfig>>]"+EV_json.toString(),"log.txt");	
		return EVprotocol.EV_mdbCoinConfig(EV_json.toString());		
	}
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbPayout
	** Descriptions		:		�ֽ��豸�ұҽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������;
	*							billPayout:ֽ�����·��ұҽ�� ��Ϊ��λ ;coinPayout:Ӳ�����·��ұҽ�� ��Ϊ��λ 
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":28,"port_id":0,"bill":0,"coin":1,
	*							"billPayout":0,"coinPayout":100,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_PAYOUT = 28: ��ʾMDB�ұҽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ������,"coin":ԭ������,"billPayout":ԭ������,"coinPayout":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ұҳɹ�     0:�ұ�ʧ��
	*							"bill_changed":ֽ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*							"coin_changed":Ӳ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbPayout(int port_id,int bill,int coin,int billPay,int coinPay)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[mdbPayout>>port_id=]"+port_id+"[bill]="+bill+"[coin]="+coin+"[billPay]="+billPay+"[coinPay]="+coinPay,"log.txt");
		return EVprotocol.EV_mdbPayout(port_id,bill,coin,billPay,coinPay);
	}
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbHopperPayout
	** Descriptions		:		Hopper�ұҽӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;no:hopper��� 1-8  nums:��Ҫ�ұҵ�ö�� ;
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	�ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":31,"port_id":0,"no":1,"nums":5,
	*							"is_success":1,"result":1,"changed":5}}
	*							"EV_type"= EV_MDB_HP_PAYOUT = 31: ��ʾhopper�ұҽ����Ӧ������
	*							"port_id":ԭ������,"no":ԭ������,"nums":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ұҳɹ�     0:�ұ�ʧ��
	*							"changed":ʵ������ö��
	*********************************************************************************************************/
	public static int EV_mdbHopperPayout(int port_id,int no,int nums)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[HopperPayout>>no=]"+no+" nums="+nums,"log.txt");	
		return EVprotocol.EV_mdbHopperPayout(port_id,no,nums);
	}
		
	
	//����ҳ��ʹ��
	/*********************************************************************************************************
	** Function name	:		EV_mdbHeart
	** Descriptions		:		�ֽ��豸������ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":23,"port_id":0,"is_success":1,
	*							"bill_enable":1,"bill_payback":0,"bill_err":0,"bill_recv":0,"bill_remain":0,
	*							"coin_enable":1,"coin_payback":0,"coin_err":0,"coin_recv":0,"coin_remain":0,
	*                           "hopper":{"hopper1":1,"hopper2":1,"hopper3":0,"hopper4":0,"hopper5":0,"hopper6":0,
	*                           "hopper7":0,"hopper8":0}
	*                           }}
	*							"EV_type"= EV_MDB_HEART = 23: ��ʾMDB������ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_enable": ֽ����ʹ��״̬		1:ʹ��   0:����
	*							"bill_payback": ֽ�����˱Ұ�ť����	1:����   0:�Ǵ���
	*							"bill_err":ֽ��������״̬			0:����   ��0 Ϊ������
	*							"bill_recv":ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
	*							"bill_remain":ֽ������ǰ���ҽ��	�Է�Ϊ��λ
	*	*						"coin_enable": Ӳ����ʹ��״̬		1:ʹ��   0:����
	*							"coin_payback": Ӳ�����˱Ұ�ť����	1:����   0:�Ǵ���
	*							"coin_err":Ӳ��������״̬			0:����   ��0 Ϊ������
	*							"coin_recv":Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
	*							"coin_remain":Ӳ������ǰ���ҽ��	�Է�Ϊ��λ
	*		                    "hopper":8��hopper��״̬,0����,1ȱ��,2����,3ͨѶ����
	*********************************************************************************************************/
	public  static int EV_mdbHeart(int port_id)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[mdbHeart>>port_id=]"+port_id,"log.txt");
		return EVprotocol.EV_mdbHeart(port_id);
	}
	/*********************************************************************************************************
	** Function name	:		EV_mdbCost
	** Descriptions		:		MDB�ۿ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;cost:�ۿ���  �Է�Ϊ��λ
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":26,"port_id":0,"cost":100,"is_success":1,
	*							"result":1,"bill_recv":0,"coin_recv":0}}
	*							"EV_type"= EV_MDB_COST = 26: ��ʾMDB�ۿ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ۿ�ɹ�     0:�ۿ�ʧ��
	*							"bill_recv":ֽ������ǰ�ձҽ��	  �Է�Ϊ��λ
	*							"coin_recv":Ӳ������ǰ�ձҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbCost(int port_id,int cost)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[mdbmdbCost>>port_id=]"+port_id+"[cost]="+cost,"log.txt");
		return EVprotocol.EV_mdbCost(port_id,cost);
	}
	/*********************************************************************************************************
	** Function name	:		EV_mdbPayback
	** Descriptions		:		MDB�˱ҽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":26,"port_id":0,"bill":1,"coin":1,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_PAYBACK = 27: ��ʾMDB�˱ҽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ������,"coin":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�˱ҳɹ�     0:�˱�ʧ��
	*							"bill_changed":ֽ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*							"coin_changed":Ӳ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbPayback(int port_id,int bill,int coin)
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","[mdbPayback>>port_id=]"+port_id+"[bill]="+bill+"[coin]="+coin,"log.txt");
		return EVprotocol.EV_mdbPayback(port_id,bill,coin);
	}
					
}
