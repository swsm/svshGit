Ext.define('app.system.component.CreateCronWin',{
	extend : 'Ext.window.Window',
	width : 840,
	height : 678,
	modal : true,
	layout : 'fit',
    constrain : true,
    resizable : false,
    closable : true,
    border : false,   
	initComponent : function(){
		var records = Ext.getCmp('id_taskConfigGrid').getSelectionModel().getSelection()[0];
		var url = 'script/cron/createCron.jsp?value='+records.get("cronExpression");
		this.html = '<iframe frameborder=0 id = "id_createCronWin" width="100%" height="100%" marginheight="0" marginwidth="0" scrolling="no" src="'+url+'"></iframe>';
		this.bbar = [{
	         	xtype:"tbfill"
			}, {
				xtype : 'button',
				text : '保存',
				iconCls : 'fa fa-save',
				handler: 'saveCronExpression'
			}
		];
		this.callParent();
	}
		
});