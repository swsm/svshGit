Ext.define('app.system.controller.RoleResourceController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.roleResourceController',
	doQuery : function() {
		var roleResourceList = this.lookupReference('roleResourceList');
		var store = roleResourceList.getStore();
		var parentId = Ext.getCmp('roleResourceMng_resName').getValue();
		store.load({
			params:{
				parentId : parentId,
				flag : '1'
			}
		});
	},
	doReset : function() {
		Ext.getCmp('roleResourceMng_resName').setValue(null);
	}
});
