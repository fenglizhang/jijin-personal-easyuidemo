<%@page language="java" import="java.lang.*" pageEncoding="UTF-8"%>
<%@page import="com.alibaba.druid.support.json.JSONUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String jiJin = "";
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Object jiJinName = session.getAttribute("jiJinName");
	if (jiJinName != null) {
		jiJin = JSONUtils.toJSONString(jiJinName);
	}
%>
<script type="text/javascript">
	var jiJinData=<%=jiJin%>;
	function getAllJiJinData(){
		if([] !=jiJinData){
			var temp=$.extend(true,[],jiJinData);
			temp.unshift({'id':'','name':'全部'});
			return temp;
		}
	}
</script>
<script type="text/javascript"
	src="<%=basePath%>static/js/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>static/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>static/js/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=basePath%>static/js/easyui/base64.js"></script>
<link type="text/css" rel="stylesheet"
	href="<%=basePath%>static/js/easyui/themes/default/easyui.css" />
<link type="text/css" rel="stylesheet"
	href="<%=basePath%>static/js/easyui/themes/icon.css" />

