Ext.define('app.system.view.CodeDefinitionMng', {
	requires:['app.system.controller.CodeDefinitionController', 'app.utils.statics.MesUtil', 'app.common.viewmodel.CommonModel'],
	
	init : function() {
		var Util=app.utils.statics.MesUtil;
		var queryFormDefaultBtns=Util.createQueryFormDefaultBtn();
		var codeTypeStore = Tool.createDictStore({parentKey:'CODE_TYPE'}, 0);
		codeTypeStore.load();
		var codeTypesStore = Tool.createDictStore({parentKey:'CODE_TYPE'}, 1);
		codeTypesStore.load();
		var spaceMarkStore = Tool.createDictStore({parentKey:'SPACE_MARK'});
		spaceMarkStore.load();
		var codeNameText= {
				xtype : 'textfield',
				maxWidth:300,
				fieldLabel : '编码名称',
				labelSeparator:'：',
				name : 'codeName'
		};
		var codeTypeCombo={
				xtype : 'combobox',
				name : 'codeType',
				fieldLabel : '编码类别',
				maxWidth:300,
				labelSeparator:'：',
				store : codeTypesStore,
				emptyText: '请选择',
				queryMode : 'local',
				displayField : 'dictValue',
				valueField : 'dictKey',
				editable:false
		};
		var form = Util.createQueryForm([codeNameText,codeTypeCombo,{
			columnWidth : .25,
			align : 'right',
			layout : 'column',
			items : queryFormDefaultBtns,
			listeners : {
				afterrender:function(){
					form.getKeyMap().on(13, function() {
						codeDefinitionGrid.getStore().loadPage(1);
					});
				}
			}
		}],{reference:'codeDefinitionForm', id: 'id_codeDefinitionMng_codeDefinitionForm'});
		var store = Ext.create('Ext.data.Store', {
			pageSize : Tool.page.limit,
			listeners : {
				'beforeload' : function() {
					Ext.apply(this.proxy.extraParams, {
						queryParams : Ext.encode(Ext.getCmp('id_codeDefinitionMng_codeDefinitionForm').getValues())
					});
				}
			},
			proxy : {
				type : 'ajax',
				url : 'main/codeDefinition/queryCodeDefinition.mvc',
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
		var grid = Ext.create('Ext.grid.GridPanel', {
			reference : 'codeDefinitionGrid',
			store : store,
			plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
				clicksToEdit : 1
			}) ],
			bbar : Ext.create('app.utils.PagingToolbar', { 
				store : store,
				displayInfo : true
			}),
			viewConfig : {
				enableTextSelection : true,
				selModel : Ext.create(
						'Ext.selection.CheckboxModel', {
						})
			},
			bind : {
				title : '编码列表'
			},
			listeners : {
				'render' : 'doRender'
			},
			border : true,
			columns : [
			{
        		header : '序号',
        		width : 60,
        		xtype : "rownumberer",
        		align : 'center',
        		sortable : false
		    },
		    Util.createGridColumn({header : '<span style=\'color:red\'>*</span>编码名称',dataIndex : 'codeName',width : 150,
		    	editor : new Ext.form.TextField({
		    		maxLength : Tool.fieldmaxlength.V100,
					enforceMaxLength:true
				}),
		    }),
		    Util.createGridColumn({header: '<span style=\'color:red\'>*</span>编码类别',dataIndex: 'codeType',width : 120, 
		    	renderer:function(value, record, store){
			    	return Util.renderDictColumn('CODE_TYPE', value, Tool.getDicts('CODE_TYPE'));
			    },
		    	editor : new Ext.form.ComboBox({
					store : codeTypeStore,  
				    valueField : 'dictKey',  
				    displayField : 'dictValue',  
				    editable : false,
					autoHeight : true
		    	})
		    }),
		    Util.createGridColumn({header : '<span style=\'color:red\'>*</span>段数',width : 100,dataIndex : 'segmentNum',
		    	editor : new Ext.form.NumberField({
					minValue : 2,
					maxValue : 10,
					allowDecimals : false
			    }),
			}),
		    Util.createGridColumn({header : '<span style=\'color:red\'>*</span>间隔符',width : 100,dataIndex : 'spacer',
		    	editor : new Ext.form.ComboBox({
					store : spaceMarkStore,
				    valueField : 'dictKey',
				    displayField : 'dictValue',
				    editable : false
				}),
				renderer:function(value, metaData, record, rowIndex, colIndex, store, view){
					if(!Ext.isEmpty(value)){
						for(var i=0;i<spaceMarkStore.getCount();i++) {
							if(spaceMarkStore.getAt(i).get('dictKey') == value){
								value = spaceMarkStore.getAt(i).get('dictValue');
							}
						}
						value=Ext.util.Format.htmlEncode(value);
						var qtipValue=Ext.util.Format.htmlEncode(value);
						metaData.tdAttr='data-qtip="'+qtipValue+'"';
						return value;
					}
				}
		    }),
		    Util.createGridColumn({header : '编码示例',width : 150,dataIndex : 'codeExample',
		    	editor : new Ext.form.TextField({
		    		maxLength : Tool.fieldmaxlength.V100,
					enforceMaxLength:true
				}),
		    }),
    		Util.createGridColumn({header : '备注',width : 180,dataIndex : 'remark',
    			editor : new Ext.form.TextField({
		    		maxLength : Tool.fieldmaxlength.V500,
					enforceMaxLength:true
				}),
    		})],
			tbar : [ {
				bind : {
					text : '{insert}'
				},
				xtype : 'button',
				iconCls :'fa fa-plus-circle',
				handler : 'doInsert'
			}, {
				bind : {
					text : '{save}'
				},
				xtype : 'button',
				iconCls :'fa fa-save',
				handler : 'doSave'
			}, {
				bind : {
					text : '{delete}'
				},
				xtype : 'button',
				iconCls :'fa fa-minus-circle',
				handler : 'doDelete'
			},{
				bind : {
					text : '{reflush}'
				},
				xtype : 'button',
				iconCls :'fa fa-refresh',
				handler :'doReflush'
			},{
				text : '编码定义',
				xtype : 'button',
				iconCls :'fa fa-edit',
				handler :'doDetail'
			}]
		});
		return {
			createView : function(obj) {
				var codeDefinitionController = Ext.create('app.system.controller.CodeDefinitionController');
				var codeDefinitionViewModel = Ext.create("app.common.viewmodel.CommonModel");
				var panel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:codeDefinitionController,
					viewModel : codeDefinitionViewModel,
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
				Ext.apply(panel, obj);
				return panel;
			}
		};
	}
});