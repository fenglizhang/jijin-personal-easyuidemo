package com.zlf.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class FileUtils {
	private static Logger logger=LoggerFactory.getLogger(FileUtils.class);
	/**
	 * 【本地、本应用】文件下载
	 * 
	 * @param response
	 * @param filePath
	 *            文件路径
	 */
	public static void downloadFiles(HttpServletResponse response,
			String filePath) {
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("UTF-8");
		FileInputStream fs = null;
		BufferedInputStream buff = null;
		OutputStream myout = null;

		try {
			File file = new File(filePath.trim());
			if (file.exists()) {
				String fileName = file.getName();
				fs = new FileInputStream(file);
				response.addHeader(
						"Content-Disposition",
						"attachment;filename="
								+ URLEncoder.encode(fileName, "UTF-8"));
				buff = new BufferedInputStream(fs);
				byte[] b = new byte[1024];
				long k = 0;
				myout = response.getOutputStream();
				while (k < file.length()) {
					int j = buff.read(b, 0, 1024);
					k += j;
					myout.write(b, 0, j);
				}
				buff.close();
			} else {
				PrintWriter os = response.getWriter();
				os.write("文件不存在");
				os.close();
			}
			if (myout != null) {
				myout.flush();
				myout.close();
			}
			if (fs != null) {
				fs.close();
			}
		} catch (FileNotFoundException e) {
			logger.error(""+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(""+e);
		} catch (IOException e) {
			logger.error(""+e.getMessage());
		} finally {
			if (myout != null) {
				try {
					myout.flush();
					myout.close();
				} catch (IOException e) {
					logger.error(""+e.getMessage());
				}
			}
		}

	}

	/**
	 * 【远程】文件下载
	 * 
	 * @param request
	 * @param response
	 * @param fileUrl
	 *            请求下载的远程文件路径/地址
	 */
	public void getDownloadData(HttpServletRequest request,
			HttpServletResponse response, String fileUrl) {
		// Strng fileUrl="http://111.98.34.182/voice/Moo/text.txt";
		HttpURLConnection urlCon = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			URL romoteUrl = new URL(fileUrl);
			urlCon = (HttpURLConnection) romoteUrl.openConnection();
			urlCon.setConnectTimeout(10000);
			urlCon.setReadTimeout(30000);

			is = urlCon.getInputStream();
			response.reset();

			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader(
					"Content-Disposition",
					"attachment;filename="
							+ new String((System.currentTimeMillis() + fileUrl
									.substring(fileUrl.lastIndexOf("/")))
									.getBytes(), "iso-8859-1"));
			bis=new BufferedInputStream(is);
			bos=new BufferedOutputStream(response.getOutputStream());
			
			byte[] buff=new byte[2048];
			int bytesRead;
			while(-1 !=(bytesRead=bis.read(buff, 0, buff.length))){
				bos.write(buff,0,bytesRead);
			}
		} catch (MalformedURLException e) {
			logger.error(""+e.getMessage());
		} catch (IOException e) {
			logger.error(""+e.getMessage());
		}finally{
			try {
				is.close();
				bis.close();
				bos.close();
				urlCon.disconnect();
			} catch (IOException e) {
				logger.error(""+e.getMessage());
			}
		}
	}

	
	public static Workbook getWorkbookFromRequest(HttpServletRequest request,HttpServletResponse response) throws IOException{
		InputStream is=null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out=response.getWriter();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(multipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
			Iterator<String> iterator = multiRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multipartFile = multiRequest.getFile(iterator.next());
				if(multipartFile !=null){
					is=new ByteArrayInputStream(multipartFile.getBytes());
				}
			}
		}
		Workbook wb=new XSSFWorkbook(is);
		return wb;
	}
	

	
	
	/**
	 * 根据单元格的值属性来获取excel单元格的值。 日期默认返回格式自己根据需求定，这里返回yyyy-MM-dd类型和HH:mm这两种。
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		String result = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			// 数字类型 +日期类型
			case HSSFCell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
					SimpleDateFormat sdf = null;
					if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
							.getBuiltinFormat("h:mm")) {
						sdf = new SimpleDateFormat("HH:mm");
					} else {// 日期
						sdf = new SimpleDateFormat("yyyy-MM-dd");
					}
					try {
						Date date = cell.getDateCellValue();
						result = sdf.format(date);
					} catch (Exception e) {
						logger.error("单元格的日期格式不对！转换出错"+e.getMessage());
					}
				} else if (cell.getCellStyle().getDataFormat() == 58) {
					// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					double value = cell.getNumericCellValue();
					Date date = org.apache.poi.ss.usermodel.DateUtil
							.getJavaDate(value);
					result = sdf.format(date);
				} else {
					DecimalFormat df = new DecimalFormat();
					df.setGroupingUsed(false);
					result = String.valueOf(df.format(cell
							.getNumericCellValue()));
				}
				break;
			// String类型
			case HSSFCell.CELL_TYPE_STRING:
				result = String.valueOf(cell.getStringCellValue());
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = "";
			default:
				result = "";
				break;
			}
		}

		return result;
	}
}
