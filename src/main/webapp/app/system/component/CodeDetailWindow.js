/**
 * 创建一个window，编辑编码明细
 */
Ext.define('app.system.component.CodeDetailWindow',{
	extend : 'Ext.window.Window',
	width : 420,
	height : 460,
	modal : true,
	layout : 'form',
    constrain : true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden;',
    activeItem: 0,
    resizable : false,
    closable : true,
    border : false,
    
	initComponent : function(){
		var segmentTypeStore = Tool.createDictStore({parentKey:'SEGMENT_TYPE'}, 0);
		segmentTypeStore.load();
		var segmentCategoryStore = Tool.createDictStore({parentKey:'SEGMENT_CATEGORY'}, 0);
		segmentCategoryStore.load();
		var dateFormatStore = Tool.createDictStore({parentKey:'DATE_FORMAT'}, 0);
		dateFormatStore.load();
		var winSaveBtn = Ext.create('Ext.button.Button',{
			text : '保存',
			iconCls : 'fa fa-save',
			reference : 'winSaveBtn',
			handler : 'win_doSave'
		});
		var winCancelBtn = Ext.create('Ext.button.Button',{
			text : '取消',
			iconCls : 'fa fa-close',
			reference : 'winCancelBtn',
			handler : 'win_doClose'
		});
		var segmentTypeCombo = {
 		    	xtype: "combobox",
 		    	itemId : 'winSegmentTypeCombo',
		    	name : 'segmentType',
                fieldLabel: "<span style='color:red'>*</span>编码类型",
                editable:false,
                store: segmentTypeStore,
                displayField : 'dictValue',
				valueField : 'dictKey',
                width:300,
                listeners : {
    				'change' : 'win_doChange'
    			}
		};
		var segmentCategoryCombo = {
 		    	xtype: "combobox",
 		    	itemId : 'winSegmentCategoryCombo',
		    	name : 'segmentCategory',
		    	store: segmentCategoryStore,
		    	displayField : 'dictValue',
		    	valueField : 'dictKey',
                fieldLabel: "<span style='color:red'>*</span>编码内容",
                editable:false,
                width:300
		};
		var dateFormatCombo = {
				xtype: "combobox",
				itemId : 'winDateFormatCombo',
				store: dateFormatStore,
				displayField : 'dictValue',
				valueField : 'dictKey',
				name : 'dateFormat',
				fieldLabel: "<span style='color:red'>*</span>编码内容",
				editable:false,
				width:300
		};
		var segmentContentText = {
 		    	xtype: "textfield",
 		    	itemId : 'winSegmentContentText',
		    	name : 'segmentContent',
                fieldLabel: "<span style='color:red'>*</span>编码内容",
                maxLength:25,
                editable:true,
                enforceMaxLength:true,
                width:300
		};
		var segmentLengthNum = {
				xtype: "numberfield",
				itemId : 'winSegmentLengthText',
				name : 'segmentLength',
				fieldLabel: "<span style='color:red'>*</span>位数",
				minValue : 0,
				maxValue : 10,
				allowDecimals : false,
				width:300
		};
		var segmentDescText = {
 		    	xtype: "textfield",
 		    	itemId : 'winSegmentDescText',
		    	name : 'segmentDesc',
                fieldLabel: "<span style='color:red'>*</span>编码含义",
                maxLength:25,
                editable:true,
                enforceMaxLength:true,
                width:300
		};
		// 拼装card
		var codeDefDetails = this.codeDefDetails;
		var num = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'];
		var item = [];
		for(var i = 0; i < codeDefDetails.length; i ++){
			var fromItem = [];
			fromItem.push({xtype: 'label', html:'<p align="center">第' + num[i] + '段/共' + num[codeDefDetails.length - 1] + '段'});
			fromItem.push({xtype: 'textfield', hidden: true, name: 'id'});
			fromItem.push({xtype: 'textfield', hidden: true, name: 'codeDefId'});
			fromItem.push({xtype: 'textfield', hidden: true, name: 'serialNum'});
			fromItem.push({xtype: 'textfield', hidden: true, name: 'createUser'});
			fromItem.push({xtype: 'textfield', hidden: true, name: 'createDate'});
			fromItem.push(segmentTypeCombo);
			fromItem.push(segmentContentText);
			fromItem.push(segmentCategoryCombo);
			fromItem.push(dateFormatCombo);
			fromItem.push(segmentLengthNum);
			fromItem.push(segmentDescText);
			segmentContentText.hidden = codeDefDetails[i].segmentType !== '1';
			segmentCategoryCombo.hidden = codeDefDetails[i].segmentType !== '2';
			dateFormatCombo.hidden = codeDefDetails[i].segmentType !== '3';
			segmentLengthNum.hidden = codeDefDetails[i].segmentType !== '4';
			segmentDescText.hidden = !codeDefDetails[i].segmentType;
			var form = Ext.create('Ext.form.Panel',{
				border : false,
				reference : 'winPart' + i,
				padding : '15 10 15 10',
				defaultType: 'textfield',
				fieldDefaults: {
					labelWidth: 100,
					labelAlign: "right",
					labelSeparator:'：',
					flex: 1,
					margin: 5
				},
				items : fromItem
			});
			console.log(codeDefDetails[i]);
			form.getForm().setValues(codeDefDetails[i]);
			item.push(form);
			if(i != codeDefDetails.length){
				item.push({xtype: 'label', html:'<hr />', width:420});
			}
		}
		// 底部按钮组
		this.items = item;
		this.bbar = ['->', winSaveBtn, winCancelBtn]
		this.callParent();
	},
	config : {
		
	}
		
});