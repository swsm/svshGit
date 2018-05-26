Ext.define('app.system.controller.DictController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.dictController',
	requires: ['app.system.model.DictRecord'],
	doQuery : function() {
	},
	doReset : function() {
	},
	doInsertRecord:function(){
		var grid = this.lookupReference('dictList');
		grid.plugins[0].completeEdit();
		var record = new app.system.model.DictRecord({});
		grid.getStore().insert(0, record);
		record.set('parentKey', Ext.getCmp('id_dictIndexList').getSelectionModel().getSelection()[0].get('dictKey'));
		record.set('openFlag', '1');
		var sm = grid.getSelectionModel();
		sm.select(record);
	},
	doUpdateRecord:function(){
		var dictList = this.lookupReference('dictList');
		var store = dictList.getStore();
		for(var i=0;i<store.getCount();i++){
			if(Ext.isEmpty(store.getAt(i).get('dictValue'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"字典显示值\"不允许为空！");
				return;
			} else if(Ext.isEmpty(store.getAt(i).get('dictKey'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"字典Key\"不允许为空！");
				return;
			} else if(Ext.isEmpty(store.getAt(i).get('dictSort'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"排序\"不允许为空！");
				return;
			}
		}
		function callback() {
			var storeDate=[];
			//更新数据
		    for(var i=0;i<store.getUpdatedRecords().length;i++){
		    	var id = store.getUpdatedRecords()[i].get('id');
		    	var dictKey = store.getUpdatedRecords()[i].get('dictKey');
		      	var dictValue = store.getUpdatedRecords()[i].get('dictValue');
		      	var remark = store.getUpdatedRecords()[i].get('remark');
		      	var openFlag = store.getUpdatedRecords()[i].get('openFlag');
		      	var dictSort = store.getUpdatedRecords()[i].get('dictSort');
		      	var parentKey = store.getUpdatedRecords()[i].get('parentKey');
		      	var obj={};
		      	obj = {id:id,dictKey:dictKey,dictValue:dictValue,remark:remark,openFlag:openFlag,dictSort:dictSort,parentKey:parentKey}; 
		      	storeDate.push(obj); 
		    }
		    //新增数据
		    for(var i=0;i<store.getNewRecords().length;i++){
			    var dictKey = store.getNewRecords()[i].get('dictKey');
			    var dictValue = store.getNewRecords()[i].get('dictValue');
			    var remark = store.getNewRecords()[i].get('remark');
			    var openFlag = store.getNewRecords()[i].get('openFlag');
			    var dictSort = store.getNewRecords()[i].get('dictSort');
			    var parentKey = store.getNewRecords()[i].get('parentKey');
			    var obj={};
			    obj = {dictKey:dictKey,dictValue:dictValue,remark:remark,openFlag:openFlag,dictSort:dictSort,parentKey:parentKey}; 
			    storeDate.push(obj); 
			}
			if (!Ext.isEmpty(storeDate)) {
				var loadMarsk = new Ext.LoadMask(dictList, {
			        msg: Tool.str.msgLoadMarsk
			    });
				loadMarsk.show();
				Ext.Ajax.request( {
			    	url : 'main/system/saveOrUpdateDict.mvc',
			    	method : 'post',
			    	params : {
			    		storeDate : Ext.encode(storeDate),
			    		modual : '字典明细'
			    	},
			    	success : function(response, options) {
				    	var o = Ext.util.JSON.decode(response.responseText);
				    	if(o.message == 'success') {
				    		//操作成功
				    		loadMarsk.hide();
							store.load({params :
								{ parentKey : Ext.getCmp('id_dictIndexList').getSelectionModel().getSelection()[0].get('dictKey') }
							});
							Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
				    	}
			    	},
			    	failure : function() {
			    	}
			    });
			}
		}
		Tool.mes().checkUnique(dictList,"dictValue,parentKey","main/system/checkDictUnique.mvc","需要保存的记录中\"字典显示值\"已存在！", check,320);
		function check(){
			Tool.mes().checkUnique(dictList,"dictKey,parentKey","main/system/checkDictUnique.mvc","需要保存的记录中\"字典Key\"已存在！", callback);
		}
	},
	doDelete:function(){
		var grid = this.lookupReference('dictList');
		var store = grid.getStore();
		var deleteNum=0;
		var records = grid.getSelectionModel().getSelection();
		if(records.length > 0) {
			var messageBox = Ext.create('Ext.window.MessageBox',{
				closeToolText : "关闭"
			});
			messageBox.confirm(Tool.str.msgConfirmTitle, '</br>' + Tool.str.msgConfirmDel,function(btn) {
				if (btn == 'yes') {
					var ids = [];
					var dictValues = [];
					for (var i=0;i<records.length;i++){
						var record = records[i];
						if(Ext.isEmpty(record.get('createUser'))){
							//前台新增还未保存
							grid.getStore().remove(record);
						} else{
							deleteNum++;
							//数据库数据 后台删除
							ids.push(record.get('id'));
							dictValues.push(record.get('dictValue')+"-"+record.get('parentKey'));
						}
					}
					if(ids.length > 0){
						//要删除的有后台的数据
						Ext.Ajax.request( {
					    	url : 'main/system/deleteDict.mvc',
					    	method : 'post',
					    	params : { 
					    		ids : ids.join(","),
					    		dictValues : dictValues.join(","),
					    		modual : '字典明细'
				    		},
					    	success : function(response, options) {
						    	var o = Ext.util.JSON.decode(response.responseText);
						    	if(o.message == 'success') {
						    		//操作成功
						    		if(store.getCount()-store.getNewRecords().length==deleteNum && store.currentPage>1){
										store.previousPage({params :
											{ parentKey : Ext.getCmp('id_dictIndexList').getSelectionModel().getSelection()[0].get('dictKey') }
											});//加载上一页 'page'
									} else {
										store.load({params :
											{ parentKey : Ext.getCmp('id_dictIndexList').getSelectionModel().getSelection()[0].get('dictKey') }
											});
									}
//									store.load({params :
//										{ parentKey : Ext.getCmp('id_dictIndexList').getSelectionModel().getSelection()[0].get('dictKey') }
//									});
									Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						    	}
					    	},
					    	failure : function() {}
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
		var store = this.lookupReference('dictList').getStore();
		store.load({params :
			{ parentKey : Ext.getCmp('id_dictIndexList').getSelectionModel().getSelection()[0].get('dictKey') }
		});
	},
	doRender:function(){
	}
});
