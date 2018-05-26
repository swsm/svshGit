Ext.define('app.system.controller.LoginInfoController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.loginInfocontroller',
	doQuery : function() {
		this.lookupReference('loginInfoGrid').getStore().loadPage(1);
	},
	doReset : function() {
		this.lookupReference('loginInfoForm').reset();
	},
	doReflush:function(){
		this.lookupReference('loginInfoGrid').getStore().load();
	},
	doRender:function(){
		this.lookupReference('loginInfoGrid').getStore().load({ params: { start: Tool.page.start, limit: Tool.page.limit}});
	},
	doLogoff: function(){
		var loginInfoGrid = this.lookupReference('loginInfoGrid');
		var records = loginInfoGrid.getSelectionModel().getSelection();
		if(records.length === 0) {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgOptSelected);
			return false;
		} 
		if(records.length !== 1) {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgSingleSel);
			return false;
		} 
		var messageBox = Ext.create('Ext.window.MessageBox',{
			closeToolText : "关闭"
		});
		messageBox.confirm(Tool.str.msgConfirmTitle, '</br>是否确认注销该用户？',function(btn) {
			if (btn == 'yes') {
				var userName = records[0].get('userName');
				Ext.Ajax.request({
					url : 'main/loginInfo/logOffUser.mvc',
					method : 'post',
					params : {
						userName : userName
					},
					success : function(response,options) {
						var rlt = Ext.util.JSON.decode(response.responseText);
						if (rlt) {
							Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
							loginInfoGrid.getStore().load();
							return false;
						}
					},
					failure : function() {
					}
				});
			}
		});
	
	}
	
});
