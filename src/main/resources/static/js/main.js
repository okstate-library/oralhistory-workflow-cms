(function($) {
	// $.toggleShowPassword = function(options) {
	// var settings = $.extend({
	// field : "#password",
	// control : "#toggle_show_password",
	// }, options);
	//
	// var control = $(settings.control);
	// var field = $(settings.field);
	//
	// control.bind('click', function() {
	// if (control.is(':checked')) {
	// field.attr('type', 'text');
	// } else {
	// field.attr('type', 'password');
	// }
	// })
	// };

}(jQuery));

$(document)
		.ready(
				function() {

					$('.dates').datepicker({
						multidate : true,
						multidateSeparator : ';'
					});

					$('.date').datepicker({});

					$("#ProjectCode")
							.on(
									'change',
									function postinput() {
										var matchvalue = $(this).val();

										$
												.ajax(
														{
															url : '/validateprojectcode',
															data : {
																code : matchvalue
															},
															type : 'post'
														})

												.done(
														function(responseData) {

															if (responseData) {
																alert('Project code is alreay using. Change to procced!');

																$(
																		"#ProjectCode")
																		.focus();

																$(
																		"#ProjectCode")
																		.css(
																				"background-color",
																				"red");
															} else {
																$(
																		"#ProjectCode")
																		.css(
																				"background-color",
																				"#fff");
															}
														}).fail(function() {
													console.log('Failed');
												})

									});

					$("#submitreport")
							.click(
									function(event) {

										var data = {}

										data["interviewer"] = $("#interviewer")
												.val();
										data["location"] = $("#location").val();
										data["beginDate"] = $("#begindate")
												.val();
										data["endDate"] = $("#enddate").val();

										// Stauts
										var statusRadioButton = $(
												'input[name="statusRadioButton"]:checked')
												.val();

										if (statusRadioButton == 'undefined') {
											data["status"] = null;
										} else {
											data["status"] = statusRadioButton;
										}

										// Media
										var bornDigitalCheckboxButton = document
												.getElementById("bornDigitalCheckboxButton").checked;

										if (!bornDigitalCheckboxButton) {
											data["bornDigital"] = null;
										} else {
											data["bornDigital"] = 1;
										}

										var convertedCheckboxButton = document
												.getElementById("convertedCheckboxButton").checked;

										if (!convertedCheckboxButton) {
											data["converted"] = null;
										} else {
											data["converted"] = 1;
										}

										document.getElementById("recordCount").innerHTML = "";

										$
												.ajax({
													type : "POST",
													contentType : "application/json",
													url : "/reports",
													data : JSON.stringify(data),
													dataType : 'json',
													beforeSend : function() { // Before
														// we
														// send
														// the
														// request
														$('#loader')
																.removeClass(
																		'hidden')
													},
													success : function(data) {

														document
																.getElementById("recordCount").innerHTML = data;

														var reportButton = document
																.getElementById("downloadReportButton");

														if (data > 0) {
															reportButton.style.visibility = "visible";
														} else {
															reportButton.style.visibility = "hidden";
														}

													},
													error : function(e) {
														var err = eval("("
																+ xhr.responseText
																+ ")");
														alert(err.Message);
													},
													complete : function() {
														// class and hiding the
														// spinner.
														$('#loader').addClass(
																'hidden')
													},
												});

									});

					$("#subjectvaidate")
							.click(
									function(event) {

										document
												.getElementById("subjectrecordcount").innerHTML = "";

										$
												.ajax({
													type : "POST",
													contentType : "application/json",
													url : "/subjects/validate",
													dataType : 'json',
													beforeSend : function() { // Before
														// we
														// send
														// the
														// request
														$('#loader')
																.removeClass(
																		'hidden')
													},
													success : function(data) {

														var submitSubjectRow = document
																.getElementById("submitSubjectRow");

														var subjectNoRecordsRow = document
																.getElementById("subjectNoRecordsRow");

														if (data > 0) {
															document
																	.getElementById("subjectrecordcount").innerHTML = data
																	+ ' records found.';

															submitSubjectRow.style.visibility = "visible";
															subjectNoRecordsRow.style.visibility = "hidden";
															
														} else {
															submitSubjectRow.style.visibility = "hidden";
															subjectNoRecordsRow.style.visibility = "visible";
														}

													},
													error : function(e) {
														var err = eval("("
																+ e.responseText
																+ ")");

														alert(err.Message);
													},
													complete : function() {
														// class and hiding the
														// spinner.
														$('#loader').addClass(
																'hidden')
													},
												});

									});

//					$("#collectionvaidate")
//							.click(
//									function(event) {
//
//										$
//												.ajax({
//													type : "POST",
//													contentType : "application/json",
//													url : "/collections/validate",
//													dataType : 'json',
//													done(function (data) {
//													    alert('success');
//													})
//													,
//													error : function(e) {
//														var err = eval("("
//																+ xhr.responseText
//																+ ")");
//														alert(err.Message);
//													}
//												});
//
//									});

				});

function selectSeriesByCollection() {

	var collectionId = document.getElementById("collectitonDropDown").value;

	$.ajax({
		type : 'GET',
		url : "../series/getSeries",
		data : {
			"collectionId" : collectionId
		},
		success : function(data) {

			var seriesDropDown = $('#seriesDropDown'), option = "";

			seriesDropDown.empty();

			if (data.length > 1) {

				option = option + "<option value=''> Select Series </option>";

			}

			for (var i = 0; i < data.length; i++) {
				option = option + "<option value='" + data[i].id + "'>"
						+ data[i].seriesName + "</option>";
			}

			seriesDropDown.append(option);
		},
		error : function(xhr, status, error) {
			var err = eval("(" + xhr.responseText + ")");
			alert(err.Message);
		}

	});

}
