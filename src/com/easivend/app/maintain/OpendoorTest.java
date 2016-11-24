package com.easivend.app.maintain;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OpendoorTest extends Activity 
{
	private Button btnchuhuo=null,btncancel=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opendoortest);
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
		//����
		btnchuhuo = (Button)findViewById(R.id.btnchuhuo);		
		btnchuhuo.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	//����
		    	String httpStr="";
				String target2 = httpStr+"/api/vmcPoll";	//Ҫ�ύ��Ŀ���ַ
				
				//4.׼��������Ϣ����
				StringRequest stringRequest2 = new StringRequest(Method.POST, target2,  new Response.Listener<String>() {  
					@Override  
					public void onResponse(String response) {  
					   
//					  //�������ɹ�
//						result = response;	//��ȡ���ص��ַ���
//						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1="+result,"server.txt");
//						JSONObject object;
//						try {
//							object = new JSONObject(result);
//							int errType =  object.getInt("Error");
//							//�����й���
//							if(errType>0)
//							{
//								tomain2.what=SETERRFAILHEARTMAIN;
//								tomain2.obj=object.getString("Message");
//								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail2]SETERRFAILHEARTMAIN","server.txt");
//							}
//							else
//							{
//								tomain2.what=SETHEARTMAIN;
//								tomain2.obj=result;
//								ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[ok2]","server.txt");
//							}
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}										    	    
//						mainhand.sendMessage(tomain2); // ������Ϣ
					}  
				}, new Response.ErrorListener() {  
					@Override  
					public void onErrorResponse(VolleyError error) {  
//						result = "����ʧ�ܣ�";
//						tomain2.what=SETFAILMAIN;
//						mainhand.sendMessage(tomain2); // ������Ϣ
//						ToolClass.Log(ToolClass.INFO,"EV_SERVER","rec1=[fail2]SETFAILMAIN"+result,"server.txt");
					}  
				}) 
				{  
					@Override  
					protected Map<String, String> getParams() throws AuthFailureError {  
						//3.���params
						Map<String, String> map = new HashMap<String, String>();  
//						map.put("Token", Tok);  
//						map.put("LastPollTime", ToolClass.getLasttime());
//						ToolClass.Log(ToolClass.INFO,"EV_SERVER","Send1="+map.toString(),"server.txt");
						return map;  
				   }  
				}; 	
				//5.������Ϣ�����͵�������
//				mQueue.add(stringRequest2);	
		    }
		});
		//�˳�
		btncancel = (Button)findViewById(R.id.btncancel);		
		btncancel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	finish();
		    }
		});
	}
	
}
