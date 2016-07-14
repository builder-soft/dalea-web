function changeSelect(select) {
	var url = contextPath+'/servlet/system/roleDef/RoleDef?cId='
			+ $(select).val();
	self.location.href = url;
}

function save(){
	$('#frm').submit();
}