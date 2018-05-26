Ext.define('app.system.view.RoleResourceMng', {
	requires : ['app.utils.statics.MesUtil'],
	init : function() {
		var loading;
		var form;
		var grid;
		return {
			createView : function(obj) {
				this.createForm();
				this.createPanel(obj.roleId, obj.roleName);
				var resourceController = Ext.create('app.system.controller.RoleResourceController');
				var resourceViewModel = Ext.create("app.common.viewmodel.CommonModel");
				var resourcePanel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:resourceController,
					viewModel : resourceViewModel,
					items : [
//					         {
//						region : 'north',
//						height : 92,
//						layout : 'fit',
//						padding : '0 0 5 0',
//						xtype : 'container',
//						items : [ form ]
//					},
					{
						region : 'center',
						layout : 'fit',
						xtype : 'container',
						items : [ grid ]
					} ]
				});
				Ext.apply(resourcePanel, obj);
				viewport.RoleResourceMng=this;
				return resourcePanel;
			},
			
			createForm : function() {
				if (!form) {
					var queryResourceStore = Ext.create('Ext.data.Store', {
						fields : ['id','resName','resCode'],
						pageSize : 9999999,
						proxy : {
							type : 'ajax',
							url : 'main/system/queryModules.mvc',
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
					var resNameComBox = {
							xtype : 'combobox',
//							name : 'resName',
							id : 'roleResourceMng_resName',
							fieldLabel : '选择资源',
							maxWidth:300,
							labelSeparator:'：',
							store : queryResourceStore,
							emptyText:'请选择',
//							queryMode : 'local',
							displayField : 'resName',
							valueField : 'id',
							editable:false
					};
					var MesUtil=app.utils.statics.MesUtil;
					var queryFormDefaultBtns=MesUtil.createQueryFormDefaultBtn();
					form = MesUtil.createQueryForm([resNameComBox, {
						columnWidth : .25,
						align : 'right',
						layout : 'column',
						items : queryFormDefaultBtns,
						listeners : {
							afterrender:function(){
								form.getKeyMap().on(13, function() {
									grid.getStore().load({
										params:{
											parentId : Ext.getCmp('roleResourceMng_resName').getValue(),
											flag : '1'
										}
									});
								});
							}
						}
					}]);
				}
			},
			
			createPanel : function(roleId, roleName) {
				if (!grid) {
			        var treeStoreSelectedSon = function(node,checked,flag) { 
			        	node.isHalfSelected = false;
			        	node.eachChild(function(child) { 
			        		child.set('checked', checked); 
			        		treeStoreSelectedSon(child,checked,false); 
			        	});
			        }; 
			        var treeStoreSelectedFather = function(node,checked) {
				        var parent = node.parentNode;
				        var flag = false; 
				        var hasUnCheckedChild = false;
				        var isHalfSelected = false;
				        if (null != parent) { 
				        	parent.eachChild(function(child) {
				        		if (child.get('checked') == true) {
				        			flag = true;
				        			if (child.isHalfSelected) {
				        				isHalfSelected = true;
				        			}
				        		} else if (child.get('checked') == false) {
				        			hasUnCheckedChild = true;
				        		}
				        	});
				        	parent.set('checked', flag);
				        	if ((flag && hasUnCheckedChild) || isHalfSelected) {
				        		parent.isHalfSelected = true;
				        		setNode(grid,parent,true);
				        	} else {
				    			parent.isHalfSelected = false;
				    			setNode(grid,parent,false);
				        	}
				        	treeStoreSelectedFather(parent,flag);
				        }
				    };
				    function setNode(tree,node,value){
				    	  var checkbox=getCheckbox(tree,node);
				    	  if (node.isHalfSelected != null && checkbox != null) {
				    	    if(value==true){
				    	      checkbox.className=checkbox.className.replace('Diy-mask','')+' Diy-mask';
				    	    }else{
				    	      checkbox.className=checkbox.className.replace('Diy-mask','');
				    	    }
				    	  }        
			    	};
			    	function setParentNode(tree,node,value){
			    		var parent = node.parentNode;
			    		if (null != parent) {
			    			parent.eachChild(function(child) {
				        		setNode(tree,child,child.isHalfSelected);
				        	});
			    			setParentNode(tree,parent,parent.isHalfSelected);
			    		}
				    	          
			    	};
			    	function getCheckbox(tree,node){
				      if(tree.getView().getNode(node)!=null){
				    	  var td=tree.getView().getNode(node).firstChild.firstChild.firstChild;
				    	  var length = td.getElementsByTagName('div').length;
				    	  var checkbox=td.getElementsByTagName('div')[length-2];
				    	  return checkbox;
				      }else{
				    	  return null;
				      }
			    	};
			    	function setChildStyle(tree,node){  
			    	  if (node.isExpanded()){
			    	    node.eachChild(function(child) {                             
			    	      if (child.isHalfSelected != null){
			    	        var checkbox=getCheckbox(tree,child);
			    	        if(checkbox != null){
			    	        	if(child.isHalfSelected == true){
					    	          checkbox.className=checkbox.className.replace(' Diy-mask','')+' Diy-mask';
				    	        }else{
				    	          checkbox.className=checkbox.className.replace(' Diy-mask','');
				    	        }
			    	        }
			    	        setChildStyle(tree,child);
			    	      }                     
			    	    });
			    	  }           
			    	};
					var store = Ext.create('Ext.data.TreeStore', {
						fields : ['id','parentId','resName','resCode','resType','halfSelected'],
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : 'main/system/getResourceTree.mvc',
							reader : {
								type : 'json',
								rootProperty : 'children',
							},
							extraParams: {
								roleId : roleId
						    },
						},
						listeners : {
							'beforeload' : function(s, o) {
//								loading = Ext.MessageBox.show({
//									  title:'提示信息',
//									  width : 250, 
//									  progressText: '加载中...',
//									  wait: true,
//									  closable: false,
//									  waitConfig: { interval: 5},
//									  msg:'资源数据加载中，请稍候……'
//								});
//								if (o.node.data.id != 'root') {
//									s.proxy.extraParams.parentId=o.node.data.id;
//								}
							},
//							'load' : function() {
//								grid.expandAll(function(){
//									  var checkNodes=grid.getChecked();
//									  for(var i=0;i<checkNodes.length;i++){
//										  var node=checkNodes[i];
//									      if(node.isLeaf() && node.get('checked')){
//									    	  treeStoreSelectedSon(checkNodes[i],true);
//									    	  treeStoreSelectedFather(checkNodes[i],true);
//									      }
//									  }
//									  loading.hide();
//								});
//							}
						}
					});
					var resTypeStore=Tool.createDictStore({parentKey:'SYS_ZYLBDM'});
					var checkChange = false;
					resTypeStore.load();
					grid = Ext.create('Ext.tree.Panel', {
						id:'id_roleResourceList',
						reference : 'roleResourceList',
						store : store,
//						forceFit : true,
						title : '资源列表',
						viewConfig:{
							enableTextSelection:true 
						},
						rootVisible : false,
						useArrows : true,
						containerScroll : true,
						collapsible : false,
						viewConfig : {
							loadMask : false
						},
						lines : true,
						autoScroll : false,
						border : true,
						columns : [ {
							text : '资源名称',
							dataIndex : 'resName',
							width:300,
							xtype: 'treecolumn',
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							}
						}, {
							text : '资源代码',
							dataIndex : 'resCode',
							width:300,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							}
						}, {
							text : '资源类别',
							dataIndex : 'resType',
							width:100,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
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
						} ],
						listeners:{
							checkchange:function(node,checked,options){
								checkChange = true;
								treeStoreSelectedSon(node,checked);
								treeStoreSelectedFather(node,checked);
							},
							afteritemexpand:function(node,index,item,opt){
								setParentNode(grid,node,node.isHalfSelected);
								setChildStyle(grid,node);
							},
							itemcollapse:function(node,opt){
								setParentNode(grid,node,node.isHalfSelected);
							}
						},
						tbar : [ {
							text : '保存',
							xtype : 'button',
							iconCls : 'fa fa-save',
							handler : function(){
								if(!checkChange){
									return;
								}
								var resIdStr = "";
								var selNodes = grid.getChecked();
								for(var i=0;i<selNodes.length;i++){
								  	var node=selNodes[i];
								  	resIdStr = resIdStr + node.get('id') + "@";
								}
								Ext.Ajax.request({
									url : 'main/system/updateRoleResource.mvc',
									method : 'post',
									params : {
										resIdStr : resIdStr,
										roleId : roleId,
										roleName : roleName,
										modual : '资源配置'
									},
									success : function(response, options) {
										var o = Ext.decode(response.responseText);
										if (o.message == 'success') {
											checkChange = false;
											Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
										}
									},
									failure : function() {
									}
								});
							}
						},{
							bind : {
								text : '导出'
							},
							xtype : 'button',
							iconCls : 'fa fa-file-excel-o',
							handler : function(){
								if (Ext.getCmp('id_roleResourceList').getChecked().length == 0) {
									Tool.alert(Tool.str.msgConfirmTitle, "所导出数据为空！");
									return;
								} else {
									var resIdStr = "";
									var selNodes = grid.getChecked();
									for(var i=0;i<selNodes.length;i++){
									  	var node=selNodes[i];
									  	resIdStr = resIdStr + node.get('id') + "@";
									}
									Ext.Ajax.request({
										url : 'main/system/updateRoleResource.mvc',
										method : 'post',
										params : {
											resIdStr : resIdStr,
											roleId : roleId,
											roleName : roleName,
											modual : '资源配置'
										},
										success : function(response, options) {
											var o = Ext.decode(response.responseText);
											if (o.message == 'success') {
//												var messageBox = Ext.create('Ext.window.MessageBox',{
//													closeToolText : "关闭"
//												});
												Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>' + "确认导出？",function(btn) {
													if (btn == 'yes') {
														window.location.href=encodeURI(encodeURI("main/system/exportResourceTree.mvc?roleId=" + roleId));
													}
												});
											}
										},
										failure : function() {
										}
									});
								}
								
							}
						},{
							text : '收起所有层级',
							xtype : 'button',
							iconCls :'fa fa-folder-o',
							handler : function(){
								grid.collapseAll();
							}
						},{
							text : '展开所有层级',
							xtype : 'button',
							iconCls : 'fa fa-folder-open-o',
							handler : function(){
								grid.expandAll();
							}
						}]
					});
				}
			}
		};
	}
});