/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_order_product ������ϸ��Ϣ��      
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;

public class Tb_vmc_order_product 
{
	private String orderID;// ����ID
    private String productID;// ��ƷID
    private int yujiHuo;//Ԥ�Ƴ���:����1��
    private int realHuo;//ʵ�ʳ���: 1����0
    private String cabID;//�����
    private String columnID;//������
    private int huoStatus;//����״̬: 0�����ɹ���1����ʧ��
    // �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
    public Tb_vmc_order_product(String orderID, String productID, int yujiHuo,
    		int realHuo, String cabID, String columnID, int huoStatus) 
	{
		super();
		this.orderID = orderID;
		this.productID = productID;
		this.yujiHuo = yujiHuo;
		this.realHuo = realHuo;
		this.cabID = cabID;
		this.columnID = columnID;
		this.huoStatus = huoStatus;
	}
	public String getOrderID() {
		return orderID;
	}
	
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public int getYujiHuo() {
		return yujiHuo;
	}
	public void setYujiHuo(int yujiHuo) {
		this.yujiHuo = yujiHuo;
	}
	public int getRealHuo() {
		return realHuo;
	}
	public void setRealHuo(int realHuo) {
		this.realHuo = realHuo;
	}
	public String getCabID() {
		return cabID;
	}
	public void setCabID(String cabID) {
		this.cabID = cabID;
	}
	public String getColumnID() {
		return columnID;
	}
	public void setColumnID(String columnID) {
		this.columnID = columnID;
	}
	public int getHuoStatus() {
		return huoStatus;
	}
	public void setHuoStatus(int huoStatus) {
		this.huoStatus = huoStatus;
	}
    
}
