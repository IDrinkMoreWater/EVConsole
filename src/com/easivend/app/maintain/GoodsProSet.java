package com.easivend.app.maintain;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ClassAdapter;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsProSet extends Activity 
{
	private Uri uri=null;
	private String proID=null,imgDir=null;
	private ImageView ivProduct=null;
	private Button btnImg=null,btnaddProSave=null,btnaddProexit=null;
	private EditText edtproductID=null,edtproductName=null,edtmarketPrice=null,edtsalesPrice=null,
			edtshelfLife=null,edtproductDesc=null;
	private TextView onloadTime=null;	
	private String[] proclassID = null;// �����ַ������飬�����洢��Ʒid��Ϣ
	private Spinner spinproductclassID=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodsset);// ���ò����ļ�
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
    	final String date=df.format(new Date());
		ivProduct = (ImageView) findViewById(R.id.ivProduct);
		onloadTime = (TextView) findViewById(R.id.onloadTime);
		edtproductID = (EditText) findViewById(R.id.edtproductID);		
		edtproductName = (EditText) findViewById(R.id.edtproductName);
		edtmarketPrice = (EditText) findViewById(R.id.edtmarketPrice);		
		edtsalesPrice = (EditText) findViewById(R.id.edtsalesPrice);
		edtshelfLife = (EditText) findViewById(R.id.edtshelfLife);		
		edtproductDesc = (EditText) findViewById(R.id.edtproductDesc);
		this.spinproductclassID = (Spinner) super.findViewById(R.id.spinproductclassID);
		
		showInfo();//��ʾ�����б�
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		proID=bundle.getString("proID");
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+proID,"log.txt");
		//�����ƷID�д�����ˢ��ҳ��Ϊ�޸���Ʒ��ҳ��
		if(proID.isEmpty()!=true)
		{
			vmc_productDAO productDAO = new vmc_productDAO(GoodsProSet.this);// ����InaccountDAO����
		    // ��ȡ����������Ϣ�����洢��List���ͼ�����
		    Tb_vmc_product tb_inaccount = productDAO.find(proID);
		    imgDir=tb_inaccount.getAttBatch1().toString();
		    /*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
	        Bitmap bitmap = ToolClass.getLoacalBitmap(imgDir); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
	        ivProduct.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
	        
		    edtproductID.setText(tb_inaccount.getProductID().toString());
		    edtproductName.setText(tb_inaccount.getProductName().toString());
		    edtmarketPrice.setText(String.valueOf(tb_inaccount.getMarketPrice()));
		    edtsalesPrice.setText(String.valueOf(tb_inaccount.getSalesPrice()));
		    edtshelfLife.setText(String.valueOf(tb_inaccount.getShelfLife()));
		    edtproductDesc.setText(tb_inaccount.getProductDesc().toString());
		    onloadTime.setText(tb_inaccount.getOnloadTime().toString());
		    //����������Ĭ��ֵ
		    String classID=productDAO.findclass(proID);
		    int position=0;
		    for(int i=0;i<proclassID.length;i++)
		    {
		    	if(classID.equals(proclassID[i]))
		    	{
		    		position=i;
		    		break;
		    	}
		    }
		    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷclassID="+classID+",pos="+position,"log.txt");
		    spinproductclassID.setSelection(position);	   
		}
		
		//ѡ��ͼƬ
		btnImg = (Button)findViewById(R.id.btnImg);  
		btnImg.setOnClickListener(new View.OnClickListener() {
			
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
		
		
		//����
		btnaddProSave = (Button) findViewById(R.id.btnaddProSave);
		btnaddProSave.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) 
		    {
		    	String productID = edtproductID.getText().toString();
		    	String productName = edtproductName.getText().toString();
		    	float marketPrice = Float.parseFloat(edtmarketPrice.getText().toString());
		    	float salesPrice = Float.parseFloat(edtsalesPrice.getText().toString());
		    	int shelfLife= 0;
		    	if(edtshelfLife.getText().toString().isEmpty()!=true)
		    		shelfLife = Integer.parseInt(edtshelfLife.getText().toString());		    	
		    	String productDesc = edtproductDesc.getText().toString();
		    	//��Ʒ���
		    	String strInfo= spinproductclassID.getSelectedItem().toString();
		    	String classID= strInfo.substring(0, strInfo.indexOf('<'));// ��������Ϣ�н�ȡ������
		    	String attBatch1=imgDir;
		    	String attBatch2="";
		    	String attBatch3="";		    	    	
		    	if ((productID.isEmpty()!=true)&&(productName.isEmpty()!=true)
		    			&&(edtmarketPrice.getText().toString().isEmpty()!=true)		    				    			
		    		)
		    	{
		    		try 
		    		{
		    			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+productID+" productName="+productName+" marketPrice="
		    					+marketPrice+" salesPrice="+salesPrice+" shelfLife="+shelfLife+" productDesc="+productDesc+" attBatch1="
		    					+attBatch1+" attBatch2="+attBatch2+" attBatch3="+attBatch3+" classID="+classID,"log.txt");
		    			// ����InaccountDAO����
		    			vmc_productDAO productDAO = new vmc_productDAO(GoodsProSet.this);
			            //����Tb_inaccount����
		    			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(productID, productName,productDesc,marketPrice,
		    					salesPrice,shelfLife,date,date,attBatch1,attBatch2,attBatch3,0,0);
		    			if(proID.isEmpty()==true)
		    			{
		    				productDAO.add(tb_vmc_product,classID);// �����Ʒ��Ϣ
		    				ToolClass.addOptLog(GoodsProSet.this,0,"�����Ʒ:"+productID+productName);
		    			}
		    			else 
		    			{	
		    				productDAO.update(tb_vmc_product,classID);// �޸���Ʒ��Ϣ
		    				ToolClass.addOptLog(GoodsProSet.this,1,"�޸���Ʒ"+productID+productName);
						}
			        	// ������Ϣ��ʾ
			            Toast.makeText(GoodsProSet.this, "��������Ʒ��������ӳɹ���", Toast.LENGTH_SHORT).show();
			            //�˳�ʱ������intent
			            Intent intent=new Intent();
			            intent.putExtra("back", "ok");
			            setResult(GoodsManager.RESULT_OK,intent);
			            finish();
			            
		    		} catch (Exception e)
					{
						// TODO: handle exception
						Toast.makeText(GoodsProSet.this, "�����Ʒʧ�ܣ�", Toast.LENGTH_SHORT).show();
					}		    		
		            
		        } 
		        else
		        {
		            Toast.makeText(GoodsProSet.this, "����д��ɫ���֣�", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
		//�˳�
		btnaddProexit = (Button) findViewById(R.id.btnaddProexit);
		btnaddProexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	//�˳�ʱ������intent
	            Intent intent=new Intent();
	            intent.putExtra("back", "none");
	            setResult(GoodsManager.RESULT_CANCELED,intent);
		        finish();
		    }
		});
		
	}
	
	// ��ʾ��Ʒ������Ϣ
	private void showInfo() 
	{	  
		ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter���� 
		Vmc_ClassAdapter vmc_classAdapter=new Vmc_ClassAdapter();
	    String[] strInfos = vmc_classAdapter.showSpinInfo(GoodsProSet.this);	    
	    // ʹ���ַ��������ʼ��ArrayAdapter����
	    arrayAdapter = new ArrayAdapter<String>(this, R.layout.viewspinner, strInfos);
	    spinproductclassID.setAdapter(arrayAdapter);// ΪListView�б���������Դ
	    proclassID=vmc_classAdapter.getProclassID();
	}
	@Override  
	//ѡȡͼƬ����ֵ
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{  
	     if (resultCode == RESULT_OK) {  
	         uri = data.getData();  
	         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<uri="+ uri.toString(),"log.txt");  
	         ContentResolver cr = this.getContentResolver();  
	         try {  
	             Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
	             /* ��Bitmap�趨��ImageView */  
	             ivProduct.setImageBitmap(bitmap);  
	             imgDir=ToolClass.getRealFilePath(GoodsProSet.this,uri);
	         } catch (FileNotFoundException e) {  
	             Log.e("Exception", e.getMessage(),e);  
	         }  
	     }  
	     super.onActivityResult(requestCode, resultCode, data);  
	 }  
	  
	 
	
}
