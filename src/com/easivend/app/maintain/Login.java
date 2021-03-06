/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           Login.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        登陆选择串口号页面          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/


package com.easivend.app.maintain;


import java.util.Map;

import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.weixing.WeiConfigAPI;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity 
{
	private EditText txtlogin,txtbent,txtcolumn,txtserver,txtip;// 创建EditText对象
	private TextView tvip=null;
    private Button btnlogin, btnclose,btnGaoji,btnDel;// 创建两个Button对象
    private Switch switchallopen;
    String com =null;
    String bentcom =null;
    String columncom =null;
    String server =null,sercom=null,serip=null;
    int isallopen=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);// 设置布局文件
		//设置横屏还是竖屏的布局策略
		this.setRequestedOrientation(ToolClass.getOrientation());
		switchallopen = (Switch)findViewById(R.id.switchallopen); //获取到控件  
        txtlogin = (EditText) findViewById(R.id.txtLogin);// 获取串口号文本框
        txtbent = (EditText) findViewById(R.id.txtbent);// 获取串口号文本框
        txtcolumn = (EditText) findViewById(R.id.txtcolumn);// 获取串口号文本框
        txtserver = (EditText) findViewById(R.id.txtserver);// 获取服务端公司地址文本框
        txtip = (EditText) findViewById(R.id.txtip);// 获取服务端ip地址文本框
        tvip = (TextView) findViewById(R.id.tvip);
        btnDel = (Button) findViewById(R.id.btnDel);// 获取清除全部商品货道信息
        tvip.setVisibility(View.GONE);
        txtip.setVisibility(View.GONE);
        btnDel.setVisibility(View.GONE);
        Map<String, String> list=ToolClass.ReadConfigFile();
        if(list!=null)
        {
	        com = list.get("com");
	        bentcom = list.get("bentcom");
	        columncom = list.get("columncom");
	        if(list.containsKey("server"))
	        {
	        	server = list.get("server");
	        	sercom=server.substring(server.lastIndexOf('/')+1,server.length());
	        	serip=server.substring(0,server.lastIndexOf('/')+1);
	        }	        
	        if(list.containsKey("isallopen"))
	        {
	        	isallopen=Integer.parseInt(list.get("isallopen"));
	        }
        }
        txtlogin.setText(com);
        txtbent.setText(bentcom);
        txtcolumn.setText(columncom);
        txtserver.setText(sercom);
        txtip.setText(serip);
        switchallopen.setChecked((isallopen==1)?true:false);
        btnlogin = (Button) findViewById(R.id.btnLogin);// 获取修改按钮
        btnclose = (Button) findViewById(R.id.btnClose);// 获取取消按钮
        btnGaoji = (Button) findViewById(R.id.btnGaoji);// 获取高级按钮
        btnclose.setOnClickListener(new OnClickListener() {// 为取消按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                finish();// 退出当前程序
            }
        });
        btnlogin.setOnClickListener(new OnClickListener() {// 为登录按钮设置监听事件
            @Override
            public void onClick(View arg0)
            {
            	com = txtlogin.getText().toString();
    	        bentcom = txtbent.getText().toString(); 
    	        columncom = txtcolumn.getText().toString(); 
    	        isallopen= (switchallopen.isChecked()==true)?1:0;
    	        serip = txtip.getText().toString();
    	        sercom = txtserver.getText().toString(); 
    	        server=serip+sercom;
            	ToolClass.WriteConfigFile(com, bentcom,columncom,server,String.valueOf(isallopen));            	
            	ToolClass.addOptLog(Login.this,1,"修改串口:");
	            // 弹出信息提示
	            Toast.makeText(Login.this, "〖修改串口〗成功！", Toast.LENGTH_SHORT).show();
	            //退出时，返回intent
	            Intent intent=new Intent();
	            setResult(MaintainActivity.RESULT_OK,intent);
            	finish();// 退出当前程序           
            }
        });
        btnGaoji.setOnClickListener(new OnClickListener() {// 为取消按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                if(txtip.getVisibility()==View.GONE)
                {
                	tvip.setVisibility(View.VISIBLE);
                    txtip.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.VISIBLE);
                }
                else
                {
                	tvip.setVisibility(View.GONE);
                    txtip.setVisibility(View.GONE);
                    btnDel.setVisibility(View.GONE);
				}
            }
        });
        btnDel.setOnClickListener(new OnClickListener() {// 为取消按钮设置监听事件
            @Override
            public void onClick(View arg0) {
            	//创建警告对话框
            	Dialog alert=new AlertDialog.Builder(Login.this)
            		.setTitle("对话框")//标题
            		.setMessage("您确定要删除全部商品货道信息吗？")//表示对话框中得内容
            		.setIcon(R.drawable.ic_launcher)//设置logo
            		.setPositiveButton("删除", new DialogInterface.OnClickListener()//退出按钮，点击后调用监听事件
            			{				
        	    				@Override
        	    				public void onClick(DialogInterface dialog, int which) 
        	    				{
        	    					// TODO Auto-generated method stub	
        	    					// 创建InaccountDAO对象
        	    					vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(Login.this);
        				            cabinetDAO.deteleall();// 删除
        				            //删除交易记录
        				            vmc_orderDAO orderDAO = new vmc_orderDAO(Login.this);
        				            orderDAO.deteleall();
        				            ToolClass.addOptLog(Login.this,2,"删除全部商品货道信息");
        	    					// 弹出信息提示
        				            Toast.makeText(Login.this, "删除成功！", Toast.LENGTH_SHORT).show();						            
        				            finish();
        	    				}
            		      }
            			)		    		        
        		        .setNegativeButton("取消", new DialogInterface.OnClickListener()//取消按钮，点击后调用监听事件
        		        	{			
        						@Override
        						public void onClick(DialogInterface dialog, int which) 
        						{
        							// TODO Auto-generated method stub				
        						}
        		        	}
        		        )
        		        .create();//创建一个对话框
        		        alert.show();//显示对话框
            }
        });
	}
	
	@Override
	protected void onDestroy() {
    	//退出时，返回intent
        Intent intent=new Intent();
        setResult(MaintainActivity.RESULT_CANCELED,intent);
		super.onDestroy();		
	}
	
}
