Ext.define('app.system.model.UserRecord', {
	extend : 'Ext.data.Model',
	fields : [ {name : 'id', type : 'string'},
			   {name : 'workNo', type : 'string'}, 
			   {name : 'username', type : 'string'},
			   {name : 'truename', type : 'string'}, 
			   {name : 'password', type : 'string'}, 
			   {name : 'createDate', type : 'string'},
			   {name : 'roleNameStr', type : 'string'}, 
			   {name : 'enabled', type : 'string'}, 
			   {name : 'mobile', type : 'string'}, 
			   {name : 'email', type : 'string'},
			   {name : 'telephone', type : 'string'}, 
			   {name : 'address', type : 'string'},
			   {name : 'remark', type : 'string'},
			   {name : 'delFlag', type : 'string'}, 
			   {name : 'organName', type : 'string'},
			   {name : 'organCode', type : 'string'},
			   {name : 'organId', type : 'string'} 
			 ],
	changeName : function() {
		
	}
});
