package com.example.evconsole;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.evprotocol.ToolClass;
import com.easivend.model.Tb_vmc_class;
import com.easivend.model.Tb_vmc_product;



import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rasterizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class GoodsManager extends TabActivity 
{
	private TabHost mytabhost = null;
	Intent intent = null;// ����Intent����
	String strInfo=null;
	private final static int REQUEST_CODE=1;//���������ʶ
	private int[] layres=new int[]{R.id.tab_class,R.id.tab_product};//��Ƕ�����ļ���id
	private ListView lvinfo;// ����ListView����
	private EditText edtclassid=null,edtclassname=null;
	private Button btnclassadd=null,btnclassupdate=null,btnclassdel=null,btnclassexit=null;// ����Button�����˳���
	private Button btnproadd=null,btnproupdate=null,btnprodel=null,btnproexit=null;
	// �����ַ������飬�洢ϵͳ����
    private String[] proID = null,productID = null;
    private String[] proImage = null;
    private String[] promarket = null;
    private String[] prosales = null;
    private GridView gvProduct=null;
    
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
			        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date);
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
		        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date);
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
		        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date);
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
    	showProInfo();
    	
    	gvProduct = (GridView) findViewById(R.id.gvProduct);// ��ȡ�����ļ��е�gvInfo���
    	ProPictureAdapter adapter = new ProPictureAdapter(proID,promarket,prosales,proImage, this);// ����pictureAdapter����
    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
    	gvProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				strInfo = productID[arg2];// ��¼������Ϣ               
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+strInfo);
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
//		    	String strclassid = edtclassid.getText().toString();
//		    	String strclassname = edtclassname.getText().toString();
//		    	if ((strclassid.isEmpty()!=true)&&(strclassname.isEmpty()!=true))
//		    	{
//		        	// ����InaccountDAO����
//		        	vmc_classDAO classDAO = new vmc_classDAO(GoodsManager.this);
//		            // ����Tb_inaccount����
//		        	Tb_vmc_class tb_vmc_class = new Tb_vmc_class(strclassid, strclassname,date);
//		        	classDAO.detele(tb_vmc_class);// �޸�
//		            // ������Ϣ��ʾ
//		            Toast.makeText(GoodsManager.this, "��ɾ����𡽳ɹ���", Toast.LENGTH_SHORT).show();
//		            showInfo();
//		        } 
//		        else
//		        {
//		            Toast.makeText(GoodsManager.this, "����������ź����ƣ�", Toast.LENGTH_SHORT).show();
//		        }
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
        
	}
	//===============
	//��Ʒ��������ҳ��
	//===============
	// ��ʾ��Ʒ������Ϣ
	private void showInfo() 
	{
	    String[] strInfos = null;// �����ַ������飬�����洢������Ϣ
	    ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter����
	    vmc_classDAO classdao = new vmc_classDAO(GoodsManager.this);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	    List<Tb_vmc_class> listinfos = classdao.getScrollData(0, (int) classdao.getCount());
	    strInfos = new String[listinfos.size()];// �����ַ�������ĳ���
	    int m = 0;// ����һ����ʼ��ʶ
	    // ����List���ͼ���
	    for (Tb_vmc_class tb_inaccount : listinfos) 
	    {
	        // �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
	        strInfos[m] = tb_inaccount.getClassID() + "<---|--->" + tb_inaccount.getClassName();
	        m++;// ��ʶ��1
	    }
	    // ʹ���ַ��������ʼ��ArrayAdapter����
	    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strInfos);
	    lvinfo.setAdapter(arrayAdapter);// ΪListView�б���������Դ
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
	// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
	private void showProInfo() 
	{
	    ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter����
	    vmc_productDAO productdao = new vmc_productDAO(GoodsManager.this);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	    List<Tb_vmc_product> listinfos = productdao.getScrollData(0, (int) productdao.getCount());
	    proID = new String[listinfos.size()];// �����ַ�������ĳ���
	    productID = new String[listinfos.size()];// �����ַ�������ĳ���
	    proImage = new String[listinfos.size()];// �����ַ�������ĳ���
	    promarket = new String[listinfos.size()];// �����ַ�������ĳ���
	    prosales = new String[listinfos.size()];// �����ַ�������ĳ���
	    int m = 0;// ����һ����ʼ��ʶ
	    // ����List���ͼ���
	    for (Tb_vmc_product tb_inaccount : listinfos) 
	    {
	        // �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
	    	proID[m] = tb_inaccount.getProductID()+"-"+tb_inaccount.getProductName();
	    	productID[m] = tb_inaccount.getProductID();
	    	proImage[m] = tb_inaccount.getAttBatch1();
	    	promarket[m] = String.valueOf(tb_inaccount.getMarketPrice());
	    	prosales[m] = String.valueOf(tb_inaccount.getSalesPrice());
	    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+proID[m]+" marketPrice="
					+promarket[m]+" salesPrice="+prosales[m]+" attBatch1="
					+proImage[m]+" attBatch2="+tb_inaccount.getAttBatch2()+" attBatch3="+tb_inaccount.getAttBatch3());
	        m++;// ��ʶ��1
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
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒret="+str);
				showProInfo(); 
				ProPictureAdapter adapter = new ProPictureAdapter(proID,promarket,prosales,proImage, this);// ����pictureAdapter����
		    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
			}			
		}
	}
}

//===============
//��Ʒ��������ҳ��
//===============


//===============
//��Ʒ����ҳ��
//===============
class ProViewHolder {// ����ProViewHolder���ſؼ�����

    public TextView proID;// ������ƷID������
    public ImageView image;// ����ImageView����
    public TextView promarket;// ������Ʒԭ��
    public TextView prosales;// ������Ʒ���ۼ�
}

class ProPicture {// ����ProPicture��

    private String proID;// �����ַ�������ʾͼ�����
    private String proImage;//ͼ��λ��
    private String promarket;//ԭ��
    private String prosales;//�ּ�
	public ProPicture(String proID, String promarket, String prosales,String proImage) {
		super();
		this.proID = proID;
		this.promarket = promarket;
		this.prosales = prosales;
		this.proImage = proImage;
		
	}
	public String getTitle() {
		return proID;
	}
	public void setTitle(String proID) {
		this.proID = proID;
	}
	public String getProImage() {
		return proImage;
	}
	public void setProImage(String proImage) {
		this.proImage = proImage;
	}
	public String getPromarket() {
		return promarket;
	}
	public void setPromarket(String promarket) {
		this.promarket = promarket;
	}
	public String getProsales() {
		return prosales;
	}
	public void setProsales(String prosales) {
		this.prosales = prosales;
	}
    
}

class ProPictureAdapter extends BaseAdapter {// ��������BaseAdapter������

    private LayoutInflater inflater;// ����LayoutInflater����
    private List<ProPicture> pictures;// ����List���ͼ���

    // Ϊ�ഴ�����캯��
    public ProPictureAdapter(String[] proID, String[] promarket,String[] prosales,String[] proImage, Context context) {
        super();
        pictures = new ArrayList<ProPicture>();// ��ʼ�����ͼ��϶���
        inflater = LayoutInflater.from(context);// ��ʼ��LayoutInflater����
        for (int i = 0; i < proImage.length; i++)// ����ͼ������
        {
            ProPicture picture = new ProPicture(proID[i],promarket[i],prosales[i], proImage[i]);// ʹ�ñ����ͼ������ProPicture����
            pictures.add(picture);// ��Picture������ӵ����ͼ�����
        }
    }

    @Override
    public int getCount() {// ��ȡ���ͼ��ϵĳ���
        if (null != pictures) {// ������ͼ��ϲ�Ϊ��
            return pictures.size();// ���ط��ͳ���
        } else {
            return 0;// ����0
        }
    }

    @Override
    public Object getItem(int arg0) {
        return pictures.get(arg0);// ��ȡ���ͼ���ָ������������
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;// ���ط��ͼ��ϵ�����
    }
    
    
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ProViewHolder viewHolder;// ����ProViewHolder����
        if (arg1 == null) {// �ж�ͼ���ʶ�Ƿ�Ϊ��

            arg1 = inflater.inflate(R.layout.productgv, null);// ����ͼ���ʶ
            viewHolder = new ProViewHolder();// ��ʼ��ProViewHolder����
            viewHolder.proID = (TextView) arg1.findViewById(R.id.proID);// ����ͼ�����
            viewHolder.image = (ImageView) arg1.findViewById(R.id.proImage);// ����ͼ��Ķ�����ֵ
            viewHolder.promarket = (TextView) arg1.findViewById(R.id.promarket);// ����ͼ�����
            viewHolder.prosales = (TextView) arg1.findViewById(R.id.prosales);// ����ͼ�����
            
            arg1.setTag(viewHolder);// ������ʾ
        } 
        else
        {
            viewHolder = (ProViewHolder) arg1.getTag();// ������ʾ
        }
        
        viewHolder.proID.setText(pictures.get(arg0).getTitle());// ����ͼ��ID
        viewHolder.promarket.setText("ԭ��:"+pictures.get(arg0).getPromarket());// ����ͼ��ԭ��
        viewHolder.prosales.setText("�ּ�:"+pictures.get(arg0).getProsales());// ����ͼ��ԭ��
        /*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
        Bitmap bitmap = ToolClass.getLoacalBitmap(pictures.get(arg0).getProImage()); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
        viewHolder.image.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
        return arg1;// ����ͼ���ʶ
    }
}


