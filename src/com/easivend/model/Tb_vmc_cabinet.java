/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_cabinet �����ͱ�          
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;

public class Tb_vmc_cabinet 
{
	private String cabID;// �����[pk]
    private int cabType;// ��������,0�޻���,1���ɻ�����2 ��ʽ����̨��3 ����̨+���ʹ���4 ����̨+���ɣ�5���ӹ�
 // �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_cabinet(String cabID, int cabType) {
		super();
		this.cabID = cabID;
		this.cabType = cabType;
	}
	public String getCabID() {
		return cabID;
	}
	public void setCabID(String cabID) {
		this.cabID = cabID;
	}
	public int getCabType() {
		return cabType;
	}
	public void setCabType(int cabType) {
		this.cabType = cabType;
	}
    
}
