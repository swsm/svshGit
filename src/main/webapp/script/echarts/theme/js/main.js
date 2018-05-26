pieColorList = ['rgb(241,211,35)','rgb(83,189,182)','rgb(164,138,212)','rgb(85,171,255)','rgb(121,152,181)'];
fenrongpieColorList = ['rgb(241,211,35)','rgb(85,171,255)','rgb(164,138,212)','rgb(83,189,182)','rgb(121,152,181)'];
huachengpieColorList = ['rgb(241,211,35)','rgb(164,138,212)','rgb(85,171,255)','rgb(83,189,182)','rgb(121,152,181)'];



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

		var _dangriPie = {
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
		drawDangriPie(ec,_dangriPie);



		var _dangriAera = {
			legend : ['意向','预购','成交'],
			xAxis : ['12/22','12/23','12/24','12/25','12/26','12/27','12/28'],
			series:{ 
				line : [5.8, 3.9, 5.1, 6.0, 4.0, 3.1, 6.0]
			}
		}

		//图2
		drawDangriAera(ec,_dangriAera);


		var _yujinbar = {
			legend : ['报警','降水量'],
			xAxis : ['报警一','报警二','报警三','报警四','报警五','报警六'],
			series:{ 
				bar : [2, 3, 1, 3, 1, 4]
			}
		}

		//图3
		drawYujinbar(ec,_yujinbar)



		var _fenrongPie = {
			legend : ['不良数','良品数'],
			series:{ 
				pie : [
					{value:335, name:'不良数'},
					{value:310, name:'良品数'}
				]
			}
		}

		//图4
		drawFenrongPie(ec,_fenrongPie);


		var _huachengPie = {
			legend : ['不良数','良品数'],
			series:{ 
				pie : [
					{value:335, name:'不良数'},
					{value:310, name:'良品数'}
				]
			}
		}

		//图5
		drawHuachengPie(ec,_huachengPie);


		var _fenrongBar = {
			legend : ['化成数量', '不良率'],
			xAxis : ['12/21','12/22','12/23','12/24','12/25','12/26','12/27'],
			yAxis : {
				name:['数量','百分比'] //柱状图左右Y坐标名字
			},
			series:{ 
				bar : [5100,5800,6100,6500,5900,6000,6200],
				line : [5.3,4.3,5.3,4.3,4.2,6.0,3.2]
			}
		}

		//图6
		drawFenrongBar(ec,_fenrongBar);

		var _huachengBar = {
			legend : ['化成数量', '不良率'],
			xAxis : ['12/21','12/22','12/23','12/24','12/25','12/26','12/27'],
			yAxis : {
				name:['数量','百分比']
			},
			series:{ 
				bar : [5100,5800,6100,6500,5900,6000,6200],
				line : [5.3,4.3,5.3,4.3,4.2,6.0,3.2]
			}
		}

		drawHuachengBar(ec,_huachengBar);

	}
);

//图形1 当日	
function drawDangriPie(ec,data){

	var dangriPie = ec.init(document.getElementById('dangriPie')); 

	var _Sum = getSum(data.series.pie)

	var option = {
		
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b}: {c} ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x: '120',
			y:'80',
			data:data.legend,
			formatter: function(name,i){
				
				return name + "   " + getPercent(getValue(data.series.pie,name),_Sum);
			},
			textStyle:{
				fontSize:'18',
				fontWeight:'bold',
				color:'rgb(162,177,186)',
				fontFamily : '微软雅黑',
			},
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
							var colorList = pieColorList;
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
function drawDangriAera(ec,data){

	var dangriAera = ec.init(document.getElementById('dangriAera')); 

	var option = {
		grid:{
			backgroundColor:'rgb(242,243,245)',
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
			show : false
		},
		calculable : true,
		xAxis : [
			{
				type : 'category',
				boundaryGap : false,
				data : data.xAxis,
				axisTick:false,
				splitLine:false,
				axisLine:false,
				axisLabel:{
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			}
		],
		yAxis : [
			{
				type : 'value',
				axisTick:false,
				axisLine:false,
				axisLabel:{
					formatter: '{value} %',	
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			}
		],
		
		series : [
			{
				itemStyle: {
					 normal: {
						label : {
							show: true, 
							position: 'top',
							textStyle: {
								color: 'rgb(84,198,182)'
							},
							formatter: "{c} %"
						},
						areaStyle: {
							type: 'default',
							color: 'rgba(84,198,182,0.2)'
						},
						lineStyle:{
							color:'rgb(84,198,182)'
						},
						
						color:'#fff',
						borderColor:'rgb(84,198,182)',
						borderWidth:'2',
						
					 },
					 
				},
				showAllSymbol: true,
				symbolSize:5,
				name:'预购',
				type:'line',
				data:data.series.line
			}
		]
	};
				
	// 为echarts对象加载数据 
	dangriAera.setOption(option); 

}


//图形3 当日
function drawYujinbar(ec,data){
		
	var yujinbar = ec.init(document.getElementById('yujinbar')); 

	var option = {
		grid:{
			backgroundColor:'rgb(242,243,245)',
			borderWidth:0,
			y:5,
		},
		title : {
			show : false
		},
		tooltip : {
			show : false
		},
		legend: {
			data:data.legend,
			show : false
		},
		calculable : true,
		xAxis : [
			{
				type : 'category',
				data : data.xAxis,
				axisTick:false,
				splitLine:{
					show:true,
					lineStyle:{
						width: 1,
						color: '#fff',
					}
				},
				axisLabel:{
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				},
				axisLine:false,
			}
		],
		yAxis : [
			{
				type : 'value',
				max : 5,
				axisTick:false,
				splitLine:false,
				axisLine:false,
				axisLabel:{
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				},
			}
		],
		series : [
			{
				name:'报警',
				type:'bar',
				itemStyle: {
					 normal: {
						color:'rgb(84,198,182)',
					 },
				},
				data:data.series.bar,
				
			}
		]
	};
	yujinbar.setOption(option); 
}


//图形4 当日	
function drawFenrongPie(ec,data){
	
	var fenrongPie = ec.init(document.getElementById('fenrongPie')); 

	var _Sum = getSum(data.series.pie)

	var option = {
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b}: {c} ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x: 'left',
			data:data.legend,
			x: '20',
			y:'80',
			formatter: function(name,i){
				
				return name + "   " + getPercent(getValue(data.series.pie,name),_Sum);
			},
			textStyle:{
				fontSize:'18',
				fontWeight:'bold',
				color:'rgb(162,177,186)',
				fontFamily : '微软雅黑',
			},

		},
		series: [
			{
				name:'访问来源',
				type:'pie',
				center : ['65%', '50%'],
				radius: ['60%', '90%'],

				avoidLabelOverlap: false,
				itemStyle : {
					normal : {
						color: function(params) {
							// build a color map as your need.
							var colorList = fenrongpieColorList;
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
								fontWeight : 'bold'
							}
						}
					}
				},
				data:data.series.pie
			}
		]
	};
	// 
	fenrongPie.setOption(option); 
}



//图形5 当日	
function drawHuachengPie(ec,data){
		
	var huachengPie = ec.init(document.getElementById('huachengPie')); 

	var _Sum = getSum(data.series.pie)

	var option = {
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b}: {c} ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x: 'left',
			data:data.legend,
			x: '20',
			y:'80',
			formatter: function(name,i){
				
				return name + "   " + getPercent(getValue(data.series.pie,name),_Sum);
			},
			textStyle:{
				fontSize:'18',
				fontWeight:'bold',
				color:'rgb(162,177,186)',
				fontFamily : '微软雅黑',
			},
		},
		series: [
			{
				name:'访问来源',
				type:'pie',
				center : ['65%', '50%'],
				radius: ['60%', '90%'],
				avoidLabelOverlap: false,
				itemStyle : {
					normal : {
						color: function(params) {
							// build a color map as your need.
							var colorList = huachengpieColorList;
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
								fontWeight : 'bold'
							}
						}
					}
				},
				data:data.series.pie
			}
		]
	};
	// 
	huachengPie.setOption(option); 
}



//图形6 当日	
function drawFenrongBar(ec,data){
	var fenrongBar = ec.init(document.getElementById('fenrongBar')); 

	var option = {
		grid:{
			backgroundColor:'rgb(242,243,245)',
			borderWidth:0,
			
		},
		title : {
			text: '动态数据',
			subtext: '纯属虚构',
			show:false
		},
		tooltip : {
			trigger: 'axis'
		},
		legend: {
			data:data.legend,
			x:'40'
		},
		
		dataZoom : {
			show : false,
			start : 0,
			end : 100
		},
		xAxis : [
			{
				type : 'category',
				boundaryGap : true,
				data : data.xAxis,
				axisTick:false,
				splitLine:{
					show:true,
					lineStyle:{
						width: 1,
						color: '#fff',
					}
				},
				axisLine:false,
				axisLabel:{
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			}
		],
		yAxis : [
			{
				type : 'value',
				scale: true,
				name : data.yAxis.name[0],
				max:8000,
				axisTick:false,
				splitLine:false,

				axisLine:{
					lineStyle:{
						color: 'rgb(85,171,255)',
						width: 0,
						type: 'solid'
					}
					
				},
				nameTextStyle:{
					fontSize:14,
				},
				
				axisLabel:{
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			},
			{
				type : 'value',
				scale: true,
				name : data.yAxis.name[1],
				max:8,
				min:0,
				splitNumber:4,			//分割
				axisTick:false,
				splitLine:false,
				nameTextStyle:{
					fontSize:14,
				},
				axisLine:{
					lineStyle:{
						color: 'rgb(248,211,35)',
						width: 0,
						type: 'solid'
					},
					
					
				},
				axisLabel:{
					formatter: '{value} %',	
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			}
		],
		series : [
			{
				name:data.legend[0],
				type:'bar',
				itemStyle: {
					 normal: {
						color:'rgb(85,171,255)',
					 },
				},
				data:data.series.bar
			},
			{
				name:data.legend[1],
				type:'line',
				yAxisIndex: 1,
				itemStyle: {
					 normal: {
						label : {
							show: true, 
							position: 'top',
							textStyle: {
								color: 'rgb(248,211,35)'
							},
							formatter: "{c} %"
						},

						lineStyle:{
							color:'rgb(248,211,35)'
						},
						
						color:'rgb(248,211,35)',
						borderColor:'rgba(0,0,0,0.2)',
						borderWidth:'3',
						
					 },
					 
				},
				showAllSymbol: true,
				symbolSize:6,
				data:data.series.line
			}
		]
	};
	
	fenrongBar.setOption(option); 

}


//图形7 当日
function drawHuachengBar(ec,data){
			
	var huachengBar = ec.init(document.getElementById('huachengBar')); 

	var option = {
		grid:{
			backgroundColor:'rgb(242,243,245)',
			borderWidth:0,
			
		},
		title : {
			text: '动态数据',
			subtext: '纯属虚构',
			show:false

		},
		tooltip : {
			trigger: 'axis'
		},
		legend: {
			data:data.legend,
			x:'40'
		},
		
		dataZoom : {
			show : false,
			start : 0,
			end : 100
		},
		xAxis : [
			{
				type : 'category',
				boundaryGap : true,
				data : data.xAxis,
				axisTick:false,
				splitLine:{
					show:true,
					lineStyle:{
						width: 1,
						color: '#fff',
					}
				},
				axisLine:false,
				axisLabel:{
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			}
		],
		yAxis : [
			{
				type : 'value',
				scale: true,
				name : data.yAxis.name[0],
				max:8000,
				axisTick:false,
				splitLine:false,

				axisLine:{
					lineStyle:{
						color: 'rgb(164,138,212)',
						width: 0,
						type: 'solid'
					}
					
				},
				nameTextStyle:{
					fontSize:14,
				},
				
				axisLabel:{
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			},
			{
				type : 'value',
				scale: true,
				name : data.yAxis.name[1],
				max:8,
				min:0,
				splitNumber:4,			//分割
				axisTick:false,
				splitLine:false,
				nameTextStyle:{
					fontSize:14,
				},
				axisLine:{
					lineStyle:{
						color: 'rgb(248,211,35)',
						width: 0,
						type: 'solid'
					},
					
					
				},
				axisLabel:{
					formatter: '{value} %',	
					textStyle: {
						fontSize: 14,
						fontStyle: 'normal',
						color:'rgb(162,177,186)'
					}
				}
			}
		],
		series : [
			{
				name:data.legend[0],
				type:'bar',
				itemStyle: {
					 normal: {
						color:'rgb(164,138,212)',
					 },
				},
				data:data.series.bar
			},
			{
				name:data.legend[1],
				type:'line',
				yAxisIndex: 1,
				itemStyle: {
					 normal: {
						label : {
							show: true, 
							position: 'top',
							textStyle: {
								color: 'rgb(248,211,35)'
							},
							formatter: "{c} %"
						},

						lineStyle:{
							color:'rgb(248,211,35)'
						},
						
						color:'rgb(248,211,35)',
						borderColor:'rgba(0,0,0,0.2)',
						borderWidth:'3',
						
					 },
					 
				},
				showAllSymbol: true,
				symbolSize:6,
				data:data.series.line
			}
		]
	};
	// 

	huachengBar.setOption(option); 
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



