package com.easivend.app.maintain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ShowSortAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ClassAdapter;
import com.easivend.common.Vmc_ProductAdapter;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;
import com.example.evconsole.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rasterizer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class GoodsManager extends TabActivity 
{
	private TabHost mytabhost = null;
	Intent intent = null;// ����Intent����
	String strInfo="";
	private final static int REQUEST_CODE=1,REQCLASS_CODE=2;//���������ʶ
	private int[] layres=new int[]{R.id.tab_class,R.id.tab_product};//��Ƕ�����ļ���id
	private ListView lvinfo;// ����ListView����
	private Uri uri=null;
	private String imgDir=null;
	private String[] imgDirs=null;
	private EditText edtclassid=null,edtclassname=null,edtfindProduct=null;
	private ImageView imgclassname=null;
	private Button btnclassname=null,btnclassadd=null,btnclassupdate=null,btnclassdel=null,btnclassexit=null;// ����Button�����˳���
	private Button btnproadd=null,btnproupdate=null,btnprodel=null,btnproexit=null;	
	// ������Ʒ�б�
	Vmc_ProductAdapter productAdapter=null;
    private GridView gvProduct=null;
    //�����йصĶ���
    private ShowSortAdapter showSortAdapter=null;
    private ArrayAdapter<String> arrayadapter = null;
    private Spinner spinprodsort=null;
    private String datasort="shoudong";
    private Button btnsortup=null,btnsortdown=null;
    		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.goodsmanager);// ���ò����ļ�
		this.mytabhost = super.getTabHost();//ȡ��TabHost����
        LayoutInflater.from(this).inflate(R.layout.goodsmanage, this.mytabhost.getTabContentView(),true);
        //����Tab�����
        TabSpec myTabclass=this.mytabhost.newTabSpec("tab0");
    	myTabclass.setIndicator("��Ʒ��������");
    	myTabclass.setContent(this.layres[0]);
    	this.mytabhost.addTab(myTabclass); 
    	
    	TabSpec myTabproduct=this.mytabhost.newTabSpec("tab1");
    	myTabproduct.setIndicator("��Ʒ����");
    	myTabproduct.setContent(this.layres[1]);
    	this.mytabhost.addTab(myTabproduct); 
    	
    	//===============
    	//��Ʒ��������ҳ��
    	//===============
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
    	final String date=df.format(new Date());
    	imgclassname = (ImageView) findViewById(R.id.imgclassname);
    	//ѡ��ͼƬ
    	btnclassname=(Button) findViewById(R.id.btnclassname);
    	btnclassname.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();  
               /* ����Pictures����Type�趨Ϊimage */  
               intent.setType("image/*");  
               /* ʹ��Intent.ACTION_GET_CONTENT���Action */  
               intent.setAction(Intent.ACTION_GET_CONTENT);   
               /* ȡ����Ƭ�󷵻ر����� */  
               startActivityForResult(intent, REQCLASS_CODE);
			}
		}); 
    	// ΪListView�������¼�
    	lvinfo = (ListView) findViewById(R.id.lvclass);// ��ȡ�����ļ��е�ListView���
    	lvinfo.setOnItemClickListener(new OnItemClickListener() 
    	{
            // ��дonItemClick����
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strInfoclass = String.valueOf(((TextView) view).getText());// ��¼������Ϣ
                String strid = strInfoclass.substring(0, strInfoclass.indexOf('<'));// ��������Ϣ�н�ȡ������
                String strname = strInfoclass.substring(strInfoclass.indexOf('>')+1);// ��������Ϣ�н�ȡ������
                edtclassid.setText(strid);
                edtclassname.setText(strname);
                imgDir=imgDirs[position];
                if(imgDir!=null)
                {	                
	                /*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
	    	        Bitmap bitmap = ToolClass.getLoacalBitmap(imgDir); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
	    	        imgclassname.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
                }
                else
                {
                	imgclassname.setImageResource(R.drawable.wutupian);	
				}
            }
        });
    	showInfo();// �����Զ��巽����ʾ��Ʒ������Ϣ
    	edtclassid = (EditText) findViewById(R.id.edtclassid);
    	edtclassname = (EditText) findViewById(R.id.edtclassname);
    	//���
    	btnclassadd = (Button) findViewById(R.id.btnclassadd);
    	btnclassadd.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	String strclassid = edtclassid.getText().toString();
		    	String strclassname = edtclassname.getText().toString();
		    	if ((strclassid.isEmpty()!=true)&&(strclassname.isEmpty()!=true))
		    	{
		    		try 
		    		{
		    			// ����InaccountDAO����
			        	vmc_classDAO classDAO = new vmc_classDAO(GoodsManager.this);
			            // ����Tb_inaccount����
			        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date,imgDir);
			        	classDAO.add(tb_vmc_class);// ���������Ϣ
			        	// ������Ϣ��ʾ
			            Toast.makeText(GoodsManager.this, "���������������ӳɹ���", Toast.LENGTH_SHORT).show();
			            
					} catch (Exception e)
					{
						// TODO: handle exception
						Toast.makeText(GoodsManager.this, "������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
					}			    		
		            showInfo();
		        } 
		        else
		        {
		            Toast.makeText(GoodsManager.this, "����������ź����ƣ�", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
    	//�޸�
    	btnclassupdate = (Button) findViewById(R.id.btnclassupdate);
    	btnclassupdate.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	String strclassid = edtclassid.getText().toString();
		    	String strclassname = edtclassname.getText().toString();
		    	if ((strclassid.isEmpty()!=true)&&(strclassname.isEmpty()!=true))
		    	{
		        	// ����InaccountDAO����
		        	vmc_classDAO classDAO = new vmc_classDAO(GoodsManager.this);
		            // ����Tb_inaccount����
		        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date,imgDir);
		        	classDAO.update(tb_vmc_class);// �޸�
		            // ������Ϣ��ʾ
		            Toast.makeText(GoodsManager.this, "���޸���𡽳ɹ���", Toast.LENGTH_SHORT).show();
		            showInfo();
		        } 
		        else
		        {
		            Toast.makeText(GoodsManager.this, "����������ź����ƣ�", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
    	//ɾ��
    	btnclassdel = (Button) findViewById(R.id.btnclassdel);
    	btnclassdel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0)
		    {
		    	String strclassid = edtclassid.getText().toString();
		    	String strclassname = edtclassname.getText().toString();
		    	if ((strclassid.isEmpty()!=true)&&(strclassname.isEmpty()!=true))
		    	{
		        	// ����InaccountDAO����
		        	vmc_classDAO classDAO = new vmc_classDAO(GoodsManager.this);
		            // ����Tb_inaccount����
		        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date,imgDir);
		        	classDAO.detele(tb_vmc_class);// �޸�
		            // ������Ϣ��ʾ
		            Toast.makeText(GoodsManager.this, "��ɾ����𡽳ɹ���", Toast.LENGTH_SHORT).show();
		            showInfo();
		        } 
		        else
		        {
		            Toast.makeText(GoodsManager.this, "����������ź����ƣ�", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
    	//�˳�
    	btnclassexit = (Button) findViewById(R.id.btnclassexit);
    	btnclassexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
		    }
		});
    	
    	//===============
    	//��Ʒ����ҳ��
    	//===============
    	// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
    	productAdapter=new Vmc_ProductAdapter();
    	productAdapter.showProInfo(this,"",datasort,"");     	
    	gvProduct = (GridView) findViewById(R.id.gvProduct);// ��ȡ�����ļ��е�gvInfo���
    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), this);// ����pictureAdapter����
    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
    	gvProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String productID[]=productAdapter.getProductID();
				strInfo = productID[arg2];// ��¼������Ϣ               
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+strInfo);
//				intent = new Intent();
//		    	intent.setClass(GoodsManager.this, GoodsProSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
//                intent.putExtra("proID", strInfo);
//		    	startActivityForResult(intent, REQUEST_CODE);// ��AddInaccount	
			}// ΪGridView��������¼�
    		
    	});
    	//���
    	btnproadd = (Button) findViewById(R.id.btnproadd);
    	btnproadd.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	 intent = new Intent();
		    	 intent.setClass(GoodsManager.this, GoodsProSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                 intent.putExtra("proID", "");
		    	 startActivityForResult(intent, REQUEST_CODE);// ��AddInaccount	
		    }
		});
    	//�޸�
    	btnproupdate = (Button) findViewById(R.id.btnproupdate);
    	btnproupdate.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	 intent = new Intent();
		    	 intent.setClass(GoodsManager.this, GoodsProSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                 intent.putExtra("proID", strInfo);
		    	 startActivityForResult(intent, REQUEST_CODE);// ��AddInaccount	
		    }
		});
    	//ɾ��
    	btnprodel = (Button) findViewById(R.id.btnprodel);
    	btnprodel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0)
		    {
		    	//��������Ի���
		    	Dialog alert=new AlertDialog.Builder(GoodsManager.this)
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
					    			vmc_productDAO productDAO = new vmc_productDAO(GoodsManager.this);
						            //����Tb_inaccount����
					    			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(strInfo, "","",0,
					    					0,0,date,date,"","","",0,0);				    			
					    			productDAO.detele(tb_vmc_product);// �����Ʒ��Ϣ
					    			productAdapter.showProInfo(GoodsManager.this,"",datasort,""); 
									ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), GoodsManager.this);
					    			gvProduct.setAdapter(adapter);// ΪGridView��������Դ
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
    	//�˳�
    	btnproexit = (Button) findViewById(R.id.btnproexit);
    	btnproexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		        finish();
		    }
		});
    	//����
    	edtfindProduct = (EditText) findViewById(R.id.edtfindProduct);
    	edtfindProduct.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub				
				productAdapter.showProInfo(GoodsManager.this,edtfindProduct.getText().toString(),datasort,""); 
				ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), GoodsManager.this);// ����pictureAdapter����
		    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ				
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
    	btnsortup = (Button) findViewById(R.id.btnsortup);
    	btnsortdown = (Button) findViewById(R.id.btnsortdown);
    	//����
    	this.spinprodsort = (Spinner) super.findViewById(R.id.spinprodsort);
    	// ʹ���ַ��������ʼ��ArrayAdapter����
    	showSortAdapter=new ShowSortAdapter();    	
	    arrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, showSortAdapter.getDataSortName());
	    spinprodsort.setAdapter(arrayadapter);// ΪListView�б���������Դ
    	spinprodsort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			//��ѡ��ı�ʱ����
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				List<String> dataSortID=showSortAdapter.getDataSortID();
				datasort=dataSortID.get(arg2);
				productAdapter.showProInfo(GoodsManager.this,edtfindProduct.getText().toString(),datasort,""); 
				ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), GoodsManager.this);// ����pictureAdapter����
		    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
		    	if(datasort.equals("shoudong"))
		    	{
		    		btnsortup.setEnabled(true);
		    		btnsortdown.setEnabled(true);
		    	}
		    	else
		    	{
		    		btnsortup.setEnabled(false);
		    		btnsortdown.setEnabled(false);
		    	}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	//�����������
    	btnsortup.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	if(strInfo.isEmpty()!=true)
		    	{
			    	// ����InaccountDAO����
	    			vmc_productDAO productDAO = new vmc_productDAO(GoodsManager.this);
		            //����Tb_inaccount����
	    			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(strInfo, "","",0,
	    					0,0,date,date,"","","",0,0);				    			
	    			productDAO.sortupdown(tb_vmc_product,1);// �����Ʒ��Ϣ
	    			productAdapter.showProInfo(GoodsManager.this,"",datasort,""); 
					ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), GoodsManager.this);
	    			gvProduct.setAdapter(adapter);// ΪGridView��������Դ
		    	}
		    }
		});
    	//�����������
    	btnsortdown.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	if(strInfo.isEmpty()!=true)
		    	{
			    	// ����InaccountDAO����
	    			vmc_productDAO productDAO = new vmc_productDAO(GoodsManager.this);
		            //����Tb_inaccount����
	    			Tb_vmc_product tb_vmc_product = new Tb_vmc_product(strInfo, "","",0,
	    					0,0,date,date,"","","",0,0);				    			
	    			productDAO.sortupdown(tb_vmc_product,2);// �����Ʒ��Ϣ
	    			productAdapter.showProInfo(GoodsManager.this,"",datasort,""); 
					ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), GoodsManager.this);
	    			gvProduct.setAdapter(adapter);// ΪGridView��������Դ
		    	}
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
				+"],["+screenHeight+"]");	
		
    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvProduct.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
    	linearParams.height =  screenHeight-700;// ���ؼ��ĸ�ǿ�����75����
    	gvProduct.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
  	   
	}
	//===============
	//��Ʒ��������ҳ��
	//===============
	// ��ʾ��Ʒ������Ϣ
	private void showInfo() 
	{
	    ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter����
	    Vmc_ClassAdapter vmc_classAdapter=new Vmc_ClassAdapter();
	    String[] strInfos = vmc_classAdapter.showListInfo(GoodsManager.this);
	    // ʹ���ַ��������ʼ��ArrayAdapter����
	    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strInfos);
	    lvinfo.setAdapter(arrayAdapter);// ΪListView�б���������Դ
	    imgDirs=vmc_classAdapter.getProImage();
	    
	}
	
	@Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();// ʵ�ֻ����еķ���
        showInfo();// ������ʾ
    }
	//===============
	//��Ʒ����ҳ��
	//===============	
	//����GoodsProSet������Ϣ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE)
		{
			if(resultCode==GoodsManager.RESULT_OK)
			{
				Bundle bundle=data.getExtras();
				String str=bundle.getString("back");
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒret="+str);
				productAdapter.showProInfo(GoodsManager.this,"",datasort,""); 
				ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(), GoodsManager.this);// ����pictureAdapter����
		    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
			}			
		}
		else if(requestCode==REQCLASS_CODE)
		{
			if(resultCode==GoodsManager.RESULT_OK)
			{
				 uri = data.getData();  
		         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<uri="+ uri.toString());  
		         ContentResolver cr = this.getContentResolver();  
		         try {  
		             Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
		             /* ��Bitmap�趨��ImageView */  
		             imgclassname.setImageBitmap(bitmap);  
		             imgDir=ToolClass.getRealFilePath(GoodsManager.this,uri);
		         } catch (FileNotFoundException e) {  
		             Log.e("Exception", e.getMessage(),e);  
		         }  
			}			
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
