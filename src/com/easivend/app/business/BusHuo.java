package com.easivend.app.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.easivend.app.maintain.GoodsManager;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.easivend.http.EVServerhttp;
import com.easivend.model.Tb_vmc_order_pay;
import com.easivend.model.Tb_vmc_system_parameter;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
    private String data[][]=null;
    private ListView lvbushuo = null;
    //������ʾ�����ݰ�װ
    private List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
    private SimpleAdapter simpleada = null;//�������ݵ�ת������
    private ImageView ivbushuoquhuo=null;
    private int tempx=0;
    private String draw=null,info=null;
    private int cabinetvar=0,huodaoNo=0,cabinetTypevar=0;
    private vmc_columnDAO columnDAO =null; 
    //�������
    private int status=0;//�������	
	
    
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
							data[tempx][0]=String.valueOf(R.drawable.yes);
							data[tempx][1]=proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ";
							chuhuoLog(1);//��¼��־
						}
						else
						{
							data[tempx][0]=String.valueOf(R.drawable.no);
							data[tempx][1]=proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ";
							chuhuoLog(0);//��¼��־
						}
						updateListview();
						
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
					case EVprotocolAPI.EV_TRADE_RPT://�������߳���Ϣ
//						device=allSet.get("device");//�������
//						status=allSet.get("status");//�������
//						hdid=allSet.get("hdid");//����id
//						hdtype=allSet.get("type");//��������
//						cost=ToolClass.MoneyRec(allSet.get("cost"));//��Ǯ
//						totalvalue=ToolClass.MoneyRec(allSet.get("totalvalue"));//ʣ����
//						huodao=allSet.get("huodao");//ʣ��������
//						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
//								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");	
//						if(status==0)
//						{
//							data[tempx][0]=String.valueOf(R.drawable.yes);
//							data[tempx][1]=proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ";
//							//�۳��������
//							chuhuoupdate(cabinetvar,huodaoNo);
//							chuhuoLog(0);//��¼��־
//						}
//						else
//						{
//							data[tempx][0]=String.valueOf(R.drawable.no);
//							data[tempx][1]=proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ";
//							//�۳��������
//							chuhuoupdate(cabinetvar,huodaoNo);
//							chuhuoLog(1);//��¼��־
//						}
//						updateListview();
//						tempx++;
//						huorst=0;
//						while((huorst!=1)&&(tempx<count))
//				 	    {
//				 	    	huorst=chuhuoopt(tempx);
//				 	    	if(huorst==2)
//							{
//								data[tempx][0]=String.valueOf(R.drawable.yes);
//								data[tempx][1]=proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ";
//								updateListview();
//								tempx++;
//								//�۳��������
//								chuhuoupdate(cabinetvar,huodaoNo);
//								chuhuoLog(0);//��¼��־
//							}
//							else if(huorst==0)
//							{
//								data[tempx][0]=String.valueOf(R.drawable.no);
//								data[tempx][1]=proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ";
//								updateListview();
//								tempx++;
//								//�۳��������
//								chuhuoupdate(cabinetvar,huodaoNo);
//								chuhuoLog(1);//��¼��־
//							}
//				 	    }
//						if(tempx>=count)
//				 	    {
//							ivbushuoquhuo.setVisibility(View.VISIBLE);
//				 	    	new Handler().postDelayed(new Runnable() 
//							{
//		                        @Override
//		                        public void run() 
//		                        {
//		                        	//�������,�ѷ��ֽ�ģ��ȥ��
//		                        	if(status==0)
//		                        	{
//		                        		if(BusZhier.BusZhierAct!=null)
//		                        			BusZhier.BusZhierAct.finish(); 
//		                        		if(BusZhiwei.BusZhiweiAct!=null)
//		                        			BusZhiwei.BusZhiweiAct.finish(); 
//		                        		OrderDetail.addLog(BusHuo.this);
//		                        	}
//		                        	//����ʧ�ܣ��˵����ֽ�ģ������˱Ҳ���
//		                        	else
//		                        	{
//		                        		if(BusZhier.BusZhierAct!=null)
//		                        		{
//		                        			//�˳�ʱ������intent
//		                    	            Intent intent=new Intent();
//		                    	            setResult(BusZhier.RESULT_CANCELED,intent);
//		                        		}
//		                        		if(BusZhiwei.BusZhiweiAct!=null)
//		                        		{
//		                        			//�˳�ʱ������intent
//		                    	            Intent intent=new Intent();
//		                    	            setResult(BusZhiwei.RESULT_CANCELED,intent);
//		                        		}
//									}		                        	
//		                            finish();
//		                        }
//
//							}, SPLASH_DISPLAY_LENGHT);
//				 	    }
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
		
		
  	    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="
				+productID+" proType="
				+proType+" cabID="+cabID+" huoID="+huoID+" prosales="+prosales+" count="
				+count+" zhifutype="+zhifutype,"log.txt");		
		this.data=new String[count][2];
		draw=String.valueOf(R.drawable.shuaxin);
		info=proID+"["+prosales+"]"+"->�ȴ�����";
		for(int x=0;x<count;x++)
		{
			data[x][0]=draw;
			data[x][1]=info;
		}
		this.lvbushuo =(ListView) super.findViewById(R.id.lvbushuo);		
		updateListview();
		this.ivbushuoquhuo =(ImageView) super.findViewById(R.id.ivbushuoquhuo);
		ivbushuoquhuo.setVisibility(View.GONE);
		
		//****
		//����
		//****
		chuhuoopt(tempx);
	}
	
	//���س�����Ϣ���б���
	private void updateListview()
	{
		int x=0;
		this.listMap.clear();
		for(x=0;x<count;x++)
		{
		  	Map<String,String> map = new HashMap<String,String>();//����Map���ϣ�����ÿһ������
		   	map.put("ivbushuostatus", data[x][0]);//��Ʒ����
	    	map.put("txtbushuoname", data[x][1]);//��Ʒ״̬
	    	this.listMap.add(map);//����������
		}
		//��������ܼ��ص�data_list��
		this.simpleada = new SimpleAdapter(this,this.listMap,R.layout.bushuolist,
		    		new String[]{"ivbushuostatus","txtbushuoname"},//Map�е�key����
		    		new int[]{R.id.ivbushuostatus,R.id.txtbushuoname});
		this.lvbushuo.setAdapter(this.simpleada);
	}
	
	//����,����ֵ0ʧ��,1����ָ��ɹ����ȴ����ؽ��,2�������
	private void chuhuoopt(int huox)
	{
		int huorst=0;
		int rst=0;
		// ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_column����
 	    columnDAO = new vmc_columnDAO(this);
		data[huox][1]=proID+"["+prosales+"]"+"->���ڳ���,���Ժ�...";
		updateListview();
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
            	//���ӹ�
        		if(cabinetTypevar==5)
        		{
        			EVprotocolAPI.EV_bentoOpen(ToolClass.getBentcom_id(),cabinetvar,huodaoNo);							
        			
        		}
        		//��ͨ��
        		else 
        		{
//        			rst=EVprotocolAPI.trade(cabinetvar,huodaoNo,typevar,
//        		    			ToolClass.MoneySend(sales));
//        			if(rst==0)//��������ʧ��
//        			{
//        				huorst=0;
//        			}
//        			else if(rst==1)//�������ͳɹ�
//        			{
//        				huorst=1;
//        			}
        		}
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
}
