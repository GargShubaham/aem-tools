$(function () {
	var t = $('.component-usage-report-viewer-table').DataTable({

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
});