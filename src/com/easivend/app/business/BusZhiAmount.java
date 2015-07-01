package com.easivend.app.business;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BusZhiAmount  extends Activity
{
	private final int SPLASH_DISPLAY_LENGHT = 1000; // �ӳ�1��
	public static BusZhiAmount BusZhiAmountAct=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	TextView txtbuszhiamountcount=null,txtbuszhiamountAmount=null,txtbuszhiamountbillAmount=null,txtbuszhiamounttime=null,
			txtbuszhiamounttsxx=null;
	ImageButton imgbtnbuszhiamountqxzf=null,imgbtnbuszhiamountqtzf=null;
	float amount=0;//��Ʒ��Ҫ֧�����
	float billmoney=0,coinmoney=0,money=0;//Ͷ�ҽ��
	float RealNote=0,RealCoin=0,RealAmount=0;//�˱ҽ��
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
    private String zhifutype = "0";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
//    private String id="";
    private String out_trade_no=null;
    private int iszhiamount=0;//1�ɹ�Ͷ��Ǯ,0û�гɹ�Ͷ��Ǯ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buszhiamount);
		BusZhiAmountAct = this;
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
		out_trade_no=ToolClass.out_trade_no(BusZhiAmount.this);
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
		OrderDetail.setOrdereID(out_trade_no);
    	OrderDetail.setPayType(Integer.parseInt(zhifutype));
		txtbuszhiamountcount= (TextView) findViewById(R.id.txtbuszhiamountcount);
		txtbuszhiamountcount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiamountAmount= (TextView) findViewById(R.id.txtbuszhiamountAmount);
		txtbuszhiamountAmount.setText(String.valueOf(amount));
		txtbuszhiamountbillAmount= (TextView) findViewById(R.id.txtbuszhiamountbillAmount);		
		txtbuszhiamounttime = (TextView) findViewById(R.id.txtbuszhiamounttime);
		txtbuszhiamounttsxx = (TextView) findViewById(R.id.txtbuszhiamounttsxx);
		timer.schedule(task, 1000, 1000);       // timeTask 
		imgbtnbuszhiamountqxzf = (ImageButton) findViewById(R.id.imgbtnbuszhiamountqxzf);
		imgbtnbuszhiamountqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	if(BusgoodsSelect.BusgoodsSelectAct!=null)
					BusgoodsSelect.BusgoodsSelectAct.finish(); 
		    	finishActivity();
		    }
		});
		imgbtnbuszhiamountqtzf = (ImageButton) findViewById(R.id.imgbtnbuszhiamountqtzf);
		imgbtnbuszhiamountqtzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finishActivity();
		    }
		});
		//*****************
		//ע��Ͷ�����������
		//*****************
		EVprotocolAPI.setCallBack(new jniInterfaceImp());

		//��ʱ1s
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {            	
        		//��ֽ��Ӳ����
            	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,1);
            }

		}, SPLASH_DISPLAY_LENGHT);
	}
	
	//����һ��ר�Ŵ������ӿڵ�����
	private class jniInterfaceImp implements JNIInterface
	{

		@Override
		public void jniCallback(Map<String, Object> allSet) {
			float payin_amount=0,reamin_amount=0,payout_amount=0;
			// TODO Auto-generated method stub	
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<mdb�豸���","log.txt");
			Map<String, Object> Set= allSet;
			int jnirst=(Integer)Set.get("EV_TYPE");
			switch (jnirst)
			{
				case EVprotocolAPI.EV_MDB_ENABLE://�������߳�Ͷ�ҽ����Ϣ							
					break;
				case EVprotocolAPI.EV_MDB_B_INFO:	
					break;
				case EVprotocolAPI.EV_MDB_C_INFO:
					break;	
				case EVprotocolAPI.EV_MDB_HEART://������ѯ
					String bill_enable=((Integer)Set.get("bill_enable")==1)?"":"ֽ����������";
					String coin_enable=((Integer)Set.get("coin_enable")==1)?"":"Ӳ����������";
					String hopperString=null;
					if((Integer)Set.get("hopper1")>0)
						hopperString="[������]:"+ToolClass.gethopperstats((Integer)Set.get("hopper1"));
				  	txtbuszhiamounttsxx.setText("��ʾ��Ϣ��"+bill_enable+coin_enable+hopperString);
				  	billmoney=ToolClass.MoneyRec((Integer)Set.get("bill_recv"));	
				  	coinmoney=ToolClass.MoneyRec((Integer)Set.get("coin_recv"));
				  	money=billmoney+coinmoney;
				  	if(money>0)
				  	{
				  		iszhiamount=1;
				  		txtbuszhiamountbillAmount.setText(String.valueOf(money));
				  		OrderDetail.setSmallNote(billmoney);
				  		OrderDetail.setSmallConi(coinmoney);
				  		OrderDetail.setSmallAmount(money);
				  		if(money>=amount)
				  		{
				  			timer.cancel(); 
				  			tochuhuo();
				  		}
				  	}
					break;
				case EVprotocolAPI.EV_MDB_PAYOUT://����
					break;
				case EVprotocolAPI.EV_MDB_PAYBACK://�˱�
					RealNote=ToolClass.MoneyRec((Integer)Set.get("bill_changed"));	
					RealCoin=ToolClass.MoneyRec((Integer)Set.get("coin_changed"));	
					RealAmount=RealNote+RealCoin;						
					OrderDetail.setRealNote(RealNote);
			    	OrderDetail.setRealCoin(RealCoin);
			    	OrderDetail.setRealAmount(RealAmount);
			    	//�˱����
			    	if(RealAmount==money)
			    	{
			    		OrderDetail.setRealStatus(1);//�˿����				    		
			    	}
			    	//Ƿ��
			    	else
			    	{
			    		OrderDetail.setRealStatus(3);//�˿�ʧ��
			    		OrderDetail.setDebtAmount(money-RealAmount);//Ƿ����
			    	}
			    	OrderDetail.addLog(BusZhiAmount.this);
					//�ر�ֽ��Ӳ����
		  	    	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);			  	    	
		  	    	finish();
					break; 	
			}				
		}
		
	}
	
	//���õ���ʱ��ʱ��
	TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
  
            runOnUiThread(new Runnable() {      // UI thread 
                @Override 
                public void run() { 
                    recLen--; 
                    txtbuszhiamounttime.setText("����ʱ:"+recLen); 
                    if(recLen <= 0)
                    { 
                        timer.cancel(); 
                        finishActivity();
                    } 
                    //���Ͳ�ѯ����ָ��
                    queryLen++;
                    if(queryLen>=2)
                    {
                    	queryLen=0;
                    	EVprotocolAPI.EV_mdbHeart(ToolClass.getCom_id());
                    }                    
                } 
            }); 
        } 
    };
    //��������
    private void tochuhuo()
    {        
    	Intent intent = null;// ����Intent����                
    	intent = new Intent(BusZhiAmount.this, BusHuo.class);// ʹ��Accountflag���ڳ�ʼ��Intent
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
    	startActivityForResult(intent, REQUEST_CODE);// ��Accountflag
    }
    //����BusHuo������Ϣ
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  		// TODO Auto-generated method stub
  		if(requestCode==REQUEST_CODE)
  		{
  			if(resultCode==BusZhiAmount.RESULT_CANCELED)
  			{
  				Bundle bundle=data.getExtras();
  				int status=bundle.getInt("status");//�������1�ɹ�,0ʧ��
  				//1.
  				//�����ɹ�,��Ǯ
				if(status==1)
				{
					//��Ǯ
		  	    	EVprotocolAPI.EV_mdbCost(ToolClass.getCom_id(),ToolClass.MoneySend(amount));
		  	    	money-=amount;
				}
				//����ʧ��,����Ǯ
				else
				{					
				}
				//2.����Ͷ�ҽ��
  				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�˿�money="+money,"log.txt");
  				txtbuszhiamountbillAmount.setText(String.valueOf(money));
  				//ûʣ������ˣ����˱�
  				if(money==0)
  				{  					
			    	OrderDetail.addLog(BusZhiAmount.this);
					//�ر�ֽ��Ӳ����
		  	    	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);			  	    	
		  	    	finish();
  				}
  				//�˱�
  				else 
  				{
  					EVprotocolAPI.setCallBack(new jniInterfaceImp());
  					//�˱�
  	  	  	    	EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
				}  				
  			}			
  		}
  	}
    //��������
  	private void finishActivity()
  	{
  		timer.cancel(); 
  		if(iszhiamount==1)
  		{
  			OrderDetail.setPayStatus(2);//֧��ʧ��
  			//�˱�
  	    	EVprotocolAPI.EV_mdbPayback(ToolClass.getCom_id(),1,1);
  		} 
  		else 
  		{
  			//�ر�ֽ��Ӳ����
  	    	EVprotocolAPI.EV_mdbEnable(ToolClass.getCom_id(),1,1,0);  	    	
  			finish();
		}  		
  	}
}


