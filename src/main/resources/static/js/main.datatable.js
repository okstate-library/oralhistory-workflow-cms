$(document).ready(function() {

	callTransactionQueueRequest();

	callBrowseRequest();

});

var options = [];
var isinitialize = true;

function callTransactionQueueRequest() {

	$('.transcriptQueueOption')
			.click(

					function() {

						var optionValue = $(this).attr('value');

						if (this.checked && optionValue == 1) {
							for (i = 2; i < 11; i++) {
								var element = document
										.getElementById("transcriptionQueueOption-"
												+ i);

								element.checked = false;
							}

							options = [];
							isinitialize = true;

						} else if (this.checked) {

							options.push(optionValue);

							var element = document
									.getElementById("transcriptionQueueOption-" + 1);

							element.checked = false;

							isinitialize = false;

						} else {
							var index = options.indexOf(optionValue);

							if (index !== -1) {
								options.splice(index, 1);
							}

							isinitialize = false;

							if (options.length == 0) {
								var element = document
										.getElementById("transcriptionQueueOption-" + 1);

								element.checked = true;
								options = [];
								isinitialize = true;
							}
						}

						callTransactionQueueAjaxRequest();

					});
}

function callTransactionQueueAjaxRequest() {

	$.ajax({
		type : "GET",
		cache : false,
		url : '/transcriptionqueue/data',
		data : {
			"options" : options.toString(),
			"isinitialize" : isinitialize,
		},
		success : function(data) {

			$('#transcriptionQueueDataTable').dataTable().fnDestroy();

			var table = $('#transcriptionQueueDataTable').DataTable(
					{
						dom : 'Bfrtip',
						buttons : [ 'copy', 'csv', 'excel', 'pdf', 'print' ],
						dom: 'Blfrtip',
						data : data,
						columns : [

						{
							"data" : "interviewee",
							'render' : getLink
						}, {
							"data" : "interviewer"
						}, {
							"data" : "interviewDates"
						}, {
							"data" : "projectCode",class: 'text-center' 
						}, {
							"data" : "transcriberAssigned"
						}, {
							"data" : "isPriority",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false,
						}, {
							"data" : "auditCheckCompleted"
						}, {
							"data" : "firstEditCompleted"
						}, {
							"data" : "secondEditCompleted"
						}, {
							"data" : "draftSentDate"
						}, {
							"data" : "finalEditCompleted"
						}, ],
						sort : [ 0, 'desc' ],
						"lengthMenu" : [ [ 10, 50, 100, 200, -1 ],
								[ 10, 50, 100, 200, "All" ] ],
						"pageLength" : 10
					});

			$('#transcriptionQueueDataTable').on(
					'click',
					'tbody td',
					function() {

						var obj = $(this).closest('tr').find('td:eq(0) a')
								.attr('href');

						window.open(obj, '_blank');
					});

		}
	});

}

// Browse Window Options

var browseCollectionOptions = [];
var browseCollectionOptionTexts = [];

var browseSubserieOptions = [];
var browseSubserieOptionTexts = [];

function callBrowseRequest() {

	$('.browseCollectionOption')
			.click(
					function() {

						var optionValue = $(this).attr('value');
						var optionText = $(this).attr('name');

						if (this.checked) {
							browseCollectionOptions.push(optionValue);
							browseCollectionOptionTexts.push(optionText);
						} else {
							var index = browseCollectionOptions
									.indexOf(optionValue);

							if (index !== -1) {
								browseCollectionOptions.splice(index, 1);
							}

							var indexText = browseCollectionOptionTexts
									.indexOf(optionText);

							if (indexText !== -1) {
								browseCollectionOptionTexts
										.splice(indexText, 1);
							}
						}

						setupSelections();

						callBrowseAjaxRequest();

					});
	
	$('.browseSubserieOption')
	.click(
			function() {

				var optionValue = $(this).attr('value');
				var optionText = $(this).attr('name');

				if (this.checked) {
					browseSubserieOptions.push(optionValue);
					browseSubserieOptionTexts.push(optionText);
				} else {
					var index = browseSubserieOptions
							.indexOf(optionValue);

					if (index !== -1) {
						browseSubserieOptions.splice(index, 1);
					}

					var indexText = browseSubserieOptionTexts
							.indexOf(optionText);

					if (indexText !== -1) {
						browseSubserieOptionTexts
								.splice(indexText, 1);
					}
				}

				setupSelections();

				callBrowseAjaxRequest();

			});
}

function setupSelections()
{
	document.getElementById("browseSelections").innerHTML = "";
	
	
	document.getElementById("browseSelections").innerHTML = "(Collections)" + browseCollectionOptionTexts.toString()
	
	+ "(Subseries)" + browseSubserieOptionTexts.toString();
}

function callBrowseAjaxRequest() {
	$.ajax({
		type : "GET",
		cache : false,
		url : '/browse/data',
		data : {
			"collectionoptions" : browseCollectionOptions.toString(),
			"subseriesoptions" : browseSubserieOptions.toString(),
		},
		success : function(data) {

			$('#browseDataTable').dataTable().fnDestroy();

			var table = $('#browseDataTable').DataTable(
					{
						dom : 'Bfrtip',
						buttons : [ 'copy', 'csv', 'excel', 'pdf', 'print' ],
						dom: 'Blfrtip',
						data : data,
						columns : [

						{
							"data" : "interviewee",
							'render' : getLink
						}, {
							"data" : "interviewer"
						}, {
							"data" : "projectCode"
						}, {
							"data" : "interviewDates"
						}, {
							"data" : "collectionName"
						}, {
							"data" : "subseriesName"
						}, {
							"data" : "transcriptStatus",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false,
							width : '5px'
						}, {
							"data" : "isOnline",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false,
							width : '5px'
						}, {
							"data" : "isRestriction",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false,
							width : '5px'
						}, ],
						sort : [ 0, 'desc' ],
						"lengthMenu" : [ [ 10, 100, 50, 100, 200, -1 ],
								[ 10, 100, 50, 100, 200, "All" ] ],
						"pageLength" : 10
					});

			$('#browseDataTable').on(
					'click',
					'tbody td',
					function() {

						var obj = $(this).closest('tr').find('td:eq(0) a')
								.attr('href');

						window.open(obj, '_blank');
					});

		},
		error : function() {
			elert(error);
		},
		complete : function() {
		}
	});
}

function getBooleanImage(data, type, full, meta) {
	if (data == '1') {
		return '<i class="fa fa-check" aria-hidden="true"/>'
	} else {
		return '<i class="fa fa-times" aria-hidden="true"/>';
	}
}

function getLink(index, row, data) {

	return '<a style="text-decoration:none;" href="modifyinterview/' + data.id
			+ '" target="_blank">' + data.interviewee + '</a>'
}

var expanded = false;

function showCheckboxes() {
	var checkboxes = document.getElementById("checkboxes");
	if (!expanded) {
		checkboxes.style.display = "block";
		expanded = true;
	} else {
		checkboxes.style.display = "none";
		expanded = false;
	}
}