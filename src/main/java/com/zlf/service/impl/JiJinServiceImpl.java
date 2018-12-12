package com.zlf.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zlf.bo.JiJinBo;
import com.zlf.dao.JiJinBoMapper;
import com.zlf.service.IJiJinService;
import com.zlf.utils.Arith;
import com.zlf.utils.DateUtils;
import com.zlf.utils.FileUtils;

@Service
public class JiJinServiceImpl implements IJiJinService {
	private Logger logger=LoggerFactory.getLogger(JiJinServiceImpl.class);
	@Resource
	private JiJinBoMapper jiJinDao;

	@Override
	public List<JiJinBo> getPageList(JiJinBo jjb) {

		return jiJinDao.selectByConditionsAndPage(jjb);
	}

	@Override
	public boolean addJiJin(JiJinBo jjb) {
		int a = 0;

		List<JiJinBo> list = jiJinDao.judgeAddRepeat(jjb);
		if (list != null && list.size() > 0) {
			System.out.println("新增信息重复了");
			logger.info("新增信息重复了！！！");
			
		} else {
			String week=null;
			Date time = jjb.getJiJinTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
			String str = format.format(time);
			String monthNum=DateUtils.getMonthFromDate(time);
			jjb.setMonthNum(monthNum);
			try {
				int weekNum = DateUtils.dayForWeek(str);
				week=String.valueOf(weekNum);
				jjb.setWeekNum(week);
			} catch (Exception e) {
				logger.error("日期类型数据格式化出错，信息："+e.getMessage());
			}
			Double increases = jjb.getDailyIncreases();
			if(increases>0){
				jjb.setUpFlag("1");
			}else if(increases<0){
				jjb.setUpFlag("-1");
			}else{
				jjb.setUpFlag("0");
			}
			
			a = jiJinDao.insertSelective(jjb);
		}
		return a > 0 ? true : false;
	}

	@Override
	public int getTotal(JiJinBo jjb) {
		List<JiJinBo> list = jiJinDao.countSizeByConditions(jjb);
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public List<Map<String, String>> getJiJinNameCode() {
		List<JiJinBo> list = jiJinDao.getJiJinNameList();
		List<Map<String, String>> li = new ArrayList<>();
		if (list != null) {
			for (JiJinBo jiJinBo : list) {
				Map<String, String> map = new HashMap<String, String>();
				String name = jiJinBo.getJiJinName();
				String code = jiJinBo.getJiJinCode();
				map.put("id", code);
				map.put("name", name);
				li.add(map);
			}
		}
		return li;
	}

	@Override
	public boolean updataJiJin(JiJinBo jjb) {
		String week=null;
		Date time = jjb.getJiJinTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		String str = format.format(time);
		String monthNum=DateUtils.getMonthFromDate(time);
		jjb.setMonthNum(monthNum);
		try {
			int weekNum = DateUtils.dayForWeek(str);
			week=String.valueOf(weekNum);
			jjb.setWeekNum(week);
		} catch (Exception e) {
			logger.error("格式成日期类型出错，报错信息："+e.getMessage());
		}
		Double increases = jjb.getDailyIncreases();
		if(increases>0){
			jjb.setUpFlag("1");
		}else if(increases<0){
			jjb.setUpFlag("-1");
		}else{
			jjb.setUpFlag("0");
		}
		int a = jiJinDao.updateByPrimaryKeySelective(jjb);
		return a > 0 ? true : false;
	}

	@Override
	public Map<String, Object> batchExportJiJin(Workbook wb) {
		Sheet sheet = wb.getSheetAt(0);
		boolean fl = false;
		int count = 0;
		// 获取总行数
		int rows = sheet.getPhysicalNumberOfRows();
		if (rows > 2) {
			for (int start = 2; start < rows; start++) {
				// 从第三行开始逐行获取
				Row row = sheet.getRow(start);
				if (row == null) {
					continue;
				}
				JiJinBo jinBo = new JiJinBo();
				for (int i = 0; i < 4; i++) {
					Cell cell = row.getCell(i);
					String cellValue = FileUtils.getCellValue(cell);

					if (i == 0) {
						jinBo.setJiJinName(cellValue);
					}
					if (i == 1) {
						jinBo.setJiJinCode(cellValue);
					}
					if (i == 2) {
						jinBo.setJiJinTime(StringToDate(cellValue));
					}
					if (i == 3) {
						if(cellValue!=null && cellValue.trim()!=""){
							jinBo.setDailyIncreases(Double.parseDouble(cellValue));
						}
					}

				}
				int a = 0;
				if (jinBo.getDailyIncreases() == null
						|| jinBo.getDailyIncreases() == 0) {
						count++;
				} else {
					List<JiJinBo> list = jiJinDao.judgeAddRepeat(jinBo);
					if (list != null && list.size() > 0) {
						System.out.println("新增信息重复了");
						logger.info("新增信息重复了");
					} else {
						String week=null;
						Date time = jinBo.getJiJinTime();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
						String str = format.format(time);
						String monthNum=DateUtils.getMonthFromDate(time);
						jinBo.setMonthNum(monthNum);
						try {
							int weekNum = DateUtils.dayForWeek(str);
							week=String.valueOf(weekNum);
							jinBo.setWeekNum(week);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Double increases = jinBo.getDailyIncreases();
						if(increases>0){
							jinBo.setUpFlag("1");
						}else if(increases<0){
							jinBo.setUpFlag("-1");
						}else{
							jinBo.setUpFlag("0");
						}
						a = jiJinDao.insertSelective(jinBo);
					}
					boolean flag = a > 0 ? true : false;
					if (flag) {
						count++;
					}
				}
			}
			if (count == (rows - 2)) {
				fl = true;
			}
		}

		Map<String, Object> map = new HashMap<>();
		map.put("flag", fl);
		return map;
	}

	private Date StringToDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			logger.error(""+e);
		}
		return null;
	}
	//统计每月，每周涨跌情况
	@Override
	public List<JiJinBo> sumJiJinUpDownInfo(JiJinBo jjb) {
		List<JiJinBo> list = jiJinDao.countSizeByConditions(jjb);
		//先按查询条件判断，是查询单个基金，还是全部基金。两种情况。
		String code=jjb.getJiJinCode();
		if(code!=null && code.trim()!=""){
			//查询单个基金
			return getSumInfo(list);
		}
		return null;
	}
	//获取单个基金的每月每周涨跌统计
	private List<JiJinBo> getSumInfo(List<JiJinBo> li){
		List<JiJinBo> returnLi=new ArrayList<>();
		Set<String> set=new HashSet<>();
		//获取查询结果分为几个月。
		for (JiJinBo jiJinBo : li) {
			set.add(jiJinBo.getMonthNum());
		}
		//根据单月来统计：月涨跌，周几的涨跌次数，每个多个统一周几的涨跌统计
		for (String str : set) {
			double mUpDownCount=0;//统计月涨跌
			//统计每月周一到周五涨跌值参数
			double wc1=0;
			double wc2=0;
			double wc3=0;
			double wc4=0;
			double wc5=0;
			//统计每月周一到周五涨次数
			int wut1=0;
			int wut2=0;
			int wut3=0;
			int wut4=0;
			int wut5=0;
			//统计每月周一到周五迭次数
			int wdt1=0;
			int wdt2=0;
			int wdt3=0;
			int wdt4=0;
			int wdt5=0;
			for (JiJinBo jiJinBo : li) {
				//得到每日涨跌值
				Double di= jiJinBo.getDailyIncreases();
				if(jiJinBo.getMonthNum().equals(str)){
					mUpDownCount=Arith.add(mUpDownCount,di);
				
				if(jiJinBo.getWeekNum().equals("1")){
					if(di>0){
						wut1++;
					}else if(di<0){
						wdt1++;
					}
					wc1=Arith.add(wc1, di);
				}
				if(jiJinBo.getWeekNum().equals("2")){
					if(di>0){
						wut2++;
					}else if(di<0){
						wdt2++;
					}
					wc2=Arith.add(wc2, di);
				}
				if(jiJinBo.getWeekNum().equals("3")){
					if(di>0){
						wut3++;
					}else if(di<0){
						wdt3++;
					}
					wc3=Arith.add(wc3, di);
				}
				if(jiJinBo.getWeekNum().equals("4")){
					if(di>0){
						wut4++;
					}else if(di<0){
						wdt4++;
					}
					wc4=Arith.add(wc4, di);
				}
				if(jiJinBo.getWeekNum().equals("5")){
					if(di>0){
						wut5++;
					}else if(di<0){
						wdt5++;
					}
					wc5=Arith.add(wc5, di);
				}
				}
			}
			//统计处结果
			JiJinBo jinBo1 = new JiJinBo();
			jinBo1.setMonthNum(str);//第几月份
			jinBo1.setMonthSum(mUpDownCount);//每月涨跌值
			jinBo1.setUpNum(wut1);//一个月周一涨的总次数
			jinBo1.setWeekNum("1");
			jinBo1.setDownNum(wdt1);//一个月周一迭的总次数
			jinBo1.setWeekDaySum(wc1);//一个月每周一涨跌值统计
			jinBo1.setJiJinName(li.get(0).getJiJinName());//基金名称
			jinBo1.setJiJinCode(li.get(0).getJiJinCode());//基金代码
			returnLi.add(jinBo1);
			
			JiJinBo jinBo2 = new JiJinBo();
			jinBo2.setMonthNum(str);//第几月份
			jinBo2.setMonthSum(mUpDownCount);//每月涨跌值
			jinBo2.setUpNum(wut2);//一个月周一涨的总次数
			jinBo2.setWeekNum("2");
			jinBo2.setDownNum(wdt2);//一个月周一迭的总次数
			jinBo2.setWeekDaySum(wc2);//一个月每周一涨跌值统计
			jinBo2.setJiJinName(li.get(0).getJiJinName());//基金名称
			jinBo2.setJiJinCode(li.get(0).getJiJinCode());//基金代码
			returnLi.add(jinBo2);
			
			JiJinBo jinBo3 = new JiJinBo();
			jinBo3.setMonthNum(str);//第几月份
			jinBo3.setMonthSum(mUpDownCount);//每月涨跌值
			jinBo3.setUpNum(wut3);//一个月周一涨的总次数
			jinBo3.setWeekNum("3");
			jinBo3.setDownNum(wdt3);//一个月周一迭的总次数
			jinBo3.setWeekDaySum(wc3);//一个月每周一涨跌值统计
			jinBo3.setJiJinName(li.get(0).getJiJinName());//基金名称
			jinBo3.setJiJinCode(li.get(0).getJiJinCode());//基金代码
			returnLi.add(jinBo3);
			
			JiJinBo jinBo4 = new JiJinBo();
			jinBo4.setMonthNum(str);//第几月份
			jinBo4.setMonthSum(mUpDownCount);//每月涨跌值
			jinBo4.setUpNum(wut4);//一个月周一涨的总次数
			jinBo4.setWeekNum("4");
			jinBo4.setDownNum(wdt4);//一个月周一迭的总次数
			jinBo4.setWeekDaySum(wc4);//一个月每周一涨跌值统计
			jinBo4.setJiJinName(li.get(0).getJiJinName());//基金名称
			jinBo4.setJiJinCode(li.get(0).getJiJinCode());//基金代码
			returnLi.add(jinBo4);
			
			JiJinBo jinBo5 = new JiJinBo();
			jinBo5.setMonthNum(str);//第几月份
			jinBo5.setMonthSum(mUpDownCount);//每月涨跌值
			jinBo5.setUpNum(wut5);//一个月周一涨的总次数
			jinBo5.setWeekNum("5");
			jinBo5.setDownNum(wdt5);//一个月周一迭的总次数
			jinBo5.setWeekDaySum(wc5);//一个月每周一涨跌值统计
			jinBo5.setJiJinName(li.get(0).getJiJinName());//基金名称
			jinBo5.setJiJinCode(li.get(0).getJiJinCode());//基金代码
			returnLi.add(jinBo5);
			
			
		}
		return returnLi;
	}
}
