Ext.define('app.system.controller.DictIndexController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.dictIndexController',
	requires: ['app.system.model.DictIndexRecord'],
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
		var dictIndexList = this.lookupReference('dictIndexList');
		dictIndexList.plugins[0].completeEdit();
		var record = new app.system.model.DictIndexRecord({});
		dictIndexList.getStore().insert(0, record);
		var sm = dictIndexList.getSelectionModel();
		sm.select(record);
	},
	doUpdateRecord:function(){
		var dictIndexList = this.lookupReference('dictIndexList');
		var store = dictIndexList.getStore();
		for(var i=0;i<store.getCount();i++){
			if(Ext.isEmpty(store.getAt(i).get('dictKey'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"字典编码\"不允许为空！");
				return;
			} else if(Ext.isEmpty(store.getAt(i).get('dictName'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"字典名称\"不允许为空！");
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
				var dictKey = store.getNewRecords()[i].get('dictKey');
				var dictName = store.getNewRecords()[i].get('dictName');
				var remark = store.getNewRecords()[i].get('remark');
				var obj = {};
				obj = {dictKey : dictKey,dictName : dictName,remark : remark};
				storeDate.push(obj);
			}
			if (!Ext.isEmpty(storeDate)) {
				var loadMarsk = new Ext.LoadMask(dictIndexList, {
			        msg: Tool.str.msgLoadMarsk
			    });
				loadMarsk.show();
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
							loadMarsk.hide();
							var store = Ext.getCmp('id_dictIndexList').getStore();
							store.load();
							Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						}
					},
					failure : function() {
					}
				});
			}
		}
		Tool.mes().checkUnique(dictIndexList, "dictKey", "main/system/checkDictIndexUnique.mvc", "需要保存的记录中\"字典编码\"已存在！", callback);
	},
	doDelete:function(){
		var grid = Ext.getCmp('id_dictIndexList');
		var records = grid.getSelectionModel().getSelection();
		if (records.length > 0) {
			var messageBox = Ext.create('Ext.window.MessageBox',{
				closeToolText : "关闭"
			});
			messageBox.confirm(Tool.str.msgConfirmTitle, '</br>' + Tool.str.msgConfirmDel,function(btn) {
				if (btn == 'yes') {
					var ids = [];
					var dictNames = [];
					var deleteNum=0;
					for ( var i = 0; i < records.length; i++) {
						var record = records[i];
						if (Ext.isEmpty(record.get('createUser'))) {
							// 前台新增还未保存
							grid.getStore().remove(record);
						} else {
							// 数据库数据
							// 后台删除
							ids.push(record.get('id'));
							dictNames.push(record.get('dictName'));
							deleteNum++;
						}
					}
					if (ids.length > 0) {
						// 要删除的有后台的数据
						Ext.Ajax.request({
							url : 'main/system/deleteDictIndex.mvc',
							method : 'post',
							params : {
								ids : ids.join(","),
								dictNames : dictNames.join(",")
							},
							success : function(response,options) {
								var o = Ext.util.JSON.decode(response.responseText);
								if (o.message == 'success') {
									// 操作成功
									var store = Ext.getCmp('id_dictIndexList').getStore();
									if(store.getCount()-store.getNewRecords().length==deleteNum && store.currentPage>1){
										store.previousPage();//加载上一页 'page'
									} else {
										store.load();
									} 						
									store.load();
									Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
								}
							},
							failure : function() {
							}
						});
					}
				}
			});
		} else {
			Tool.alert(Tool.str.msgConfirmTitle, Tool.str.msgOptSelected);
			return;
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
	}
});
