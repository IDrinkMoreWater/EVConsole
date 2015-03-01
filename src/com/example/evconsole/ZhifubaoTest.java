package com.example.evconsole;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.evprotocol.ToolClass;
import com.easivend.http.Zhifubaohttp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ZhifubaoTest extends Activity 
{
	private ImageView imgzhifubaotest=null;
	private EditText edtzhifubaotest=null;
	private Button btnzhifubaotestok=null,btnzhifubaotestcancel=null;
	private final int SETMAIN=1;//what���,���߳̽��յ����߳�֧��������ά��
	private final int SETCHILD=2;//what���,���͸����߳�֧��������
	private Handler mainhand=null,childhand=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhifubaotest);	
		mainhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case SETMAIN://���߳̽������߳���Ϣ
						imgzhifubaotest.setImageBitmap(ToolClass.createQRImage(msg.obj.toString()));
						break;
				}				
			}
			
		};	
		//�����û��Լ��������
		final Zhifubaohttp zhifubaohttp=new Zhifubaohttp(mainhand);
		Thread thread=new Thread(zhifubaohttp,"Zhifubaohttp Thread");
		thread.start();
		imgzhifubaotest = (ImageView) findViewById(R.id.imgzhifubaotest);
		edtzhifubaotest = (EditText) findViewById(R.id.edtzhifubaotest);
		
		btnzhifubaotestok = (Button)findViewById(R.id.btnzhifubaotestok);
		btnzhifubaotestok.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	// ����Ϣ���͵����߳���
		    	childhand=zhifubaohttp.obtainHandler();
				Message childmsg=childhand.obtainMessage();
				childmsg.what=SETCHILD;
				JSONObject ev=null;
				try {
					ev=new JSONObject();
					ev.put("id", "0001");
					ev.put("money", edtzhifubaotest.getText());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				childmsg.obj=ev;
				childhand.sendMessage(childmsg);
		    }
		});
		//�˳�
		btnzhifubaotestcancel = (Button)findViewById(R.id.btnzhifubaotestcancel);		
		btnzhifubaotestcancel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	}

}
