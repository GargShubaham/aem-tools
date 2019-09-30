$(function () {


	var COMPONENT_LISTER_TABLE = "#component-lister-table";
	var COMPONENT_LISTER_TABLE_LENGTH = "#component-lister-table_length";
	var REPORT_COMPONENTS = "reportComponents";
	var REPORT_PATHS = "reportPaths";
    var PROCEED_FAILURE_CONTENT = "Please add atleast one page to get components list.";
    var SUBMIT_FAILURE_CONTENT = "Select components from table to generate report.";

    var dialog = document.querySelector('#report-creation-dialog');
    if (dialog) {
		document.body.appendChild(dialog);
    }
     var wait = document.querySelector('#report-creation-loader');
     if (wait) {
      wait.hide();
     }

	var table = $(COMPONENT_LISTER_TABLE).DataTable({
		'columnDefs': [{
			'targets': 0,
			'checkboxes': {
				'selectRow': true
			}
		}],
		'select': {
			'style': 'multi'
		},
		'order': [
			[1, 'asc']
		],

		"lengthMenu": [
			[-1, 10, 50, 100, 250, 500],
			["All", 10, 50, 100, 250, 500]
		],
		bJQueryUI: true,
		dom: 'lBfrtip',
		buttons: [
			'copy', 'csv', 'excel', 'pdf', 'print'
		]
	});

	$('#form-configuration-proceed').on('click', function () {
		table.clear();


		if ($("input[name=" + REPORT_PATHS + "]").val()) {
            wait.show();


			$.ajax({
				url: "/content/component-audit-manager/createwizard.componentreport.xml?getcomponent=true",
				type: "GET",
				data: $('#report-creation-form').serialize(),
				timeout: 10000,
				beforeSend: function () {
					$('body').append(wait);
				},
				complete: function () {
                  wait.hide();
				},

				error: function (jqXHR, exception) {
				wait.hide();
					console.log(jqXHR);
				},

				success: function (result) {
					wait.hide();
					var obj = jQuery.parseJSON(result);
					$.each(obj, function (key, value) {
						buildTable(value.JCR_TITLE, value.COMPONENT_USAGE, value.COMPONENT_FULL_PATH, value.JCR_COMPONENT_GROUP, value.JCR_DESCRIPTION, value.JCR_CREATEDBY, value.JCR_CREATED, table, value.COMPONENT_RESOURCETYPE);
					});
				}
			});

		} else {

          dialog.content.innerHTML = PROCEED_FAILURE_CONTENT;
		  dialog.show();


		}

	});

	function buildTable(JCR_TITLE, COMPONENT_USAGE, COMPONENT_FULL_PATH, JCR_COMPONENT_GROUP, JCR_DESCRIPTION, JCR_CREATEDBY, JCR_CREATED, table, COMPONENT_RESOURCETYPE) {
		try {

			table.row.add([
				'',
				JCR_TITLE,
				'<span class="red"> ' + COMPONENT_USAGE + '</span>',
                COMPONENT_RESOURCETYPE,
				JCR_COMPONENT_GROUP,
				JCR_DESCRIPTION,
				JCR_CREATEDBY,
				JCR_CREATED
			]).node().id = COMPONENT_RESOURCETYPE;
			table.draw(false);

		} catch (err) {

		}

	}
	$("#report-creation-form").submit(function (event) {

		if (table.rows('.selected').count() > 0) {
			var form = this;
			$(COMPONENT_LISTER_TABLE_LENGTH).remove();
			table.$("tr.selected").each(function () {
				var id = $(this).attr("id");
				$(form).append(
					$('<input>')
					.attr('type', 'hidden')
					.attr('name', REPORT_COMPONENTS)
					.val(id)
				);
			});
		} else {
			event.preventDefault();
            dialog.content.innerHTML = SUBMIT_FAILURE_CONTENT;
			dialog.show();
		}

	});



});