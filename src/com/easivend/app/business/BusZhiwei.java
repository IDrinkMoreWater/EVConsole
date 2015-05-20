package com.easivend.app.business;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.http.Weixinghttp;
import com.easivend.http.Zhifubaohttp;
import com.easivend.model.Tb_vmc_system_parameter;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BusZhiwei extends Activity 
{
	public static BusZhiwei BusZhiweiAct=null;
	private final static int REQUEST_CODE=1;//声明请求标识
	TextView txtbuszhiweicount=null,txtbuszhiweirount=null,txtbuszhiweirst=null,txtbuszhiweitime=null;
	ImageButton imgbtnbuszhiweiqxzf=null,imgbtnbuszhiweiqtzf=null;
	ImageView ivbuszhiwei=null;
	private int recLen = 180; 
	private int queryLen = 0; 
    private TextView txtView; 
    Timer timer = new Timer(); 
	private String proID = null;
	private String productID = null;
	private String proType = null;
	private String cabID = null;
	private String huoID = null;
    private String prosales = null;
    private String count = null;
    private String reamin_amount = null;
    private String zhifutype = "4";//0现金，1银联，2支付宝声波，3支付宝二维码，4微信扫描
    private float amount=0;
    //线程进行微信二维码操作
    private Thread thread=null;
    private Handler mainhand=null,childhand=null;   
    private String out_trade_no=null;
    Weixinghttp weixinghttp=null;
    private int iszhiwei=0;//1成功生成了二维码,0没有成功生成二维码
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buszhiwei);
		BusZhiweiAct = this;
		//从商品页面中取得锁选中的商品
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		proID=bundle.getString("proID");
		productID=bundle.getString("productID");
		proType=bundle.getString("proType");
		cabID=bundle.getString("cabID");
		huoID=bundle.getString("huoID");
		prosales=bundle.getString("prosales");
		count=bundle.getString("count");
		reamin_amount=bundle.getString("reamin_amount");
		amount=Float.parseFloat(prosales)*Integer.parseInt(count);
		txtbuszhiweicount= (TextView) findViewById(R.id.txtbuszhiweicount);
		txtbuszhiweicount.setText(count);
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
		    	if(BusZhiSelect.BusZhiSelectAct!=null)
		    		BusZhiSelect.BusZhiSelectAct.finish(); 
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
		//线程进行微信二维码操作
		//***********************
		mainhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				//barweixingtest.setVisibility(View.GONE);
				// TODO Auto-generated method stub				
				switch (msg.what)
				{
					case Weixinghttp.SETMAIN://子线程接收主线程消息
						ivbuszhiwei.setImageBitmap(ToolClass.createQRImage(msg.obj.toString()));
						txtbuszhiweirst.setText("交易结果:"+msg.obj.toString());
						iszhiwei=1;
						break;
					case Weixinghttp.SETPAYOUTMAIN://子线程接收主线程消息
						txtbuszhiweirst.setText("交易结果:退款成功");
						break;
					case Weixinghttp.SETDELETEMAIN://子线程接收主线程消息
						txtbuszhiweirst.setText("交易结果:撤销成功");
						break;	
					case Weixinghttp.SETQUERYMAINSUCC://子线程接收主线程消息		
						txtbuszhiweirst.setText("交易结果:交易成功");
						reamin_amount=String.valueOf(amount);
						timer.cancel(); 
						tochuhuo();
						break;
					case Weixinghttp.SETFAILPROCHILD://子线程接收主线程消息
					case Weixinghttp.SETFAILBUSCHILD://子线程接收主线程消息	
					case Weixinghttp.SETFAILQUERYPROCHILD://子线程接收主线程消息
					case Weixinghttp.SETFAILQUERYBUSCHILD://子线程接收主线程消息		
					case Weixinghttp.SETQUERYMAIN://子线程接收主线程消息	
					case Weixinghttp.SETFAILPAYOUTPROCHILD://子线程接收主线程消息		
					case Weixinghttp.SETFAILPAYOUTBUSCHILD://子线程接收主线程消息
					case Weixinghttp.SETFAILDELETEPROCHILD://子线程接收主线程消息		
					case Weixinghttp.SETFAILDELETEBUSCHILD://子线程接收主线程消息		
						txtbuszhiweirst.setText("交易结果:"+msg.obj.toString());
						break;		
				}				
			}
			
		};
		//启动用户自己定义的类
		weixinghttp=new Weixinghttp(mainhand);
		thread=new Thread(weixinghttp,"Weixinghttp Thread");
		thread.start();
		//发送订单
		sendzhiwei();
	}
	//发送订单
	private void sendzhiwei()
	{				
    	// 将信息发送到子线程中
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
	//查询交易
	private void queryzhiwei()
	{
		// 将信息发送到子线程中
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
	
	//调用倒计时定时器
	TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
  
            runOnUiThread(new Runnable() {      // UI thread 
                @Override 
                public void run() { 
                    recLen--; 
                    txtbuszhiweitime.setText("倒计时:"+recLen); 
                    if(recLen <= 0)
                    { 
                        timer.cancel(); 
                        finishActivity();
                    } 
                    //发送查询交易指令
                    if(iszhiwei==1)
                    {
	                    queryLen++;
	                    if(queryLen>=10)
	                    {
	                    	queryLen=0;
	                    	queryzhiwei();
	                    }
                    }
                } 
            }); 
        } 
    };
	//结束界面
	private void finishActivity()
	{		
		timer.cancel(); 
		finish();		
	}
	
	//跳到出货页面
	private void tochuhuo()
	{
		Intent intent = null;// 创建Intent对象                
    	intent = new Intent(BusZhiwei.this, BusHuo.class);// 使用Accountflag窗口初始化Intent    	
    	intent.putExtra("out_trade_no", out_trade_no);
    	intent.putExtra("proID", proID);
    	intent.putExtra("productID", productID);
    	intent.putExtra("proType", proType);
    	intent.putExtra("cabID", cabID);
    	intent.putExtra("huoID", huoID);
    	intent.putExtra("prosales", prosales);
    	intent.putExtra("count", count);
    	intent.putExtra("reamin_amount", reamin_amount);
    	intent.putExtra("zhifutype", zhifutype);
    	startActivityForResult(intent, REQUEST_CODE);// 打开Accountflag
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==BusZhiwei.RESULT_CANCELED)
			{
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<退款amount="+amount);
				////退款
			}			
		}
	}
}
