function changeSelect(path, select) {
	var url = path+'/servlet/system/roleDef/RoleDef?cId='
			+ $(select).val();
	self.location.href = url;
}

function save(){
	$('#frm').submit();
}