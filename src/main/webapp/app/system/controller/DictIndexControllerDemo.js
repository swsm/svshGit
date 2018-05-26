Ext.define('app.system.controller.DictIndexControllerDemo', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.dictIndexController',
	doQuery : function() {
		var dictIndexList = this.lookupReference('dictIndexList');
		var store = dictIndexList.getStore();
		store.loadPage(1,{params :{
				dictName : Ext.getCmp('dictName').getValue()
			}
		});
	},
	doReset : function() {
		Ext.getCmp('dictName').setValue(null);
	},
	doInsertRecord:function(){
		var detailGridPanel = this.lookupReference('detailGridPanel');
		detailGridPanel.setVisible(true);
		Ext.getCmp('detailGridPanel_id').setValue(null);
		this.detailGridPanel_reset();
	},
	doUpdateRecord:function(){
		var dictIndexList = this.lookupReference('dictIndexList');
		var store = dictIndexList.getStore();
		for(var i=0;i<store.getCount();i++){
			if(Ext.isEmpty(store.getAt(i).get('dictKey'))){
				Tool.alert("提示", "\"字典编码\"不能为空！");
				return;
			} else if(Ext.isEmpty(store.getAt(i).get('dictName'))){
				Tool.alert("提示", "\"字典名称\"不能为空！");
				return;
			}
		}
		function callback(){
			var storeDate = [];
			// 更新数据
			for ( var i = 0; i < store.getUpdatedRecords().length; i++) {
				var id = store.getUpdatedRecords()[i].get('id');
				var dictKey = store.getUpdatedRecords()[i].get('dictKey');
				var dictName = store.getUpdatedRecords()[i].get('dictName');
				var remark = store.getUpdatedRecords()[i].get('remark');
				var obj = {};
				obj = {id : id,dictKey : dictKey,dictName : dictName,remark : remark};
				storeDate.push(obj);
			}
			// 新增数据
			for ( var i = 0; i < store.getNewRecords().length; i++) {
				var id = store.getNewRecords()[i].get('id');
				var dictKey = store.getNewRecords()[i].get('dictKey');
				var dictName = store.getNewRecords()[i].get('dictName');
				var remark = store.getNewRecords()[i].get('remark');
				var obj = {};
				obj = {id : id,dictKey : dictKey,dictName : dictName,remark : remark};
				storeDate.push(obj);
			}
			Ext.Ajax.request({
				url : 'main/system/saveOrUpdateDictIndex.mvc',
				method : 'post',
				params : {
					storeDate : Ext.encode(storeDate)
				},
				success : function(response,
						options) {
					var o = Ext.util.JSON.decode(response.responseText);
					if (o.message == 'success') {
						// 操作成功
						var store = Ext.getCmp('id_dictIndexList').getStore();
						store.load();
					}
				},
				failure : function() {
				}
			});
		}
		Tool.mes().checkUnique(dictIndexList, "dictKey", "main/system/checkDictIndexUnique.mvc", "需要保存的记录中\"字典编码\"已存在！", callback);
	},
	doDelete:function(){
		var grid = Ext.getCmp('id_dictIndexList');
		var records = grid.getSelectionModel().getSelection();
		if (records.length > 0) {
			Ext.Msg.confirm('系统提示','确定要删除所选记录？',function(btn) {
				if (btn == 'yes') {
					var ids = [];
					var dictNames = [];
					for ( var i = 0; i < records.length; i++) {
						var record = records[i];
						if (record.get('id').charAt(0) == '_') {
							// 前台新增还未保存
							grid.getStore().remove(record);
						} else {
							// 数据库数据
							// 后台删除
							ids.push(record.get('id'));
							dictNames.push(record.get('dictName'));
						}
					}
					if (ids.length > 0) {
						// 要删除的有后台的数据
						Ext.Ajax.request({
							url : 'main/system/deleteDictIndex.mvc',
							method : 'post',
							params : {
								ids : ids.join(","),
								dictNames : dictNames.join(","),
								modual : '字典管理'
							},
							success : function(response,options) {
								var o = Ext.util.JSON.decode(response.responseText);
								if (o.message == 'success') {
									// 操作成功
									var store = Ext.getCmp('id_dictIndexList').getStore();
									store.load();
								}
							},
							failure : function() {
							}
						});
					}
				}
			});
		} else {
			Tool.alert("提示", "请选择需要操作的记录！");
		}
	},
	doReflush:function(){
		var store = Ext.getCmp('id_dictIndexList').getStore();
		store.load({
			params : {
				dictName : Ext.getCmp('dictName').getValue()
			}
		});
	},
	doRender:function(){
		var dictIndexList = this.lookupReference('dictIndexList');
		dictIndexList.getStore().load({ params: { start: Tool.page.start, limit: Tool.page.limit}});
	}, 
	detailGridPanel_reset : function() {
		if (Ext.getCmp('detailGridPanel_id').getValue()) {
			var records = this.lookupReference('dictIndexList').getSelectionModel().getSelection();
			for(var i = 0; i < records.length; i++) {
				if (records[i].get('id') == Ext.getCmp('detailGridPanel_id').getValue()) {
					//修改
					Ext.getCmp('detailGridPanel_dictKey').setValue(records[i].get('dictKey'));
					Ext.getCmp('detailGridPanel_dictName').setValue(records[i].get('dictName'));
					Ext.getCmp('detailGridPanel_remark').setValue(records[i].get('remark'));
				}
			}
			
		} else {
			//新增
			Ext.getCmp('detailGridPanel_dictKey').setValue(null);
			Ext.getCmp('detailGridPanel_dictName').setValue(null);
			Ext.getCmp('detailGridPanel_remark').setValue(null);
		}
	},
	doBeforeItemClick : function() {
		var detailGridPanel = this.lookupReference('detailGridPanel');
		detailGridPanel.setVisible(false);
		this.detailGridPanel_reset();
		
	},
	doBeforeItemDbClick : function( grid, record, item, index, e, eOpts) {
		var detailGridPanel = this.lookupReference('detailGridPanel');
		if (detailGridPanel.isHidden()) {
			detailGridPanel.setVisible(true);
		}
		Ext.getCmp('detailGridPanel_id').setValue(record.get('id'));
		this.detailGridPanel_reset();		
	}
	
	
});
