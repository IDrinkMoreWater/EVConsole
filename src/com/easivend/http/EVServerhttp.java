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

import com.easivend.app.maintain.GoodsManager;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;
import com.easivend.view.EVServerService;
import com.google.gson.Gson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EVServerhttp implements Runnable {
	String vmc_no="";
	String vmc_auth_code="";
	public final static int SETCHILD=2;//what标记,发送给子线程签到
	public final static int SETMAIN=1;//what标记,发送给主线程签到完成返回	
	public final static int SETERRFAILMAIN=4;//what标记,发送给主线程签到故障失败返回	
	
	public final static int SETHEARTCHILD=5;//what标记,发送给子线程心跳
	public final static int SETERRFAILHEARTMAIN=6;//what标记,发送给主线程心跳故障
	public final static int SETHEARTMAIN=7;//what标记,发送给主线程获取心跳返回
	
	public final static int SETCLASSCHILD=8;//what标记,发送给子线程获取商品分类
	public final static int SETERRFAILCLASSMAIN=9;//what标记,发送给主线程获取商品分类故障
	public final static int SETCLASSMAIN=10;//what标记,发送给主线程获取商品分类返回
	
	public final static int SETPRODUCTCHILD=11;//what标记,发送给子线程获取商品信息
	public final static int SETERRFAILRODUCTMAIN=12;//what标记,发送给主线程获取商品故障
	public final static int SETRODUCTMAIN=13;//what标记,发送给主线程获取商品返回
	
	public final static int SETHUODAOCHILD=14;//what标记,发送给子线程获取货道信息
	public final static int SETERRFAILHUODAOMAIN=15;//what标记,发送给主线程获取货道故障
	public final static int SETHUODAOMAIN=16;//what标记,发送给主线程获取货道返回
	
	public final static int SETHUODAOSTATUCHILD=17;//what标记,发送给子线程上报货道信息
	public final static int SETERRFAILHUODAOSTATUMAIN=18;//what标记,发送给主线程上报货道信息故障
	public final static int SETHUODAOSTATUMAIN=19;//what标记,发送给主线程上报货道信息
	
	public final static int SETDEVSTATUCHILD=20;//what标记,发送给子线程上报设备状态信息
	public final static int SETERRFAILDEVSTATUMAIN=21;//what标记,发送给主线程获取设备故障
	public final static int SETDEVSTATUMAIN=22;//what标记,发送给主线程获取设备返回	
	
	public final static int SETRECORDCHILD=23;//what标记,发送给子线程上报交易记录
	public final static int SETERRFAILRECORDMAIN=24;//what标记,发送给主线程上报交易故障
	public final static int SETRECORDMAIN=25;//what标记,发送给主线程获取上报交易返回
	
	public final static int SETCHECKCHILD=26;//what标记,发送给子线程更改签到信息码
	public final static int SETFAILMAIN=3;//what标记,发送给主线程网络失败返回	
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
		Looper.prepare();//用户自己定义的类，创建线程需要自己准备loop
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread start["+Thread.currentThread().getId()+"]","server.txt");
		childhand=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what)
				{
				case SETCHILD://子线程接收主线程签到消息					
					//1.得到本机编号信息
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
					//得到服务端地址信息
					Map<String, String> list=ToolClass.ReadConfigFile();
					if(list!=null)
					{				       
						httpStr= list.get("server");
					}
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 签到["+Thread.currentThread().getId()+"]="+httpStr,"server.txt");
										
					//设备签到
					String target = httpStr+"/api/vmcCheckin";	//要提交的目标地址
					HttpClient httpclient = new DefaultHttpClient();	//创建HttpClient对象
					httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
					httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
					HttpPost httppost = new HttpPost(target);	//创建HttpPost对象
					//1.添加到类集中，其中key,value类型为String
					Map<String,Object> parammap = new TreeMap<String,Object>() ;
					parammap.put("vmc_no",vmc_no);
					parammap.put("vmc_auth_code",vmc_auth_code);			
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap.toString(),"server.txt");
					//将2.map类集转为json格式
					Gson gson=new Gson();
					String param=gson.toJson(parammap);		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param.toString(),"server.txt");
					//3.添加params
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("param", param));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params.toString(),"server.txt");
					try {
						httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //设置编码方式
						HttpResponse httpResponse = httpclient.execute(httppost);	//执行HttpClient请求
						//向主线程返回信息
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
							//如果请求成功
							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//返回有故障
							if(errType>0)
							{
								tomain.what=SETERRFAILMAIN;
								tomain.obj=object.getString("Message");
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail1]SETERRFAILMAIN","server.txt");
							}
							else
							{
								tomain.what=SETMAIN;
								Tok=object.getString("Token");
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok1]","server.txt");
							}			    	    
				    	    mainhand.sendMessage(tomain); // 发送消息
						}else{
							result = "请求失败！";
							tomain.what=SETFAILMAIN;
							mainhand.sendMessage(tomain); // 发送消息
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail1]SETFAILMAIN"+result,"server.txt");
						}
						
						
					} 
			       catch (Exception e) 
			       {  
			           //e.printStackTrace();  
			    	   //向主线程返回网络失败信息
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // 发送消息
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail1]SETFAILMAIN","server.txt");
			       }
					break;
				case SETHEARTCHILD://子线程接收主线程心跳消息
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 心跳["+Thread.currentThread().getId()+"]","server.txt");
					//心跳
					String target2 = httpStr+"/api/vmcPoll";	//要提交的目标地址
					
					HttpClient httpclient2 = new DefaultHttpClient();	//创建HttpClient对象
					httpclient2.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
					httpclient2.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
					HttpPost httppost2 = new HttpPost(target2);	//创建HttpPost对象
					//添加到类集中，其中key,value类型为String
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("Token",Tok);
//					parammap.put("LastPollTime",new Date());			
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",parammap.toString(),"server.txt");
//					//将map类集转为json格式
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",param.toString(),"server.txt");
					//添加params
					List<NameValuePair> params2 = new ArrayList<NameValuePair>();
					params2.add(new BasicNameValuePair("Token", Tok));
					params2.add(new BasicNameValuePair("LastPollTime", ToolClass.getLasttime()));
					//params2.add(new BasicNameValuePair("LastPollTime", "2015-08-31T10:45:19"));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params2.toString(),"server.txt");
					try {
						httppost2.setEntity(new UrlEncodedFormEntity(params2, "utf-8")); //设置编码方式
						HttpResponse httpResponse = httpclient2.execute(httppost2);	//执行HttpClient请求
						//向主线程返回签到信息
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//返回有故障
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
							mainhand.sendMessage(tomain); // 发送消息
						}else{
							result = "请求失败！";
							tomain.what=SETFAILMAIN;
							mainhand.sendMessage(tomain); // 发送消息
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail2]SETFAILMAIN"+result,"server.txt");
						}
						
						//向主线程返回签到信息
//						Message tomain=mainhand.obtainMessage();
//						JSONObject object=new JSONObject(result);
//						int errType =  object.getInt("Error");
//						//返回有故障
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
//			    	    mainhand.sendMessage(tomain); // 发送消息
					}
					catch (Exception e) 
			        {  
			           //e.printStackTrace();  
			    	   //向主线程返回网络失败信息
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // 发送消息
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail2]SETFAILMAIN","server.txt");
			        }
					break;
				case SETCLASSCHILD://获取商品分类
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 获取商品分类["+Thread.currentThread().getId()+"]","server.txt");
					String target3 = httpStr+"/api/productClass";	//要提交的目标地址
					String LAST_EDIT_TIME3=msg.obj.toString();
					
					HttpClient httpclient3 = new DefaultHttpClient();	//创建HttpClient对象
					httpclient3.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
					httpclient3.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
					HttpPost httppost3 = new HttpPost(target3);	//创建HttpPost对象
					//添加到类集中，其中key,value类型为String
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("Token",Tok);
//					parammap.put("LastPollTime",new Date());			
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",parammap.toString(),"server.txt");
//					//将map类集转为json格式
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",param.toString(),"server.txt");
					//添加params
					List<NameValuePair> params3 = new ArrayList<NameValuePair>();
					params3.add(new BasicNameValuePair("Token", Tok));
					params3.add(new BasicNameValuePair("LAST_EDIT_TIME", LAST_EDIT_TIME3));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params3.toString(),"server.txt");
					try {
						httppost3.setEntity(new UrlEncodedFormEntity(params3, "utf-8")); //设置编码方式
						HttpResponse httpResponse = httpclient3.execute(httppost3);	//执行HttpClient请求
						//向主线程返回签到信息
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//返回有故障
							if(errType>0)
							{
								tomain.what=SETERRFAILCLASSMAIN;
								tomain.obj=object.getString("Message");
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail3]SETERRFAILCLASSMAIN","server.txt");
							}
							else
							{
								tomain.what=SETCLASSMAIN;
								tomain.obj=result;
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok3]","server.txt");
							}			    	    
				    	    mainhand.sendMessage(tomain); // 发送消息							
						}else{
							result = "请求失败！";
							tomain.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain); // 发送消息
				    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail3]SETFAILMAIN"+result,"server.txt");
						}

					}
					catch (Exception e) 
			        {  
			           //e.printStackTrace();  
			    	   //向主线程返回网络失败信息
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // 发送消息
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail3]SETFAILMAIN","server.txt");
			        }
					break;
				case SETPRODUCTCHILD://获取商品信息
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 获取商品信息["+Thread.currentThread().getId()+"]","server.txt");
					boolean isshp=false;
					String target4 = httpStr+"/api/productData";	//要提交的目标地址
					String LAST_EDIT_TIME4=msg.obj.toString();
					
					HttpClient httpclient4 = new DefaultHttpClient();	//创建HttpClient对象
					httpclient4.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
					httpclient4.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
					HttpPost httppost4 = new HttpPost(target4);	//创建HttpPost对象
					//添加到类集中，其中key,value类型为String
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("Token",Tok);
//					parammap.put("LastPollTime",new Date());			
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",parammap.toString(),"server.txt");
//					//将map类集转为json格式
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",param.toString(),"server.txt");
					//添加params
					List<NameValuePair> params4 = new ArrayList<NameValuePair>();
					params4.add(new BasicNameValuePair("Token", Tok));
					params4.add(new BasicNameValuePair("VMC_NO", vmc_no));
					params4.add(new BasicNameValuePair("PAGE_INDEX", ""));
					params4.add(new BasicNameValuePair("PAGE_SIZE", ""));
					params4.add(new BasicNameValuePair("LAST_EDIT_TIME", LAST_EDIT_TIME4));
					params4.add(new BasicNameValuePair("PRODUCT_NO", ""));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params4.toString(),"server.txt");
					try {
						httppost4.setEntity(new UrlEncodedFormEntity(params4, "utf-8")); //设置编码方式
						HttpResponse httpResponse = httpclient4.execute(httppost4);	//执行HttpClient请求
						//向主线程返回签到信息
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//返回有故障
							if(errType>0)
							{
								tomain.what=SETERRFAILRODUCTMAIN;
								tomain.obj=object.getString("Message");
								mainhand.sendMessage(tomain); // 发送消息
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail4]SETERRFAILRODUCTMAIN","server.txt");
							}
							else
							{
								isshp=true;	
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok4]","server.txt");
							}						
						}else{
							result = "请求失败！";
							tomain.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain); // 发送消息
				    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail4]SETFAILMAIN"+result,"server.txt");
						}
					}
					catch (Exception e) 
			        {  
			           //e.printStackTrace();  
						//向主线程返回网络失败信息
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // 发送消息
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail4]SETFAILMAIN","server.txt");
			        }
					
					//成功获取商品信息
					if(isshp)
					{
						try 
						{
							//向主线程返回签到信息
							Message tomain=mainhand.obtainMessage();
							tomain.what=SETRODUCTMAIN;
							tomain.obj=updateproductImg(result);
							mainhand.sendMessage(tomain); // 发送消息
						}
						catch (Exception e) 
				        {  
				           //e.printStackTrace();  
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail4]SETFAILMAIN=isshp","server.txt");
				    	   //向主线程返回网络失败信息
//							Message tomain=mainhand.obtainMessage();
//				    	    tomain.what=SETFAILMAIN;
//				    	    mainhand.sendMessage(tomain); // 发送消息
				        }
					}
					break;
				case SETHUODAOCHILD://获取货道信息
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 获取货道信息["+Thread.currentThread().getId()+"]","server.txt");
					String target5 = httpStr+"/api/vmcPathConfigDownload";	//要提交的目标地址
					String LAST_EDIT_TIME5=msg.obj.toString();
					
					HttpClient httpclient5 = new DefaultHttpClient();	//创建HttpClient对象
					httpclient5.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
					httpclient5.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
					HttpPost httppost5 = new HttpPost(target5);	//创建HttpPost对象
					//添加到类集中，其中key,value类型为String
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("Token",Tok);
//					parammap.put("LastPollTime",new Date());			
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",parammap.toString(),"server.txt");
//					//将map类集转为json格式
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",param.toString(),"server.txt");
					//添加params
					List<NameValuePair> params5 = new ArrayList<NameValuePair>();
					params5.add(new BasicNameValuePair("Token", Tok));
					params5.add(new BasicNameValuePair("VMC_NO", vmc_no));
					params5.add(new BasicNameValuePair("LAST_EDIT_TIME", LAST_EDIT_TIME5));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+params5.toString(),"server.txt");
					try {
						httppost5.setEntity(new UrlEncodedFormEntity(params5, "utf-8")); //设置编码方式
						HttpResponse httpResponse = httpclient5.execute(httppost5);	//执行HttpClient请求
						//向主线程返回信息
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//返回有故障
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
				    	    mainhand.sendMessage(tomain); // 发送消息							
						}else{
							result = "请求失败！";
							tomain.what=SETFAILMAIN;
				    	    mainhand.sendMessage(tomain); // 发送消息
				    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail5]SETFAILMAIN"+result,"server.txt");
						}

					}
					catch (Exception e) 
			        {  
			           //e.printStackTrace();  
			    	   //向主线程返回网络失败信息
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // 发送消息
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail5]SETFAILMAIN","server.txt");
			        }
					break;										
				case EVServerhttp.SETDEVSTATUCHILD://设备状态上报
					int bill_err=0;
	    			int coin_err=0;
					//1.得到本机编号信息
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
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 设备状态上报["+Thread.currentThread().getId()+"]","server.txt");
					//设备状态上报
					String target7 = httpStr+"/api/vmcStatus";	//要提交的目标地址
					HttpClient httpclient7 = new DefaultHttpClient();	//创建HttpClient对象
					httpclient7.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
					httpclient7.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
					HttpPost httppost7 = new HttpPost(target7);	//创建HttpPost对象
					//1.添加到类集中，其中key,value类型为String
					Map<String,Object> parammap7 = new TreeMap<String,Object>() ;
					parammap7.put("VMC_NO",vmc_no);
					parammap7.put("CLIENT_STATUS","0");
					parammap7.put("COINS_STATUS",coin_err);
					parammap7.put("NOTE_STATUS",bill_err);	
					parammap7.put("DOOR_STATUS","0");	
					parammap7.put("WAREHOUSE_TEMPERATURE","0");					
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap7.toString(),"server.txt");
					//将2.map类集转为json格式
					Gson gson7=new Gson();
					String param7=gson7.toJson(parammap7);		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param7.toString(),"server.txt");
					//3.添加params
					List<NameValuePair> params7 = new ArrayList<NameValuePair>();
					params7.add(new BasicNameValuePair("param", param7));
					params7.add(new BasicNameValuePair("Token", Tok));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params7.toString(),"server.txt");
					
					try {
						httppost7.setEntity(new UrlEncodedFormEntity(params7, "utf-8")); //设置编码方式
						HttpResponse httpResponse = httpclient7.execute(httppost7);	//执行HttpClient请求
						//向主线程返回信息
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//返回有故障
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
							mainhand.sendMessage(tomain); // 发送消息		
						}else{
							result = "请求失败！";
							tomain.what=SETFAILMAIN;
							mainhand.sendMessage(tomain); // 发送消息
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail6]SETFAILMAIN"+result,"server.txt");
						}
						
					} 
			       catch (Exception e) 
			       {  
			           //e.printStackTrace();  
			    	   //向主线程返回网络失败信息
						Message tomain=mainhand.obtainMessage();
			    	    tomain.what=SETFAILMAIN;
			    	    mainhand.sendMessage(tomain); // 发送消息
			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail6]SETFAILMAIN","server.txt");
			       }
					break;
				case EVServerhttp.SETRECORDCHILD://交易记录上报	
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 交易记录上报["+Thread.currentThread().getId()+"]","server.txt");
					//1.得到交易记录编号信息
					JSONArray ev8=null;
					try {
						ev8 = new JSONArray(msg.obj.toString());
						JSONArray retjson=new JSONArray();
						//向主线程返回信息
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
						mainhand.sendMessage(tomain); // 发送消息		
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						//向主线程返回信息
						Message tomain=mainhand.obtainMessage();
						tomain.what=SETFAILMAIN;
						mainhand.sendMessage(tomain); // 发送消息	
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail7]SETFAILMAIN","server.txt");
					}
					break;
				case SETHUODAOSTATUCHILD://货道状态上报消息	
//					String CABINET_NO=null;
//	    			String PATH_NO=null;
//	    			String PATH_STATUS=null;
//	    			String PATH_COUNT=null;
//	    			String PATH_REMAINING=null;
//	    			String PRODUCT_NO=null;
//	    			String PATH_ID=null;
//					//1.得到本机编号信息
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
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 货道状态上报["+Thread.currentThread().getId()+"]","server.txt");
					//1.得到交易记录编号信息
					JSONArray ev9=null;
					try {
						ev9 = new JSONArray(msg.obj.toString());
						JSONArray retjson=new JSONArray();
						//向主线程返回信息
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
						mainhand.sendMessage(tomain); // 发送消息		
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						//向主线程返回信息
						Message tomain=mainhand.obtainMessage();
						tomain.what=SETFAILMAIN;
						mainhand.sendMessage(tomain); // 发送消息	
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail8]SETFAILMAIN","server.txt");
					}
					
					
					
//					String target6 = httpStr+"/api/vmcPathStatus";	//要提交的目标地址
//					HttpClient httpclient6 = new DefaultHttpClient();	//创建HttpClient对象
//					HttpPost httppost6 = new HttpPost(target6);	//创建HttpPost对象
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
////					//1.添加到类集中，其中key,value类型为String
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
////					//将2.map类集转为json格式
////					Gson gson6=new Gson();
//					String param6=json.toString();		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param6.toString(),"server.txt");
//					//3.添加params
//					List<NameValuePair> params6 = new ArrayList<NameValuePair>();
//					params6.add(new BasicNameValuePair("param", param6));
//					params6.add(new BasicNameValuePair("Token", Tok));
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params6.toString(),"server.txt");
//					
//					try {
//						httppost6.setEntity(new UrlEncodedFormEntity(params6, "utf-8")); //设置编码方式
//						HttpResponse httpResponse = httpclient6.execute(httppost6);	//执行HttpClient请求
//						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
//							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
//							
//						}else{
//							result = "请求失败！";
//						}
//						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
//
//					} 
//			       catch (Exception e) 
//			       {  
//			           //e.printStackTrace();  
//			    	   //向主线程返回网络失败信息
//						Message tomain=mainhand.obtainMessage();
//			    	    tomain.what=SETFAILMAIN;
//			    	    mainhand.sendMessage(tomain); // 发送消息
//			    	    ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=fail8","server.txt");
//			       }
					break;	
				case SETCHECKCHILD://子线程接收主线程签到消息					
					//1.得到本机编号信息
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
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Thread 设备签到["+Thread.currentThread().getId()+"]","server.txt");
					//设备签到
					String target11 = httpStr+"/api/vmcCheckin";	//要提交的目标地址
					HttpClient httpclient11 = new DefaultHttpClient();	//创建HttpClient对象
					httpclient11.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
					httpclient11.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
					HttpPost httppost11 = new HttpPost(target11);	//创建HttpPost对象
					//1.添加到类集中，其中key,value类型为String
					Map<String,Object> parammap11 = new TreeMap<String,Object>() ;
					parammap11.put("vmc_no",vmc_no);
					parammap11.put("vmc_auth_code",vmc_auth_code);			
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+parammap11.toString(),"server.txt");
					//将2.map类集转为json格式
					Gson gson11=new Gson();
					String param11=gson11.toJson(parammap11);		
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param11.toString(),"server.txt");
					//3.添加params
					List<NameValuePair> params11 = new ArrayList<NameValuePair>();
					params11.add(new BasicNameValuePair("param", param11));
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params11.toString(),"server.txt");
					try {
						httppost11.setEntity(new UrlEncodedFormEntity(params11, "utf-8")); //设置编码方式
						HttpResponse httpResponse = httpclient11.execute(httppost11);	//执行HttpClient请求
						//向主线程返回信息
						Message tomain=mainhand.obtainMessage();
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
							//如果请求成功
							result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
							//获取返回的字符串
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
							JSONObject object=new JSONObject(result);
							int errType =  object.getInt("Error");
							//返回有故障
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
							result = "请求失败！";
							tomain.what=SETFAILMAIN;
							mainhand.sendMessage(tomain); // 发送消息
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail9]SETFAILMAIN"+result,"server.txt");
						}							
					} 
			       catch (Exception e) 
			       {  
			           //e.printStackTrace(); 
			    	   Message tomain=mainhand.obtainMessage();
						tomain.what=SETFAILMAIN;
						mainhand.sendMessage(tomain); // 发送消息	
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail9]SETFAILMAIN","server.txt");
			       }
					break;		
				default:
					break;
				}
			}
			
		};
		Looper.loop();//用户自己定义的类，创建线程需要自己准备loop
	}
		
	//更新商品图片信息
	private String updateproductImg(String classrst) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(classrst); 
		JSONArray arr1=jsonObject.getJSONArray("ProductList");
		JSONArray zhuheArray=new JSONArray();
		JSONObject zhuhejson = new JSONObject(); 
		for(int i=0;i<arr1.length();i++)
		{
			JSONObject object2=arr1.getJSONObject(i);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","更新商品图片="+object2.toString(),"server.txt");										
			JSONObject zhuheobj=object2;
			//第一步.获取商品图片名字
			String target6 = httpStr+"/api/productImage";	//要提交的目标地址
			HttpClient httpclient6 = new DefaultHttpClient();	//创建HttpClient对象
			httpclient6.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
			httpclient6.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
			HttpPost httppost6 = new HttpPost(target6);	//创建HttpPost对象
			JSONObject json=new JSONObject();
			try {
				json.put("VmcNo", vmc_no);
				json.put("attId", object2.getString("att_batch_id"));				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			String param6=json.toString();		
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send2="+param6.toString(),"server.txt");
			//3.添加params
			List<NameValuePair> params6 = new ArrayList<NameValuePair>();
			params6.add(new BasicNameValuePair("param", param6));
			params6.add(new BasicNameValuePair("Token", Tok));
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send3="+params6.toString(),"server.txt");
			
			try {
				httppost6.setEntity(new UrlEncodedFormEntity(params6, "utf-8")); //设置编码方式
				HttpResponse httpResponse = httpclient6.execute(httppost6);	//执行HttpClient请求
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
					//如果请求成功
					result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
					//获取返回的字符串
					JSONObject jsonObject3 = new JSONObject(result); 
					JSONArray arr3=jsonObject3.getJSONArray("ProductImageList");
					JSONObject object3=arr3.getJSONObject(0);
					String ATT_ID=object3.getString("ATT_ID");
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec2="+result+" ATT_ID="+ATT_ID,"server.txt");
					if(ATT_ID.isEmpty())
					{
						ToolClass.Log(ToolClass.INFO,"EV_SERVER","商品["+object2.getString("product_Name")+"]无图片","server.txt");
					}
					else
					{
						if(ToolClass.isImgFile(ATT_ID))
						{
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","商品["+object2.getString("product_Name")+"]图片已存在","server.txt");
						}
						else 
						{
							ToolClass.Log(ToolClass.INFO,"EV_SERVER","商品["+object2.getString("product_Name")+"]图片,下载图片","server.txt");
							//第二步.准备下载
							String url= httpStr+"/topic/getFile/"+ATT_ID + ".jpg";	//要提交的目标地址
							HttpClient httpClient4=new DefaultHttpClient();
							HttpGet httprequest4=new HttpGet(url);
							HttpResponse httpResponse4;
							httpResponse4=httpClient4.execute(httprequest4);
							if (httpResponse4.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
							{	//如果请求成功
								//result = EntityUtils.toString(httpResponse4.getEntity());	//获取返回的字符串
								//取得相关信息 取得HttpEntiy  
				                HttpEntity httpEntity4 = httpResponse4.getEntity();  
				                //获得一个输入流  
				                InputStream is = httpEntity4.getContent();  
				                Bitmap bitmap = BitmapFactory.decodeStream(is);  
				                ToolClass.saveBitmaptofile(bitmap,ATT_ID);				                 
							}else{
								result = "请求失败！";
								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec2=Pic[fail10]"+result,"server.txt");
							}
						}
						//第三步，把图片名字保存到json中
						zhuheobj.put("AttImg", ToolClass.getImgFile(ATT_ID));
					}
					
				}else{
					result = "请求失败！";
					//第三步，把图片名字保存到json中
					zhuheobj.put("AttImg", "");
					ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec2=[fail10]"+result,"server.txt");
				}
				

			} 
	       catch (Exception e) 
	       {  
	           //e.printStackTrace();  
	    	   //第三步，把图片名字保存到json中
			   zhuheobj.put("AttImg", "");
	    	   ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail10]="+i,"server.txt");
	       }
		   zhuheArray.put(zhuheobj);
		}
		zhuhejson.put("ProductList", zhuheArray);
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","reczhuhe="+zhuhejson.toString(),"server.txt");
		return zhuhejson.toString();
	}
	
	//更新交易记录信息
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
		//组装json包
		//交易记录信息
		String target = httpStr+"/api/vmcTransactionRecords";	//要提交的目标地址
				
		JSONObject param=null;
		try {
			JSONObject order=new JSONObject();
			order.put("orderID", 121);
			order.put("orderNo", orderNo);//订单id
			order.put("orderType", 1);
			order.put("orderTime", orderTime);//支付时间
			order.put("productCount", quantity);//交易数量
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
			order.put("orderStatus", orderStatus);//1未支付,2出货成功,3出货未完成
			order.put("shipTime", orderTime);//支付时间
			order.put("isNoFreight", 3);
			order.put("lastUpdateTime", orderTime);//支付时间
			order.put("orderDesc", "test");
			order.put("shouldPay", 2);
			order.put("integre", 3);
			order.put("sendStatus", 2);
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","order="+order.toString(),"server.txt");
			
			JSONObject orderpay=new JSONObject();
			orderpay.put("payID", 120);
			orderpay.put("orderID", 121);
			orderpay.put("payStatus", payStatus);//0未付款,1正在付款,2付款完成,3付款失败，4付款取消，5支付过程事件--仅银联支付)
			orderpay.put("payType", payType);//0现金,1支付宝声波,2银联,3支付宝二维码,4微信
			orderpay.put("shouldPay", shouldPay);//交易金额,如2.5元
			orderpay.put("realPay", 2);
			orderpay.put("RefundAmount", RefundAmount);//退款金额,如1.5元
			orderpay.put("smallChange", 0);
			orderpay.put("realNote", 1);
			orderpay.put("realCoins", 0);
			orderpay.put("smallNote", 1);
			orderpay.put("smallConis", 0);
			orderpay.put("integre", 0);
			orderpay.put("payDesc", "test");
			orderpay.put("payTime", orderTime);	//支付时间
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","orderpay="+orderpay.toString(),"server.txt");
			
			JSONObject orderrefund=new JSONObject();
			orderrefund.put("RefundId", 122);
			orderrefund.put("orderNo", orderNo);//商品id
			orderrefund.put("Reason", "test");
			orderrefund.put("Amount", 1);
			orderrefund.put("Refund", 0);
			orderrefund.put("Debt", 0);
			orderrefund.put("ResultCode", "test");
			orderrefund.put("TradeNo", "test");
			orderrefund.put("Description", "test");
			orderrefund.put("Status", Status);//0：未退款；1：正在退款；2：退款成功；3：退款失败'
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","orderrefund="+orderrefund.toString(),"server.txt");
			
			JSONObject orderproduct=new JSONObject();
			orderproduct.put("opID", 123);
			orderproduct.put("orderID", 121);
			orderproduct.put("productID", 120);
			orderproduct.put("productNo", productNo);//商品编号
			orderproduct.put("productType", "0001");
			orderproduct.put("quantity", quantity);//预计出货
			orderproduct.put("actualQuantity", actualQuantity);//实际出货
			orderproduct.put("productPrice", 1);			
			orderproduct.put("customerPrice", customerPrice);//商品单价
			orderproduct.put("productName", productName);//商品名称
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
		
		HttpClient httpclient = new DefaultHttpClient();	//创建HttpClient对象
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
		HttpPost httppost = new HttpPost(target);	//创建HttpPost对象
//				//添加到类集中，其中key,value类型为String
//				Map<String,Object> parammap = new TreeMap<String,Object>() ;
//				parammap.put("VMC_NO", "junpeng0004");
//				parammap.put("TOTAL",1);	
//				parammap.put("ACTUAL_TOTAL",1);
//				Oreder_Product_Pay orederProductPayList=new Oreder_Product_Pay();
//				parammap.put("TRANSACTION",orederProductPayList);
//				ToolClass.Log(ToolClass.INFO,"EV_SERVER",,parammap.toString());
//				//将map类集转为json格式
//				Gson gson=new Gson();
//				String param=gson.toJson(parammap);		
//				ToolClass.Log(ToolClass.INFO,"EV_SERVER",,param.toString());
		//添加params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Token", Tok));
		params.add(new BasicNameValuePair("param", param.toString()));
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","Records="+params.toString(),"server.txt");
		try{
			httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //设置编码方式
			HttpResponse httpResponse = httpclient.execute(httppost);	//执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
				//如果请求成功
				result = EntityUtils.toString(httpResponse.getEntity());	
				//获取返回的字符串
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
				JSONObject object=new JSONObject(result);
				int errType =  object.getInt("Error");
				//返回有故障
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
				result = "请求失败！";
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail7]"+result,"server.txt");
				ret=null;
			}			
		} 
		catch (Exception e1) 
		{
			//e1.printStackTrace();	//输出异常信息
			ret=null;
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail7]Records","server.txt");
		}		
		return ret;
	}
	
	//更新交易记录信息
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
		//组装json包
		//交易记录信息
		String target = httpStr+"/api/vmcPathStatus";	//要提交的目标地址
				
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
			
		HttpClient httpclient = new DefaultHttpClient();	//创建HttpClient对象
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//请求超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//读取超时
		HttpPost httppost = new HttpPost(target);	//创建HttpPost对象
//					//添加到类集中，其中key,value类型为String
//					Map<String,Object> parammap = new TreeMap<String,Object>() ;
//					parammap.put("VMC_NO", "junpeng0004");
//					parammap.put("TOTAL",1);	
//					parammap.put("ACTUAL_TOTAL",1);
//					Oreder_Product_Pay orederProductPayList=new Oreder_Product_Pay();
//					parammap.put("TRANSACTION",orederProductPayList);
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",,parammap.toString());
//					//将map类集转为json格式
//					Gson gson=new Gson();
//					String param=gson.toJson(parammap);		
//					ToolClass.Log(ToolClass.INFO,"EV_SERVER",,param.toString());
		//添加params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Token", Tok));
		params.add(new BasicNameValuePair("param", param.toString()));
		ToolClass.Log(ToolClass.INFO,"EV_SERVER","columns="+params.toString(),"server.txt");
		try{
			httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //设置编码方式
			HttpResponse httpResponse = httpclient.execute(httppost);	//执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
				result = EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
				JSONObject object=new JSONObject(result);
				int errType =  object.getInt("Error");
				//返回有故障
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
				result = "请求失败！";
				ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1[fail8]="+result,"server.txt");
				ret=null;
			}			
		} 
		catch (Exception e1) 
		{
			//e1.printStackTrace();	//输出异常信息
			ret=null;
			ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=Net[fail8]SETFAILMAIN","server.txt");
		}		
		return ret;
	}

}
