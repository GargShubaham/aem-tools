$(function () {


	var REPORTS_NOT_SELECTED_HEADER = "Failure";
	var REPORTS_NOT_SELECTED_CONTENT = "Select at least one report to delete.";
	var REPORTS_NOT_SELECTED_FOOTER = "";

	var CONFIRM_DELETE_ALERT_HEADER = "Warning!";
	var CONFIRM_DELETE_ALERT_CONTENT = "Are you sure you want to delete?"
	var CONFIRM_DELETE_ALERT_FOOTER = '<button id="cancelButton" is="coral-button" variant="default" coral-close>Cancel</button><button id="acceptButton" is="coral-button" variant="primary">Yes</button>';

	var DELETE_SUCCESS_VARIANT = "success";
	var DELETE_SUCCESS_HEADER = "Success";
	var DELETE_SUCCESS_CONTENT = "Selected reports has been deleted.";
	var DELETE_SUCCESS_FOOTER = "";


	var dialog = document.querySelector('#report-lister-dialog');
	if (dialog) {
		document.body.appendChild(dialog);

		$('#reports-delete-button').on('click', function () {

			var paths = getItemsToDelete();

			if (paths) {
				if (paths.length == '0') {
					dialog.header.innerHTML = REPORTS_NOT_SELECTED_HEADER;
					dialog.content.innerHTML = REPORTS_NOT_SELECTED_CONTENT;
					dialog.footer.innerHTML = REPORTS_NOT_SELECTED_FOOTER;
					dialog.show();
				} else {
					dialog.header.innerHTML = CONFIRM_DELETE_ALERT_HEADER;
					dialog.content.innerHTML = CONFIRM_DELETE_ALERT_CONTENT;
					dialog.footer.innerHTML = CONFIRM_DELETE_ALERT_FOOTER;
					dialog.show();


					dialog.on('coral-overlay:close', function (event) {

					});
					dialog.on('click', '#acceptButton', function () {
						dialog.hide();
						$.ajax({
							url: "/content/component-audit-manager/createwizard.componentreport.xml?deletereports=true",
							type: "GET",
							data: {
								'deletepaths': paths
							},
							timeout: 6000,
							error: function (jqXHR, exception) {

								dialog.variant = DELETE_SUCCESS_VARIANT;
								dialog.header.innerHTML = DELETE_SUCCESS_HEADER;
								dialog.content.innerHTML = DELETE_SUCCESS_CONTENT;
								dialog.footer.innerHTML = DELETE_SUCCESS_FOOTER;
								dialog.show();

								console.log(jqXHR);
							},
							success: function (data, textStatus, xhr) {

								if (xhr.status == '200') {
									dialog.variant = DELETE_SUCCESS_VARIANT;
									dialog.header.innerHTML = DELETE_SUCCESS_HEADER;
									dialog.content.innerHTML = DELETE_SUCCESS_CONTENT;
									dialog.footer.innerHTML = DELETE_SUCCESS_FOOTER;
									dialog.show();
									window.setTimeout(reload, 1000);
								}
							}
						});
					});
				}
			}
		});

	}

	function getItemsToDelete() {
		var deletepaths = [];
		$('.coral-Table-row.is-selected').each(function (i, obj) {
			var item = $(obj);
			deletepaths.push(item.data("foundationCollectionItemId"));
		});
		return deletepaths;
	}

	function reload() {
		location.reload();
	}


});