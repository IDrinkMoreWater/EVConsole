package com.easivend.app.maintain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_OrderAdapter;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.model.Tb_vmc_order_pay;
import com.example.evconsole.R;

import android.R.integer;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class Order extends TabActivity
{
	private TabHost mytabhost = null;
	private int[] layres=new int[]{R.id.tab_grid,R.id.tab_picture};//��Ƕ�����ļ���id
	private TextView txtpayTypeValue=null,txtpayStatusValue=null,txtRealStatusValue=null,txtsmallNoteValue=null,txtsmallConiValue=null,
			txtsmallAmountValue=null,txtsmallCardValue=null,txtrealNoteValue=null,txtrealCoinValue=null,txtrealAmountValue=null,
			txtdebtAmountValue=null,txtrealCardValue=null,txtpayTimeValue=null,txtproductNameValue=null,txtsalesPriceValue=null,
			txtcabIDValue=null,txtcolumnIDValue=null;
	private EditText edtordergridstart=null,edtordergridend=null;
	private Button btnordergridquery=null,btnordergriddel=null,btnordergridexit=null;	
	private ListView lvorder=null;
	private SimpleDateFormat df;
	private String date=null;
	private int datetype=0;//1��ʼʱ��,2����ʱ��
	private static final int DATE_DIALOG_IDSTART = 1;// ������ʼʱ��Ի�����	
	private int mYear=0,mMon=0,mDay=0;
	private static final int DATE_DIALOG_IDEND = 2;// ��������ʱ��Ի�����
	private int eYear=0,eMon=0,eDay=0;
	//��֧������
	private	String[] ordereID;// ����ID[pk]
	private	String[] payType;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
	private	String[] payStatus;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
	private	String[] RealStatus;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	private	String[] smallNote;// ֽ�ҽ��
	private	String[] smallConi;// Ӳ�ҽ��
	private	String[] smallAmount;// �ֽ�Ͷ����
	private	String[] smallCard;// ���ֽ�֧�����
	private	String[] shouldPay;// ��Ʒ�ܽ��
	private	String[] shouldNo;// ��Ʒ������
	private	String[] realNote;// ֽ���˱ҽ��
	private	String[] realCoin;// Ӳ���˱ҽ��
	private	String[] realAmount;// �ֽ��˱ҽ��
	private	String[] debtAmount;// Ƿ����
	private	String[] realCard;// ���ֽ��˱ҽ��
	private	String[] payTime;//֧��ʱ��
		//��ϸ֧������
	private	String[] productID;//��Ʒid
	private	String[] cabID;//�����
	private String[] columnID;//������
	    //��Ʒ��Ϣ
	private String[] productName;// ��Ʒȫ��
	private String[] salesPrice;// �Żݼ�,�硱20.00��
	
	private SimpleAdapter simpleada = null;//�������ݵ�ת������
	//������ʾ�����ݰ�װ
    private List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.order);// ���ò����ļ�
		this.mytabhost = super.getTabHost();//ȡ��TabHost����
        LayoutInflater.from(this).inflate(R.layout.order, this.mytabhost.getTabContentView(),true);
        //����Tab�����
        TabSpec myTabhuodaomana=this.mytabhost.newTabSpec("tab0");
        myTabhuodaomana.setIndicator("������Ϣ");
        myTabhuodaomana.setContent(this.layres[0]);
    	this.mytabhost.addTab(myTabhuodaomana); 
    	
    	TabSpec myTabhuodaotest=this.mytabhost.newTabSpec("tab1");
    	myTabhuodaotest.setIndicator("ͼ����Ϣ");
    	myTabhuodaotest.setContent(this.layres[1]);
    	this.mytabhost.addTab(myTabhuodaotest); 
    	
    	//===============
    	//�����ѯҳ��
    	//===============   
		df = new SimpleDateFormat("yyyy");//�������ڸ�ʽ
    	date=df.format(new Date()); 
    	mYear=Integer.parseInt(date);
    	eYear=Integer.parseInt(date);
    	df = new SimpleDateFormat("MM");//�������ڸ�ʽ
    	date=df.format(new Date()); 
    	mMon=Integer.parseInt(date);
    	eMon=Integer.parseInt(date);
    	df = new SimpleDateFormat("dd");//�������ڸ�ʽ
    	date=df.format(new Date()); 
    	mDay=Integer.parseInt(date);
    	eDay=Integer.parseInt(date);
    	//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����ʱ����"+y+"-"+m+"-"+d);    
    	txtpayTypeValue = (TextView) findViewById(R.id.txtpayTypeValue);
    	txtpayStatusValue = (TextView) findViewById(R.id.txtpayStatusValue);
    	txtRealStatusValue = (TextView) findViewById(R.id.txtRealStatusValue);
    	txtsmallNoteValue = (TextView) findViewById(R.id.txtsmallNoteValue);
    	txtsmallConiValue = (TextView) findViewById(R.id.txtsmallConiValue);
    	txtsmallAmountValue = (TextView) findViewById(R.id.txtsmallAmountValue);
    	txtsmallCardValue = (TextView) findViewById(R.id.txtsmallCardValue);
    	txtrealNoteValue = (TextView) findViewById(R.id.txtrealNoteValue);
    	txtrealCoinValue = (TextView) findViewById(R.id.txtrealCoinValue);
    	txtrealAmountValue = (TextView) findViewById(R.id.txtrealAmountValue);
    	txtdebtAmountValue = (TextView) findViewById(R.id.txtdebtAmountValue);
    	txtrealCardValue = (TextView) findViewById(R.id.txtrealCardValue);
    	txtpayTimeValue = (TextView) findViewById(R.id.txtpayTimeValue);
    	txtproductNameValue = (TextView) findViewById(R.id.txtproductNameValue);
    	txtsalesPriceValue = (TextView) findViewById(R.id.txtsalesPriceValue);
    	txtcabIDValue = (TextView) findViewById(R.id.txtcabIDValue);
    	txtcolumnIDValue = (TextView) findViewById(R.id.txtcolumnIDValue);
    	
    	
    	lvorder = (ListView) findViewById(R.id.lvorder);
    	lvorder.setOnItemClickListener(new OnItemClickListener() 
    	{
            // ��дonItemClick����
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	txtpayTypeValue.setText(payType[position]);
            	txtpayStatusValue.setText(payStatus[position]);
            	txtRealStatusValue.setText(RealStatus[position]);
            	txtsmallNoteValue.setText(smallNote[position]);
            	txtsmallConiValue.setText(smallConi[position]);
            	txtsmallAmountValue.setText(smallAmount[position]);
            	txtsmallCardValue.setText(smallCard[position]);
            	txtrealNoteValue.setText(realNote[position]);
            	txtrealCoinValue.setText(realCoin[position]);
            	txtrealAmountValue.setText(realAmount[position]);
            	txtdebtAmountValue.setText(debtAmount[position]);
            	txtrealCardValue.setText(realCard[position]);
            	txtpayTimeValue.setText(payTime[position]);
            	txtproductNameValue.setText(productName[position]);
            	txtsalesPriceValue.setText(salesPrice[position]);
            	txtcabIDValue.setText(cabID[position]);
            	txtcolumnIDValue.setText(columnID[position]);
            }
        });
    	edtordergridstart = (EditText) findViewById(R.id.edtordergridstart);// ��ȡʱ���ı���
    	edtordergridstart.setOnClickListener(new OnClickListener() {// Ϊʱ���ı������õ��������¼�
            @Override
            public void onClick(View arg0) {
            	datetype=DATE_DIALOG_IDSTART;
                showDialog(DATE_DIALOG_IDSTART);// ��ʾ����ѡ��Ի���                
            }
        });
    	edtordergridend = (EditText) findViewById(R.id.edtordergridend);// ��ȡʱ���ı���
    	edtordergridend.setOnClickListener(new OnClickListener() {// Ϊʱ���ı������õ��������¼�
            @Override
            public void onClick(View arg0) {
            	datetype=DATE_DIALOG_IDEND;
                showDialog(DATE_DIALOG_IDEND);// ��ʾ����ѡ��Ի���                
            }
        });
    	//��ѯ
    	btnordergridquery = (Button) findViewById(R.id.btnordergridquery);
    	btnordergridquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	grid();
		    }
		});
    	//�˳�
    	btnordergridexit = (Button) findViewById(R.id.btnordergridexit);
    	btnordergridexit.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	}
	//===============
	//�����ѯҳ��
	//===============
	@Override
    protected Dialog onCreateDialog(int id) {// ��дonCreateDialog����	
        switch (id)
        {
	        case DATE_DIALOG_IDSTART:// ��������ѡ��Ի���
	            return new DatePickerDialog(this, mDateSetListener, mYear, mMon-1, mDay);            
	        case DATE_DIALOG_IDEND:// ��������ѡ��Ի���
	            return new DatePickerDialog(this, mDateSetListener, eYear, eMon-1, eDay);    
        }
        return null;
    }
	
	private DatePickerDialog.OnDateSetListener mDateSetListener= new DatePickerDialog.OnDateSetListener()
	{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			switch (datetype)
	        {
		        case DATE_DIALOG_IDSTART:// ��������ѡ��Ի���
		        	mYear=year;
					mMon=(monthOfYear+1);
					mDay=dayOfMonth;
					edtordergridstart.setText(mYear+"-"+mMon+"-"+mDay);
					break;
	            //edtordergridstart.setText(mYear+"-"+mMon+"-"+mDay);
		        case DATE_DIALOG_IDEND:// ��������ѡ��Ի���
		        	eYear=year;
					eMon=(monthOfYear+1);
					eDay=dayOfMonth; 
					edtordergridend.setText(eYear+"-"+eMon+"-"+eDay);
					break;
	        }
			
			
		}
		
	};
	//��ѯ����
	private void grid()
	{
		if((mYear>0)&&(eYear>0))
		{
			Vmc_OrderAdapter vmc_OrderAdapter=new Vmc_OrderAdapter();
			vmc_OrderAdapter.grid(Order.this, mYear, mMon, mDay, eYear, eMon, eDay);
			//��֧������
			ordereID = vmc_OrderAdapter.getOrdereID();// ����ID[pk]
			payType = vmc_OrderAdapter.getPayType();// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
			payStatus = vmc_OrderAdapter.getPayStatus();// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
			RealStatus = vmc_OrderAdapter.getRealStatus();// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
			smallNote = vmc_OrderAdapter.getSmallNote();// ֽ�ҽ��
			smallConi = vmc_OrderAdapter.getSmallConi();// Ӳ�ҽ��
			smallAmount = vmc_OrderAdapter.getSmallAmount();// �ֽ�Ͷ����
			smallCard = vmc_OrderAdapter.getSmallCard();// ���ֽ�֧�����
			shouldPay = vmc_OrderAdapter.getShouldPay();// ��Ʒ�ܽ��
			shouldNo = vmc_OrderAdapter.getShouldNo();// ��Ʒ������
			realNote = vmc_OrderAdapter.getRealNote();// ֽ���˱ҽ��
			realCoin = vmc_OrderAdapter.getRealCoin();// Ӳ���˱ҽ��
			realAmount = vmc_OrderAdapter.getRealAmount();// �ֽ��˱ҽ��
			debtAmount = vmc_OrderAdapter.getDebtAmount();// Ƿ����
			realCard = vmc_OrderAdapter.getRealCard();// ���ֽ��˱ҽ��
			payTime = vmc_OrderAdapter.getPayTime();//֧��ʱ��
			//��ϸ֧������
			productID = vmc_OrderAdapter.getProductID();//��Ʒid
			cabID = vmc_OrderAdapter.getCabID();//�����
		    columnID = vmc_OrderAdapter.getColumnID();//������
		    //��Ʒ��Ϣ
		    productName = vmc_OrderAdapter.getProductName();// ��Ʒȫ��
		    salesPrice = vmc_OrderAdapter.getSalesPrice();// �Żݼ�,�硱20.00��
			
			int x=0;
			this.listMap.clear();
			for(x=0;x<vmc_OrderAdapter.getCount();x++)
			{
			  	Map<String,String> map = new HashMap<String,String>();//����Map���ϣ�����ÿһ������
			   	map.put("ordereID", ordereID[x]);
		    	map.put("payType", payType[x]);
		    	map.put("payStatus", payStatus[x]);
		    	map.put("RealStatus", RealStatus[x]);
		    	map.put("productName", productName[x]);
		    	map.put("salesPrice", salesPrice[x]);
		    	map.put("cabID", cabID[x]);
		    	map.put("columnID", columnID[x]);
		    	map.put("payTime", payTime[x]);
		    	this.listMap.add(map);//����������
			}
			//��������ܼ��ص�data_list��
			this.simpleada = new SimpleAdapter(this,this.listMap,R.layout.orderlist,
			    		new String[]{"ordereID","payType","payStatus","RealStatus","productName","salesPrice","cabID","columnID","payTime"},//Map�е�key����
			    		new int[]{R.id.txtordereID,R.id.txtpayType,R.id.txtpayStatus,R.id.txtRealStatus,R.id.txtproductName,R.id.txtsalesPrice,R.id.txtcabID,R.id.txtcolumnID,R.id.txtpayTime});
			this.lvorder.setAdapter(this.simpleada);
			
		}
		else
		{
			Toast.makeText(Order.this, "��������ȷ��ѯʱ�䣡", Toast.LENGTH_SHORT).show();
		}
	}

}
