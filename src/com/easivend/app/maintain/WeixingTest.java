package com.easivend.app.maintain;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.ToolClass;
import com.easivend.http.Weixinghttp;
import com.easivend.http.Zhifubaohttp;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeixingTest extends Activity 
{
	private ImageView imgweixingtest=null;
	private TextView txtweixingtest=null;
	private EditText edtweixingtest=null;
	private Button btnweixingtestok=null,btnweixingtestcancel=null,btnweixingtestquery=null
			,btnweixingtestdelete=null,btnweixingtestpayout=null;
	private ProgressBar barweixingtest=null;
	
	private Handler mainhand=null,childhand=null;
	private String out_trade_no=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weixingtest);	
		mainhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				barweixingtest.setVisibility(View.GONE);
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case Weixinghttp.SETMAIN://���߳̽������߳���Ϣ
						imgweixingtest.setImageBitmap(ToolClass.createQRImage(msg.obj.toString()));
						txtweixingtest.setText("���׽��:"+msg.obj.toString());
						break;
					case Weixinghttp.SETFAILNETCHILD://���߳̽������߳���Ϣ
						txtweixingtest.setText("���׽��:"+msg.obj.toString());
						break;	
					case Weixinghttp.SETPAYOUTMAIN://���߳̽������߳���Ϣ
						txtweixingtest.setText("���׽��:�˿�ɹ�");
						break;
					case Weixinghttp.SETDELETEMAIN://���߳̽������߳���Ϣ
						txtweixingtest.setText("���׽��:�����ɹ�");
						break;	
					case Weixinghttp.SETQUERYMAINSUCC://���߳̽������߳���Ϣ	
						txtweixingtest.setText("���׽��:���׳ɹ�");
						break;		
					case Weixinghttp.SETFAILPROCHILD://���߳̽������߳���Ϣ
					case Weixinghttp.SETFAILBUSCHILD://���߳̽������߳���Ϣ	
					case Weixinghttp.SETFAILQUERYPROCHILD://���߳̽������߳���Ϣ
					case Weixinghttp.SETFAILQUERYBUSCHILD://���߳̽������߳���Ϣ		
					case Weixinghttp.SETQUERYMAIN://���߳̽������߳���Ϣ	
					case Weixinghttp.SETFAILPAYOUTPROCHILD://���߳̽������߳���Ϣ		
					case Weixinghttp.SETFAILPAYOUTBUSCHILD://���߳̽������߳���Ϣ
					case Weixinghttp.SETFAILDELETEPROCHILD://���߳̽������߳���Ϣ		
					case Weixinghttp.SETFAILDELETEBUSCHILD://���߳̽������߳���Ϣ	
						txtweixingtest.setText("���׽��:"+msg.obj.toString());
						break;		
				}				
			}
			
		};	
		//�����û��Լ��������
		final Weixinghttp weixinghttp=new Weixinghttp(mainhand);
		Thread thread=new Thread(weixinghttp,"Weixinghttp Thread");
		thread.start();
		imgweixingtest = (ImageView) findViewById(R.id.imgweixingtest);
		edtweixingtest = (EditText) findViewById(R.id.edtweixingtest);
		txtweixingtest = (TextView) findViewById(R.id.txtweixingtest);
		barweixingtest= (ProgressBar) findViewById(R.id.barweixingtest);
		//���id��Ϣ
		Intent intent=getIntent();
		final String id=intent.getStringExtra("id");
		Log.i("EV_JNI","Send0.0="+id);
		//���Ͷ���
		btnweixingtestok = (Button)findViewById(R.id.btnweixingtestok);
		btnweixingtestok.setOnClickListener(new OnClickListener() {			
		    @Override
		    public void onClick(View arg0) {
		    	barweixingtest.setVisibility(View.VISIBLE);
		    	// ����Ϣ���͵����߳���
		    	childhand=weixinghttp.obtainHandler();
				Message childmsg=childhand.obtainMessage();
				childmsg.what=Weixinghttp.SETCHILD;
				JSONObject ev=null;
				try {
					ev=new JSONObject();
					SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddhhmmssSSS"); //��ȷ������ 
			        String datetime = tempDate.format(new java.util.Date()).toString(); 					
			        out_trade_no=id+datetime;
			        ev.put("out_trade_no", out_trade_no);
					ev.put("total_fee", edtweixingtest.getText());
					Log.i("EV_JNI","Send0.1="+ev.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				childmsg.obj=ev;
				childhand.sendMessage(childmsg);
		    }
		});
		//��ѯ
		btnweixingtestquery = (Button)findViewById(R.id.btnweixingtestquery);	
		btnweixingtestquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	barweixingtest.setVisibility(View.VISIBLE);
		    	// ����Ϣ���͵����߳���
		    	childhand=weixinghttp.obtainHandler();
				Message childmsg=childhand.obtainMessage();
				childmsg.what=Zhifubaohttp.SETQUERYCHILD;
				JSONObject ev=null;
				try {
					ev=new JSONObject();
					ev.put("out_trade_no", out_trade_no);		
					//ev.put("out_trade_no", "000120150301113215800");	
					Log.i("EV_JNI","Send0.1="+ev.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				childmsg.obj=ev;
				childhand.sendMessage(childmsg);
		    }
		});
		//�˿�
		btnweixingtestpayout = (Button)findViewById(R.id.btnweixingtestpayout);	
		btnweixingtestpayout.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	barweixingtest.setVisibility(View.VISIBLE);
		    	// ����Ϣ���͵����߳���
		    	childhand=weixinghttp.obtainHandler();
				Message childmsg=childhand.obtainMessage();
				childmsg.what=Zhifubaohttp.SETPAYOUTCHILD;
				JSONObject ev=null;
				try {
					ev=new JSONObject();
					ev.put("out_trade_no", out_trade_no);		
					//ev.put("out_trade_no", "000120150301113215800");
					ev.put("total_fee", edtweixingtest.getText());
					ev.put("refund_fee", edtweixingtest.getText());
					SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddhhmmssSSS"); //��ȷ������ 
			        String datetime = tempDate.format(new java.util.Date()).toString(); 					
			        ev.put("out_refund_no", id+datetime);
					Log.i("EV_JNI","Send0.1="+ev.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				childmsg.obj=ev;
				childhand.sendMessage(childmsg);
		    }
		});	
		//�˳�
		btnweixingtestcancel = (Button)findViewById(R.id.btnweixingtestcancel);		
		btnweixingtestcancel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	}
	
}
