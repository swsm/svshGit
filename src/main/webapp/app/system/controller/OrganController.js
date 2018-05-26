Ext.define('app.system.controller.OrganController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.organController',
	requires:['Ext.ux.TreePicker'],
	doQuery : function() {
	},
	doReset : function() {
	},
	doInsertRecord:function(){
		var organPanel = Ext.getCmp('id_organPanel');
		var row = organPanel.getSelectionModel().getSelection();
		//上级资源名称
		var parentOrganName = '';
		var parentOrganId = '';
		if(row.length!==0) {
			parentOrganId = row[0].get('id');
			parentOrganName = row[0].get('organName');
		}
		if(Ext.getCmp('id_insertOrganWin') == undefined) {
			// 不存在
			insertOrganWin = Ext.create("Ext.window.Window", {
				id : 'id_insertOrganWin',
				title : "新增",
				modal : true,
				width : 400,
				height : 430,
				layout : 'fit',
				constrain : true,
				resizable : true,
				closeAction : 'hide',
				closeToolText : '关闭', 
				border : false,
				listeners : {
					close : function(win, options) {
						//上级资源下拉列表 置 为 null
						Ext.getCmp('id_parentOrganForAdd').setValue(null);
						Ext.getCmp('id_parentOrganForAdd').setValue(null);
			    		Ext.getCmp('id_organNameForAdd').setValue(null),
		    		    Ext.getCmp('id_organCodeForAdd').setValue(null),
		    		    Ext.getCmp('id_dutyUsernameForAdd').setValue(null),
		    		    Ext.getCmp('id_organOrderForAdd').setValue(null),
		    		    Ext.getCmp('id_remarkForAdd').setValue(null),
						win.hide();
					}
				},
				items : [{
					xtype: "container",
			        layout: {
			          type: 'table',
			          columns: 1,
			          tableAttrs: {
			          	padding : '5px',
			            style: "border:true"
			          }
			        },
					items:[{
					      id:'id_parentOrganForAdd',
					      xtype: 'treepicker',
					      fieldLabel: '上级机构',
					      name : 'childSys',
					      labelSeparator:'：',
					      displayField: 'organName',
					      margin : '15 0 15 0',
					      labelAlign: 'right',
					      valueField: 'id',
					      width : '75%',
					      minPickerHeight: 30, //最小高度，不设置的话有时候下拉会出问题
					      minPickerWidth: 300, //最小宽度，不设置的话有时候下拉会出问题
					      autoScroll:true,
					      bodyStyle : 'overflow-x:hidden; overflow-y:scroll',
					      editable: false, //启用编辑，主要是为了清空当前的选择项
					      enableKeyEvents: true, //激活键盘事件
					      store: Ext.create('Ext.data.TreeStore',{
						      proxy: {
						          type : 'ajax',
									url : 'main/system/getOrganTree.mvc',
									reader : {
										type : 'json',
										rootProperty : 'rows'// 数据
									}
					      },
					      root: {
					          text: '根节点',
					          id: 'root',
					          expanded: true
					      },
					      clearOnLoad: true,
					      nodeParam: 'node' //传到后台的父节点的id
					      }),
					      listeners: {
					          'expand' : function(field, eOpts ) {
					    		  field.store.load({scope: this,
									    callback: function(records, operation, success) {
									        field.getPicker().expandAll();
									    }
								  });
					    	  }
					      }
					},{
						 xtype: "textfield",
						 id: "id_organNameForAdd",
						 labelSeparator:'：',
			             fieldLabel: "<span style=\'color:red\'>*</span>机构名称",
			             labelAlign : 'right',
			             enforceMaxLength:true,
						 maxLength : Tool.fieldmaxlength.V100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "<span style=\'color:red\'>*</span>机构代码",
			             id: "id_organCodeForAdd",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             enforceMaxLength:true,
						 maxLength : Tool.fieldmaxlength.V100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "负责人",
			             id: "id_dutyUsernameForAdd",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             enforceMaxLength:true,
						 maxLength : Tool.fieldmaxlength.V100,
			             width : '75%'
					},{
						 xtype: "numberfield",
			             fieldLabel: "<span style=\'color:red\'>*</span>排序",
			             id: "id_organOrderForAdd",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             maxValue: 99,
        			     minValue: 0,
        			     enforceMaxLength : true,
        			     maxLength : Tool.fieldmaxlength.N2,
        			     allowDecimals : false,
        			     decimalPrecision : 0,
        			     negativeText : '请输入0-99的整数',
        			     width : '75%'
					},{
						 xtype: "textareafield",
			             fieldLabel: "备注",
			             id: "id_remarkForAdd",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             maxLength: Tool.fieldmaxlength.V2000,
				         allowBlank: true,
				         enforceMaxLength: true,
				         wight : 200,
				         colspan: 3,
				         width : '75%'
					}					
				]
				}],
				bbar: [{
			      id: "menuItem1",
			      xtype: "tbfill"
			    },
			    {
			      xtype: "button",
			      iconCls : 'fa fa-save',
			      text: '保存',
			      listeners: {
			        click: function(item, event, options) {
			        	if(Ext.isEmpty(Ext.getCmp('id_organNameForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"机构名称\"不允许为空！');
			        		return;
			        	} else if(Ext.isEmpty(Ext.getCmp('id_organCodeForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"机构代码\"不允许为空！');
			        		return;
			        	}  else if(Ext.isEmpty(Ext.getCmp('id_organOrderForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为空！');
			        		return;
			        	}
			        	if (Ext.getCmp('id_organOrderForAdd').getValue() < 0) {
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为负数！');
			        		return;
			        	}
						Ext.Ajax.request( {
					    	url : 'main/system/saveOrgan.mvc',
					    	method : 'post',
					    	params : {
					    		parentOrganId : Ext.getCmp('id_parentOrganForAdd').getValue(),
					    		organName : Ext.getCmp('id_organNameForAdd').getValue(),
					    		organCode : Ext.getCmp('id_organCodeForAdd').getValue(),
					    		dutyUsername : Ext.getCmp('id_dutyUsernameForAdd').getValue(),
					    		organOrder : Ext.getCmp('id_organOrderForAdd').getValue(),
					    		remark : Ext.getCmp('id_remarkForAdd').getValue(),
					    		modual : '机构管理'
					    	},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		//操作成功
									var parentNode = Ext.getCmp('id_organPanel').getStore().getNodeById(
										Ext.getCmp('id_parentOrganForAdd').getValue());
						    		var opions = {node:parentNode};//进行封装                                
									var store = Ext.getCmp('id_organPanel').getStore();
//						    		if(parentNode != null && !parentNode.isLeaf()) {
//						    			store.load(opions);//局部加载
//						    		} else {
										store.load({scope: this,
											    callback: function(records, operation, success) {
											        Ext.getCmp('id_organPanel').expandAll();
											    }
										});
									
									function t() {
										store.each(
											function(recorde) {
												if (recorde.get("organCode") == Ext.getCmp('id_organCodeForAdd').getValue()) {
													console.info('select');
													Ext.getCmp('id_organPanel').getSelectionModel().select(recorde);
													return;
												}
											});
											Ext.getCmp('id_parentOrganForAdd').setValue(null);
						    		Ext.getCmp('id_organNameForAdd').setValue(null);
					    		    Ext.getCmp('id_organCodeForAdd').setValue(null);
					    		    Ext.getCmp('id_dutyUsernameForAdd').setValue(null);
					    		    Ext.getCmp('id_organOrderForAdd').setValue(null);
					    		    Ext.getCmp('id_remarkForAdd').setValue(null);
										}
									setTimeout(t,1000);	
//						    		}
//						    		Ext.getCmp('id_parentOrganForAdd').setValue(null);
//						    		Ext.getCmp('id_organNameForAdd').setValue(null),
//					    		    Ext.getCmp('id_organCodeForAdd').setValue(null),
//					    		    Ext.getCmp('id_dutyUsernameForAdd').setValue(null),
//					    		    Ext.getCmp('id_organOrderForAdd').setValue(null),
//					    		    Ext.getCmp('id_remarkForAdd').setValue(null),
									insertOrganWin.hide();
									Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	} else if (o.message == "organCodeExist") {
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"机构代码\"已存在！');
						    	}
					    	},
					    	failure : function() {
					    	}
						});
			        }
			      }
			    },
			    {
			      xtype: "button",
			      iconCls : 'fa fa-close',
			      text: '取消',
			      handler: function() {
			      	insertOrganWin.close();
			      }
			    }]
			  });
		}
		var organView = this.getView(); 
		//延时20ms执行下面的程序 因store ajax同步获取数据问题
		setTimeout(function(){
			if(Ext.isEmpty(Ext.getCmp('id_parentOrganForAdd').getValue())) {
				//设置上级资源 combobox的 值
				Ext.getCmp('id_parentOrganForAdd').setValue(parentOrganId);
				Ext.getCmp('id_parentOrganForAdd').setRawValue(parentOrganName);
			}
			organView.add(insertOrganWin);
			insertOrganWin.show();
		}, 20);
		
	},
	doUpdateRecord:function(){
		//根据organ_edit判断哪些字段可修改
		var readOnly = false;
		console.log(organ_edit);
		if(organ_edit === '1'){
			readOnly = true;
		}
		
		
		Ext.require(['Ext.ux.TreePicker']);
		
		var organPanel = Ext.getCmp('id_organPanel');
		var row = organPanel.getSelectionModel().getSelection();
		//上级资源名称
		var parentOrganName = '';
		var parentOrganId = '';
		var organId = '';
		var organName = '';
		var organCode = '';
		var dutyUsername = '';
		var organOrder = '';
		var remark = '';
		if(row.length!==0) {
			var parentOrgan = row[0].get('parentOrgan');
			if (!Ext.isEmpty(parentOrgan)) {
				parentOrganId = parentOrgan.id;
				parentOrganName = parentOrgan.organName;
			}
			organId = row[0].get('id');
			organName = row[0].get('organName');
			organCode = row[0].get('organCode');
			dutyUsername = row[0].get('dutyUsername');
			organOrder = row[0].get('organOrder');
			remark = row[0].get('remark');
		} else {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要更新的机构！');
			return;
		}
		if(Ext.getCmp('id_updateOrganWin') == undefined) {
			// 不存在
			updateOrganWin = Ext.create("Ext.window.Window", {
				id : 'id_updateOrganWin',
				title : "修改",
				modal : true,
				width : 400,
				height : 420,
				layout : 'fit',
				constrain : true,
				resizable : true,
				closeToolText : '关闭', 
				closeAction : 'hide',
				border : false,
				listeners : {
					close : function(win, options) {
						//上级资源下拉列表 置 为 null
						Ext.getCmp('id_parentOrganForUpdate').setValue(null);
						Ext.getCmp('id_parentOrganForUpdate').setValue(null);
			    		Ext.getCmp('id_organNameForUpdate').setValue(null),
		    		    Ext.getCmp('id_organCodeForUpdate').setValue(null),
		    		    Ext.getCmp('id_dutyUsernameForUpdate').setValue(null),
		    		    Ext.getCmp('id_organOrderForUpdate').setValue(null),
		    		    Ext.getCmp('id_remarkForUpdate').setValue(null),
						win.hide();
					}
				},
				items : [{
					xtype: "container",
			        layout: {
			          type: 'table',
			          columns: 1,
			          tableAttrs: {
			          	padding : '5px',
			            style: "border:true"
			          }
			        },
					items:[{
					      id:'id_parentOrganForUpdate',
					      xtype: 'treepicker',
					      fieldLabel: '上级机构',
					      name : 'childSys',
					      displayField: 'organName',
					      labelSeparator:'：',
					      labelAlign: 'right',
					      margin : '15 0 15 0',
					      valueField: 'id',
					      width : '75%',
//					      readOnly: readOnly,
					      minPickerHeight: 30, //最小高度，不设置的话有时候下拉会出问题
					      minPickerWidth: 300, //最小宽度，不设置的话有时候下拉会出问题
					      autoScroll:true,
					      bodyStyle : 'overflow-x:hidden; overflow-y:scroll',
					      editable: false, //启用编辑，主要是为了清空当前的选择项
					      enableKeyEvents: true, //激活键盘事件
					      store: Ext.create('Ext.data.TreeStore',{
					      	  autoLoad : true,
						      proxy: {
						          type : 'ajax',
									url : 'main/system/getOrganTree.mvc',
									reader : {
										type : 'json',
										rootProperty : 'rows'// 数据
									}
					      	  },
						      root: {
						          text: '根节点',
						          id: 'root',
						          expanded: true
						      },
						      clearOnLoad: true,
						      nodeParam: 'node' //传到后台的父节点的id
					      }),
					      listeners: {
							  'expand' : function(field, eOpts ) {
					    		  field.store.load({scope: this,
									    callback: function(records, operation, success) {
									        field.getPicker().expandAll();
									    }
								  });
					    	  }
					      }
					},{
						 xtype: "textfield",
						 id: "id_organNameForUpdate",
			             fieldLabel: "<span style=\'color:red\'>*</span>机构名称",
			             labelAlign : 'right',
			             labelSeparator:'：',
//			             readOnly: readOnly,
			             enforceMaxLength:true,
						 maxLength : 100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "<span style=\'color:red\'>*</span>机构代码",
			             id: "id_organCodeForUpdate",
			             labelAlign : 'right',
			             labelSeparator:'：',
//			             readOnly: readOnly,
			             enforceMaxLength:true,
						 maxLength : 100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "负责人",
			             id: "id_dutyUsernameForUpdate",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             enforceMaxLength:true,
					     maxLength : 100,
			             width : '75%'
					},{
						 xtype: "numberfield",
			             fieldLabel: "<span style=\'color:red\'>*</span>排序",
			             id: "id_organOrderForUpdate",
			             labelAlign : 'right',
			             labelSeparator:'：',
//			             readOnly: readOnly,
			             maxValue: 99,
        			     minValue: 0,
        			     enforceMaxLength : true,
        			     maxLength : 2,
        			     allowDecimals : false,
        			     negativeText : '请输入0-99的整数',
        			     decimalPrecision : 0, 
        			     width : '75%'
					},{
						 xtype: "textareafield",
			             fieldLabel: "备注",
			             id: "id_remarkForUpdate",
			             labelSeparator:'：',
//			             readOnly: readOnly,
			             labelAlign : 'right',
			             maxLength: 1000,
				         allowBlank: true,
				         enforceMaxLength: true,
				         wight : 200,
				         colspan: 3,
				         width : '75%'
					}					
				]
				}],
				bbar: [{
			      id: "menuItem2",
			      xtype: "tbfill"
			    },
			    {
			      xtype: "button",
			      iconCls : 'fa fa-save',
			      text: '保存',
			      listeners: {
			        click: function(item, event, options) {
						if(Ext.isEmpty(Ext.getCmp('id_organNameForUpdate').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"机构名称\"不允许为空！');
			        		return;
			        	} else if(Ext.isEmpty(Ext.getCmp('id_organCodeForUpdate').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"机构代码\"不允许为空！');
			        		return;
			        	} else if(Ext.isEmpty(Ext.getCmp('id_organOrderForUpdate').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为空！');
			        		return;
			        	}
			        	if (Ext.getCmp('id_organOrderForUpdate').getValue() < 0) {
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为负数！');
			        		return;
			        	}
			        	if(Ext.getCmp('id_organPanel').getSelectionModel().getSelection()[0].get('id')
			        		== Ext.getCmp('id_parentOrganForUpdate').getValue()) {
							//上级机构和机构是同一个  
							Tool.alert(Tool.str.msgConfirmTitle, '\"上级机构\"和\"机构名称\"不能是同一个机构！', {width:350});
							return;
						}
						Ext.Ajax.request( {
					    	url : 'main/system/updateOrgan.mvc',
					    	method : 'post',
					    	params : {
					    		parentOrganId : Ext.getCmp('id_parentOrganForUpdate').getValue(),
					    		organId : Ext.getCmp('id_organPanel').getSelectionModel().getSelection()[0].get('id'),
					    		organName : Ext.getCmp('id_organNameForUpdate').getValue(),
					    		organCode : Ext.getCmp('id_organCodeForUpdate').getValue(),
					    		dutyUsername : Ext.getCmp('id_dutyUsernameForUpdate').getValue(),
					    		organOrder : Ext.getCmp('id_organOrderForUpdate').getValue(),
					    		remark : Ext.getCmp('id_remarkForUpdate').getValue(),
					    		modual : '机构管理'
					    	},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		Ext.getCmp('id_parentOrganForUpdate').setValue(null);
						    		Ext.getCmp('id_organNameForUpdate').setValue(null),
					    		    Ext.getCmp('id_organCodeForUpdate').setValue(null),
					    		    Ext.getCmp('id_dutyUsernameForUpdate').setValue(null),
					    		    Ext.getCmp('id_organOrderForUpdate').setValue(null),
					    		    Ext.getCmp('id_remarkForUpdate').setValue(null),
									updateOrganWin.hide();
						    		//操作成功
//									var curNode = Ext.getCmp('id_organPanel').getStore().getNodeById(
//										Ext.getCmp('id_organPanel').getSelectionModel().getSelection()[0].get('id'));
//									var parentNode = curNode.parentNode;
//						    		var opions = {node:parentNode};//进行封装                                
//									var store = Ext.getCmp('id_organPanel').getStore();
//						    		if(parentNode != null && parentNode.id != 'root') {
//						    			store.load(opions);//局部加载
//						    		} else {
										Ext.getCmp('id_organPanel').getStore().load({scope: this,
											    callback: function(records, operation, success) {
											        Ext.getCmp('id_organPanel').expandAll();
											    }
										});
						    		//}
						    		Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	} else if (o.message == "organCodeExist") {
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"机构代码\"已存在！');
						    	} else if (o.message == "parentOrganNotChildOrgan") {
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"上级机构\"不能为该机构的下级机构！', {width:330});
						    	}
					    	},
					    	failure : function() {
					    	}
						});
			        }
			      }
			    },
			    {
			      xtype: "button",
			      iconCls : 'fa fa-close',
			      text: '取消',
			      handler: function() {
			      	updateOrganWin.close();
			      }
			    }]
			  });
		}
		var organView = this.getView();
		//延时20ms执行下面的程序 因store ajax同步获取数据问题
		setTimeout(function () {
			if(Ext.isEmpty(Ext.getCmp('id_parentOrganForUpdate').getValue())) {
				//设置上级资源 combobox的 值
				Ext.getCmp('id_parentOrganForUpdate').setValue(parentOrganId);
				Ext.getCmp('id_parentOrganForUpdate').setRawValue(parentOrganName);
				Ext.getCmp('id_organNameForUpdate').setValue(organName);
				Ext.getCmp('id_organCodeForUpdate').setValue(organCode);
				Ext.getCmp('id_dutyUsernameForUpdate').setValue(dutyUsername);
				Ext.getCmp('id_organOrderForUpdate').setValue(organOrder);
				Ext.getCmp('id_remarkForUpdate').setValue(remark);
			}
			organView.add(updateOrganWin);
			updateOrganWin.show();
		},20);
	
	},
	doDelete:function(){
		var organPanel = Ext.getCmp('id_organPanel');
		var row = organPanel.getSelectionModel().getSelection();
		if(row.length===0) {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgOptSelected);
			return;
		} else {
			var curNode = Ext.getCmp('id_organPanel').getStore().getNodeById(row[0].get('id'));
			if(!curNode.isLeaf()){
				var s = "</br>当前机构含有子部门，删除将会连同子部门一并删除，确定要删除吗？";
			} else {
				var s = "</br>确认删除此部门？";	
			}
			Ext.Msg.show( {
				title : Tool.str.msgConfirmTitle,
				msg : s,
				closeToolText : '关闭',
				buttons : Ext.MessageBox.OKCANCEL,
				icon : Ext.MessageBox.QUESTION,
				fn : function(b) {
                  	if (b == 'ok'){
						Ext.Ajax.request( {
					    	url : 'main/system/deleteOrgan.mvc',
					    	method : 'post',
					    	params : {
					    		organId : row[0].get('id'),
					    		organName : row[0].get("organName"),
					    		modual : '机构管理'
					    	},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		//操作成功
						    		var parentNode = curNode.parentNode.parentNode;
						    		var opions = {node:parentNode};//进行封装                                
									var store = Ext.getCmp('id_organPanel').getStore();
						    		if(parentNode != null && parentNode.id != 'root') {
						    			store.load(opions);//局部加载
						    		} else {
										store.load({scope: this,
											    callback: function(records, operation, success) {
											        Ext.getCmp('id_organPanel').expandAll();
											    }
										});
						    		}
						    		Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	}
					    	},
					    	failure : function() {
					    	}
					    });
                  	}
				}
			});
		}
	},
	doReflush:function(){
		var store = Ext.getCmp('id_organPanel').getStore();
		store.load({scope: this,
			    callback: function(records, operation, success) {
			        Ext.getCmp('id_organPanel').collapseAll();
			    }
		});
	},
	doSpread:function(){
			        Ext.getCmp('id_organPanel').expandAll();
	},
	doRender:function(){
	},
	doTreeDropMove : function(sourceNode, targetNode, dropPosition) {
		Ext.Ajax.request( {
	    	url : 'main/system/treeDropMove.mvc',
	    	method : 'post',
	    	params : {
	    		sourceNode : sourceNode,
	    		targetNode : targetNode,
	    		dropPosition : dropPosition
	    	},
	    	success : function(response, options) {
	    		var o = Ext.util.JSON.decode(response.responseText);
		    	if(o.message == 'success') {
		    		//操作成功
		    		var node = Ext.getCmp('id_organPanel').getStore().getNodeById(targetNode);
		    		var parentNode = node.parentNode;
		    		var opions = {node:parentNode};//进行封装                                
					var store = Ext.getCmp('id_organPanel').getStore();
		    		if(parentNode != null && parentNode.id != 'root') {
		    			store.load(opions);//局部加载
		    			parentNode.expand(true);
		    		} else {
						store.load({scope: this,
							    callback: function(records, operation, success) {
							        Ext.getCmp('id_organPanel').expandAll();
							    }
						});
		    		}
		    		Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
		    	}
	    	},
	    	failure : function() {
	    	}
	    });
	},
	doTreeDropCopy : function(sourceNode, targetNode, dropPosition) {
		Ext.Ajax.request( {
	    	url : 'main/system/treeDropCopy.mvc',
	    	method : 'post',
	    	params : {
	    		sourceNode : sourceNode,
	    		targetNode : targetNode,
	    		dropPosition : dropPosition
	    	},
	    	success : function(response, options) {
	    		var o = Ext.util.JSON.decode(response.responseText);
		    	if(o.message == 'success') {
		    		//操作成功
		    		//刷新复制后的节点
		    		var node = Ext.getCmp('id_organPanel').getStore().getNodeById(targetNode);
		    		var parentNode = node.parentNode;
		    		var opions = {node:parentNode};//进行封装                                
					var store = Ext.getCmp('id_organPanel').getStore();
		    		if(parentNode != null && parentNode.id != 'root') {
		    			store.load(opions);//局部加载
		    			parentNode.expand(true);
		    		} else {
						store.load({scope: this,
							    callback: function(records, operation, success) {
							        Ext.getCmp('id_organPanel').expandAll();
							    }
						});
						return;
		    		}
					//刷新原节点		    		
		    		node = Ext.getCmp('id_organPanel').getStore().getNodeById(sourceNode);
		    		parentNode = node.parentNode;
		    		opions = {node:parentNode};//进行封装                                
		    		if(parentNode != null && parentNode.id != 'root') {
		    			store.load(opions);//局部加载
		    			parentNode.expand(true);
		    		} else {
						store.load({scope: this,
							    callback: function(records, operation, success) {
							        Ext.getCmp('id_organPanel').expandAll();
							    }
						});
		    		}
		    		Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
		    	}
	    	},
	    	failure : function() {
	    	}
	    });
	}
});
