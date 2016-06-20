package com.easivend.fragment;

import com.easivend.app.business.BusPort;
import com.easivend.app.business.BusgoodsSelect;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_productDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.model.Tb_vmc_product;
import com.easivend.model.Tb_vmc_system_parameter;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BusgoodsselectFragment extends Fragment 
{
	ImageView ivbusgoodselProduct=null,imgbtnbusgoodsback=null;
	ImageView ivbuszhiselamount=null,ivbuszhiselzhier=null,ivbuszhiselweixing=null;
	TextView txtbusgoodselName=null,txtbusgoodselAmount=null;
	WebView webproductDesc;
	private String proID = null;
	private String productID = null;
	private String proImage = null;	
    private String prosales = null;
    private String procount = null;
    private String proType=null;
    private String cabID = null;
	private String huoID = null;
	private Context context;
	
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
	    if(tb_vmc_product.getProductDesc().isEmpty()!=true)
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
		ivbuszhiselzhier = (ImageView) view.findViewById(R.id.ivbuszhiselzhier);
		ivbuszhiselzhier.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(Integer.parseInt(procount)>0)
		    	{
			    	sendzhifu();
//			    	Intent intent = null;// ����Intent����                
//	            	intent = new Intent(context, BusZhier.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//	            	startActivity(intent);// ��Accountflag
			    	listterner.BusgoodsselectSwitch(BusPort.BUSZHIER);
		    	}
		    }
		});
		ivbuszhiselweixing = (ImageView) view.findViewById(R.id.ivbuszhiselweixing);	
		ivbuszhiselweixing.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(Integer.parseInt(procount)>0)
		    	{
			    	sendzhifu();
//			    	Intent intent = null;// ����Intent����                
//	            	intent = new Intent(context, BusZhiwei.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//	            	startActivity(intent);// ��Accountflag
			    	listterner.BusgoodsselectSwitch(BusPort.BUSZHIWEI);
		    	}
		    }
		});
		//*********************
		//�������Եõ���֧����ʽ
		//*********************
		vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		int zhifucount=0;
    		if(tb_inaccount.getAmount()!=1)
    		{
    			ivbuszhiselamount.setVisibility(View.GONE);//�ر�
    		}
    		else
    		{
    			ivbuszhiselamount.setVisibility(View.VISIBLE);//��
    			zhifucount++;
    		}	
    		if(tb_inaccount.getZhifubaoer()!=1)
    		{
    			ivbuszhiselzhier.setVisibility(View.GONE);//�ر�
    		}
    		else
    		{
    			ivbuszhiselzhier.setVisibility(View.VISIBLE);//��
    			zhifucount++;
    		}
    		if(tb_inaccount.getWeixing()!=1)
    		{
    			ivbuszhiselweixing.setVisibility(View.GONE);//�ر�
    		}
    		else
    		{
    			ivbuszhiselweixing.setVisibility(View.VISIBLE);//��
    			zhifucount++;
    		}
    		switch(zhifucount)
    		{
    			case 3:
    				ivbuszhiselamount.setImageResource(R.drawable.amountnormal);
    				ivbuszhiselzhier.setImageResource(R.drawable.zhiernormal);
    				ivbuszhiselweixing.setImageResource(R.drawable.weixingnormal);
    				break;
    			case 2:
    			case 1:	
    				ivbuszhiselamount.setImageResource(R.drawable.amountlarge);
    				ivbuszhiselzhier.setImageResource(R.drawable.zhierlarge);
    				ivbuszhiselweixing.setImageResource(R.drawable.weixinglarge);
    				break;	
    		}
    	}		
    	imgbtnbusgoodsback=(ImageView)view.findViewById(R.id.imgbtnbusgoodsback);
    	imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	listterner.BusgoodsselectFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		return view;
	}
	
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
