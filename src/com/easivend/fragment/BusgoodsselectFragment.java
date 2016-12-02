package com.easivend.fragment;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.easivend.app.business.BusPort;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_product;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class BusgoodsselectFragment extends Fragment 
{
	ImageView ivbusgoodselProduct=null,imgbtnbusgoodsback=null;
	ImageView ivbuszhiselamount=null;
	TextView txtbusgoodselName=null,txtbusgoodselAmount=null;
	WebView webproductDesc;
	Button btnreturn=null;
	private String proID = null;
	private String productID = null;
	private String proImage = null;	
    private String prosales = null;
    private String procount = null;
    private String proType=null;
    private String cabID = null;
	private String huoID = null;
	private Context context;
	//ɨ��
    private String editstr="";
	private int editread=0;
	EditText editTextTimeCOMA;
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
	//=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BusgoodsselectFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BusgoodsselectFragInteraction)
        {
            listterner = (BusgoodsselectFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BusgoodsselectFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BusgoodsselectFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        void BusgoodsselectSwitch(int buslevel);//�л���BusZhixxҳ��
        void BusgoodsSwitch();//�л���busgoodsҳ��
        void BusgoodsselectFinish();      //�л���businessҳ��
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
		View view = inflater.inflate(R.layout.fragment_busgoodsselect, container, false);  
		//������������activity���������
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
		Bundle bundle = getArguments();//��ô�activity�д��ݹ�����ֵ
		proID=bundle.getString("proID");
		productID=bundle.getString("productID");
		proImage=bundle.getString("proImage");
		prosales=bundle.getString("prosales");
		procount=bundle.getString("procount");
		proType=bundle.getString("proType");
		cabID=bundle.getString("cabID");
		huoID=bundle.getString("huoID");
		context=this.getActivity();//��ȡactivity��context
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
		
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="+productID+" proImage="
					+proImage+" prosales="+prosales+" procount="
					+procount+" proType="+proType+" cabID="+cabID+" huoID="+huoID,"log.txt");
		ivbusgoodselProduct = (ImageView) view.findViewById(R.id.ivbusgoodselProduct);
		/*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
        Bitmap bitmap = ToolClass.getLoacalBitmap(proImage); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
        ivbusgoodselProduct.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
		txtbusgoodselName = (TextView) view.findViewById(R.id.txtbusgoodselName);
		txtbusgoodselName.setText(proID);
		txtbusgoodselAmount = (TextView) view.findViewById(R.id.txtbusgoodselAmount);
		if(Integer.parseInt(procount)>0)
		{
			txtbusgoodselAmount.setText(prosales);
		}
		else
		{
			txtbusgoodselAmount.setText("������");
		}	
		//�õ���Ʒ����
		webproductDesc = (WebView) view.findViewById(R.id.webproductDesc); 
		vmc_productDAO productDAO = new vmc_productDAO(context);// ����InaccountDAO����
	    Tb_vmc_product tb_vmc_product = productDAO.find(productID);
	    if(ToolClass.isEmptynull(tb_vmc_product.getProductDesc())!=true)
	    {
	    	//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷDesc="+tb_vmc_product.getProductDesc().toString(),"log.txt");
		    WebSettings settings = webproductDesc.getSettings();
		    settings.setSupportZoom(true);
		    settings.setTextSize(WebSettings.TextSize.LARGEST);
		    webproductDesc.getSettings().setSupportMultipleWindows(true);
		    webproductDesc.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //���ù�������ʽ
		    webproductDesc.getSettings().setDefaultTextEncodingName("UTF -8");//����Ĭ��Ϊutf-8
		    webproductDesc.loadDataWithBaseURL(null,tb_vmc_product.getProductDesc().toString(), "text/html; charset=UTF-8","utf-8", null);//����д��������ȷ���Ľ���
	    }
	    else
	    {
	    	webproductDesc.setVisibility(View.GONE);
	    }
		ivbuszhiselamount = (ImageView) view.findViewById(R.id.ivbuszhiselamount);
		ivbuszhiselamount.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(Integer.parseInt(procount)>0)
		    	{
			    	sendzhifu();
//			    	Intent intent = null;// ����Intent����                
//	            	intent = new Intent(context, BusZhiAmount.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//	            	startActivity(intent);// ��Accountflag
			    	listterner.BusgoodsselectSwitch(BusPort.BUSZHIAMOUNT);
		    	}
		    }
		});						
    	imgbtnbusgoodsback=(ImageView)view.findViewById(R.id.imgbtnbusgoodsback);
    	imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	listterner.BusgoodsselectFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
    	btnreturn=(Button)view.findViewById(R.id.btnreturn);
	    btnreturn.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	listterner.BusgoodsSwitch();//�������fragment��activity���ͻص���Ϣ
		    }
		});	
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
		        			 editstr="";
		        			 editTextTimeCOMA.setText("");
		        			 editTextTimeCOMA.setFocusable(true);
		     				 editTextTimeCOMA.setFocusableInTouchMode(true);
		     				 editTextTimeCOMA.requestFocus();  
		        		 }
		        	 }
		        } 
            });
        }     	    
    };
	private void sendzhifu()
	{
		OrderDetail.setProID(proID);
    	OrderDetail.setProductID(productID);
    	OrderDetail.setProType(proType);
    	OrderDetail.setShouldPay(Float.parseFloat(prosales));
    	OrderDetail.setShouldNo(1);
    	OrderDetail.setCabID(cabID);
    	OrderDetail.setColumnID(huoID);
	}
}
