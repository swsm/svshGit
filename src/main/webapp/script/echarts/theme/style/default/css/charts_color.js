charts_Config = {
	buliangPie:{
		legendColor:{
			fontSize:'18',
			fontWeight:'bold',
			color:'rgb(162,177,186)',
			fontFamily : '微软雅黑',
		},
		pieColorList:['rgb(230,242,28)','rgb(39,195,70)','rgb(16,212,208)','rgb(255,255,255)','rgb(215,127,17)']
	},
	BuliangqushiArea:{
		gridBackgroundColor:'rgba(0,0,0,0)',
		axisLabelStyle:{
			fontSize: 14,
			fontStyle: 'normal',
			color:'rgb(162,177,186)'
		},
		splitxAxisLine:{
			show:false,
			lineStyle:{
				width: 1,
				color: '#eee',
			}
		},
		splityAxisLine:{
			show:true,
			lineStyle:{
				width: 1,
				color: '#eee',
			}
		},
		itemStyleLine1:{
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
				
				color:'rgb(84,198,182)',
				borderColor:'rgba(0,0,0,0.5)',
				borderWidth:'2',
			 },
		},
		itemStyleLine2:{
			 normal: {
				label : {
					show: true, 
					position: 'top',
					textStyle: {
						color: 'rgb(254,185,20)'
					},
					formatter: "{c} %"
				},
				areaStyle: {
					type: 'default',
					color: 'rgba(254,185,20,0.2)'
				},
				lineStyle:{
					color:'rgb(254,185,20)'
				},
				
				color:'rgb(254,185,20)',
				borderColor:'rgba(0,0,0,0.5)',
				borderWidth:'2',
			 },
		},

	},
	biaozhunBar:{
		gridBackgroundColor:'rgba(0,0,0,0)',
		axisLabelStyle:{
			fontSize: 14,
			fontStyle: 'normal',
			color:'rgb(162,177,186)'
		},
		splitxAxisLine:{
			show:false,
			lineStyle:{
				width: 1,
				color: '#fff',
			}
		},
		splityAxisLine:{
			show:true,
		},
		itemStyleBar1:{
			 normal: {
				color:'rgb(230,242,8)',
				label : {
					show:true,
					position: 'top',
					formatter: "{c} %"
				},
			 },
		},
		itemStyleBar2:{
			normal: {
				color:'rgb(84,198,182)',
				label : {
					show:true,
					position: 'top',
					formatter: "{c} %"
				},
			},
			
		},
	},
	BuliangqushiLines:{
		gridBackgroundColor:'rgba(0,0,0,0)',	
		axisLabelStyle:{
			fontSize: 14,
			fontStyle: 'normal',
			color:'rgb(162,177,186)'
		},
		splitxAxisLine:{
			show:false,
			lineStyle:{
				width: 1,
				color: '#eee',
			}
		},
		splityAxisLine:{
			show:true,
			lineStyle:{
				width: 1,
				color: '#eee',
			}
		},
		itemStyleLine1:{
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
				color:'rgb(84,198,182)',
				borderColor:'rgba(0,0,0,0.5)',
				borderWidth:'2',
			 },
		},
		itemStyleLine2:{
			normal: {
				label : {
					show: true, 
					position: 'top',
					textStyle: {
						color: 'rgb(254,185,20)'
					},
					formatter: "{c} %"
				},
				areaStyle: {
					type: 'default',
					color: 'rgba(254,185,20,0.2)'
				},
				lineStyle:{
					color:'rgb(254,185,20)'
				},
				
				color:'rgb(254,185,20)',
				borderColor:'rgba(0,0,0,0.5)',
				borderWidth:'2',
				
			 },
		},
	},
	zhiliangBar:{
		gridBackgroundColor:'rgba(0,0,0,0)',
		axisLabelStyle:{
			fontSize: 16,
			fontStyle: 'normal',
			fontFamily:'微软雅黑',
			color:'rgb(162,177,186)'
		},
		splitxAxisLine:{
			show:false,
			lineStyle:{
				width: 1,
				color: '#fff',
			}
		},
		splityAxisLine:{
			show:true,
		},
		itemStyleBar1:{
			 normal: {
				color:'rgb(230,242,8)',
				label : {
					show:true,
					textStyle: {
						fontSize:24
					},
					position: 'top',
					formatter: "{c} "
				},
			 },
		},
		itemStyleBar2:{
			normal: {
				color:'rgb(84,198,182)',
				label : {
					show:true,
					textStyle: {
						fontSize:24
					},
					position: 'top',
					formatter: "{c} %"
				},
			},
			
		},
	},
	cellBar:{
		gridBackgroundColor:'rgba(0,0,0,0)',
		axisLabelStyle:{
			fontSize: 16,
			fontStyle: 'normal',
			fontFamily:'微软雅黑',
			color:'rgb(162,177,186)'
		},
		splitxAxisLine:{
			show:false,
			lineStyle:{
				width: 1,
				color: '#fff',
			}
		},
		splityAxisLine:{
			show:true,
		},
		itemStyleBar1:{
			 normal: {
				color:'rgb(230,242,8)',
				label : {
					show:true,
					textStyle: {
						fontSize:24
					},
					position: 'top',
					formatter: "{c} "
				},
			 },
		},
		itemStyleBar2:{
			normal: {
				color:'rgb(84,198,182)',
				label : {
					show:true,
					position: 'top',
					formatter: "{c} "
				},
			},
			
		},
	}
}
