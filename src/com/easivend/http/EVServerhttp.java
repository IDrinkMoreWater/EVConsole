package com.easivend.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.easivend.app.maintain.GoodsManager;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_classDAO;
import com.easivend.model.Tb_vmc_class;
import com.easivend.view.EVServerService;
import com.google.gson.Gson;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVServerhttp implements Runnable {
	String vmc_no="";
	String vmc_auth_code="";
	public final static int SETCHILD=2;//what���,���͸����߳�ǩ��
	public final static int SETMAIN=1;//what���,���͸����߳�ǩ����ɷ���	
	public final static int SETERRFAILMAIN=4;//what���,���͸����߳�ǩ������ʧ�ܷ���	
	
	public final static int SETHEARTCHILD=5;//what���,���͸����߳�����
	
	public final static int SETCLASSCHILD=6;//what���,���͸����̻߳�ȡ��Ʒ����
	public final static int SETERRFAILCLASSMAIN=7;//what���,���͸����̻߳�ȡ��Ʒ�������
	public final static int SETCLASSMAIN=8;//what���,���͸����̻߳�ȡ��Ʒ���෵��
	
	public final static int SETPRODUCTCHILD=9;//what���,���͸����̻߳�ȡ��Ʒ��Ϣ
	public final static int SETERRFAILRODUCTMAIN=10;//what���,���͸����̻߳�ȡ��Ʒ����
	public final static int SETRODUCTMAIN=11;//what���,���͸����̻߳�ȡ��Ʒ����
	
	public final static int SETHUODAOCHILD=12;//what���,���͸����̻߳�ȡ������Ϣ
	public final static int SETERRFAILHUODAOMAIN=13;//what���,���͸����̻߳�ȡ��������
	public final static int SETHUODAOMAIN=14;//what���,���͸����̻߳�ȡ��������
	
	public final static int SETHUODAOSTATUONECHILD=15;//what���,���͸����߳��ϱ�һ��������Ϣ
	public final static int SETHUODAOSTATUALLCHILD=16;//what���,���͸����߳��ϱ�ȫ��������Ϣ
	
	public final static int SETDEVSTATUCHILD=17;//what���,���͸����߳��ϱ��豸״̬��Ϣ
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
						//�����̷߳���ǩ����Ϣ
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//�����й���
							if(errType>0)
							{
								tomain.what=SETERRFAILCLASSMAIN;
								tomain.obj=object.getString("Message");
							}
							else
							{
								tomain.what=SETCLASSMAIN;
								tomain.obj=result;
							}			    	    
				    	    mainhand.sendMessage(tomain); // ������Ϣ							
						}else{
							result = "����ʧ�ܣ�";
							tomain.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain); // ������Ϣ
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
						}

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
				case SETPRODUCTCHILD://��ȡ��Ʒ��Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ��ȡ��Ʒ��Ϣ","server.txt");
					String target4 = httpStr+"/api/productData";	//Ҫ�ύ��Ŀ���ַ
					
					HttpClient httpclient4 = new DefaultHttpClient();	//����HttpClient����
					HttpPost httppost4 = new HttpPost(target4);	//����HttpPost����
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
					List<NameValuePair> params4 = new ArrayList<NameValuePair>();
					params4.add(new BasicNameValuePair("Token", Tok));
					params4.add(new BasicNameValuePair("VMC_NO", vmc_no));
					params4.add(new BasicNameValuePair("PAGE_INDEX", ""));
					params4.add(new BasicNameValuePair("PAGE_SIZE", ""));
					params4.add(new BasicNameValuePair("LAST_EDIT_TIME", ""));
					params4.add(new BasicNameValuePair("PRODUCT_NO", ""));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params4.toString(),"server.txt");
					try {
						httppost4.setEntity(new UrlEncodedFormEntity(params4, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient4.execute(httppost4);	//ִ��HttpClient����
						//�����̷߳���ǩ����Ϣ
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//�����й���
							if(errType>0)
							{
								tomain.what=SETERRFAILRODUCTMAIN;
								tomain.obj=object.getString("Message");
							}
							else
							{
								tomain.what=SETRODUCTMAIN;
								tomain.obj=result;
							}			    	    
				    	    mainhand.sendMessage(tomain); // ������Ϣ							
						}else{
							result = "����ʧ�ܣ�";
							tomain.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain); // ������Ϣ
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
						}

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
				case SETHUODAOCHILD://��ȡ������Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ��ȡ������Ϣ","server.txt");
					String target5 = httpStr+"/api/vmcPathConfigDownload";	//Ҫ�ύ��Ŀ���ַ
					
					HttpClient httpclient5 = new DefaultHttpClient();	//����HttpClient����
					HttpPost httppost5 = new HttpPost(target5);	//����HttpPost����
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
					List<NameValuePair> params5 = new ArrayList<NameValuePair>();
					params5.add(new BasicNameValuePair("Token", Tok));
					params5.add(new BasicNameValuePair("VMC_NO", vmc_no));
					params5.add(new BasicNameValuePair("LAST_EDIT_TIME", ""));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params5.toString(),"server.txt");
					try {
						httppost5.setEntity(new UrlEncodedFormEntity(params5, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient5.execute(httppost5);	//ִ��HttpClient����
						//�����̷߳���ǩ����Ϣ
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//�����й���
							if(errType>0)
							{
								tomain.what=SETERRFAILHUODAOMAIN;
								tomain.obj=object.getString("Message");
							}
							else
							{
								tomain.what=SETHUODAOMAIN;
								tomain.obj=result;
							}			    	    
				    	    mainhand.sendMessage(tomain); // ������Ϣ							
						}else{
							result = "����ʧ�ܣ�";
							tomain.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain); // ������Ϣ
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
						}

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
				case SETHUODAOSTATUONECHILD://���߳̽������̵߳��������ϱ���Ϣ	
					String CABINET_NO=null;
	    			String PATH_NO=null;
	    			String PATH_STATUS=null;
	    			String PATH_COUNT=null;
	    			String PATH_REMAINING=null;
	    			String PRODUCT_NO=null;
					//1.�õ����������Ϣ
					JSONObject ev2=null;
					try {
						ev2 = new JSONObject(msg.obj.toString());
						CABINET_NO=ev2.getString("CABINET_NO");
		    			PATH_NO=ev2.getString("PATH_NO");
		    			PATH_STATUS=ev2.getString("PATH_STATUS");
		    			PATH_COUNT=ev2.getString("PATH_COUNT");
		    			PATH_REMAINING=ev2.getString("PATH_REMAINING");
		    			PRODUCT_NO=ev2.getString("PRODUCT_NO");
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.2=vmc_no="+vmc_no+"CABINET_NO="+CABINET_NO
						+" PATH_NO="+PATH_NO+" PATH_STATUS="+PATH_STATUS+" PATH_COUNT="+PATH_COUNT
						+" PATH_REMAINING="+PATH_REMAINING+" PRODUCT_NO="+PRODUCT_NO,"server.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String target6 = httpStr+"/api/vmcPathStatus";	//Ҫ�ύ��Ŀ���ַ
					HttpClient httpclient6 = new DefaultHttpClient();	//����HttpClient����
					HttpPost httppost6 = new HttpPost(target6);	//����HttpPost����
					JSONObject json=new JSONObject();
					try {
						json.put("VMC_NO", vmc_no);
						json.put("PATH_NO", PATH_NO);
						json.put("cabinetNumber", CABINET_NO);
						json.put("PATH_STATUS",Integer.parseInt(PATH_STATUS));
						json.put("PATH_REMAINING",Integer.parseInt(PATH_REMAINING));
						json.put("PATH_COUNT",Integer.parseInt(PATH_COUNT));
						json.put("PRODUCT_NO", PRODUCT_NO);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
//					//1.��ӵ��༯�У�����key,value����ΪString
//					Map<String,Object> parammap6 = new TreeMap<String,Object>() ;
////					parammap6.put("VMC_NO",vmc_no);
////					//parammap6.put("cabinetNumber",CABINET_NO);
////					parammap6.put("PATH_NO",PATH_NO);	
////					parammap6.put("PATH_STATUS",PATH_STATUS);	
////					parammap6.put("PATH_COUNT",PATH_COUNT);
////					parammap6.put("PATH_REMAINING",PATH_REMAINING);	
////					parammap6.put("PRODUCT_NO",PRODUCT_NO);	
//					
//					parammap6.put("VMC_NO",vmc_no);
//					parammap6.put("pathID","2596");
//					parammap6.put("cabinetNumber",CABINET_NO);
//					parammap6.put("pathName",PATH_NO);	
//					parammap6.put("pathStatus",PATH_STATUS);	
//					parammap6.put("pathCount",PATH_COUNT);
//					parammap6.put("pathRemaining",PATH_REMAINING);	
//					parammap6.put("productID",PRODUCT_NO);
//					parammap6.put("productNum","12");
//					parammap6.put("LAST_EDIT_TIME",getLasttime());
//					parammap6.put("IS_DISABLE","0");
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap6.toString(),"server.txt");
//					//��2.map�༯תΪjson��ʽ
//					Gson gson6=new Gson();
					String param6=json.toString();		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param6.toString(),"server.txt");
					//3.���params
					List<NameValuePair> params6 = new ArrayList<NameValuePair>();
					params6.add(new BasicNameValuePair("param", param6));
					params6.add(new BasicNameValuePair("Token", Tok));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params6.toString(),"server.txt");
					
					try {
						httppost6.setEntity(new UrlEncodedFormEntity(params6, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient6.execute(httppost6);	//ִ��HttpClient����
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							
						}else{
							result = "����ʧ�ܣ�";
						}
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");

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
				case EVServerhttp.SETDEVSTATUCHILD://�豸״̬�ϱ�
					int bill_err=0;
	    			int coin_err=0;
					//1.�õ����������Ϣ
					JSONObject ev7=null;
					try {
						ev7 = new JSONObject(msg.obj.toString());
						bill_err=ev7.getInt("bill_err");
						coin_err=ev7.getInt("coin_err");
		    			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.2=bill_err="+bill_err+"coin_err="+coin_err
						,"server.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//�豸״̬�ϱ�
					String target7 = httpStr+"/api/vmcStatus";	//Ҫ�ύ��Ŀ���ַ
					HttpClient httpclient7 = new DefaultHttpClient();	//����HttpClient����
					HttpPost httppost7 = new HttpPost(target7);	//����HttpPost����
					//1.��ӵ��༯�У�����key,value����ΪString
					Map<String,Object> parammap7 = new TreeMap<String,Object>() ;
					parammap7.put("VMC_NO",vmc_no);
					parammap7.put("CLIENT_STATUS","0");
					parammap7.put("COINS_STATUS",coin_err);
					parammap7.put("NOTE_STATUS",bill_err);	
					parammap7.put("DOOR_STATUS","0");	
					parammap7.put("WAREHOUSE_TEMPERATURE","0");					
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap7.toString(),"server.txt");
					//��2.map�༯תΪjson��ʽ
					Gson gson7=new Gson();
					String param7=gson7.toJson(parammap7);		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param7.toString(),"server.txt");
					//3.���params
					List<NameValuePair> params7 = new ArrayList<NameValuePair>();
					params7.add(new BasicNameValuePair("param", param7));
					params7.add(new BasicNameValuePair("Token", Tok));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params7.toString(),"server.txt");
					
					try {
						httppost7.setEntity(new UrlEncodedFormEntity(params7, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient7.execute(httppost7);	//ִ��HttpClient����
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							
						}else{
							result = "����ʧ�ܣ�";
						}
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");

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
