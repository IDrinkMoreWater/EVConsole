package com.easivend.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.easivend.alipay.AlipayConfig;
import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.alipay.AlipaySubmit;
import com.easivend.common.ToolClass;
import com.easivend.weixing.WeixingSubmit;
import com.easivend.alipay.HttpRequester;
import com.easivend.alipay.HttpRespons;

public class Zhifubaohttp implements Runnable
{
	//����
	public final static int SETCHILD=2;//what���,���͸����߳�֧��������
	public final static int SETMAIN=1;//what���,���͸����߳�֧��������ά��	
	public final static int SETFAILPROCHILD=5;//what���,���͸����߳̽���Э��ʧ��
	public final static int SETFAILBUSCHILD=6;//what���,���͸����߳̽�����Ϣʧ��
	public final static int SETFAILNETCHILD=4;//what���,���͸����߳̽�������
	//��ѯ
	public final static int SETQUERYCHILD=7;//what���,���͸����߳�֧������ѯ
	public final static int SETQUERYMAIN=8;//what���,���͸����̲߳�ѯ������ڸ���
	public final static int SETQUERYMAINSUCC=9;//what���,���͸����̲߳�ѯ�������ɹ�
	public final static int SETFAILQUERYPROCHILD=10;//what���,���͸����߳̽���Э��ʧ��
	public final static int SETFAILQUERYBUSCHILD=11;//what���,���͸����߳̽�����Ϣʧ��
	//�˿�
	public final static int SETPAYOUTCHILD=12;//what���,���͸����߳�֧�����˿�
	public final static int SETPAYOUTMAIN=13;//what���,���͸����߳��˿���
	public final static int SETFAILPAYOUTPROCHILD=14;//what���,���͸����߳̽���Э��ʧ��
	public final static int SETFAILPAYOUTBUSCHILD=15;//what���,���͸����߳̽�����Ϣʧ��
	//��������
	public final static int SETDELETECHILD=16;//what���,���͸����߳�֧������������
	public final static int SETDELETEMAIN=17;//what���,���͸����߳��˿���
	public final static int SETFAILDELETEPROCHILD=18;//what���,���͸����߳̽���Э��ʧ��
	public final static int SETFAILDELETEBUSCHILD=19;//what���,���͸����߳̽�����Ϣʧ��
	
	//private final int SETWEIMAIN=3;//what���,���߳̽��յ����߳�΢�Ž���ά��
	//private final int SETWEICHILD=4;//what���,���͸����߳�΢�Ž���
	private Handler mainhand=null,childhand=null;
	
	public Zhifubaohttp(Handler mainhand) {
		this.mainhand=mainhand;		
	}
	public Handler obtainHandler()
	{
		return this.childhand;
	}
	@Override
	public void run() {
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<zhifubaothread="+Thread.currentThread().getId(),"log.txt");
		// TODO Auto-generated method stub
		Looper.prepare();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
		childhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what)
				{
				case SETCHILD://���߳̽������߳���Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIzhifubao>>]["+Thread.currentThread().getId()+"]"+msg.obj.toString(),"log.txt");
						Map<String, String> sPara = new HashMap<String, String>();
						//1.��Ӷ�����Ϣ
						JSONObject ev=null;
						try {
							ev = new JSONObject(msg.obj.toString());							
							sPara.put("out_trade_no", ev.getString("out_trade_no"));//�������
							sPara.put("total_fee",ev.getString("total_fee"));//�����ܽ��		
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						sPara.put("subject","֧������ά��");//��������
						//��Ʒ��ϸ
						String json=null;
					    try {
					    	JSONObject temp=new JSONObject();
							temp.put("goodsName","��Ʒˮ");
							temp.put("quantity","1");
						    temp.put("price",ev.getString("total_fee"));
						    JSONArray singArray=new JSONArray();//�����������
						    singArray.put(temp);
						    json=singArray.toString();	
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    sPara.put("goods_detail",json);//��Ʒ��ϸ
					    sPara.put("it_b_pay","10m");//���׹ر�ʱ��
					    Log.i("EV_JNI","Send0.2="+sPara.toString());
						//2.����֧��������Ϣ
						Map<String, String> map1 = AlipayConfigAPI.PostAliBuy(sPara);
				        try {          	       	                    	       	
				           HttpRequester request = new HttpRequester();              
				           String url = "https://mapi.alipay.com/gateway.do?" + "_input_charset=" + AlipayConfig.getInput_charset();           
				           //3.����֧��������Ϣ
				           HttpRespons hr = request.sendPost(url, map1);
				           //4.�õ������ַ���
				           String strpicString=hr.getContent();	
				           Log.i("EV_JNI","rec1="+strpicString);
				           //5.������ص���Ϣ
				           InputStream is = new ByteArrayInputStream(strpicString.getBytes());// ��ȡ��������
				           Map<String, String> map2=AlipayConfigAPI.PendAliBuy(is);
				           Log.i("EV_JNI","rec2="+map2.toString());
				           //�����̷߳�����Ϣ
				           Message tomain=mainhand.obtainMessage();	
				           //Э��ʧ��
				           if(map2.get("is_success").equals("F"))
				           {
				        	   tomain.what=SETFAILPROCHILD;
							   tomain.obj=map2.get("error");
				           }
				           else
				           {
				        	   //ҵ����ʧ��
				        	   if(map2.get("result_code").equals("FAIL"))
					           {
					        	   tomain.what=SETFAILBUSCHILD;
								   tomain.obj=map2.get("detail_error_code")+map2.get("detail_error_des");
					           }
				        	   //ͨ��֧�����ṩ�Ķ���ֱ�����ɶ�ά��
				        	   else if(map2.get("result_code").equals("SUCCESS"))
					           {
					        	   tomain.what=SETMAIN;
								   tomain.obj=map2.get("qr_code");
					           }
				           }
				           Log.i("EV_JNI","rec3="+tomain.obj);				           
						   mainhand.sendMessage(tomain); // ������Ϣ
						   
						   
//				           //����ͼƬ
//				           result=strpicString.substring(strpicString.indexOf("<pic_url>")+9, strpicString.indexOf("</pic_url>"));
//						   //txt.setText(strpicString); // ������ݱ༭��	
//						   Log.i("EV_JNI","rec1="+result);		  		   
//						   HttpClient httpClient=new DefaultHttpClient();
//							HttpGet httprequest=new HttpGet(result);
//							HttpResponse httpResponse;
//							try {
//								httpResponse=httpClient.execute(httprequest);
//								if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
//									//result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
//									//ȡ�������Ϣ ȡ��HttpEntiy  
//					                HttpEntity httpEntity = httpResponse.getEntity();  
//					                //���һ��������  
//					                InputStream is = httpEntity.getContent();  
//					                bitmap = BitmapFactory.decodeStream(is);  
//					                is.close();  
//					                Message m = handler.obtainMessage(); // ��ȡһ��Message
//					                m.what=1;
//									handler.sendMessage(m); // ������Ϣ
//					                 
//								}else{
//									result = "����ʧ�ܣ�";
//								}
//							} catch (ClientProtocolException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
				       } catch (Exception e) {  
				           //e.printStackTrace();  
				    	   //�����̷߳�����Ϣ
				           Message tomain=mainhand.obtainMessage();	
				    	   tomain.what=SETFAILNETCHILD;
				    	   tomain.obj="netfail";
				    	   Log.i("EV_JNI","rec="+tomain.obj);				           
						   mainhand.sendMessage(tomain); // ������Ϣ
				       }
						
						
					break;
				case SETQUERYCHILD://���߳̽������߳���Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIzhifubao>>]["+Thread.currentThread().getId()+"]"+msg.obj.toString(),"log.txt");
					Map<String, String> sPara2 = new HashMap<String, String>();
					//1.��Ӷ�����Ϣ
					JSONObject ev2=null;
					try {
						ev2 = new JSONObject(msg.obj.toString());							
						sPara2.put("out_trade_no", ev2.getString("out_trade_no"));//�������
								
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
				    Log.i("EV_JNI","Send0.2="+sPara2.toString());
					//2.����֧��������Ϣ
					Map<String, String> map3 = AlipayConfigAPI.PostAliQuery(sPara2);
			        try {          	       	                    	       	
			           HttpRequester request = new HttpRequester();              
			           String url = "https://mapi.alipay.com/gateway.do?" + "_input_charset=" + AlipayConfig.getInput_charset();           
			           //3.����֧��������Ϣ
			           HttpRespons hr = request.sendPost(url, map3);
			           //4.�õ������ַ���
			           String strpicString=hr.getContent();	
			           Log.i("EV_JNI","rec1="+strpicString);
			           //5.������ص���Ϣ
			           InputStream is = new ByteArrayInputStream(strpicString.getBytes());// ��ȡ��������
			           Map<String, String> map4=AlipayConfigAPI.PendAliQuery(is);
			           Log.i("EV_JNI","rec2="+map4.toString());
			           //�����̷߳�����Ϣ
			           Message tomain=mainhand.obtainMessage();	
			           //Э��ʧ��
			           if(map4.get("is_success").equals("F"))
			           {
			        	   tomain.what=SETFAILQUERYPROCHILD;
						   tomain.obj=map4.get("error");
			           }
			           else
			           {
			        	   //ҵ����ʧ��
			        	   if(map4.get("result_code").equals("FAIL"))
				           {
				        	   tomain.what=SETFAILQUERYBUSCHILD;
							   tomain.obj=map4.get("detail_error_code")+map4.get("detail_error_des");
				           }
			        	   //���׳ɹ�״̬
			        	   else if(map4.get("result_code").equals("SUCCESS"))
				           {
			        		   //���ڵȴ�֧��
			        		   if(map4.get("trade_status").equals("WAIT_BUYER_PAY"))
			        		   {
					        	   tomain.what=SETQUERYMAIN;
								   tomain.obj=map4.get("trade_status");
			        		   }
			        		   //ͨ��֧�����ṩ�Ķ���ֱ�����ɶ�ά��
			        		   else if(map4.get("trade_status").equals("TRADE_SUCCESS"))
			        		   {
					        	   tomain.what=SETQUERYMAINSUCC;
								   tomain.obj=map4.get("trade_status");
			        		   }
				           }
			           }
			           Log.i("EV_JNI","rec3="+tomain.obj);				           
					   mainhand.sendMessage(tomain); // ������Ϣ
					   
			       } catch (Exception e) {  
			           e.printStackTrace();  
			       }
				break;
				case SETPAYOUTCHILD://���߳̽������߳���Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIzhifubao>>]["+Thread.currentThread().getId()+"]"+msg.obj.toString(),"log.txt");
					Map<String, String> sPara3 = new HashMap<String, String>();
					//1.��Ӷ�����Ϣ
					JSONObject ev3=null;
					try {
						ev3 = new JSONObject(msg.obj.toString());							
						sPara3.put("out_trade_no", ev3.getString("out_trade_no"));//�������
						sPara3.put("refund_amount", ev3.getString("refund_amount"));
						sPara3.put("out_request_no", ev3.getString("out_request_no"));
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
				    Log.i("EV_JNI","Send0.2="+sPara3.toString());
					//2.����֧��������Ϣ
					Map<String, String> map5 = AlipayConfigAPI.PostAliPayout(sPara3);
			        try {          	       	                    	       	
			           HttpRequester request = new HttpRequester();              
			           String url = "https://mapi.alipay.com/gateway.do?" + "_input_charset=" + AlipayConfig.getInput_charset();           
			           //3.����֧��������Ϣ
			           HttpRespons hr = request.sendPost(url, map5);
			           //4.�õ������ַ���
			           String strpicString=hr.getContent();	
			           Log.i("EV_JNI","rec1="+strpicString);
			           //5.������ص���Ϣ
			           InputStream is = new ByteArrayInputStream(strpicString.getBytes());// ��ȡ��������
			           Map<String, String> map6=AlipayConfigAPI.PendAliPayout(is);
			           Log.i("EV_JNI","rec2="+map6.toString());
			           //�����̷߳�����Ϣ
			           Message tomain=mainhand.obtainMessage();	
			           //Э��ʧ��
			           if(map6.get("is_success").equals("F"))
			           {
			        	   tomain.what=SETFAILPAYOUTPROCHILD;
						   tomain.obj=map6.get("error");
			           }
			           else
			           {
			        	   //ҵ����ʧ��
			        	   if(map6.get("result_code").equals("FAIL"))
				           {
				        	   tomain.what=SETFAILPAYOUTBUSCHILD;
							   tomain.obj=map6.get("detail_error_code")+map6.get("detail_error_des");
				           }
			        	   //ͨ��֧�����ṩ�Ķ���ֱ�����ɶ�ά��
			        	   else if(map6.get("result_code").equals("SUCCESS"))
				           {
				        	   tomain.what=SETPAYOUTMAIN;							   
				           }
			           }
			           Log.i("EV_JNI","rec3="+tomain.obj);				           
					   mainhand.sendMessage(tomain); // ������Ϣ
					   
			       } catch (Exception e) {  
			           e.printStackTrace();  
			       }
				break;	
				case SETDELETECHILD://���߳̽������߳���Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIzhifubao>>]["+Thread.currentThread().getId()+"]"+msg.obj.toString(),"log.txt");
					Map<String, String> sPara4 = new HashMap<String, String>();
					//1.��Ӷ�����Ϣ
					JSONObject ev4=null;
					try {
						ev4 = new JSONObject(msg.obj.toString());							
						sPara4.put("out_trade_no", ev4.getString("out_trade_no"));//�������
								
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
				    Log.i("EV_JNI","Send0.2="+sPara4.toString());
					//2.����֧��������Ϣ
					Map<String, String> map7 = AlipayConfigAPI.PostAliDelete(sPara4);
			        try {          	       	                    	       	
			           HttpRequester request = new HttpRequester();              
			           String url = "https://mapi.alipay.com/gateway.do?" + "_input_charset=" + AlipayConfig.getInput_charset();           
			           //3.����֧��������Ϣ
			           HttpRespons hr = request.sendPost(url, map7);
			           //4.�õ������ַ���
			           String strpicString=hr.getContent();	
			           Log.i("EV_JNI","rec1="+strpicString);
			           //5.������ص���Ϣ
			           InputStream is = new ByteArrayInputStream(strpicString.getBytes());// ��ȡ��������
			           Map<String, String> map8=AlipayConfigAPI.PendAliDelete(is);
			           Log.i("EV_JNI","rec2="+map8.toString());
			           //�����̷߳�����Ϣ
			           Message tomain=mainhand.obtainMessage();	
			           //Э��ʧ��
			           if(map8.get("is_success").equals("F"))
			           {
			        	   tomain.what=SETFAILDELETEPROCHILD;
						   tomain.obj=map8.get("error");
			           }
			           else
			           {
			        	   //ҵ����ʧ��
			        	   if(map8.get("result_code").equals("FAIL"))
				           {
				        	   tomain.what=SETFAILDELETEBUSCHILD;
							   tomain.obj=map8.get("detail_error_code")+map8.get("detail_error_des");
				           }
			        	   //ͨ��֧�����ṩ�Ķ���ֱ�����ɶ�ά��
			        	   else if(map8.get("result_code").equals("SUCCESS"))
				           {
				        	   tomain.what=SETDELETEMAIN;
							   tomain.obj=map8.get("trade_status");
				           }
			           }
			           Log.i("EV_JNI","rec3="+tomain.obj);				           
					   mainhand.sendMessage(tomain); // ������Ϣ
					   
			       } catch (Exception e) {  
			           e.printStackTrace();  
			       }
				break;
				
//				case SETWEICHILD://���߳̽������߳�΢����Ϣ
//					ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIweixing>>]"+msg.obj.toString());
//					Map<String, String> swPara = new HashMap<String, String>();
//					 
//					//����ʹ�õ����ӣ����������΢���ṩ��demo
////					 sPara.put("appid","wxd930ea5d5a258f4f");
////					 sPara.put("auth_code","123456");//�����ܽ��
////					 sPara.put("body","test");//����		
////					 sPara.put("device_info","123");//�̻���վΨһ������	
////					 sPara.put("mch_id","1900000109");
////					 sPara.put("nonce_str","960f228109051b9969f76c82bde183ac");	 
////					 sPara.put("out_trade_no", "1400755861");//����֧�����ʻ�
////					 sPara.put("spbill_create_ip", "127.0.0.1");//����֧�����ʻ�
////					 sPara.put("sub_mch_id", "124");
////					 sPara.put("total_fee", "1");//����֧�����ʻ�
////					 String sign=buildRequestPara(sPara);
////					 StringBuilder xml = new StringBuilder();
////				     xml.append("<xml>");
////				     xml.append("<appid>wxd930ea5d5a258f4f</appid>");
////				     xml.append("<auth_code>123456</auth_code>");
////				     xml.append("<body><![CDATA[test]]></body>");
////				     xml.append("<device_info>123</device_info>");
////				     xml.append("<mch_id>1900000109</mch_id>");
////				     xml.append("<nonce_str>960f228109051b9969f76c82bde183ac</nonce_str>");
////				     xml.append("<out_trade_no>1400755861</out_trade_no>");
////				     xml.append("<spbill_create_ip>127.0.0.1</spbill_create_ip>");
////				     xml.append("<sub_mch_id>124</sub_mch_id>");
////				     xml.append("<total_fee>1</total_fee>");
////				     xml.append("<sign><![CDATA["+sign+"]]></sign>");
////				     xml.append("</xml>");
//					
//					
//					swPara.put("appid","wx37a5d49081f487c4");
//					swPara.put("mch_id","10052966");
//					swPara.put("nonce_str","960f228109051b9969f76c82bde183ac");	 		 
//					swPara.put("body","test");//����		
//					swPara.put("out_trade_no", "1400755861");//����֧�����ʻ�
//					swPara.put("total_fee", "1");//����֧�����ʻ�
//					swPara.put("spbill_create_ip", "127.0.0.1");//����֧�����ʻ�
//					swPara.put("notify_url", "127.0.0.1");//����֧�����ʻ�
//					swPara.put("trade_type","NATIVE");//�̻���վΨһ������	
//					String key="1bd78d29964553116c7c405dd87b2072";
//					String sign=WeixingSubmit.buildRequestPara(swPara,key);
//					 
//					 StringBuilder xml = new StringBuilder();
//				     xml.append("<xml>");
//				     xml.append("<appid>wx37a5d49081f487c4</appid>");
//				     xml.append("<mch_id>10052966</mch_id>");
//				     xml.append("<nonce_str>960f228109051b9969f76c82bde183ac</nonce_str>");
//				     xml.append("<body><![CDATA[test]]></body>");
//				     xml.append("<out_trade_no>1400755861</out_trade_no>");
//				     xml.append("<total_fee>1</total_fee>");
//				     xml.append("<spbill_create_ip>127.0.0.1</spbill_create_ip>");	     
//				     xml.append("<notify_url>127.0.0.1</notify_url>");	     
//				     xml.append("<trade_type>NATIVE</trade_type>");
//				     xml.append("<sign><![CDATA["+sign+"]]></sign>");
//				     xml.append("</xml>");	
//					
//				     try {
//				            byte[] xmlbyte = xml.toString().getBytes("UTF-8");
//				            
//				            Log.i("EV_JNI","Send5="+xml);
//
//				            URL url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
//				            
//				            
//				            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//				            conn.setConnectTimeout(5000);
//				            conn.setDoOutput(true);// �������
//				            conn.setDoInput(true);
//				            conn.setUseCaches(false);// ��ʹ�û���
//				            conn.setRequestMethod("POST");
//				            conn.setRequestProperty("Connection", "Keep-Alive");// ά�ֳ�����
//				            conn.setRequestProperty("Charset", "UTF-8");
//				            conn.setRequestProperty("Content-Length",
//				                    String.valueOf(xmlbyte.length));
//				            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//				            conn.setRequestProperty("X-ClientType", "2");//�����Զ����ͷ��Ϣ
//
//				            conn.getOutputStream().write(xmlbyte);
//				            conn.getOutputStream().flush();
//				            conn.getOutputStream().close();
//
//
//				            if (conn.getResponseCode() != 200)
//				                throw new RuntimeException("����urlʧ��");
//
//				            InputStream is = conn.getInputStream();// ��ȡ��������
//				              
//
//				            // ʹ�������������ַ�(��ѡ)
//				            ByteArrayOutputStream out = new ByteArrayOutputStream();
//				            byte[] buf = new byte[1024];
//				            int len;
//				            while ((len = is.read(buf)) != -1) {
//				                out.write(buf, 0, len);
//				            }
//				            String strpicStringwei = out.toString("UTF-8");
//				            out.close();
//				            
//				           String resultwei=strpicStringwei.substring(strpicStringwei.indexOf("<code_url><![CDATA[")+19, strpicStringwei.indexOf("]]></code_url>"));
//				 		   //txt.setText(strpicString); // ������ݱ༭��	
//				 		   Log.i("EV_JNI","rec1="+resultwei);
//				 		   Message tomain=mainhand.obtainMessage();
//						   tomain.what=SETWEIMAIN;
//						   tomain.obj=resultwei;
//						   mainhand.sendMessage(tomain); // ������Ϣ
//				           
//				     } catch (Exception e) {
//				            // TODO Auto-generated catch block
//				            System.out.println(e);
//				        }    
//					break;
				default:
					break;
				}
			}
			
		};
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
	}
}
