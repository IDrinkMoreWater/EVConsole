package com.example.evconsole;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.evprotocol.ToolClass;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsProSet extends Activity {
	private Uri uri=null;
	private ImageView ivProduct=null;
	private Button btnImg=null,btnaddProSave=null,btnaddProexit=null;
	private EditText edtproductID=null,edtproductName=null,edtmarketPrice=null,edtsalesPrice=null,
			edtshelfLife=null,edtproductDesc=null;
	private TextView onloadTime=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		    	int shelfLife = Integer.parseInt(edtshelfLife.getText().toString());
		    	String productDesc = edtproductDesc.getText().toString();
		    	//String attBatch1=uri.toString();
		    	String attBatch1=ToolClass.getRealFilePath(GoodsProSet.this,uri);
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
		    					+attBatch1+" attBatch2="+attBatch2+" attBatch3="+attBatch3);
		    			// ����InaccountDAO����
		    			vmc_productDAO productDAO = new vmc_productDAO(GoodsProSet.this);
			            //����Tb_inaccount����
		    			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(productID, productName,productDesc,marketPrice,
		    					salesPrice,shelfLife,date,date,attBatch1,attBatch2,attBatch3,0,0);
		    			productDAO.add(tb_vmc_product);// ���������Ϣ
			        	// ������Ϣ��ʾ
			            Toast.makeText(GoodsProSet.this, "��������Ʒ��������ӳɹ���", Toast.LENGTH_SHORT).show();
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
		        finish();
		    }
		});
		
	}
	
	  @Override  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	     if (resultCode == RESULT_OK) {  
	         uri = data.getData();  
	         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<uri="+ uri.toString());  
	         ContentResolver cr = this.getContentResolver();  
	         try {  
	             Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
	             /* ��Bitmap�趨��ImageView */  
	             ivProduct.setImageBitmap(bitmap);  
	         } catch (FileNotFoundException e) {  
	             Log.e("Exception", e.getMessage(),e);  
	         }  
	     }  
	     super.onActivityResult(requestCode, resultCode, data);  
	 }  
	  
	 
	
}
