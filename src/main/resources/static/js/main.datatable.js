
$(document).ready(function() {

	// callBrowseRequest();
});

// Transaction Queue page options

function callTransactionQueueAjaxRequest() {

	$.ajax({
		type : "GET",
		cache : false,
		url : '/transcriptionqueue/data',
		 beforeSend: function () { 
             $('#loader').removeClass('hidden') // Loader
         },
		success : function(data) {

			$('#transcriptionQueueDataTable').dataTable().fnDestroy();

			$('#transcriptionQueueDataTable thead tr').clone(true).appendTo( '#transcriptionQueueDataTable thead' );
			
			const checkBoxBooleanColumn = [5];
			
			const checkBoxColumn = [4,6,7,8,9,10,11];
			
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
		            
		                var select = $(this).val() ;
			        	 
			        	 if(select === "reset")
			        	 {
			        		 table
			                 .column(i) 
			                 .search( '^(?!\s*$).+', true, false)
			                 .draw(); 
			        		 
			        	 }else if  (select === "true")
			        	 { 
			        		 table
			                 .column(i) 
			                 .search( '1')
			                 .draw(); 
			        		 
			        	 }else
		        		 {
			        		 table
			                 .column(i) 
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
						buttons : [ 'excel', 'pdf' ],
						dom: 'Blfrtip',
						data : data,
						orderCellsTop: true,
				        fixedHeader: true,
						"columnDefs": [
						      {  "width": "90px",title: "Interviewee(s)" , "targets": 0 },
						      { "width": "90px", title: "Interviewer(s)" ,"targets": 1 },
						      { "width": "40px", title: "Interview date(s)" ,"targets": 2 },
						      { "width": "15px", title: "Code" ,"targets": 3 },
						      { "width": "40px",title: "Transcriber assigned" , "targets": 4 },
						      { "width": "15px",title: "Priority" , "targets": 5 },
						      { "width": "10px", title: "Audit check" ,"targets": 6 },
						      { "width": "10px", title: "First edit" ,"targets": 7 },
						      { "width": "15px",title: "Second edit" , "targets": 8 },
						      { "width": "10px",title: "Draft sent" , "targets": 9 },
						      { "width": "10px",title: "Corrections" , "targets": 10 },
						      { "width": "10px",title: "Final edit" , "targets": 11 },
						      { "width": "10px",title: "Media edit" , "targets": 12 },
						      { "width": "1px",  title: "Subject" , "targets": 13 ,searchable: true,visible: false},
					          { "width": "1px",  title: "Keywords" , "targets": 14 ,searchable: true,visible: false},
					          { "width": "1px",  title: "Description" , "targets": 15 ,searchable: true,visible: false},
					          { "width": "1px",  title: "Coverage Spatial" , "targets": 16 ,searchable: true, visible: false},
					          { "width": "1px",  title: "Coverage Temporal " , "targets": 17 ,searchable: true,visible: false},
						    ],
						    columns : [

						{
							"data" : "interviewee",
							'render' : getLink,
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
							"data" : "isProductionMasterFileStatus",
							render : getBooleanImage,
							class  : 'text-center' ,
							'sortable': false
						},{
							"data" : "subject"
						},{
							"data" : "keywords"
						},{
							"data" : "description"
						},{
							"data" : "coverageSpatial"
						},{
							"data" : "coverageTemporal"
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

var browseSeriesOptions = [];
var browseSeriesOptionTexts = [];

var browseInterviewerOptionTexts = [];

var browseSubjectOptionTexts = [];

var browseOnlineOfflineOptionTexts = "";

var browseDarkArchiveOptionTexts = "";


$("#reset_selection").click(function(){
	
	browseCollectionOptions = [];
	browseCollectionOptionTexts = [];
	
	browseSeriesOptions = [];
	browseSeriesOptionTexts = [];
	
	browseInterviewerOptionTexts = [];
	
	browseSubjectOptionTexts = [];
	
	browseOnlineOfflineOptionTexts = "";
	
	browseDarkArchiveOptionTexts = "";
	
	setupSelections();
	 
	callBrowseAjaxRequest();
	
	document.getElementById("reset_selection").style.display = "none";
	
});


function callBrowseRequest() {
	
	document.getElementById("reset_selection").style.display = "inline";
	
	// Collection Section
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
	
	// Series Section
	
	$('.browseSeriesOption')
	.click(
			
			function() {
				
				
				var optionValue = $(this).attr('value');
				var optionText = $(this).attr('name');

				if (this.checked) {
					browseSeriesOptions.push(optionValue);
					browseSeriesOptionTexts.push(optionText);
				} else {
					var index = browseSeriesOptions
							.indexOf(optionValue);

					if (index !== -1) {
						browseSeriesOptions.splice(index, 1);
					}

					var indexText = browseSeriesOptionTexts
							.indexOf(optionText);

					if (indexText !== -1) {
						browseSeriesOptionTexts
								.splice(indexText, 1);
					}
				}

				setupSelections();

				callBrowseAjaxRequest();

			});
	
	// Interviewer Section
	
	$('.browseInterviewerOption')
	.click(
			function() {
				
				var optionText = $(this).attr('value');

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
				
				var optionText = $(this).attr('value');

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
		
		browseOnlineOfflineOptionTexts = "";
		
        if(this.checked) {
        	 
        	browseOnlineOfflineOptionTexts = "online";
        	// $('#browseIsOfflineOption').prop('checked', false);
        }
        
        setupSelections();

		callBrowseAjaxRequest();
              
    });
	
	
	
	$('#browseIsOfflineOption').change(function() {
		
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
			"series" : browseSeriesOptions.toString(),
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
						scrollX: false,
						buttons : [ 'excel', 'pdf'],
						dom: 'Blfrtip',
						data : data.transcriptions,
						"columnDefs": [
							  
						      {  "width": "90px", title: "Interviewee(s)" , "targets": 0 },
						      { "width": "90px", title: "Interviewer(s)" ,  "targets": 1 },
						      { "width": "8px", title: "Code" ,  "targets": 2 },
						      { "width": "15px",  title: "Interview date(s)" , "targets": 3 },
						      { "width": "100px",  title: "Collection/ Series" , "targets": 4 },
						      { "width": "25px", title: "Sent out" ,  "targets": 5 },
						      { "width": "15px", title: "Online" ,  "targets": 6 },
						      { "width": "25px", title: "Restriction" ,  "targets": 7 },
						      { "width": "1px",  title: "Subject" , "targets": 8 ,searchable: true,visible: false},
					          { "width": "1px",  title: "Keywords" , "targets": 9 ,searchable: true,visible: false},
					          { "width": "1px",  title: "Description" , "targets": 10 ,searchable: true,visible: false},
					          { "width": "1px",  title: "Coverage Spatial" , "targets": 11 ,searchable: true, visible: false},
					          { "width": "1px",  title: "Coverage Temporal " , "targets": 12 ,searchable: true,visible: false},
						    ],
						columns : [

						{
							"data" : "interviewee",
							'render' : getLink
						}, {
							"data" : "interviewer",
							"render" : function( data, type, full ) {
								  data = data.split(';')
								     .map(w => w + '; ')
								     .join(' ')
								  return data;                         
								}
						}, {
							"data" : "projectCode"
						}, 
						{
							"data" : "interviewDates",
// "render" : function( data, type, full ) {
// data = data.split(';')
// .map(w => w + ';')
// .join(' ')
// return data;
// }
// "render": function(data, type) {
// return type === 'sort' ? data : moment(data).format("YYYYMMDD");
// }
						}, 
						{
							"data" : data,// "collectionName",
							render : getShortText,
						}, {
							"data" : "sentOut",
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
						},{
							"data" : "subject"
						},{
							"data" : "keywords"
						},{
							"data" : "description"
						},{
							"data" : "coverageSpatial"
						},{
							"data" : "coverageTemporal"
						}, ],
						sort : [ 0, 'desc' ],
						"lengthMenu" : [ [ 10, 100, 50, 100, 200, -1 ],
								[ 10, 100, 50, 100, 200, "All" ] ],
						"pageLength" : 10,
						"select" : {
					            style: 'multi'
					        }
					});

			
			$("#browseSubmit").click( function(){
			             
				var ids = [];
			              
     			var rows_selected = table.rows('.selected').data().toArray()
     
     			      
     			// Iterate over all selected rows.
     			$.each(rows_selected, function(index, rowId){
     			      ids.push(rowId["id"]);
     			});
     			     
     			if(ids.length > 0){
    			     window.location = '/bulkmodify?ids=' + ids;
        			}else
        				{
        				alert('Select at least one record')
        				}
  			
				// //var url = 'http://example.com/vote/' + ids;
				// var form = $('<form action="/bulkmodify" method="post">' +
				// '<input type="text" name="ids" value="' + ids + '" />' +
				// '</form>');
				// $('body').append(form);
				// form.submit();

     			   
			});
			
			   
			if(data.searchWord != null)
			{
				table.search(data.searchWord).draw();
			}
			 
// $('#browseDataTable').on(
// 'click',
// 'tbody td',
// function() {
//
// var obj = $(this).closest('tr').find('td:eq(0) a')
// .attr('href');
//
// window.open(obj, '_blank');
// });
//			
			
			// Collections
			 
			 $('#browseCollections').empty();
			 
			 for (var i = 0; i < data.collections.length; i++) {
					
					var collection = data.collections[i];
					
					option = "<label class='dropdown-item small' " +
					"title='" + collection.identification +  "'>" +
					"<input class='browseCollectionOption' type='checkbox'" +
					"name='" + collection.name +  "'" +
					"value='" + collection.id +  "'" +
					"id='browseCollectionOption-" + collection.id +  "'>" 
					+ " " +collection.name + " </label> <br /> ";
										
					$('#browseCollections').append(option);
				}
			
			 
			 
			 $('#collections-dropdown').empty();
			 
			// Series
			$('#browseSeries').empty();

			for (var i = 0; i < data.series.length; i++) {
				
				var aseries = data.series[i];
				
				
						option = "<label class='dropdown-item small' " +
						"title='" + aseries.identification +  "'>" +
						"<input class='browseSeriesOption' type='checkbox'" +
						"name='" + aseries.name +  "'" +
						"value='" + aseries.id +  "'" +
						"id='browseSeriesOption-" + aseries.id +  "'>"
						+ " " +aseries.name + " </label> <br /> ";
						
								
				$('#browseSeries').append(option);
			}
			
			accordianVisibility('#seriesCard',data.series);
			

			// Interviewers
			
			$('#browseInterviewers').empty();

			for (var i = 0; i < data.interviewers.length; i++) {
				
				var interviewer = data.interviewers[i];
				
				
					option = "<label class='dropdown-item small' " +
					"title='" + interviewer.identification +  "'>" +
					"<input class='browseInterviewerOption' type='checkbox'" +
					"name='" + interviewer.name +  "'" +
					"value='" + interviewer.identification +  "'" +
					"id='browseInterviewerOption-" + interviewer.id +  "'>"
					+ " " + interviewer.name + "</label> <br /> ";
				
				$('#browseInterviewers').append(option);
			}
			
			// Subjects
			
			$('#browseSubjects').empty();

			for (var i = 0; i < data.subjects.length; i++) {
				
				var subject = data.subjects[i];
				
				
					option = "<label class='dropdown-item small' " +
					"title='" + subject.identification +  "'>" +
					"<input class='browseSubjectOption' type='checkbox'" +
					"name='" + subject.name +  "'" +
					"value='" + subject.identification +  "'" +
					"id='browseSubjectOption-" + subject.id +  "'>"
					+ " " + subject.name + "</label> <br /> ";
				
				$('#browseSubjects').append(option);
			}
			
			accordianVisibility('#subjectCard',data.subjects);
			
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
			
			accordianVisibility('#darkArchieveCard',data.darkArchieve);
			
			$('#loader').addClass('hidden');
		
		},
		error : function() {
			alert(error);
		},
		complete : function() {
		}
	});
}

function AddNewRIKDialog(e,dt, node, config)
{
	alert(JSON.stringify(e));
	// alert(JSON.stringify(dt));
	alert(JSON.stringify(node));
	alert(JSON.stringify(config));
	
}

function accordianVisibility(card , data)
{
	if (data.length == 0)
	{
		$(card).hide();
	}else
	{
		$(card).show();
	}
}

function setupSelections()
{
	document.getElementById("browseSelections").innerHTML = "";
	
	document.getElementById("browseSelections").innerHTML = 
		(browseCollectionOptionTexts.length == 0 ? "" :"<b>Collections </b> <i>" + browseCollectionOptionTexts.toString() + "</i><br/>")
		+ (browseSeriesOptionTexts.length == 0 ? "" :"<b>Series</b> <i>" + browseSeriesOptionTexts.toString()  + "</i><br/>")
		+ (browseInterviewerOptionTexts.length == 0 ? "" :"<b>Interviewers </b> <i>" + browseInterviewerOptionTexts.toString() + "</i><br/>")
		+ (browseSubjectOptionTexts.length == 0 ? "" :"<b>Subjects </b> <i>" + browseSubjectOptionTexts.toString() + "</i><br/>")
		+ (browseOnlineOfflineOptionTexts == "" ? "" :"<b>Status</b> <i>" + browseOnlineOfflineOptionTexts + "</i><br/>")
		+ ( browseDarkArchiveOptionTexts == "" ? "" : "<b>Dark Archieve </b> <i>"+ browseDarkArchiveOptionTexts+ "</i><br/>") ;
}

function getShortText(data) {
	
	return  data.collectionName  + (data.seriesName != "N/A" ? "/ " + data.seriesName : "") ;
}

function getBooleanImage(data, type, full, meta) {
	if (data == '1') {
		return '<i class="fa fa-check" aria-hidden="true"/>'
	} else {
		return '<i class="fa fa-times" aria-hidden="true"/>';
	}
}

function getLink(index, row, data) {

	return '<a target="_blank" href="modifyinterview/' + data.id
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