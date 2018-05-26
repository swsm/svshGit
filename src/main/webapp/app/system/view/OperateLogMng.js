Ext.define('app.system.view.OperateLogMng', {
	requires:['app.utils.statics.MesUtil'],
	init : function() {
		var form;
		var grid;
		return {
			createView : function(obj) {
				this.createForm();
				this.createPanel();
				var operateLogController = Ext.create('app.system.controller.OperateLogController');
				var operateLogViewModel = Ext.create("app.common.viewmodel.CommonModel");
				var operateLogPanel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:operateLogController,
					viewModel :operateLogViewModel,
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
				Ext.apply(operateLogPanel, obj);
				viewport.OperateLogMng=this;
				return operateLogPanel;
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
							id : 'operateLog_queryStartDate'
					});
					var queryEndDate = Ext.create('Ext.ux.form.DateTimeField', {
//						xtype : 'datefield',
						fieldLabel : '结束时间',
						formatText : false,
						labelSeparator:'：',
						maxWidth:300,
						format:"Y-m-d",
						editable:false,
						id : 'operateLog_queryEndDate',
						value : new Date(),
						listeners:{
							'focus' : function(date,options){
								var startDate = Ext.getCmp('operateLog_queryStartDate').getValue();
								if(startDate !== null){
									Ext.getCmp('operateLog_queryEndDate').setMinValue(startDate);
								}else{
									Ext.getCmp('operateLog_queryEndDate').setMinValue();
								}
							}
					    }
					});
					var modualText = {
							xtype : 'textfield',
							fieldLabel : '模块名称',
							maxWidth:300,
							labelSeparator:'：',
							id : 'operateLog_modual'
					};
					var logContenText = {
							xtype : 'textfield',
							fieldLabel : '日志内容',
							maxWidth:300,
							padding : '5 0 0 0',
							labelSeparator:'：',
							id : 'operateLog_logContent'
					};
					var operationUserText = {
							xtype : 'textfield',
							fieldLabel : '操作用户',
							maxWidth:300,
							padding : '5 0 0 0',
							labelSeparator:'：',
							id : 'operateLog_operationUser'
					};
					var operateIpText = {
							xtype : 'textfield',
							fieldLabel : '用户IP',
							maxWidth:300,
							padding : '5 0 0 0',
							labelSeparator:'：',
							id : 'operateLog_operateIp'
					};
					var MesUtil=app.utils.statics.MesUtil;
					var queryFormDefaultBtns=MesUtil.createQueryFormDefaultBtn();
					form = MesUtil.createQueryForm([queryStartDate, queryEndDate, modualText, operationUserText, operateIpText, logContenText, {
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
								console.log(Ext.getCmp('operateLog_queryStartDate').getValue());
								Ext.apply(this.proxy.extraParams, {
									queryStartDate : Ext.getCmp('operateLog_queryStartDate').getValue(),
									queryEndDate : Ext.getCmp('operateLog_queryEndDate').getValue(),
									modual : Ext.getCmp('operateLog_modual').getValue(),
									logContent : Ext.getCmp('operateLog_logContent').getValue(),
									operationUser : Ext.getCmp('operateLog_operationUser').getValue(),
									operateIp : Ext.getCmp('operateLog_operateIp').getValue(),
								});
							}
						},
						proxy : {
							type : 'ajax',
							url : 'main/log/getOperateLog.mvc',
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
						reference : 'operateLogList',
						store : store,
						bbar : Ext.create('app.utils.PagingToolbar', { 
							store : store,
							displayInfo : true
						}),
//						forceFit : true,
						title : '操作日志列表',
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
							header : '操作时间',
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
							header : '操作用户',
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
							dataIndex : 'logContent',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							},
							sortable : true
						}],
						tbar: [{
							bind : {
								text : '导出'
							},
							xtype : 'button',
							iconCls : 'fa fa-file-excel-o',
							handler : 'doExport'
						}]
					});
				}
			}
		};
	}
});