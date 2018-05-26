Ext.define('app.system.viewmodel.UserListModel', {
	extend : 'app.common.viewmodel.CommonModel',
	alias : 'viewmodel.userlistmodel',
	data : {},
	constructor : function() {
		this.callParent(arguments);
		Ext.apply(this.data, {
			'workNo' : '工号',
			'truename' : '姓名',
			'username' : '用户名',
			'organName' : '所属机构',
			'roleNameStr' : '角色',
			'enabled' : '状态',
			'mobile' : '手机',
			'telephone' : '短号',
			'address' : '联系地址',
			'email' : '邮箱地址',
			'createDate' : '创建日期',
			'title' : '用户信息列表'
		});
	}
});
