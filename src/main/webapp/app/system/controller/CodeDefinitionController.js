Ext.define('app.system.controller.CodeDefinitionController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.codeDefinitioncontroller',
	doQuery : function() {
		this.lookupReference('codeDefinitionGrid').getStore().loadPage(1);
	},
	doReset : function() {
		this.lookupReference('codeDefinitionForm').reset();
	},
	doReflush:function(){
		this.lookupReference('codeDefinitionGrid').getStore().load();
	},
	doRender:function(){
		this.lookupReference('codeDefinitionGrid').getStore().load({ params: { start: Tool.page.start, limit: Tool.page.limit}});
	},
	doInsert: function(){
		var grid = this.lookupReference('codeDefinitionGrid');
		grid.plugins[0].completeEdit();
		var record = {};
		grid.getStore().insert(0, record);
		var sm = grid.getSelectionModel();
		sm.select(0);
	},
	doSave: function(){
		var grid = this.lookupReference('codeDefinitionGrid');
		var store = grid.getStore();
		for(var i=0;i<store.getCount();i++){
			if(Ext.isEmpty(store.getAt(i).get('codeName'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"编码名称\"不允许为空！");
				return;
			} else if(Ext.isEmpty(store.getAt(i).get('codeType'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"编码类别\"不允许为空！");
				return;
			} else if(Ext.isEmpty(store.getAt(i).get('segmentNum'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"段数\"不允许为空！");
				return;
			} else if(Ext.isEmpty(store.getAt(i).get('spacer'))){
				Tool.alert(Tool.str.msgConfirmTitle, "\"间隔符\"不允许为空！");
				return;
			}
		}
		var storeDate=[];
		//更新数据
		for(var i=0; i<store.getUpdatedRecords().length; i++){
			storeDate.push(store.getUpdatedRecords()[i].data); 
		}
		//新增数据
		for(var i=0;i<store.getNewRecords().length;i++){
			var obj = store.getNewRecords()[i].data;
			delete obj.id;
			storeDate.push(obj); 
		}
		function callback() {
			if (!Ext.isEmpty(storeDate)) {
				var loadMarsk = new Ext.LoadMask(grid, {
			        msg: Tool.str.msgLoadMarsk
			    });
				loadMarsk.show();
				Ext.Ajax.request( {
			    	url : 'main/codeDefinition/saveCodeDefinition.mvc',
			    	method : 'post',
			    	params : {
			    		codeStr : Ext.encode(storeDate)
			    	},
			    	success : function(response, options) {
				    	loadMarsk.hide();
				    	Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
				    	store.load();
			    	},
			    	failure : function() {
			    	}
			    });
			}
		}
		Tool.mes().checkUnique(grid, "codeName", "main/codeDefinition/checkCodeUnique.mvc", "需要保存的记录中\"编码名称\"已存在！", callback);
	},
	doDelete: function(){
		var grid = this.lookupReference('codeDefinitionGrid');
		var store = grid.getStore();
		var records = grid.getSelectionModel().getSelection();
		if(records.length > 0) {
			var messageBox = Ext.create('Ext.window.MessageBox',{
				closeToolText : "关闭"
			});
			messageBox.confirm(Tool.str.msgConfirmTitle, '</br>' + Tool.str.msgConfirmDel,function(btn) {
				if (btn == 'yes') {
					var ids = [];
					for (var i=0; i<records.length; i++){
						var record = records[i];
						if(Ext.isEmpty(record.get('createUser'))){
							//前台新增还未保存
							grid.getStore().remove(record);
						} else{
							//数据库数据 后台删除
							ids.push(record.get('id'));
						}
					}
					if(ids.length > 0){
						//要删除的有后台的数据
						Ext.Ajax.request( {
					    	url : 'main/codeDefinition/deleteCodeDefinition.mvc',
					    	method : 'post',
					    	params : { 
					    		ids : ids.join(",")
				    		},
					    	success : function(response, options) {
					    		store.load();
					    		Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
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
	doDetail:function(){
		var grid = this.lookupReference('codeDefinitionGrid');
		var rows=grid.getSelectionModel().getSelection();
		if(rows.length===0){
			Tool.alert(Tool.str.msgConfirmTitle, "请选择需要操作的记录！");
		    return;
		}
		if(rows.length>1){
			Tool.alert(Tool.str.msgConfirmTitle, "请选择单条记录！");
		    return;
		}
		var codeDefDetails;
		Ext.Ajax.request( {
	    	url : 'main/codeDefinition/getCodeDefDetail.mvc',
	    	method : 'post',
	    	async: false,
	    	params : { 
	    		codeId : rows[0].data.id
    		},
	    	success : function(response, options) {
	    		codeDefDetails = Ext.decode(response.responseText);
	    	},
	    	failure : function() {}
	    });
		var resCode = 'app.system.component.CodeDetailWindow';
		var me = this;
		var codeDetailWin = this.lookupReference('codeDetailWin');
		if(!codeDetailWin){
			codeDetailWin = Ext.create(resCode, {
				reference : 'codeDetailWin', 
				title: '编码管理-' + rows[0].data.codeName,
				codeDefDetails: codeDefDetails,
				codeLength: codeDefDetails.length
			});
			this.getView().add(codeDetailWin);
		}
		codeDetailWin.show();
	},
	doNavigate: function(btn){    
        var layout = this.lookupReference('codeDetailWin').getLayout();
        layout[btn.param]();     
        this.lookupReference('prevBtn').setDisabled(!layout.getPrev());
        this.lookupReference('winSaveBtn').setDisabled(layout.getNext());
        this.lookupReference('nextBtn').setDisabled(!layout.getNext());
    },
    win_doChange: function(th){
    	var form = th.up('form');
    	var rcd = form.getValues();
    	if(rcd.segmentType === '1'){
    		form.getComponent('winSegmentContentText').show();
    		form.getComponent('winSegmentCategoryCombo').hide();
    		form.getComponent('winDateFormatCombo').hide();
    		form.getComponent('winSegmentLengthText').hide();
    		form.getComponent('winSegmentDescText').show();
		}else if(rcd.segmentType === '2'){
			form.getComponent('winSegmentContentText').hide();
			form.getComponent('winSegmentCategoryCombo').show();
			form.getComponent('winDateFormatCombo').hide();
			form.getComponent('winSegmentLengthText').hide();
			form.getComponent('winSegmentDescText').show();
		}else if(rcd.segmentType === '3'){
			form.getComponent('winSegmentContentText').hide();
			form.getComponent('winSegmentCategoryCombo').hide();
			form.getComponent('winDateFormatCombo').show();
			form.getComponent('winSegmentLengthText').hide();
			form.getComponent('winSegmentDescText').show();
		}else if(rcd.segmentType === '4'){
			form.getComponent('winSegmentContentText').hide();
			form.getComponent('winSegmentCategoryCombo').hide();
			form.getComponent('winDateFormatCombo').hide();
			form.getComponent('winSegmentLengthText').show();
			form.getComponent('winSegmentDescText').show();
		}
    },
	win_doSave: function(){
		var me = this;
		var num = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'];
		var codeDetailWin = this.lookupReference('codeDetailWin');
		var codeLength = codeDetailWin.codeLength;
		var data = [];
		for(var i = 0; i < codeLength; i ++){
			var detData = this.lookupReference('winPart' + i).getValues();
			if(Ext.isEmpty(detData.segmentType)){
				Tool.alert(Tool.str.msgConfirmTitle, "第" + num[i] + "段的\"编码类型\"不允许为空！", {width: 300});
				return;
			}
			if(detData.segmentType === '1'){
				if(Ext.isEmpty(detData.segmentContent)){
					Tool.alert(Tool.str.msgConfirmTitle, "第" + num[i] + "段的\"编码内容\"不允许为空！", {width: 300});
					return;
				}
			}else if(detData.segmentType === '2'){
				if(Ext.isEmpty(detData.segmentCategory)){
					Tool.alert(Tool.str.msgConfirmTitle, "第" + num[i] + "段的\"编码内容\"不允许为空！", {width: 300});
					return;
				}
			}else if(detData.segmentType === '3'){
				if(Ext.isEmpty(detData.dateFormat)){
					Tool.alert(Tool.str.msgConfirmTitle, "第" + num[i] + "段的\"编码内容\"不允许为空！", {width: 300});
					return;
				}
			}else if(detData.segmentType === '4'){
				if(Ext.isEmpty(detData.segmentLength)){
					Tool.alert(Tool.str.msgConfirmTitle, "第" + num[i] + "段的\"位数\"不允许为空！", {width: 300});
					return;
				}
			}
			if(Ext.isEmpty(detData.segmentDesc)){
				Tool.alert(Tool.str.msgConfirmTitle, "第" + num[i] + "段的\"编码含义\"不允许为空！", {width: 300});
				return;
			}
			data.push(this.lookupReference('winPart' + i).getValues());
		}

		var loadMarsk = new Ext.LoadMask(this.lookupReference('codeDefinitionGrid'), {
	        msg: Tool.str.msgLoadMarsk
	    });
		loadMarsk.show();
		Ext.Ajax.request( {
	    	url : 'main/codeDefinition/saveCodeDefDetail.mvc',
	    	method : 'post',
	    	params : {
	    		detStr : Ext.encode(data)
	    	},
	    	success : function(response, options) {
		    	loadMarsk.hide();
		    	me.win_doClose();
		    	Tool.toast(Tool.str.msgConfirmTitle, Tool.str.msgOptSuccess);
		    	me.lookupReference('codeDefinitionGrid').load();
	    	},
	    	failure : function() {
	    	}
	    });
	
	},
	win_doClose: function(){
		this.lookupReference('codeDetailWin').close();
	}
});
