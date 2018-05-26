Ext.define('app.system.store.VarConfigStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.varConfigstore',
    proxy : {
		type : 'ajax',
		url : 'main/system/getVarConfigTree.mvc',
		reader : {
			type : 'json',
			rootProperty : 'rows'// 数据
		}
//,
//		extraParams : {
//			
//		}
	},
	fields : [ 'id', 'varDisplay','varName','varValue','varType','varOrder','remark', 'parentVarConfig' ],
	listeners : {
		'beforeexpand' : function(node, eOpts) {
			this.proxy.extraParams.id = node.raw.id;
		},
		'beforeload' : function(s, o) {
			
		}
	}
});