Ext.define('app.system.model.ResourceRecord', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id',
		type : 'string'
	}, {
		name : 'parentId',
		type : 'string'
	}, {
		name : 'resName',
		type : 'string'
	}, {
		name : 'parentResName',
		type : 'string'
	}, {
		name : 'resType',
		type : 'string'
	}, {
		name : 'resCode',
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
	}, {
		name : 'enabled',
		type : 'string'
	}, {
		name : 'resOrder',
		type : 'string'
	}, {
		name : 'modualFalg',
		type : 'string'
	}],
	changeName : function() {
		
	}
});
