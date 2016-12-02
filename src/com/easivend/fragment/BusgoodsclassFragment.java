package com.easivend.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.easivend.common.ClassPictureAdapter;
import com.easivend.common.Vmc_ClassAdapter;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class BusgoodsclassFragment extends Fragment 
{
	GridView gvbusgoodsclass=null;
	ImageView imgbtnbusgoodsback=null;
	Button btnreturn=null;
	private Context context;
	//ɨ��
    private String editstr="";
	private int editread=0;
	EditText editTextTimeCOMA;
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
	//=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BusgoodsclassFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BusgoodsclassFragInteraction)
        {
            listterner = (BusgoodsclassFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BusgoodsclassFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BusgoodsclassFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        void BusgoodsclassSwitch(Map<String, String> str);//�л���Busgoodsҳ��
        void BusgoodsclassFinish();      //�л���businessҳ��
    }
    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_busgoodsclass, container, false);  
		context=this.getActivity();//��ȡactivity��context
		//ɨ��ģ��
		editTextTimeCOMA=(EditText)view.findViewById(R.id.editTextTimeCOMA);
        editTextTimeCOMA.setInputType(InputType.TYPE_NULL);  
        editTextTimeCOMA.setFocusable(true);
		editTextTimeCOMA.setFocusableInTouchMode(true);
		editTextTimeCOMA.requestFocus();
		editTextTimeCOMA.setText("");
		editTextTimeCOMA.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				editstr=s.toString().trim();	
				//Log.i("EV_JNI","String s="+editstr);
				editread=100;
			}
		});
        timer.scheduleWithFixedDelay(task, 100, 10, TimeUnit.MILLISECONDS);
		gvbusgoodsclass=(GridView) view.findViewById(R.id.gvbusgoodsclass); 
		Vmc_ClassAdapter vmc_classAdapter=new Vmc_ClassAdapter();
	    String[] strInfos = vmc_classAdapter.showSpinInfo(context);
	    ClassPictureAdapter adapter = new ClassPictureAdapter(vmc_classAdapter.getProclassName(),vmc_classAdapter.getProImage(),context);// ����pictureAdapter����
	    final String proclassID[]=vmc_classAdapter.getProclassID();
	    gvbusgoodsclass.setAdapter(adapter);// ΪGridView��������Դ	
	    gvbusgoodsclass.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Intent intent = null;// ����Intent����
            	Map<String, String>str=new HashMap<String, String>();
                switch (arg2) {
                case 0:
//                	intent = new Intent(BusgoodsClass.this, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//                	intent.putExtra("proclassID", "");
//                	startActivity(intent);// ��Accountflag                	
    	        	str.put("proclassID", "");
                	listterner.BusgoodsclassSwitch(str);//�������fragment��activity���ͻص���Ϣ
                    break;
                default:
//                	intent = new Intent(BusgoodsClass.this, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//                	intent.putExtra("proclassID", proclassID[arg2]);
//                	startActivity(intent);// ��Accountflag
                	str.put("proclassID", proclassID[arg2]);
                	listterner.BusgoodsclassSwitch(str);//�������fragment��activity���ͻص���Ϣ
                    break;                
                }
            }
        });
	    imgbtnbusgoodsback=(ImageView)view.findViewById(R.id.imgbtnbusgoodsback);
	    imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	listterner.BusgoodsclassFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});	 
	    btnreturn=(Button)view.findViewById(R.id.btnreturn);
	    btnreturn.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	listterner.BusgoodsclassFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});	 
		return view;
	}
	//���õ���ʱ��ʱ��
    TimerTask task = new TimerTask() { 
    	@Override 
        public void run() { 
  
    		((Activity)context).runOnUiThread(new Runnable() {      // UI thread 
		         @Override 
		        public void run()
		        { 
		        	 if(editread>0)
		        		 editread--;
		        	 if(editread==0)
		        	 {
		        		 if(editstr.equals("")==false)
		        		 {
		        			 editstr="";
		        			 editTextTimeCOMA.setText("");
		        			 editTextTimeCOMA.setFocusable(true);
		     				 editTextTimeCOMA.setFocusableInTouchMode(true);
		     				 editTextTimeCOMA.requestFocus();  
		        		 }
		        	 }
		        } 
            });
        }     	    
    };
}
