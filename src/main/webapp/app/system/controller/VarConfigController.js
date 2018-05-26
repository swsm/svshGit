Ext.define('app.system.controller.VarConfigController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.varConfigController',
	requires:['Ext.ux.TreePicker'],
	
	doClear:function() {
		var me = this.lookupReference('clearComboBox');
        me.setValue('');
   },
	
	doInsertRecord:function(){
		if(!Ext.isEmpty(Ext.getCmp("id_varConfigPanel").getSelectionModel().getSelection())){
		var selectVarType = Ext.getCmp("id_varConfigPanel").getSelectionModel().getSelection()[0];
			if(selectVarType.get('varType') == '2'){
				Tool.alert(Tool.str.msgConfirmTitle, '配置项不允许新增子项');
				return ;
			}
		}
		var varConfigPanel = Ext.getCmp('id_varConfigPanel');
		var row = varConfigPanel.getSelectionModel().getSelection();
		var store = Tool.createDictStore({parentKey:'VAR_TYPE'});
		store.load();
		//上级资源名称
		var parentVarConfigName = '';
		var parentVarConfigId = '';
		if(row.length!==0) {
			parentVarConfigId = row[0].get('id');
			parentVarConfigName = row[0].get('varName');
		}
		if(Ext.getCmp('id_insertVarConfigWin') == undefined) {
			// 不存在
			insertVarConfigWin = Ext.create("Ext.window.Window", {
				id : 'id_insertVarConfigWin',
				title : "新增",
				modal : true,
				width : 400,
				height : 460,
				layout : 'fit',
				constrain : true,
				resizable : true,
				closeAction : 'hide',
				closeToolText : '关闭', 
				border : false,
				listeners : {
					close : function(win, options) {
						//上级资源下拉列表 置 为 null
						Ext.getCmp('id_parentVarConfigForAdd').setValue(null);
			    		Ext.getCmp('id_varConfigNameForAdd').setValue(null),
		    		    Ext.getCmp('id_varConfigDisplayForAdd').setValue(null),
		    		    Ext.getCmp('id_varConfigValueForAdd').setValue(null),
		    		    Ext.getCmp('id_varConfigTypeForAdd').setValue(null),
		    		    Ext.getCmp('id_varConfigOrderForAdd').setValue(null),
		    		    Ext.getCmp('id_varConfigRemarkForAdd').setValue(null),
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
					      id:'id_parentVarConfigForAdd',
					      xtype: 'treepicker',
					      fieldLabel: '所属层级',
					      name : 'childSys',
					      labelSeparator:'：',
					      displayField: 'varName',
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
					      reference : 'clearComboBox',
						  triggers : {
							foo : {
								cls : Ext.baseCSSPrefix + 'form-clear-trigger',
								handler : 'doClear'
							}
						},
					      store: Ext.create('Ext.data.TreeStore',{
					      	  filterOnLoad:true,
						      proxy: {
						          type : 'ajax',
									url : 'main/system/getVarConfigTree.mvc',
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
					      filters:[{property:'varType',value:/^[1]$/}],
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
						xtype : "combobox",
						fieldLabel: "<span style=\'color:red\'>*</span>新增项类型",
						id: "id_varConfigTypeForAdd",
						labelSeparator:'：',
			            labelAlign : 'right',
			            enforceMaxLength:true,
						maxLength : Tool.fieldmaxlength.V100,
			            width : '75%',
						emptyText : "请选择",
						editable : false,
						value:'1',
						store : store,
						displayField : 'dictValue',
						valueField : 'dictKey',
						
						listeners:{
							'select':function(combo,records, obj){
							var varType=Ext.getCmp('id_varConfigTypeForAdd').getValue();
							if(varType=="1"){
		      	   	 		Ext.getCmp("id_varConfigDisplayForAdd").setDisabled(true);
		      	   		    Ext.getCmp("id_varConfigValueForAdd").setDisabled(true);
		      	   		    Ext.getCmp("id_varConfigDisplayForAdd").setFieldLabel("新增项编码");
		      	   		    Ext.getCmp("id_varConfigValueForAdd").setFieldLabel("初始值");
					   		}
					   		else{
					   			Ext.getCmp("id_varConfigDisplayForAdd").setDisabled(false);
		      	   		   	 	Ext.getCmp("id_varConfigValueForAdd").setDisabled(false);
		      	   		   	 	Ext.getCmp("id_varConfigDisplayForAdd").setFieldLabel("<span style=\'color:blue\'>*</span>新增项编码");
		      	   		    	Ext.getCmp("id_varConfigValueForAdd").setFieldLabel("<span style=\'color:blue\'>*</span>初始值");
		      	   		    	Ext.getCmp("id_parentVarConfigForAdd").setFieldLabel("<span style=\'color:blue\'>*</span>所属层级");
					   		}
							}
						}
					},{
						 xtype: "textfield",
						 id: "id_varConfigNameForAdd",
						 labelSeparator:'：',
			             fieldLabel: "<span style=\'color:red\'>*</span>新增项名称",
			             labelAlign : 'right',
			             enforceMaxLength:true,
						 maxLength : Tool.fieldmaxlength.V100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "新增项编码",
			             reference:'varConfigDisplayForAdd',
			             id: "id_varConfigDisplayForAdd",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             enforceMaxLength:true,
			             disabled:true,
						 maxLength : Tool.fieldmaxlength.V100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "初始值",
			             id: "id_varConfigValueForAdd",
			             labelSeparator:'：',
			             labelAlign : 'right',
			             enforceMaxLength:true,
			             disabled:true,
						 maxLength : Tool.fieldmaxlength.V100,
			             width : '75%'
					}, {
						 xtype: "numberfield",
			             fieldLabel: "<span style=\'color:red\'>*</span>排序",
			             id: "id_varConfigOrderForAdd",
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
			             id: "id_varConfigRemarkForAdd",
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
			        	var myMask = new Ext.LoadMask(insertVarConfigWin,{msg:Tool.str.msgLoadMarsk});
			        	if(Ext.isEmpty(Ext.getCmp('id_varConfigTypeForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"新增项类型\"不允许为空！');
			        		return;
			        	}
			        	if(Ext.isEmpty(Ext.getCmp('id_varConfigNameForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"新增项名称\"不允许为空！');
			        		return;
			        	} 
			        	
			        	if(Ext.getCmp("id_varConfigTypeForAdd").getValue() =='2'){
			        		if(Ext.isEmpty(Ext.getCmp('id_parentVarConfigForAdd').getValue())){
			        			Tool.alert(Tool.str.msgConfirmTitle, '\"所属层级\"不允许为空！');
				        		return;
			        		}
			        		if(Ext.isEmpty(Ext.getCmp('id_varConfigDisplayForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"新增项编码\"不允许为空！');
			        		return;
			        		}
			        		if(Ext.isEmpty(Ext.getCmp('id_varConfigValueForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"初始值\"不允许为空！');
			        		return;
			        		}
			        	}
			        	
			        	if(Ext.getCmp('id_varConfigTypeForAdd').getValue()=='1'){
			        		if(!Ext.isEmpty(Ext.getCmp('id_varConfigDisplayForAdd').getValue())){
			        			Tool.alert(Tool.str.msgConfirmTitle, '新增项为文件夹时，没有编码');
			        			return;
			        		}
			        		if(!Ext.isEmpty(Ext.getCmp('id_varConfigValueForAdd').getValue())){
			        			Tool.alert(Tool.str.msgConfirmTitle, '新增项为文件夹时，初始值为空');
			        			return;
			        		}
			        	}
			        	if(Ext.isEmpty(Ext.getCmp('id_varConfigOrderForAdd').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为空！');
			        		return;
			        	}
			        	if (Ext.getCmp('id_varConfigOrderForAdd').getValue() < 0) {
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为负数！');
			        		return;
			        	}
			        	myMask.show();
						Ext.Ajax.request( {
					    	url : 'main/system/saveVarConfig.mvc',
					    	method : 'post',
					    	params : {
					    		parentVarConfigId : Ext.getCmp('id_parentVarConfigForAdd').getValue(),
					    		varName : Ext.getCmp('id_varConfigNameForAdd').getValue(),
					    		varDisplay : Ext.getCmp('id_varConfigDisplayForAdd').getValue(),
					    		varValue : Ext.getCmp('id_varConfigValueForAdd').getValue(),
					    		varType : Ext.getCmp('id_varConfigTypeForAdd').getValue(),
					    		varOrder : Ext.getCmp('id_varConfigOrderForAdd').getValue(),
					    		remark : Ext.getCmp('id_varConfigRemarkForAdd').getValue(),
					    		modual : '配置项管理'
					    	},
					    	success : function(response, options) {
					    		myMask.hide();
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		//操作成功
									var parentNode = Ext.getCmp('id_varConfigPanel').getStore().getNodeById(
										Ext.getCmp('id_parentVarConfigForAdd').getValue());
									
						    		var opions = {node:parentNode};//进行封装                                
									var store = Ext.getCmp('id_varConfigPanel').getStore();
//						    		if(parentNode != null && !parentNode.isLeaf()) {
//						    			store.load(opions);//局部加载
//						    		} else {
										store.load({scope: this,
											    callback: function(records, operation, success) {
											        Ext.getCmp('id_varConfigPanel').expandAll();
											    }
										});
										
									function t() {
										store.each(
											function(recorde) {
												if (recorde.get("varName") == Ext.getCmp('id_varConfigNameForAdd').getValue()) {
													Ext.getCmp('id_varConfigPanel').getSelectionModel().select(recorde);
													return;
												}
											});
									Ext.getCmp('id_parentVarConfigForAdd').setValue(null);
						    		Ext.getCmp('id_varConfigNameForAdd').setValue(null);
					    		    Ext.getCmp('id_varConfigDisplayForAdd').setValue(null);
					    		    Ext.getCmp('id_varConfigValueForAdd').setValue(null);
					    		    Ext.getCmp('id_varConfigTypeForAdd').setValue(null);
					    		    Ext.getCmp('id_varConfigOrderForAdd').setValue(null);
					    		    Ext.getCmp('id_varConfigRemarkForAdd').setValue(null);
										}
									setTimeout(t,1000);
									insertVarConfigWin.hide();
									Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	} else if(o.message == "varNameExist"){
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"新增项名称\"已存在！');
						    	} else if (o.message == "varDisplayExist") {
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"新增项编码\"已存在！');
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
			      	insertVarConfigWin.close();
			      }
			    }]
			  });
		}
		var varConfigView = this.getView(); 
		//延时20ms执行下面的程序 因store ajax同步获取数据问题
		setTimeout(function(){
			if(Ext.isEmpty(Ext.getCmp('id_parentVarConfigForAdd').getValue())) {
				//设置上级资源 combobox的 值
				Ext.getCmp("id_varConfigDisplayForAdd").setDisabled(true);
  	   		    Ext.getCmp("id_varConfigValueForAdd").setDisabled(true);
  	   		    Ext.getCmp("id_varConfigDisplayForAdd").setFieldLabel("新增项编码");
  	   		    Ext.getCmp("id_varConfigValueForAdd").setFieldLabel("初始值");
  	   		    Ext.getCmp("id_parentVarConfigForAdd").setFieldLabel("所属层级");
  	   		    
				Ext.getCmp('id_parentVarConfigForAdd').setValue(parentVarConfigId);
				Ext.getCmp('id_parentVarConfigForAdd').setRawValue(parentVarConfigName);
				Ext.getCmp("id_varConfigTypeForAdd").setValue('1');
			}
			varConfigView.add(insertVarConfigWin);
			insertVarConfigWin.show();
		}, 20);
		
	},
	doUpdateRecord:function(){
		//根据varConfig_edit判断哪些字段可修改
		var readOnly = false;
		if(varConfig_edit === '1'){
			readOnly = true;
		}
		Ext.require(['Ext.ux.TreePicker']);
		
		var varConfigPanel = Ext.getCmp('id_varConfigPanel');
		var row = varConfigPanel.getSelectionModel().getSelection();
		var store = Tool.createDictStore({parentKey:'VAR_TYPE'});
		store.load();
		//上级资源名称
		var parentVarConfigName = '';
		var parentVarConfigId = '';
		var varId = '';
		var varName = '';
		var varDisplay = '';
		var varValue = '';
		var varType='';
		var varOrder = '';
		var remark = '';
		if(row.length!==0) {
			var parentVarConfig = row[0].get('parentVarConfig');
			if (!Ext.isEmpty(parentVarConfig)) {
				parentVarConfigId = parentVarConfig.id;
				parentVarConfigName = parentVarConfig.varName;
			}
			varId = row[0].get('id');
			varName = row[0].get('varName');
			varDisplay = row[0].get('varDisplay');
			varValue = row[0].get('varValue');
			varType =  row[0].get('varType');
			varOrder = row[0].get('varOrder');
			remark = row[0].get('remark');
		} else {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要更新的配置项！');
			return;
		}
		
		if(Ext.getCmp('id_updateVarConfigWin') == undefined) {
			// 不存在
			updateVarConfigWin = Ext.create("Ext.window.Window", {
				id : 'id_updateVarConfigWin',
				title : "修改",
				modal : true,
				width : 400,
				height : 460,
				layout : 'fit',
				constrain : true,
				resizable : true,
				closeToolText : '关闭', 
				closeAction : 'hide',
				border : false,
				listeners : {
					close : function(win, options) {
						//上级资源下拉列表 置 为 null
						Ext.getCmp('id_parentVarConfigForUpdate').setValue(null);
			    		Ext.getCmp('id_varConfigNameForUpdate').setValue(null),
		    		    Ext.getCmp('id_varConfigDisplayForUpdate').setValue(null),
		    		    Ext.getCmp('id_varConfigValueForUpdate').setValue(null),
		    		    Ext.getCmp('id_varConfigTypeForUpdate').setValue(null),
		    		    Ext.getCmp('id_varConfigOrderForUpdate').setValue(null),
		    		    Ext.getCmp('id_varConfigRemarkForUpdate').setValue(null),
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
					      id:'id_parentVarConfigForUpdate',
					      xtype: 'treepicker',
					      fieldLabel: '所属层级',
					      name : 'childSys',
					      displayField: 'varName',
					      labelSeparator:'：',
					      labelAlign: 'right',
					      margin : '15 0 15 0',
					      valueField: 'id',
					      width : '75%',
					      readOnly: readOnly,
					      minPickerHeight: 30, //最小高度，不设置的话有时候下拉会出问题
					      minPickerWidth: 300, //最小宽度，不设置的话有时候下拉会出问题
					      autoScroll:true,
					      bodyStyle : 'overflow-x:hidden; overflow-y:scroll',
					      editable: false, //启用编辑，主要是为了清空当前的选择项
					      enableKeyEvents: true, //激活键盘事件
					      reference : 'clearComboBox',
						  triggers : {
							foo : {
								cls : Ext.baseCSSPrefix + 'form-clear-trigger',
								handler : 'doClear'
							}
						},
					      store: Ext.create('Ext.data.TreeStore',{
					      	  filterOnLoad:true,
					      	  autoLoad : true,
						      proxy: {
						          type : 'ajax',
									url : 'main/system/getVarConfigTree.mvc',
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
						      filters:[{property:'varType',value:/^[1]$/}],
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
						xtype : "combobox",
						fieldLabel : "<span style=\'color:red\'>*</span>配置项类型",
						id: "id_varConfigTypeForUpdate",
						labelSeparator:'：',
			            labelAlign : 'right',
			            enforceMaxLength:true,
						maxLength : 100,
			            width : '75%',
						emptyText : "请选择",
						editable : false,
						disabled:true,
						store : store,
						displayField : 'dictValue',
						valueField : 'dictKey'
					},{
						 xtype: "textfield",
						 id: "id_varConfigNameForUpdate",
			             fieldLabel: "<span style=\'color:red\'>*</span>配置项名称",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             readOnly: readOnly,
			             enforceMaxLength:true,
						 maxLength : 100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "配置项编码",
			             id: "id_varConfigDisplayForUpdate",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             readOnly: readOnly,
			             enforceMaxLength:true,
						 maxLength : 100,
			             width : '75%'
					},{
						 xtype: "textfield",
			             fieldLabel: "初始值",
			             id: "id_varConfigValueForUpdate",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             enforceMaxLength:true,
					     maxLength : 100,
			             width : '75%'
					},{
						 xtype: "numberfield",
			             fieldLabel: "<span style=\'color:red\'>*</span>排序",
			             id: "id_varConfigOrderForUpdate",
			             labelAlign : 'right',
			             labelSeparator:'：',
			             readOnly: readOnly,
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
			             id: "id_varConfigRemarkForUpdate",
			             labelSeparator:'：',
			             readOnly: readOnly,
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
			        	var myMask = new Ext.LoadMask(updateVarConfigWin,{msg:Tool.str.msgLoadMarsk});
			        	if(Ext.getCmp('id_varConfigPanel').getSelectionModel().getSelection()[0].get('id')
				        		== Ext.getCmp('id_parentVarConfigForUpdate').getValue()) {
								//所属层级不能为当前配置项所在的层级
								Tool.alert(Tool.str.msgConfirmTitle, '\"所属层级\"不能为当前配置项所在的层级！', {width:350});
								return;
							}
			        	if(Ext.isEmpty(Ext.getCmp('id_varConfigTypeForUpdate').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"配置项类型\"不允许为空！');
			        		return;
			        	} 
						if(Ext.isEmpty(Ext.getCmp('id_varConfigNameForUpdate').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"配置项名称\"不允许为空！');
			        		return;
			        	} 
			        	
						if(Ext.getCmp("id_varConfigTypeForUpdate").getValue() =='2'){
							if(Ext.isEmpty(Ext.getCmp("id_parentVarConfigForUpdate").getValue())){
								Tool.alert(Tool.str.msgConfirmTitle, '\"所属层级\"不允许为空！');
				        		return;
							}
			        		if(Ext.isEmpty(Ext.getCmp('id_varConfigDisplayForUpdate').getValue())){
				        		Tool.alert(Tool.str.msgConfirmTitle, '\"新增项编码\"不允许为空！');
				        		return;
			        		}
			        		if(Ext.isEmpty(Ext.getCmp('id_varConfigValueForUpdate').getValue())){
				        		Tool.alert(Tool.str.msgConfirmTitle, '\"初始值\"不允许为空！');
				        		return;
			        		}
			        	}
			        	
			        	if(Ext.getCmp('id_varConfigTypeForUpdate').getValue()=='1'){
			        		if(!Ext.isEmpty(Ext.getCmp('id_varConfigDisplayForUpdate').getValue())){
			        			Tool.alert(Tool.str.msgConfirmTitle, '新增项为文件夹时，没有编码');
			        			return;
			        		}
			        		if(!Ext.isEmpty(Ext.getCmp('id_varConfigValueForUpdate').getValue())){
			        			Tool.alert(Tool.str.msgConfirmTitle, '新增项为文件夹时，初始值为空');
			        			return;
			        		}
			        	}
			        	if(Ext.isEmpty(Ext.getCmp('id_varConfigOrderForUpdate').getValue())){
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为空！');
			        		return;
			        	}
			        	if (Ext.getCmp('id_varConfigOrderForUpdate').getValue() < 0) {
			        		Tool.alert(Tool.str.msgConfirmTitle, '\"排序\"不允许为负数！');
			        		return;
			        	}
			        	myMask.show();
						Ext.Ajax.request( {
					    	url : 'main/system/updateVarConfig.mvc',
					    	method : 'post',
					    	params : {
					    		parentVarConfigId : Ext.getCmp('id_parentVarConfigForUpdate').getValue(),
					    		varId : Ext.getCmp('id_varConfigPanel').getSelectionModel().getSelection()[0].get('id'),
					    		varName : Ext.getCmp('id_varConfigNameForUpdate').getValue(),
					    		varDisplay : Ext.getCmp('id_varConfigDisplayForUpdate').getValue(),
					    		varValue : Ext.getCmp('id_varConfigValueForUpdate').getValue(),
					    		varType: Ext.getCmp('id_varConfigTypeForUpdate').getValue(),
					    		varOrder : Ext.getCmp('id_varConfigOrderForUpdate').getValue(),
					    		remark : Ext.getCmp('id_varConfigRemarkForUpdate').getValue(),
					    		modual : '配置项管理'
					    	},
					    	success : function(response, options) {
					    		myMask.hide();
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		Ext.getCmp('id_parentVarConfigForUpdate').setValue(null);
						    		Ext.getCmp('id_varConfigNameForUpdate').setValue(null),
					    		    Ext.getCmp('id_varConfigDisplayForUpdate').setValue(null),
					    		    Ext.getCmp('id_varConfigValueForUpdate').setValue(null),
					    		    Ext.getCmp('id_varConfigTypeForUpdate').setValue(null),
					    		    Ext.getCmp('id_varConfigOrderForUpdate').setValue(null),
					    		    Ext.getCmp('id_varConfigRemarkForUpdate').setValue(null),
									updateVarConfigWin.hide();
						    		//操作成功
//									var curNode = Ext.getCmp('id_varConfigPanel').getStore().getNodeById(
//										Ext.getCmp('id_varConfigPanel').getSelectionModel().getSelection()[0].get('id'));
//									var parentNode = curNode.parentNode;
//						    		var opions = {node:parentNode};//进行封装                                
//									var store = Ext.getCmp('id_varConfigPanel').getStore();
//						    		if(parentNode != null && parentNode.id != 'root') {
//						    			store.load(opions);//局部加载
//						    		} else {
										Ext.getCmp('id_varConfigPanel').getStore().load({scope: this,
											    callback: function(records, operation, success) {
											        Ext.getCmp('id_varConfigPanel').expandAll();
											    }
										});
						    		//}
						    		Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	} else if(o.message == "varNameExist"){
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"配置项名称\"已存在！');
						    	} else if (o.message == "varDisplayExist") {
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"配置项编码\"已存在！');
						    	} else if (o.message == "parentVarConfigNotChildVarConfig") {
						    		Tool.alert(Tool.str.msgConfirmTitle, '\"所属层级\"不能为该层级的子项！', {width:330});
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
			      	updateVarConfigWin.close();
			      }
			    }]
			  });
		}
		var varConfigView = this.getView();
		//延时20ms执行下面的程序 因store ajax同步获取数据问题
		setTimeout(function () {
			if(Ext.isEmpty(Ext.getCmp('id_parentVarConfigForUpdate').getValue())) {
				var selectVarType = Ext.getCmp("id_varConfigPanel").getSelectionModel().getSelection()[0];
				if(selectVarType.get('varType')=='2'){
					Ext.getCmp("id_varConfigDisplayForUpdate").setFieldLabel("<span style=\'color:blue\'>*</span>新增项编码");
		      	   	Ext.getCmp("id_varConfigValueForUpdate").setFieldLabel("<span style=\'color:blue\'>*</span>初始值");
		      	   	Ext.getCmp("id_parentVarConfigForUpdate").setFieldLabel("<span style=\'color:blue\'>*</span>所属层级");
		      	   	Ext.getCmp("id_varConfigDisplayForUpdate").setDisabled(false);
					Ext.getCmp("id_varConfigValueForUpdate").setDisabled(false);
				} else{
					Ext.getCmp("id_varConfigDisplayForUpdate").setFieldLabel("新增项编码");
		      	   	Ext.getCmp("id_varConfigValueForUpdate").setFieldLabel("初始值");
		      	   	Ext.getCmp("id_parentVarConfigForUpdate").setFieldLabel("所属层级");
					Ext.getCmp("id_varConfigDisplayForUpdate").setDisabled(true);
					Ext.getCmp("id_varConfigValueForUpdate").setDisabled(true);
				}
				//设置上级资源 combobox的 值
				Ext.getCmp('id_parentVarConfigForUpdate').setValue(parentVarConfigId);
				Ext.getCmp('id_parentVarConfigForUpdate').setRawValue(parentVarConfigName);
				Ext.getCmp('id_varConfigNameForUpdate').setValue(varName);
				Ext.getCmp('id_varConfigDisplayForUpdate').setValue(varDisplay);
				Ext.getCmp('id_varConfigValueForUpdate').setValue(varValue);
				Ext.getCmp('id_varConfigTypeForUpdate').setValue(varType);
				Ext.getCmp('id_varConfigOrderForUpdate').setValue(varOrder);
				Ext.getCmp('id_varConfigRemarkForUpdate').setValue(remark);
			}
			varConfigView.add(updateVarConfigWin);
			updateVarConfigWin.show();
		},20);
	},
	doDelete:function(){
		var varConfigPanel = Ext.getCmp('id_varConfigPanel');
		var row = varConfigPanel.getSelectionModel().getSelection();
		if(row.length===0) {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgOptSelected);
			return;
		} else {
			var curNode = Ext.getCmp('id_varConfigPanel').getStore().getNodeById(row[0].get('id'));
			if(!curNode.isLeaf()){
				var s = "</br>当前文件夹含有子项，删除将会连同子项一并删除，确定要删除吗？";
			} else {
				var s = "</br>确认删除此配置项？";	
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
					    	url : 'main/system/deleteVarConfig.mvc',
					    	method : 'post',
					    	params : {
					    		varId : row[0].get('id'),
					    		varName : row[0].get("varConfigName"),
					    		modual : '配置项管理'
					    	},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		//操作成功
						    		var parentNode = curNode.parentNode.parentNode;
						    		var opions = {node:parentNode};//进行封装                                
									var store = Ext.getCmp('id_varConfigPanel').getStore();
//						    		if(parentNode != null && parentNode.id != 'root') {
//						    			store.load(opions);//局部加载
//						    		} else {
										store.load({scope: this,
											    callback: function(records, operation, success) {
											        Ext.getCmp('id_varConfigPanel').expandAll();
											    }
										});
						    		//}
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
		var store = Ext.getCmp('id_varConfigPanel').getStore();
		store.load({scope: this,
			    callback: function(records, operation, success) {
			    	Ext.getCmp('id_varConfigPanel').collapseAll();
			    }
		});
	},
	doRender:function(){
	},
	doTreeDropMove : function(sourceNode, targetNode, dropPosition) {
		Ext.Ajax.request( {
	    	url : 'main/system/treeVarDropMove.mvc',
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
		    		var node = Ext.getCmp('id_varConfigPanel').getStore().getNodeById(targetNode);
		    		var parentNode = node.parentNode;
		    		var opions = {node:parentNode};//进行封装                                
					var store = Ext.getCmp('id_varConfigPanel').getStore();
		    		if(parentNode != null && parentNode.id != 'root') {
		    			store.load(opions);//局部加载
		    			parentNode.expand(true);
		    		} else {
						store.load({scope: this,
							    callback: function(records, operation, success) {
							        Ext.getCmp('id_varConfigPanel').expandAll();
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
	    	url : 'main/system/treeVarDropCopy.mvc',
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
		    		var node = Ext.getCmp('id_varConfigPanel').getStore().getNodeById(targetNode);
		    		var parentNode = node.parentNode;
		    		var opions = {node:parentNode};//进行封装                                
					var store = Ext.getCmp('id_varConfigPanel').getStore();
		    		if(parentNode != null && parentNode.id != 'root') {
		    			store.load(opions);//局部加载
		    			parentNode.expand(true);
		    		} else {
						store.load({scope: this,
							    callback: function(records, operation, success) {
							        Ext.getCmp('id_varConfigPanel').expandAll();
							    }
						});
						return;
		    		}
					//刷新原节点		    		
		    		node = Ext.getCmp('id_varConfigPanel').getStore().getNodeById(sourceNode);
		    		parentNode = node.parentNode;
		    		opions = {node:parentNode};//进行封装                                
		    		if(parentNode != null && parentNode.id != 'root') {
		    			store.load(opions);//局部加载
		    			parentNode.expand(true);
		    		} else {
						store.load({scope: this,
							    callback: function(records, operation, success) {
							        Ext.getCmp('id_varConfigPanel').expandAll();
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
	
	doSpread:function(){
		var store = Ext.getCmp('id_varConfigPanel').getStore();
		store.load({scope: this,
			    callback: function(records, operation, success) {
			        Ext.getCmp('id_varConfigPanel').expandAll();
			    }
		});
	}
	
});