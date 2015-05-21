package com.easivend.app.maintain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ProductAdapter;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_column;
import com.easivend.model.Tb_vmc_product;
import com.easivend.view.GoodsSelect;
import com.example.evconsole.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HuodaoSet extends Activity 
{
	private ImageView ivhuoProID=null;
	private Button btnhuoProID=null,btnhuoaddSave=null,btnhuodelSave=null,btnhuoexit=null;
	private TextView txthuoCabID=null,txthuoColID=null,txthuoProID=null,txthuoProName=null,txthuomarketPrice=null,
			txthuosalesPrice=null,txthuocolumnStatus=null,txthuoshelfLife=null,txthuolasttime=null,txthuoIslast=null;	
	private EditText edthuopathCount=null,edthuopathRemain=null;
	private String huoID=null,cabID=null,temphuostatus=null,huostatus=null,productID=null,imgDir=null;;
	private View popview=null;
	private PopupWindow popWin=null;
	private GridView gvselectProduct=null;
	private Button btnselectexit=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huoset);// ���ò����ļ�
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
    	final String date=df.format(new Date());
		ivhuoProID = (ImageView) findViewById(R.id.ivhuoProID);
		txthuoCabID = (TextView) findViewById(R.id.txthuoCabID);
		txthuoColID = (TextView) findViewById(R.id.txthuoColID);
		txthuoProID = (TextView) findViewById(R.id.txthuoProID);
		txthuoProName = (TextView) findViewById(R.id.txthuoProName);
		txthuomarketPrice = (TextView) findViewById(R.id.txthuomarketPrice);
		txthuosalesPrice = (TextView) findViewById(R.id.txthuosalesPrice);
		txthuocolumnStatus = (TextView) findViewById(R.id.txthuocolumnStatus);
		txthuoshelfLife = (TextView) findViewById(R.id.txthuoshelfLife);
		txthuolasttime = (TextView) findViewById(R.id.txthuolasttime);
		txthuoIslast = (TextView) findViewById(R.id.txthuoIslast);
		edthuopathCount = (EditText) findViewById(R.id.edthuopathCount);	
		edthuopathCount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub				
				updatehuodaostatus();				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		}); 
		edthuopathRemain = (EditText) findViewById(R.id.edthuopathRemain);	
		edthuopathRemain.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub				
				updatehuodaostatus();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		}); 
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		huoID=bundle.getString("huoID");
		cabID=bundle.getString("cabID");
		temphuostatus=bundle.getString("huoStatus");		
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷhuoID="+huoID+"cabID="+cabID+"status="+temphuostatus);
		txthuoCabID.setText(cabID);
		txthuoColID.setText(huoID);
		//����û����а���ƷID�д�����ˢ��ҳ��Ϊ�޸���Ʒ��ҳ��
		updateHuodao(cabID,huoID);
		
		//�޸Ļ���״̬����		
		updatehuodaostatus();
		//ѡ��󶨵���Ʒ
		btnhuoProID = (Button) findViewById(R.id.btnhuoProID);
		btnhuoProID.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0)
		    {
		    	Intent intent = new Intent();
		    	intent.setClass(HuodaoSet.this, GoodsSelect.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
	            startActivityForResult(intent, REQUEST_CODE);
		    }
		});
		//�˳�
		btnhuoexit = (Button) findViewById(R.id.btnhuoexit);
		btnhuoexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
		//����
		btnhuoaddSave = (Button) findViewById(R.id.btnhuoaddSave);
		btnhuoaddSave.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) 
		    {
		    	int pathCount= 0;
		    	int pathRemain= 0;
		    	if(edthuopathCount.getText().toString().isEmpty()!=true)
		    		pathCount= Integer.parseInt(edthuopathCount.getText().toString());
		    	if(edthuopathRemain.getText().toString().isEmpty()!=true)
		    		pathRemain= Integer.parseInt(edthuopathRemain.getText().toString());
		    	
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����cabineID="+cabID+" columnID="+huoID+" productID="
    					+productID+" pathCount="+pathCount+" pathRemain="+pathRemain+" columnStatus="+huostatus);
    			
		    	if ((productID.isEmpty()!=true)&&(edthuopathCount.getText().toString().isEmpty()!=true)
		    			&&(edthuopathRemain.getText().toString().isEmpty()!=true)
		    			&&(pathCount>0)
		    		)
		    	{
		    		try 
		    		{
		    			// ����InaccountDAO����
		    			vmc_columnDAO columnDAO = new vmc_columnDAO(HuodaoSet.this);
			            //����Tb_inaccount����
		    			Tb_vmc_column tb_vmc_column = new Tb_vmc_column(cabID, huoID,productID,pathCount,
		    					pathRemain,Integer.parseInt(huostatus),date);
		    			
		    			columnDAO.addorupdate(tb_vmc_column);// �����Ʒ��Ϣ
		    			ToolClass.addOptLog(HuodaoSet.this,0,"�ϼܻ���:"+cabID+huoID);
		    			
			        	// ������Ϣ��ʾ
			            Toast.makeText(HuodaoSet.this, "��������Ʒ��������ӳɹ���", Toast.LENGTH_SHORT).show();
			            //�˳�ʱ������intent
			            Intent intent=new Intent();
			            intent.putExtra("back", "ok");
			            setResult(GoodsManager.RESULT_OK,intent);
			            finish();
//			            
		    		} catch (Exception e)
					{
						// TODO: handle exception
						Toast.makeText(HuodaoSet.this, "���û���ʧ�ܣ�", Toast.LENGTH_LONG).show();
					}		    		
		            
		        } 
		        else
		        {
		            Toast.makeText(HuodaoSet.this, "�����������", Toast.LENGTH_LONG).show();
		        }
		    }
		});
		//ɾ���󶨵���Ʒ
		btnhuodelSave = (Button) findViewById(R.id.btnhuodelSave);
		btnhuodelSave.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0)
		    {
		    	//��������Ի���
		    	Dialog alert=new AlertDialog.Builder(HuodaoSet.this)
		    		.setTitle("�Ի���")//����
		    		.setMessage("��ȷ��Ҫɾ������Ʒ��")//��ʾ�Ի����е�����
		    		.setIcon(R.drawable.ic_launcher)//����logo
		    		.setPositiveButton("ɾ��", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
		    			{				
			    				@Override
			    				public void onClick(DialogInterface dialog, int which) 
			    				{
			    					// TODO Auto-generated method stub	
			    					// ����InaccountDAO����
			    					vmc_columnDAO columnDAO = new vmc_columnDAO(HuodaoSet.this);
						            //����Tb_inaccount����
			    					Tb_vmc_column tb_vmc_column = new Tb_vmc_column(cabID, huoID,productID,0,
					    					0,Integer.parseInt(huostatus),date);				    			
			    					columnDAO.detele(tb_vmc_column);// ɾ��������Ϣ	
			    					ToolClass.addOptLog(HuodaoSet.this,2,"�¼ܻ���:"+cabID+huoID);
			    					// ������Ϣ��ʾ
						            Toast.makeText(HuodaoSet.this, "��������Ʒ��������ӳɹ���", Toast.LENGTH_SHORT).show();
						            //�˳�ʱ������intent
						            Intent intent=new Intent();
						            intent.putExtra("back", "ok");
						            setResult(GoodsManager.RESULT_OK,intent);
						            finish();
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
		});
	}
	//����ѡ��󶨵���Ʒҳ��
	private class OnClickListenerpop implements OnClickListener
    {

		@Override
		public void onClick(View arg0) 
		{
			// TODO Auto-generated method stub
//			LayoutInflater inflater=LayoutInflater.from(HuodaoSet.this);
//			//�ҵ����˲����ļ��е�view
//			HuodaoSet.this.popview = inflater.inflate(R.layout.goodsselect, null);
//			//�½������˵�ʵ����ʹ�ò����ļ��е�view,��600,��800���н���
//			HuodaoSet.this.popWin = new PopupWindow(HuodaoSet.this.popview,300,800,true);
//			//��ʼ����popWin�еĿؼ�
//			HuodaoSet.this.gvselectProduct = (GridView)HuodaoSet.this.popview.findViewById(R.id.gvselectProduct);
//			HuodaoSet.this.btnselectexit = (Button)HuodaoSet.this.popview.findViewById(R.id.btnselectexit);
//			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
//			final Vmc_ProductAdapter productAdapter=new Vmc_ProductAdapter();
//	    	productAdapter.showProInfo(HuodaoSet.this,"","shoudong",""); 
//	    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), HuodaoSet.this);// ����pictureAdapter����
//	    	gvselectProduct.setAdapter(adapter);// ΪGridView��������Դ
//			
//			//����popwindow��ͼƬ��ť
//			HuodaoSet.this.gvselectProduct.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//						long arg3) {
//					// TODO Auto-generated method stub
//					String strInfo[]=productAdapter.getProductID();
//					productID = strInfo[arg2];// ��¼������Ϣ               
//					ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+productID);
//					HuodaoSet.this.popWin.dismiss();
//					updateProduct(productID);
//				}// ΪGridView��������¼�
//	    		
//	    	});
//			//���·��ذ�ť
//			HuodaoSet.this.btnselectexit.setOnClickListener(new View.OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					HuodaoSet.this.popWin.dismiss();
//				}
//				
//			});
//			//��������
//			HuodaoSet.this.popWin.showAtLocation(HuodaoSet.this.btnhuoProID, Gravity.CENTER, 0, 0);
			
		}
    	
    }
	//����GoodsProSet������Ϣ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==HuodaoSet.RESULT_OK)
			{
				Bundle bundle=data.getExtras();
				productID = bundle.getString("productID");
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+productID);
				updateProduct(productID);
			}			
		}
	}
	//����û�����Ϣ
	private void updateHuodao(String cabID,String huoID)
	{
		// ����InaccountDAO����
		vmc_columnDAO columnDAO = new vmc_columnDAO(HuodaoSet.this);
		Tb_vmc_column tb_vmc_column = columnDAO.find(cabID,huoID);// �����Ʒ��Ϣ
		if(tb_vmc_column!=null)
		{
			edthuopathCount.setText(String.valueOf(tb_vmc_column.getPathCount()));
			edthuopathRemain.setText(String.valueOf(tb_vmc_column.getPathRemain()));
			txthuolasttime.setText(tb_vmc_column.getLasttime());
			HuodaoSet.this.productID=tb_vmc_column.getProductID();
			updateProduct(HuodaoSet.this.productID);//������Ʒ��Ϣ��ҳ����
		}		
	}
	//������Ʒ��Ϣ��ҳ����
	private void updateProduct(String productID)
	{
		vmc_productDAO productDAO = new vmc_productDAO(HuodaoSet.this);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	    Tb_vmc_product tb_inaccount = productDAO.find(productID);
	    imgDir=tb_inaccount.getAttBatch1().toString();
	    /*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
        Bitmap bitmap = ToolClass.getLoacalBitmap(imgDir); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
        ivhuoProID.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
                
		txthuoProID.setText(tb_inaccount.getProductID().toString());
		txthuoProName.setText(tb_inaccount.getProductName().toString());
		txthuomarketPrice.setText(String.valueOf(tb_inaccount.getMarketPrice()));
		txthuosalesPrice.setText(String.valueOf(tb_inaccount.getSalesPrice()));
		txthuoshelfLife.setText(String.valueOf(tb_inaccount.getShelfLife()));		
	}
	//���»�����Ϣ
	private void updatehuodaostatus()
	{
		if(temphuostatus.equals("1")==true)//��������
			huostatus="2";
		else if(temphuostatus.equals("0")==true)//��������
		{
			int tempCount= 0;
	    	int tempRemain= 0;
	    	if(edthuopathCount.getText().toString().isEmpty()!=true)
	    		tempCount= Integer.parseInt(edthuopathCount.getText().toString());
	    	if(edthuopathRemain.getText().toString().isEmpty()!=true)
	    		tempRemain= Integer.parseInt(edthuopathRemain.getText().toString());
	    	
			if ((edthuopathCount.getText().toString().isEmpty()!=true)
	    			&&(edthuopathRemain.getText().toString().isEmpty()!=true)
	    			&&(tempCount>0)&&(tempRemain>0)
	    		)//����
	    		huostatus="1";
	    	else                             //ȱ��
	    		huostatus="3";	
		}
		
		if(huostatus.equals("1")==true)
			txthuocolumnStatus.setText("����");
		else if(huostatus.equals("2")==true)
			txthuocolumnStatus.setText("����");
		else if(huostatus.equals("3")==true)
			txthuocolumnStatus.setText("ȱ��");
	}
}
