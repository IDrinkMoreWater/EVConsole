/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_class ��Ʒ����           
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;

import java.util.Date;

public class Tb_vmc_class 
{
	private String classID;// ��Ʒ���ID
	private String className;// ��Ʒ�������	
	private String classTime;//������ʱ��
	// �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_class(String classID, String className,String classTime) 
	{
		super();
		this.classID = classID;
		this.className = className;		
		this.classTime = classTime;
	}
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}	
	public String getClassTime() {
		return classTime;
	}
	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}
	
}
