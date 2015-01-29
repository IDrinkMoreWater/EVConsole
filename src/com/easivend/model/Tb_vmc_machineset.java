/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_machineset�������ñ�           
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;

import java.util.Date;

public class Tb_vmc_machineset 
{
	private int machID;// [pk]
    private int islogo;// �Ƿ�ʹ��logo��1ʹ��,Ĭ��0
    private int audioWork;// �����տ���������С��0��100
    private Date audioWorkstart;//�����տ���ʱ��
    private Date audioWorkend;//�����ս���ʱ��
    private int audioSun;// �ڼ��տ���������С��0��100
    private Date audioSunstart;//�ڼ��տ���ʱ��
    private Date audioSunend;//�ڼ��ս���ʱ��
    private int tempWork;// �����¶ȴ�С��0��50��0.1Ϊ����
    private Date tempWorkstart;//�¶ȹ����տ���ʱ��
    private Date tempWorkend;//�¶ȹ����ս���ʱ��
    private Date tempSunstart;//�¶Ƚڼ��տ���ʱ��
    private Date tempSunend;//�¶Ƚڼ��ս���ʱ��
    private Date ligntWorkstart;//���������տ���ʱ��
    private Date ligntWorkend;//���������ս���ʱ��
    private Date ligntSunstart;//�����ڼ��տ���ʱ��
    private Date ligntSunend;//�����ڼ��ս���ʱ��
    private Date coldWorkstart;//���乤���տ���ʱ��
    private Date coldWorkend;//���乤���ս���ʱ��
    private Date coldSunstart;//����ڼ��տ���ʱ��
    private Date coldSunend;//����ڼ��ս���ʱ��
    private Date chouWorkstart;//���������տ���ʱ��
    private Date chouWorkend;//���������ս���ʱ��
    private Date chouSunstart;//�����ڼ��տ���ʱ��
    private Date chouSunend;//�����ڼ��ս���ʱ��
    // �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_machineset(int machID, int islogo, int audioWork,
			Date audioWorkstart, Date audioWorkend, int audioSun,
			Date audioSunstart, Date audioSunend, int tempWork,
			Date tempWorkstart, Date tempWorkend, Date tempSunstart,
			Date tempSunend, Date ligntWorkstart, Date ligntWorkend,
			Date ligntSunstart, Date ligntSunend, Date coldWorkstart,
			Date coldWorkend, Date coldSunstart, Date coldSunend,
			Date chouWorkstart, Date chouWorkend, Date chouSunstart,
			Date chouSunend) {
		super();
		this.machID = machID;
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
	public int getMachID() {
		return machID;
	}
	public void setMachID(int machID) {
		this.machID = machID;
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
	public Date getAudioWorkstart() {
		return audioWorkstart;
	}
	public void setAudioWorkstart(Date audioWorkstart) {
		this.audioWorkstart = audioWorkstart;
	}
	public Date getAudioWorkend() {
		return audioWorkend;
	}
	public void setAudioWorkend(Date audioWorkend) {
		this.audioWorkend = audioWorkend;
	}
	public int getAudioSun() {
		return audioSun;
	}
	public void setAudioSun(int audioSun) {
		this.audioSun = audioSun;
	}
	public Date getAudioSunstart() {
		return audioSunstart;
	}
	public void setAudioSunstart(Date audioSunstart) {
		this.audioSunstart = audioSunstart;
	}
	public Date getAudioSunend() {
		return audioSunend;
	}
	public void setAudioSunend(Date audioSunend) {
		this.audioSunend = audioSunend;
	}
	public int getTempWork() {
		return tempWork;
	}
	public void setTempWork(int tempWork) {
		this.tempWork = tempWork;
	}
	public Date getTempWorkstart() {
		return tempWorkstart;
	}
	public void setTempWorkstart(Date tempWorkstart) {
		this.tempWorkstart = tempWorkstart;
	}
	public Date getTempWorkend() {
		return tempWorkend;
	}
	public void setTempWorkend(Date tempWorkend) {
		this.tempWorkend = tempWorkend;
	}
	public Date getTempSunstart() {
		return tempSunstart;
	}
	public void setTempSunstart(Date tempSunstart) {
		this.tempSunstart = tempSunstart;
	}
	public Date getTempSunend() {
		return tempSunend;
	}
	public void setTempSunend(Date tempSunend) {
		this.tempSunend = tempSunend;
	}
	public Date getLigntWorkstart() {
		return ligntWorkstart;
	}
	public void setLigntWorkstart(Date ligntWorkstart) {
		this.ligntWorkstart = ligntWorkstart;
	}
	public Date getLigntWorkend() {
		return ligntWorkend;
	}
	public void setLigntWorkend(Date ligntWorkend) {
		this.ligntWorkend = ligntWorkend;
	}
	public Date getLigntSunstart() {
		return ligntSunstart;
	}
	public void setLigntSunstart(Date ligntSunstart) {
		this.ligntSunstart = ligntSunstart;
	}
	public Date getLigntSunend() {
		return ligntSunend;
	}
	public void setLigntSunend(Date ligntSunend) {
		this.ligntSunend = ligntSunend;
	}
	public Date getColdWorkstart() {
		return coldWorkstart;
	}
	public void setColdWorkstart(Date coldWorkstart) {
		this.coldWorkstart = coldWorkstart;
	}
	public Date getColdWorkend() {
		return coldWorkend;
	}
	public void setColdWorkend(Date coldWorkend) {
		this.coldWorkend = coldWorkend;
	}
	public Date getColdSunstart() {
		return coldSunstart;
	}
	public void setColdSunstart(Date coldSunstart) {
		this.coldSunstart = coldSunstart;
	}
	public Date getColdSunend() {
		return coldSunend;
	}
	public void setColdSunend(Date coldSunend) {
		this.coldSunend = coldSunend;
	}
	public Date getChouWorkstart() {
		return chouWorkstart;
	}
	public void setChouWorkstart(Date chouWorkstart) {
		this.chouWorkstart = chouWorkstart;
	}
	public Date getChouWorkend() {
		return chouWorkend;
	}
	public void setChouWorkend(Date chouWorkend) {
		this.chouWorkend = chouWorkend;
	}
	public Date getChouSunstart() {
		return chouSunstart;
	}
	public void setChouSunstart(Date chouSunstart) {
		this.chouSunstart = chouSunstart;
	}
	public Date getChouSunend() {
		return chouSunend;
	}
	public void setChouSunend(Date chouSunend) {
		this.chouSunend = chouSunend;
	}
    
}
