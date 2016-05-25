package com.easivend.app.maintain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_logDAO;
import com.easivend.model.Tb_vmc_log;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class LogOpt extends Activity {
	private EditText edtloggridstart=null,edtloggridend=null;
	private Button btnloggridquery=null,btnloggriddel=null,btnloggridexit=null;	
	private ListView lvlog=null;
	private SimpleDateFormat df;
	private String date=null;
	private int datetype=0;//1��ʼʱ��,2����ʱ��
	private static final int DATE_DIALOG_IDSTART = 1;// ������ʼʱ��Ի�����	
	private int mYear=0,mMon=0,mDay=0;
	private static final int DATE_DIALOG_IDEND = 2;// ��������ʱ��Ի�����
	private int eYear=0,eMon=0,eDay=0;
	private SimpleAdapter simpleada = null;//�������ݵ�ת������
	//������ʾ�����ݰ�װ
    private List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
    private String[] logID;// ����ID[pk]
    private String[] logType;// ��������0���,1�޸�,2ɾ��
    private String[] logDesc;// ��������
    private String[] logTime;//����ʱ��
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);	
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
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
    	lvlog = (ListView) findViewById(R.id.lvlog);
    	edtloggridstart = (EditText) findViewById(R.id.edtloggridstart);// ��ȡʱ���ı���
    	edtloggridstart.setFocusable(false);//���ø�edittext��ý���
    	edtloggridstart.setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						// �ر�����̣������������edittext��ʱ�򣬲��ᵯ��ϵͳ�Դ������뷨
						edtloggridstart.setInputType(InputType.TYPE_NULL);
						return false;
					}
				});
    	edtloggridstart.setOnClickListener(new OnClickListener() {// Ϊʱ���ı������õ��������¼�
            @Override
            public void onClick(View arg0) {
            	datetype=DATE_DIALOG_IDSTART;
                showDialog(DATE_DIALOG_IDSTART);// ��ʾ����ѡ��Ի���                
            }
        });
    	edtloggridend = (EditText) findViewById(R.id.edtloggridend);// ��ȡʱ���ı���
    	edtloggridend.setFocusable(false);//���ø�edittext��ý���
    	edtloggridend.setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						// �ر�����̣������������edittext��ʱ�򣬲��ᵯ��ϵͳ�Դ������뷨
						edtloggridend.setInputType(InputType.TYPE_NULL);
						return false;
					}
				});
    	edtloggridend.setOnClickListener(new OnClickListener() {// Ϊʱ���ı������õ��������¼�
            @Override
            public void onClick(View arg0) {
            	datetype=DATE_DIALOG_IDEND;
                showDialog(DATE_DIALOG_IDEND);// ��ʾ����ѡ��Ի���                
            }
        });
    	//��ѯ
    	btnloggridquery = (Button) findViewById(R.id.btnloggridquery);
    	btnloggridquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	loggrid();
		    }
		});
    	//ɾ����ѯ
    	btnloggriddel = (Button) findViewById(R.id.btnloggriddel);
    	btnloggriddel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	delloggrid();
		    }
		});
    	//�˳�
    	btnloggridexit = (Button) findViewById(R.id.btnloggridexit);
    	btnloggridexit.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
    	//��̬���ÿؼ��߶�
    	//
    	DisplayMetrics  dm = new DisplayMetrics();  
        //ȡ�ô�������  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        //���ڵĿ��  
        int screenWidth = dm.widthPixels;          
        //���ڸ߶�  
        int screenHeight = dm.heightPixels;      
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ļ"+screenWidth
				+"],["+screenHeight+"]","log.txt");	
	  //����
	  if(ToolClass.getOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
	  {
		  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lvlog.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
	      linearParams.height =  screenHeight-252;// ���ؼ��ĸ�ǿ�����75����
	      lvlog.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
	  }
	  //����
	  else
	  {
		  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lvlog.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
	      linearParams.height =  screenHeight-700;// ���ؼ��ĸ�ǿ�����75����
	      lvlog.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
	  }
    	
	}
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
					edtloggridstart.setText(mYear+"-"+mMon+"-"+mDay);
					break;
	            //edtloggridstart.setText(mYear+"-"+mMon+"-"+mDay);
		        case DATE_DIALOG_IDEND:// ��������ѡ��Ի���
		        	eYear=year;
					eMon=(monthOfYear+1);
					eDay=dayOfMonth; 
					edtloggridend.setText(eYear+"-"+eMon+"-"+eDay);
					break;
	        }
			
			
		}
		
	};
	//��ѯ����
	private void loggrid()
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<start:"+ToolClass.getDayOfMonth(mYear, mMon, mDay)+"end:"+ToolClass.getDayOfMonth(eYear, eMon, eDay)+"ʱ���С="+ToolClass.dateCompare(ToolClass.getDayOfMonth(mYear, mMon, mDay),ToolClass.getDayOfMonth(eYear, eMon, eDay)),"log.txt");
		if(
				(!edtloggridstart.getText().toString().isEmpty())
			  &&(!edtloggridend.getText().toString().isEmpty())
			  &&(ToolClass.dateCompare(ToolClass.getDayOfMonth(mYear, mMon, mDay),ToolClass.getDayOfMonth(eYear, eMon, eDay))<0)
		  )
		{
			LoggridQueryThread logquery=new LoggridQueryThread();
			logquery.execute();
		}
		else
		{
			ToolClass.failToast("��������ȷ��ѯʱ�䣡");	
		}
	}
	//****************
	//�첽�̣߳����ڲ�ѯ��¼
	//****************
	private class LoggridQueryThread extends AsyncTask<Void,Void,List<Tb_vmc_log>>
	{

		@Override
		protected List<Tb_vmc_log> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String mYearStr=null,mMonthStr=null,mDayStr=null;
			String eYearStr=null,eMonthStr=null,eDayStr=null;
			
			mYearStr=((mYear<10)?("0"+String.valueOf(mYear)):String.valueOf(mYear));
			mMonthStr=((mMon<10)?("0"+String.valueOf(mMon)):String.valueOf(mMon));
			mDayStr=((mDay<10)?("0"+String.valueOf(mDay)):String.valueOf(mDay));
			eYearStr=((eYear<10)?("0"+String.valueOf(eYear)):String.valueOf(eYear));
			eMonthStr=((eMon<10)?("0"+String.valueOf(eMon)):String.valueOf(eMon));
			eDayStr=((eDay<10)?("0"+String.valueOf(eDay)):String.valueOf(eDay));
			// ����InaccountDAO����
			vmc_logDAO logDAO = new vmc_logDAO(LogOpt.this);
			String start=mYearStr+"-"+mMonthStr+"-"+mDayStr;
			String end=eYearStr+"-"+eMonthStr+"-"+eDayStr;
			List<Tb_vmc_log> listinfos=logDAO.getScrollPay(start,end);
			return listinfos;
		}

		@Override
		protected void onPostExecute(List<Tb_vmc_log> listinfos) {
			// TODO Auto-generated method stub
			String[] strInfos = new String[listinfos.size()];
			logID = new String[listinfos.size()];
			logType = new String[listinfos.size()];
			logDesc = new String[listinfos.size()];
			logTime = new String[listinfos.size()];
			int m=0;
			// ����List���ͼ���
		    for (Tb_vmc_log tb_inaccount : listinfos) 
		    {
		    	//��֧������
		    	logID[m]= tb_inaccount.getLogID();
		    	logType[m] = ToolClass.typestr(3,tb_inaccount.getLogType());
		    	logDesc[m] = tb_inaccount.getLogDesc();
		    	logTime[m] = tb_inaccount.getLogTime();			
		    	m++;// ��ʶ��1
		    }
			
			int x=0;
			LogOpt.this.listMap.clear();
			for(x=0;x<listinfos.size();x++)
			{
			  	Map<String,String> map = new HashMap<String,String>();//����Map���ϣ�����ÿһ������
			   	map.put("logID", logID[x]);
		    	map.put("logType", logType[x]);
		    	map.put("logDesc", logDesc[x]);
		    	map.put("logTime", logTime[x]);
		    	LogOpt.this.listMap.add(map);//����������
			}
			//��������ܼ��ص�data_list��
			LogOpt.this.simpleada = new SimpleAdapter(LogOpt.this,LogOpt.this.listMap,R.layout.loglist,
			    		new String[]{"logID","logType","logDesc","logTime"},//Map�е�key����
			    		new int[]{R.id.txtlogID,R.id.txtlogType,R.id.txtlogDesc,R.id.txtlogTime});
			LogOpt.this.lvlog.setAdapter(LogOpt.this.simpleada);
		}
				
	}
	
	//ɾ����ѯ����
	private void delloggrid()
	{
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<start:"+ToolClass.getDayOfMonth(mYear, mMon, mDay)+"end:"+ToolClass.getDayOfMonth(eYear, eMon, eDay)+"ʱ���С="+ToolClass.dateCompare(ToolClass.getDayOfMonth(mYear, mMon, mDay),ToolClass.getDayOfMonth(eYear, eMon, eDay)),"log.txt");
		if(
				(!edtloggridstart.getText().toString().isEmpty())
			  &&(!edtloggridend.getText().toString().isEmpty())
			  &&(ToolClass.dateCompare(ToolClass.getDayOfMonth(mYear, mMon, mDay),ToolClass.getDayOfMonth(eYear, eMon, eDay))<0)
		  )
		{
			//��������Ի���
	    	Dialog alert=new AlertDialog.Builder(LogOpt.this)
	    		.setTitle("�Ի���")//����
	    		.setMessage("��ȷ��Ҫɾ���ü�¼��")//��ʾ�Ի����е�����
	    		.setIcon(R.drawable.ic_launcher)//����logo
	    		.setPositiveButton("ɾ��", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
	    			{				
		    				@Override
		    				public void onClick(DialogInterface dialog, int which) 
		    				{
		    					// TODO Auto-generated method stub	
		    					LoggridDelThread logdel=new LoggridDelThread();
		    					logdel.execute();
		    				}
	    		      }
	    			)		    		        
			        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener()//ȡ����ť���������ü����¼�
			        	{			
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub				
							}
			        	}
			        )
			        .create();//����һ���Ի���
			        alert.show();//��ʾ�Ի���
			
		}
		else
		{
			ToolClass.failToast("��������ȷ��ѯʱ�䣡");	
		}
	}
	
	//*******************
	//�첽�̣߳�����ɾ����ѯ��¼
	//*******************
	private class LoggridDelThread extends AsyncTask<Void,Void,Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String mYearStr=null,mMonthStr=null,mDayStr=null;
			String eYearStr=null,eMonthStr=null,eDayStr=null;
			
			mYearStr=((mYear<10)?("0"+String.valueOf(mYear)):String.valueOf(mYear));
			mMonthStr=((mMon<10)?("0"+String.valueOf(mMon)):String.valueOf(mMon));
			mDayStr=((mDay<10)?("0"+String.valueOf(mDay)):String.valueOf(mDay));
			eYearStr=((eYear<10)?("0"+String.valueOf(eYear)):String.valueOf(eYear));
			eMonthStr=((eMon<10)?("0"+String.valueOf(eMon)):String.valueOf(eMon));
			eDayStr=((eDay<10)?("0"+String.valueOf(eDay)):String.valueOf(eDay));
			// ����InaccountDAO����
			vmc_logDAO logDAO = new vmc_logDAO(LogOpt.this);
			String start=mYearStr+"-"+mMonthStr+"-"+mDayStr;
			String end=eYearStr+"-"+eMonthStr+"-"+eDayStr;	
			logDAO.detele(start,end);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			// ������Ϣ��ʾ
            Toast.makeText(LogOpt.this, "��¼ɾ���ɹ���", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
    	//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}
}
