package com.easivend.http;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.easivend.evprotocol.AlipayConfig;
import com.easivend.alipay.AlipaySubmit;
import com.easivend.evprotocol.ToolClass;
import com.easivend.alipay.HttpRequester;
import com.easivend.alipay.HttpRespons;

public class Zhifubaohttp implements Runnable
{
	private PostZhifubaoInterface callBack=null;//��activity����ע��ص�
	private final int SETMAIN=1;//what���,�������߳�
	private final int SETCHILD=2;//what���,�������߳�
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
		// TODO Auto-generated method stub
		Looper.prepare();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
		childhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what)
				{
				case SETCHILD://���߳̽������߳���Ϣ
						ToolClass.Log(ToolClass.INFO,"EV_JNI","[APIzhifubao>>]"+msg.obj.toString());
						//��֧�������������ͽ�����Ϣ
						Map<String, String> sPara = new HashMap<String, String>();
						 sPara.put("service","alipay.acquire.precreate");
						 sPara.put("partner","2088711021642556");//֧���� PID��
						 sPara.put("_input_charset","utf-8");//����		
						 sPara.put("seller_email", "2544282805@qq.com");//����֧�����ʻ�		 
						 sPara.put("product_code","QR_CODE_OFFLINE");//��ά��
						 sPara.put("total_fee","0.1");//�����ܽ��		   
						 sPara.put("out_trade_no","205211376305670");//�̻���վΨһ������		 
						 sPara.put("subject","��������");	 
					    String json=null;
					    try {
					    	JSONObject temp=new JSONObject();
							temp.put("goodsName","water");
							temp.put("quantity","1");
						    temp.put("price","0.1");
						    JSONArray singArray=new JSONArray();//�����������
						    singArray.put(temp);
						    json=singArray.toString();	
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					     sPara.put("goods_detail",json);	 
						 Map<String, String> map1 = AlipaySubmit.buildRequestPara(sPara);
						 Log.i("EV_JNI","Send1="+map1);
				       try {          	       	                    	       	
				           HttpRequester request = new HttpRequester();              
				           String url = "https://mapi.alipay.com/gateway.do?" + "_input_charset=" + AlipayConfig.input_charset;           
				           HttpRespons hr = request.sendPost(url, map1);
				           //result=hr.getContent();           
				           String strpicString=hr.getContent();	//�õ������ַ���
				           
				           
				           //ͨ��֧�����ṩ�Ķ���ֱ�����ɶ�ά��
				           String result=strpicString.substring(strpicString.indexOf("<qr_code>")+9, strpicString.indexOf("</qr_code>"));
				           Log.i("EV_JNI","rec1="+result);
				           Message tomain=mainhand.obtainMessage();
						   tomain.what=SETMAIN;
						   tomain.obj=result;
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
				           e.printStackTrace();  
				       }
						
						
					break;

				default:
					break;
				}
			}
			
		};
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
	}
}
