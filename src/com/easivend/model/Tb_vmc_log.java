/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_log ������־��Ϣ�� 
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;


public class Tb_vmc_log 
{
	private String logID;// ����ID[pk]
    private int logType;// ��������0���,1�޸�,2ɾ��
    private String logDesc;// ��������
    private String logTime;//����ʱ��
 // �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_log(String logID, int logType, String logDesc, String logTime) {
		super();
		this.logID = logID;
		this.logType = logType;
		this.logDesc = logDesc;
		this.logTime = logTime;
	}
	public String getLogID() {
		return logID;
	}
	public void setLogID(String logID) {
		this.logID = logID;
	}
	public int getLogType() {
		return logType;
	}
	public void setLogType(int logType) {
		this.logType = logType;
	}
	public String getLogDesc() {
		return logDesc;
	}
	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
    
}
