Ext.define('app.system.controller.RoleController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.rolecontroller',
	requires: ['app.system.model.RoleRecord','app.system.component.CopyRoleWin'],
	doQuery : function() {
		var roleList = this.lookupReference('roleList');
		var store = roleList.getStore();
		store.loadPage(1,{
			params:{
				roleName:Ext.getCmp('roleMng_roleName').getValue(),
				roleType:Ext.getCmp('roleMng_roleType').getValue()
			}
		});
	},
	doReset : function() {
		Ext.getCmp('roleMng_roleName').setValue(null);
		Ext.getCmp('roleMng_roleType').setValue(null);
		
	},
	doInsertRecord:function(){
		var grid = this.lookupReference('roleList');
		grid.plugins[0].completeEdit();
		var record = new app.system.model.RoleRecord({});
		grid.getStore().insert(0, record);
		record.set('enabled','1');
		var sm = grid.getSelectionModel();
		sm.select(record);
	},
	doSave:function(){
		var roleGrid = this.lookupReference('roleList');
		var store = roleGrid.getStore();
		for(var i=0;i<store.getCount();i++){
			if(Ext.isEmpty(store.getAt(i).get('roleName'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"角色名称\"不允许为空！");
				return;
			}
			if(Ext.isEmpty(store.getAt(i).get('roleType'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"角色类别\"不允许为空！");
				return;
			}
			if(Ext.isEmpty(store.getAt(i).get('ignoreDecesion'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"忽略决策\"不允许为空！");
				return;
			}
		}
		function callback(){
			var storeDate = [];
			// 更新数据
			for ( var i = 0; i < store.getUpdatedRecords().length; i++) {
				var id = store.getUpdatedRecords()[i].get('id');
				var roleName = store.getUpdatedRecords()[i].get('roleName');
				var roleType = store.getUpdatedRecords()[i].get('roleType');
				var remark = store.getUpdatedRecords()[i].get('remark');
				var enabled = store.getUpdatedRecords()[i].get('enabled');
				var ignoreDecesion = store.getUpdatedRecords()[i].get('ignoreDecesion');
				var obj = {};
				obj = {id:id,roleName:roleName,roleType:roleType,remark:remark,enabled:enabled,ignoreDecesion:ignoreDecesion};
				storeDate.push(obj);
			}
			// 新增数据
			for ( var i = 0; i < store.getNewRecords().length; i++) {
				var roleName = store.getNewRecords()[i].get('roleName');
				var roleType = store.getNewRecords()[i].get('roleType');
				var remark = store.getNewRecords()[i].get('remark');
				var enabled = store.getNewRecords()[i].get('enabled');
				var ignoreDecesion = store.getNewRecords()[i].get('ignoreDecesion');
				var obj = {};
				obj = {roleName:roleName,roleType:roleType,remark:remark,enabled:enabled,ignoreDecesion:ignoreDecesion};
				storeDate.push(obj);
			}
			if (!Ext.isEmpty(storeDate)) {
				var loadMarsk = new Ext.LoadMask(roleGrid, {
			        msg: Tool.str.msgLoadMarsk
			    });
				loadMarsk.show();
				Ext.Ajax.request({
					url : 'main/system/saveRole.mvc',
					method : 'post',
					params : {
						storeDate : Ext.encode(storeDate),
						modual : '角色管理'
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
		Tool.mes().checkUnique(roleGrid,"roleName","main/system/getCheckRoleUnique.mvc","需要保存的记录中\"角色名称\"已存在！",callback);
	},
	doDelete:function(){
		var roleGrid = this.lookupReference('roleList');
		var records = roleGrid.getSelectionModel().getSelection();
		if (records.length == 0) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要操作的记录！');
			return;
		}
		Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>是否确认删除所选择的记录？', function(btn) {
			if (btn == 'yes') {
				var ids = [];
				var roleNames = [];
				var deleteNum=0;
				for (var i=0;i<records.length;i++){
					if(Ext.isEmpty(records[i].get('createUser'))){
						//前台新增还未保存
						roleGrid.getStore().remove(records[i]);
					} else{
						//数据库数据 后台删除
						ids.push(records[i].get('id'));
						deleteNum++;
						roleNames.push(records[i].get('roleName'));
					}
				}
				if (ids.length > 0) {
					//要删除的有后台的数据
					Ext.Ajax.request( {
				    	url : 'main/system/deleteRole.mvc',
				    	method : 'post',
				    	params : { 
				    		ids : ids.join(","),
				    		roleNames : roleNames.join(","),
				    		modual : '角色管理'
			    		},
				    	success : function(response, options) {
					    	var o = Ext.decode(response.responseText);
					    	if(o.message == 'success') {
					    		//操作成功
					    		var store = roleGrid.getStore();
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
	doDesable:function(){
		var roleGrid = this.lookupReference('roleList');
		var records = roleGrid.getSelectionModel().getSelection();
		if (records.length == 0) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要操作的记录！');
			return;
		}
		Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>是否确认禁用所选择的记录？', function(btn) {
			if (btn == 'yes') {
				var ids = [];
				var roleNames = [];
				for (var i=0;i<records.length;i++){
					if(!Ext.isEmpty(records[i].get('createUser'))){
						ids.push(records[i].get('id'));
						roleNames.push(records[i].get('roleName'));
					}
				}
				if (ids.length > 0) {
					Ext.Ajax.request( {
				    	url : 'main/system/updateRoleEnabled.mvc',
				    	method : 'post',
				    	params : { 
				    		ids : ids.join(","),
				    		roleNames : roleNames.join(","),
				    		modual : '角色管理',
				    		enabled : '0'
			    		},
				    	success : function(response, options) {
					    	var o = Ext.decode(response.responseText);
					    	if(o.message == 'success') {
					    		//操作成功
					    		var store = roleGrid.getStore();
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
		var roleGrid = this.lookupReference('roleList');
		var records = roleGrid.getSelectionModel().getSelection();
		if (records.length == 0) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要操作的记录！');
			return;
		}
		Ext.Msg.confirm(Tool.str.msgConfirmTitle, '</br>是否确认启用所选择的记录？', function(btn) {
			if (btn == 'yes') {
				var ids = [];
				var roleNames = [];
				for (var i=0;i<records.length;i++){
					if(!Ext.isEmpty(records[i].get('createUser'))){
						ids.push(records[i].get('id'));
						roleNames.push(records[i].get('roleName'));
					}
				}
				if (ids.length > 0) {
					Ext.Ajax.request( {
				    	url : 'main/system/updateRoleEnabled.mvc',
				    	method : 'post',
				    	params : { 
				    		ids : ids.join(","),
				    		roleNames : roleNames.join(","),
				    		modual : '角色管理',
				    		enabled : '1'
			    		},
				    	success : function(response, options) {
					    	var o = Ext.decode(response.responseText);
					    	if(o.message == 'success') {
					    		//操作成功
					    		var store = roleGrid.getStore();
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
	doCopy : function(){
		var roleGrid = this.lookupReference('roleList');
		var records = roleGrid.getSelectionModel().getSelection();
		if (records.length === 0) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择需要操作的记录！');
			return;
		}
		if (records.length !== 1) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择一条记录操作！');
			return;
		}
		if(!records[0].get('createUser')){
			Tool.alert(Tool.str.msgConfirmTitle, '请选择已存在的记录操作！');
			return;
		}
		var copyRoleWin = this.lookupReference('copyRoleWin');
		if(!copyRoleWin){
			copyRoleWin = Ext.create('app.system.component.CopyRoleWin', {reference: 'copyRoleWin', title: '复制'});
		}
		this.getView().add(copyRoleWin);
		this.lookupReference('winRoleNameText').setValue(records[0].get('roleName'));
		this.lookupReference('winRoleTypeCombo').setValue(records[0].get('roleType'));
		this.lookupReference('winIgnoreDecesionBox').setValue(records[0].get('ignoreDecesion'));
		copyRoleWin.show();
	},
	win_doSave: function(){
		var copyRoleWin = this.lookupReference('copyRoleWin');
		var roleGrid = this.lookupReference('roleList');
		var records = roleGrid.getSelectionModel().getSelection();
		var roleName = this.lookupReference('winRoleNameText').getValue();
		var roleType = this.lookupReference('winRoleTypeCombo').getValue();
		var ignoreDecesion = this.lookupReference('winIgnoreDecesionBox').getValue() ? '1' : '0';
		var remark = this.lookupReference('winRemarkArea').getValue();
		console.log(roleName + roleType + ignoreDecesion + remark);
		if(Ext.isEmpty(roleName)){
			Tool.alert(Tool.str.msgConfirmTitle, "\"角色名称\"不允许为空！");
			return;
		}
		if(Ext.isEmpty(roleType)){
			Tool.alert(Tool.str.msgConfirmTitle, "\"角色类别\"不允许为空！");
			return;
		}
		if(Ext.isEmpty(ignoreDecesion)){
			Tool.alert(Tool.str.msgConfirmTitle, "\"忽略决策\"不允许为空！");
			return;
		}
		var store = roleGrid.getStore();
		for(var i=0;i<store.getCount();i++){
			if(store.getAt(i).get('roleName') === roleName){
				Tool.alert(Tool.str.msgConfirmTitle, "需要保存的记录中\"角色名称\"已存在！", {height: 170});
				return;
			}
		}
		var loadMask = new Ext.LoadMask(copyRoleWin, {
			msg : Tool.str.msgLoadMarsk
		});
		loadMask.show();
		Ext.Ajax.request({
	    	url : 'main/system/copyRole.mvc',
	    	method : 'post',
	    	params : { 
	    		id : records[0].get('id'),
	    		roleName : roleName,
	    		roleType : roleType,
	    		ignoreDecesion : ignoreDecesion,
	    		remark : remark,
	    		modual : '角色管理'
    		},
	    	success : function(response, options) {
	    		loadMask.hide();
		    	var o = Ext.decode(response.responseText);
		    	if(o.message == 'success') {
		    		//操作成功
					store.load();
					copyRoleWin.close();
					Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
		    	}
	    	},
	    	failure : function() {}
	    });
	},
	win_doClose : function(){
		var copyRoleWin = this.lookupReference('copyRoleWin');
		if(copyRoleWin){
//			this.lookupReference('winCopyRoleForm').reset();
			copyRoleWin.close();
		}
	},
	doReflush:function(){
		var roleList = this.lookupReference('roleList');
		roleList.getStore().load();
	},
	doRender:function(){
		var roleList = this.lookupReference('roleList');
		roleList.getStore().load({ params: { start: Tool.page.start, limit: Tool.page.limit}});
	}
});
