(function($) {
	$.toggleShowPassword = function(options) {
		var settings = $.extend({
			field : "#password",
			control : "#toggle_show_password",
		}, options);

		var control = $(settings.control);
		var field = $(settings.field);

		control.bind('click', function() {
			if (control.is(':checked')) {
				field.attr('type', 'text');
			} else {
				field.attr('type', 'password');
			}
		})
	};

}(jQuery));

$(document)
		.ready(
				function() {

					$.toggleShowPassword({
						field : '#password',
						control : "#showPassword"
					});

					var end = new Date();

					$('.date').datepicker({
						multidate : true
					});

					// Transcription Window Options

					var options = [];
					var optionTexts = [];

					$('.transcriptQueueOption')
							.click(
									function() {

										var optionValue = $(this).attr('value');
										var optionText = $(this).attr('name');

										if (this.checked) {
											options.push(optionValue);
											optionTexts.push(optionText);
										} else {
											var index = options
													.indexOf(optionValue);

											if (index !== -1) {
												options.splice(index, 1);
											}

											var indexText = optionTexts
													.indexOf(optionText);

											if (indexText !== -1) {
												optionTexts
														.splice(indexText, 1);
											}
										}

										document
												.getElementById("transcriptQueueOptionSelections").innerHTML = '<b>Selected options : </b>'
												+ optionTexts.toString();

										$
												.ajax({
													type : "GET",
													cache : false,
													url : '/transcriptionqueue/data',
													data : {
														"options" : options
																.toString()
													},
													success : function(data) {

														$(
																'#transcriptionQueueDataTable')
																.dataTable()
																.fnDestroy();

														var table = $(
																'#transcriptionQueueDataTable')
																.DataTable(
																		{
																			data : data,
																			columns : [

																					{
																						"data" : "interviewee"
																					},
																					{
																						"data" : "interviewer"
																					},
																					{
																						"data" : "interviewDate"
																					},
																					{
																						"data" : "projectCode"
																					},
																					{
																						"data" : "transcriberAssigned"
																					},
																					{
																						"data" : "isPriority",
																						render : getBooleanImage,
																					},
																					{
																						"data" : "auditCheckCompleted"
																					},
																					{
																						"data" : "firstEditCompleted"
																					},
																					{
																						"data" : "secondEditCompleted"
																					},
																					{
																						"data" : "draftSentDate"
																					},
																					{
																						"data" : "finalEditCompleted"
																					}, ],
																			sort : [
																					0,
																					'desc' ],
																			"lengthMenu" : [
																					[
																							50,
																							100,
																							200,
																							-1 ],
																					[
																							50,
																							100,
																							200,
																							"All" ] ],
																			"pageLength" : 100
																		});

													},
													error : function() {
														elert(error);
													},
													complete : function() {
													}
												});
									});

					// Browse Window Options

					var browseCollectionOptions = [];
					var browseCollectionOptionTexts = [];

					$('.browseCollectionOption')
							.click(
									function() {

										var optionValue = $(this).attr('value');
										var optionText = $(this).attr('name');

										if (this.checked) {
											browseCollectionOptions
													.push(optionValue);
											browseCollectionOptionTexts
													.push(optionText);
										} else {
											var index = browseCollectionOptions
													.indexOf(optionValue);

											if (index !== -1) {
												browseCollectionOptions.splice(
														index, 1);
											}

											var indexText = browseCollectionOptionTexts
													.indexOf(optionText);

											if (indexText !== -1) {
												browseCollectionOptionTexts
														.splice(indexText, 1);
											}
										}

										document
												.getElementById("browseCollectionSelections").innerHTML = '<b>Selected options : </b>'
												+ browseCollectionOptionTexts
														.toString();

										callAjaxRequest();

									});

					function callAjaxRequest() {
						$
								.ajax({
									type : "GET",
									cache : false,
									url : '/browse/data',
									data : {
										"collectionoptions" : browseCollectionOptions
												.toString()
									},
									success : function(data) {

										$('#browseDataTable').dataTable()
												.fnDestroy();

										var table = $('#browseDataTable')
												.DataTable(
														{
															data : data,
															columns : [

																	{
																		"data" : "interviewee"
																	},
																	{
																		"data" : "interviewer"
																	},
																	{
																		"data" : "projectCode"
																	},
																	{
																		"data" : "interviewDate"
																	},
																	{
																		"data" : "collectionId"
																	},
																	{
																		"data" : "subseriesId"
																	},
																	{
																		"data" : "transcriptStatus",
																		render : getBooleanImage,
																	},
																	{
																		"data" : "isOnline",
																		render : getBooleanImage,
																	},
																	{
																		"data" : "isRestriction",
																		render : getBooleanImage,
																	}, ],
															sort : [ 0, 'desc' ],
															"lengthMenu" : [
																	[
																			10,
																			50,
																			100,
																			200,
																			-1 ],
																	[
																			10,
																			50,
																			100,
																			200,
																			"All" ] ],
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

				});

function getBooleanImage(data, type, full, meta) {
	if (data == '1') {
		return '<i class="fa fa-check" aria-hidden="true"/>'
	} else {
		return '<i class="fa fa-times" aria-hidden="true"/>';
	}
}

function selectSebseries() {

	var collectionId = document.getElementById("collectitonDropDown").value;

	alert(collectionId);
	$.ajax({
		type : 'GET',
		url : "getSubseries",
		data : {
			"collectionId" : collectionId
		},
		success : function(data) {

			alert(data);
			// var subseriesDropDown = $('#subseriesDropDown'), option = "";
			//
			// subseriesDropDown.empty();
			//
			// option = option
			// + "<option value=''> Select subseries </option>";
			//
			// for (var i = 0; i < data.length; i++) {
			// option = option + "<option value='"
			// + data[i].Id + "'>"
			// + data[i].SubseriesName + "</option>";
			// }
			//
			// subseriesDropDown.append(option);
		},
		error : function(xhr, status, error) {
			var err = eval("(" + xhr.responseText + ")");
			alert(err.Message);
		}

	});

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