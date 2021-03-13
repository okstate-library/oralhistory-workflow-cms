
$(document).ready(function() {
	
	callTransactionQueueRequest();

	// callBrowseRequest();
});

// Transaction Queue page options

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
		 beforeSend: function () { // Before we send the request, remove the
									// .hidden class from the spinner and
									// default to inline-block.
             $('#loader').removeClass('hidden')
         },
		success : function(data) {

			$('#transcriptionQueueDataTable').dataTable().fnDestroy();

			$('#transcriptionQueueDataTable thead tr').clone(true).appendTo( '#transcriptionQueueDataTable thead' );
			
			
			const checkBoxBooleanColumn = [5];
			
			const checkBoxColumn = [4,6,7,8,9,10,11,12];
			
			function exists(arr, search) {
			    return arr.some(row => row.includes(search));
			}

			const textBoxColumns = {
				    0: 20,
				    1: 20,
				    3: 6,
				};

			function keyexists(searchKey)
			{
				for(key in textBoxColumns){
					
					if(key == searchKey )
					   return true;
					}
				return false;
			}
			
			
			$('#transcriptionQueueDataTable thead tr:eq(1) th').each( function (i) {
		        
				var title = $(this).text();
				
				if (keyexists( i))
				{
			        $(this).html( '<input type="text" size="' + textBoxColumns[i] +'" placeholder="'+title+'" />' );
			 
			        $( 'input', this ).on( 'keyup change', function () {
			            if ( table.column(i).search() !== this.value ) {
			                table
			                    .column(i)
			                    .search(this.value)
			                    .draw();
			            }
			        } );
				}
				else if (checkBoxColumn.includes(i))
				{
			        $(this).html( '<input type="checkbox" />' );
			 
			        $( 'input', this ).on( 'keyup change', function () {

			        	 if(this.checked == true)
			        	 {
				        	 table
					        	 .column(i)
					        	 .search( '^(?!\s*$).+', true, false )
					        	 .draw();
			        	 }else
			        	 { 		
			        		 
				        	 table
					        	 .column(i)
					        	 .search( '' )
					        	 .draw();		  
			        	 }
			        	
			        } );
				}
				else if (checkBoxBooleanColumn.includes(i))
				{
			        $(this).html( '<select id="prioritySelect" >'
			        		+'<option value="reset">Reset</option>'
			        		+'<option value="true">True</option>'
			        	    +'<option value="false">False</option>'
			        	  +'</select>' );
			 
			        $("#prioritySelect").change(function () {
		                // alert($(this).val());
		                
		                var select = $(this).val() ;// alert($(this).val());
			        	 
			        	 if(select === "reset")
			        	 {
			        		 table
			                 .column(i) // or columns???
			                 .search( '^(?!\s*$).+', true, false)
			                 .draw(); 
			        		 
			        	 }else if  (select === "true")
			        	 { 	
			        		 // alert(table.column(i).data())
//			        		 
			        		 table
			                 .column(i) // or columns???
			                 .search( '1')
			                 .draw(); 
			        		 
			        	 }else
		        		 {
			        		 table
			                 .column(i) // or columns???
			                 .search( '0')
			                 .draw(); 
		        		 }
		                
		                
		            });
			      
				}else
					{
					 $(this).html( '' );
					}
		        
		    } );
			
		    
			var table = $('#transcriptionQueueDataTable').DataTable(
					{
						dom : 'Bfrtip',
						buttons : [ 'copy', 'csv', 'excel', 'pdf', 'print' ],
						dom: 'Blfrtip',
						data : data,
						orderCellsTop: true,
				        fixedHeader: true,
						"columnDefs": [
						      {  "width": "90px", "targets": 0 },
						      { "width": "90px", "targets": 1 },
						      { "width": "40px", "targets": 2 },
						      { "width": "15px", "targets": 3 },
						      { "width": "50px", "targets": 4 },
						      { "width": "10px", "targets": 5 },
						      { "width": "10px", "targets": 6 },
						      { "width": "10px", "targets": 7 },
						      { "width": "10px", "targets": 8 },
						      { "width": "10px", "targets": 9 },
						      { "width": "10px", "targets": 10 },
						      { "width": "10px", "targets": 11 },
						      { "width": "10px", "targets": 12 }
						    ],
						    columns : [

						{
							"data" : "interviewee",
							'render' : getLink,
							'sortable': false,
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
// render : getBooleanImage,
// class : 'text-center' ,
							// 'sortable': false,
						}, {
							"data" : "auditCheckCompleted"
						}, {
							"data" : "firstEditCompleted"
						}, {
							"data" : "secondEditCompleted"
						}, {
							"data" : "draftSentDate" 
						}, {
							"data" : "editWithCorrectionCompleted" 
						}, {
							"data" : "finalEditCompleted"
						},{
							"data" : "finalSentDate"
						}, ],
						
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

		},
        complete: function () { // Set our complete callback, adding the .hidden
								// class and hiding the spinner.
            $('#loader').addClass('hidden')
        },
	});

}

// Browse page options

var browseCollectionOptions = [];
var browseCollectionOptionTexts = [];

var browseSubserieOptions = [];
var browseSubserieOptionTexts = [];

var browseInterviewerOptionTexts = [];

var browseSubjectOptionTexts = [];

var browseOnlineOfflineOptionTexts = "";

var browseDarkArchiveOptionTexts = "";


$("#reset_selection").click(function(){
	
	browseCollectionOptions = [];
	browseCollectionOptionTexts = [];
	
	browseSubserieOptions = [];
	browseSubserieOptionTexts = [];
	
	browseInterviewerOptionTexts = [];
	
	browseSubjectOptionTexts = [];
	
	browseOnlineOfflineOptionTexts = "";
	
	browseDarkArchiveOptionTexts = "";
	
	setupSelections();
	 
	callBrowseAjaxRequest();
	
	document.getElementById("reset_selection").style.display = "none";
	
});


function callBrowseRequest() {

	alert('callBrowseRequest');
	
	// Collection Section
	$('.browseCollectionOption')
			.click(
					function() {
						
						var optionValue = $(this).attr('value');
						var optionText = $(this).attr('name');


						alert('browse Collaetion called' + optionValue);
						
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
	
	// Subseries Section
	
	$('.browseSubserieOption')
	.click(
			
			function() {
				
				
				
				var optionValue = $(this).attr('value');
				var optionText = $(this).attr('name');

				alert('browseSubserieOption called' + optionValue);
				
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
				
				// selectBrowseSubseries();

			});
	
	// Interviewer Section
	
	$('.browseInterviewerOption')
	.click(
			function() {
				
				var optionText = $(this).attr('name');

				if (this.checked) {
					browseInterviewerOptionTexts.push(optionText);
				} else {

					var indexText = browseInterviewerOptionTexts
							.indexOf(optionText);

					if (indexText !== -1) {
						browseInterviewerOptionTexts
								.splice(indexText, 1);
					}
				}

				setupSelections();

				callBrowseAjaxRequest();

			});
	
	// Subjects Section
	
	$('.browseSubjectOption')
	.click(
			function() {
				
				var optionText = $(this).attr('name');

				if (this.checked) {
					browseSubjectOptionTexts.push(optionText);
				} else {

					var indexText = browseSubjectOptionTexts
							.indexOf(optionText);

					if (indexText !== -1) {
						browseSubjectOptionTexts
								.splice(indexText, 1);
					}
				}

				setupSelections();

				callBrowseAjaxRequest();

			});
	
	// online off line Section
	
	$('#browseIsOnlineOption').change(function() {
		
		alert('browse online called');
		
		browseOnlineOfflineOptionTexts = "";
		
        if(this.checked) {
        	 
        	browseOnlineOfflineOptionTexts = "online";
        	// $('#browseIsOfflineOption').prop('checked', false);
        }
        
        setupSelections();

		callBrowseAjaxRequest();
              
    });
	
	
	
	$('#browseIsOfflineOption').change(function() {
		
	c
		
		browseOnlineOfflineOptionTexts = "";
		
        if(this.checked) {
        	browseOnlineOfflineOptionTexts = "offline";
        	// $('#browseIsOnlineOption').prop('checked', false);
        }
        
        setupSelections();

		callBrowseAjaxRequest();
              
    });
	
	// Dark Archive Section
	
	 $('#browseDarkArchiveOption').change(function() {
		
			alert('browse dark archieve called');
			
		 browseDarkArchiveOptionTexts= "";
		 
	        if(this.checked) {
	        	 
	        	browseDarkArchiveOptionTexts= "true";
	        }
	        
	        setupSelections();

			callBrowseAjaxRequest();
	              
	    });
	
	// callBrowseAjaxRequest();
}

function callBrowseAjaxRequest() {
	$.ajax({
		type : "GET",
		cache : false,
		url : '/browse/data',
		data : {
			"collections" : browseCollectionOptions.toString(),
			"subseries" : browseSubserieOptions.toString(),
			"interviewers" : browseInterviewerOptionTexts.toString(),
			"subjects" : browseSubjectOptionTexts.toString(),
			"onlineoffline" : browseOnlineOfflineOptionTexts,
			"darkarchive" :browseDarkArchiveOptionTexts,
			
		},
		 beforeSend: function () { // Before we send the request
			$('#loader').removeClass('hidden')
			},
		 success : function(data) {

			
			$('#browseDataTable').dataTable().fnDestroy();

			var table = $('#browseDataTable').DataTable(
					{
						scrollX:        false,
						dom : 'Bfrtip',
						buttons : [ 'copy', 'csv', 'excel', 'pdf', 'print' ],
						dom: 'Blfrtip',
						data : data.transcriptions,
						"columnDefs": [
						      {  "width": "90px", "targets": 0 },
						      { "width": "90px", "targets": 1 },
						      { "width": "15px", "targets": 2 },
						      { "width": "40px", "targets": 3 },
						      { "width": "100px", "targets": 4 },
						      { "width": "10px", "targets": 5 },
						      { "width": "10px", "targets": 6 },
						      { "width": "10px", "targets": 7 }
						    ],
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
							"data" : data,// "collectionName",
							render : getShortText,
						}, {
							"data" : "transcriptStatus",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false
						}, {
							"data" : "isOnline",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false
						}, {
							"data" : "isRestriction",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false
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
			
			
			
			// Collections
			
			 $('#browseCollections').empty();
			
			 for (var i = 0; i < data.collections.length; i++) {
					
					var collection = data.collections[i];
					
					
							
							option = "<label class='dropdown-item small'> " +
							"<input class='browseCollectionOption' type='checkbox'" +
							"name='" + collection.name +  "'" +
							"value='" + collection.id +  "'" +
							"id='browseCollectionOption-" + collection.id +  "'>"
							+ " " +collection.name + " </label> <br /> ";
							
									
					$('#browseCollections').append(option);
				}
			
			// Subseries
			$('#browseSubseries').empty();

			for (var i = 0; i < data.subseries.length; i++) {
				
				var subseries = data.subseries[i];
				
				
						option = "<label class='dropdown-item small'> " +
						"<input class='browseSubserieOption' type='checkbox'" +
						"name='" + subseries.name +  "'" +
						"value='" + subseries.id +  "'" +
						"id='browseSubserieOption-" + subseries.id +  "'>"
						+ " " +subseries.name + " </label> <br /> ";
						
								
				$('#browseSubseries').append(option);
			}

			// Interviewers
			
			$('#browseInterviewers').empty();

			for (var i = 0; i < data.interviewers.length; i++) {
				
				var interviewer = data.interviewers[i];
				
				
					option = "<label class='dropdown-item small'> " +
					"<input class='browseInterviewerOption' type='checkbox'" +
					"name='" + interviewer.name +  "'" +
					"value='" + interviewer.id +  "'" +
					"id='browseInterviewerOption-" + interviewer.id +  "'>"
					+ " " + interviewer.name + "</label> <br /> ";
				
				$('#browseInterviewers').append(option);
			}
			
			// Online offline
			$('#browseStatusOnline').empty();

			for (var i = 0; i < data.statusOnline.length; i++) {
				
				var status = data.statusOnline[i];
				
				
					option = "<label class='dropdown-item small'> " +
					"<input type='checkbox'" +
					"name='" + status.name +  "'" +
					"id='" + status.identification +  "'>"
					+ " " + status.name + "</label> <br /> ";
				
				$('#browseStatusOnline').append(option);
			}
			
			// Dark Archive
			$('#browseDarkArchieve').empty();

			for (var i = 0; i < data.darkArchieve.length; i++) {
				
				var dark = data.darkArchieve[i];
				
				
					option = "<label class='dropdown-item small'> " +
					"<input type='checkbox'" +
					"name='" + dark.name +  "'" +
					"id='" + dark.identification +  "'>"
					+ " " + dark.name + "</label> <br /> ";
				
				$('#browseDarkArchieve').append(option);
			}
			
			$('#loader').addClass('hidden');
		},
		error : function() {
			alert(error);
		},
		complete : function() {
		}
	});
}

function setupSelections()
{
	document.getElementById("browseSelections").innerHTML = "";
	
	document.getElementById("browseSelections").innerHTML = 
		(browseCollectionOptionTexts.length == 0 ? "" :"<b>Collections </b> <i>" + browseCollectionOptionTexts.toString() + "</i><br/>")
		+ (browseSubserieOptionTexts.length == 0 ? "" :"<b>Subseries</b> <i>" + browseSubserieOptionTexts.toString()  + "</i><br/>")
		+ (browseInterviewerOptionTexts.length == 0 ? "" :"<b>Interviewers </b> <i>" + browseInterviewerOptionTexts.toString() + "</i><br/>")
		+ (browseSubjectOptionTexts.length == 0 ? "" :"<b>Subjects </b> <i>" + browseSubjectOptionTexts.toString() + "</i><br/>")
		+ (browseOnlineOfflineOptionTexts == "" ? "" :"<b>Status</b> <i>" + browseOnlineOfflineOptionTexts + "</i><br/>")
		+ ( browseDarkArchiveOptionTexts == "" ? "" : "<b>Dark Archieve </b> <i>"+ browseDarkArchiveOptionTexts+ "</i><br/>") ;
}

function getShortText(data) {
	
	return  "(c)"+  data.collectionName  + (data.subseriesName != "N/A" ? "</br> (s)" + data.subseriesName : "") ;
	// return "<p title='" + data + "'>" + (data.length > 20 ?
	// data.collectionName.substr(0, 17) +"..." : data.collectionName ) +
	// "</p>";
}

function getBooleanImage(data, type, full, meta) {
	if (data == '1') {
		return '<i class="fa fa-check" aria-hidden="true"/>'
	} else {
		return '<i class="fa fa-times" aria-hidden="true"/>';
	}
}

function getLink(index, row, data) {

	return '<a href="modifyinterview/' + data.id
			+ '">' + data.interviewee + '</a>'
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