
$(document).ready(function(){	
	//autoRoll()
})


function autoRoll(){
	//自动滚动
	setInterval(function(){
		if(Math.floor($("#wrap").scrollTop() + $("#wrap").height()) <= $("#wrap").height()){
			//$("#wrap").scrollTop($("#wrap").height())
			$("#wrap").animate({ scrollTop:$("#wrap").height() },   "slow");

		}else{
			//$("#wrap").scrollTop(0)
			$("#wrap").animate({ scrollTop:0 },   "slow");
		}
		
	},1000*20)
}


// 路径配置
require.config({
	paths: {
		echarts: '../js/common/echarts-2.2.3/build/dist'
	}
});


// 使用
require(
	[
		'echarts',
		'echarts/chart/bar',	 //使用柱状图就加载bar模块，按需加载
		'echarts/chart/pie',
		'echarts/chart/line'
	],
	function (ec) {
		alert(20);
		
		var _buliangPie = {
			legend : ['麸皮','划痕','腐蚀','漏液','注液不良'],
			series:{ 
				pie:[
					{value:335, name:'麸皮'},
					{value:310, name:'划痕'},
					{value:234, name:'腐蚀'},
					{value:135, name:'漏液'},
					{value:1548, name:'注液不良'}
				]
			}
		}
		
		/*********
			ajax getData
			//jquery
			$.ajax{
				success(){
					drawDangriPie()
				}
			}
		*********/

		//图1
	//	drawbuliangPie(ec,_buliangPie);



		var _buliangqushiArea = {
			legend : ['预购','成交'],
			xAxis : ['12/22','12/23','12/24','12/25','12/26','12/27','12/28','12/29','12/30','12/31','1/1'],
			series:{ 
				line1 : [5.8, 3.9, 5.1, 6.0, 4.0, 3.1, 6.0, 3.9, 5.1, 6.0, 4.0],
				line2 : [3.9, 5.1, 6.0, 4.0, 3.1, 6.0, 3.9, 5.1, 6.0, 4.0, 5.8]
			}
		}

		//图2
		drawBuliangqushiArea(ec,_buliangqushiArea);

		
		
		var _biaozhunBar = {
			legend : ['报警','降水量'],
			xAxis : ['12/22','12/23','12/24','12/25','12/26','12/27','12/28','12/29','12/30','12/31','1/1'],
			series:{ 
				bar1 : [5.8, 3.9, 5.1, 6.0, 4.0, 3.1, 6.0, 3.9, 5.1, 6.0, 4.0],
				bar2 : [3.9, 5.1, 6.0, 4.0, 3.1, 6.0, 3.9, 5.1, 6.0, 4.0, 5.8]
			}
		}

		//图3
	//	drawbiaozhunBar(ec,_biaozhunBar)

		
		var _buliangqushiLines = {
			legend : ['预购','成交'],
			xAxis : ['12/22','12/23','12/24','12/25','12/26','12/27','12/28','12/29','12/30','12/31','1/1'],
			series:{ 
				line1 : [5.8, 3.9, 5.1, 6.0, 4.0, 3.1, 6.0, 3.9, 5.1, 6.0, 4.0],
				line2 : [3.9, 5.1, 6.0, 4.0, 3.1, 6.0, 3.9, 5.1, 6.0, 4.0, 5.8]
			}
		}

		//图2
	//	drawBuliangqushiLines(ec,_buliangqushiLines);
		
	}
);

//图形1 当日	
function drawbuliangPie(ec,data){

	var dangriPie = ec.init(document.getElementById('buliangPie')); 

	var _Sum = getSum(data.series.pie)

	var option = {
		
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b}: {c} ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x:'left',
			y:'80',
			data:data.legend,
			formatter: function(name,i){
				return name + "   " + getPercent(getValue(data.series.pie,name),_Sum);
			},
			textStyle:charts_Config.buliangPie.legendColor,
		},
		series: [
			{
				name:'访问来源',
				center : ['65%', '50%'],
				type:'pie',
				radius: ['60%', '90%'],
				avoidLabelOverlap: false,
				itemStyle : {
					normal : {
						color: function(params) {
							// build a color map as your need.
							var colorList = charts_Config.buliangPie.pieColorList;
							return colorList[params.dataIndex]
						},
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					},
					emphasis : {
						label : {
							show : true,
							position : 'center',
							textStyle : {
								fontSize : '30',
								fontWeight : 'bold',
							}
						}
					}
				},
				data:data.series.pie
			}
		]
	};

	dangriPie.setOption(option); 
}



//图形2 当日
function drawBuliangqushiArea(ec,data){
		
	var buliangqushiArea = ec.init(document.getElementById('buliangqushiArea')); 

	var option = {
		grid:{
			backgroundColor:charts_Config.BuliangqushiArea.gridBackgroundColor,
			borderWidth:0,
			y:30,
		},
		title : {
			show : false
		},
		tooltip : {
			trigger: 'axis'
		},
		legend: {
			data:data.legend,
			orient:'vertical',
			y:'center',
			x:'right',
			textStyle:{
				color: 'auto',
				fontSize: 14
			}
		},
		calculable : true,
		xAxis : [
			{
				type : 'category',
				boundaryGap : false,
				data : data.xAxis,
				axisTick:false,
				splitLine:charts_Config.BuliangqushiArea.splitxAxisLine,
				axisLine:false,
				axisLabel:{
					textStyle: charts_Config.BuliangqushiArea.axisLabelStyle
				}
			}
		],
		yAxis : [
			{
				type : 'value',
				axisTick:false,
				axisLine:false,
				splitLine:charts_Config.BuliangqushiArea.splitxAxisLine,
				max:10,
				axisLabel:{
					formatter: '{value} %',	
					textStyle: charts_Config.BuliangqushiArea.axisLabelStyle
				}
			}
		],
		
		series : [
			{
				itemStyle: charts_Config.BuliangqushiArea.itemStyleLine1,
				showAllSymbol: false,
				symbolSize:5,
				symbol:'circle',
				smooth:true,
				name:'预购',
				type:'line',				
				data:data.series.line1
			},
			{
				itemStyle:charts_Config.BuliangqushiArea.itemStyleLine2,
				showAllSymbol: false,
				symbolSize:5,
				symbol:'circle',
				smooth:true,
				name:'成交',
				type:'line',			
				data:data.series.line2
			}

		]
	};
	alert(1);			
	// 为echarts对象加载数据 
	buliangqushiArea.setOption(option); 
}


//图形3 当日
function drawbiaozhunBar(ec,data){
		
	var biaozhunBar = ec.init(document.getElementById('biaozhunBar')); 

	var option = {
		grid:{
			backgroundColor:charts_Config.biaozhunBar.gridBackgroundColor,
			borderWidth:0,
			y:50,
			x:30,
			x2:30,
		},
		title : {
			show : false
		},
		tooltip : {
			show : false
		},
		legend: {
			data:data.legend,
			x:'right',
			textStyle:{
				color: 'auto',
				fontSize: 14
			}
		},
		calculable : true,
		xAxis : [
			{
				type : 'category',
				data : data.xAxis,
				axisTick:false,				
				axisLine:false,
				splitLine:charts_Config.biaozhunBar.splitxAxisLine,
				axisLabel:{
					textStyle: charts_Config.biaozhunBar.axisLabelStyle
				},
				
			}
		],
		yAxis : [
			{
				type : 'value',
				axisTick:false,
				axisLine:false,
				splitLine:charts_Config.biaozhunBar.splityAxisLine,
				axisLabel:{
					textStyle: charts_Config.biaozhunBar.axisLabelStyle
				},
			}
		],
		series : [
			{
				name:'报警',
				type:'bar',
				itemStyle: charts_Config.biaozhunBar.itemStyleBar1,
				//symbol:'droplet',
				data:data.series.bar1,
				
			},
			{
				name:'降水量',
				type:'bar',
				itemStyle: charts_Config.biaozhunBar.itemStyleBar2,				
				symbol:'droplet',
				data:data.series.bar2,
				
			}
		]
	};

	biaozhunBar.setOption(option); 
}




//图形4 当日
function drawBuliangqushiLines(ec,data){
	
	
	var buliangqushiArea = ec.init(document.getElementById('buliangqushiLines')); 

	var option = {
		grid:{
			backgroundColor:charts_Config.BuliangqushiLines.gridBackgroundColor,
			borderWidth:0,
			y:30,
			x:50,
			x2:50,
		},
		title : {
			show : false
		},
		tooltip : {
			trigger: 'axis'
		},
		legend: {
			data:data.legend,
			x:'right',
			textStyle:{
				color: 'auto',
				fontSize: 14
			}
		},
		calculable : true,
		xAxis : [
			{
				type : 'category',
				boundaryGap : false,
				data : data.xAxis,
				axisTick:false,
				splitLine:charts_Config.BuliangqushiLines.splitxAxisLine,
				axisLine:false,
				axisLabel:{
					textStyle: charts_Config.BuliangqushiLines.axisLabelStyle
				}
			}
		],
		yAxis : [
			{
				type : 'value',
				axisTick:false,
				axisLine:false,
				splitLine:charts_Config.BuliangqushiLines.splityAxisLine,
				max:10,
				axisLabel:{
					formatter: '{value} %',	
					textStyle: charts_Config.BuliangqushiLines.axisLabelStyle
				}
			}
		],
		
		series : [
			{
				itemStyle: charts_Config.BuliangqushiLines.itemStyleLine1,
				showAllSymbol: false,
				symbolSize:5,
				symbol:'circle',
				smooth:true,
				name:'预购',
				type:'line',
				stack: '总量',
				data:data.series.line1
			},
			{
				itemStyle:charts_Config.BuliangqushiLines.itemStyleLine2,
				showAllSymbol: false,
				symbolSize:5,
				symbol:'circle',
				smooth:true,
				name:'成交',
				type:'line',
			
				data:data.series.line2
			}
		]
	};
				
	// 为echarts对象加载数据 
	buliangqushiArea.setOption(option); 

}




function getSum(data){	
	var Sum = 0;
	for(i in data){		
		Sum += Math.floor(data[i].value);
	}
	return Sum;
}

function getValue(data,name){	
	var value = '';
	for(i in data){		
		if(data[i].name == name){
			value = data[i].value
			break;
		}
	}
	return value;
}


function getPercent(value,sum){
	
	return (value/sum * 100).toFixed(2) + '%';
}



