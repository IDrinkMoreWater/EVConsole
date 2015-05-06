package com.easivend.app.business;

import java.util.Map;

import com.easivend.app.maintain.HuodaoTest;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ProductAdapter;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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
    private float reamin_amount=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busgoods);
		this.gvbusgoodsProduct=(GridView) findViewById(R.id.gvbusgoodsProduct);
		this.imgbtnbusgoodsback=(ImageButton)findViewById(R.id.imgbtnbusgoodsback);
		//��̬���ÿؼ��߶�
    	//
    	DisplayMetrics  dm = new DisplayMetrics();  
        //ȡ�ô�������  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        //���ڵĿ��  
        int screenWidth = dm.widthPixels;          
        //���ڸ߶�  
        int screenHeight = dm.heightPixels;      
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ļ"+screenWidth
				+"],["+screenHeight+"]");	
		
    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvbusgoodsProduct.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
    	linearParams.height =  screenHeight-170;// ���ؼ��ĸ�ǿ�����75����
    	gvbusgoodsProduct.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
    	//����Ʒ����ҳ����ȡ����ѡ�е���Ʒ����
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		proclassID=bundle.getString("proclassID");
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID="+proclassID);
		//���������Ʒ����id
		if(proclassID!=null)
		{
			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
	    	productAdapter=new Vmc_ProductAdapter();
	    	productAdapter.showProInfo(this,"","",proclassID);  
	    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), this);// ����pictureAdapter����
	    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ
	    	proID=productAdapter.getProID();
	    	productID=productAdapter.getProductID();
	    	proImage=productAdapter.getProImage();
	    	prosales=productAdapter.getProsales();
	    	procount=productAdapter.getProcount();
		}
		
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	backDialog();
		    }
		});
		gvbusgoodsProduct.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;// ����Intent����                
            	intent = new Intent(Busgoods.this, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
            	intent.putExtra("proID", proID[arg2]);
            	intent.putExtra("productID", productID[arg2]);
            	intent.putExtra("proImage", proImage[arg2]);
            	intent.putExtra("prosales", prosales[arg2]);
            	intent.putExtra("procount", procount[arg2]);
            	intent.putExtra("reamin_amount", String.valueOf(reamin_amount));
            	startActivity(intent);// ��Accountflag
            }
        });
//		//ע��Ͷ�����������
//  	    EVprotocolAPI.setCallBack(new JNIInterface() 
//		{
//			
//			@Override
//			public void jniCallback(Map<String, Integer> allSet) {
//				float payin_amount=0,reamin_amount=0,payout_amount=0;
//				// TODO Auto-generated method stub	
//				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<payinout���");
//				Map<String, Integer> Set= allSet;
//				int jnirst=Set.get("EV_TYPE");
//				switch (jnirst)
//				{
//					case EVprotocolAPI.EV_PAYIN_RPT://�������߳�Ͷ�ҽ����Ϣ						
//						payin_amount=ToolClass.MoneyRec((Integer) allSet.get("payin_amount"));
//						reamin_amount=ToolClass.MoneyRec((Integer) allSet.get("reamin_amount"));
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","API<<Ͷ��:"+payin_amount
//								+"�ܹ�:"+reamin_amount);							
//						//txtpayin.setText(String.valueOf(payin_amount));
//						//txtreamin.setText(String.valueOf(reamin_amount));
//						break;
//					case EVprotocolAPI.EV_PAYOUT_RPT://�������߳���������Ϣ
//						payout_amount=ToolClass.MoneyRec((Integer) allSet.get("payout_amount"));
//						reamin_amount=ToolClass.MoneyRec((Integer) allSet.get("reamin_amount"));
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<������"+String.valueOf(payout_amount));							
//						//txtpaymoney.setText(String.valueOf(payout_amount));
//						break;	
//				}				
//			}
//			
//		});
//		EVprotocolAPI.cashControl(1);//���ձ��豸	
	}

		
	private void backDialog()
	{
		
		//��������Ի���
    	Dialog alert=new AlertDialog.Builder(Busgoods.this)
    		.setTitle("�Ի���")//����
    		.setMessage("��ȷ��Ҫ����������")//��ʾ�Ի����е�����
    		.setIcon(R.drawable.ic_launcher)//����logo
    		.setPositiveButton("����", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
    			{				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) 
	    				{
	    					// TODO Auto-generated method stub	
	    					finish();
	    				}
    		      }
    			)		    		        
		        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
		        	{			
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							// TODO Auto-generated method stub				
						}
		        	}
		        )
		        .create();//����һ���Ի���
		        alert.show();//��ʾ�Ի���
		        //ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<flag="+flag[0]); 
	}
	
	
}
