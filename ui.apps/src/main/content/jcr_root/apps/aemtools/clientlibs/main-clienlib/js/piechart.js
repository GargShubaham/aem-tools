$(function () {
      var wait = document.querySelector('#report-viewer-loader');
     if (wait) {
      wait.hide();
     }

	if ($('input#piechartjson').val()) {

		var json = jQuery.parseJSON($('input#piechartjson').val());

		Highcharts.chart('piechart', {
			chart: {
				type: 'pie',
				options3d: {
					enabled: true,
					alpha: 45,
					beta: 0
				}
			},
			title: {
				text: 'Component Usage Report'
			},
			tooltip: {
				pointFormat: '<br>Component Used <b>{point.y}</b> times<br><b>{point.percentage:.1f}%</b>'
			},
			plotOptions: {
				pie: {
					allowPointSelect: true,
					cursor: 'pointer',
					depth: 75,
					dataLabels: {
						enabled: true,
						format: '<b>{point.name}</b> {point.percentage:.1f}% ({point.y} times)'
					}
				}
			},
			series: [{
				type: 'pie',
				name: 'Component Usage',
				data: json
			}],
            exporting: {
        showTable: true
    }

		});

	}

});