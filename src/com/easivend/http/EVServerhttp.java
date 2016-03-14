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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easivend.app.maintain.GoodsManager;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;
import com.easivend.view.EVServerService;
import com.google.gson.Gson;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVServerhttp implements Runnable {
	RequestQueue mQueue = null; 
	String vmc_no="";
	String vmc_auth_code="";
	public final static int SETCHILD=2;//what���,���͸����߳�ǩ��
	public final static int SETMAIN=1;//what���,���͸����߳�ǩ����ɷ���	
	public final static int SETERRFAILMAIN=4;//what���,���͸����߳�ǩ������ʧ�ܷ���	
	
	public final static int SETHEARTCHILD=5;//what���,���͸����߳�����
	public final static int SETERRFAILHEARTMAIN=6;//what���,���͸����߳���������
	public final static int SETHEARTMAIN=7;//what���,���͸����̻߳�ȡ��������
	
	public final static int SETCLASSCHILD=8;//what���,���͸����̻߳�ȡ��Ʒ����
	public final static int SETERRFAILCLASSMAIN=9;//what���,���͸����̻߳�ȡ��Ʒ�������
	public final static int SETCLASSMAIN=10;//what���,���͸����̻߳�ȡ��Ʒ���෵��
	
	public final static int SETPRODUCTCHILD=11;//what���,���͸����̻߳�ȡ��Ʒ��Ϣ
	public final static int SETERRFAILRODUCTMAIN=12;//what���,���͸����̻߳�ȡ��Ʒ����
	public final static int SETRODUCTMAIN=13;//what���,���͸����̻߳�ȡ��Ʒ����
	
	public final static int SETHUODAOCHILD=14;//what���,���͸����̻߳�ȡ������Ϣ
	public final static int SETERRFAILHUODAOMAIN=15;//what���,���͸����̻߳�ȡ��������
	public final static int SETHUODAOMAIN=16;//what���,���͸����̻߳�ȡ��������
	
	public final static int SETHUODAOSTATUCHILD=17;//what���,���͸����߳��ϱ�������Ϣ
	public final static int SETERRFAILHUODAOSTATUMAIN=18;//what���,���͸����߳��ϱ�������Ϣ����
	public final static int SETHUODAOSTATUMAIN=19;//what���,���͸����߳��ϱ�������Ϣ
	
	public final static int SETDEVSTATUCHILD=20;//what���,���͸����߳��ϱ��豸״̬��Ϣ
	public final static int SETERRFAILDEVSTATUMAIN=21;//what���,���͸����̻߳�ȡ�豸����
	public final static int SETDEVSTATUMAIN=22;//what���,���͸����̻߳�ȡ�豸����	
	
	public final static int SETRECORDCHILD=23;//what���,���͸����߳��ϱ����׼�¼
	public final static int SETERRFAILRECORDMAIN=24;//what���,���͸����߳��ϱ����׹���
	public final static int SETRECORDMAIN=25;//what���,���͸����̻߳�ȡ�ϱ����׷���
	
	public final static int SETCHECKCHILD=26;//what���,���͸����̸߳���ǩ����Ϣ��
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
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread start["+Thread.currentThread().getId()+"]","server.txt");
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
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ǩ��["+Thread.currentThread().getId()+"]="+httpStr,"server.txt");
					//�½�Volley
					mQueue = Volley.newRequestQueue(ToolClass.getContext()); 
					
					//2.���ò���,���÷�������ַ
					String target = httpStr+"/api/vmcCheckin";	//Ҫ�ύ��Ŀ���ַ
					//1.��ӵ��༯�У�����key,value����ΪString
					Map<String,Object> parammap = new TreeMap<String,Object>() ;
					parammap.put("vmc_no",vmc_no);
					parammap.put("vmc_auth_code",vmc_auth_code);			
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap.toString(),"server.txt");
					//��2.map�༯תΪjson��ʽ
					Gson gson=new Gson();
					final String param=gson.toJson(parammap);		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param.toString(),"server.txt");
					
					//�����̷߳�����Ϣ
					final Message tomain1=mainhand.obtainMessage();
					//4.�豸ǩ��
					StringRequest stringRequest = new StringRequest(Method.POST, target,  new Response.Listener<String>() {  
                        @Override  
                        public void onResponse(String response) {  
                           
                          //�������ɹ�
							result = response;	//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object;
							try {
								object = new JSONObject(result);
								int errType =  object.getInt("Error");
								//�����й���
								if(errType>0)
								{
									tomain1.what=SETERRFAILMAIN;
									tomain1.obj=object.getString("Message");
									ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail1]SETERRFAILMAIN","server.txt");
								}
								else
								{
									tomain1.what=SETMAIN;
									Tok=object.getString("Token");
									ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok1]","server.txt");
								}	
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}										    	    
				    	    mainhand.sendMessage(tomain1); // ������Ϣ
                        }  
                    }, new Response.ErrorListener() {  
                        @Override  
                        public void onErrorResponse(VolleyError error) {  
                        	result = "����ʧ�ܣ�";
							tomain1.what=SETFAILMAIN;
							mainhand.sendMessage(tomain1); // ������Ϣ
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail1]SETFAILMAIN"+result,"server.txt");  
                        }  
                    }) 
					{  
					    @Override  
					    protected Map<String, String> getParams() throws AuthFailureError {  
					    	//3.���params
					    	Map<String, String> map = new HashMap<String, String>();  
					        map.put("param", param);  
					        //params.add(new BasicNameValuePair("param", param));
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+param.toString(),"server.txt");
					        return map;  
					    }  
					}; 	
					//5.������Ϣ�����͵�������
					mQueue.add(stringRequest);
					break;
				case SETHEARTCHILD://���߳̽������߳�������Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ����["+Thread.currentThread().getId()+"]","server.txt");
					//����
					String target2 = httpStr+"/api/vmcPoll";	//Ҫ�ύ��Ŀ���ַ
					
					HttpClient httpclient2 = new DefaultHttpClient();	//����HttpClient����
					httpclient2.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//����ʱ
					httpclient2.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//��ȡ��ʱ
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
					params2.add(new BasicNameValuePair("LastPollTime", ToolClass.getLasttime()));
					//params2.add(new BasicNameValuePair("LastPollTime", "2015-08-31T10:45:19"));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params2.toString(),"server.txt");
					try {
						httppost2.setEntity(new UrlEncodedFormEntity(params2, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient2.execute(httppost2);	//ִ��HttpClient����
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
								tomain.what=SETERRFAILHEARTMAIN;
								tomain.obj=object.getString("Message");
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail2]SETERRFAILHEARTMAIN","server.txt");
							}
							else
							{
								tomain.what=SETHEARTMAIN;
								tomain.obj=result;
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok2]","server.txt");
							}			    	    
							mainhand.sendMessage(tomain); // ������Ϣ
						}else{
							result = "����ʧ�ܣ�";
							tomain.what=SETFAILMAIN;
							mainhand.sendMessage(tomain); // ������Ϣ
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail2]SETFAILMAIN"+result,"server.txt");
						}
						
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
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail2]SETFAILMAIN","server.txt");
			        }
					break;
				case SETCLASSCHILD://��ȡ��Ʒ����
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ��ȡ��Ʒ����["+Thread.currentThread().getId()+"]","server.txt");
					String target3 = httpStr+"/api/productClass";	//Ҫ�ύ��Ŀ���ַ
					final String LAST_EDIT_TIME3=msg.obj.toString();
					
					//�����̷߳�����Ϣ
					final Message tomain3=mainhand.obtainMessage();
					//4.׼��������Ϣ����
					StringRequest stringRequest3 = new StringRequest(Method.POST, target3,  new Response.Listener<String>() {  
                        @Override  
                        public void onResponse(String response) {  
                           
                          //�������ɹ�
                        	result = response;	//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object;
							try {
								object = new JSONObject(result);
								int errType =  object.getInt("Error");
								//�����й���
								if(errType>0)
								{
									tomain3.what=SETERRFAILCLASSMAIN;
									tomain3.obj=object.getString("Message");
									ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail3]SETERRFAILCLASSMAIN","server.txt");
								}
								else
								{
									tomain3.what=SETCLASSMAIN;
									tomain3.obj=result;
									ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok3]","server.txt");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}										    	    
				    	    mainhand.sendMessage(tomain3); // ������Ϣ
                        }  
                    }, new Response.ErrorListener() {  
                        @Override  
                        public void onErrorResponse(VolleyError error) {  
                        	result = "����ʧ�ܣ�";
							tomain3.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain3); // ������Ϣ
				    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail3]SETFAILMAIN"+result,"server.txt");
                        }  
                    }) 
					{  
					    @Override  
					    protected Map<String, String> getParams() throws AuthFailureError {  
					    	//3.���params
					    	Map<String, String> map = new HashMap<String, String>();  
					        map.put("Token", Tok);  
					        map.put("LAST_EDIT_TIME", LAST_EDIT_TIME3);
					        ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+map.toString(),"server.txt");
					        return map;  
					   }  
					}; 	
					//5.������Ϣ�����͵�������
					mQueue.add(stringRequest3);									
					break;
				case SETPRODUCTCHILD://��ȡ��Ʒ��Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ��ȡ��Ʒ��Ϣ["+Thread.currentThread().getId()+"]","server.txt");
					String target4 = httpStr+"/api/productData";	//Ҫ�ύ��Ŀ���ַ
					final String LAST_EDIT_TIME4=msg.obj.toString();
					
					//�����̷߳�����Ϣ
					final Message tomain4=mainhand.obtainMessage();
					//4.׼��������Ϣ����
					StringRequest stringRequest4 = new StringRequest(Method.POST, target4,  new Response.Listener<String>() {  
						@Override  
						public void onResponse(String response) {  
						   
						  //�������ɹ�
							result = response;	//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object;
							try {
								object = new JSONObject(result);
								int errType =  object.getInt("Error");
								//�����й���
								if(errType>0)
								{
									tomain4.what=SETERRFAILRODUCTMAIN;
									tomain4.obj=object.getString("Message");
									mainhand.sendMessage(tomain4); // ������Ϣ
									ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail4]SETERRFAILRODUCTMAIN","server.txt");
								}
								else
								{
									ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok4]׼��������Ʒ��Ϣ...","server.txt");
									//�����̷߳���ǩ����Ϣ
									//tomain4.what=SETRODUCTMAIN;
									//tomain4.obj=updateproductImg(result);
									//mainhand.sendMessage(tomain4); // ������Ϣ
									productArray(result);
									updateproduct(0);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}										  
						}  
					}, new Response.ErrorListener() {  
						@Override  
						public void onErrorResponse(VolleyError error) {  
							result = "����ʧ�ܣ�";
							tomain4.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain4); // ������Ϣ
				    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail4]SETFAILMAIN"+result,"server.txt");
						}  
					}) 
					{  
						@Override  
						protected Map<String, String> getParams() throws AuthFailureError {  
							//3.���params
							Map<String, String> map = new HashMap<String, String>();  
							map.put("Token", Tok);
							map.put("VMC_NO", vmc_no);
							map.put("PAGE_INDEX", "");
							map.put("PAGE_SIZE", "");
							map.put("LAST_EDIT_TIME", LAST_EDIT_TIME4);
							map.put("PRODUCT_NO", "");
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+map.toString(),"server.txt");
							return map;  
					   }  
					}; 	
					//5.������Ϣ�����͵�������
					mQueue.add(stringRequest4);	
					break;
				case SETHUODAOCHILD://��ȡ������Ϣ
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ��ȡ������Ϣ["+Thread.currentThread().getId()+"]","server.txt");
					String target5 = httpStr+"/api/vmcPathConfigDownload";	//Ҫ�ύ��Ŀ���ַ
					String LAST_EDIT_TIME5=msg.obj.toString();
					
					HttpClient httpclient5 = new DefaultHttpClient();	//����HttpClient����
					httpclient5.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//����ʱ
					httpclient5.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//��ȡ��ʱ
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
					params5.add(new BasicNameValuePair("LAST_EDIT_TIME", LAST_EDIT_TIME5));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params5.toString(),"server.txt");
					try {
						httppost5.setEntity(new UrlEncodedFormEntity(params5, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient5.execute(httppost5);	//ִ��HttpClient����
						//�����̷߳�����Ϣ
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
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail5]SETERRFAILHUODAOMAIN","server.txt");
							}
							else
							{
								tomain.what=SETHUODAOMAIN;
								tomain.obj=result;
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok5]","server.txt");
							}			    	    
				    	    mainhand.sendMessage(tomain); // ������Ϣ							
						}else{
							result = "����ʧ�ܣ�";
							tomain.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain); // ������Ϣ
				    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail5]SETFAILMAIN"+result,"server.txt");
						}

					}
					catch (Exception e) 
			        {  
			           //e.printStackTrace();  
			    	   //�����̷߳�������ʧ����Ϣ
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // ������Ϣ
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail5]SETFAILMAIN","server.txt");
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
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread �豸״̬�ϱ�["+Thread.currentThread().getId()+"]","server.txt");
					//�豸״̬�ϱ�
					String target7 = httpStr+"/api/vmcStatus";	//Ҫ�ύ��Ŀ���ַ
					HttpClient httpclient7 = new DefaultHttpClient();	//����HttpClient����
					httpclient7.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//����ʱ
					httpclient7.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//��ȡ��ʱ
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
						//�����̷߳�����Ϣ
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//�����й���
							if(errType>0)
							{
								tomain.what=SETERRFAILDEVSTATUMAIN;
								tomain.obj=object.getString("Message");
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail6]SETERRFAILDEVSTATUMAIN","server.txt");
							}
							else
							{
								tomain.what=SETDEVSTATUMAIN;
								tomain.obj=result;
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok6]","server.txt");
							}			    	    
							mainhand.sendMessage(tomain); // ������Ϣ		
						}else{
							result = "����ʧ�ܣ�";
							tomain.what=SETFAILMAIN;
							mainhand.sendMessage(tomain); // ������Ϣ
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail6]SETFAILMAIN"+result,"server.txt");
						}
						
					} 
			       catch (Exception e) 
			       {  
			           //e.printStackTrace();  
			    	   //�����̷߳�������ʧ����Ϣ
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // ������Ϣ
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail6]SETFAILMAIN","server.txt");
			       }
					break;
				case EVServerhttp.SETRECORDCHILD://���׼�¼�ϱ�	
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ���׼�¼�ϱ�["+Thread.currentThread().getId()+"]","server.txt");
					//1.�õ����׼�¼�����Ϣ
					JSONArray ev8=null;
					try {
						ev8 = new JSONArray(msg.obj.toString());
						JSONArray retjson=new JSONArray();
						//�����̷߳�����Ϣ
						Message tomain=mainhand.obtainMessage();
						
						for(int i=0;i<ev8.length();i++)
						{
							JSONObject object2=ev8.getJSONObject(i);
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.1="+object2.toString()
									,"server.txt");
							String orderno=updaterecords(object2.toString());
							if(orderno!=null)
							{
								JSONObject ret=new JSONObject();
								ret.put("orderno", orderno);
								retjson.put(ret);
							}
						}	
						tomain.what=SETRECORDMAIN;
						tomain.obj=retjson;
						mainhand.sendMessage(tomain); // ������Ϣ		
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						//�����̷߳�����Ϣ
						Message tomain=mainhand.obtainMessage();
						tomain.what=SETFAILMAIN;
						mainhand.sendMessage(tomain); // ������Ϣ	
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail7]SETFAILMAIN","server.txt");
					}
					break;
				case SETHUODAOSTATUCHILD://����״̬�ϱ���Ϣ	
//					String CABINET_NO=null;
//	    			String PATH_NO=null;
//	    			String PATH_STATUS=null;
//	    			String PATH_COUNT=null;
//	    			String PATH_REMAINING=null;
//	    			String PRODUCT_NO=null;
//	    			String PATH_ID=null;
//					//1.�õ����������Ϣ
//					JSONObject ev2=null;
//					try {
//						ev2 = new JSONObject(msg.obj.toString());
//						CABINET_NO=ev2.getString("CABINET_NO");
//		    			PATH_NO=ev2.getString("PATH_NO");
//		    			PATH_STATUS=ev2.getString("PATH_STATUS");
//		    			PATH_COUNT=ev2.getString("PATH_COUNT");
//		    			PATH_REMAINING=ev2.getString("PATH_REMAINING");
//		    			PRODUCT_NO=ev2.getString("PRODUCT_NO");
//		    			PATH_ID=ev2.getString("PATH_ID");
//						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.2=vmc_no="+vmc_no+"PATH_ID="+PATH_ID+"CABINET_NO="+CABINET_NO
//						+" PATH_NO="+PATH_NO+" PATH_STATUS="+PATH_STATUS+" PATH_COUNT="+PATH_COUNT
//						+" PATH_REMAINING="+PATH_REMAINING+" PRODUCT_NO="+PRODUCT_NO,"server.txt");
//					} catch (JSONException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread ����״̬�ϱ�["+Thread.currentThread().getId()+"]","server.txt");
					//1.�õ����׼�¼�����Ϣ
					JSONArray ev9=null;
					try {
						ev9 = new JSONArray(msg.obj.toString());
						JSONArray retjson=new JSONArray();
						//�����̷߳�����Ϣ
						Message tomain=mainhand.obtainMessage();
						
						for(int i=0;i<ev9.length();i++)
						{
							JSONObject object2=ev9.getJSONObject(i);
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.1="+object2.toString()
									,"server.txt");
							JSONObject ret=updatecolumns(object2.toString());
							if(ret!=null)
							{
								retjson.put(ret);
							}
						}	
						tomain.what=SETHUODAOSTATUMAIN;
						tomain.obj=retjson;
						mainhand.sendMessage(tomain); // ������Ϣ		
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						//�����̷߳�����Ϣ
						Message tomain=mainhand.obtainMessage();
						tomain.what=SETFAILMAIN;
						mainhand.sendMessage(tomain); // ������Ϣ	
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail8]SETFAILMAIN","server.txt");
					}
					
					
					
//					String target6 = httpStr+"/api/vmcPathStatus";	//Ҫ�ύ��Ŀ���ַ
//					HttpClient httpclient6 = new DefaultHttpClient();	//����HttpClient����
//					HttpPost httppost6 = new HttpPost(target6);	//����HttpPost����
//					JSONObject json=new JSONObject();
//					try {
//						json.put("pathID", 1223);
//						json.put("VMC_NO", vmc_no);
//						json.put("pathName", PATH_NO);
//						json.put("cabinetNumber", CABINET_NO);
//						json.put("pathStatus",Integer.parseInt(PATH_STATUS));
//						json.put("pathRemaining",Integer.parseInt(PATH_REMAINING));
//						json.put("pathCount",Integer.parseInt(PATH_COUNT));
//						json.put("productID", 11);
//						json.put("productNum", PRODUCT_NO);
//						json.put("lastedittime",getLasttime());
//						json.put("isdisable","0");
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					
////					//1.��ӵ��༯�У�����key,value����ΪString
////					Map<String,Object> parammap6 = new TreeMap<String,Object>() ;
//////					parammap6.put("VMC_NO",vmc_no);
//////					//parammap6.put("cabinetNumber",CABINET_NO);
//////					parammap6.put("PATH_NO",PATH_NO);	
//////					parammap6.put("PATH_STATUS",PATH_STATUS);	
//////					parammap6.put("PATH_COUNT",PATH_COUNT);
//////					parammap6.put("PATH_REMAINING",PATH_REMAINING);	
//////					parammap6.put("PRODUCT_NO",PRODUCT_NO);	
////					
////					parammap6.put("VMC_NO",vmc_no);
////					parammap6.put("pathID","2596");
////					parammap6.put("cabinetNumber",CABINET_NO);
////					parammap6.put("pathName",PATH_NO);	
////					parammap6.put("pathStatus",PATH_STATUS);	
////					parammap6.put("pathCount",PATH_COUNT);
////					parammap6.put("pathRemaining",PATH_REMAINING);	
////					parammap6.put("productID",PRODUCT_NO);
////					parammap6.put("productNum","12");
////					parammap6.put("LAST_EDIT_TIME",getLasttime());
////					parammap6.put("IS_DISABLE","0");
////					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap6.toString(),"server.txt");
////					//��2.map�༯תΪjson��ʽ
////					Gson gson6=new Gson();
//					String param6=json.toString();		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param6.toString(),"server.txt");
//					//3.���params
//					List<NameValuePair> params6 = new ArrayList<NameValuePair>();
//					params6.add(new BasicNameValuePair("param", param6));
//					params6.add(new BasicNameValuePair("Token", Tok));
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params6.toString(),"server.txt");
//					
//					try {
//						httppost6.setEntity(new UrlEncodedFormEntity(params6, "utf-8")); //���ñ��뷽ʽ
//						HttpResponse httpResponse = httpclient6.execute(httppost6);	//ִ��HttpClient����
//						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
//							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
//							
//						}else{
//							result = "����ʧ�ܣ�";
//						}
//						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
//
//					} 
//			       catch (Exception e) 
//			       {  
//			           //e.printStackTrace();  
//			    	   //�����̷߳�������ʧ����Ϣ
//						Message tomain=mainhand.obtainMessage();
//			    	    tomain.what=SETFAILMAIN;
//			    	    mainhand.sendMessage(tomain); // ������Ϣ
//			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=fail8","server.txt");
//			       }
					break;	
				case SETCHECKCHILD://���߳̽������߳�ǩ����Ϣ					
					//1.�õ����������Ϣ
					JSONObject ev10=null;
					try {
						ev10 = new JSONObject(msg.obj.toString());
						vmc_no=ev10.getString("vmc_no");
						vmc_auth_code=ev10.getString("vmc_auth_code");
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.2=vmc_no="+vmc_no+"vmc_auth_code="+vmc_auth_code,"server.txt");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread �豸ǩ��["+Thread.currentThread().getId()+"]","server.txt");
					//�豸ǩ��
					String target11 = httpStr+"/api/vmcCheckin";	//Ҫ�ύ��Ŀ���ַ
					HttpClient httpclient11 = new DefaultHttpClient();	//����HttpClient����
					httpclient11.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//����ʱ
					httpclient11.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//��ȡ��ʱ
					HttpPost httppost11 = new HttpPost(target11);	//����HttpPost����
					//1.��ӵ��༯�У�����key,value����ΪString
					Map<String,Object> parammap11 = new TreeMap<String,Object>() ;
					parammap11.put("vmc_no",vmc_no);
					parammap11.put("vmc_auth_code",vmc_auth_code);			
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap11.toString(),"server.txt");
					//��2.map�༯תΪjson��ʽ
					Gson gson11=new Gson();
					String param11=gson11.toJson(parammap11);		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param11.toString(),"server.txt");
					//3.���params
					List<NameValuePair> params11 = new ArrayList<NameValuePair>();
					params11.add(new BasicNameValuePair("param", param11));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params11.toString(),"server.txt");
					try {
						httppost11.setEntity(new UrlEncodedFormEntity(params11, "utf-8")); //���ñ��뷽ʽ
						HttpResponse httpResponse = httpclient11.execute(httppost11);	//ִ��HttpClient����
						//�����̷߳�����Ϣ
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
							//�������ɹ�
							result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
							//��ȡ���ص��ַ���
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//�����й���
							if(errType>0)
							{	
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail9]SETERRFAILMAIN","server.txt");	
							}
							else
							{
								Tok=object.getString("Token");	
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok9]","server.txt");
							}
						}else{
							result = "����ʧ�ܣ�";
							tomain.what=SETFAILMAIN;
							mainhand.sendMessage(tomain); // ������Ϣ
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail9]SETFAILMAIN"+result,"server.txt");
						}							
					} 
			       catch (Exception e) 
			       {  
			           //e.printStackTrace(); 
			    	   Message tomain=mainhand.obtainMessage();
						tomain.what=SETFAILMAIN;
						mainhand.sendMessage(tomain); // ������Ϣ	
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail9]SETFAILMAIN","server.txt");
			       }
					break;		
				default:
					break;
				}
			}
			
		};
		Looper.loop();//�û��Լ�������࣬�����߳���Ҫ�Լ�׼��loop
	}
	
	//==========
	//==��Ʒ����ģ��
	//==========
	JSONArray productarr=null;
	JSONArray zhuheproductArray=null;
	JSONObject zhuheproductjson = null; 
	int productint=0;
	//�ֽ���Ʒ��Ϣ
	private void productArray(String classrst) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(classrst); 
		productarr=jsonObject.getJSONArray("ProductList");
		productint=0;
		zhuheproductArray=new JSONArray();
		zhuheproductjson = new JSONObject(); 
	}
	//������ƷͼƬ��Ϣ
	private String updateproduct(int i) throws JSONException
	{
		final JSONObject object2=productarr.getJSONObject(i);
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","������ƷͼƬ="+object2.toString(),"server.txt");										
		final JSONObject zhuheobj=object2;
		//��һ��.��ȡ��ƷͼƬ����
		String target6 = httpStr+"/api/productImage";	//Ҫ�ύ��Ŀ���ַ
		JSONObject json=new JSONObject();
		try {
			json.put("VmcNo", vmc_no);
			json.put("attId", object2.getString("att_batch_id"));				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
		final String param6=json.toString();		
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param6.toString(),"server.txt");
		//4.׼��������Ϣ����
		StringRequest stringRequest6 = new StringRequest(Method.POST, target6,  new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  				   
				//�������ɹ�
				result = response;	//��ȡ���ص��ַ���
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1[ok10]="+result,"server.txt");
				try
				{
					//�ڶ�������ȡͼƬ����ATTID
					JSONObject jsonObject3 = new JSONObject(result); 
					JSONArray arr3=jsonObject3.getJSONArray("ProductImageList");
					JSONObject object3=arr3.getJSONObject(0);
					final String ATT_ID=object3.getString("ATT_ID");
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec2[ok10]ATT_ID="+ATT_ID,"server.txt");
					//ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec2[ok10]zhuheobj="+zhuheobj+"zhuheproductArray="+zhuheproductArray,"server.txt");
					//����������ͼƬ���ֱ��浽json��
					zhuheobj.put("AttImg", ToolClass.getImgFile(ATT_ID));
					zhuheproductArray.put(zhuheobj);
					if(ATT_ID.isEmpty())
					{
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","��Ʒ["+object2.getString("product_Name")+"]��ͼƬ","server.txt");
					}
					else
					{
						if(ToolClass.isImgFile(ATT_ID))
						{
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","��Ʒ["+object2.getString("product_Name")+"]ͼƬ�Ѵ���","server.txt");
						}
						else 
						{
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","��Ʒ["+object2.getString("product_Name")+"]ͼƬ,����ͼƬ...","server.txt");
							//���Ĳ�.׼������	
							String url= httpStr+"/topic/getFile/"+ATT_ID + ".jpg";	//Ҫ�ύ��Ŀ���ַ
							ImageRequest imageRequest = new ImageRequest(  
									url,  
							        new Response.Listener<Bitmap>() {  
							            @Override  
							            public void onResponse(Bitmap response) {  
							            	ToolClass.saveBitmaptofile(response,ATT_ID);
							            	try {
												ToolClass.Log(ToolClass.INFO,"EV_SERVER","��Ʒ["+object2.getString("product_Name")+"]ͼƬ,����ͼƬ���","server.txt");
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
							            }  
							        }, 0, 0, Config.RGB_565, new Response.ErrorListener() {  
							            @Override  
							            public void onErrorResponse(VolleyError error) {  
											result = "����ʧ�ܣ�";
											try {
												ToolClass.Log(ToolClass.INFO,"EV_SERVER","��Ʒ["+object2.getString("product_Name")+"]ͼƬ,����ͼƬʧ��","server.txt");
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
							            }  
							        });
							mQueue.add(imageRequest); 
						}
						
					}
				}
				catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				//���岽��������һ����Ʒ��Ϣ
				productint++;
				if(productint<productarr.length())
				{
					try {
						updateproduct(productint);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						zhuheproductjson.put("ProductList", zhuheproductArray);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","reczhuhe="+zhuheproductjson.toString(),"server.txt");

					//�ϴ���server
					//�����̷߳�����Ϣ
					Message tomain4=mainhand.obtainMessage();
					tomain4.what=SETRODUCTMAIN;
					tomain4.obj=zhuheproductjson.toString();
					mainhand.sendMessage(tomain4); // ������Ϣ
				}
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				result = "����ʧ�ܣ�";
				//����������ͼƬ���ֱ��浽json��
				try {
					zhuheobj.put("AttImg", "");
					zhuheproductArray.put(zhuheobj);
					zhuheproductjson.put("ProductList", zhuheproductArray);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec2=[fail10]"+result,"server.txt");
				
				//�ϴ���server
				//�����̷߳�����Ϣ
				Message tomain4=mainhand.obtainMessage();
				tomain4.what=SETRODUCTMAIN;
				tomain4.obj=zhuheproductjson.toString();
				mainhand.sendMessage(tomain4); // ������Ϣ
			}  
		}) 
		{  
			@Override  
			protected Map<String, String> getParams() throws AuthFailureError {  
				//3.���params
				Map<String, String> map = new HashMap<String, String>();  
				map.put("Token", Tok);  
				map.put("param", param6);
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+map.toString(),"server.txt");
				return map;  
		   }  
		}; 	
		//5.������Ϣ�����͵�������
		mQueue.add(stringRequest6);	
		
		//zhuheproductjson.put("ProductList", zhuheproductArray);
		//ToolClass.Log(ToolClass.INFO,"EV_SERVER","reczhuhe="+zhuheproductjson.toString(),"server.txt");
		//return zhuheproductjson.toString();
		return "";
	}
	
	
	//���½��׼�¼��Ϣ
	private String updaterecords(String classrst) 
	{
		String ret=null;
		JSONObject jsonObject = null; 
		String productNo="";
		String shouldPay="";
		String orderNo="";
		int payStatus=0;
		String customerPrice="";
		int payType=0;
		int actualQuantity=0;
		int quantity=0;
		String orderTime="";
		int orderStatus=0;
		String productName="";
		String RefundAmount="";
		int Status=0;
		try {
			jsonObject = new JSONObject(classrst); 
			productNo=jsonObject.getString("productNo");
			shouldPay=jsonObject.getString("shouldPay");
			orderNo=jsonObject.getString("orderNo");
			payStatus=jsonObject.getInt("payStatus");
			customerPrice=jsonObject.getString("customerPrice");
			payType=jsonObject.getInt("payType");
			actualQuantity=jsonObject.getInt("actualQuantity");
			quantity=jsonObject.getInt("quantity");
			orderTime=jsonObject.getString("orderTime");
			orderStatus=jsonObject.getInt("orderStatus");
			productName=jsonObject.getString("productName");
			RefundAmount=jsonObject.getString("RefundAmount");
			Status=jsonObject.getInt("Status");
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.2=orderNo="+orderNo+"orderTime="+orderTime+"orderStatus="+orderStatus+"payStatus="
				+payStatus+"payType="+payType+"shouldPay="+shouldPay+"RefundAmount="+RefundAmount+"Status="+Status+"productNo="+productNo+"quantity="+quantity+
				"actualQuantity="+actualQuantity+"customerPrice="+customerPrice+"productName="+productName,"server.txt");			
			    	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//��װjson��
		//���׼�¼��Ϣ
		String target = httpStr+"/api/vmcTransactionRecords";	//Ҫ�ύ��Ŀ���ַ
				
		JSONObject param=null;
		try {
			JSONObject order=new JSONObject();
			order.put("orderID", 121);
			order.put("orderNo", orderNo);//����id
			order.put("orderType", 1);
			order.put("orderTime", orderTime);//֧��ʱ��
			order.put("productCount", quantity);//��������
			order.put("customerName", "test");
			order.put("proCode", "test");
			order.put("cityCode", "test");
			order.put("areaCode", "test");
			order.put("address", "test");
			order.put("addressDetail", "test");
			order.put("consTel", "test");
			order.put("consName", "test");
			order.put("freight", 3);
			order.put("insurance", 2);
			order.put("consPost", "test");
			order.put("shipType", 3);
			order.put("orderStatus", orderStatus);//1δ֧��,2�����ɹ�,3����δ���
			order.put("shipTime", orderTime);//֧��ʱ��
			order.put("isNoFreight", 3);
			order.put("lastUpdateTime", orderTime);//֧��ʱ��
			order.put("orderDesc", "test");
			order.put("shouldPay", 2);
			order.put("integre", 3);
			order.put("sendStatus", 2);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","order="+order.toString(),"server.txt");
			
			JSONObject orderpay=new JSONObject();
			orderpay.put("payID", 120);
			orderpay.put("orderID", 121);
			orderpay.put("payStatus", payStatus);//0δ����,1���ڸ���,2�������,3����ʧ�ܣ�4����ȡ����5֧�������¼�--������֧��)
			orderpay.put("payType", payType);//0�ֽ�,1֧��������,2����,3֧������ά��,4΢��
			orderpay.put("shouldPay", shouldPay);//���׽��,��2.5Ԫ
			orderpay.put("realPay", 2);
			orderpay.put("RefundAmount", RefundAmount);//�˿���,��1.5Ԫ
			orderpay.put("smallChange", 0);
			orderpay.put("realNote", 1);
			orderpay.put("realCoins", 0);
			orderpay.put("smallNote", 1);
			orderpay.put("smallConis", 0);
			orderpay.put("integre", 0);
			orderpay.put("payDesc", "test");
			orderpay.put("payTime", orderTime);	//֧��ʱ��
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","orderpay="+orderpay.toString(),"server.txt");
			
			JSONObject orderrefund=new JSONObject();
			orderrefund.put("RefundId", 122);
			orderrefund.put("orderNo", orderNo);//��Ʒid
			orderrefund.put("Reason", "test");
			orderrefund.put("Amount", 1);
			orderrefund.put("Refund", 0);
			orderrefund.put("Debt", 0);
			orderrefund.put("ResultCode", "test");
			orderrefund.put("TradeNo", "test");
			orderrefund.put("Description", "test");
			orderrefund.put("Status", Status);//0��δ�˿1�������˿2���˿�ɹ���3���˿�ʧ��'
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","orderrefund="+orderrefund.toString(),"server.txt");
			
			JSONObject orderproduct=new JSONObject();
			orderproduct.put("opID", 123);
			orderproduct.put("orderID", 121);
			orderproduct.put("productID", 120);
			orderproduct.put("productNo", productNo);//��Ʒ���
			orderproduct.put("productType", "0001");
			orderproduct.put("quantity", quantity);//Ԥ�Ƴ���
			orderproduct.put("actualQuantity", actualQuantity);//ʵ�ʳ���
			orderproduct.put("productPrice", 1);			
			orderproduct.put("customerPrice", customerPrice);//��Ʒ����
			orderproduct.put("productName", productName);//��Ʒ����
			orderproduct.put("moneyAmount", 1);
			orderproduct.put("productIntegre", 1);
			orderproduct.put("IntegreAmount", 1);
			orderproduct.put("firstpurchaseprice", 1);
			JSONArray orderpro=new JSONArray();
			orderpro.put(orderproduct);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","orderproduct="+orderpro.toString(),"server.txt");
			
			
			
			JSONObject trans=new JSONObject();
			trans.put("Order", order);
			trans.put("OrderPay", orderpay);
			trans.put("OrderProducts", orderpro);
			trans.put("OrderRefund", orderrefund);			
			JSONArray TRANSACTION=new JSONArray();
			TRANSACTION.put(trans);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","TRANSACTION="+TRANSACTION.toString(),"server.txt");
			param=new JSONObject();
			param.put("VMC_NO", vmc_no);
			param.put("TOTAL",1);
			param.put("ACTUAL_TOTAL",1);
			param.put("TRANSACTION", TRANSACTION);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","param="+param.toString(),"server.txt");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpClient httpclient = new DefaultHttpClient();	//����HttpClient����
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//����ʱ
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//��ȡ��ʱ
		HttpPost httppost = new HttpPost(target);	//����HttpPost����
//				//��ӵ��༯�У�����key,value����ΪString
//				Map<String,Object> parammap = new TreeMap<String,Object>() ;
//				parammap.put("VMC_NO", "junpeng0004");
//				parammap.put("TOTAL",1);	
//				parammap.put("ACTUAL_TOTAL",1);
//				Oreder_Product_Pay orederProductPayList=new Oreder_Product_Pay();
//				parammap.put("TRANSACTION",orederProductPayList);
//				ToolClass.Log(ToolClass.INFO,"EV_SERVER",,parammap.toString());
//				//��map�༯תΪjson��ʽ
//				Gson gson=new Gson();
//				String param=gson.toJson(parammap);		
//				ToolClass.Log(ToolClass.INFO,"EV_SERVER",,param.toString());
		//���params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Token", Tok));
		params.add(new BasicNameValuePair("param", param.toString()));
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Records="+params.toString(),"server.txt");
		try{
			httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //���ñ��뷽ʽ
			HttpResponse httpResponse = httpclient.execute(httppost);	//ִ��HttpClient����
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
				//�������ɹ�
				result = EntityUtils.toString(httpResponse.getEntity());	
				//��ȡ���ص��ַ���
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
				JSONObject object=new JSONObject(result);
				int errType =  object.getInt("Error");
				//�����й���
				if(errType>0)
				{
					ret=null;
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail7]Records","server.txt");
				}
				else
				{
					ret=orderNo;
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok7]","server.txt");
				}	
			}else{
				result = "����ʧ�ܣ�";
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail7]"+result,"server.txt");
				ret=null;
			}			
		} 
		catch (Exception e1) 
		{
			//e1.printStackTrace();	//����쳣��Ϣ
			ret=null;
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail7]Records","server.txt");
		}		
		return ret;
	}
	
	//���½��׼�¼��Ϣ
	private JSONObject updatecolumns(String classrst) 
	{
		JSONObject ret=new JSONObject();
		JSONObject jsonObject = null; 
		String pathID="";
		String cabinetNumber="";
		String pathName="";
		String productID="";
		String productNum="";
		String pathCount="";
		String pathStatus="";
		String pathRemaining="";
		String lastedittime="";
		String isdisable="";
		try {
			jsonObject = new JSONObject(classrst); 
			pathID=jsonObject.getString("pathID");
			cabinetNumber=jsonObject.getString("cabinetNumber");
			pathName=jsonObject.getString("pathName");
			productID=jsonObject.getString("productID");
			productNum=jsonObject.getString("productNum");
			pathCount=jsonObject.getString("pathCount");
			pathStatus=jsonObject.getString("pathStatus");
			pathRemaining=jsonObject.getString("pathRemaining");
			lastedittime=jsonObject.getString("lastedittime");
			isdisable=jsonObject.getString("isdisable");
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send0.2=pathID="+pathID+"cabinetNumber="+cabinetNumber+"pathName="+pathName+"productID="
				+productID+"productNum="+productNum+"pathCount="+pathCount+"pathStatus="+pathStatus+"pathRemaining="+pathRemaining+"lastedittime="+lastedittime+
				"isdisable="+isdisable,"server.txt");			
			  	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		//��װjson��
		//���׼�¼��Ϣ
		String target = httpStr+"/api/vmcPathStatus";	//Ҫ�ύ��Ŀ���ַ
				
		JSONObject param=null;
		try {
			JSONObject trans=new JSONObject();
			trans.put("pathID", pathID);
			trans.put("cabinetNumber", cabinetNumber);
			trans.put("pathName", pathName);
			trans.put("productID", productID);	
			trans.put("productNum", productNum);	
			trans.put("pathCount", pathCount);	
			trans.put("pathStatus", pathStatus);	
			trans.put("pathRemaining", pathRemaining);
			trans.put("lastedittime", lastedittime);	
			trans.put("isdisable", isdisable);
			JSONArray PATHLIST=new JSONArray();
			PATHLIST.put(trans);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","PATHLIST="+PATHLIST.toString(),"server.txt");
			param=new JSONObject();
			param.put("VMC_NO", vmc_no);
			param.put("PATHLIST", PATHLIST);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","param="+param.toString(),"server.txt");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			ret.put("cabinetNumber", cabinetNumber);
//			ret.put("pathName", pathName);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
			
		HttpClient httpclient = new DefaultHttpClient();	//����HttpClient����
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//����ʱ
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//��ȡ��ʱ
		HttpPost httppost = new HttpPost(target);	//����HttpPost����
//					//��ӵ��༯�У�����key,value����ΪString
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("VMC_NO", "junpeng0004");
//					parammap.put("TOTAL",1);	
//					parammap.put("ACTUAL_TOTAL",1);
//					Oreder_Product_Pay orederProductPayList=new Oreder_Product_Pay();
//					parammap.put("TRANSACTION",orederProductPayList);
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",,parammap.toString());
//					//��map�༯תΪjson��ʽ
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",,param.toString());
		//���params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Token", Tok));
		params.add(new BasicNameValuePair("param", param.toString()));
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","columns="+params.toString(),"server.txt");
		try{
			httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //���ñ��뷽ʽ
			HttpResponse httpResponse = httpclient.execute(httppost);	//ִ��HttpClient����
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
				result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
				JSONObject object=new JSONObject(result);
				int errType =  object.getInt("Error");
				//�����й���
				if(errType>0)
				{
					ret=null;
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail8]column","server.txt");
				}
				else
				{
					ret.put("cabinetNumber", cabinetNumber);
					ret.put("pathName", pathName);
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok8]","server.txt");
				}	
			}else{
				result = "����ʧ�ܣ�";
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1[fail8]="+result,"server.txt");
				ret=null;
			}			
		} 
		catch (Exception e1) 
		{
			//e1.printStackTrace();	//����쳣��Ϣ
			ret=null;
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail8]SETFAILMAIN","server.txt");
		}		
		return ret;
	}

}
