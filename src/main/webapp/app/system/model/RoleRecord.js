Ext.define('app.system.model.RoleRecord', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id',
		type : 'string'
	}, {
		name : 'roleName',
		type : 'string'
	}, {
		name : 'roleType',
		type : 'string'
	}, {
		name : 'enabled',
		type : 'string'
	}, {
		name : 'createUser',
		type : 'string'
	},{
		name : 'createDate',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}],
	changeName : function() {
		
	}
});
