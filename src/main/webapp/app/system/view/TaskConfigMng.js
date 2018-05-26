Ext.define('app.system.view.TaskConfigMng',{
	requires:['app.utils.statics.MesUtil','app.system.controller.TaskConfigController'],
	init:function(){
		var Util = app.utils.statics.MesUtil;
		var queryFormDefaultBtns = Util.createQueryFormDefaultBtn();
		var enableFlagDictType="ENABLE_FLAG";
		var enableFlagDictValues=Tool.getDicts(enableFlagDictType);
		/*********************查询Form**************************/
		
		var taskName = {
			xtype:'textfield',
			columnWidth:0.25,
			fieldLabel:'任务名称',
			labelSeparator:'：',
			maxWidth:300,
			name:'taskName'
		}
		
		var taskCode = {
			xtype:'textfield',
			columnWidth:0.25,
			fieldLabel:'任务编码',
			labelSeparator:'：',
			maxWidth:300,
			name:'taskCode'
		}
		
		var form = Ext.create('Ext.form.Panel',{
			border : true, 
			layout : 'column', 
			title : '查询条件', 
			reference : 'queryForm',
			id:'id_queryForm',
			bodyStyle : {  padding : '5px'  },
			fieldDefaults : {labelAlign : 'right'  },
			items : [
				taskName,
				taskCode,
				 {
					columnWidth : 0.2,
					align : 'right',
					layout : 'column',
					items : queryFormDefaultBtns
				}
			],
			listeners : {
                afterrender:function(){
					form.getKeyMap().on(13, function() {
						grid.getStore().loadPage(1);
					});
				}
            }
			
		});
		
		var store= Ext.create('Ext.data.Store',{
			pageSize:Tool.page.limit,
			listeners : {
				'beforeload' : function() {
					Ext.apply(this.proxy.extraParams, {
						queryParams : Ext.encode(Ext.getCmp('id_queryForm').getValues())
					});
				}
			},
			proxy:{
				type:'ajax',
				url:'main/taskConfigAction/getTaskConfig.mvc',
				actionMethods:{read:'post'},
				reader:{
					type:'json',
					rootProperty:'rows',
					totalProperty:'total'
				}
			}
		});
		
		var grid = Ext.create('Ext.grid.Panel',{
			title:'任务调度配置表',
			reference:'taskConfigGrid',
			id:"id_taskConfigGrid",
			store:store,
			border : true,
			forceFit : false,
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
			
			listeners : {
				'render' : function(grid, e) {grid.getStore().load();}
			},
			columns:[{
				header:'序号',
				width:60,
				xtype:'rownumberer',
				align:'center',
				sortable:false
			},
			Util.createGridColumn({header:'任务名称',width:180,dataIndex:'taskName',editor : new Ext.form.TextField({
					            			enforceMaxLength:true,
					            			autoHeight : true
					            		})}),
			Util.createGridColumn({header:'任务编码',width:180,dataIndex:'taskCode'}),
			Util.createGridColumn({header:'是否启用',width:180,dataIndex:'enableFlag',renderer:function(value,metaData,record,rowIndex){
					return Util.renderDictColumn(enableFlagDictType,value,enableFlagDictValues)}}),
			Util.createGridColumn({header:'调度设置',width:180,dataIndex:'cronExpression',renderer:function(value, mata, rcd){
			    		return '<a style="text-decoration:none;color:blue"  href="javascript:void(0)" onclick=viewport.TaskConfigMng.createWin(\''
						+rcd.get('taskCode')+'\')>'+value+'</a>';
						
			    	}}),
			Util.createGridColumn({header:'备注',width:180,dataIndex:'remark',editor : new Ext.form.TextField({
					            			enforceMaxLength:true,
					            			autoHeight : true
					            		})})
			],
			tbar : [{
					bind : {
						text : '保存'
					},
					xtype : 'button',
					iconCls : 'fa fa-save',
					handler : 'doSave'
					},{
					bind : {
						text : '启用'
					},
					xtype : 'button',
					iconCls : 'fa fa-check-circle',
					handler : 'doUse'
					},{
					bind : {
						text : '禁用'
					},
					xtype : 'button',
					iconCls : 'fa fa-times-circle',
					handler : 'doForbid'
					},{
					bind : {
						text : '{reflush}'
					},
					xtype : 'button',
					iconCls : 'fa fa-refresh',
					handler : 'doReflush'
					}]
		});
		
		return {
			createView : function(obj) {
				var Controller = Ext.create('app.system.controller.TaskConfigController');
				var taskConfigPanel = Ext.create('Ext.panel.Panel',{
					padding:5,
					layout:{
						type:'border'
					},
					bodyStyle : 'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:Controller,
					items :[
						{
							region:'north',
							height:92,
							layout:'fit',
							padding:'0 0 5 0',
							xtype:'container',
							items:[form]
						},{
							region:'center',
							layout:'fit',
							xtype:'container',
							items:[grid]
						}
					]
				});
				Ext.apply(taskConfigPanel,obj);
				viewport.TaskConfigMng = Controller;
				return taskConfigPanel;
			}
		};
	}
})