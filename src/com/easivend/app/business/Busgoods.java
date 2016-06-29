package com.easivend.app.business;

import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ProductAdapter;
import com.example.evconsole.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class Busgoods extends Activity 
{
	private final int SPLASH_DISPLAY_LENGHT = 5*60*1000; // �ӳ�5����	
	public static Busgoods BusgoodsAct=null;
	// ������Ʒ�б�
	Vmc_ProductAdapter productAdapter=null;
	GridView gvbusgoodsProduct=null;
	String proclassID=null;
	ImageView imgbtnbusgoodsback=null,imgback=null,imgnext=null;
	TextView txtpage=null;
	private String[] proID = null,pageproID=null;
	private String[] productID = null,pageproductID = null;
	private String[] productName = null,pageproductName = null;
	private String[] proImage = null,pageproImage = null;
	private String[] promarket = null,pagepromarket = null;
    private String[] prosales = null,pageprosales = null;
    private String[] procount = null,pageprocount = null;
    int count=0,page=0,pageindex=0;
	
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
		this.imgbtnbusgoodsback=(ImageView)findViewById(R.id.imgbtnbusgoodsback);
		this.imgback=(ImageView)findViewById(R.id.imgback);
		this.imgnext=(ImageView)findViewById(R.id.imgnext);
		this.txtpage=(TextView)findViewById(R.id.txtpage);
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
    	
    	imgback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(pageindex>0)
		    	{
		    		pageindex--;
		    		updategrid(pageindex);
		    	}
		    }
		});
        imgnext.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(pageindex<(page-1))
		    	{
		    		pageindex++;
		    		updategrid(pageindex);
		    	}
		    }
		});
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
            	if(Integer.parseInt(pageprocount[arg2])>0)
                {
	            	Intent intent = null;// ����Intent����                
	            	intent = new Intent(Busgoods.this, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
	            	intent.putExtra("proID", pageproID[arg2]);
	            	intent.putExtra("productID", pageproductID[arg2]);
	            	intent.putExtra("proImage", pageproImage[arg2]);
	            	intent.putExtra("prosales", pageprosales[arg2]);
	            	intent.putExtra("procount", pageprocount[arg2]);
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
			proID=productAdapter.getProID();
	    	productID=productAdapter.getProductID();
	    	productName=productAdapter.getProductName();
	    	proImage=productAdapter.getProImage();
	    	promarket=productAdapter.getPromarket();
	    	prosales=productAdapter.getProsales();
	    	procount=productAdapter.getProcount();
	    	count=proID.length;
	        page=(count%3>0)?(count/3)+1:(count/3);
	        pageindex=0;
	        updategrid(pageindex);	    	
		}
				
	}
	
	private void updategrid(int pagein)
    {
    	int max=((pagein*3+3)>count)?count:(pagein*3+3);
    	int index=pagein*3;
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<count="+count+",page="+page+",pageindex="+pagein+"index="+index+"max="+max,"log.txt");
    	StringBuilder info=new StringBuilder();
    	info.append(pagein+1).append("/").append(page);    	
    	txtpage.setText(info);
    	pageproID=new String[max-index];
    	pageproductID=new String[max-index];
    	pageproductName=new String[max-index];
    	pageproImage=new String[max-index];
    	pagepromarket=new String[max-index];
    	pageprosales=new String[max-index];
    	pageprocount=new String[max-index];
    	for(int i=0;index<max;i++,index++)
        {
    		pageproID[i]=proID[index];
    		pageproductID[i]=productID[index];
    		pageproductName[i]=productName[index];
    		pageproImage[i]=proImage[index];
    		pagepromarket[i]=promarket[index];
    		pageprosales[i]=prosales[index];
    		pageprocount[i]=procount[index];
        }
    	ProPictureAdapter adapter = new ProPictureAdapter(pageproductName,pagepromarket,pageprosales,pageproImage,pageprocount, Busgoods.this);// ����pictureAdapter����
    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ      
    }

}
