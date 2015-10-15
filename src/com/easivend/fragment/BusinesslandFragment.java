package com.easivend.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.easivend.app.business.Busgoods;
import com.easivend.app.business.BusgoodsClass;
import com.easivend.app.business.BusgoodsSelect;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.app.maintain.ParamManager;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.fragment.MoviewlandFragment.MovieFragInteraction;
import com.easivend.model.Tb_vmc_product;
import com.easivend.model.Tb_vmc_system_parameter;
import com.easivend.view.DogService;
import com.example.evconsole.R;

import android.R.bool;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class BusinesslandFragment extends Fragment 
{	
	final static int REQUEST_CODE=1; 	
	EditText txtadsTip=null;
	ImageButton btnads1=null,btnads2=null,btnads3=null,btnads4=null,btnads5=null,btnads6=null,
			   btnads7=null,btnads8=null,btnads9=null,btnadscancel=null,btnadsenter=null;
	ImageButton btnadsclass=null,btnadscuxiao=null,btnadsbuysale=null,btnadsquhuo=null,btnads0=null;	
	Intent intent=null;
	private static int count=0;
	private static String huo="";
	//��ʱ��������������Ĺ���
	Timer timer = new Timer(true);
//    private final int SPLASH_DISPLAY_LENGHT = 10; //  5*60�ӳ�5����	
//    private int recLen = SPLASH_DISPLAY_LENGHT; 
	//���ͳ���ָ��
    private String proID = null;
	private String productID = null;
	private String proImage = null;
	private String cabID = null;
	private String huoID = null;
    private String prosales = null; 
    private Context context;
    //�����
    private static int pwdcount=0;
    
    //=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BusFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof MovieFragInteraction)
        {
            listterner = (BusFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BusFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BusFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        void finishBusiness();//�ر�activityҳ��
        void gotoBusiness(int buslevel,Map<String, String>str);  //��ת����Ʒҳ��     
        void stoptimer();//�رն�ʱ��
        void restarttimer();//���´򿪶�ʱ��
    }
    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_businessland, container, false);  
		context=this.getActivity();//��ȡactivity��context	
		//��ʱ�����ع��ҳ��
		timer.schedule(new TimerTask() { 
	        @Override 
	        public void run() { 	        	  
        		  if(pwdcount > 0)
	              { 
        			  pwdcount--;
	              }		        	  
	        } 
	    }, 1000, 5000);       // timeTask  
		//=======
		//����ģ��
		//=======
		txtadsTip = (EditText) view.findViewById(R.id.txtadsTip);
		txtadsTip.setFocusable(false);//���ø�edittext��ý���
		txtadsTip.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// �ر�����̣������������edittext��ʱ�򣬲��ᵯ��ϵͳ�Դ������뷨
				txtadsTip.setInputType(InputType.TYPE_NULL);
				return false;
			}
		});
		btnads1 = (ImageButton) view.findViewById(R.id.btnads1);		
		btnads1.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("1",1);
		    }
		});
		btnads2 = (ImageButton) view.findViewById(R.id.btnads2);
		btnads2.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("2",1);
		    }
		});
		btnads3 = (ImageButton) view.findViewById(R.id.btnads3);
		btnads3.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("3",1);
		    }
		});
		btnads4 = (ImageButton) view.findViewById(R.id.btnads4);
		btnads4.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("4",1);
		    }
		});
		btnads5 = (ImageButton) view.findViewById(R.id.btnads5);
		btnads5.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("5",1);
		    }
		});
		btnads6 = (ImageButton) view.findViewById(R.id.btnads6);
		btnads6.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("6",1);
		    }
		});
		btnads7 = (ImageButton) view.findViewById(R.id.btnads7);
		btnads7.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("7",1);
		    }
		});
		btnads8 = (ImageButton) view.findViewById(R.id.btnads8);
		btnads8.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("8",1);
		    }
		});
		btnads9 = (ImageButton) view.findViewById(R.id.btnads9);
		btnads9.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("9",1);
		    }
		});
		btnads0 = (ImageButton) view.findViewById(R.id.btnads0);
		btnads0.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("0",1);
		    }
		});
		btnadscancel = (ImageButton) view.findViewById(R.id.btnadscancel);
		btnadscancel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("0",0);
		    }
		});
		btnadsenter = (ImageButton) view.findViewById(R.id.btnadsenter);
		btnadsenter.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	passwd();
		    }
		});
		btnadsclass = (ImageButton) view.findViewById(R.id.btnadsclass);
		btnadsclass.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	vmc_classDAO classdao = new vmc_classDAO(context);// ����InaccountDAO����
		    	long count=classdao.getCount();
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒ��������="+count,"log.txt");
		    	if(count>0)
		    	{
			    	//intent = new Intent(context, BusgoodsClass.class);// ʹ��Accountflag���ڳ�ʼ��Intent
			    	//startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
		    		listterner.gotoBusiness(1,null);
		    	}
		    	else
		    	{
//		    		intent = new Intent(context, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//                	intent.putExtra("proclassID", "");
//                	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag		    		
                	listterner.gotoBusiness(2,null);
		    	}
		    	
		    }
		});
		return view;  
	}
	//num�������,type=1�������֣�type=0��������
    private void chuhuo(String num,int type)
    {    	
		if(type==1)
		{
			if(count<3)
	    	{
	    		count++;
	    		huo=huo+num;
	    		txtadsTip.setText(huo);
	    	}
		}
		else if(type==0)
		{
			if(count>0)
			{
				count--;
				huo=huo.substring(0,huo.length()-1);
				if(count==0)
					txtadsTip.setText("");
				else
					txtadsTip.setText(huo);
			}
		}  
		if(count==3)
		{
			cabID=huo.substring(0,1);
		    huoID=huo.substring(1,huo.length());
		    vmc_columnDAO columnDAO = new vmc_columnDAO(context);// ����InaccountDAO����		    
		    Tb_vmc_product tb_inaccount = columnDAO.getColumnproduct(cabID,huoID);
		    if(tb_inaccount!=null)
		    {
			    productID=tb_inaccount.getProductID().toString();
			    prosales=String.valueOf(tb_inaccount.getSalesPrice());
			    proImage=tb_inaccount.getAttBatch1();
			    proID=productID+"-"+tb_inaccount.getProductName().toString();
			    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="
						+productID+" proType="
						+"2"+" cabID="+cabID+" huoID="+huoID+" prosales="+prosales+" count="
						+"1","log.txt");
			    count=0;
			    huo="";
			    txtadsTip.setText("");
//				Intent intent = null;// ����Intent����                
//	        	intent = new Intent(context, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//	        	intent.putExtra("proID", proID);
//	        	intent.putExtra("productID", productID);
//	        	intent.putExtra("proImage", proImage);
//	        	intent.putExtra("prosales", prosales);
//	        	intent.putExtra("procount", "1");
//	        	intent.putExtra("proType", "2");//1����ͨ����ƷID����,2����ͨ����������
//	        	intent.putExtra("cabID", cabID);//�������,proType=1ʱ��Ч
//	        	intent.putExtra("huoID", huoID);//����������,proType=1ʱ��Ч
//
//
////	        	OrderDetail.setProID(proID);
////            	OrderDetail.setProductID(productID);
////            	OrderDetail.setProType("2");
////            	OrderDetail.setCabID(cabID);
////            	OrderDetail.setColumnID(huoID);
////            	OrderDetail.setShouldPay(Float.parseFloat(prosales));
////            	OrderDetail.setShouldNo(1);
//	        	
//	        	startActivityForResult(intent,REQUEST_CODE);// ��Accountflag
	        	Map<String, String>str=new HashMap<String, String>();
	        	str.put("proID", proID);
	        	str.put("productID", productID);
	        	str.put("proImage", proImage);
	        	str.put("prosales", prosales);
	        	str.put("procount", "1");
	        	str.put("proType", "2");//1����ͨ����ƷID����,2����ͨ����������
	        	str.put("cabID", cabID);//�������,proType=1ʱ��Ч
	        	str.put("huoID", huoID);//����������,proType=1ʱ��Ч
	        	listterner.gotoBusiness(3,str);
		    }
		    else
		    {
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="
						+productID+" proType="
						+"2"+" cabID="+cabID+" huoID="+huoID+" prosales="+prosales+" count="
						+"1","log.txt");
			    count=0;
			    huo="";
			    txtadsTip.setText("");
			}
		    
		}
    } 
    
    //���������
    private void passwd()
    {
    	pwdcount++;
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<pwdcount="+pwdcount,"log.txt");
    	if(pwdcount>=30)
    	{
    		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<pwdinfo","log.txt");
    		pwdcount=0;
    		passdialog();
    	}
    }
    
    //�����
    private void passdialog()
    {
    	View myview=null;  
    	listterner.stoptimer();//�رն�ʱ��
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(context);
		myview=factory.inflate(R.layout.selectinteger, null);
		final EditText dialoginte=(EditText) myview.findViewById(R.id.dialoginte);
		
		Dialog dialog = new AlertDialog.Builder(context)
		.setTitle("����")
		.setPositiveButton("����", new DialogInterface.OnClickListener() 	
		{
				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				boolean istrue=false;
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ֵ="+dialoginte.getText().toString(),"log.txt");
				//����ά��ҳ������
				vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
			    // ��ȡ����������Ϣ�����洢��List���ͼ�����
		    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
		    	if(tb_inaccount!=null)
		    	{
		    		String Pwd=tb_inaccount.getMainPwd().toString();
		    		if(Pwd==null)
		    		{
		    			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ֵ=null","log.txt");
		    			istrue=passcmp(null,dialoginte.getText().toString());
		    		}
		    		else
		    		{
		    			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ֵ="+Pwd,"log.txt");
		    			istrue=passcmp(Pwd,dialoginte.getText().toString());
		    		}
		    	}
		    	else
		    	{
		    		istrue=passcmp(null,dialoginte.getText().toString());
				}
		    	
		    	if(istrue)
		    	{
		    		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<ȷ���˳�","log.txt");
		    		//�������fragment��activity���ͻص���Ϣ
		        	listterner.finishBusiness();
		    	}
		    	else
		    	{
		    		listterner.restarttimer();//���´򿪶�ʱ��
				}
			}
		})
		.setNegativeButton("ȡ��",  new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
    	{			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub	
				listterner.restarttimer();//���´򿪶�ʱ��
			}
    	})
		.setView(myview)//���ｫ�Ի��򲼾��ļ����뵽�Ի�����
		.create();
		dialog.show();
    }
    
    //�����ȶ�
    private boolean passcmp(String pwd,String value)
    {
    	boolean istrue=false;
    	if((pwd==null)||(pwd.equals("")==true))
    	{
    		if(value.equals("83718557"))
    		{
    			istrue=true;
    		}
    	}
    	else
    	{
    		if(value.equals(pwd))
    		{
    			istrue=true;
    		}
    	}
    	return istrue;
    }
}
