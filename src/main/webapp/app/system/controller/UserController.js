Ext.define('app.system.controller.UserController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.usercontroller',
	//requires:['app.utils.Tool'],
	doQuery : function() {
		var userList = this.lookupReference('userList');
		var store = userList.getStore();
		store.loadPage(1,{params :{
				username : Ext.getCmp('username').getValue(),
				truename : Ext.getCmp('truename').getValue(),
				organName : Ext.getCmp('id_organTree').getValue(),
				workNo : Ext.getCmp('id_UserMng_workNo').getValue()
				}
		});
	},
	doReset : function() {
		Ext.getCmp('username').setValue(null),
		Ext.getCmp('truename').setValue(null),
		Ext.getCmp('id_organTree').setValue(null),
		Ext.getCmp('id_UserMng_workNo').setValue(null)
	},
	doInsertRecord : function() {
		if(Ext.getCmp('id_insertUserWin') == undefined) {
			var obj = Tool.getErpRequiredField("UserMng");
			var erpFlag = obj.erpFlag;
			var array = [];
			if (erpFlag) {
				array = obj.json.split(",");
			}
			var roleStore = Ext.create('Ext.data.Store', {
				pageSize: 100,
				autoLoad : false,
				listeners : {
					'beforeload' : function() {
					}
				},
				proxy : {
					type : 'ajax',
					url : 'main/system/getRoleForUserMng.mvc',
					reader : {
						type : 'json',
						rootProperty : "rows"
					}
				}, 
				model : Ext.create('Ext.data.Model', {
							fields : [ 	{name : 'id', type : 'string'}, 
										{name : 'roleName', type : 'string'}
									],
							changeName : function() {
							}
						})
			});
			roleStore.load();
			
			// 不存在
			var teleRgx = /^0\d{2,3}-?\d{7,8}$/;
			var mobileRgx = /^1\d{10}$/;
			var emailRgx = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
			insertUserWin = Ext.create("Ext.window.Window", {
				id : 'id_insertUserWin', title : "新增", modal : true, width : 700, height : 500, layout : 'fit', constrain : true,
				resizable : true, closeToolText : '关闭',  closeAction : 'hide', border : false,
				listeners : {
					close : function(win, options) {
						Ext.getCmp('id_truenameForAdd').setValue(null);
		    			Ext.getCmp('id_workNoForAdd').setValue(null);
		    		    Ext.getCmp('id_usernameForAdd').setValue(null);
			    		Ext.getCmp('id_passwordForAdd').setValue(null);
			    		Ext.getCmp('id_organForAdd').setValue(null);
			    		Ext.getCmp('id_mobileForAdd').setValue(null);
			    		Ext.getCmp('id_telephoneForAdd').setValue(null);
			    		Ext.getCmp('id_roleForAdd').setValue(null);
			    		Ext.getCmp('id_wechatNameForAdd').setValue(null);
			    		Ext.getCmp('id_emailForAdd').setValue(null);
			    		Ext.getCmp('id_addressForAdd').setValue(null);
			    		Ext.getCmp('id_remarkUserForAdd').setValue(null);
						win.hide();
					}
				},
				items : [{
					xtype: "container",
			        layout: {
			          type: 'table',
			          columns: 2,
			          tableAttrs: {
			          	padding : '5px',
			            style: "border:true"
			          }
			        },
					items:[ {
						 xtype: "textfield",
						 id: "id_truenameForAdd",
			             fieldLabel: "<span style='color:red'>*</span>姓名",
			             labelAlign : 'right',
			             enforceMaxLength:true,
			             labelSeparator:'：',
						 maxLength : Tool.fieldmaxlength.V100,
			             margin : '15 0 10 0',
			             width : '85%'
					}, {
						 xtype: "textfield",
						 id: "id_workNoForAdd",
			             fieldLabel: "<span style='color:red'>*</span>工号",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             enforceMaxLength:true,
			             maxLength : Tool.fieldmaxlength.V100,
			             margin : '15 0 10 0',
			             width : '85%'
					}, {
						 xtype: "textfield",
						 id: "id_usernameForAdd",
			             fieldLabel: "<span style='color:red'>*</span>用户名",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             enforceMaxLength:true,
			             maxLength : Tool.fieldmaxlength.V100,
			             width : '85%'
					},  {
						 xtype: "textfield",
						 id: "id_passwordForAdd",
			             fieldLabel: "<span style='color:red'>*</span>密码",
			             inputType: 'password',
			             enforceMaxLength:true,
			             labelSeparator:'：',
			             maxLength : Tool.fieldmaxlength.V100,
			             labelAlign : 'right',
			             width : '85%'
					}, {
						 xtype: "textfield",
						 id: "id_mobileForAdd",
			             fieldLabel: "手机",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             enforceMaxLength:true,
			             maxLength : Tool.fieldmaxlength.V100,
			             width : '85%',
			             regex: mobileRgx,
			             regexText:"手机号码格式不正确！"
					}, {
						 xtype: "textfield",
						 id: "id_telephoneForAdd",
			             fieldLabel: "电话",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             enforceMaxLength:true,
			             maxLength : Tool.fieldmaxlength.V100,
			             width : '85%',
			             regex: teleRgx,
			             regexText:"电话号码格式不正确！"
					}, {
						 xtype: "textfield",
						 id: "id_wechatNameForAdd",
			             fieldLabel: "<span style='color:red'>*</span>微信名",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             enforceMaxLength:true,
			             maxLength : Tool.fieldmaxlength.V100,
			             width : '85%'
					}, {
						 xtype: "combobox",
						 id: "id_roleForAdd",
			             fieldLabel: "<span style='color:red'>*</span>角色",
			             displayField :'roleName',
			             valueField : 'id',
			             multiSelect : true,
			             queryMode : 'remote',
			             store : roleStore,
			             labelSeparator:'：',
			             labelAlign : 'right',
			             editable : false,
//			             colspan: 2,
			             width : '85%',
			             listeners : {
			             	'expand' : function (combo, e) {	
			             		combo.store.load();
			             	}
			             }
					}, {
					      id:'id_organForAdd',
					      xtype: 'treepicker',
					      fieldLabel: "<span style='color:red'>*</span>所属机构",
					      name : 'childSys',
					      displayField: 'organName',
					      labelSeparator:'：',
					      labelAlign: 'right',
					      width : '85%',
					      valueField: 'id',
					      maxPickerHeight:200,
					      minPickerWidth: 20, //最小宽度，不设置的话有时候下拉会出问题
					      minPickerHeight: 30,
//					      autoScroll:true,
					      overflowX:'auto',
					      overflowY:'auto',
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
								  var grid = Ext.getCmp('id_userList');
								  var records = grid.getSelectionModel().getSelection();
//								  Ext.getCmp('id_organForAdd').setValue(records[0].get('organId'));
//								  Ext.getCmp('id_organForAdd').setRawValue(records[0].get('organName'));
					    	  }
					      }
					},{
						 xtype: "checkboxfield",
						 id: "id_enableForAdd",
			             fieldLabel: "启用",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             value: '1',
            			 checked: true,
			             width : '85%'
					},{
						 xtype: "textfield",
			             fieldLabel: "邮箱地址",
			             id: "id_emailForAdd",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             enforceMaxLength:true,
			             maxLength : Tool.fieldmaxlength.V100,
			             colspan: 2,
			             width : '92.5%',
			             regex: emailRgx,
			             regexText:"邮箱地址格式不正确！"
					},{
						 xtype: "textfield",
			             fieldLabel: "联系地址",
			             id: "id_addressForAdd",
			             labelAlign : 'right',
			             enforceMaxLength:true,
			             labelSeparator:'：',
			             maxLength : Tool.fieldmaxlength.V400,
			             colspan: 2,
			             width : '92.5%'
					}, {
						 xtype: "textareafield",
			             fieldLabel: "备注",
			             id: "id_remarkUserForAdd",
			             labelAlign : 'right',
			             maxLength: Tool.fieldmaxlength.V1000,
			             labelSeparator:'：',
				         allowBlank: true,
				         enforceMaxLength: true,
				         wight : 200,
				         colspan: 3,
				         width : '92.5%'
					}					
				]
				}],
				bbar: [{
			      xtype: "tbfill"
			    },
			    {
			      xtype: "button",
			      iconCls : 'fa fa-save',
			      text: '保存',
			      listeners: {
			        click: function(item, event, options) {
			        	if (Ext.isEmpty(Ext.getCmp('id_truenameForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"姓名\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_workNoForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"工号\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_usernameForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"用户名\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_passwordForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"密码\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_organForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"所属机构\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_wechatNameForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"微信名\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_roleForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"角色\"不允许为空！');
							return;
						} else if (!Ext.isEmpty(Ext.getCmp('id_mobileForAdd').getValue()) && !mobileRgx.test(Ext.getCmp('id_mobileForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '手机号码格式不正确');
							return;
						} else if (!Ext.isEmpty(Ext.getCmp('id_telephoneForAdd').getValue()) && !teleRgx.test(Ext.getCmp('id_telephoneForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '电话号码格式不正确');
							return;
						} else if (!Ext.isEmpty(Ext.getCmp('id_emailForAdd').getValue()) && !emailRgx.test(Ext.getCmp('id_emailForAdd').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '邮箱地址格式不正确');
							return;
						}
						Ext.Ajax.request( {
					    	url : 'main/system/saveUser.mvc',
					    	method : 'post',
					    	params : {
					    		truename : Ext.getCmp('id_truenameForAdd').getValue(),
					    		workNo : Ext.getCmp('id_workNoForAdd').getValue(),
					    		username : Ext.getCmp('id_usernameForAdd').getValue(),
					    		enable : Ext.getCmp('id_enableForAdd').getValue() ? '1' : '0',
					    		password : Ext.getCmp('id_passwordForAdd').getValue(),
					    		organId : Ext.getCmp('id_organForAdd').getValue(),
					    		mobile : Ext.getCmp('id_mobileForAdd').getValue(),
					    		telephone : Ext.getCmp('id_telephoneForAdd').getValue(),
					    		wechatName : Ext.getCmp('id_wechatNameForAdd').getValue(),
					    		role : Ext.getCmp('id_roleForAdd').getValue().join(","),
					    		email : Ext.getCmp('id_emailForAdd').getValue(),
					    		address : Ext.getCmp('id_addressForAdd').getValue(),
					    		remark : Ext.getCmp('id_remarkUserForAdd').getValue(),
					    		modual : '用户管理'
					    	},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		Ext.getCmp('id_insertUserWin').close();
						    		var store = Ext.getCmp('id_userList').getStore();
									store.load({params :{
										username : Ext.getCmp('username').getValue(),
										truename : Ext.getCmp('truename').getValue(),
										organId : Ext.getCmp('id_organTree').getValue(),
										workNo : Ext.getCmp('id_UserMng_workNo').getValue()
									    }
									});
									Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	} else if(o.message == 'workNoExist') {
						    		//工号重复
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"工号\"已存在！');
						    	} else if(o.message == 'usernameExist') {
						    		//用户名重复	
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"用户名\"已存在！');
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
			      	insertUserWin.close();
			      }
			    }]
			  });
		}
		this.getView().add(insertUserWin);
		insertUserWin.show();
	},
	doUpdateRecord:function(){
		var grid = Ext.getCmp('id_userList');
		var records = grid.getSelectionModel().getSelection();
		if (records.length != 1) {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgSingleSel);
			return;
		}
		var roleStore = Ext.create('Ext.data.Store', {
			pageSize: 100,
			autoLoad : false,
			listeners : {
				'beforeload' : function() {
				}
			},
			proxy : {
				type : 'ajax',
				url : 'main/system/getRoleForUserMng.mvc',
				reader : {
					type : 'json',
					rootProperty : "rows"
				}
			}, 
			model : Ext.create('Ext.data.Model', {
						fields : [ 	{name : 'id', type : 'string'}, 
									{name : 'roleName', type : 'string'}
								],
						changeName : function() {
						}
					})
		});
		roleStore.load();
		if(Ext.getCmp('id_updateUserWin') == undefined) {
			var obj = Tool.getErpRequiredField("UserMng");
			var erpFlag = obj.erpFlag;
			var array = [];
			if (erpFlag) {
				array = obj.json.split(",");
			}
			// 不存在
			var teleRgx = /^0\d{2,3}-?\d{7,8}$/;
			var mobileRgx = /^1\d{10}$/;
			var emailRgx = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
			updateUserWin = Ext.create("Ext.window.Window", {
				id : 'id_updateUserWin',
				title : "修改",
				modal : true,
				width : 700,
				height : 500,
				layout : 'fit',
				constrain : true,
				resizable : true,
				closeAction : 'hide',
				closeToolText : '关闭', 
				border : false,
				listeners : {
					close : function(win, options) {
						Ext.getCmp('id_truenameForUpdate').setValue(null);
		    			Ext.getCmp('id_workNoForUpdate').setValue(null);
		    		    Ext.getCmp('id_usernameForUpdate').setValue(null);
			    		Ext.getCmp('id_passwordForUpdate').setValue(null);
			    		Ext.getCmp('id_organForUpdate').setValue(null);
			    		Ext.getCmp('id_mobileForUpdate').setValue(null);
			    		Ext.getCmp('id_telephoneForUpdate').setValue(null);
			    		Ext.getCmp('id_roleForUpdate').setValue(null);
			    		Ext.getCmp('id_wechatNameForUpdate').setValue(null);
			    		Ext.getCmp('id_emailForUpdate').setValue(null);
			    		Ext.getCmp('id_addressForUpdate').setValue(null);
			    		Ext.getCmp('id_remarkUserForUpdate').setValue(null);
						win.hide();
					}
				},
				items : [{
					xtype: "container",
			        layout: {
			          type: 'table',
			          columns: 2,
			          tableAttrs: {
			          	padding : '5px',
			            style: "border:true"
			          }
			        },
					items:[ {
						 xtype: "textfield",
						 id: "id_truenameForUpdate",
			             fieldLabel: "<span style='color:red'>*</span>姓名",
			             labelAlign : 'right',
			             margin : '15 0 10 0',
			             enforceMaxLength:true,
			             labelSeparator:'：',
						  maxLength: Tool.fieldmaxlength.V100,
			             width : '85%',
						 readOnly : erpFlag ? Ext.Array.contains(array,'trueName') : false, 
					     fieldStyle : (erpFlag && Ext.Array.contains(array,'trueName')) ? "background-color:#F0F0F0" : "background-color:#FFFFFF"
					}, {
						 xtype: "textfield",
						 id: "id_workNoForUpdate",
			             fieldLabel: "<span style='color:red'>*</span>工号",
			             labelAlign : 'right',
			             margin : '15 0 10 0',
			             labelSeparator:'：',
			             enforceMaxLength:true,
						 maxLength: Tool.fieldmaxlength.V100,
			             width : '85%',
			             readOnly : erpFlag ? Ext.Array.contains(array,'workNo') : false, 
						 fieldStyle : (erpFlag && Ext.Array.contains(array,'workNo')) ? "background-color:#F0F0F0" : "background-color:#FFFFFF"
					}, {
						 xtype: "textfield",
						 id: "id_usernameForUpdate",
			             fieldLabel: "<span style='color:red'>*</span>用户名",
			             labelAlign : 'right',
			             enforceMaxLength:true,
			             labelSeparator:'：',
						  maxLength: Tool.fieldmaxlength.V100,
			             width : '85%',
						 readOnly : erpFlag ? Ext.Array.contains(array,'userName') : false, 
			             fieldStyle : (erpFlag && Ext.Array.contains(array,'userName')) ? "background-color:#F0F0F0" : "background-color:#FFFFFF"
					}, {
						 xtype: "textfield",
						 id: "id_passwordForUpdate",
			             fieldLabel: "<span style='color:red'>*</span>密码",
			             inputType: 'password',
			             enforceMaxLength:true,
						 maxLength : 32,
						 labelSeparator:'：',
			             labelAlign : 'right',
			             width : '85%'
					}, {
						 xtype: "textfield",
						 id: "id_mobileForUpdate",
			             fieldLabel: "手机",
			             enforceMaxLength:true,
			             labelSeparator:'：',
						  maxLength: Tool.fieldmaxlength.V100,
			             labelAlign : 'right',
			             width : '85%',
			             regex: mobileRgx,
			             regexText:"手机号码格式不正确！"
					}, {
						 xtype: "textfield",
						 id: "id_telephoneForUpdate",
			             fieldLabel: "电话",
			             enforceMaxLength:true,
						  maxLength: Tool.fieldmaxlength.V100,
						 labelSeparator:'：',
			             labelAlign : 'right',
			             width : '85%',
			             regex: teleRgx,
			             regexText:"电话号码格式不正确！"
					}, {
						 xtype: "textfield",
						 id: "id_wechatNameForUpdate",
			             fieldLabel: "<span style='color:red'>*</span>微信名",
			             enforceMaxLength:true,
						  maxLength: Tool.fieldmaxlength.V100,
						 labelSeparator:'：',
			             labelAlign : 'right',
			             width : '85%'
					}, {
						 xtype: "combobox",
						 id: "id_roleForUpdate",
			             fieldLabel: "<span style='color:red'>*</span>角色",
			             displayField : 'roleName',
			             valueField : 'id',
			             multiSelect : true,
			             queryMode : 'remote',
			             labelSeparator:'：',
			             store : roleStore,
			             labelAlign : 'right',
			             editable : false,
//			             colspan: 2, 
			             width : '85%',
			             listeners : {
			             	'expand' : function (combo, e) {	
			             		combo.store.load();
			             	}
			             }
					}, {
					      id:'id_organForUpdate',
					      xtype: 'treepicker',
					      fieldLabel: "<span style='color:red'>*</span>所属机构",
					      name : 'childSys',
					      displayField: 'organName',
					      labelSeparator:'：',
					      labelAlign: 'right',
					      valueField: 'id',
					      //minPickerHeight: 30, //最小高度，不设置的话有时候下拉会出问题
					      width : '85%',
//					      autoScroll:true,
					      overflowX:'auto',
					      overflowY:'auto',
					      maxPickerHeight:200,
					      minPickerWidth: 20, //最小宽度，不设置的话有时候下拉会出问题
					      minPickerHeight: 30,
					      editable: false, //启用编辑，主要是为了清空当前的选择项
					      readOnly : erpFlag ? Ext.Array.contains(array,'ownerRegion') : false, 
					      fieldStyle : (erpFlag && Ext.Array.contains(array,'ownerRegion')) ? "background-color:#F0F0F0" : "background-color:#FFFFFF",
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
								  var grid = Ext.getCmp('id_userList');
								  var records = grid.getSelectionModel().getSelection();
//								  Ext.getCmp('id_organForUpdate').setValue(records[0].get('organId'));
//								  Ext.getCmp('id_organForUpdate').setRawValue(records[0].get('organName'));
					    	 }
					      }
					}, {
						 xtype: "checkboxfield",
						 id: "id_enableForUpdate",
			             fieldLabel: "启用",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             value: '1',
            			 checked: true,
			             width : '85%'
					},{
						 xtype: "textfield",
			             fieldLabel: "邮箱地址",
			             id: "id_emailForUpdate",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             enforceMaxLength:true,
						 maxLength: Tool.fieldmaxlength.V100,
			             colspan: 2,
			             width : '92.5%',
			             regex: emailRgx,
			             regexText:"邮箱地址格式不正确！"
					},{
						 xtype: "textfield",
			             fieldLabel: "联系地址",
			             id: "id_addressForUpdate",
			             labelAlign : 'right',
			             enforceMaxLength:true,
						 maxLength : Tool.fieldmaxlength.V400,
						 labelSeparator:'：',
			             colspan: 2,
			             width : '92.5%'
					}, {
						 xtype: "textareafield",
			             fieldLabel: "备注",
			             id: "id_remarkUserForUpdate",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             maxLength: Tool.fieldmaxlength.V1000,
				         allowBlank: true,
				         enforceMaxLength: true,
				         wigth : 200,
				         colspan: 3,
				         width : '92.5%'
					}					
				]
				}],
				bbar: [{
			      xtype: "tbfill"
			    },
			    {
			      xtype: "button",
			      iconCls : 'fa fa-save',
			      text: '保存',
			      listeners: {
			        click: function(item, event, options) {
			        	if (Ext.isEmpty(Ext.getCmp('id_truenameForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"姓名\"不允许为允许！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_workNoForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"工号\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_usernameForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"用户名\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_passwordForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"密码\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_organForUpdate').getValue()) || Ext.getCmp('id_organForUpdate').getValue() == 'root') {
							Tool.alert(Tool.str.msgConfirmTitle, '\"所属机构\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_wechatNameForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"微信名\"不允许为空！');
							return;
						} else if (Ext.isEmpty(Ext.getCmp('id_roleForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '\"角色\"不允许为空！');
							return;
						} else if (!Ext.isEmpty(Ext.getCmp('id_mobileForUpdate').getValue()) && !mobileRgx.test(Ext.getCmp('id_mobileForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '手机号码格式不正确');
							return;
						} else if (!Ext.isEmpty(Ext.getCmp('id_telephoneForUpdate').getValue()) && !teleRgx.test(Ext.getCmp('id_telephoneForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '电话号码格式不正确');
							return;
						} else if (!Ext.isEmpty(Ext.getCmp('id_emailForUpdate').getValue()) && !emailRgx.test(Ext.getCmp('id_emailForUpdate').getValue())) {
							Tool.alert(Tool.str.msgConfirmTitle, '邮箱地址格式不正确');
							return;
						}
						Ext.Ajax.request( {
					    	url : 'main/system/updateUser.mvc',
					    	method : 'post',
					    	params : {
					    		userId : Ext.getCmp('id_userList').getSelectionModel().getSelection()[0].get('id'),
					    		truename : Ext.getCmp('id_truenameForUpdate').getValue(),
					    		workNo : Ext.getCmp('id_workNoForUpdate').getValue(),
					    		username : Ext.getCmp('id_usernameForUpdate').getValue(),
					    		enable : Ext.getCmp('id_enableForUpdate').getValue() ? '1' : '0',
					    		password : Ext.getCmp('id_passwordForUpdate').getValue(),
					    		organId : Ext.getCmp('id_organForUpdate').getValue(),
					    		mobile : Ext.getCmp('id_mobileForUpdate').getValue(),
					    		telephone : Ext.getCmp('id_telephoneForUpdate').getValue(),
					    		wechatName : Ext.getCmp('id_wechatNameForUpdate').getValue(),
					    		role : Ext.getCmp('id_roleForUpdate').getValue().join(","),
					    		email : Ext.getCmp('id_emailForUpdate').getValue(),
					    		address : Ext.getCmp('id_addressForUpdate').getValue(),
					    		remark : Ext.getCmp('id_remarkUserForUpdate').getValue(),
					    		modual : '用户管理'
					    	},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		Ext.getCmp('id_updateUserWin').close();
						    		var store = Ext.getCmp('id_userList').getStore();
									store.load({params :{
										username : Ext.getCmp('username').getValue(),
										truename : Ext.getCmp('truename').getValue(),
										organId : Ext.getCmp('id_organTree').getValue(),
										workNo : Ext.getCmp('id_UserMng_workNo').getValue()
									    }
									});
									Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	} else if(o.message == 'workNoExist') {
						    		//工号重复
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"工号\"已存在！');
						    	} else if(o.message == 'usernameExist') {
						    		//用户名重复	
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"用户名\"已存在！');
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
			      	updateUserWin.close();
			      }
			    }]
			  });
		}
		
		//将窗口内控件赋上值
		var userMng_enable = records[0].get('enabled');
		if (userMng_enable == '0') {
			//不启用 用户
			Ext.getCmp('id_enableForUpdate').setValue(null);
		}
		if (userMng_enable == '1') {
			//不启用 用户
			Ext.getCmp('id_enableForUpdate').setValue(true);
		}
		var userMng_roleName = [];
		var userMng_roleId = [];
		var userMng_roleArray = [];
		if (records[0].get('roleNameStr') != null) {
			userMng_roleName = records[0].get('roleNameStr').split(", ");
		} 
		Ext.getCmp('id_roleForUpdate').getStore().load();
		var userView = this.getView(); 
		//获取角色
		Ext.Ajax.request( {
	    	url : 'main/system/getRoleForUserMng.mvc',
	    	method : 'post',
	    	async : false,
	    	reader : {
				type : 'json',
				rootProperty : "rows"
			},
	    	params : {
	    	},
	    	success : function(response, options) {
		    	var o = Ext.util.JSON.decode(response.responseText);
		    	userMng_roleArray = o.rows;
		    	for(var j = 0; j < userMng_roleName.length; j++) {
					var roleName = userMng_roleName[j];
					for(var i = 0; i < userMng_roleArray.length; i++) {
						if(userMng_roleArray[i].roleName == roleName) {
							userMng_roleId.push(userMng_roleArray[i].id);		
						}
					}
				}
				setTimeout(function () {
					Ext.getCmp('id_truenameForUpdate').setValue(records[0].get('truename'));
					Ext.getCmp('id_workNoForUpdate').setValue(records[0].get('workNo'));
				    Ext.getCmp('id_usernameForUpdate').setValue(records[0].get('username'));
					Ext.getCmp('id_passwordForUpdate').setValue(records[0].get('password'));
					Ext.getCmp('id_organForUpdate').setValue(records[0].get('organId'));
					Ext.getCmp('id_organForUpdate').setRawValue(records[0].get('organName'));
					Ext.getCmp('id_mobileForUpdate').setValue(records[0].get('mobile'));
					Ext.getCmp('id_telephoneForUpdate').setValue(records[0].get('telephone'));
					Ext.getCmp('id_wechatNameForUpdate').setValue(records[0].get('wechatName'));
					Ext.getCmp('id_roleForUpdate').setValue(userMng_roleId);
					Ext.getCmp('id_emailForUpdate').setValue(records[0].get('email'));
					Ext.getCmp('id_addressForUpdate').setValue(records[0].get('address'));
					Ext.getCmp('id_remarkUserForUpdate').setValue(records[0].get('remark'));
					userView.add(updateUserWin);
					updateUserWin.show();
				},30);
	    	},
	    	failure : function() {
	    	}
	    });
	},
	doDelete:function(){
		var grid = Ext.getCmp('id_userList');
		var records = grid.getSelectionModel().getSelection();
		if (records.length > 0) {
			var messageBox = Ext.create('Ext.window.MessageBox',{
				closeToolText : "关闭"
			});
			messageBox.confirm(Tool.str.msgConfirmTitle, '</br>' + Tool.str.msgConfirmDel,function(btn) {
				if (btn == 'yes') {
					var ids = [];
					var usernames = [];
					var deleteNum=0;
					for ( var i = 0; i < records.length; i++) {
						var record = records[i];
						ids.push(record.get('id'));
						usernames.push(record.get('username'));
						deleteNum++;
					}
					Ext.Ajax.request({
						url : 'main/system/deleteUser.mvc',
						method : 'post',
						params : {
							userIds : ids.join(","),
							usernames : usernames.join(","),
							modual : '用户管理'
						},
						success : function(response,options) {
							var o = Ext.util.JSON.decode(response.responseText);
							if (o.message == 'success') {
								// 操作成功
								var store = Ext.getCmp('id_userList').getStore();
								if(store.getCount()-store.getNewRecords().length==deleteNum && store.currentPage>1){
									store.previousPage();//加载上一页 'page'
								} else {
									store.load({params :{
										username : Ext.getCmp('username').getValue(),
										truename : Ext.getCmp('truename').getValue(),
										organId : Ext.getCmp('id_organTree').getValue(),
										workNo : Ext.getCmp('id_UserMng_workNo').getValue()
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
		} else {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgOptSelected);
			return;
		}
	},
	doDisable:function(){
		var userList = Ext.getCmp('id_userList');
		var row = userList.getSelectionModel().getSelection();
		if(row.length===0) {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgOptSelected);
			return;
		}  else {
			for(i=0;i<row.length;i++){
				if(row[i].get('enabled')==0){
					Tool.alert(Tool.str.msgConfirmTitle,'所选信息中存在已禁用的记录',{width:260});
					return;
				}
			}
			var messageBox = Ext.create('Ext.window.MessageBox',{
				closeToolText : "关闭"
			});
			messageBox.show( {
				title : Tool.str.msgConfirmTitle,
				msg : '</br>确定要禁用所选用户？',
				buttons : Ext.MessageBox.OKCANCEL,
				icon : Ext.MessageBox.QUESTION,
				fn : function(b) {
                  	if (b == 'ok'){
                  		var ids = [];
                  		var usernames = [];
    					for ( var i = 0; i < row.length; i++) {
    						var record = row[i];
    						if(currentLoginUser === record.get('username')){
    							Tool.alert(Tool.str.msgConfirmTitle, '无法禁用当前用户！');
    							return false;
    						}
    						ids.push(record.get('id'));
    						usernames.push(record.get('username'));
    					}
						Ext.Ajax.request( {
					    	url : 'main/system/disableUser.mvc',
					    	method : 'post',
					    	params : {
								userIds : ids.join(","),
								usernames : usernames.join(","),
								modual : '用户管理'
							},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		//操作成功
						    		var store = Ext.getCmp('id_userList').getStore();
									store.load({params :{
									username : Ext.getCmp('username').getValue(),
									truename : Ext.getCmp('truename').getValue(),
									organId : Ext.getCmp('id_organTree').getValue(),
									workNo : Ext.getCmp('id_UserMng_workNo').getValue()
									}
									});
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
	doEnable:function(){
		var userList = Ext.getCmp('id_userList');
		var row = userList.getSelectionModel().getSelection();
		if(row.length===0) {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgOptSelected);
			return;
		} else {
			for(i=0;i<row.length;i++){
				if(row[i].get('enabled')==1){
					Tool.alert(Tool.str.msgConfirmTitle,'所选信息中存在已启用的记录',{width:260});
					return;
				}
			}
			var messageBox = Ext.create('Ext.window.MessageBox',{
				closeToolText : "关闭"
			});
			messageBox.show( {
				title : Tool.str.msgConfirmTitle,
				msg : '</br>确定要启用所选用户？',
				buttons : Ext.MessageBox.OKCANCEL,
				closeToolText : "关闭",
				icon : Ext.MessageBox.QUESTION,
				fn : function(b) {
                  	if (b == 'ok'){
                  		var ids = [];
                  		var usernames = [];
    					for ( var i = 0; i < row.length; i++) {
    						var record = row[i];
    						ids.push(record.get('id'));
    						usernames.push(record.get('username'));
    					}
						Ext.Ajax.request( {
					    	url : 'main/system/enableUser.mvc',
					    	method : 'post',
					    	params : {
								userIds : ids.join(","),
								usernames : usernames.join(","),
								modual : '用户管理'
							},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		//操作成功
						    		var store = Ext.getCmp('id_userList').getStore();
									store.load({params :{
									username : Ext.getCmp('username').getValue(),
									truename : Ext.getCmp('truename').getValue(),
									organId : Ext.getCmp('id_organTree').getValue(),
									workNo : Ext.getCmp('id_UserMng_workNo').getValue()
									    }
									});
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
		var userList = this.lookupReference('userList');
		userList.getStore().load({  params: { 
				username : Ext.getCmp('username').getValue(),
				truename : Ext.getCmp('truename').getValue(),
				organId : Ext.getCmp('id_organTree').getValue(),
				workNo : Ext.getCmp('id_UserMng_workNo').getValue()}});
	},
	doRender:function(){
//		var userList = this.lookupReference('userList');
//		userList.getStore().load({  params: { start: Tool.page.start, limit: Tool.page.limit}});
	},
	doExport : function(){
		var me =this;
		this.lookupReference('userList').getStore().load({
		    scope: this,
		    callback: function(records, operation, success) {
		        if (records.length == 0) {
					Tool.alert(Tool.str.msgConfirmTitle, "所导出数据为空！");
					return;
				}
//				var messageBox = Ext.create('Ext.window.MessageBox',{
//					closeToolText : "关闭"
//				});
				Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>' + "确认导出？",function(btn) {
					if (btn == 'yes') {
						window.location.href=encodeURI(encodeURI("main/system/exportData.mvc?username=" + Ext.getCmp('username').getValue()
						+ "&&truename=" + Ext.getCmp('truename').getValue()
						+ "&&organName=" + Ext.getCmp('id_organTree').getValue()
						+ "&&workNo=" + Ext.getCmp('id_UserMng_workNo').getValue()));
					}
				});
		    }
		});
	}
});
