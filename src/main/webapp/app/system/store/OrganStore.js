Ext.define('app.system.store.OrganStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.organstore',
    proxy : {
		type : 'ajax',
		url : 'main/system/getOrganTree.mvc',
		reader : {
			type : 'json',
			rootProperty : 'rows'// 数据
		}
//,
//		extraParams : {
//			
//		}
	},
	fields : [ 'id', 'organCode','organName','dutyUsername','organOrder','remark', 'parentOrgan' ],
	listeners : {
		'beforeexpand' : function(node, eOpts) {
			this.proxy.extraParams.id = node.raw.id;
		},
		'beforeload' : function(s, o) {
			
		}
	}
});
