Ext.define('app.system.view.ResourceMng', {
	requires:['app.utils.statics.MesUtil'],
	init : function() {
		var form;
		var grid;
		var parentResourceMenu;
		var parentResouceTreeGrid;
		var flag;
		return {
			createResourceMenu : function() {
				if (!parentResourceMenu) {
					parentResouceTreeGrid = Ext.create('Ext.tree.Panel', {
						reference : 'parentResourceList',
						rootVisible : false,
						height:400,
						animate:false,
//						scroll:"vertical",
						width:350,
						border:false,
						listeners:{
							itemclick:function(view,record,item,index,event,options){
								var resource = grid.getSelectionModel().getSelection()[0];
								resource.set('parentResName', record.data.text);
								resource.set('parentId', record.data.id);
								parentResourceMenu.hide();
							},
							cellkeydown:function (parentGrid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
				                if (e.getKey() == 13) { 
									var resource = grid.getSelectionModel().getSelection()[0];
									resource.set('parentResName', record.data.text);
									resource.set('parentId', record.data.id);
									parentResourceMenu.hide();
								}
			                }
						}
					});
					parentResourceMenu = Ext.create("Ext.menu.Menu", {
						items : [ parentResouceTreeGrid ]
					});
				}
			},
			
			createView : function(obj) {
				this.createForm();
				this.createPanel();
				this.createResourceMenu();
				var ResourceController = Ext.create('app.system.controller.ResourceController');
				var ResourceViewModel = Ext.create("app.common.viewmodel.CommonModel");
				var ResourcePanel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:ResourceController,
					viewModel : ResourceViewModel,
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
					} ]
				});
				Ext.apply(ResourcePanel, obj);
				viewport.ResourceMng=this;
				return ResourcePanel;
			},
			
			createForm : function() {
				if (!form) {
					var resTypeStore=Tool.createDictStore({parentKey:'SYS_ZYLBDM'}, 1);
					resTypeStore.load();
					var resNameText = {
							xtype : 'textfield',
							fieldLabel : '资源名称',
							labelSeparator:'：',
							maxWidth:300,
							id : 'resourceMng_resName'
					};
					var resTypeComBox = {
							xtype : 'combobox',
							id : 'resourceMng_resType',
							fieldLabel : '资源类型',
							maxWidth:300,
							labelSeparator:'：',
							store : resTypeStore,
							emptyText:'请选择',
							queryMode : 'local',
							displayField : 'dictValue',
							valueField : 'dictKey',
							editable:false
					};
					var MesUtil=app.utils.statics.MesUtil;
					var queryFormDefaultBtns=MesUtil.createQueryFormDefaultBtn();
					form = MesUtil.createQueryForm([resNameText, resTypeComBox, {
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
						autoLoad : false,
						pageSize : Tool.page.limit,
						requires : [ 'app.system.model.ResourceRecord'],
						listeners : {
							'beforeload' : function() {
								Ext.apply(this.proxy.extraParams, {
									resName:Ext.getCmp('resourceMng_resName').getValue(),
									resType:Ext.getCmp('resourceMng_resType').getValue()
								});
							}
						},
						proxy : {
							type : 'ajax',
							url : 'main/system/queryResource.mvc',
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
					var resTypeStore=Tool.createDictStore({parentKey:'SYS_ZYLBDM'});
					resTypeStore.load();
					var belongSystemStore=Tool.createDictStore({parentKey:'BELONG_SYSTEM'});
					belongSystemStore.load();
					grid = Ext.create('Ext.grid.GridPanel', {
						plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
							clicksToEdit : 1
						}) ],
						reference : 'resourceList',
						store : store,
						bbar : Ext.create('app.utils.PagingToolbar', { 
							store : store,
							displayInfo : true
						}),
//						forceFit : true,
						title : '资源列表',
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
							header : '<span style=\'color:red\'>*</span>资源名称',
							width : 200,
							dataIndex : 'resName',
							sortable : true,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							editor : new Ext.form.TextField({
								maxLength : Tool.fieldmaxlength.V100,
								enforceMaxLength:true,
								autoHeight : true
							})
						}, {
							header : '<span style=\'color:red\'>*</span>资源Code',
							width : 200,
							dataIndex : 'resCode',
							sortable : true,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							editor : new Ext.form.TextField({
								maxLength : 200,//不能有中文
								enforceMaxLength:true,
								autoHeight : true
							})
						}, {
							header : '<span style=\'color:red\'>*</span>资源类型',
							width : 100,
							dataIndex : 'resType',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									for(var i=0;i<resTypeStore.getCount();i++) {
					    				if(resTypeStore.getAt(i).get('dictKey') == value){
					    					value = resTypeStore.getAt(i).get('dictValue');
					    				}
					    			}
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							sortable : true,
							editor : new Ext.form.ComboBox({
								store : resTypeStore,  
							    valueField : 'dictKey',  
							    displayField : 'dictValue',  
							    editable : false,
								autoHeight : true
							})
						}, {
							header : '上级资源',
							width : 150,
							dataIndex : 'parentResName',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							sortable : true,
							editor : new Ext.form.TextField({
//								onTriggerClick : function(event){parentResourceMenu.showAt(event.getXY());},
							    editable : true,
							    id : 'parentResNameText',
							    listeners:{
									change:function(comboBox,newValue,oldValue){
										 Ext.fly(this.el).fireEvent('click');
									},
					                specialkey : function(field,e){
						                if (e.getKey() == 40) {
						                	parentResouceTreeGrid.getView().focusRow(0);
						                	parentResouceTreeGrid.getSelectionModel().select(0);
						                }
		                            },
		                            focus : function() {
		                            	Ext.fly(this.el).fireEvent('click');
									},
									render : function() {
								        Ext.fly(this.el).on('click', function(e, t) {
								        	Ext.Ajax.request({
								        		url : 'main/system/queryTreeForParent.mvc',
												method : 'post',
												params : {
													resName : Ext.getCmp('parentResNameText').getValue()
												},
												success : function(response, options) {
													if(response.responseText!='null'){
														var root=response.responseText;
														parentResouceTreeGrid.setRootNode(Ext.decode(root));
													}else{
														parentResouceTreeGrid.setRootNode(null);
													}
												}
											});
								        	var x=this.getX();
								        	var y=this.getY()+30;
								        	console.log(document.body.clientHeight);
								        	console.log(this.getY());
								        	if(document.body.clientHeight<this.getY()+400){
								        		y=this.getY()-400;
								        	}
								        	parentResourceMenu.showAt([x,y]);
							            });
									}
								}
							}),
							renderer:function(value,metaData,record){
								if (Ext.isEmpty(record.get('parentId'))) {
									return null;
								} else {
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							}
						}, {
							header : '<span style=\'color:red\'>*</span>所属系统',
							width : 180,
							dataIndex : 'belongSystem',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									for(var i=0;i<belongSystemStore.getCount();i++) {
					    				if(belongSystemStore.getAt(i).get('dictKey') == value){
					    					value = belongSystemStore.getAt(i).get('dictValue');
					    				}
					    			}
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							sortable : true,
							editor : new Ext.form.ComboBox({
								store : belongSystemStore,  
							    valueField : 'dictKey',  
							    displayField : 'dictValue',  
							    editable : false,
								autoHeight : true
							})
						}, {
							header : '备注',
							width :200,
							dataIndex : 'remark',
							sortable : true,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							editor : new Ext.form.TextField({
								maxLength : Tool.fieldmaxlength.V1000,
								enforceMaxLength:true,
								autoHeight : true
							})
						}, {
							header : '<span style=\'color:red\'>*</span>排序',
							width :80,
							dataIndex : 'resOrder',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							editor : new Ext.form.NumberField({
								maxLength : Tool.fieldmaxlength.N4,
								enforceMaxLength : true,
								minValue : 0,
								allowDecimals : false
							})
						}, {
							header : '创建日期',
							width : 150,
							dataIndex : 'createDate',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							sortable : true
						}],
						tbar : [ {
							bind : {
								text : '{insert}'
							},
							xtype : 'button',
							iconCls : 'fa fa-plus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.ResourceMng.insert"),
							handler : 'doInsertRecord'
						}, {
							bind : {
								text : '{save}'
							},
							xtype : 'button',
							iconCls : 'fa fa-save',
							hidden : Tool.hasNotAuthor("app.system.view.ResourceMng.save"),
							handler : 'doSave'
						}, {
							bind : {
								text : '{delete}'
							},
							xtype : 'button',
							iconCls : 'fa fa-minus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.ResourceMng.delete"),
							handler : 'doDelete'
						}, {
							bind : {
								text : '{disable}'
							},
							xtype : 'button',
							iconCls : 'fa fa-times-circle',
							hidden : true,
							handler : 'doDisable'
						}, {
							bind : {
								text : '{enable}'
							},
							xtype : 'button',
							iconCls : 'fa fa-check-circle',
							hidden : true,
							handler : 'doEnable'
						}, {
							bind : {
								text : '导出'
							},
							xtype : 'button',
							iconCls : 'fa fa-file-excel-o',
							handler : 'doExport'
						}, {
							bind : {
								text : '{reflush}'
							},
							xtype : 'button',
							iconCls : 'fa fa-refresh',
							handler : 'doReflush'
						} ]
					});
				}
			}
		};
	}
});