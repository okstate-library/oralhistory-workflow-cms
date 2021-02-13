$(document).ready(function() {

	callTransactionQueueRequest();

	callBrowseRequest();

});

var options = [];
var isinitialize = false;

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
						data : data,
						columns : [

						{
							"data" : "interviewee"
						}, {
							"data" : "interviewer"
						}, {
							"data" : "interviewDates"
						}, {
							"data" : "projectCode"
						}, {
							"data" : "transcriberAssigned"
						}, {
							"data" : "isPriority",
							render : getBooleanImage,
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
						}, {
							"data" : "id",
							render : getLink,
						}, ],
						sort : [ 0, 'desc' ],
						"lengthMenu" : [ [ 10, 50, 100, 200, -1 ],
								[ 10, 50, 100, 200, "All" ] ],
						"pageLength" : 10
					});
		}
	});

}

// Browse Window Options

var browseCollectionOptions = [];
var browseCollectionOptionTexts = [];

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

						document.getElementById("browseCollectionSelections").innerHTML = '<b>Selected options : </b>'
								+ browseCollectionOptionTexts.toString();

						callBrowseAjaxRequest();

					});
}

function callBrowseAjaxRequest() {
	$.ajax({
		type : "GET",
		cache : false,
		url : '/browse/data',
		data : {
			"collectionoptions" : browseCollectionOptions.toString()
		},
		success : function(data) {

			$('#browseDataTable').dataTable().fnDestroy();

			var table = $('#browseDataTable').DataTable(
					{
						dom : 'Bfrtip',
						buttons : [ 'copy', 'csv', 'excel', 'pdf', 'print' ],
						data : data,
						columns : [

						{
							"data" : "interviewee"
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
						}, {
							"data" : "isOnline",
							render : getBooleanImage,
						}, {
							"data" : "isRestriction",
							render : getBooleanImage,
						}, {
							"data" : "id",
							render : getLink,
						}, ],
						sort : [ 0, 'desc' ],
						"lengthMenu" : [ [ 10, 100, 50, 100, 200, -1 ],
								[ 10, 100, 50, 100, 200, "All" ] ],
						"pageLength" : 10
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

function getLink(data, type, row) {
	return '<a href="modifyinterview/' + data + '" target="_blank">edit</a>'
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