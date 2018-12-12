
function addJiJinInfo() {
	$("#divDialog").window('open');
}
function addInfo() {
	$.post(basepath + "jijin/addJiJin", $('#addInfo').serializeArray(),
			function(data) {
				var fl = data.flag;
				if (fl) {
					$("#divDialog").window('close');
					$.messager.alert('提示', '新增成功');
				} else {
					$.messager.alert('提示', '新增失败');
				}
			})
}

function editJiJinInfo() {
	alert("修改");
}

function getParam(arr) {
	var obj = {};
	$.each(arr, function(i, n) {
		obj[arr[i].name] = arr[i].value;
	});
	return obj;
}