Ext.define('app.system.controller.TaskConfigController',{
	extend:'Ext.app.ViewController',
	alias:'controller.TaskConfigController',
	doQuery:function() {
		this.lookupReference('taskConfigGrid').getStore().load();
	},
	doReset:function() {
		this.lookupReference('queryForm').reset();
	},
	
	doSave:function() {
		var storeDate=[];
		var store=this.lookupReference('taskConfigGrid').getStore();
			//更新数据
		    for(var i=0;i<store.getUpdatedRecords().length;i++){
		      	var remark = store.getUpdatedRecords()[i].get('remark');
		      	var id = store.getUpdatedRecords()[i].get('id');
		      	var taskName = store.getUpdatedRecords()[i].get('taskName');
		      	var taskCode = store.getUpdatedRecords()[i].get('taskCode');
		      	var enableFlag = store.getUpdatedRecords()[i].get('enableFlag');
		      	var cronExpression = store.getUpdatedRecords()[i].get('cronExpression');
		      	var obj={};
		      	obj = {id:id,taskName:taskName,remark:remark,taskCode:taskCode,enableFlag:enableFlag,cronExpression:cronExpression}; 
		      	storeDate.push(obj); 
		    }
		
			if (!Ext.isEmpty(storeDate)) {
				Ext.Ajax.request( {
			    	url : 'main/taskConfigAction/saveTaskConfig.mvc',
			    	method : 'post',
			    	params : {
			    		storeDate : Ext.encode(storeDate)
			    	},
			    	success : function() {
			    		
						Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
			    	}
			    });
			}
			this.lookupReference('taskConfigGrid').getStore().load();
	},
		
	createWin:function(taskCode){
		var taskConfigGrid=this.lookupReference("taskConfigGrid");
		taskConfigGrid.getSelectionModel().deselectAll(true);
		var store = taskConfigGrid.getStore();
	  	store.each(function(record){
	    if(record.get("taskCode")==taskCode){
	   	taskConfigGrid.getSelectionModel().select(record); 
	    	}
	 	 });
		var createCronWin = this.lookupReference('createCronWin');
		if(!createCronWin){
			createCronWin = Ext.create('app.system.component.CreateCronWin',{
						reference : 'createCronWin',
						title : '任务调度配置表'
					}
			);
		}
		this.getView().add(createCronWin);
		createCronWin.show();
	},	
	
	doUse:function(){
		var records = Ext.getCmp('id_taskConfigGrid').getSelectionModel().getSelection();
		if(records.length != 1) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择单条操作！');
			return;
		}
		if(records[0].get('enableFlag')!=0){
			Tool.alert(Tool.str.msgConfirmTitle, '所选任务已启用！');
			return;
		}
			
		var taskCode = records[0].get('taskCode');
		Ext.Ajax.request({
			url : 'main/taskConfigAction/useTaskConfig.mvc',
			method  :'post',
			params:{
				taskCode:taskCode
			},
			success:function(response) {
				Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
			}
		});
		this.lookupReference('taskConfigGrid').getStore().load();
	},
	doForbid:function(){
		var records = Ext.getCmp('id_taskConfigGrid').getSelectionModel().getSelection();
		if(records.length != 1) {
			Tool.alert(Tool.str.msgConfirmTitle, '请选择单条操作！');
			return;
		};
		if(records[0].get('enableFlag')!=1){
			Tool.alert(Tool.str.msgConfirmTitle, '所选任务已禁用！');
			return;
		}
		var taskCode = records[0].get('taskCode');
		Ext.Ajax.request({
			url : 'main/taskConfigAction/forbidTaskConfig.mvc',
			method  :'post',
			params:{
				taskCode:taskCode
			},
			success:function(response) {
				Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
			}
		});	
		this.lookupReference('taskConfigGrid').getStore().load();
	
	},
	doReflush:function(){
		this.lookupReference('taskConfigGrid').getStore().load();
	},
	saveCronExpression:function() {
					var cronExpression=document.getElementById('id_createCronWin').contentWindow.document.getElementById('cron').value;
					var records = Ext.getCmp('id_taskConfigGrid').getSelectionModel().getSelection()[0];
					var taskCode=records.get("taskCode");
					var createCronWin = this.lookupReference('createCronWin')
					Ext.Ajax.request({
						url : 'main/taskConfigAction/updateCronExpression.mvc',
						method  :'post',
						params:{
							taskCode:taskCode,
							cronExpression:cronExpression
						},
						success:function(response) {
							createCronWin.close();
							Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
						}
					});
					this.lookupReference('taskConfigGrid').getStore().load();
			    }
	
})