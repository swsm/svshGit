Ext.define('app.system.controller.ResourceController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.resourcecontroller',
	requires: ['app.system.model.ResourceRecord'],
	doQuery : function() {
		var resourceList = this.lookupReference('resourceList');
		var store = resourceList.getStore();
		store.loadPage(1,{
			params:{
				resName:Ext.getCmp('resourceMng_resName').getValue(),
				resType:Ext.getCmp('resourceMng_resType').getValue()
			}
		});
	},
	doReset : function() {
		Ext.getCmp('resourceMng_resName').setValue(null);
		Ext.getCmp('resourceMng_resType').setValue(null);
		
	},
	doInsertRecord:function(){
		var grid = this.lookupReference('resourceList');
		grid.plugins[0].completeEdit();
		var record = new app.system.model.ResourceRecord({});
		grid.getStore().insert(0, record);
		record.set('enabled','1');
		record.set('belongSystem','1');
		var sm = grid.getSelectionModel();
		sm.select(record);
	},
	doSave:function(){
		var resourceGrid = this.lookupReference('resourceList');
		var store = resourceGrid.getStore();
		for(var i=0;i<store.getCount();i++){
			if(Ext.isEmpty(store.getAt(i).get('resName'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"资源名称\"不允许为空！");
				return;
			}
			if(Ext.isEmpty(store.getAt(i).get('resCode'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"资源Code\"不允许为空！");
				return;
			}
			if(Ext.isEmpty(store.getAt(i).get('resType'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"资源类型\"不允许为空！");
				return;
			}
			if(Ext.isEmpty(store.getAt(i).get('resOrder'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"排序\"不允许为空！");
				return;
			}
			if(Ext.isEmpty(store.getAt(i).get('belongSystem'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"所属系统\"不允许为空！");
				return;
			}
		}
		function callback(){
			var storeDate = [];
			// 更新数据
			for ( var i = 0; i < store.getUpdatedRecords().length; i++) {
				var id = store.getUpdatedRecords()[i].get('id');
				var resName = store.getUpdatedRecords()[i].get('resName');
				var resCode = store.getUpdatedRecords()[i].get('resCode');
				var resType = store.getUpdatedRecords()[i].get('resType');
				var parentId = store.getUpdatedRecords()[i].get('parentId');
				var remark = store.getUpdatedRecords()[i].get('remark');
				var enabled = store.getUpdatedRecords()[i].get('enabled');
				var resOrder = store.getUpdatedRecords()[i].get('resOrder');
				var belongSystem = store.getUpdatedRecords()[i].get('belongSystem');
				var modualFalg;
				if (resType == '2') {
					modualFalg = '1';
				}
				var obj = {};
				if (Ext.isEmpty(parentId)) {
					obj = {id:id,resName:resName,resCode:resCode,resType:resType,
							remark:remark,enabled:enabled,modualFalg:modualFalg,resOrder:resOrder,belongSystem:belongSystem};
				} else {
					obj = {id:id,resName:resName,resCode:resCode,resType:resType,modualFalg:modualFalg,
							parentResource:{id:parentId},remark:remark,enabled:enabled,resOrder:resOrder,belongSystem:belongSystem};
				}
				storeDate.push(obj);
			}
			// 新增数据
			for ( var i = 0; i < store.getNewRecords().length; i++) {
				var resName = store.getNewRecords()[i].get('resName');
				var resCode = store.getNewRecords()[i].get('resCode');
				var resType = store.getNewRecords()[i].get('resType');
				var parentId = store.getNewRecords()[i].get('parentId');
				var remark = store.getNewRecords()[i].get('remark');
				var enabled = store.getNewRecords()[i].get('enabled');
				var resOrder = store.getNewRecords()[i].get('resOrder');
				var belongSystem = store.getNewRecords()[i].get('belongSystem');
				var modualFalg;
				if (resType == '2') {
					modualFalg = '1';
				}
				var obj = {};
				if (Ext.isEmpty(parentId)) {
					obj = {resName:resName,resCode:resCode,resType:resType,resOrder:resOrder,
							remark:remark,enabled:enabled,modualFalg:modualFalg,belongSystem:belongSystem};
				} else {
					obj = {resName:resName,resCode:resCode,resType:resType,resOrder:resOrder,
							parentResource:{id:parentId},remark:remark,enabled:enabled,modualFalg:modualFalg,belongSystem:belongSystem};
				}
				storeDate.push(obj);
			}
			if (!Ext.isEmpty(storeDate)) {
				var loadMarsk = new Ext.LoadMask(resourceGrid, {
			        msg: Tool.str.msgLoadMarsk
			    });
				loadMarsk.show();
				Ext.Ajax.request({
					url : 'main/system/saveResource.mvc',
					method : 'post',
					params : {
						storeDate : Ext.encode(storeDate),
						modual : '资源管理'
					},
					success : function(response, options) {
						var o = Ext.decode(response.responseText);
						if (o.message == 'success') {
							// 操作成功
							loadMarsk.hide();
							store.load();
							Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						}
					},
					failure : function() {
					}
				});
			}
		}
		Tool.mes().checkUnique(resourceGrid,"resCode","main/system/getResourceUnique.mvc","需要保存的记录中\"资源Code\"已存在！",callback);
	},
	doDelete:function(){
		var resourceGrid = this.lookupReference('resourceList');
		var records = resourceGrid.getSelectionModel().getSelection();
		if (records.length == 0) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要操作的记录！');
			return;
		}
		Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>是否确认删除所选择的记录？', function(btn) {
			if (btn == 'yes') {
				var ids = [];
				var resNames = [];
				var deleteNum=0;
				for (var i=0;i<records.length;i++){
					if(Ext.isEmpty(records[i].get('createUser'))){
						//前台新增还未保存
						resourceGrid.getStore().remove(records[i]);
					} else{
						//数据库数据 后台删除
						ids.push(records[i].get('id'));
						deleteNum++;
						resNames.push(records[i].get('resName'));
					}
				}
				if (ids.length > 0) {
					//要删除的有后台的数据
					Ext.Ajax.request( {
				    	url : 'main/system/deleteResource.mvc',
				    	method : 'post',
				    	params : { 
				    		ids : ids.join(","),
				    		resNames : resNames.join(","),
				    		modual : '资源管理'
			    		},
				    	success : function(response, options) {
					    	var o = Ext.decode(response.responseText);
					    	if(o.message == 'success') {
					    		//操作成功
					    		var store = resourceGrid.getStore();
					    		if(store.getCount()-store.getNewRecords().length==deleteNum && store.currentPage>1){
									store.previousPage();//加载上一页 'page'
								} else {
									store.load();
								}
								Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
					    	}
				    	},
				    	failure : function() {}
				    });
				}
			}
		});
	},
	doDisable:function(){
		var resourceGrid = this.lookupReference('resourceList');
		var records = resourceGrid.getSelectionModel().getSelection();
		if (records.length == 0) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要操作的记录！');
			return;
		}
		Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>是否确认禁用所选择的记录？', function(btn) {
			if (btn == 'yes') {
				var ids = [];
				var resNames = [];
				for (var i=0;i<records.length;i++){
					if(!Ext.isEmpty(records[i].get('createUser'))){
						ids.push(records[i].get('id'));
						resNames.push(records[i].get('resName'));
					}
				}
				if (ids.length > 0) {
					Ext.Ajax.request( {
				    	url : 'main/system/updateResourceEnabled.mvc',
				    	method : 'post',
				    	params : { 
				    		ids : ids.join(","),
				    		resNames : resNames.join(","),
				    		enabled : '0',
				    		modual : '资源管理'
			    		},
				    	success : function(response, options) {
					    	var o = Ext.decode(response.responseText);
					    	if(o.message == 'success') {
					    		//操作成功
					    		var store = resourceGrid.getStore();
								store.load();
								Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
					    	}
				    	},
				    	failure : function() {}
				    });
				}
			}
		});
	},
	doEnable:function(){
		var resourceGrid = this.lookupReference('resourceList');
		var records = resourceGrid.getSelectionModel().getSelection();
		if (records.length == 0) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要操作的记录！');
			return;
		}
		Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>是否确认启用所选择的记录？', function(btn) {
			if (btn == 'yes') {
				var ids = [];
				var resNames = [];
				for (var i=0;i<records.length;i++){
					if(!Ext.isEmpty(records[i].get('createUser'))){
						ids.push(records[i].get('id'));
						resNames.push(records[i].get('resName'));
					}
				}
				if (ids.length > 0) {
					Ext.Ajax.request( {
				    	url : 'main/system/updateResourceEnabled.mvc',
				    	method : 'post',
				    	params : { 
				    		ids : ids.join(","),
				    		resNames : resNames.join(","),
				    		enabled : '1',
				    		modual : '资源管理'
			    		},
				    	success : function(response, options) {
					    	var o = Ext.decode(response.responseText);
					    	if(o.message == 'success') {
					    		//操作成功
					    		var store = resourceGrid.getStore();
								store.load();
								Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
					    	}
				    	},
				    	failure : function() {}
				    });
				}
			}
		});
	},
	doReflush:function(){
		var resourceList = this.lookupReference('resourceList');
		resourceList.getStore().load();
	},
	doRender:function(){
		var resourceList = this.lookupReference('resourceList');
		resourceList.getStore().load({ params: { start: Tool.page.start, limit: Tool.page.limit}});
	},
	doExport : function(){
		var me =this;
		this.lookupReference('resourceList').getStore().load({
		    scope: this,
		    callback: function(records, operation, success) {
		        if (records.length == 0) {
					Tool.alert(Tool.str.msgConfirmTitle, "所导出数据为空！");
					return;
				}
//				var messageBox = Ext.create('Ext.window.MessageBox',{
//					closeToolText : "关闭"
//				});
				Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>' + "确认导出？",function(btn) {
					if (btn == 'yes') {
						window.location.href=encodeURI(encodeURI("main/system/exportResource.mvc?resName=" + Ext.getCmp('resourceMng_resName').getValue()
						+ "&&resType=" + Ext.getCmp('resourceMng_resType').getValue()
						));
					}
				});
		    }
		});
	}
});
