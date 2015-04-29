$(function() {

	function exec_pathParam(httpMethod) {
		var year = $('#pathParam_year').val();
		var month = $('#pathParam_month').val();
		var day = $('#pathParam_day').val();
		if (! year || ! month || ! day) {
			alert('Input!');
			return;
		}
		var url = '/blog/' + year + '/' + month + '/' + day;
		$('#pathParam_request').text(url);
		$.ajax({
			type: httpMethod,
			url: url,
			success: function(text) {
				$('#pathParam_response').text(text);
			}
		});
	}

	$('#pathParam_GET').click(function(){
		exec_pathParam('GET');
	});

	$('#pathParam_POST').click(function(){
		exec_pathParam('POST');
	});
	
	$('#jsonMap_GET').click(function(){
		var url = '/json/map';
		$('#jsonMap_request').text(url);
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			success: function(json) {
				$('#jsonMap_response').text(JSON.stringify(json));
				$('#jsonMap_name').text(json.name);
				$('#jsonMap_age').text(json.age);
			}
		});
	});

	$('#jsonBean_GET').click(function(){
		var url = '/json/bean';
		$('#jsonBean_request').text(url);
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			success: function(json) {
				$('#jsonBean_response').text(JSON.stringify(json));
				$('#jsonBean_name').text(json.name);
				$('#jsonBean_age').text(json.age);
			}
		});
	});

	$('#forward_GET').click(function(){
		var url = '/forward';
		$('#forward_request').text(url);
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			success: function(json) {
				$('#forward_response').text(JSON.stringify(json));
				$('#forward_name').text(json.name);
				$('#forward_age').text(json.age);
			}
		});
	});

	$('#session_GET').click(function(){
		var url = '/session';
		$('#session_request').text(url);
		$.ajax({
			type: 'GET',
			url: url,
			success: function(text) {
				$('#session_response').text(text);
			}
		});
	});

	$('#session_POST').click(function(){
		var data = $('#sessionData').val();
		if (! data) {
			alert('Input!');
			return;
		}
		var url = '/session?sessionData=' + data;
		$('#session_request').text(url);
		$.ajax({
			type: 'POST',
			url: url,
			success: function(text) {
				$('#session_response').text(text);
			}
		});
	});

	$('#filter_GET').click(function(){
		var url = '/filter';
		$('#filter_request').text(url);
		$.ajax({
			type: 'GET',
			url: url,
			success: function(text) {
				$('#filter_response').text(text);
			}
		});
	});
});