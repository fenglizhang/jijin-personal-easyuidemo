<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.lang.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基金涨跌统计表</title>
<%@ include file="/common.jsp" %>
<script type="text/javascript">
	var basepath='<%=basePath%>';
</script>

<script type="text/javascript">
	$(function(){
		
		$("#jiJinCode").combobox({
			editable:false,
			data:getAllJiJinData(),
			valueField:"id",
			textField:"name"
		});
		//新增弹框前置隐藏设置
		$("#divDialog").dialog({
			title:"新增-基金日涨幅信息",
			width:400,
			height:200,
			closed:true,
			cache:false,
			modal:true,
			buttons:[{
				iconCls:"icon-save",
				text:'保存',
				handler:function(){
					addInfo();
				}
			},{
				iconCls:"icon-undo",
				text:'关闭',
				handler:function(){
					$("#divDialog").window('close');
				}
			}]
		});
		
		$("#batchDivDialog").dialog({
			title:"批量新增-基金日涨幅信息",
			width:600,
			height:300,
			closed:true,
			cache:false,
			modal:true,
			buttons:[{
				iconCls:"icon-undo",
				text:'关闭',
				handler:function(){
					$("#batchDivDialog").window('close');
				}
			}]
		});
		
		$("#sumJiJinDivDialog").dialog({
			title:"统计基金日涨幅规律",
			width:1050,
			height:350,
			closed:true,
			cache:false,
			modal:true
		});
		
	})
</script>
</head>
<body>
<div style="height:100%; overflow:auto;">
	<div class="easyui-layout" style="margin-top:10px;margin-left:0px;width:100%;height:130px;">
		<!-- 	查询条件 -->
		<div  title="查询条件" iconCls="icon-search" style="overflow:auto;padding:0px;height:130px;"
			data-options="region:'north',border:false,collapsible:true">
			<form id="query" method="post">
				<table class="tableStyle" style="width: 100%; margin-top: 10px;height:60px;">
					<tr>
						<td class="title">基金</td>
						<td class="content"><input id="jiJinCode"
							class="easyui-combobox" name="jiJinCode" style="width: 160px;"
							size="4" data-options="editable:false" /></td>
						<td class="title">统计的时间范围从</td>
						<td class="content"><input id="jiJinStartTime"
							class="easyui-datebox" name="jiJinStartTime"
							style="width: 160px;" size="4" data-options="editable:false" /></td>
						<td class="title">到</td>
						<td class="content"><input id="jiJinEndTime"
							class="easyui-datebox" name="jiJinEndTime" style="width: 160px;"
							size="4" data-options="editable:false" /></td>
					</tr>
				</table>
			</form>
			<div class="serbut" align="right" style="float: right;height:30px;width:70%;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					iconCls="icon-search" style="width: 70px;" onclick="searchInfo()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					iconCls="icon-remove" style="width: 70px;" onclick="clearInfo()">清空</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					iconCls="icon-sum" style="width: 70px;" onclick="SumInfo()">统计</a>
			</div>
		</div>
	</div>
		<!-- 	查询结果 -->
	<div class="easyui-accordion" style="margin-top:5px;margin-left:0px;width:100%;height:450px;">	
		<div title="查询结果" iconCls="icon-chaxunjieguo">
			<table id="cxjg" iconCls="icon-table"></table>
		</div>
	</div>	
	<div id="divDialog" class="easyui-dialog">
			<form id="addInfo" method="post">
				<input type="hidden" id="jiJinId" name="jiJinId" class="easyui-textbox">
				<table class="tableStyle">
					<tr>
						<td class="title">基金名称</td>
						<td class="content"><input class="easyui-textbox" id="eJiJinName" name="eJiJinName"/></td>
					</tr>
					<tr>
						<td class="title">基金代码</td>
						<td class="content"><input class="easyui-textbox" id="eJiJinCode" name="eJiJinCode"/></td>
					</tr>
					<tr>
						<td class="title">日期</td>
						<td class="content" ><input id="eJiJinTime" class="easyui-datebox" name="eJiJinTime" data-options="editable:false"  /></td>
					</tr>
					<tr>
						<td class="title">日涨幅</td>
						<td class="content"><input class="easyui-textbox" id="eDailyIncreases" name="eDailyIncreases"/></td>
					</tr>
				</table>
			</form>
	</div>
	<div id="batchDivDialog" class="easyui-dialog">
			<form id="batchAddInfo" method="post" enctype="multipart/form-data">
				<div style="width:95%;height:250px;">
					<fieldset>
						<legend style="margin-bottom:10px;"><font color="red">批量新增</font></legend>
						<table>
						<tr><td><span>文件导入</span></td></tr>
						<tr><td><input id="importFile" name="importFile" type="file" style="width:200px;"/></td></tr>
						<tr><td><a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;" onclick="fileUpload()" iconCls="icon-reload">导入</a></td></tr>
						<tr><td><a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;" onclick="downloadFile()" iconCls="icon-print">模板下载</a></td></tr>
						</table>
					</fieldset>
				</div>
			</form>
	</div>
	<div id="sumJiJinDivDialog" class="easyui-dialog">
		<div title="统计结果" iconCls="icon-chaxunjieguo">
			<table id="tjjg" iconCls="icon-table"></table>
		</div>
	</div>
</div>
	<script type="text/javascript">
		$(function(){
			$('#cxjg').datagrid({
				iconCls:'',
				singleSelect: true,
				striped:true,
				fit:true,
				data:[],
				pageSize:10,
				pageList:[10,15,20],
				idField:"id",
				method:'post',
				frozenColumns:[[{field:'ck',checkbox:true}]],
				columns:[[
				       {field:'id', 			title:"id", 	width:100,	align:'center',hidden:true},   
				       {field:'jiJinName', 		title:'基金名称',	width:400,	align:'center'},   
				       {field:'jiJinCode', 		title:'基金代码',	width:100,	align:'center'},   
				       {field:'jiJinTime', 		title:'日期' 	,	width:300,	align:'center'},   
				       {field:'dailyIncreases', title:'日涨幅', 	width:200,	align:'center',formatter:function(value,row,inde){
				    	   if(value>0){
				    		   return "<font color='red'>"+value+"</font>";
				    	   }else{
				    		   return "<font color='blue'>"+value+"</font>"; 
				    	   }
				       }}   
				          ]],
				toolbar:[{
					id:'xzjjxx',
					text:'新增基金信息',
					iconCls:'icon-add',
					handler:function(){
						addJiJinInfo();
					}
				},{
					id:'xgjjxx',
					text:'修改基金信息',
					iconCls:'icon-edit',
					handler:function(){
						editJiJinInfo();
					}
				},{
					id:'plxzjjxx',
					text:'批量新增基金信息',
					iconCls:'icon-add',
					handler:function(){
						batchAddJiJinInfo();
					}
				}],
				pagination:true,
				rownumbers:true
			});
			
		});
		//新增按钮
		function addJiJinInfo(){
			$("#jiJinId").textbox('setValue','');
			$("#eJiJinName").textbox('setValue','');
			$("#eJiJinCode").textbox('setValue','');
			$("#eDailyIncreases").textbox('setValue',"");
			$("#eJiJinTime").datebox("setValue","");	
			$("#divDialog").window('open');
		}
		//批量新增按钮
		function batchAddJiJinInfo(){
			$("#batchDivDialog").window('open');
		}
		
		//确认新增
		function addInfo(){
			$.post(basepath+"jijin/addJiJin",$('#addInfo').serializeArray(),function(data){
					var fl=data.flag;
					if(fl){
						$("#divDialog").window('close');
						$.messager.alert('提示','新增成功');
						searchInfo();
					}else{
						$.messager.alert('提示','新增失败，信息有重复');
					}
				});
		}
		//修改按钮
		function editJiJinInfo(){
			var row=$("#cxjg").datagrid('getSelected');
			
			if(row){
				var id=row.id;
				var name=row.jiJinName;	
				var code=row.jiJinCode;
				var num=row.dailyIncreases;
				var time=row.jiJinTime;
				$("#jiJinId").textbox('setValue',id);
				$("#eJiJinName").textbox('setValue',name);
				$("#eJiJinCode").textbox('setValue',code);
				$("#eDailyIncreases").textbox('setValue',num);
				$("#eJiJinTime").datebox("setValue",time);			
				
				$("#divDialog").dialog({
					title:"修改-基金日涨幅信息",
					width:400,
					height:200,
					closed:true,
					cache:false,
					modal:true,
					buttons:[{
						iconCls:"icon-save",
						text:'确认修改',
						handler:function(){
							updataInfo();
						}
					},{
						iconCls:"icon-undo",
						text:'关闭',
						handler:function(){
							$("#divDialog").window('close');
						}
					}]
				});
				$("#divDialog").window('open');
			}else{
				$.messager.alert('提示','请选择一条记录修改');
			}
		}
		
		//查询按钮
		function searchInfo(){
// 			$("#cxjg").datagrid({
// 				url:basepath+'jijin/getList',
// // 				queryParams:getParam($("#query").serializeArray()),
// queryParams : {'age':23,'name':'zhangsan'},
// 				method:"post"
// 			});
//  var url=basepath+'jijin/getList';
//  var param={'age':23,'name':'zhangsan'};
//   $.post(url,param,function(data){});
	
  $.ajax(
          {
//         	  dataType: "json",
              type: "POST",
              data:"{'mobilenum':'15867426421','age':'23','name':'zhangsan'}",
//               contentType: "application/json; charset=utf-8",
              url :basepath+'jijin/getList',
              success: function(data){    
              }
           });
    }
  
  
  
  
		
		
		
		
		
		function getParam(arr){
			var obj={};
			$.each(arr,function(i,n){
				obj[arr[i].name]=arr[i].value;
			});
			return obj;
		}
		function clearInfo(){
			$("#jiJinCode").combobox("setValue","");
			$("#jiJinStartTime").datebox("setValue","");
			$("#jiJinEndTime").datebox("setValue","");
		}
		//确认修改
		function updataInfo(){
			$.post(basepath+"jijin/updateJiJinInfo",$('#addInfo').serializeArray(),function(data){
				var fl=data.flag;
				if(fl){
					$("#divDialog").window('close');
					$.messager.alert('提示','修改成功');
					searchInfo();
				}else{
					$.messager.alert('提示','修改失败');
				}
			});
		}
		
		//模板下载
		function downloadFile(){
			window.location.href=basepath+"jijin/downloadModel";
		}
		
		//文件提交
		function fileUpload(){
			var importFile=$("#importFile").val();
			var reg=".xlsx$";
			var patrn=new RegExp(reg);
			if(patrn.exec(importFile)){
				//不做任何事
			}else{
				$.messager.alert("提示","请导入.xls形式的Excel!");
				return false;
			}
			
			$("#batchAddInfo").form('submit',{
				url:basepath+"jijin/fileUpload",
				onSubmit:function(){},
				success:function(data){
					data=eval('('+data+')');
					var flag=data.flag;
					if(flag){
						$("#batchDivDialog").window("close");
						$.messager.alert('提示',"批量新增成功！");
						searchInfo();
					}else{
						$("#batchDivDialog").window("close");
						$.messager.alert('提示',"批量新增失败！");
						searchInfo();
					}
				}
			});
			
			
		}
		
	function SumInfo(){
		var code=$("#jiJinCode").combobox("getValue");
		if(code){
			$('#tjjg').datagrid({
				iconCls:'',
				singleSelect: true,
				striped:true,
				width:1010,
				height:320,
				data:[],
// 				pageSize:10,
// 				pageList:[10,15,20],
				idField:"id",
				method:'post',
				frozenColumns:[[{field:'ck',checkbox:true}]],
				columns:[[
				       {field:'jiJinName', 		title:'基金名称',	width:180,	align:'center'},   
				       {field:'jiJinCode', 		title:'基金代码',	width:100,	align:'center'},   
				       {field:'monthNum', 		title:'月份' 	,	width:50,	align:'center'},
				       {field:'monthSum', 		title:'整月涨跌情况汇总', 	width:150,	align:'center',formatter:function(value,row,index){
				    	   if(value>0){
				    		   return "<font color='red'>"+value+"</font>";
				    	   }else{
				    		   return "<font color='blue'>"+value+"</font>"; 
				    	   }
				       }},
				       {field:'weekNum', 		title:'周几' 	,	width:70,	align:'center',formatter:function(value,row,index){
				    	   if(value==1){
				    		   return "周一";
				    	   }else if(value==2){return "周二";}else if(value==3){
				    		   return "周三";
				    	   }else if(value==4){return "周四";}else if(value==5){
				    		   return "周五";
				    	   }else{
				    		   return "";
				    	   }
				       }},   
				       {field:'upNum', 		title:'每月周几涨的次数' 	,	width:120,	align:'center',formatter:function(value){
				    	   return "基金涨了<font color='red' size=3>"+value+"</font>次";
				       }},   
				       {field:'downNum', 		title:'每月周几跌的次数' 	,	width:120,	align:'center',formatter:function(value){
				    	   return "基金跌了<font color='blue' size=3>"+value+"</font>次";
				       }},   
				       {field:'weekDaySum', 		title:'周几涨跌情况汇总', 	width:150,	align:'center',formatter:function(value,row,index){
				    	   if(value>0){
				    		   return "<font color='red'>"+value+"</font>";
				    	   }else{
				    		   return "<font color='blue'>"+value+"</font>"; 
				    	   }
				       }}
				          ]],
// 				          pagination:true,
						  rownumbers:true
			});
			
			$("#tjjg").datagrid({
				url:basepath+'jijin/sumInfo',
				queryParams:getParam($("#query").serializeArray()),
				method:"post"
			});
			
			$("#sumJiJinDivDialog").window("open");
		}else{
			$.messager.alert('提示','请选择一种基金进行统计结果分析！');
		}
	}
	</script>
</body>
</html>