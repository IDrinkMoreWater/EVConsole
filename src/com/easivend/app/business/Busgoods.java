package com.easivend.app.business;

import java.util.Map;

import com.easivend.app.maintain.GoodsManager;
import com.easivend.app.maintain.HuodaoTest;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ProductAdapter;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_columnDAO;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Busgoods extends Activity 
{
	private final int SPLASH_DISPLAY_LENGHT = 5*60*1000; // �ӳ�5����	
	public static Busgoods BusgoodsAct=null;
	// ������Ʒ�б�
	Vmc_ProductAdapter productAdapter=null;
	GridView gvbusgoodsProduct=null;
	String proclassID=null;
	ImageButton imgbtnbusgoodsback=null;
	private String[] proID = null;
	private String[] productID = null;
	private String[] proImage = null;
    private String[] prosales = null;
    private String[] procount = null;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.busgoods);
		BusgoodsAct = this;
		this.gvbusgoodsProduct=(GridView) findViewById(R.id.gvbusgoodsProduct);
		this.imgbtnbusgoodsback=(ImageButton)findViewById(R.id.imgbtnbusgoodsback);
//		//��̬���ÿؼ��߶�
//    	//
//    	DisplayMetrics  dm = new DisplayMetrics();  
//        //ȡ�ô�������  
//        getWindowManager().getDefaultDisplay().getMetrics(dm);  
//        //���ڵĿ��  
//        int screenWidth = dm.widthPixels;          
//        //���ڸ߶�  
//        int screenHeight = dm.heightPixels;      
//        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ļ"+screenWidth
//				+"],["+screenHeight+"]","log.txt");	
//		
//    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvbusgoodsProduct.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
//    	linearParams.height =  screenHeight-170;// ���ؼ��ĸ�ǿ�����75����
//    	gvbusgoodsProduct.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
    	//����Ʒ����ҳ����ȡ����ѡ�е���Ʒ����
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		proclassID=bundle.getString("proclassID");
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID="+proclassID,"log.txt");
		//���������Ʒ����id
//		if((proclassID!=null)&&(proclassID.isEmpty()!=true))
//		{
//			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID��ѯ");
//			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
//	    	productAdapter=new Vmc_ProductAdapter();
//	    	productAdapter.showProInfo(this,"","",proclassID);  
//	    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProductName(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), this);// ����pictureAdapter����
//	    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ
//	    	proID=productAdapter.getProID();
//	    	productID=productAdapter.getProductID();
//	    	proImage=productAdapter.getProImage();
//	    	prosales=productAdapter.getProsales();
//	    	procount=productAdapter.getProcount();
//		}
//		//�����������Ʒ����id
//		else 
//		{
//			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒȫ����ѯ");
//			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
//	    	productAdapter=new Vmc_ProductAdapter();
//	    	productAdapter.showProInfo(this,"","shoudong","");  
//	    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProductName(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), this);// ����pictureAdapter����
//	    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ
//	    	proID=productAdapter.getProID();
//	    	productID=productAdapter.getProductID();
//	    	proImage=productAdapter.getProImage();
//	    	prosales=productAdapter.getProsales();
//	    	procount=productAdapter.getProcount();
//		}
		VmcProductThread vmcProductThread=new VmcProductThread();
    	vmcProductThread.execute();
    	
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(BusgoodsClass.BusgoodsClassAct!=null)
					BusgoodsClass.BusgoodsClassAct.finish(); 
				finish();
		    }
		});
		
		gvbusgoodsProduct.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(Integer.parseInt(procount[arg2])>0)
                {
	            	Intent intent = null;// ����Intent����                
	            	intent = new Intent(Busgoods.this, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
	            	intent.putExtra("proID", proID[arg2]);
	            	intent.putExtra("productID", productID[arg2]);
	            	intent.putExtra("proImage", proImage[arg2]);
	            	intent.putExtra("prosales", prosales[arg2]);
	            	intent.putExtra("procount", procount[arg2]);
	            	intent.putExtra("proType", "1");//1����ͨ����ƷID����,2����ͨ����������
	            	intent.putExtra("cabID", "");//�������,proType=1ʱ��Ч
		        	intent.putExtra("huoID", "");//����������,proType=1ʱ��Ч
	            	startActivity(intent);// ��Accountflag
                }
            }
        });
		//5������֮���˳�ҳ��
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {	
            	if(BusgoodsClass.BusgoodsClassAct!=null)
					BusgoodsClass.BusgoodsClassAct.finish(); 
				finish(); 
            }

		}, SPLASH_DISPLAY_LENGHT);
	}
	
	//****************
	//�첽�̣߳����ڲ�ѯ��¼
	//****************
	private class VmcProductThread extends AsyncTask<Void,Void,Vmc_ProductAdapter>
	{

		@Override
		protected Vmc_ProductAdapter doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
	    	productAdapter=new Vmc_ProductAdapter();
	    	//���������Ʒ����id
			if((proclassID!=null)&&(proclassID.isEmpty()!=true))
			{
				productAdapter.showProInfo(Busgoods.this,"","",proclassID);  
			}
			//�����������Ʒ����id
			else
			{
				productAdapter.showProInfo(Busgoods.this,"","shoudong","");  
			}
			return productAdapter;
		}

		@Override
		protected void onPostExecute(Vmc_ProductAdapter productAdapter) {
			// TODO Auto-generated method stub
			ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProductName(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), Busgoods.this);// ����pictureAdapter����
	    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ
	    	proID=productAdapter.getProID();
	    	productID=productAdapter.getProductID();
	    	proImage=productAdapter.getProImage();
	    	prosales=productAdapter.getProsales();
	    	procount=productAdapter.getProcount();	    	
		}
				
	}

}
