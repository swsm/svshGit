Ext.define('app.system.view.VarConfigMng', {
	requires:['app.utils.statics.MesUtil','app.system.controller.VarConfigController'/*,'app.system.model.VarConfigRecord'*/],
	init : function() {
		var varConfigTreePanel;
		var varConfigController;
		var MesUtil = app.utils.statics.MesUtil;
		return {
			createVarConfigTreePanel:function(){
				//移动树节点 事件 用于 做 移动 或者 复制 树节点功能
				var dragE;
				varConfigTreePanel = Ext.create('Ext.tree.Panel', {
					xtype : 'varConfigtree',
					title : '配置项管理',
					id : 'id_varConfigPanel',
					tbar : Ext.create('Ext.toolbar.Toolbar', {
						items : [ {
							bind : {
								text : '{insert}'
							},
							xtype : 'button',
							iconCls :'fa fa-plus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.VarConfigMng.insert"),
							handler : 'doInsertRecord'
						}, {
							bind : {
								text : '{update}'
							},
							xtype : 'button',
							iconCls :'fa fa-edit',
							hidden : Tool.hasNotAuthor("app.system.view.VarConfigMng.update"),
							handler : 'doUpdateRecord'
						}, {
							bind : {
								text : '{delete}'
							},
							xtype : 'button',
							iconCls :'fa fa-minus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.VarConfigMng.delete"),
							handler : 'doDelete'
						}, {
							bind : {
								text : '{spread}'
							},
							xtype : 'button',
							text:'展开所有层级',
							iconCls :'fa fa-folder-open-o',
							//hidden : Tool.hasNotAuthor("app.system.view.VarConfigMng.update"),
							handler : 'doSpread'
						},{
							bind : {
								text : '{reflush}'
							},
							xtype : 'button',
							iconCls :'fa fa-refresh',
							handler : 'doReflush'
						} ]
					}),
					columns : [ {
						text : '配置项名称',
						dataIndex : 'varName',
						width:340,
						xtype: 'treecolumn',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								return value;
							}
						}
					}, {
						text : '配置项编码',
						width:160,
						dataIndex : 'varDisplay',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								return value;
							}
						}
					}, {
						text : '配置项值',
						width:130,
						dataIndex : 'varValue',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								return value;
							}
						}
					}, {
						text : '类型',
						width:80,
						dataIndex : 'varType',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								//value = MesUtil.renderDictColumn("VAR_TYPE",value,Tool.getDicts("VAR_TYPE"));
								return MesUtil.renderDictColumn("VAR_TYPE",value,Tool.getDicts("VAR_TYPE"));
							}
						}
					},{
						text : '排序',
						width:100,
						align:'right',
						dataIndex : 'varOrder',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								return value;
							}
						}
					}, {
						text : '备注',
						width:240,
						dataIndex : 'remark',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								return value;
							}
						}
					} ],
					store: Ext.create('app.system.store.VarConfigStore',{}),
					border : false,
					rootVisible : false,
					useArrows : true,
					containerScroll : true,
					collapsible : false,
					viewConfig : {
						loadMask : false,
			            listeners: {
			            } 
					},
					autoScroll : false
				});
			},
			createView : function(obj) {
				window.top.varConfig_edit
				varConfig_edit = window.top.getVarValue('varConfig_edit') ? window.top.getVarValue('varConfig_edit') : 0;
				console.log(window.top.getVarValue('varConfig_edit') + '!!!');
				this.createVarConfigTreePanel();
				varConfigController = Ext.create('app.system.controller.VarConfigController');
				var varConfig = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					controller:varConfigController,
					items : [ {
						region : 'center',
						layout : 'fit',
						xtype : 'container',
						items : [ varConfigTreePanel ]
					} ],
					listeners : {
						resize : function( panel , width , height , oldWidth , oldHeight , eOpts ) {
							if (Ext.getCmp('id_insertVarConfigWin') != null && !Ext.getCmp('id_insertVarConfigWin').isHidden()) {
								Ext.getCmp('id_insertVarConfigWin').show();
							}
							if (Ext.getCmp('id_updateVarConfigWin') != null && !Ext.getCmp('id_updateVarConfigWin').isHidden()) {
								Ext.getCmp('id_updateVarConfigWin').show();
							}
						}
					}
				});
				Ext.apply(varConfig, obj);
				return varConfig;
			}
			
		};
	}
});