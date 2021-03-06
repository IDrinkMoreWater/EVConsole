/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_system_parameter系统参数表           
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
	private String devID;// 设备号[PK]
    private String devhCode;// 设备签到码
    private int isNet;// 是否联网版,1联网版,0单机版
    private int isfenClass;// 补货方式，0手动补货，1自动补货 (默认0)
    private int isbuyCar;// 是否出货确认，0不用，1用(默认0)
    private int liebiaoKuan;//  强制购买0不用，1使用, (默认0)
    private String mainPwd;// 维护密码
    private int amount;// 现金开关0关，1开，默认0
    private int card;// 银联开关0关，1开，默认0
    private int zhifubaofaca;// 支付宝当面付开关0关，1开，默认0
    private int zhifubaoer;// 支付宝二维码开关0关，1开，默认0
    private int weixing;// 微信开关0关，1开，默认0
    private int printer;// 打印机开关0关，1开，默认0
    private int language;// 语言模式0中文，1英文，默认0
    private String rstTime;//重启时间
    private int rstDay;// 重启间隔天数
    private int baozhiProduct;// 显示过保质期商品0不显示，1显示
    private int emptyProduct;// 显示无货的商品0不显示，1显示    
    private int proSortType;// 显示排序方式    
    private float marketAmount;// 纸币投币金额上限,如”20.00”
    private float billAmount;// 最低纸币器开启金额,如”20.00”
    // 定义有参构造函数，用来初始化收入信息实体类中的各个字段
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
