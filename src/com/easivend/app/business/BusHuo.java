package com.easivend.app.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.easivend.app.business.BusPort.COMReceiver;
import com.easivend.app.maintain.GoodsManager;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.OrderDetail;
import com.easivend.common.SerializableMap;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_order_pay;
import com.easivend.model.Tb_vmc_system_parameter;
import com.easivend.view.COMService;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BusHuo extends Activity 
{
	private final int SPLASH_DISPLAY_LENGHT = 3000; // �ӳ�2��	
	//���ȶԻ���
	ProgressDialog dialog= null;
	private String proID = null;
	private String productID = null;
	private String proType = null;
	private String cabID = null;
	private String huoID = null;
    private float prosales = 0;
    private int count = 0;
    private int zhifutype = 0;//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
    private TextView txtbushuoname = null;
    private ImageView ivbushuoquhuo=null;
    private int tempx=0;
    private String draw=null,info=null;
    private int cabinetvar=0,huodaoNo=0,cabinetTypevar=0;
    private vmc_columnDAO columnDAO =null; 
    //�������
    private int status=0;//�������	
    //=================
    //COM�������
    //=================
  	LocalBroadcastManager comBroadreceiver;
  	COMReceiver comreceiver;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.bushuo);	
		//ɾ��ǰ���activity
		if(BusgoodsClass.BusgoodsClassAct!=null)
			BusgoodsClass.BusgoodsClassAct.finish(); 
		if(Busgoods.BusgoodsAct!=null)
			Busgoods.BusgoodsAct.finish(); 
		if(BusgoodsSelect.BusgoodsSelectAct!=null)
			BusgoodsSelect.BusgoodsSelectAct.finish(); 
		
		
		//4.ע�������
		comBroadreceiver = LocalBroadcastManager.getInstance(this);
		comreceiver=new COMReceiver();
		IntentFilter comfilter=new IntentFilter();
		comfilter.addAction("android.intent.action.comrec");
		comBroadreceiver.registerReceiver(comreceiver,comfilter);
		//ע�����������
  	    EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Object> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<bushuo�������","log.txt");
				Map<String, Object> Set= allSet;
				int jnirst=(Integer)Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_COLUMN_OPEN://�������
					case EVprotocolAPI.EV_BENTO_OPEN://���ӹ����
						status=(Integer)allSet.get("result");//�������
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+cabinetvar+"],hdid=["+huodaoNo+"],status=["+status+"]","log.txt");	
						dialog.dismiss();
						//1.���³������
						//�۳��������
						chuhuoupdate(cabinetvar,huodaoNo);
						//�����ɹ�
						if(status==1)
						{
							txtbushuoname.setText(proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ");
							txtbushuoname.setTextColor(android.graphics.Color.BLUE);
							chuhuoLog(1);//��¼��־
						}
						else
						{
							txtbushuoname.setText(proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ");
							txtbushuoname.setTextColor(android.graphics.Color.RED);
							chuhuoLog(0);//��¼��־
						}
												
						//3.�˻�����ҳ��
						ivbushuoquhuo.setVisibility(View.VISIBLE);
			 	    	new Handler().postDelayed(new Runnable() 
						{
	                        @Override
	                        public void run() 
	                        {	   
	                        	//�˳�ʱ������intent
                	            Intent intent=new Intent();
                	            intent.putExtra("status", status);//�������
	                        	if(zhifutype==0)//�ֽ�֧��
	                        	{                        			
                    	            setResult(BusZhiAmount.RESULT_CANCELED,intent);                    	            
                        		}
	                        	else if(zhifutype==3)//֧������ά��
	                        	{
                    	            setResult(BusZhier.RESULT_CANCELED,intent);                    	            
                        		}
	                        	else if(zhifutype==4)//΢��ɨ��
	                        	{                        			
                    	            setResult(BusZhiwei.RESULT_CANCELED,intent);                    	            
                        		}
	                        	else if(zhifutype==5)//�����
	                        	{                        			
                    	            setResult(BusZhitihuo.RESULT_CANCELED,intent);                    	            
                        		}
	                        	finish();
//	                        	//�������,�ѷ��ֽ�ģ��ȥ��
//	                        	if(status==0)
//	                        	{
//	                        		if(BusZhier.BusZhierAct!=null)
//	                        			BusZhier.BusZhierAct.finish(); 
//	                        		if(BusZhiwei.BusZhiweiAct!=null)
//	                        			BusZhiwei.BusZhiweiAct.finish(); 
//	                        		OrderDetail.addLog(BusHuo.this);
//	                        	}
//	                        	//����ʧ�ܣ��˵����ֽ�ģ������˱Ҳ���
//	                        	else
//	                        	{
//	                        		if(BusZhier.BusZhierAct!=null)
//	                        		{
//	                        			//�˳�ʱ������intent
//	                    	            Intent intent=new Intent();
//	                    	            setResult(BusZhier.RESULT_CANCELED,intent);
//	                        		}
//	                        		if(BusZhiwei.BusZhiweiAct!=null)
//	                        		{
//	                        			//�˳�ʱ������intent
//	                    	            Intent intent=new Intent();
//	                    	            setResult(BusZhiwei.RESULT_CANCELED,intent);
//	                        		}
//								}		                        	
	                            
	                        }

						}, SPLASH_DISPLAY_LENGHT);
						break;										
				}
			}
		}); 
  	    
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
//		Intent intent=getIntent();
//		Bundle bundle=intent.getExtras();
		proID=OrderDetail.getProID();
		productID=OrderDetail.getProductID();
		proType=OrderDetail.getProType();
		cabID=OrderDetail.getCabID();
		huoID=OrderDetail.getColumnID();
		prosales=OrderDetail.getShouldPay();//��Ʒ����
		count=OrderDetail.getShouldNo();//����
		zhifutype=OrderDetail.getPayType();
		txtbushuoname=(TextView)findViewById(R.id.txtbushuoname);
		
  	    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="
				+productID+" proType="
				+proType+" cabID="+cabID+" huoID="+huoID+" prosales="+prosales+" count="
				+count+" zhifutype="+zhifutype,"log.txt");		
  	    txtbushuoname.setText(proID+"["+prosales+"]"+"->�ȴ�����");
		this.ivbushuoquhuo =(ImageView) super.findViewById(R.id.ivbushuoquhuo);
		ivbushuoquhuo.setVisibility(View.GONE);
		
		//****
		//����
		//****
		chuhuoopt(tempx);
	}
			
	//����,����ֵ0ʧ��,1����ָ��ɹ����ȴ����ؽ��,2�������
	private void chuhuoopt(int huox)
	{
		int huorst=0;
		int rst=0;
		// ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_column����
 	    columnDAO = new vmc_columnDAO(this);
 	    txtbushuoname.setText(proID+"["+prosales+"]"+"->���ڳ���,���Ժ�...");
		//1.�������������
		//����Ʒid����
		if(proType.equals("1")==true)
		{
	 	    // ��ȡ����������Ϣ�����洢��Map������
			List<String> alllist = columnDAO.getproductColumn(productID);
			cabinetvar=Integer.parseInt(alllist.get(0));
			huodaoNo=Integer.parseInt(alllist.get(1));
			cabinetTypevar=Integer.parseInt(alllist.get(2));
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷcabID="+cabinetvar+"huoID="+huodaoNo+"cabType="+cabinetTypevar,"log.txt"); 
		}
		//������id����
		else if(proType.equals("2")==true)
		{
	 	    // ��ȡ����������Ϣ�����洢��Map������
			String alllist = columnDAO.getcolumnType(cabID);
			cabinetvar=Integer.parseInt(cabID);
			huodaoNo=Integer.parseInt(huoID);
			cabinetTypevar=Integer.parseInt(alllist);
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷcabID="+cabinetvar+"huoID="+huodaoNo+"cabType="+cabinetTypevar,"log.txt"); 
		}
		
		dialog= ProgressDialog.show(BusHuo.this,"���ڳ�����","���Ժ�...");
		new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {	  
        		ToolClass.Log(ToolClass.INFO,"EV_JNI",
        		    	"[APPsend>>]cabinet="+String.valueOf(cabinetvar)
        		    	+" column="+huodaoNo		    	
        		    	,"log.txt");
        		Intent intent = new Intent();
        		//4.����ָ��㲥��COMService
        		intent.putExtra("EVWhat", COMService.EV_CHUHUOCHILD);	
        		intent.putExtra("cabinet", cabinetvar);	
        		intent.putExtra("column", huodaoNo);	
        		intent.setAction("android.intent.action.comsend");//action���������ͬ
        		comBroadreceiver.sendBroadcast(intent);
            }

		}, SPLASH_DISPLAY_LENGHT);
	}
	//�޸Ĵ������
	private void chuhuoupdate(int cabinetvar,int huodaoNo)
	{
		String cab=null,huo=null;
		cab=String.valueOf(cabinetvar);
		//����id=1��9����Ϊ01��09
        if(huodaoNo<10)
        {
        	huo="0"+String.valueOf(huodaoNo);
        }
        else
        {
        	huo=String.valueOf(huodaoNo);
        }	
        //�۳��������
		columnDAO.update(cab,huo);		
	}
	
	//��¼��־�������type=1�����ɹ���0����ʧ��
	private void chuhuoLog(int type)
	{
		OrderDetail.setYujiHuo(1);
		OrderDetail.setCabID(String.valueOf(cabinetvar));
		OrderDetail.setColumnID(String.valueOf(huodaoNo));
		if(type==1)//�����ɹ�
		{
			OrderDetail.setPayStatus(0);
			OrderDetail.setRealHuo(1);
			OrderDetail.setHuoStatus(0);
		}
		else//����ʧ��
		{
			OrderDetail.setPayStatus(1);
			OrderDetail.setRealHuo(0);
			OrderDetail.setHuoStatus(1);
		}
	}
	
	//2.����COMReceiver�Ľ������㲥���������շ�����ͬ��������
	public class COMReceiver extends BroadcastReceiver 
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int EVWhat=bundle.getInt("EVWhat");
			switch(EVWhat)
			{
			//��������	
			case COMService.EV_OPTMAIN: 
				SerializableMap serializableMap2 = (SerializableMap) bundle.get("result");
				Map<String, Integer> Set2=serializableMap2.getMap();
				ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��������="+Set2,"com.txt");
				status=Set2.get("result");//�������
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+cabinetvar+"],hdid=["+huodaoNo+"],status=["+status+"]","log.txt");	
				dialog.dismiss();
				//1.���³������
				//�۳��������
				chuhuoupdate(cabinetvar,huodaoNo);
				//�����ɹ�
				if(status==1)
				{
					txtbushuoname.setText(proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ");
					txtbushuoname.setTextColor(android.graphics.Color.BLUE);
					chuhuoLog(1);//��¼��־
				}
				else
				{
					txtbushuoname.setText(proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ");
					txtbushuoname.setTextColor(android.graphics.Color.RED);
					chuhuoLog(0);//��¼��־
				}
										
				//3.�˻�����ҳ��
				ivbushuoquhuo.setVisibility(View.VISIBLE);
	 	    	new Handler().postDelayed(new Runnable() 
				{
                    @Override
                    public void run() 
                    {	   
                    	//�˳�ʱ������intent
        	            Intent intentrec=new Intent();
        	            intentrec.putExtra("status", status);//�������
//                    	if(zhifutype==0)//�ֽ�֧��
//                    	{                        			
//            	            setResult(BusZhiAmount.RESULT_CANCELED,intentrec);                    	            
//                		}
//                    	else if(zhifutype==3)//֧������ά��
//                    	{
//            	            setResult(BusZhier.RESULT_CANCELED,intentrec);                    	            
//                		}
//                    	else if(zhifutype==4)//΢��ɨ��
//                    	{                        			
//            	            setResult(BusZhiwei.RESULT_CANCELED,intentrec);                    	            
//                		}
//                    	else if(zhifutype==5)//�����
//                    	{                        			
//            	            setResult(BusZhitihuo.RESULT_CANCELED,intentrec);                    	            
//                		}
                    	finish();	
                    }

				}, SPLASH_DISPLAY_LENGHT);
				break;
			}			
		}

	}
	
	@Override
	protected void onDestroy() {
		//=============
		//COM�������
		//=============
		//5.���ע�������
		comBroadreceiver.unregisterReceiver(comreceiver);	
		super.onDestroy();		
	}
}
