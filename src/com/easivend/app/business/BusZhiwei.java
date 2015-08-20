package com.easivend.app.business;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.http.EVServerhttp;
import com.easivend.http.Weixinghttp;
import com.easivend.http.Zhifubaohttp;
import com.easivend.model.Tb_vmc_system_parameter;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BusZhiwei extends Activity 
{
	private final int SPLASH_DISPLAY_LENGHT = 1500; // �ӳ�1.5��
	//���ȶԻ���
	ProgressDialog dialog= null;
	public static BusZhiwei BusZhiweiAct=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	TextView txtbuszhiweicount=null,txtbuszhiweirount=null,txtbuszhiweirst=null,txtbuszhiweitime=null;
	ImageButton imgbtnbuszhiweiqxzf=null,imgbtnbuszhiweiqtzf=null;
	ImageView ivbuszhiwei=null;
	private int recLen = 180; 
	private int queryLen = 0; 
    private TextView txtView; 
    Timer timer = new Timer(); 
//	private String proID = null;
//	private String productID = null;
//	private String proType = null;
//	private String cabID = null;
//	private String huoID = null;
//    private String prosales = null;
//    private String count = null;
//    private String reamin_amount = null;
    private String zhifutype = "4";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
    private float amount=0;
    //�߳̽���΢�Ŷ�ά�����
    private Thread thread=null;
    private Handler mainhand=null,childhand=null;   
    private String out_trade_no=null;
    Weixinghttp weixinghttp=null;
    private int iszhiwei=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buszhiwei);
		BusZhiweiAct = this;
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
//		Intent intent=getIntent();
//		Bundle bundle=intent.getExtras();
//		proID=bundle.getString("proID");
//		productID=bundle.getString("productID");
//		proType=bundle.getString("proType");
//		cabID=bundle.getString("cabID");
//		huoID=bundle.getString("huoID");
//		prosales=bundle.getString("prosales");
//		count=bundle.getString("count");
//		reamin_amount=bundle.getString("reamin_amount");
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
		txtbuszhiweicount= (TextView) findViewById(R.id.txtbuszhiweicount);
		txtbuszhiweicount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiweirount= (TextView) findViewById(R.id.txtbuszhiweirount);
		txtbuszhiweirount.setText(String.valueOf(amount));
		txtbuszhiweirst= (TextView) findViewById(R.id.txtbuszhiweirst);
		txtbuszhiweitime= (TextView) findViewById(R.id.txtbuszhiweitime);
		ivbuszhiwei= (ImageView) findViewById(R.id.ivbuszhiwei);
		timer.schedule(task, 1000, 1000);       // timeTask 
		imgbtnbuszhiweiqxzf = (ImageButton) findViewById(R.id.imgbtnbuszhiweiqxzf);
		imgbtnbuszhiweiqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	 
		    	if(BusgoodsSelect.BusgoodsSelectAct!=null)
					BusgoodsSelect.BusgoodsSelectAct.finish(); 
		    	finishActivity();		    	
		    }
		});
		imgbtnbuszhiweiqtzf = (ImageButton) findViewById(R.id.imgbtnbuszhiweiqtzf);
		imgbtnbuszhiweiqtzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finishActivity();		    	
		    }
		});
		//***********************
		//�߳̽���΢�Ŷ�ά�����
		//***********************
		mainhand=new Handler()
		{
			int con=0;
			@Override
			public void handleMessage(Message msg) {
				//barweixingtest.setVisibility(View.GONE);
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case Weixinghttp.SETMAIN://���߳̽������߳���Ϣ
						ivbuszhiwei.setImageBitmap(ToolClass.createQRImage(msg.obj.toString()));
						txtbuszhiweirst.setText("���׽��:"+msg.obj.toString());
						iszhiwei=1;
						break;
					case Weixinghttp.SETFAILNETCHILD://���߳̽������߳���Ϣ
						txtbuszhiweirst.setText("���׽��:����"+msg.obj.toString()+con);
						con++;
						break;	
					case Weixinghttp.SETPAYOUTMAIN://���߳̽������߳���Ϣ
						txtbuszhiweirst.setText("���׽��:�˿�ɹ�");
						break;
					case Weixinghttp.SETDELETEMAIN://���߳̽������߳���Ϣ
						txtbuszhiweirst.setText("���׽��:�����ɹ�");
						break;	
					case Weixinghttp.SETQUERYMAINSUCC://���߳̽������߳���Ϣ		
						txtbuszhiweirst.setText("���׽��:���׳ɹ�");
						//reamin_amount=String.valueOf(amount);
						timer.cancel(); 
						tochuhuo();
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
						txtbuszhiweirst.setText("���׽��:"+msg.obj.toString());
						break;		
				}				
			}
			
		};
		//�����û��Լ��������
		weixinghttp=new Weixinghttp(mainhand);
		thread=new Thread(weixinghttp,"Weixinghttp Thread");
		thread.start();
		//��ʱ3s
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {            	
            	//���Ͷ���
        		sendzhiwei();
            }

		}, SPLASH_DISPLAY_LENGHT);			
	}
	//���Ͷ���
	private void sendzhiwei()
	{				
    	// ����Ϣ���͵����߳���
    	childhand=weixinghttp.obtainHandler();
		Message childmsg=childhand.obtainMessage();
		childmsg.what=Weixinghttp.SETCHILD;
		JSONObject ev=null;
		try {
			ev=new JSONObject();
			out_trade_no=ToolClass.out_trade_no(BusZhiwei.this);
	        ev.put("out_trade_no", out_trade_no);
			ev.put("total_fee", String.valueOf(amount));
			Log.i("EV_JNI","Send0.1="+ev.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		childmsg.obj=ev;
		childhand.sendMessage(childmsg);
	}
	//��ѯ����
	private void queryzhiwei()
	{
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
	
	//���õ���ʱ��ʱ��
	TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
  
            runOnUiThread(new Runnable() {      // UI thread 
                @Override 
                public void run() { 
                    recLen--; 
                    txtbuszhiweitime.setText("����ʱ:"+recLen); 
                    //�˳�ҳ��
                    if(recLen <= 0)
                    { 
                        timer.cancel(); 
                        finishActivity();
                    } 
                    //���Ͳ�ѯ����ָ��
                    if(iszhiwei==1)
                    {
	                    queryLen++;
	                    if(queryLen>=5)
	                    {
	                    	queryLen=0;
	                    	queryzhiwei();
	                    }
                    }
                    //���Ͷ�������ָ��
                    else if(iszhiwei==0)
                    {
	                    queryLen++;
	                    if(queryLen>=5)
	                    {
	                    	queryLen=0;
	                    	//���Ͷ���
	                		sendzhiwei();
	                    }
                    }
                } 
            }); 
        } 
    };
	//��������
	private void finishActivity()
	{		
		timer.cancel(); 
		finish();		
	}
	
	//��������ҳ��
	private void tochuhuo()
	{
		Intent intent = null;// ����Intent����                
    	intent = new Intent(BusZhiwei.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent    	
//    	intent.putExtra("out_trade_no", out_trade_no);
//    	intent.putExtra("proID", proID);
//    	intent.putExtra("productID", productID);
//    	intent.putExtra("proType", proType);
//    	intent.putExtra("cabID", cabID);
//    	intent.putExtra("huoID", huoID);
//    	intent.putExtra("prosales", prosales);
//    	intent.putExtra("count", count);
//    	intent.putExtra("reamin_amount", reamin_amount);
//    	intent.putExtra("zhifutype", zhifutype);
    	OrderDetail.setOrdereID(out_trade_no);
    	OrderDetail.setPayType(Integer.parseInt(zhifutype));
    	OrderDetail.setSmallCard(amount);
    	startActivityForResult(intent, REQUEST_CODE);// ��Accountflag
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==BusZhiwei.RESULT_CANCELED)
			{
				Bundle bundle=data.getExtras();
  				int status=bundle.getInt("status");//�������1�ɹ�,0ʧ��
  				//1.
  				//�����ɹ�,��������
				if(status==1)
				{
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<���˿�","log.txt");
					OrderDetail.addLog(BusZhiwei.this);					
					finish();
				}
				//����ʧ��,��Ǯ
				else
				{
					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�˿�amount="+amount,"log.txt");
					dialog= ProgressDialog.show(BusZhiwei.this,"�����˿���","���Ժ�...");
					////�˿�
					OrderDetail.setRealStatus(3);//��¼�˱�ʧ��
					OrderDetail.addLog(BusZhiwei.this);	
					dialog.dismiss();
					finish();
				}
			}			
		}
	}
}
