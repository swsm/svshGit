Ext.define('app.system.view.LoginInfoMng', {
	requires:['app.system.controller.LoginInfoController', 'app.utils.statics.MesUtil', 'app.common.viewmodel.CommonModel'],
	
	init : function() {
		var Util=app.utils.statics.MesUtil;
		var queryFormDefaultBtns=Util.createQueryFormDefaultBtn();
		var userNameText= {
				xtype : 'textfield',
				maxWidth:300,
				fieldLabel : '用户名',
				labelSeparator:'：',
				name : 'userName'
			};
		var trueNameText={
				xtype : 'textfield',
				maxWidth:300,
				fieldLabel : '姓名',
				labelSeparator:'：',
				name : 'trueName'
			};
		var ipAddressText={
				xtype : 'textfield',
				maxWidth:300,
				fieldLabel : 'IP地址',
				labelSeparator:'：',
				name : 'ipAddress'
			};
		var loginInfoForm=Util.createQueryForm([userNameText,trueNameText ,ipAddressText,{
			columnWidth : .25,
			align : 'right',
			layout : 'column',
			items : queryFormDefaultBtns,
			listeners : {
				afterrender:function(){
					loginInfoForm.getKeyMap().on(13, function() {
						loginInfoGrid.getStore().loadPage(1);
					});
				}
			}
		}],{reference:'loginInfoForm', id : 'id_loginInfoMng_loginInfoForm'});
		var store = Ext.create('Ext.data.Store', {
			pageSize : Tool.page.limit,
			listeners : {
				'beforeload' : function() {
					Ext.apply(this.proxy.extraParams, {
						queryParams : Ext.encode(Ext.getCmp('id_loginInfoMng_loginInfoForm').getValues())
					});
				}
			},
			proxy : {
				type : 'ajax',
				url : 'main/loginInfo/queryLoginInfo.mvc',
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
		var loginInfoGrid = Ext.create('Ext.grid.GridPanel', {
			reference : 'loginInfoGrid',
			store : store,
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
				title : '用户列表'
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
		    Util.createGridColumn({header : '用户名',dataIndex : 'userName',width : 150}),
		    Util.createGridColumn({header : '姓名',dataIndex : 'trueName',width : 150}),
		    Util.createGridColumn({header : 'IP地址',width : 120,dataIndex : 'ipAddress'}),
		    Util.createGridColumn({header : '最近访问模块',width : 150,dataIndex : 'moduleName'}),
    		Util.createGridColumn({header : '登录时间',width : 150,dataIndex : 'loginTime'})],
			tbar : [ {
				bind : {
					text : '强制注销'
				},
				xtype : 'button',
				iconCls : 'fa fa-power-off',
				handler : 'doLogoff'
			}, {
				bind : {
					text : '{reflush}'
				},
				xtype : 'button',
				iconCls : 'fa fa-refresh',
				handler : 'doReflush'
			} ]
		});
		return {
			createView : function(obj) {
				var loginInfoController = Ext.create('app.system.controller.LoginInfoController');
				var loginInfoViewModel = Ext.create("app.common.viewmodel.CommonModel");
				var panel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					controller:loginInfoController,
					viewModel : loginInfoViewModel,
					items : [ {
						region : 'north',
						height : 92,
						layout : 'fit',
						padding : '0 0 5 0',
						xtype : 'container',
						items : [ loginInfoForm ]
					}, {
						region : 'center',
						layout : 'fit',
						xtype : 'container',
						items : [ loginInfoGrid ]
					} ]
				});
				Ext.apply(panel, obj);
				return panel;
			}
		};
	}
});