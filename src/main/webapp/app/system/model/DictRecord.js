Ext.define('app.system.model.DictRecord', {
	extend : 'Ext.data.Model',
	fields : [ 	{name : 'id', type : 'string'}, 
				{name : 'dictKey', type : 'string'}, 
				{name : 'dictValue', type : 'string'}, 
				{name : 'dictSort', type : 'int'}, 
				{name : 'openFlag', type : 'string'}, 
				{name : 'parentKey', type : 'string'}, 
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
