Ext.define('app.system.viewmodel.DictIndexListModel', {
	extend : 'app.common.viewmodel.CommonModel',
	alias : 'viewmodel.dictIndexListModel',
	data : {},
	constructor : function() {
		this.callParent(arguments);
		Ext.apply(this.data, {
			'title' : '字典列表',
			'dictKey' : '字典代码',
			'dictName' : '字典名称',
			'dictType' : '字典类型',
			'remark' : '备注'
		});
	}
});
