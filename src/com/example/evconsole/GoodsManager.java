package com.example.evconsole;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easivend.dao.vmc_classDAO;
import com.easivend.model.Tb_vmc_class;



import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private int[] layres=new int[]{R.id.tab_class,R.id.tab_product};//��Ƕ�����ļ���id
	private ListView lvinfo;// ����ListView����
	private EditText edtclassid=null,edtclassname=null;
	private Button btnclassadd=null,btnclassupdate=null,btnclassdel=null,btnclassexit=null;// ����Button�����˳���
	private Button btnproadd=null,btnproupdate=null,btnprodel=null,btnproexit=null;
	// �����ַ������飬�洢ϵͳ����
    private String[] proID = new String[9];
    private String[] proImage = new String[9];
    private String[] promarket = new String[9];
    private String[] prosales = new String[9];
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
                String strInfo = String.valueOf(((TextView) view).getText());// ��¼������Ϣ
                String strid = strInfo.substring(0, strInfo.indexOf('<'));// ��������Ϣ�н�ȡ������
                String strname = strInfo.substring(strInfo.indexOf('>')+1);// ��������Ϣ�н�ȡ������
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
    	proImage[0]="/sdcard/productimage/449.jpg";
    	proImage[1]="/sdcard/productimage/chaomiandawng.jpg";
    	proImage[2]="/sdcard/productimage/niurouganbanmian.jpg";
    	proImage[3]="/sdcard/productimage/P1070588.jpg";
    	proImage[4]="/sdcard/productimage/P1070589.jpg";
    	proImage[5]="/sdcard/productimage/shimianbafang.jpg";
    	proImage[6]="/sdcard/productimage/xiangnaroujiang.jpg";
    	proImage[7]="/sdcard/productimage/xuebi500ml.jpg";
    	proImage[8]="/sdcard/productimage/yibao.jpg";
    	proID[0]="449.jpg";
    	proID[1]="chaomiandawng.jpg";
    	proID[2]="niurouganbanmian.jpg";
    	proID[3]="P1070588.jpg";
    	proID[4]="P1070589.jpg";
    	proID[5]="shimianbafang.jpg";
    	proID[6]="xiangnaroujiang.jpg";
    	proID[7]="xuebi500ml.jpg";
    	proID[8]="yibao.jpg";
    	promarket[0]="20";
    	promarket[1]="21";
    	promarket[2]="22";
    	promarket[3]="23";
    	promarket[4]="24";
    	promarket[5]="25";
    	promarket[6]="26";
    	promarket[7]="27";
    	promarket[8]="28";
    	prosales[0]="10";
    	prosales[1]="11";
    	prosales[2]="12";
    	prosales[3]="13";
    	prosales[4]="14";
    	prosales[5]="15";
    	prosales[6]="16";
    	prosales[7]="17";
    	prosales[8]="18";
    	
    	gvProduct = (GridView) findViewById(R.id.gvProduct);// ��ȡ�����ļ��е�gvInfo���
    	ProPictureAdapter adapter = new ProPictureAdapter(proID,promarket,prosales,proImage, this);// ����pictureAdapter����
    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
    	//���
    	btnproadd = (Button) findViewById(R.id.btnproadd);
    	btnproadd.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	 intent = new Intent(GoodsManager.this, GoodsProSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                 startActivity(intent);// ��AddInaccount	
		    }
		});
    	//�޸�
    	btnproupdate = (Button) findViewById(R.id.btnproupdate);
    	btnproupdate.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0)
		    {
		    	intent = new Intent(GoodsManager.this, GoodsProSet.class);// ʹ��AddInaccount���ڳ�ʼ��Intent
                startActivity(intent);// ��AddInaccount	
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
	    vmc_classDAO inaccountinfo = new vmc_classDAO(GoodsManager.this);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	    List<Tb_vmc_class> listinfos = inaccountinfo.getScrollData(0, (int) inaccountinfo.getCount());
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
}

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
    
    /**
     * ���ر���ͼƬ
     * @param url
     * @return
     */
     public Bitmap getLoacalBitmap(String url) {
          try {
               FileInputStream fis = new FileInputStream(url);
               return BitmapFactory.decodeStream(fis);  ///����ת��ΪBitmapͼƬ        

            } catch (FileNotFoundException e) {
               e.printStackTrace();
               return null;
          }
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
        Bitmap bitmap = getLoacalBitmap(pictures.get(arg0).getProImage()); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
        viewHolder.image.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
        return arg1;// ����ͼ���ʶ
    }
}


