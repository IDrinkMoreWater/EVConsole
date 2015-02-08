/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified String:  2015-01-10
** Last Version:         
** Descriptions:        vmc_machineset�������ñ�           
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created String:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;



public class Tb_vmc_machineset 
{
	private int islogo;// �Ƿ�ʹ��logo��1ʹ��,Ĭ��0
    private int audioWork;// �����տ���������С��0��100
    private String audioWorkstart;//�����տ���ʱ��
    private String audioWorkend;//�����ս���ʱ��
    private int audioSun;// �ڼ��տ���������С��0��100
    private String audioSunstart;//�ڼ��տ���ʱ��
    private String audioSunend;//�ڼ��ս���ʱ��
    private int tempWork;// �����¶ȴ�С��0��50��0.1Ϊ����
    private String tempWorkstart;//�¶ȹ����տ���ʱ��
    private String tempWorkend;//�¶ȹ����ս���ʱ��
    private String tempSunstart;//�¶Ƚڼ��տ���ʱ��
    private String tempSunend;//�¶Ƚڼ��ս���ʱ��
    private String ligntWorkstart;//���������տ���ʱ��
    private String ligntWorkend;//���������ս���ʱ��
    private String ligntSunstart;//�����ڼ��տ���ʱ��
    private String ligntSunend;//�����ڼ��ս���ʱ��
    private String coldWorkstart;//���乤���տ���ʱ��
    private String coldWorkend;//���乤���ս���ʱ��
    private String coldSunstart;//����ڼ��տ���ʱ��
    private String coldSunend;//����ڼ��ս���ʱ��
    private String chouWorkstart;//���������տ���ʱ��
    private String chouWorkend;//���������ս���ʱ��
    private String chouSunstart;//�����ڼ��տ���ʱ��
    private String chouSunend;//�����ڼ��ս���ʱ��
    // �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_machineset(int islogo, int audioWork,
			String audioWorkstart, String audioWorkend, int audioSun,
			String audioSunstart, String audioSunend, int tempWork,
			String tempWorkstart, String tempWorkend, String tempSunstart,
			String tempSunend, String ligntWorkstart, String ligntWorkend,
			String ligntSunstart, String ligntSunend, String coldWorkstart,
			String coldWorkend, String coldSunstart, String coldSunend,
			String chouWorkstart, String chouWorkend, String chouSunstart,
			String chouSunend) {
		super();
		this.islogo = islogo;
		this.audioWork = audioWork;
		this.audioWorkstart = audioWorkstart;
		this.audioWorkend = audioWorkend;
		this.audioSun = audioSun;
		this.audioSunstart = audioSunstart;
		this.audioSunend = audioSunend;
		this.tempWork = tempWork;
		this.tempWorkstart = tempWorkstart;
		this.tempWorkend = tempWorkend;
		this.tempSunstart = tempSunstart;
		this.tempSunend = tempSunend;
		this.ligntWorkstart = ligntWorkstart;
		this.ligntWorkend = ligntWorkend;
		this.ligntSunstart = ligntSunstart;
		this.ligntSunend = ligntSunend;
		this.coldWorkstart = coldWorkstart;
		this.coldWorkend = coldWorkend;
		this.coldSunstart = coldSunstart;
		this.coldSunend = coldSunend;
		this.chouWorkstart = chouWorkstart;
		this.chouWorkend = chouWorkend;
		this.chouSunstart = chouSunstart;
		this.chouSunend = chouSunend;
	}	
	public int getIslogo() {
		return islogo;
	}
	public void setIslogo(int islogo) {
		this.islogo = islogo;
	}
	public int getAudioWork() {
		return audioWork;
	}
	public void setAudioWork(int audioWork) {
		this.audioWork = audioWork;
	}
	public String getAudioWorkstart() {
		return audioWorkstart;
	}
	public void setAudioWorkstart(String audioWorkstart) {
		this.audioWorkstart = audioWorkstart;
	}
	public String getAudioWorkend() {
		return audioWorkend;
	}
	public void setAudioWorkend(String audioWorkend) {
		this.audioWorkend = audioWorkend;
	}
	public int getAudioSun() {
		return audioSun;
	}
	public void setAudioSun(int audioSun) {
		this.audioSun = audioSun;
	}
	public String getAudioSunstart() {
		return audioSunstart;
	}
	public void setAudioSunstart(String audioSunstart) {
		this.audioSunstart = audioSunstart;
	}
	public String getAudioSunend() {
		return audioSunend;
	}
	public void setAudioSunend(String audioSunend) {
		this.audioSunend = audioSunend;
	}
	public int getTempWork() {
		return tempWork;
	}
	public void setTempWork(int tempWork) {
		this.tempWork = tempWork;
	}
	public String getTempWorkstart() {
		return tempWorkstart;
	}
	public void setTempWorkstart(String tempWorkstart) {
		this.tempWorkstart = tempWorkstart;
	}
	public String getTempWorkend() {
		return tempWorkend;
	}
	public void setTempWorkend(String tempWorkend) {
		this.tempWorkend = tempWorkend;
	}
	public String getTempSunstart() {
		return tempSunstart;
	}
	public void setTempSunstart(String tempSunstart) {
		this.tempSunstart = tempSunstart;
	}
	public String getTempSunend() {
		return tempSunend;
	}
	public void setTempSunend(String tempSunend) {
		this.tempSunend = tempSunend;
	}
	public String getLigntWorkstart() {
		return ligntWorkstart;
	}
	public void setLigntWorkstart(String ligntWorkstart) {
		this.ligntWorkstart = ligntWorkstart;
	}
	public String getLigntWorkend() {
		return ligntWorkend;
	}
	public void setLigntWorkend(String ligntWorkend) {
		this.ligntWorkend = ligntWorkend;
	}
	public String getLigntSunstart() {
		return ligntSunstart;
	}
	public void setLigntSunstart(String ligntSunstart) {
		this.ligntSunstart = ligntSunstart;
	}
	public String getLigntSunend() {
		return ligntSunend;
	}
	public void setLigntSunend(String ligntSunend) {
		this.ligntSunend = ligntSunend;
	}
	public String getColdWorkstart() {
		return coldWorkstart;
	}
	public void setColdWorkstart(String coldWorkstart) {
		this.coldWorkstart = coldWorkstart;
	}
	public String getColdWorkend() {
		return coldWorkend;
	}
	public void setColdWorkend(String coldWorkend) {
		this.coldWorkend = coldWorkend;
	}
	public String getColdSunstart() {
		return coldSunstart;
	}
	public void setColdSunstart(String coldSunstart) {
		this.coldSunstart = coldSunstart;
	}
	public String getColdSunend() {
		return coldSunend;
	}
	public void setColdSunend(String coldSunend) {
		this.coldSunend = coldSunend;
	}
	public String getChouWorkstart() {
		return chouWorkstart;
	}
	public void setChouWorkstart(String chouWorkstart) {
		this.chouWorkstart = chouWorkstart;
	}
	public String getChouWorkend() {
		return chouWorkend;
	}
	public void setChouWorkend(String chouWorkend) {
		this.chouWorkend = chouWorkend;
	}
	public String getChouSunstart() {
		return chouSunstart;
	}
	public void setChouSunstart(String chouSunstart) {
		this.chouSunstart = chouSunstart;
	}
	public String getChouSunend() {
		return chouSunend;
	}
	public void setChouSunend(String chouSunend) {
		this.chouSunend = chouSunend;
	}
    
}
