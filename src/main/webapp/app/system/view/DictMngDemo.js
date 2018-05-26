Ext.define('app.system.view.DictMngDemo', {
	requires:['app.system.controller.DictController','app.system.viewmodel.DictIndexListModel'],
	init : function() {
		var form;
		var grid;
		var editDictWin;
		var editWinDictGrid;
		var detailGridPanel;
		return {
			createDictGridPanel:function(){
				var store = Tool.createDictStore();
				var ds = Tool.createDictStore({parentKey:'BOOL_FLAG'});
				ds.load();
				
				var dictGridPanel= Ext.create('Ext.grid.GridPanel', {
					layout : 'fit',
					reference : 'dictList',
					//id:'id_dictList',
					store:store,
					bbar: Ext.create('app.utils.PagingToolbar', {
						store : store,
						displayInfo : true
					}) ,
					plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
						clicksToEdit : 1
					}) ],
					height : 450,
					viewConfig : {
						enableTextSelection:true ,
						selModel : Ext.create('Ext.selection.CheckboxModel', {
				        listeners:{
				        		'selectionchange':function(sm, selections){}
				        	}
						})
					},
					border : false,
					columns : [ 
					               	{
					            		header : '序号',
					            		width : 60,
					            		xtype : "rownumberer",
					            		align : 'center',
					            		sortable : false
					            	}, {
					            		header : '<span style=\'color:red\'>*</span>字典显示值',
					            		width : 190,
					            		align : 'left',
					            		dataIndex : 'dictValue',
					            		lableAlign : 'left',
					            		sortable : true,
					            		renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
											if (!Ext.isEmpty(value)) {
												value = Ext.util.Format.htmlEncode(value);
												var qtipValue = Ext.util.Format.htmlEncode(value);
												metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
												return value;
											}
										},
					            		editor : new Ext.form.TextArea({
					            			allowBlank : false,
					            			maxLength : Tool.fieldmaxlength.V100,
					            			autoHeight : true
					            		})
					            	}, {
					            		header : '<span style=\'color:red\'>*</span>字典Key',
					            		width : 80,
					            		dataIndex : 'dictKey',
					            		lableAlign : 'left',
					            		align : 'left',
					            		sortable : true,
					            		renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
											if (!Ext.isEmpty(value)) {
												value = Ext.util.Format.htmlEncode(value);
												var qtipValue = Ext.util.Format.htmlEncode(value);
												metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
												return value;
											}
										},
					            		editor : new Ext.form.TextArea({
					            			allowBlank : false,
					            			maxLength : Tool.fieldmaxlength.V2,
					            			autoHeight : true
					            		})
					            	}, {
					            		header : '<span style=\'color:red\'>*</span>排序',
					            		width : 80,
					            		dataIndex : 'dictSort',
					            		lableAlign : 'right',
					            		align : 'right',
					            		sortable : true,
					            		renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
											if (!Ext.isEmpty(value)) {
												value = Ext.util.Format.htmlEncode(value);
												var qtipValue = Ext.util.Format.htmlEncode(value);
												metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
												return value;
											}
										},
					            		editor : new Ext.form.TextArea({
					            			allowBlank : false,
					            			maxLength : Tool.fieldmaxlength.N2,
					            			autoHeight : true
					            		})
					            	},{
					            		header : '<span style=\'color:red\'>*</span>启用',
					            		width : 80,
					            		dataIndex : 'openFlag',
					            		lableAlign : 'left',
					            		sortable : true,
					            		align : 'left',
					            		editor : new Ext.form.ComboBox({
					            			allowBlank : false,
					            			reference:'ref_openFlag',
					            			store : ds,
					            	        displayField : "dictValue",
					            	        valueField : "dictKey",
					            			autoHeight : true,
					            			editable : false
					            		}),
					            		renderer:function(value, record, store){
					            			for(var i=0;i<ds.getCount();i++) {
					            				if(ds.getAt(i).get('dictKey') == value){
					            					value = ds.getAt(i).get('dictValue');
					            				}
					            			}
					            			return value;
					            		}
					            	},{
					            		header : '备注',
					            		width : 250,
					            		dataIndex : 'remark',
					            		lableAlign : 'left',
					            		sortable : true,
					            		align : 'left',
					            		renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
											if (!Ext.isEmpty(value)) {
												value = Ext.util.Format.htmlEncode(value);
												var qtipValue = Ext.util.Format.htmlEncode(value);
												metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
												return value;
											}
										},
					            		editor : new Ext.form.TextField({
					            			allowBlank : true,
					            			maxLength : Tool.fieldmaxlength.V2000
					            	  })
					                } ],
					tbar : [ {
						bind : {
							text : '{insert}'
						},
						xtype : 'button',
						icon:'resources/images/plus.png',
						hidden : Tool.hasNotAuthor("app.system.view.DictMng.insert"),
						handler : 'doInsertRecord'
					}, {
						bind : {
							text : '{save}'
						},
						xtype : 'button',
						icon:'resources/images/accept.png',
						hidden : Tool.hasNotAuthor("app.system.view.DictMng.save"),
						handler : 'doUpdateRecord'
					}, {
						bind : {
							text : '{delete}'
						},
						xtype : 'button',
						icon:'resources/images/delete.png',
						hidden : Tool.hasNotAuthor("app.system.view.DictMng.delete"),
						handler : 'doDelete'
					}, {
						bind : {
							text : '{reflush}'
						},
						icon:'resources/images/refresh.png',
						xtype : 'button',
						handler : 'doReflush'
					} ],
					listeners : {
						'render' : function(){
							
						}
					}
				});
				return dictGridPanel;
			},
			createEditDictWin : function(parentKey) {
				if (!editDictWin) {
					//if(!editWinDictGrid) {
						editWinDictGrid=this.createDictGridPanel();
					//}
					var dictController = Ext.create('app.system.controller.DictController');
					var indexViewModel = Ext.create("app.system.viewmodel.DictIndexListModel");
					editDictWin = Ext.create("Ext.window.Window", {
						//id : 'id_editDictWin',
						title : "编辑字典值",
						modal : true,
						width : 800,
						height : 500,
						layout : 'fit',
						constrain : true,
						resizable : true,
						closeAction : 'hide',
						controller:dictController,
						viewModel : indexViewModel,
						border : false,
						listeners : {
							close : function(win, options) {
								win.down('pagingtoolbar').down('combobox').setValue(15);
								win.hide();
							}
						},
						items : [ editWinDictGrid ]
					});
				}
				Ext.apply(
						editWinDictGrid.getStore().proxy.extraParams,
								{
									parentKey : parentKey
								});
				editWinDictGrid.getStore().load();
				editDictWin.show();
			},
			createView : function(obj) {
				this.createForm();
				this.createPanel();
				var dictIndexController = Ext.create('app.system.controller.DictIndexControllerDemo');
				var indexViewModel = Ext.create("app.system.viewmodel.DictIndexListModel");
				var dict = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 1px 0 !important;background:#FFFFFF',
					controller:dictIndexController,
					viewModel : indexViewModel,
					items : [ {
						region : 'north',
						height : 92,
						layout : 'fit',
						padding : '0 0 5 0',
						xtype : 'container',
						items : [ form ]
					}, {
						region : 'center',
						layout : 'fit',
						xtype : 'container',
						items : [ grid ]
					}, {
						region : 'east',
						layout : 'fit',
						xtype : 'container',
						items : [ detailGridPanel ]
					} ]
				});
				Ext.apply(dict, obj);
				viewport.dictMng=this;
				return dict;
			},
			createForm : function() {
				if (!form) {
					var dictName = Ext.create('Ext.form.field.Text', {
						fieldLabel : '字典名称',
						name : 'dictName',
						id : 'dictName',
						anchor : '96%'
					});
					var container = Ext.create('Ext.container.Container', {
						anchor : '96%',
						layout : 'column',
						border : false,
						items : [ {
							xtype : 'container',
							columnWidth : .30,
							layout : 'anchor',
							items : [ dictName ]
						}, {
							columnWidth : .25,
							align : 'right',
							layout : 'column',
							items : [ {
								xtype : 'container',
								layout : 'anchor',
								columnWidth : .3,
								items : {
									text : '',
									xtype : 'label'
								}
							}, {
								xtype : 'container',
								layout : 'anchor',
								columnWidth : .3,
								items : {
									text : '查询',
									align : 'right',
									xtype : 'button',
									iconCls : 'fa fa-search', 
									handler : 'doQuery'
								}
							}, {
								xtype : 'container',
								layout : 'anchor',
								columnWidth : .3,
								items : {
									text : '重置',
									xtype : 'button',
									iconCls : 'fa fa-repeat',
									handler : function() {
										Ext.getCmp('dictName').setValue(null);
									}
								}
							} ]
						} ]
					});
					form = Ext.create('Ext.form.Panel', {
						border : true,
						title : '查询条件',
						reference : 'dictIndexForm',
						bodyStyle : {
							padding : '5px'
						},
						fieldDefaults : {
							labelAlign : 'right',
							msgTarget : 'side'
						},
						items : [ container ]
					});
				}
			},
			createPanel : function() {
				if (!grid) {
					var store = Ext.create('Ext.data.Store', {
						requires : [ 'app.system.model.DictIndexRecord' ],
						autoLoad : false,
						pageSize : Tool.page.limit,
						listeners : {
							'beforeload' : function() {
								Ext.apply(this.proxy.extraParams, {
									'dictName' : Ext.getCmp('dictName')
											.getValue()
								});
							}
						},
						proxy : {
							type : 'ajax',
							url : 'main/system/getDictIndex.mvc',
							actionMethods : {
								read : "POST"
							},
							reader : {
								type : 'json',
								rootProperty : "rows",
								totalProperty : "total"
							}
						}
					});
					grid = Ext.create('Ext.grid.GridPanel', {
						id:'id_dictIndexList',
						reference : 'dictIndexList',
						store : store,
						bbar :  Ext.create('app.utils.PagingToolbar', {
							store : store,
							displayInfo : true
						}),
						forceFit : true,
						viewConfig : {
							enableTextSelection : true,
							selModel : Ext.create(
									'Ext.selection.CheckboxModel', {
										listeners : {
											'selectionchange' : function(sm,
													selections) {
											}
										}
									})
						},
						bind : {
							title : '{title}'
						},
						listeners : {
							'render' : 'doRender',
							//'itemclick' : 'doBeforeItemClick',
							'itemdblclick' : 'doBeforeItemDbClick'
						},
						border : true,
						columns : [ {
							header : '序号',
							width : 60,
							xtype : "rownumberer",
							sortable : false,
							align : 'center'
						}, {
							header : '<span style=\'color:red\'>*</span>字典编码',
							width : 180,
							dataIndex : 'dictKey',
							lableAlign : 'left',
							sortable : true,
							align : 'left',
							renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
								if (!Ext.isEmpty(value)) {
									value = Ext.util.Format.htmlEncode(value);
									var qtipValue = Ext.util.Format.htmlEncode(value);
									metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
									return value;
								}
							}
						}, {
							header : '<span style=\'color:red\'>*</span>字典名称',
							width : 200,
							align : 'left',
							lableAlign : 'left',
							dataIndex : 'dictName',
							sortable : true,
							renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
								if (!Ext.isEmpty(value)) {
									value = Ext.util.Format.htmlEncode(value);
									var qtipValue = Ext.util.Format.htmlEncode(value);
									metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
									return value;
								}
							}
						}, {
							header : '备注',
							width : 280,
							dataIndex : 'remark',
							lableAlign : 'left',
							sortable : true,
							align : 'left',
							renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
								if (!Ext.isEmpty(value)) {
									value = Ext.util.Format.htmlEncode(value);
									var qtipValue = Ext.util.Format.htmlEncode(value);
									metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
									return value;
								}
							}
						}, {
							header : '编辑字典值',
							width : 90,
							lableAlign : 'center',
							dataIndex : 'dictKey',
							sortable : true,
							align : 'center',
							renderer : function(value, record, store) {
								if (store.get('id').charAt(0) != '_') {
									value='<img src="resources/images/edit.png" style="color:blue; cursor:hand;" onclick=viewport.dictMng.createEditDictWin(\''
										+ value + '\')></img>';
									return value;
								}
								return "";
							}
						} ],
						tbar : [ {
							bind : {
								text : '{insert}'
							},
							xtype : 'button',
							iconCls :'fa fa-plus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.DictIndexMng.insert"),
							handler : 'doInsertRecord'
						} 
//						{
//							bind : {
//								text : '{save}'
//							},
//							xtype : 'button',
//							iconCls :'fa fa-save',
//							hidden : Tool.hasNotAuthor("app.system.view.DictIndexMng.save"),
//							handler : 'doUpdateRecord'
//						}
						, {
							bind : {
								text : '{delete}'
							},
							xtype : 'button',
							iconCls :'fa fa-minus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.DictIndexMng.delete"),
							handler : 'doDelete'
						}, {
							bind : {
								text : '{reflush}'
							},
							xtype : 'button',
							iconCls :'fa fa-refresh',
							handler : 'doReflush'
						} ]
					});
				}
				if (!detailGridPanel) {
					var container = Ext.create('Ext.container.Container', {
						layout: {
				            type: 'table',
				            columns: 1,
				            tableAttrs: {
					          	padding : '5px',
					            style: "border:true"
				            }
				        },
						items:[ {
							 xtype: "textfield",
							 id: "detailGridPanel_id",
				             hidden : true,
				             width : '85%'
						},{
							 xtype: "textfield",
							 id: "detailGridPanel_dictKey",
				             fieldLabel: "<span style='color:red'>*</span>字典编码",
				             labelAlign : 'right',
				             maxLength : 50,
				             enforceMaxLength : true,
				             margin : '15 0 15 0',
				             width : '85%'
						}, {
							 xtype: "textfield",
							 id: "detailGridPanel_dictName",
				             fieldLabel: "<span style='color:red'>*</span>字典名称",
				             labelAlign : 'right',
				             maxLength : 50,
				             enforceMaxLength : true,
				             margin : '15 0 15 0',
				             width : '85%'
						}, {
							 xtype: "textarea",
							 id: "detailGridPanel_remark",
				             fieldLabel: "备注",
				             labelAlign : 'right',
				             columns : 3,
				             maxLength : 200,
				             enforceMaxLength : true,
				             margin : '15 0 15 0',
				             width : '85%'
						}, {
						      xtype: "button",
						      iconCls :'fa fa-save',
						      margin : '15 0 0 120',
						      text: '保存',
						      listeners: {
						        click: function(item, event, options) {
						        	if(Ext.isEmpty(Ext.getCmp('detailGridPanel_dictKey').getValue())) {
						        		Tool.alert('系统提示', '"字典编码"不能为空！');
						        		return;
						        		
						        	} else if (Ext.isEmpty(Ext.getCmp('detailGridPanel_dictName').getValue())) {
						        		Tool.alert('系统提示', '"字典名称"不能为空！');
						        		return;
						        	}
						        	
									Ext.Ajax.request( {
								    	url : 'main/system/saveOrUpdateDictIndexDemo.mvc',
								    	method : 'post',
								    	params : {
								    		id : Ext.getCmp('detailGridPanel_id').getValue(),
								    		dictKey : Ext.getCmp('detailGridPanel_dictKey').getValue(),
											dictName : Ext.getCmp('detailGridPanel_dictName').getValue(),
											remark : Ext.getCmp('detailGridPanel_remark').getValue(),
											modual : '字典管理'
								    	},
								    	success : function(response, options) {
								    		var o = Ext.util.JSON.decode(response.responseText);
											if (o.message == 'success') {
												// 操作成功
												var store = Ext.getCmp('id_dictIndexList').getStore();
												store.load();
											} else if (o.message == 'dictKeyIsExist') {
												//dictKey 已存在
												Tool.alert('系统提示', '"字典编码"已存在！');
												return;
											}
								    	},
								    	failure : function() {
								    	}
									});
						        }
						      }
						},  {
						      xtype: "button",
						      iconCls : 'fa fa-repeat',
						      margin : '-32 0 0 220',
						      text: '重置',
						      listeners: {
						        click: 'detailGridPanel_reset'
						      }
						 }]
					});
					detailGridPanel = Ext.create('Ext.panel.Panel', {
						closable : true,
						closeAction : 'hide',
						margin : '0 0 0 3',
						border : true,
						title : 'grid操作',
						hidden : true,
						reference : 'detailGridPanel',
						width : 370,
						bodyStyle:'border-width : 1px 1px 1px 1px !important;background:#FFFFFF',
						items : [ container ], 
						
					});
				}
			}
		};
	}
});