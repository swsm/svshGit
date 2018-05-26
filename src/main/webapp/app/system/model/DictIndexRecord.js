Ext.define('app.system.model.DictIndexRecord', {
	extend : 'Ext.data.Model',
	fields : [ 	{name : 'id', type : 'string'}, 
				{name : 'dictKey', type : 'string'}, 
				{name : 'dictName', type : 'string'}, 
				{name : 'dictType', type : 'string'}, 
				{name : 'remark', type : 'string'}, 
				{name : 'delFlag', type : 'string'}, 
				{name : 'creatUser', type : 'string'}, 
				{name : 'createDate', type : 'date'}, 
				{name : 'updateDate', type : 'date'}, 
				{name : 'updateUser', type : 'string'}
			],
	changeName : function() {
		
	}
});
