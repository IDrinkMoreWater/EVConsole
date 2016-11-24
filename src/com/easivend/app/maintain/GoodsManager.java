package com.easivend.app.maintain;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ClassAdapter;
import com.easivend.common.Vmc_ProductAdapter;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;
import com.example.evconsole.R;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
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
	//������ʾ�����ݰ�װ
    private List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
    private SimpleAdapter simpleada = null;//�������ݵ�ת������
	private String[] proclassID = null;//������������ͱ��
	private String[] proclassName = null;//�����������������
	private String[] imgDirs=null;
	private EditText edtclassid=null,edtclassname=null;
	private ImageView imgclassname=null;
	private Button btnclassname=null,btnclassadd=null,btnclassupdate=null,btnclassdel=null,btnclassexit=null;// ����Button�����˳���
	private Button btnproadd=null,btnprodel=null,btnprodelselect=null,btnproexit=null;	
	// ������Ʒ�б�
	Vmc_ProductAdapter productAdapter=null;
    private GridView gvProduct=null;
    private TextView txtproidValue=null,txtpronameValue=null;
    private String datasort="shoudong";
    
    		
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.goodsmanager);// ���ò����ļ�
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
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
            	edtclassid.setText(proclassID[position]);
                edtclassname.setText(proclassName[position]);
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
		    	if((ToolClass.isEmptynull(strclassid)!=true)&&(ToolClass.isEmptynull(strclassname)!=true))
		    	{
		    		try 
		    		{
		    			// ����InaccountDAO����
			        	vmc_classDAO classDAO = new vmc_classDAO(GoodsManager.this);
			            // ����Tb_inaccount����
			        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date,imgDir);
			        	classDAO.add(tb_vmc_class);// ���������Ϣ
			        	ToolClass.addOptLog(GoodsManager.this,0,"������:"+strclassid+strclassname);
			        	// ������Ϣ��ʾ
			            Toast.makeText(GoodsManager.this, "���������������ӳɹ���", Toast.LENGTH_SHORT).show();
			            
					} catch (Exception e)
					{
						// TODO: handle exception
						ToolClass.failToast("������ʧ�ܣ�");	
					}			    		
		            showInfo();
		        } 
		        else
		        {
		        	ToolClass.failToast("����������ź����ƣ�");	
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
		    	if((ToolClass.isEmptynull(strclassid)!=true)&&(ToolClass.isEmptynull(strclassname)!=true))
		    	{
		        	// ����InaccountDAO����
		        	vmc_classDAO classDAO = new vmc_classDAO(GoodsManager.this);
		            // ����Tb_inaccount����
		        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date,imgDir);
		        	classDAO.update(tb_vmc_class);// �޸�
		        	ToolClass.addOptLog(GoodsManager.this,1,"�޸����:"+strclassid+strclassname);
		            // ������Ϣ��ʾ
		            Toast.makeText(GoodsManager.this, "���޸���𡽳ɹ���", Toast.LENGTH_SHORT).show();
		            showInfo();
		        } 
		        else
		        {
		        	ToolClass.failToast("����������ź����ƣ�");	
		        }
		    }
		});
    	//ɾ��
    	btnclassdel = (Button) findViewById(R.id.btnclassdel);
    	btnclassdel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0)
		    {
		    	final String strclassid = edtclassid.getText().toString();
		    	final String strclassname = edtclassname.getText().toString();
		    	if((ToolClass.isEmptynull(strclassid)!=true)&&(ToolClass.isEmptynull(strclassname)!=true))
		    	{
			    	//��������Ի���
			    	Dialog alert=new AlertDialog.Builder(GoodsManager.this)
			    		.setTitle("�Ի���")//����
			    		.setMessage("��ȷ��Ҫɾ������Ʒ�����")//��ʾ�Ի����е�����
			    		.setIcon(R.drawable.ic_launcher)//����logo
			    		.setPositiveButton("ɾ��", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
			    			{				
				    				@Override
				    				public void onClick(DialogInterface dialog, int which) 
				    				{
				    		        	// ����InaccountDAO����
				    		        	vmc_classDAO classDAO = new vmc_classDAO(GoodsManager.this);
				    		            // ����Tb_inaccount����
				    		        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date,imgDir);
				    		        	classDAO.detele(tb_vmc_class);// �޸�
				    		        	ToolClass.addOptLog(GoodsManager.this,2,"ɾ�����:"+strclassid+strclassname);
				    		            // ������Ϣ��ʾ
				    		            Toast.makeText(GoodsManager.this, "��ɾ����𡽳ɹ���", Toast.LENGTH_SHORT).show();
				    		            showInfo();
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
		        	ToolClass.failToast("����������ź����ƣ�");	
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
    	txtproidValue = (TextView) findViewById(R.id.txtproidValue);
    	txtpronameValue = (TextView) findViewById(R.id.txtpronameValue);    	  	
    	gvProduct = (GridView) findViewById(R.id.gvProduct);// ��ȡ�����ļ��е�gvInfo���
    	// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������  
    	VmcProductThread vmcProductThread=new VmcProductThread();
    	vmcProductThread.execute();
    	gvProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { 
				// TODO Auto-generated method stub
				String productID[]=productAdapter.getProductID();
				String productName[]=productAdapter.getProductName();
				strInfo = productID[arg2];// ��¼������Ϣ               
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+strInfo,"log.txt");
				txtproidValue.setText(strInfo);
				txtpronameValue.setText(productName[arg2]);
				//��תҳ��
				intent = new Intent();
		    	intent.setClass(GoodsManager.this, GoodsProSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                intent.putExtra("proID", strInfo);
		    	startActivityForResult(intent, REQUEST_CODE);// ��AddInaccount	
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
    	//ɾ��
    	btnprodel = (Button) findViewById(R.id.btnprodel);
    	btnprodel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0)
		    {   if(ToolClass.isEmptynull(txtproidValue.getText().toString())!=true)
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
										ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), GoodsManager.this);
						    			gvProduct.setAdapter(adapter);// ΪGridView��������Դ
						    			ToolClass.addOptLog(GoodsManager.this,2,"ɾ����Ʒ:"+strInfo);
						    			// ������Ϣ��ʾ
				    		            Toast.makeText(GoodsManager.this, "��ɾ����Ʒ���ɹ���", Toast.LENGTH_SHORT).show();
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
		    		ToolClass.failToast("��ѡ����Ҫɾ������Ʒ��");	
		        }
		    }
		});
    	//ɾ����������
    	btnprodelselect = (Button) findViewById(R.id.btnprodelselect);
    	btnprodelselect.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0)
		    {
		    	
		    	//��������Ի���
		    	Dialog alert=new AlertDialog.Builder(GoodsManager.this)
		    		.setTitle("�Ի���")//����
		    		.setMessage("��ȷ��Ҫȫ�����ѡ������������Ϣ��")//��ʾ�Ի����е�����
		    		.setIcon(R.drawable.ic_launcher)//����logo
		    		.setPositiveButton("���", new DialogInterface.OnClickListener()//�˳���ť���������ü����¼�
		    			{				
			    				@Override
			    				public void onClick(DialogInterface dialog, int which) 
			    				{
			    					// TODO Auto-generated method stub	
			    					ToolClass.DelSelectFile();
					    			ToolClass.addOptLog(GoodsManager.this,2,"���ѡ����������");
					    			// ������Ϣ��ʾ
			    		            Toast.makeText(GoodsManager.this, "�����ѡ���������á��ɹ���", Toast.LENGTH_SHORT).show();
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
			//��Ʒ�ĸ�
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvProduct.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
	    	linearParams.height =  screenHeight-252;// ���ؼ��ĸ�ǿ�����75����
	    	gvProduct.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
	    	//��Ʒ����ĸ�
	    	LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) lvinfo.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
	    	linearParams2.height =  screenHeight-700;// ���ؼ��ĸ�ǿ�����75����
	    	lvinfo.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
		}
		//����
		else
		{
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvProduct.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
	    	linearParams.height =  screenHeight-700;// ���ؼ��ĸ�ǿ�����75����
	    	gvProduct.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
	    	LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) lvinfo.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
	    	linearParams2.height =  screenHeight-700;// ���ؼ��ĸ�ǿ�����75����
	    	lvinfo.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
		}
		
	}
	//===============
	//��Ʒ��������ҳ��
	//===============
	// ��ʾ��Ʒ������Ϣ
	private void showInfo() 
	{
		VmcClassThread vmcClassThread=new VmcClassThread();
		vmcClassThread.execute();
	}
	
	//****************
	//�첽�̣߳����ڲ�ѯ��¼
	//****************
	private class VmcClassThread extends AsyncTask<Void,Void,Vmc_ClassAdapter>
	{

		@Override
		protected Vmc_ClassAdapter doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Vmc_ClassAdapter vmc_classAdapter=new Vmc_ClassAdapter();
		    String[] strInfos = vmc_classAdapter.showListInfo(GoodsManager.this);
			return vmc_classAdapter;
		}

		@Override
		protected void onPostExecute(Vmc_ClassAdapter vmc_classAdapter) {
			// TODO Auto-generated method stub
			imgDirs=vmc_classAdapter.getProImage();
		    proclassID=vmc_classAdapter.getProclassID();
			proclassName=vmc_classAdapter.getProclassName();
			int x=0;
			GoodsManager.this.listMap.clear();
			for(x=0;x<proclassID.length;x++)
			{
			  	Map<String,String> map = new HashMap<String,String>();//����Map���ϣ�����ÿһ������
			   	map.put("proclassID", proclassID[x]);
		    	map.put("proclassName", proclassName[x]);	    	
		    	GoodsManager.this.listMap.add(map);//����������
			}
			//��������ܼ��ص�data_list��
			GoodsManager.this.simpleada = new SimpleAdapter(GoodsManager.this,GoodsManager.this.listMap,R.layout.goodsclasslist,
			    		new String[]{"proclassID","proclassName"},//Map�е�key����
			    		new int[]{R.id.txtclassID,R.id.txtclassName});
			GoodsManager.this.lvinfo.setAdapter(GoodsManager.this.simpleada);
		}
				
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
	//****************
	//�첽�̣߳����ڲ�ѯ��¼
	//****************
	private class VmcProductThread extends AsyncTask<Void,Void,Vmc_ProductAdapter>
	{

		@Override
		protected Vmc_ProductAdapter doInBackground(Void... params) {
			// TODO Auto-generated method stub
			productAdapter=new Vmc_ProductAdapter();
	    	productAdapter.showProInfo(GoodsManager.this,"",datasort,""); 
			return productAdapter;
		}

		@Override
		protected void onPostExecute(Vmc_ProductAdapter productAdapter) {
			// TODO Auto-generated method stub
			ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), GoodsManager.this);// ����pictureAdapter����
	    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ	    	
		}
				
	}
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
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒret="+str,"log.txt");
				productAdapter.showProInfo(GoodsManager.this,"",datasort,""); 
				ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), GoodsManager.this);// ����pictureAdapter����
		    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
			}			
		}
		else if(requestCode==REQCLASS_CODE)
		{
			if(resultCode==GoodsManager.RESULT_OK)
			{
				 uri = data.getData();  
		         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<uri="+ uri.toString(),"log.txt");  
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
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
    	//�˳�ʱ������intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}
	
}
