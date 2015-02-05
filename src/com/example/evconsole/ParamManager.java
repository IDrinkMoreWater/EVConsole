package com.example.evconsole;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TimePicker;


public class ParamManager extends TabActivity 
{
	private TabHost mytabhost = null;
	private int[] layres=new int[]{R.id.tab_machine,R.id.tab_device,R.id.tab_run};//��Ƕ�����ļ���id
	private static final int TIME_DIALOG_ID = 0;// ����ʱ��Ի�����
	private EditText edtrstTime=null;
	private Switch switchisNet = null;  
	private Spinner spinparamsort=null;
	//�����йصĶ���
    private ArrayAdapter<String> arrayadapter = null;
	private List<String> dataSortID = null,dataSortName=null;
	private int mHour=0,mMinute=0;
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
    	
    	//===============
    	//������������ҳ��
    	//===============
    	edtrstTime = (EditText) findViewById(R.id.edtrstTime);// ��ȡʱ���ı���
    	edtrstTime.setOnClickListener(new OnClickListener() {// Ϊʱ���ı������õ��������¼�
            @Override
            public void onClick(View arg0) {
                showDialog(TIME_DIALOG_ID);// ��ʾ����ѡ��Ի���
            }
        });
    	//����
    	this.spinparamsort = (Spinner) super.findViewById(R.id.spinparamsort);
    	showsortInfo();
    	switchisNet = (Switch)findViewById(R.id.switchisNet); //��ȡ���ؼ�  
    	switchisNet.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
			}  
            
            
        });   
	}
	//===============
	//������������ҳ��
	//===============
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
    	edtrstTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute + 1));
    }
}
