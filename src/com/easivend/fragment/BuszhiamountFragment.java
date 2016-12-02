package com.easivend.fragment;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.easivend.app.business.BusPort;
import com.easivend.app.business.BusPort.BusPortFragInteraction;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.model.Tb_vmc_system_parameter;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BuszhiamountFragment extends Fragment 
{
	TextView txtbuszhiamountcount=null,txtbuszhiamountAmount=null,txtbuszhiamountbillAmount=null,txtbuszhiamounttime=null,
			txtbuszhiamounttsxx=null;
	ImageButton imgbtnbuszhiamountqxzf=null,imgbtnbuszhiamountqtzf=null;
	ImageView imgbtnbusgoodsback=null;
	float amount=0;//��Ʒ��Ҫ֧�����	
	ImageView ivbuszhiselamount=null,ivbuszhiselzhier=null,ivbuszhiselweixing=null,ivbuszhiselpos=null;
	ImageView ivbuszhier=null,ivbuszhiwei=null;
	//ɨ��
	private String editstr="";
	private int editread=0;
	EditText editTextTimeCOMA;
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
//	private String proID = null;
//	private String productID = null;
//	private String proType = null;
//	private String cabID = null;
//	private String huoID = null;
//    private String prosales = null;
//    private String count = null;
//    private String reamin_amount = null;    
//    private String id="";
    private String out_trade_no=null;    
	private Context context;
	//=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BuszhiamountFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BuszhiamountFragInteraction)
        {
            listterner = (BuszhiamountFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BuszhiamountFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BuszhiamountFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        //void BusgoodsselectSwitch(int buslevel);//�л���BusZhixxҳ��
        void BuszhiamountFinish();      //�л���businessҳ��
    }
    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_buszhiamount, container, false);  
		context=this.getActivity();//��ȡactivity��context
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();		
		txtbuszhiamountcount= (TextView) view.findViewById(R.id.txtbuszhiamountcount);
		txtbuszhiamountcount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiamountAmount= (TextView) view.findViewById(R.id.txtbuszhiamountAmount);
		txtbuszhiamountAmount.setText(String.valueOf(amount));
		txtbuszhiamountbillAmount= (TextView) view.findViewById(R.id.txtbuszhiamountbillAmount);		
		txtbuszhiamounttime = (TextView) view.findViewById(R.id.txtbuszhiamounttime);
		txtbuszhiamounttsxx = (TextView) view.findViewById(R.id.txtbuszhiamounttsxx);
		//ɨ��ģ��
		editTextTimeCOMA=(EditText)view.findViewById(R.id.editTextTimeCOMA);
        editTextTimeCOMA.setInputType(InputType.TYPE_NULL);  
        editTextTimeCOMA.setFocusable(true);
		editTextTimeCOMA.setFocusableInTouchMode(true);
		editTextTimeCOMA.requestFocus();
		editTextTimeCOMA.setText("");
        editTextTimeCOMA.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				editstr=s.toString().trim();	
				//Log.i("EV_JNI","String s="+editstr);
				editread=100;
			}
		});
        timer.scheduleWithFixedDelay(task, 100, 10, TimeUnit.MILLISECONDS);  
		imgbtnbuszhiamountqxzf = (ImageButton) view.findViewById(R.id.imgbtnbuszhiamountqxzf);
		imgbtnbuszhiamountqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	listterner.BuszhiamountFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		this.imgbtnbusgoodsback=(ImageView)view.findViewById(R.id.imgbtnbusgoodsback);
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	listterner.BuszhiamountFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		/**
	     * ����������fragment������,
	     * �����塢��Fragment�����ص�activity��ʱ��ע��ص���Ϣ
	     * @param activity
	     */
		BusPort.setCallBack(new buportInterfaceImp());
		ivbuszhiselamount = (ImageView) view.findViewById(R.id.ivbuszhiselamount);
		ivbuszhiselzhier = (ImageView) view.findViewById(R.id.ivbuszhiselzhier);
		ivbuszhiselweixing = (ImageView) view.findViewById(R.id.ivbuszhiselweixing);	
		ivbuszhiselpos = (ImageView) view.findViewById(R.id.ivbuszhiselpos);
		ivbuszhier = (ImageView) view.findViewById(R.id.ivbuszhier);
		ivbuszhiwei = (ImageView) view.findViewById(R.id.ivbuszhiwei);
		//*********************
		//�������Եõ���֧����ʽ
		//*********************
		vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		if(tb_inaccount.getAmount()==0)
    		{
    			ivbuszhiselamount.setVisibility(View.GONE);//�ر�
    		}
    		else
    		{
    			ivbuszhiselamount.setVisibility(View.VISIBLE);//��
    		}	
    		if(tb_inaccount.getZhifubaoer()==0)
    		{
    			ivbuszhiselzhier.setVisibility(View.GONE);//�ر�
    			ivbuszhier.setVisibility(View.GONE);//�ر�
    		}
    		else
    		{
    			ivbuszhiselzhier.setVisibility(View.VISIBLE);//��
    			ivbuszhier.setVisibility(View.VISIBLE);//��
    		}
    		if(tb_inaccount.getWeixing()==0)
    		{
    			ivbuszhiselweixing.setVisibility(View.GONE);//�ر�
    			ivbuszhiwei.setVisibility(View.GONE);//�ر�
    		}
    		else
    		{
    			ivbuszhiselweixing.setVisibility(View.VISIBLE);//��
    			ivbuszhiwei.setVisibility(View.VISIBLE);//��
    		}
    		if(tb_inaccount.getZhifubaofaca()==0)
    		{
    			ivbuszhiselpos.setVisibility(View.GONE);//�ر�
    		}
    		else
    		{
    			ivbuszhiselpos.setVisibility(View.VISIBLE);//��
    		}    		
    	}
		return view;
	}
	//���õ���ʱ��ʱ��
    TimerTask task = new TimerTask() { 
    	@Override 
        public void run() { 
  
    		((Activity)context).runOnUiThread(new Runnable() {      // UI thread 
		         @Override 
		        public void run()
		        { 
		        	 if(editread>0)
		        		 editread--;
		        	 if(editread==0)
		        	 {
		        		 if(editstr.equals("")==false)
		        		 {
		        			 int length=editstr.length();
		        			 if(length<18)
		        			 {
		        				 Log.i("EV_JNI","String edit=readerr!!!");
		        				 editstr="";
			        			 editTextTimeCOMA.setText("");
			        			 editTextTimeCOMA.setFocusable(true);
			     				 editTextTimeCOMA.setFocusableInTouchMode(true);
			     				 editTextTimeCOMA.requestFocus();
		        			 }
		        			 else
		        			 {
		        				 Log.i("EV_JNI","String edit="+editstr);
		        				 editstr="";
			        			 editTextTimeCOMA.setText("");
			        			 editTextTimeCOMA.setFocusable(true);
			     				 editTextTimeCOMA.setFocusableInTouchMode(true);
			     				 editTextTimeCOMA.requestFocus();
		        			 }	        			 
		        			 
		        		 }
		        	 }
		        } 
            });
        }     	    
    };
	private class buportInterfaceImp implements BusPortFragInteraction//���ؽӿ�
	{
		/**
	     * ����������fragment������,
	     * ��������ʵ��BusPortFragInteraction�ӿ�
	     * @param activity
	     */
		@Override
		public void BusportTsxx(String str) {
			// TODO Auto-generated method stub
			txtbuszhiamounttsxx.setText(str);
		}

		@Override
		public void BusportTbje(String str) {
			// TODO Auto-generated method stub
			txtbuszhiamountbillAmount.setText(str);
		}

		@Override
		public void BusportChjg(int sta) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void BusportSend(String str) {
			// TODO Auto-generated method stub
			ivbuszhier.setImageBitmap(ToolClass.createQRImage(str));
		}

		@Override
		public void BusportSendWei(String str) {
			// TODO Auto-generated method stub
			ivbuszhiwei.setImageBitmap(ToolClass.createQRImage(str));
		}

		
	}
		
}


