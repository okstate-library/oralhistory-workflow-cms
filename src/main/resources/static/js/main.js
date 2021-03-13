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

					// $.toggleShowPassword({
					// field : '#password',
					// control : "#showPassword"
					// });

					var end = new Date();

					$('.dates').datepicker({
						multidate : true
					});

					$('.date').datepicker({});

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

				});

function selectSebseries() {

	var collectionId = document.getElementById("collectitonDropDown").value;

	$.ajax({
		type : 'GET',
		url : "../subseries/getSubseries",
		data : {
			"collectionId" : collectionId
		},
		success : function(data) {

			var subseriesDropDown = $('#subseriesDropDown'), option = "";

			subseriesDropDown.empty();

			if (data.length > 1) {

				option = option
						+ "<option value=''> Select Subseries </option>";

			}

			for (var i = 0; i < data.length; i++) {
				option = option + "<option value='" + data[i].id + "'>"
						+ data[i].subseriesName + "</option>";
			}

			subseriesDropDown.append(option);
		},
		error : function(xhr, status, error) {
			var err = eval("(" + xhr.responseText + ")");
			alert(err.Message);
		}

	});

}
