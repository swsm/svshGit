Ext.define('app.system.model.AccessLogRecord', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id',
		type : 'string'
	}, {
		name : 'username',
		type : 'string'
	}, {
		name : 'remoteIp',
		type : 'string'
	}, {
		name : 'modual',
		type : 'string'
	}, {
		name : 'logConten',
		type : 'string'
	}, {
		name : 'userId',
		type : 'string'
	}, {
		name : 'createUser',
		type : 'string'
	}, {
		name : 'createDate',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}],
	changeName : function() {
		
	}
});
