package com.easivend.app.business;

import java.text.SimpleDateFormat;

import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.model.Tb_vmc_system_parameter;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BusZhiAmount  extends Activity
{
	private final int SPLASH_DISPLAY_LENGHT = 5000; // �ӳ�5��
	public static BusZhiAmount BusZhiAmountAct=null;
	TextView txtbuszhiamountcount=null,txtbuszhiamountAmount=null,txtbuszhiamountbillAmount=null;
	ImageButton imgbtnbuszhiamountqxzf=null,imgbtnbuszhiamountqtzf=null;
	private String proID = null;
	private String productID = null;
	private String proType = null;
	private String cabID = null;
	private String huoID = null;
    private String prosales = null;
    private String count = null;
    private String reamin_amount = null;
    private String zhifutype = "0";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
    private String id="";
    private String out_trade_no=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buszhiamount);
		BusZhiAmountAct = this;
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
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
		float amount=Float.parseFloat(prosales)*Integer.parseInt(count);
		txtbuszhiamountcount= (TextView) findViewById(R.id.txtbuszhiamountcount);
		txtbuszhiamountcount.setText(count);
		txtbuszhiamountAmount= (TextView) findViewById(R.id.txtbuszhiamountAmount);
		txtbuszhiamountAmount.setText(String.valueOf(amount));
		txtbuszhiamountbillAmount= (TextView) findViewById(R.id.txtbuszhiamountbillAmount);
		txtbuszhiamountbillAmount.setText(reamin_amount);
		imgbtnbuszhiamountqxzf = (ImageButton) findViewById(R.id.imgbtnbuszhiamountqxzf);
		imgbtnbuszhiamountqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(BusZhiSelect.BusZhiSelectAct!=null)
		    		BusZhiSelect.BusZhiSelectAct.finish(); 
		    	if(BusgoodsSelect.BusgoodsSelectAct!=null)
					BusgoodsSelect.BusgoodsSelectAct.finish(); 
		    	finish();
		    }
		});
		imgbtnbuszhiamountqtzf = (ImageButton) findViewById(R.id.imgbtnbuszhiamountqtzf);
		imgbtnbuszhiamountqtzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
		//5���������ҳ��
		new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {            						
    	        out_trade_no=ToolClass.out_trade_no(BusZhiAmount.this);
            	Intent intent = null;// ����Intent����                
            	intent = new Intent(BusZhiAmount.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent
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
            	startActivity(intent);// ��Accountflag
            }

		}, SPLASH_DISPLAY_LENGHT);
	}
	
}
