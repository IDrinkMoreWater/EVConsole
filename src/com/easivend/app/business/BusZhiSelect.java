package com.easivend.app.business;

import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class BusZhiSelect extends Activity
{
	public static BusZhiSelect BusZhiSelectAct=null;
	ImageView ivbuszhiselamount=null,ivbuszhiselzhier=null,ivbuszhiselweixing=null;
	ImageButton imgbtnbuszhiselfanhui=null;
	private String proID = null;
	private String productID = null;
	private String proType = null;
	private String cabID = null;
	private String huoID = null;
    private String prosales = null;
    private String count = null;
    private String reamin_amount = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buszhiselect);
		BusZhiSelectAct = this;
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
		ivbuszhiselamount = (ImageView) findViewById(R.id.ivbuszhiselamount);
		ivbuszhiselamount.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	Intent intent = null;// ����Intent����                
            	intent = new Intent(BusZhiSelect.this, BusZhiAmount.class);// ʹ��Accountflag���ڳ�ʼ��Intent
            	intent.putExtra("proID", proID);
            	intent.putExtra("productID", productID);
            	intent.putExtra("proType", proType);
            	intent.putExtra("cabID", cabID);
            	intent.putExtra("huoID", huoID);
            	intent.putExtra("prosales", prosales);
            	intent.putExtra("count", count);
            	intent.putExtra("reamin_amount", reamin_amount);
            	startActivity(intent);// ��Accountflag
		    }
		});
		ivbuszhiselzhier = (ImageView) findViewById(R.id.ivbuszhiselzhier);
		ivbuszhiselzhier.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	Intent intent = null;// ����Intent����                
            	intent = new Intent(BusZhiSelect.this, BusZhier.class);// ʹ��Accountflag���ڳ�ʼ��Intent
            	intent.putExtra("proID", proID);
            	intent.putExtra("productID", productID);
            	intent.putExtra("proType", proType);
            	intent.putExtra("cabID", cabID);
            	intent.putExtra("huoID", huoID);
            	intent.putExtra("prosales", prosales);
            	intent.putExtra("count", count);
            	intent.putExtra("reamin_amount", reamin_amount);
            	startActivity(intent);// ��Accountflag
		    }
		});
		ivbuszhiselweixing = (ImageView) findViewById(R.id.ivbuszhiselweixing);	
		ivbuszhiselweixing.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	Intent intent = null;// ����Intent����                
            	intent = new Intent(BusZhiSelect.this, BusZhiwei.class);// ʹ��Accountflag���ڳ�ʼ��Intent
            	intent.putExtra("proID", proID);
            	intent.putExtra("productID", productID);
            	intent.putExtra("proType", proType);
            	intent.putExtra("cabID", cabID);
            	intent.putExtra("huoID", huoID);
            	intent.putExtra("prosales", prosales);
            	intent.putExtra("count", count);
            	intent.putExtra("reamin_amount", reamin_amount);
            	startActivity(intent);// ��Accountflag
		    }
		});
		imgbtnbuszhiselfanhui = (ImageButton) findViewById(R.id.imgbtnbuszhiselfanhui);	
		imgbtnbuszhiselfanhui.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	}
	
}
