Ext.define('app.system.controller.AccessLogController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.accessLogController',
	doQuery : function() {
		var queryStartDate = Ext.getCmp('accessLog_queryStartDate').getValue();
		var queryEndDate = Ext.getCmp('accessLog_queryEndDate').getValue();
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
		var accessLogList = this.lookupReference('accessLogList');
		var store = accessLogList.getStore();
		store.loadPage(1,{
			params:{
				queryStartDate : Ext.getCmp('accessLog_queryStartDate').getValue(),
				queryEndDate : Ext.getCmp('accessLog_queryEndDate').getValue(),
				modual : Ext.getCmp('accessLog_modual').getValue(),
				logConten : Ext.getCmp('accessLog_logConten').getValue()
			}
		});
	},
	doReset : function() {
		Ext.getCmp('accessLog_queryStartDate').setValue(Ext.Date.add(new Date(),Ext.Date.DAY,-1));
		Ext.getCmp('accessLog_queryEndDate').setValue(new Date());
		Ext.getCmp('accessLog_modual').setValue(null);
		Ext.getCmp('accessLog_logConten').setValue(null);
	},
	doRender:function(){
		var accessLogList = this.lookupReference('accessLogList');
		accessLogList.getStore().load({ params: { start: Tool.page.start, limit: Tool.page.limit}});
	}
});
