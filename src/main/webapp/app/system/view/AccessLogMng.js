Ext.define('app.system.view.AccessLogMng', {
	requires:['app.utils.statics.MesUtil'],
	init : function() {
		var form;
		var grid;
		return {
			createView : function(obj) {
				this.createForm();
				this.createPanel();
				var accessLogController = Ext.create('app.system.controller.AccessLogController');
				var accessLogViewModel = Ext.create("app.common.viewmodel.CommonModel");
				var accessLogPanel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:accessLogController,
					viewModel : accessLogViewModel,
					items : [ {
						region : 'north',
						height : 130,
						layout : 'fit',
						padding : '0 0 5 0',
						xtype : 'container',
						items : [ form ]
					}, {
						region : 'center',
						layout : 'fit',
						xtype : 'container',
						items : [ grid ]
					} ]
				});
				Ext.apply(accessLogPanel, obj);
				viewport.AccessLogMng=this;
				return accessLogPanel;
			},
			
			createForm : function() {
				if (!form) {
					var queryStartDate = Ext.create('Ext.ux.form.DateTimeField', {
//							xtype : 'datefield',
							fieldLabel : '开始时间',
							formatText : false,
							maxWidth:300,
							labelSeparator:'：',
							format:"Y-m-d",
							editable:false,
							value : Ext.Date.add(new Date(),Ext.Date.DAY,-1),
							id : 'accessLog_queryStartDate'
					});
					var queryEndDate = Ext.create('Ext.ux.form.DateTimeField', {
//							xtype : 'datefield',
							fieldLabel : '结束时间',
							formatText : false,
							maxWidth:300,
							labelSeparator:'：',
							format:"Y-m-d",
							editable:false,
							name : 'queryEndDate',
							id : 'accessLog_queryEndDate',
							value : new Date(),
							listeners:{
								'focus' : function(date,options){
									var startDate = Ext.getCmp('accessLog_queryStartDate').getValue();
									if(startDate !== null){
										Ext.getCmp('accessLog_queryEndDate').setMinValue(startDate);
									}else{
										Ext.getCmp('accessLog_queryEndDate').setMinValue();
									}
								}
						    }
					});
					var modualText = {
							xtype : 'textfield',
							fieldLabel : '模块名称',
							maxWidth:300,
							labelSeparator:'：',
							id : 'accessLog_modual'
					};
					var logContenText = {
							xtype : 'textfield',
							fieldLabel : '日志内容',
							maxWidth:300,
							padding : '5 0 0 0',
							labelSeparator:'：',
							id : 'accessLog_logConten'
					};
					var MesUtil=app.utils.statics.MesUtil;
					var queryFormDefaultBtns=MesUtil.createQueryFormDefaultBtn();
					form = MesUtil.createQueryForm([queryStartDate, queryEndDate, modualText, logContenText, {
						columnWidth : .25,
						align : 'right',
						layout : 'column',
						padding : '5 0 0 0',
						items : queryFormDefaultBtns,
						listeners : {
							afterrender:function(){
								form.getKeyMap().on(13, function() {
									grid.getStore().loadPage(1);
								});
							}
						}
					}]);
				}
			},
			createPanel : function() {
				if (!grid) {
					var store = Ext.create('Ext.data.Store', {
						pageSize : Tool.page.limit,
						autoLoad : true,
						listeners : {
							'beforeload' : function() {
								Ext.apply(this.proxy.extraParams, {
									queryStartDate : Ext.getCmp('accessLog_queryStartDate').getValue(),
									queryEndDate : Ext.getCmp('accessLog_queryEndDate').getValue(),
									modual : Ext.getCmp('accessLog_modual').getValue(),
									logConten : Ext.getCmp('accessLog_logConten').getValue()
								});
							}
						},
						proxy : {
							type : 'ajax',
							url : 'main/log/getAccessLog.mvc',
							actionMethods:{
					            read: "POST"
							},
							reader : {
								type : 'json',
								rootProperty : "rows",
								totalProperty : "total"
							}
						}
					});
					grid = Ext.create('Ext.grid.GridPanel', {
						reference : 'accessLogList',
						store : store,
						bbar : Ext.create('app.utils.PagingToolbar', { 
							store : store,
							displayInfo : true
						}),
//						forceFit : true,
						title : '访问日志列表',
						viewConfig : {
							enableTextSelection : true,
						},
						listeners : {
							'render' : 'doRender'
						},
						border : true,
						columns : [{
							header : '序号',
							width : 50,
							xtype : "rownumberer",
							sortable : false,
							align : 'center'
							
						}, {
							header : '访问时间',
							width : 180,
							dataIndex : 'createDate',
							sortable : true,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							}
						}, {
							header : '访问用户',
							width : 150,
							dataIndex : 'username',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							},
							sortable : true
						}, {
							header : '用户IP',
							width :180,
							dataIndex : 'remoteIp',
							sortable : true,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							}
						}, {
							header : '模块名称',
							width : 180,
							dataIndex : 'modual',
							sortable : true,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
							}
						}, {
							header : '日志内容',
							width : 235,
							dataIndex : 'logConten',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							},
							sortable : true
						}]
					});
				}
			}
		};
	}
});