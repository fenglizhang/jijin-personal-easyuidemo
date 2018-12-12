package com.zlf.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zlf.bo.JiJinBo;
import com.zlf.service.IJiJinService;
import com.zlf.utils.FileUtils;

@Controller
@RequestMapping("/jijin")
public class JiJinController {

	@Resource
	private IJiJinService jiJinService;
	
	@RequestMapping("/showJiJin")
	public String returnJsp(HttpServletRequest request){
		HttpSession session = request.getSession();
		List<Map<String, String>> list = jiJinService.getJiJinNameCode();
		session.setAttribute("jiJinName", list);
		return "jijin";
	}
	//查询方法
	@RequestMapping("/getList")
	@ResponseBody
	public Map<String,Object> searchJiJinList(HttpServletRequest request,HttpServletResponse response){
	System.out.println("---------------------");
		String jiJinCode=request.getParameter("jiJinCode");
		String startTime=request.getParameter("jiJinStartTime");
		String endTime=request.getParameter("jiJinEndTime");
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		JiJinBo jjb=new JiJinBo();
		if(jiJinCode!=null && jiJinCode.trim() !=""){
			jjb.setJiJinCode(jiJinCode);
		}
		if(startTime!=null &&startTime.trim() !=""){
			jjb.setJiJinStartTime(StringToDate(startTime));
		}
		if(endTime!=null &&endTime.trim() !=""){
			jjb.setJiJinEndTime(StringToDate(endTime));
		}
		int page1=(Integer.parseInt(rows))*(Integer.parseInt(page)-1);
		int page2=Integer.parseInt(rows);
		jjb.setPage1(page1);
		jjb.setPage2(page2);
		List<JiJinBo> list = jiJinService.getPageList(jjb);
		int total = jiJinService.getTotal(jjb);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		return map;
	}
	
	
	//新增方法
	@RequestMapping("/addJiJin")
	@ResponseBody
	public Map<String,Object> addJiJin(HttpServletRequest request) throws UnsupportedEncodingException{
		String name=new String(request.getParameter("eJiJinName").getBytes(),"UTF-8");
		String code=request.getParameter("eJiJinCode");
		String time=request.getParameter("eJiJinTime");
		String num=request.getParameter("eDailyIncreases");
		JiJinBo jjb=new JiJinBo();
		jjb.setJiJinName(name);
		jjb.setDailyIncreases(Double.parseDouble(num));
		jjb.setJiJinTime(StringToDate(time));
		jjb.setJiJinCode(code);
		boolean flag = jiJinService.addJiJin(jjb);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("flag",flag);
		return map;
	}
	
	//修改的方法
	@RequestMapping("/updateJiJinInfo")
	@ResponseBody
	public Map<String,Object> updateJiJinInfo(HttpServletRequest request) throws UnsupportedEncodingException{
		String name=new String(request.getParameter("eJiJinName").getBytes(),"UTF-8");
		String code=request.getParameter("eJiJinCode");
		String time=request.getParameter("eJiJinTime");
		String num=request.getParameter("eDailyIncreases");
		String id=request.getParameter("jiJinId");
		JiJinBo jjb=new JiJinBo();
		jjb.setJiJinName(name);
		jjb.setDailyIncreases(Double.parseDouble(num));
		jjb.setJiJinTime(StringToDate(time));
		jjb.setJiJinCode(code);
		jjb.setId(Integer.valueOf(id));
		boolean flag = jiJinService.updataJiJin(jjb);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("flag", flag);
		return map;
	}
	
	//下载模板
	@RequestMapping("/downloadModel")
	public void downloadExcel(HttpServletRequest request,HttpServletResponse response){
		String path=request.getSession().getServletContext().getRealPath("excelModel");
		path=path.replace("\\", "/");
		if(!path.endsWith("/")){
			path+="/";
		}
		File file=new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName="基金信息导入模板.xlsx";
		String strFileName=path+fileName;
		FileUtils.downloadFiles(response, strFileName);
	}
	//批量新增，文件解析
	@RequestMapping("/fileUpload")
	public void fileUpload(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out=response.getWriter();
		Workbook wb = FileUtils.getWorkbookFromRequest(request,response);
		Map<String, Object> map = jiJinService.batchExportJiJin(wb);
		JSONObject json = new JSONObject(map);
		String jsonString = json.toJSONString();
		out.print(jsonString);
		out.flush();
		out.close();
	}
	
//	统计基金信息
	@RequestMapping("/sumInfo")
	@ResponseBody
	public Map<String, Object> sumJiJinInfo(HttpServletRequest request){
		String jiJinCode=request.getParameter("jiJinCode");
		String startTime=request.getParameter("jiJinStartTime");
		String endTime=request.getParameter("jiJinEndTime");
		JiJinBo jjb=new JiJinBo();
		if(jiJinCode!=null && jiJinCode.trim() !=""){
			jjb.setJiJinCode(jiJinCode);
		}
		if(startTime!=null &&startTime.trim() !=""){
			jjb.setJiJinStartTime(StringToDate(startTime));
		}
		if(endTime!=null &&endTime.trim() !=""){
			jjb.setJiJinEndTime(StringToDate(endTime));
		}
		
		List<JiJinBo> list = jiJinService.sumJiJinUpDownInfo(jjb);
		
		
		Map<String, Object> map=new HashMap<>();
		map.put("rows", list);
		return map;
	}
	
	
	private Date StringToDate(String time) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
