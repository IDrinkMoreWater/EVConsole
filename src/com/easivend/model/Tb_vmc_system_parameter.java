/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_system_parameterϵͳ������           
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;

import java.util.Date;



public class Tb_vmc_system_parameter 
{
	private String devID;// �豸��[PK]
    private String devhCode;// �豸ǩ����
    private int isNet;// �Ƿ�������,1������,0������
    private int isfenClass;// ������ʽ��0�ֶ�������1�Զ����� (Ĭ��0)
    private int isbuyCar;// �Ƿ����ȷ�ϣ�0���ã�1��(Ĭ��0)
    private int liebiaoKuan;//  ǿ�ƹ���0���ã�1ʹ��, (Ĭ��0)
    private String mainPwd;// ά������
    private int amount;// �ֽ𿪹�0�أ�1����Ĭ��0
    private int card;// ��������0�أ�1����Ĭ��0
    private int zhifubaofaca;// ֧�������渶����0�أ�1����Ĭ��0
    private int zhifubaoer;// ֧������ά�뿪��0�أ�1����Ĭ��0
    private int weixing;// ΢�ſ���0�أ�1����Ĭ��0
    private int printer;// ��ӡ������0�أ�1����Ĭ��0
    private int language;// ����ģʽ0���ģ�1Ӣ�ģ�Ĭ��0
    private String rstTime;//����ʱ��
    private int rstDay;// �����������
    private int baozhiProduct;// ��ʾ����������Ʒ0����ʾ��1��ʾ
    private int emptyProduct;// ��ʾ�޻�����Ʒ0����ʾ��1��ʾ    
    private int proSortType;// ��ʾ����ʽ    
    private float marketAmount;// ֽ��Ͷ�ҽ������,�硱20.00��
    private float billAmount;// ���ֽ�����������,�硱20.00��
    // �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_system_parameter(String devID, String devhCode, int isNet,
			int isfenClass, int isbuyCar, int liebiaoKuan, String mainPwd,
			int amount, int card, int zhifubaofaca, int zhifubaoer,
			int weixing, int printer, int language, String rstTime, int rstDay,
			int baozhiProduct, int emptyProduct,int proSortType,float marketAmount,float billAmount) {
		super();
		this.devID = devID;
		this.devhCode = devhCode;
		this.isNet = isNet;
		this.isfenClass = isfenClass;
		this.isbuyCar = isbuyCar;
		this.liebiaoKuan = liebiaoKuan;
		this.mainPwd = mainPwd;
		this.amount = amount;
		this.card = card;
		this.zhifubaofaca = zhifubaofaca;
		this.zhifubaoer = zhifubaoer;
		this.weixing = weixing;
		this.printer = printer;
		this.language = language;
		this.rstTime = rstTime;
		this.rstDay = rstDay;
		this.baozhiProduct = baozhiProduct;
		this.emptyProduct = emptyProduct;
		this.proSortType = proSortType;
		this.marketAmount=marketAmount;
		this.billAmount=billAmount;
	}

	public String getDevID() {
		return devID;
	}

	public void setDevID(String devID) {
		this.devID = devID;
	}

	public String getDevhCode() {
		return devhCode;
	}

	public void setDevhCode(String devhCode) {
		this.devhCode = devhCode;
	}

	public int getIsNet() {
		return isNet;
	}

	public void setIsNet(int isNet) {
		this.isNet = isNet;
	}

	public int getIsfenClass() {
		return isfenClass;
	}

	public void setIsfenClass(int isfenClass) {
		this.isfenClass = isfenClass;
	}

	public int getIsbuyCar() {
		return isbuyCar;
	}

	public void setIsbuyCar(int isbuyCar) {
		this.isbuyCar = isbuyCar;
	}

	public int getLiebiaoKuan() {
		return liebiaoKuan;
	}

	public void setLiebiaoKuan(int liebiaoKuan) {
		this.liebiaoKuan = liebiaoKuan;
	}

	public String getMainPwd() {
		return mainPwd;
	}

	public void setMainPwd(String mainPwd) {
		this.mainPwd = mainPwd;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getCard() {
		return card;
	}

	public void setCard(int card) {
		this.card = card;
	}

	public int getZhifubaofaca() {
		return zhifubaofaca;
	}

	public void setZhifubaofaca(int zhifubaofaca) {
		this.zhifubaofaca = zhifubaofaca;
	}

	public int getZhifubaoer() {
		return zhifubaoer;
	}

	public void setZhifubaoer(int zhifubaoer) {
		this.zhifubaoer = zhifubaoer;
	}

	public int getWeixing() {
		return weixing;
	}

	public void setWeixing(int weixing) {
		this.weixing = weixing;
	}

	public int getPrinter() {
		return printer;
	}

	public void setPrinter(int printer) {
		this.printer = printer;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public String getRstTime() {
		return rstTime;
	}

	public void setRstTime(String rstTime) {
		this.rstTime = rstTime;
	}

	public int getRstDay() {
		return rstDay;
	}

	public void setRstDay(int rstDay) {
		this.rstDay = rstDay;
	}

	public int getBaozhiProduct() {
		return baozhiProduct;
	}

	public void setBaozhiProduct(int baozhiProduct) {
		this.baozhiProduct = baozhiProduct;
	}

	public int getEmptyProduct() {
		return emptyProduct;
	}

	public void setEmptyProduct(int emptyProduct) {
		this.emptyProduct = emptyProduct;
	}

	public int getProSortType() {
		return proSortType;
	}

	public void setProSortType(int proSortType) {
		this.proSortType = proSortType;
	}

	public float getMarketAmount() {
		return marketAmount;
	}

	public void setMarketAmount(float marketAmount) {
		this.marketAmount = marketAmount;
	}

	public float getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(float billAmount) {
		this.billAmount = billAmount;
	}
    
    
    
    
    
}
