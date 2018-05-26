Ext.define('app.system.view.RoleMng', {
	requires:['app.utils.statics.MesUtil'],
	init : function() {
		var form;
		var grid;
		return {
			showRoleResources : function(roleId, roleName){
				var center=viewport.down('center');
				var resCode='app.system.view.RoleResourceMng';
				var isOpen = false;
				center.items.each(function(item) {
					if (item.resCode == resCode && item.roleId == roleId) {
						center.setActiveTab(item);
						isOpen = true;
						return;
					} else if (item.resCode == resCode && item.roleId != roleId) {
						if(item.closable){
							 center.remove(item);
		                 }
					}
				});
				if (!isOpen) {
					var obj=Ext.create(resCode).init();
					var panel=obj.createView({
						border : false,
						closable : true,
						title : '配置资源-'+roleName,
						roleId : roleId,
						roleName : roleName,
						resCode : resCode
					});
					var p=center.add(panel);
					center.setActiveTab(p);
				}
			},
			createView : function(obj) {
				this.createForm();
				this.createPanel();
				var roleController = Ext.create('app.system.controller.RoleController');
				var roleViewModel = Ext.create("app.common.viewmodel.CommonModel");
				var rolePanel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:roleController,
					viewModel : roleViewModel,
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
				Ext.apply(rolePanel, obj);
				viewport.RoleMng=this;
				return rolePanel;
			},
			
			createForm : function() {
				if (!form) {
					var roleTypeStore=Tool.createDictStore({parentKey:'SYS_JSLBDM'}, 1);
					roleTypeStore.load();
					var roleNameText = {
							xtype : 'textfield',
							fieldLabel : '角色名称',
							maxWidth:300,
							labelSeparator:'：',
							id : 'roleMng_roleName'
					};
					var roleTypeComBox = {
							xtype : 'combobox',
							id : 'roleMng_roleType',
							fieldLabel : '角色类别',
							maxWidth:300,
							labelSeparator:'：',
							store : roleTypeStore,
							emptyText:'请选择',
							queryMode : 'local',
							displayField : 'dictValue',
							valueField : 'dictKey',
							editable:false
					};
					var MesUtil=app.utils.statics.MesUtil;
					var queryFormDefaultBtns=MesUtil.createQueryFormDefaultBtn();
					form = MesUtil.createQueryForm([roleNameText, roleTypeComBox, {
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
				var Util=app.utils.statics.MesUtil;
				if (!grid) {
					var store = Ext.create('Ext.data.Store', {
						pageSize : Tool.page.limit,
						requires : [ 'app.system.model.RoleRecord'],
						autoLoad : true,
						listeners : {
							'beforeload' : function() {
								Ext.apply(this.proxy.extraParams, {
									roleName : Ext.getCmp('roleMng_roleName').getValue(),
									roleType : Ext.getCmp('roleMng_roleType').getValue()
								});
							}
						},
						proxy : {
							type : 'ajax',
							url : 'main/system/getRole.mvc',
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
					var roleTypeStore=Tool.createDictStore({parentKey:'SYS_JSLBDM'});
					roleTypeStore.load();
					var boolFlagStore=Tool.createDictStore({parentKey:'BOOL_FLAG'});
					boolFlagStore.load();
					grid = Ext.create('Ext.grid.GridPanel', {
						plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
							clicksToEdit : 1
						}) ],
						id:'id_roleList',
						reference : 'roleList',
						store : store,
						bbar : Ext.create('app.utils.PagingToolbar', { 
							store : store,
							displayInfo : true
						}),
//						forceFit : true,
						title : '角色列表',
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
							header : '<span style=\'color:red\'>*</span>角色名称',
							width : 150,
							dataIndex : 'roleName',
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
							header : '<span style=\'color:red\'>*</span>角色类别',
							width : 150,
							dataIndex : 'roleType',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									for(var i=0;i<roleTypeStore.getCount();i++) {
					    				if(roleTypeStore.getAt(i).get('dictKey') == value){
					    					value = roleTypeStore.getAt(i).get('dictValue');
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
								store : roleTypeStore,  
							    valueField : 'dictKey',  
							    displayField : 'dictValue',  
							    editable : false,
								autoHeight : true
							})
						}, {
							header : '<span style=\'color:red\'>*</span>忽略决策',
							width : 180,
							dataIndex : 'ignoreDecesion',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									for(var i=0;i<boolFlagStore.getCount();i++) {
					    				if(boolFlagStore.getAt(i).get('dictKey') == value){
					    					value = boolFlagStore.getAt(i).get('dictValue');
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
								store : boolFlagStore,  
							    valueField : 'dictKey',  
							    displayField : 'dictValue',  
							    editable : false,
								autoHeight : true
							})
						}, {
							header : '备注',
							width :300,
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
								 maxLength: Tool.fieldmaxlength.V2000,
								enforceMaxLength:true,
								autoHeight : true
							})
						}, {
							header : '创建日期',
							width : 200,
							dataIndex : 'createDate',
							format : 'Y-m-d',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								if(!Ext.isEmpty(value)){
									value=Ext.util.Format.htmlEncode(value);
									var qtipValue=Ext.util.Format.htmlEncode(value);
									metaData.tdAttr='data-qtip="'+qtipValue+'"';
									return value;
								}
							},
							sortable : true
						}, {
							header : '配置资源',
							width : 100,
							sortable : true,
							align : 'center',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								var roleId = record.get('id');
								var roleName = record.get('roleName');
								if (!Ext.isEmpty(record.get('createDate'))) {
									value='<div class="fa fa-edit" style="font-size:18px;cursor:hand;" onclick="viewport.RoleMng.showRoleResources(\''+roleId+'\',\''
										+roleName+'\')"></div>';
									return value;
								}
							}
						} ],
						tbar : [ {
							bind : {
								text : '{insert}'
							},
							xtype : 'button',
							iconCls : 'fa fa-plus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.RoleMng.insert"),
							handler : 'doInsertRecord'
						}, {
							bind : {
								text : '{save}'
							},
							xtype : 'button',
							iconCls : 'fa fa-save',
							hidden : Tool.hasNotAuthor("app.system.view.RoleMng.save"),
							handler : 'doSave'
						}, {
							bind : {
								text : '{delete}'
							},
							xtype : 'button',
							iconCls : 'fa fa-minus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.RoleMng.delete"),
							handler : 'doDelete'
						}, {
							bind : {
								text : '复制于'
							},
							xtype : 'button',
							iconCls : 'fa fa-copy',
							hidden : Tool.hasNotAuthor("app.system.view.RoleMng.copy"),
							handler : 'doCopy'
						}, {
							bind : {
								text : '{disable}'
							},
							xtype : 'button',
							iconCls : 'fa fa-times-circle',
							hidden : true,
							handler : 'doDesable'
						}, {
							bind : {
								text : '{enable}'
							},
							xtype : 'button',
							iconCls : 'fa fa-check-circle',
							hidden : true,
							handler : 'doEnable'
						}, {
//							text : '刷新',
//							xtype : 'button',
//							handler : function() {
//								var grid = Ext.getCmp('id_roleList');
//								grid.getStore().load();
//							}
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