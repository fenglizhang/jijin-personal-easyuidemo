package com.zlf.bo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JiJinBo {
	private Integer id;

	private String jiJinName;
	
	private String jiJinCode;
	
	private Date jiJinTime;

	private Date jiJinStartTime;

	private Date jiJinEndTime;

	private Double dailyIncreases;

	private int page1;
	private int page2;
	
	private String monthNum;	//月份
	private String weekNum;		//周几
	private String upFlag;		//涨跌标记
	
	private int downNum;		//每月周几迭的次数
	private int upNum;			//每月周几涨的次数
	private double monthSum;	//整月涨跌请情况汇总
	private double weekDaySum;	//周几涨跌情况汇总

	
	
	
	/**
	 * @return the monthNum
	 */
	public String getMonthNum() {
		return monthNum;
	}


	/**
	 * @param monthNum the monthNum to set
	 */
	public void setMonthNum(String monthNum) {
		this.monthNum = monthNum;
	}


	/**
	 * @return the weekNum
	 */
	public String getWeekNum() {
		return weekNum;
	}


	/**
	 * @param weekNum the weekNum to set
	 */
	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}


	/**
	 * @return the upFlag
	 */
	public String getUpFlag() {
		return upFlag;
	}


	/**
	 * @param upFlag the upFlag to set
	 */
	public void setUpFlag(String upFlag) {
		this.upFlag = upFlag;
	}


	public double getMonthSum() {
		return monthSum;
	}


	public int getUpNum() {
		return upNum;
	}

	public int getDownNum() {
		return downNum;
	}

	
	public double getWeekDaySum() {
		return weekDaySum;
	}

	
	public void setMonthSum(double monthSum) {
		this.monthSum = monthSum;
	}

	
	public void setUpNum(int upNum) {
		this.upNum = upNum;
	}


	public void setDownNum(int downNum) {
		this.downNum = downNum;
	}

	public void setWeekDaySum(double weekDaySum) {
		this.weekDaySum = weekDaySum;
	}

	public String getJiJinCode() {
		return jiJinCode;
	}

	public void setJiJinCode(String jiJinCode) {
		this.jiJinCode = jiJinCode;
	}
	
	public int getPage1() {
		return page1;
	}
	
	public int getPage2() {
		return page2;
	}

	public void setPage1(int page1) {
		this.page1 = page1;
	}

	
	public void setPage2(int page2) {
		this.page2 = page2;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getJiJinStartTime() {
		return jiJinStartTime;
	}

	public Date getJiJinEndTime() {
		return jiJinEndTime;
	}

	public void setJiJinStartTime(Date jiJinStartTime) {
		this.jiJinStartTime = jiJinStartTime;
	}

	public String getJiJinName() {
		return jiJinName;
	}
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	public Date getJiJinTime() {
		return jiJinTime;
	}


	public Double getDailyIncreases() {
		return dailyIncreases;
	}


	public void setJiJinName(String jiJinName) {
		this.jiJinName = jiJinName;
	}

	public void setJiJinTime(Date jiJinTime) {
//		this.jiJinTime = jiJinTime.substring(0, 10);
		this.jiJinTime = jiJinTime;
	}

	public void setDailyIncreases(Double dailyIncreases) {
		this.dailyIncreases = dailyIncreases;
	}

	public void setJiJinEndTime(Date jiJinEndTime) {
		this.jiJinEndTime = jiJinEndTime;
	}

}