package com.example.evconsole;

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
	private int[] layres=new int[]{R.id.tab_class,R.id.tab_product};//��Ƕ�����ļ���id
	private ListView lvinfo;// ����ListView����
	private EditText edtclassid=null,edtclassname=null;
	private Button btnclassadd=null,btnclassupdate=null,btnclassdel=null,btnclassexit=null;// ����Button�����˳���
	// �����ַ������飬�洢ϵͳ����
    private String[] titles = new String[] { "�ֽ��豸����", "��������", "�����Ӳ���", "��Ʒ����", "Ԥ���ӿ�", "Ԥ���ӿ�", "Ԥ���ӿ�", "�˳�","�ֽ��豸����", "��������", "�����Ӳ���", "��Ʒ����", "Ԥ���ӿ�", "Ԥ���ӿ�", "Ԥ���ӿ�", "�˳�" };
    // ����int���飬�洢���ܶ�Ӧ��ͼ��
    private int[] images = new int[] { R.drawable.addoutaccount, R.drawable.addinaccount, R.drawable.outaccountinfo, R.drawable.inaccountinfo,
            R.drawable.showinfo, R.drawable.sysset, R.drawable.accountflag, R.drawable.exit,R.drawable.addoutaccount, R.drawable.addinaccount, R.drawable.outaccountinfo, R.drawable.inaccountinfo,
            R.drawable.showinfo, R.drawable.sysset, R.drawable.accountflag, R.drawable.exit };
    GridView gvProduct=null;
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
    	gvProduct = (GridView) findViewById(R.id.gvProduct);// ��ȡ�����ļ��е�gvInfo���
    	ProPictureAdapter adapter = new ProPictureAdapter(titles, images, this);// ����pictureAdapter����
    	gvProduct.setAdapter(adapter);// ΪGridView��������Դ
        
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
class ProViewHolder {// ����ProViewHolder��

    public TextView title;// ����TextView����
    public ImageView image;// ����ImageView����
}

class ProPicture {// ����ProPicture��

    private String title;// �����ַ�������ʾͼ�����
    private int imageId;// ����int��������ʾͼ��Ķ�����ֵ
	public ProPicture(String title, int imageId) {
		super();
		this.title = title;
		this.imageId = imageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
    
}

class ProPictureAdapter extends BaseAdapter {// ��������BaseAdapter������

    private LayoutInflater inflater;// ����LayoutInflater����
    private List<ProPicture> pictures;// ����List���ͼ���

    // Ϊ�ഴ�����캯��
    public ProPictureAdapter(String[] titles, int[] images, Context context) {
        super();
        pictures = new ArrayList<ProPicture>();// ��ʼ�����ͼ��϶���
        inflater = LayoutInflater.from(context);// ��ʼ��LayoutInflater����
        for (int i = 0; i < images.length; i++)// ����ͼ������
        {
            ProPicture picture = new ProPicture(titles[i], images[i]);// ʹ�ñ����ͼ������ProPicture����
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
            viewHolder.title = (TextView) arg1.findViewById(R.id.proID);// ����ͼ�����
            viewHolder.image = (ImageView) arg1.findViewById(R.id.proImage);// ����ͼ��Ķ�����ֵ
            arg1.setTag(viewHolder);// ������ʾ
        } else {
            viewHolder = (ProViewHolder) arg1.getTag();// ������ʾ
        }
        viewHolder.title.setText(pictures.get(arg0).getTitle());// ����ͼ�����
        viewHolder.image.setImageResource(pictures.get(arg0).getImageId());// ����ͼ��Ķ�����ֵ
        return arg1;// ����ͼ���ʶ
    }
}
