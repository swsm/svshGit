/**
 * 创建一个window，复制角色使用
 */
Ext.define('app.system.component.CopyRoleWin',{
	extend : 'Ext.window.Window',
	width : 620,
	height : 295,
	modal : true,
	layout : 'fit',
    constrain : true,
    resizable : false,
//    closeAction : 'destory',
    closable : true,
    border : false,
    
	initComponent : function(){
		var roleTypeStore=Tool.createDictStore({parentKey:'SYS_JSLBDM'});
		roleTypeStore.load();
		//底部按钮组
		var winSaveBtn = Ext.create('Ext.button.Button',{
			text : '保存',
			iconCls : 'fa fa-save',
			reference : 'winSaveBtn',
			handler : 'win_doSave'
		});
		var winCancelBtn = Ext.create('Ext.button.Button',{
			text : '取消',
			iconCls : 'fa fa-close',
			reference : 'winCancelBtn',
			handler : 'win_doClose'
		});
		//填写信息的Form
		var batchForm = Ext.create('Ext.form.Panel',{
			layout : {type:'table',columns:2,tableAttrs:{style:"border:none"}},
			border : false,
			reference : 'winCopyRoleForm',
			defaults: {
		        anchor: '100%'
		    },
		    padding : '15 10 15 10',
		    defaultType: 'textfield',
		    fieldDefaults: {
		          labelWidth: 100,
		          labelAlign: "right",
		          labelSeparator:'：',
		          allowBlank: false,
		          editable:false,
		          flex: 1,
		          margin: 5
		        },
		    items : [{
		 		    	xtype: "textfield",
		                reference : 'winRoleNameText',
				    	name : 'roleName',
		                fieldLabel: "<span style='color:red'>*</span>角色名称",
		                maxLength:25,
		                editable:true,
		                enforceMaxLength:true,
		                width:560,
				    	colspan:3
				    },{
				    	xtype: "combobox",
				    	reference : 'winRoleTypeCombo',
				    	name : 'roleType',
				    	fieldLabel: "<span style='color:red'>*</span>角色类别",
				    	store:roleTypeStore,
				    	valueField:'dictKey',
				    	displayField:'dictValue',
				    	emptyText:'请选择'
				    },{
				    	xtype: "checkbox",
				    	reference : 'winIgnoreDecesionBox',
				    	boxLabel: "忽略决策",
				    	labelSeparator:'',
				    	style:"position:relative;left:10px"
				    },{
				    	xtype: "textarea",
				    	reference : 'winRemarkArea',
				    	name : 'remark',
				    	editable:true,
				    	fieldLabel: '备注',
				    	allowBlank: true,
				    	enforceMaxLength:true,
				    	width:560,
				    	maxLength : 50,
				    	colspan:3
				    }]
		});
		this.items = [batchForm];
		this.bbar = [{
	         xtype:"tbfill"
			},
			winSaveBtn,winCancelBtn]
		this.callParent();
	},
	config : {
		
	}
		
});