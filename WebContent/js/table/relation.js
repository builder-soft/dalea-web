function moveToRight() {
	moveElement("left", "right");
	return;
}

function moveToLeft() {
	moveElement("right", "left");
	return;
}

function moveElement(source, target) {
	if ($('#' + source + ' option:selected').size() > 0) {
		var value = $('#' + source + ' option:selected').val();
		var html = $('#' + source + ' option:selected').html();

		$('#' + target).append($('<option></option>').val(value).html(html));

		$('#' + source + ' option:selected').remove();
	}
}

function save(){
	$('#right').attr('multiple', 'yes');
	$('#right option').prop('selected',true);
	$('#frm').submit();
}