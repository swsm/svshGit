Ext.define('app.system.view.DictMng', {
	requires:['app.system.controller.DictController','app.system.viewmodel.DictIndexListModel','app.utils.statics.MesUtil'],
	init : function() {
		var form;
		var grid;
		var editDictWin;
		var editWinDictGrid;
		var dictIndexController;
		return {
			createDictGridPanel:function(){
//				var store = Tool.createDictStore();
				var modelCode="app.system.model.DictRecord";
				var store=Ext.create('Ext.data.Store', {
					autoLoad : false,
					pageSize: Tool.page.limit,
					listeners : {
//						'beforeload' : function() {
//							if(config){
//								Ext.apply(this.proxy.extraParams, config);
//							}
//						},
//						 'load' : function(store, records, successful, operation){
//						    if(flag == 1){
//						        store.insert(0, {
//						            dictKey : '',
//						            dictValue : '不限',
//						            remark : '不限'
//						        });
//						    }
//						 }
					},
					proxy : {
						type : 'ajax',
						url : 'main/system/getDict.mvc',
						reader : {
							type : 'json',
							rootProperty : "rows",
							totalProperty : "total"
						}
					},
					model:Ext.create(modelCode)
				});
				var ds = Tool.createDictStore({parentKey:'BOOL_FLAG'});
				ds.load();
				
				var dictGridPanel= Ext.create('Ext.grid.GridPanel', {
					layout : 'fit',
					reference : 'dictList',
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
					            		editor : new Ext.form.TextField({
					            			enforceMaxLength:true,
											maxLength : 25,
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
					            		editor : new Ext.form.TextField({
					            			enforceMaxLength:true,
											maxLength : 25,
					            			autoHeight : true
					            		})
					            	}, {
					            		header : '<span style=\'color:red\'>*</span>排序',
					            		width : 80,
					            		dataIndex : 'dictSort',
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
					            		editor : new Ext.form.NumberField({
					            			enforceMaxLength:true,
											maxLength : 4,
											allowNegative:false,
					            			autoHeight : true
					            		})
					            	},{
					            		header : '<span style=\'color:red\'>*</span>启用',
					            		width : 80,
					            		hidden : true,
					            		dataIndex : 'openFlag',
					            		lableAlign : 'left',
					            		sortable : true,
					            		align : 'left',
					            		editor : new Ext.form.ComboBox({
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
					            			enforceMaxLength:true,
					            			maxLength : Tool.fieldmaxlength.V2000,
					            	  })
					                } ],
					tbar : [ {
						bind : {
							text : '{insert}'
						},
						xtype : 'button',
						iconCls : 'fa fa-plus-circle',
						hidden : Tool.hasNotAuthor("app.system.view.DictMng.insert"),
						handler : 'doInsertRecord'
					}, {
						bind : {
							text : '{save}'
						},
						xtype : 'button',
						iconCls : 'fa fa-save',
						hidden : Tool.hasNotAuthor("app.system.view.DictMng.save"),
						handler : 'doUpdateRecord'
					}, {
						bind : {
							text : '{delete}'
						},
						xtype : 'button',
						iconCls : 'fa fa-minus-circle',
						hidden : Tool.hasNotAuthor("app.system.view.DictMng.delete"),
						handler : 'doDelete'
					}, {
						bind : {
							text : '{reflush}'
						},
						iconCls : 'fa fa-refresh',
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
					editWinDictGrid=this.createDictGridPanel();
					var dictController = Ext.create('app.system.controller.DictController');
					var indexViewModel = Ext.create("app.system.viewmodel.DictIndexListModel");
					editDictWin = Ext.create("Ext.window.Window", {
						title : "编辑字典值",
						id : 'id_dict_editDictWin',
						modal : true,
						width : 800,
						height : 500,
						layout : 'fit',
						constrain : true,
						resizable : true,
						closeAction : 'hide',
						closeToolText : '关闭', 
						controller:dictController,
						viewModel : indexViewModel,
						border : false,
						listeners : {
							close : function(win, options) {
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
				console.info(dictIndexController);
				dictIndexController.getView().add(editDictWin);
				editDictWin.show();
			},
			createView : function(obj) {
				this.createForm();
				this.createPanel();
				dictIndexController = Ext.create('app.system.controller.DictIndexController');
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
					} ],
					listeners : {
						resize : function( panel , width , height , oldWidth , oldHeight , eOpts ) {
							if (Ext.getCmp('id_dict_editDictWin') != null && !Ext.getCmp('id_dict_editDictWin').isHidden()) {
								Ext.getCmp('id_dict_editDictWin').show();
							}
							
						}
					}
				});
				Ext.apply(dict, obj);
				viewport.dictMng=this;
				return dict;
			},
			createForm : function() {
				if (!form) {
					var dictName = Ext.create('Ext.form.field.Text', {
						fieldLabel : '字典名称',
						maxWidth : 300,
						labelSeparator:'：',
						name : 'dictName',
						id : 'dictName',
						anchor : '96%'
					});
					var Util=app.utils.statics.MesUtil;
					var queryFormDefaultBtns=Util.createQueryFormDefaultBtn();
					form=Util.createQueryForm([dictName,{
						columnWidth : .25,
						align : 'right',
						layout : 'column',
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
						plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
							clicksToEdit : 1
						}) ],
						id:'id_dictIndexList',
						reference : 'dictIndexList',
						store : store,
						bbar :  Ext.create('app.utils.PagingToolbar', {
							store : store,
							displayInfo : true
						}),
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
							'render' : 'doRender'
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
							width : 230,
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
							},
							editor : new Ext.form.Text({
								enforceMaxLength:true,
								maxLength : Tool.fieldmaxlength.V100,
								autoHeight : true
							})
						}, {
							header : '<span style=\'color:red\'>*</span>字典名称',
							width : 230,
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
							},
							editor : new Ext.form.TextField({
								enforceMaxLength:true,
								maxLength : Tool.fieldmaxlength.V100,
								autoHeight : true
							})
						}, {
							header : '备注',
							width : 290,
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
								enforceMaxLength:true,
								maxLength : Tool.fieldmaxlength.V2000
							})
						}, {
							header : '编辑字典值',
							width : 110,
							lableAlign : 'center',
							dataIndex : 'dictKey',
							sortable : true,
							align : 'center',
							renderer : function(value, record, store) {
								if (!Ext.isEmpty(store.get('createUser'))) {
									value='<div class="fa fa-edit" style="font-size:18px;cursor:hand;" onclick=viewport.dictMng.createEditDictWin(\''
										+ value + '\')></div>';
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
						}, {
							bind : {
								text : '{save}'
							},
							xtype : 'button',
							iconCls :'fa fa-save',
							hidden : Tool.hasNotAuthor("app.system.view.DictIndexMng.save"),
							handler : 'doUpdateRecord'
						}, {
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
			}
		};
	}
});