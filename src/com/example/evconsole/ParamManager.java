package com.example.evconsole;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.easivend.dao.vmc_productDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.ToolClass;
import com.easivend.model.Tb_vmc_product;
import com.easivend.model.Tb_vmc_system_parameter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TimePicker;


public class ParamManager extends TabActivity 
{
	private TabHost mytabhost = null;
	private int[] layres=new int[]{R.id.tab_machine,R.id.tab_device,R.id.tab_run};//��Ƕ�����ļ���id
	private static final int TIME_DIALOG_ID = 0;// ����ʱ��Ի�����
	private EditText edtdevID=null,edtdevhCode=null,edtmainPwd=null,edtrstTime=null,edtrstDay=null;
	private Switch switchisNet = null,switchisbuyCar = null,switchlanguage = null,switchbaozhiProduct = null,switchemptyProduct = null,switchamount = null,switchcard = null,
			switchzhifubaofaca = null,switchzhifubaoer = null,switchweixing = null,switchprinter = null;    
	private RadioGroup grpisfenClass=null, grpliebiaoKuan=null;
	private RadioButton hunradiobtn=null,typeradiobtn=null,guiradiobtn=null,smallradiobtn=null,normalradiobtn=null,bigradiobtn=null;
	private Spinner spinparamsort=null;
	private Button btnmachineSave=null,btnmachineexit=null,btndeviceSave=null,btndeviceexit=null,btnamount=null,btncard=null,btnzhifubaofaca=null,
			btnzhifubaoer=null,btnweixing=null,btnprinter=null;
	private int isfenClass=0,liebiaoKuan=0;
	private int proSortType=6;
	//�����йصĶ���
    private ArrayAdapter<String> arrayadapter = null;
	private List<String> dataSortID = null,dataSortName=null;
	private int mHour=0,mMinute=0;
	
	private ExpandableListView emachinelistview = null;
	private ExpandableListAdapter adapter = null;
	private ImageView ivmachineLogo=null;
	private Button btnmachineImg=null;
	private Uri uri=null;
	private String imgDir=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.parammanage);		
		this.mytabhost = super.getTabHost();//ȡ��TabHost����
        LayoutInflater.from(this).inflate(R.layout.parammanage, this.mytabhost.getTabContentView(),true);
        //����Tab�����
        TabSpec myTabmachine=this.mytabhost.newTabSpec("tab0");
        myTabmachine.setIndicator("������������");
        myTabmachine.setContent(this.layres[0]);
    	this.mytabhost.addTab(myTabmachine); 
    	
    	TabSpec myTabdevice=this.mytabhost.newTabSpec("tab1");
    	myTabdevice.setIndicator("�豸��������");
    	myTabdevice.setContent(this.layres[1]);
    	this.mytabhost.addTab(myTabdevice); 
    	
    	TabSpec myTabrun=this.mytabhost.newTabSpec("tab2");
    	myTabrun.setIndicator("���в�������");
    	myTabrun.setContent(this.layres[2]);
    	this.mytabhost.addTab(myTabrun); 
    	
    	//===========================
    	//������������
    	//===========================
    	edtdevID = (EditText) findViewById(R.id.edtdevID);
    	edtdevhCode = (EditText) findViewById(R.id.edtdevhCode);
    	switchisNet = (Switch)findViewById(R.id.switchisNet); //��ȡ���ؼ�  
    	this.grpisfenClass = (RadioGroup) super.findViewById(R.id.grpisfenClass);
	    this.hunradiobtn = (RadioButton) super.findViewById(R.id.hunradiobtn);
	    this.typeradiobtn = (RadioButton) super.findViewById(R.id.typeradiobtn);	
	    this.guiradiobtn = (RadioButton) super.findViewById(R.id.guiradiobtn);		    
	    this.grpisfenClass.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 
				if(hunradiobtn.getId()==checkedId)
				{
					isfenClass=0;
				}
				//
				else if(typeradiobtn.getId()==checkedId)
				{
					isfenClass=1;
				}
				else if(guiradiobtn.getId()==checkedId)
				{
					isfenClass=2;
				}
			}
		});
	    switchisbuyCar = (Switch)findViewById(R.id.switchisbuyCar);    	
	    this.grpliebiaoKuan = (RadioGroup) super.findViewById(R.id.grpliebiaoKuan);
	    this.smallradiobtn = (RadioButton) super.findViewById(R.id.smallradiobtn);
	    this.normalradiobtn = (RadioButton) super.findViewById(R.id.normalradiobtn);	
	    this.bigradiobtn = (RadioButton) super.findViewById(R.id.bigradiobtn);	
	    this.grpliebiaoKuan.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 
				if(smallradiobtn.getId()==checkedId)
				{
					liebiaoKuan=0;
				}
				//
				else if(normalradiobtn.getId()==checkedId)
				{
					liebiaoKuan=1;
				}
				else if(bigradiobtn.getId()==checkedId)
				{
					liebiaoKuan=2;
				}
			}
		});
	    edtmainPwd = (EditText) findViewById(R.id.edtmainPwd);
	    switchlanguage = (Switch)findViewById(R.id.switchlanguage);
    	edtrstTime = (EditText) findViewById(R.id.edtrstTime);// ��ȡʱ���ı���
    	edtrstTime.setOnClickListener(new OnClickListener() {// Ϊʱ���ı������õ��������¼�
            @Override
            public void onClick(View arg0) {
                showDialog(TIME_DIALOG_ID);// ��ʾ����ѡ��Ի���
            }
        });
    	edtrstDay = (EditText) findViewById(R.id.edtrstDay);
    	switchbaozhiProduct = (Switch)findViewById(R.id.switchbaozhiProduct);
    	switchemptyProduct = (Switch)findViewById(R.id.switchemptyProduct);
    	//����
    	this.spinparamsort = (Spinner) super.findViewById(R.id.spinparamsort); 
    	showsortInfo();
    	spinparamsort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			//��ѡ��ı�ʱ����
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				proSortType=arg2;				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		}); 
    	loadmachineparam();
    	btnmachineSave = (Button) findViewById(R.id.btnmachineSave);
    	btnmachineSave.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        saveparam();
		    }
		});
    	btnmachineexit = (Button) findViewById(R.id.btnmachineexit);
    	btnmachineexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
		    }
		});
    	
    	
    	
    	
    	
    	//===========================
    	//�豸��������ҳ��
    	//===========================
    	switchamount = (Switch)findViewById(R.id.switchamount);
    	switchamount.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnamount.setEnabled(isChecked);	
			}  
            
            
        }); 
    	switchcard = (Switch)findViewById(R.id.switchcard);
    	switchcard.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btncard.setEnabled(isChecked);	
			}  
            
            
        });
    	switchzhifubaofaca = (Switch)findViewById(R.id.switchzhifubaofaca);
    	switchzhifubaofaca.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnzhifubaofaca.setEnabled(isChecked);	
			}  
            
            
        });
    	switchzhifubaoer = (Switch)findViewById(R.id.switchzhifubaoer);
    	switchzhifubaoer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnzhifubaoer.setEnabled(isChecked);	
			}  
            
            
        });
    	switchweixing = (Switch)findViewById(R.id.switchweixing);
    	switchweixing.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnweixing.setEnabled(isChecked);	
			}  
            
            
        });
    	switchprinter = (Switch)findViewById(R.id.switchprinter);
    	switchprinter.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				btnprinter.setEnabled(isChecked);	
			}  
            
            
        });
    	    	    	
    	btndeviceSave = (Button) findViewById(R.id.btndeviceSave);
    	btndeviceSave.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	saveparam();
		    }
		});
    	btndeviceexit = (Button) findViewById(R.id.btndeviceexit);
    	btndeviceexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
		    }
		});
    	btnamount = (Button) findViewById(R.id.btnamount);
    	btnamount.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	Intent intent = new Intent(ParamManager.this, AddInaccount.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
		        startActivity(intent);// ��AddInaccount
		    }
		});
    	
    	btncard = (Button) findViewById(R.id.btncard);
    	btnzhifubaofaca = (Button) findViewById(R.id.btnzhifubaofaca);
    	btnzhifubaoer = (Button) findViewById(R.id.btnzhifubaoer);
    	btnweixing = (Button) findViewById(R.id.btnweixing);
    	btnprinter = (Button) findViewById(R.id.btnprinter);
    	loaddeviceparam();
    	
    	
    	//===========================
    	//���в�������
    	//===========================
    	this.ivmachineLogo = (ImageView) findViewById(R.id.ivmachineLogo);
    	this.emachinelistview = (ExpandableListView) super.findViewById(R.id.emachinelistview);
    	
    	this.adapter = new MyExpanseListAdapter(this);
        this.emachinelistview.setAdapter(this.adapter);
        
        this.emachinelistview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(ParamManager.this, "��ѡ��,group="+groupPosition+",child="+childPosition, Toast.LENGTH_LONG).show();
				if((groupPosition==0&&childPosition==0)||(groupPosition==1&&childPosition==0))
				{	
					emachineListviewSet(1);				
				}
				else 
				{
					emachineListviewSet(2);	
				}
				return false;
			}
		});
        //ѡ��ͼƬ
        btnmachineImg = (Button)findViewById(R.id.btnmachineImg);  
        btnmachineImg.setOnClickListener(new View.OnClickListener() {
  			
  			@Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
  				Intent intent = new Intent();  
                 /* ����Pictures����Type�趨Ϊimage */  
                 intent.setType("image/*");  
                 /* ʹ��Intent.ACTION_GET_CONTENT���Action */  
                 intent.setAction(Intent.ACTION_GET_CONTENT);   
                 /* ȡ����Ƭ�󷵻ر����� */  
                 startActivityForResult(intent, 1);
  			}
  		}); 
        
	}
	//===========================
	//������������
	//===========================
	// spinner��ʾ��Ʒ������Ϣ
	private void showsortInfo() 
	{	    
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	    dataSortID = new ArrayList<String>();
	    dataSortName = new ArrayList<String>();
	    dataSortID.add("sale");
	    dataSortName.add("sale�������");
	    dataSortID.add("marketPrice");
	    dataSortName.add("marketPriceԭ��");
	    dataSortID.add("salesPrice");
	    dataSortName.add("salesPrice���ۼ�");
	    dataSortID.add("shelfLife");
	    dataSortName.add("shelfLife��������");
	    dataSortID.add("colucount");
	    dataSortName.add("colucountʣ������");
	    dataSortID.add("onloadTime");
	    dataSortName.add("onloadTime�ϼ�ʱ��");
	    dataSortID.add("shoudong");
	    dataSortName.add("shoudong�ֶ�����");	    
	    // ʹ���ַ��������ʼ��ArrayAdapter����
	    arrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSortName);
	    spinparamsort.setAdapter(arrayadapter);// ΪListView�б���������Դ
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {// ��дonCreateDialog����

        switch (id) {
        case TIME_DIALOG_ID:// ��������ѡ��Ի���
            return new TimePickerDialog(this, mDateSetListener, mHour, mMinute, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mDateSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour=hourOfDay;
			mMinute=minute;
			updateDisplay(); 
		}
        
    };
    private void updateDisplay() 
    {
        // ��ʾ���õ�ʱ��
    	edtrstTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute));
    }
    
    private void saveparam()
    {
    	String devID = edtdevID.getText().toString();
    	String devhCode = edtdevhCode.getText().toString();
    	int isNet = (switchisNet.isChecked()==true)?1:0;
    	int isbuyCar = (switchisbuyCar.isChecked()==true)?1:0;
    	String mainPwd= edtmainPwd.getText().toString();
    	int amount = (switchamount.isChecked()==true)?1:0;
    	int card = (switchcard.isChecked()==true)?1:0;
    	int zhifubaofaca = (switchzhifubaofaca.isChecked()==true)?1:0;
    	int zhifubaoer = (switchzhifubaoer.isChecked()==true)?1:0;
    	int weixing = (switchweixing.isChecked()==true)?1:0;
    	int printer = (switchprinter.isChecked()==true)?1:0;
    	int language = (switchlanguage.isChecked()==true)?1:0;
    	String rstTime = edtrstTime.getText().toString();
    	int rstDay = 0;
    	if(edtrstDay.getText().toString().isEmpty()!=true)
    		rstDay = Integer.parseInt(edtrstDay.getText().toString());
    	int baozhiProduct = (switchbaozhiProduct.isChecked()==true)?1:0;
    	int emptyProduct = (switchemptyProduct.isChecked()==true)?1:0;
    	if ((devID.isEmpty()!=true)&&(devhCode.isEmpty()!=true)    				    			
    		)
    	{
    		try 
    		{
    			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����devID="+devID+" devhCode="+devhCode+" isNet="
    					+isNet+" isfenClass="+isfenClass+" isbuyCar="+isbuyCar+" liebiaoKuan="+liebiaoKuan+" mainPwd="
    					+mainPwd+" amount="+amount+" card="+card+" zhifubaofaca="+zhifubaofaca+" zhifubaoer="+zhifubaoer
    					+" weixing="+weixing+" printer="+printer+" language="+language
    					+" rstTime="+rstTime+" rstDay="+rstDay+" baozhiProduct="+baozhiProduct
    					+" emptyProduct="+emptyProduct+" proSortType="+proSortType);
    			// ����InaccountDAO����
    			vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(ParamManager.this);
	            //����Tb_inaccount���� 
    			Tb_vmc_system_parameter tb_vmc_system_parameter = new Tb_vmc_system_parameter(devID, devhCode, isNet,isfenClass, 
    					isbuyCar,liebiaoKuan,mainPwd,amount,card,zhifubaofaca,zhifubaoer,weixing,printer,language,rstTime,rstDay,
    					baozhiProduct,emptyProduct, proSortType);
    			parameterDAO.add(tb_vmc_system_parameter);    			
	        	// ������Ϣ��ʾ
	            Toast.makeText(ParamManager.this, "������ӳɹ���", Toast.LENGTH_SHORT).show();	            
	            
    		} catch (Exception e)
			{
				// TODO: handle exception
				Toast.makeText(ParamManager.this, "�������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			}		    		
            
        } 
        else
        {
            Toast.makeText(ParamManager.this, "����д��ɫ���֣�", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void loadmachineparam()
    {
    	vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(ParamManager.this);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
	    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����devID="+tb_inaccount.getDevID().toString()+" devhCode="+tb_inaccount.getDevhCode().toString()+" isNet="
					+tb_inaccount.getIsNet()+" isfenClass="+tb_inaccount.getIsfenClass()+" isbuyCar="+tb_inaccount.getIsbuyCar()+" liebiaoKuan="+tb_inaccount.getLiebiaoKuan()+" mainPwd="
					+tb_inaccount.getMainPwd()+" amount="+tb_inaccount.getAmount()+" card="+tb_inaccount.getCard()+" zhifubaofaca="+tb_inaccount.getZhifubaofaca()+" zhifubaoer="+tb_inaccount.getZhifubaoer()
					+" weixing="+tb_inaccount.getWeixing()+" printer="+tb_inaccount.getPrinter()+" language="+tb_inaccount.getLanguage()
					+" rstTime="+tb_inaccount.getRstTime().toString()+" rstDay="+tb_inaccount.getRstDay()+" baozhiProduct="+tb_inaccount.getBaozhiProduct()
					+" emptyProduct="+tb_inaccount.getEmptyProduct()+" proSortType="+tb_inaccount.getProSortType()); 
		    edtdevID.setText(tb_inaccount.getDevID().toString());
		    edtdevhCode.setText(tb_inaccount.getDevhCode().toString());	  
		    //switchisNet.setChecked(true);
		    switchisNet.setChecked((tb_inaccount.getIsNet()==1)?true:false);
		    switch(tb_inaccount.getIsfenClass())
		    {
		    	case 0:
		    		hunradiobtn.setChecked(true);				
		    		break;
		    	case 1:
		    		typeradiobtn.setChecked(true);				
		    		break;
		    	case 2:
		    		guiradiobtn.setChecked(true);
		    		break;
		    }
		    switchisbuyCar.setChecked((tb_inaccount.getIsbuyCar()==1)?true:false);
		    switch(tb_inaccount.getLiebiaoKuan())
		    {
		    	case 0:
		    		smallradiobtn.setChecked(true);						
		    		break;
		    	case 1:
		    		normalradiobtn.setChecked(true);										
		    		break;
		    	case 2:
		    		bigradiobtn.setChecked(true);		
		    		break;
		    }
		    edtmainPwd.setText(tb_inaccount.getMainPwd().toString());
		    switchlanguage.setChecked((tb_inaccount.getLanguage()==1)?true:false);
		    edtrstTime.setText(tb_inaccount.getRstTime().toString());
		    edtrstDay.setText(String.valueOf(tb_inaccount.getRstDay()));
		    switchbaozhiProduct.setChecked((tb_inaccount.getBaozhiProduct()==1)?true:false);
		    switchemptyProduct.setChecked((tb_inaccount.getEmptyProduct()==1)?true:false);
		    //����������Ĭ��ֵ
		    spinparamsort.setSelection(tb_inaccount.getProSortType());
    	}
	}
    
    
    //===========================
	//�豸��������ҳ��
	//===========================
    private void loaddeviceparam()
    {
    	vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(ParamManager.this);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
	    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����devID="+tb_inaccount.getDevID().toString()+" devhCode="+tb_inaccount.getDevhCode().toString()+" isNet="
					+tb_inaccount.getIsNet()+" isfenClass="+tb_inaccount.getIsfenClass()+" isbuyCar="+tb_inaccount.getIsbuyCar()+" liebiaoKuan="+tb_inaccount.getLiebiaoKuan()+" mainPwd="
					+tb_inaccount.getMainPwd()+" amount="+tb_inaccount.getAmount()+" card="+tb_inaccount.getCard()+" zhifubaofaca="+tb_inaccount.getZhifubaofaca()+" zhifubaoer="+tb_inaccount.getZhifubaoer()
					+" weixing="+tb_inaccount.getWeixing()+" printer="+tb_inaccount.getPrinter()+" language="+tb_inaccount.getLanguage()
					+" rstTime="+tb_inaccount.getRstTime().toString()+" rstDay="+tb_inaccount.getRstDay()+" baozhiProduct="+tb_inaccount.getBaozhiProduct()
					+" emptyProduct="+tb_inaccount.getEmptyProduct()+" proSortType="+tb_inaccount.getProSortType()); 
		    
		    switchamount.setChecked((tb_inaccount.getAmount()==1)?true:false);
		    switchcard.setChecked((tb_inaccount.getCard()==1)?true:false);
		    switchzhifubaofaca.setChecked((tb_inaccount.getZhifubaofaca()==1)?true:false);
		    switchzhifubaoer.setChecked((tb_inaccount.getZhifubaoer()==1)?true:false);
		    switchweixing.setChecked((tb_inaccount.getWeixing()==1)?true:false);
		    switchprinter.setChecked((tb_inaccount.getPrinter()==1)?true:false);
    	}
	}
    
    //===========================
  	//���в�������ҳ��
  	//===========================
    @Override  
	//ѡȡͼƬ����ֵ
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if (resultCode == RESULT_OK) {  
	         uri = data.getData();  
	         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<uri="+ uri.toString());  
	         ContentResolver cr = this.getContentResolver();  
	         try {  
	             Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
	             /* ��Bitmap�趨��ImageView */  
	             ivmachineLogo.setImageBitmap(bitmap);  
	             imgDir=ToolClass.getRealFilePath(ParamManager.this,uri);
	         } catch (FileNotFoundException e) {  
	             Log.e("Exception", e.getMessage(),e);  
	         }  
	     }  
    	super.onActivityResult(requestCode, resultCode, data);  
    }
    
    //dialogtype==1������ֵѡ���,2����ʱ��ѡ���
    private void emachineListviewSet(int dialogtype)
    {    	
    	View myview=null;
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(ParamManager.this);
		if(dialogtype==1)//��ֵѡ���
		{
			myview=factory.inflate(R.layout.selectinteger, null);
		}
		else if(dialogtype==2)//ʱ��ѡ���
		{
			myview=factory.inflate(R.layout.selecttimepick, null);
		}
			
		Dialog dialog = new AlertDialog.Builder(ParamManager.this)
		.setTitle("����")
		.setPositiveButton("����", new DialogInterface.OnClickListener() 		{
				
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				;
			}
		})
		.setNegativeButton("ȡ��",  new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
    	{			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub				
			}
    	}
    )
		.setView(myview)//���ｫ�Ի��򲼾��ļ����뵽�Ի�����
		.create();
		dialog.show();	    	
    }
}

//===========================
//���в�������ҳ��
//===========================
//ר��������ݵ���������
class MyExpanseListAdapter extends BaseExpandableListAdapter 
{
	//������
	private String group[]=new String[]{">>>��������",">>>�¶�����",">>>����",">>>����",">>>����"};
	//������
	private String child[][]=new String[][]{{"�����տ�������:","�����տ���ʱ���","�ڼ��տ�������:","�ڼ��տ���ʱ���"},{"�¶�ֵ:","�����տ���ʱ���","�ڼ��տ���ʱ���"},
			{"�����տ���ʱ���","�ڼ��տ���ʱ���"},{"�����տ���ʱ���","�ڼ��տ���ʱ���"},{"�����տ���ʱ���","�ڼ��տ���ʱ���"}};
	private Context context=null;

	//���췽��
	public MyExpanseListAdapter(Context context)
	{
		this.context = context;
	}
	@Override
	//ȡ��ָ������ѡ��
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return this.child[arg0][arg1];
	}

	@Override
	//ȡ����ID
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}
	
	private TextView buildTextView()
	{
		AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,35); 
		TextView textView=new TextView(this.context);
		textView.setLayoutParams(params);
		//textView.setTextSize(25.0f);
		//textView.setGravity(Gravity.LEFT);
		//textView.setPadding(40, 8, 3, 3);
		return textView;
	}

	@Override
	//��������ͼ
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textview=new TextView(this.context);
		textview.setText(this.getChild(groupPosition, childPosition).toString());
		return textview;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.child[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.group[groupPosition];
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.group.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textview=this.buildTextView();
		textview.setText(this.getGroup(groupPosition).toString());
		return textview;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
