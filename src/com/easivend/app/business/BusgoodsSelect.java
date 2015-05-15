package com.easivend.app.business;

import com.easivend.common.ToolClass;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BusgoodsSelect extends Activity 
{
	public static BusgoodsSelect BusgoodsSelectAct=null;
	ImageView ivbusgoodselProduct=null;
	TextView txtbusgoodselName=null,txtbusgoodselPrice=null,txtbusgoodselNum=null,txtbusgoodselNo=null,
			txtbusgoodselAmount=null;
	ImageButton imgbtnbusgoodselbuy=null,imgbtnbusgoodselcancel=null,imgbtnbusgoodselsub=null,imgbtnbusgoodseladd=null;
	private String proID = null;
	private String productID = null;
	private String proImage = null;	
    private String prosales = null;
    private String procount = null;
    private String reamin_amount = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busgoodsselect);
		BusgoodsSelectAct = this;
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		proID=bundle.getString("proID");
		productID=bundle.getString("productID");
		proImage=bundle.getString("proImage");
		prosales=bundle.getString("prosales");
		procount=bundle.getString("procount");
		reamin_amount=bundle.getString("reamin_amount");
		
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="+productID+" proImage="
					+proImage+" prosales="+prosales+" procount="
					+procount+" reamin_amount="+reamin_amount);
		ivbusgoodselProduct = (ImageView) findViewById(R.id.ivbusgoodselProduct);
		/*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
        Bitmap bitmap = ToolClass.getLoacalBitmap(proImage); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
        ivbusgoodselProduct.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
		txtbusgoodselName = (TextView) findViewById(R.id.txtbusgoodselName);
		txtbusgoodselName.setText(proID);
		txtbusgoodselPrice = (TextView) findViewById(R.id.txtbusgoodselPrice);
		txtbusgoodselPrice.setText(prosales);
		txtbusgoodselNum = (TextView) findViewById(R.id.txtbusgoodselNum);
		txtbusgoodselNum.setText(procount);
		txtbusgoodselNo = (TextView) findViewById(R.id.txtbusgoodselNo);
		txtbusgoodselAmount = (TextView) findViewById(R.id.txtbusgoodselAmount);
		if(Integer.parseInt(txtbusgoodselNum.getText().toString())>0)
			txtbusgoodselAmount.setText(prosales);
		else
			txtbusgoodselAmount.setText("0");	
		imgbtnbusgoodselsub = (ImageButton) findViewById(R.id.imgbtnbusgoodselsub);
		imgbtnbusgoodselsub.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	
		    }
		});
		imgbtnbusgoodseladd = (ImageButton) findViewById(R.id.imgbtnbusgoodseladd);
		imgbtnbusgoodseladd.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	
		    }
		});
		imgbtnbusgoodselbuy = (ImageButton) findViewById(R.id.imgbtnbusgoodselbuy);		
		imgbtnbusgoodselbuy.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(Integer.parseInt(txtbusgoodselNum.getText().toString())>0)
		    	{
			    	Intent intent = null;// ����Intent����                
	            	intent = new Intent(BusgoodsSelect.this, BusZhiSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
	            	intent.putExtra("proID", proID);
	            	intent.putExtra("productID", productID);
	            	intent.putExtra("proType", "1");//1����ͨ����ƷID����,2����ͨ����������
	            	intent.putExtra("cabID", "0");//�������,proType=1ʱ��Ч
	            	intent.putExtra("huoID", "0");//����������,proType=1ʱ��Ч
	            	intent.putExtra("prosales", prosales);
	            	intent.putExtra("count", txtbusgoodselNo.getText().toString());
	            	intent.putExtra("reamin_amount", reamin_amount);
	            	startActivity(intent);// ��Accountflag
		    	}
		    }
		});
		this.imgbtnbusgoodselcancel=(ImageButton)findViewById(R.id.imgbtnbusgoodselcancel);
		imgbtnbusgoodselcancel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	}
	
}
