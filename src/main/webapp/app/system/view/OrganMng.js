Ext.define('app.system.view.OrganMng', {
	requires:['app.system.controller.OrganController','app.system.store.OrganStore'],
	init : function() {
		var organTreePanel;
		var organController;
		return {
			createOrganTreePanel:function(){
				//移动树节点 事件 用于 做 移动 或者 复制 树节点功能
				var dragE;
				organTreePanel = Ext.create('Ext.tree.Panel', {
					xtype : 'organtree',
					title : '机构管理',
					id : 'id_organPanel',
					tbar : Ext.create('Ext.toolbar.Toolbar', {
						items : [ {
							bind : {
								text : '{insert}'
							},
							xtype : 'button',
							iconCls :'fa fa-plus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.OrganMng.insert"),
							handler : 'doInsertRecord'
						}, {
							bind : {
								text : '{update}'
							},
							xtype : 'button',
							iconCls :'fa fa-edit',
							hidden : Tool.hasNotAuthor("app.system.view.OrganMng.update"),
							handler : 'doUpdateRecord'
						}, {
							bind : {
								text : '{delete}'
							},
							xtype : 'button',
							iconCls :'fa fa-minus-circle',
							hidden : Tool.hasNotAuthor("app.system.view.OrganMng.delete"),
							handler : 'doDelete'
						}, {
							bind : {
								text : '展开所有层级'
							},
							xtype : 'button',
							iconCls :'fa fa-folder-open-o',
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
						text : '机构名称',
						dataIndex : 'organName',
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
						text : '机构代码',
						width:160,
						dataIndex : 'organCode',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								return value;
							}
						}
					}, {
						text : '负责人',
						width:130,
						dataIndex : 'dutyUsername',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view) {
							if (!Ext.isEmpty(value)) {
								value = Ext.util.Format.htmlEncode(value);
								var qtipValue = Ext.util.Format.htmlEncode(value);
								metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
								return value;
							}
						}
					}, {
						text : '排序',
						width:110,
						align:'right',
						dataIndex : 'organOrder',
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
					store: Ext.create('app.system.store.OrganStore',{}),
//					listeners: {
//		                itemcontextmenu: function(his, record, item, index, e){
//		                	var me = this; 
//    						e.preventDefault();
//		                	e.stopEvent();// 这两个很重要，否则点击鼠标右键还是会出现浏览器的选项
//		                	his.select();
//		                    var array = [ { 
//						                text : '添加部门',
//						                iconCls :'fa fa-plus-circle',
//						                handler : function() {
//						                	organController.doInsertRecord();
//						                }
//						            }, { 
//						                text : '修改部门', 
//						                iconCls :'fa fa-edit',
//						                handler : function() {
//						                	organController.doUpdateRecord();
//						                }
//						            }, { 
//						                text : '删除部门',
//						                iconCls :'fa fa-minus-circle',
//						                handler : function() {
//						                	organController.doDelete();
//						                }
//						            }]; 
//						    var nodemenu = new Ext.menu.Menu({ 
//						                items : array 
//						            }); 
//						    nodemenu.showAt(e.getXY());// 菜单打开的位置  
//		                } 
//		            },
					border : false,
					rootVisible : false,
					useArrows : true,
					containerScroll : true,
					collapsible : false,
					viewConfig : {
						loadMask : false,
//						plugins: {
//			                ptype: 'treeviewdragdrop',
//			                ddGroup: 'selDD',
//			                dragText : '已选中'
//			            },
			            listeners: {
//			                drop : function(node, data, overModel, dropPosition, options) {
//							    //ajax的操作把数据同步到后台数据库
//							    //organController.doTreeDrop(data.records[0].get('id'), overModel.get("id"), dropPosition);
//			                	var array = [ { 
//						                text : '复制',
//						                iconCls :'fa fa-copy',
//						                handler : function() {
//						                	organController.doTreeDropCopy(data.records[0].get('id'), overModel.get("id"), dropPosition);
//						                }
//						            }, { 
//						                text : '移动', 
//						                iconCls : 'fa fa-arrows',
//						                handler : function() {
//						                	organController.doTreeDropMove(data.records[0].get('id'), overModel.get("id"), dropPosition);
//						                }
//						            }
//						            ]; 
//							    var nodemenu = new Ext.menu.Menu({
//							                items : array 
//							            }); 
//							    nodemenu.showAt(dragE.getXY());// 菜单打开的位置
//						    },
//						    nodedragover : function(targetNode, position, dragData, e, eOpts ) {
//								if(targetNode.get("leaf")) {
//									targetNode.set('leaf',false);
//								}
//								dragE = e;
//						    }
			            } 
					},
					autoScroll : false
				});
			},
			createView : function(obj) {
				organ_edit = window.top.getVarValue('organ_edit') ? window.top.getVarValue('organ_edit') : 0;
				console.log(organ_edit + '!!!');
				this.createOrganTreePanel();
				organController = Ext.create('app.system.controller.OrganController');
				var organ = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					controller:organController,
					items : [ {
						region : 'center',
						layout : 'fit',
						xtype : 'container',
						items : [ organTreePanel ]
					} ],
					listeners : {
						resize : function( panel , width , height , oldWidth , oldHeight , eOpts ) {
							if (Ext.getCmp('id_insertOrganWin') != null && !Ext.getCmp('id_insertOrganWin').isHidden()) {
								Ext.getCmp('id_insertOrganWin').show();
							}
							if (Ext.getCmp('id_updateOrganWin') != null && !Ext.getCmp('id_updateOrganWin').isHidden()) {
								Ext.getCmp('id_updateOrganWin').show();
							}
						}
					}
				});
				Ext.apply(organ, obj);
				//viewport.organMng=this;
				return organ;
			}
			
		};
	}
});