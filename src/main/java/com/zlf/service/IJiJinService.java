package com.zlf.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zlf.bo.JiJinBo;

public interface IJiJinService {
	List<JiJinBo> getPageList(JiJinBo jjb);
	boolean addJiJin(JiJinBo jjb);
	int getTotal(JiJinBo jjb);
	List<Map<String, String>> getJiJinNameCode();
	boolean updataJiJin(JiJinBo jjb);
	Map<String, Object> batchExportJiJin(Workbook wb);
	List<JiJinBo> sumJiJinUpDownInfo(JiJinBo jjb);
}
