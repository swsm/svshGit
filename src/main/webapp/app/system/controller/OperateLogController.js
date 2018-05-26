Ext.define('app.system.controller.OperateLogController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.operateLogController',
	doQuery : function() {
		var queryStartDate = Ext.getCmp('operateLog_queryStartDate').getValue();
		var queryEndDate = Ext.getCmp('operateLog_queryEndDate').getValue();
		if(queryStartDate>queryEndDate){
			Tool.alert(Tool.str.msgConfirmTitle,'开始时间必须在结束时间之前');
			return;
		}
		if(!Ext.isEmpty(queryStartDate) && !Ext.isEmpty(queryEndDate)){
			if(queryEndDate > Ext.Date.add(queryStartDate,Ext.Date.DAY,7)){
				Tool.alert(Tool.str.msgConfirmTitle, '发生时段不能超过7天！');
				return;
			}
		}
		var operateLogList = this.lookupReference('operateLogList');
		var store = operateLogList.getStore();
		store.loadPage(1);
	},
	doReset : function() {
		Ext.getCmp('operateLog_queryStartDate').setValue(Ext.Date.add(new Date(),Ext.Date.DAY,-1));
		Ext.getCmp('operateLog_queryEndDate').setValue(new Date());
		Ext.getCmp('operateLog_modual').setValue(null);
		Ext.getCmp('operateLog_logContent').setValue(null);
		Ext.getCmp('operateLog_operationUser').setValue(null);
		Ext.getCmp('operateLog_operateIp').setValue(null);
		
	},
	doRender:function(){
		var operateLogList = this.lookupReference('operateLogList');
		operateLogList.getStore().load({ params: { start: Tool.page.start, limit: Tool.page.limit}});
	},
	doExport : function(){
		var me =this;
		this.lookupReference('operateLogList').getStore().load({
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
						window.location.href=encodeURI(encodeURI("main/log/exportData.mvc?queryStartDate="
						+ Ext.util.Format.date(Ext.getCmp('operateLog_queryStartDate').getValue(), "Y-m-d H:i:s")
						+ "&&queryEndDate=" + Ext.util.Format.date(Ext.getCmp('operateLog_queryEndDate').getValue(), "Y-m-d H:i:s")
						+ "&&modual=" + Ext.getCmp('operateLog_modual').getValue()
						+ "&&logContent=" + Ext.getCmp('operateLog_logContent').getValue()
						+ "&&operationUser=" + Ext.getCmp('operateLog_operationUser').getValue()
						+ "&&operateIp=" + Ext.getCmp('operateLog_operateIp').getValue()));
					}
				});
		    }
		});
	}
});
