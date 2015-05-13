package com.easivend.app.business;

import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private String zhifutype = "1";//0����ʹ�÷��ֽ�,1����ʹ���ֽ�
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
            	Intent intent = null;// ����Intent����                
            	intent = new Intent(BusZhiAmount.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent
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
