package com.easivend.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.ToolClass;
import com.google.gson.Gson;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVServerhttp implements Runnable {
	public final static int SETCHILD=2;//what���,���͸����߳�ǩ��
	public final static int SETMAIN=1;//what���,���͸����߳�ǩ����ɷ���	
	public final static int SETERRFAILMAIN=4;//what���,���͸����߳�ǩ������ʧ�ܷ���	
	public final static int SETHEARTCHILD=5;//what���,���͸����߳�����
	public final static int SETCLASSCHILD=6;//what���,���͸����߳�����
	public final static int SETFAILMAIN=3;//what���,���͸����߳�����ʧ�ܷ���	
	String result = "";
	String Tok="";	
	String httpStr="";
	private Handler mainhand=null,childhand=null;
	
	public EVServerhttp(Handler mainhand) {
		this.mainhand=mainhand;		
	}
	public Handler obtainHandler()
	{
		return this.childhand;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		Looper.prepare();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread start","server.txt");
		childhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what)
				{
				case SETCHILD://���߳̽������߳�ǩ����Ϣ
					String vmc_no="";
					String vmc_auth_code="";
					//1.�õ����������Ϣ
					JSONObject ev=null;
					try {
						ev = new JSONObject(msg.obj.toString());
						vmc_no=ev.getString("vmc_no");
						vmc_auth_code=ev.getString("vmc_auth_code");
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.2=vmc_no="+vmc_no+"vmc_auth_code="+vmc_auth_code,"server.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//�õ�����˵�ַ��Ϣ
					Map<String, String> list=ToolClass.ReadConfigFile();
					if(list!=null)
					{				       
						httpStr= list.get("server");
					}
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ǩ��="+httpStr,"server.txt");
										
					//�豸ǩ��
					String target = httpStr+"/api/vmcCheckin";	//Ҫ�ύ��Ŀ���ַ
					HttpClient httpclient = new DefaultHttpClient();	//����HttpClient����
					HttpPost httppost = new HttpPost(target);	//����HttpPost����
					//1.��ӵ��༯�У�����key,value����ΪString
					Map<String,Object> parammap = new TreeMap<String,Object>() ;
					parammap.put("vmc_no",vmc_no);
					parammap.put("vmc_auth_code",vmc_auth_code);			
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap.toString(),"server.txt");
					//��2.map�༯תΪjson��ʽ
					Gson gson=new Gson();
					String param=gson.toJson(parammap);		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param.toString(),"server.txt");
					//3.���params
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("param", param));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params.toString(),"server.txt");
					try {
						httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient.execute(httppost);	//ִ��HttpClient����
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							
						}else{
							result = "����ʧ�ܣ�";
						}
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
						//�����̷߳���ǩ����Ϣ
						Message tomain=mainhand.obtainMessage();
						JSONObject object=new JSONObject(result);
						int errType =  object.getInt("Error");
						//�����й���
						if(errType>0)
						{
							tomain.what=SETERRFAILMAIN;
							tomain.obj=object.getString("Message");
						}
						else
						{
							tomain.what=SETMAIN;
							Tok=object.getString("Token");
						}			    	    
			    	    mainhand.sendMessage(tomain); // ������Ϣ
					} 
			       catch (Exception e) 
			       {  
			           //e.printStackTrace();  
			    	   //�����̷߳�������ʧ����Ϣ
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // ������Ϣ
			       }
					break;	
				case SETHEARTCHILD://���߳̽������߳�������Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ����","server.txt");
					//����
					String target2 = httpStr+"/api/vmcPoll";	//Ҫ�ύ��Ŀ���ַ
					
					HttpClient httpclient2 = new DefaultHttpClient();	//����HttpClient����
					HttpPost httppost2 = new HttpPost(target2);	//����HttpPost����
					//��ӵ��༯�У�����key,value����ΪString
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("Token",Tok);
//					parammap.put("LastPollTime",new Date());			
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",parammap.toString(),"server.txt");
//					//��map�༯תΪjson��ʽ
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",param.toString(),"server.txt");
					//���params
					List<NameValuePair> params2 = new ArrayList<NameValuePair>();
					params2.add(new BasicNameValuePair("Token", Tok));
					params2.add(new BasicNameValuePair("LastPollTime", getLasttime()));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params2.toString(),"server.txt");
					try {
						httppost2.setEntity(new UrlEncodedFormEntity(params2, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient2.execute(httppost2);	//ִ��HttpClient����
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							
						}else{
							result = "����ʧ�ܣ�";
						}
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
						//�����̷߳���ǩ����Ϣ
//						Message tomain=mainhand.obtainMessage();
//						JSONObject object=new JSONObject(result);
//						int errType =  object.getInt("Error");
//						//�����й���
//						if(errType>0)
//						{
//							tomain.what=SETERRFAILMAIN;
//							tomain.obj=object.getString("Message");
//						}
//						else
//						{
//							tomain.what=SETMAIN;
//							Tok=object.getString("Token");
//						}			    	    
//			    	    mainhand.sendMessage(tomain); // ������Ϣ
					}
					catch (Exception e) 
			        {  
			           //e.printStackTrace();  
			    	   //�����̷߳�������ʧ����Ϣ
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // ������Ϣ
			        }
					break;
				case SETCLASSCHILD://��ȡ��Ʒ����
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ��ȡ��Ʒ����","server.txt");
					String target3 = httpStr+"/api/productClass";	//Ҫ�ύ��Ŀ���ַ
					
					HttpClient httpclient3 = new DefaultHttpClient();	//����HttpClient����
					HttpPost httppost3 = new HttpPost(target3);	//����HttpPost����
					//��ӵ��༯�У�����key,value����ΪString
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("Token",Tok);
//					parammap.put("LastPollTime",new Date());			
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",parammap.toString(),"server.txt");
//					//��map�༯תΪjson��ʽ
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",param.toString(),"server.txt");
					//���params
					List<NameValuePair> params3 = new ArrayList<NameValuePair>();
					params3.add(new BasicNameValuePair("Token", Tok));
					params3.add(new BasicNameValuePair("LastPollTime", getLasttime()));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params3.toString(),"server.txt");
					try {
						httppost3.setEntity(new UrlEncodedFormEntity(params3, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient3.execute(httppost3);	//ִ��HttpClient����
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							
						}else{
							result = "����ʧ�ܣ�";
						}
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
						//�����̷߳���ǩ����Ϣ
//						Message tomain=mainhand.obtainMessage();
//						JSONObject object=new JSONObject(result);
//						int errType =  object.getInt("Error");
//						//�����й���
//						if(errType>0)
//						{
//							tomain.what=SETERRFAILMAIN;
//							tomain.obj=object.getString("Message");
//						}
//						else
//						{
//							tomain.what=SETMAIN;
//							Tok=object.getString("Token");
//						}			    	    
//			    	    mainhand.sendMessage(tomain); // ������Ϣ
					}
					catch (Exception e) 
			        {  
			           //e.printStackTrace();  
			    	   //�����̷߳�������ʧ����Ϣ
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // ������Ϣ
			        }
					break;
				default:
					break;
				}
			}
			
		};
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
	}
	
	private String getLasttime()
	{
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd"); //��ȷ������ 
		SimpleDateFormat tempTime = new SimpleDateFormat("hh:mm:ss"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString()+"T"
        		+tempTime.format(new java.util.Date()).toString(); 
		return datetime;
	}

}
