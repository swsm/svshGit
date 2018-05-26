/**
 * 创建一个window，编辑编码明细
 */
Ext.define('app.system.component.CodeDetailWin',{
	extend : 'Ext.window.Window',
	width : 480,
	height : 320,
	modal : true,
	layout : 'card',
    constrain : true,
    activeItem: 0,
    resizable : false,
    closable : true,
    border : false,
    
	initComponent : function(){
		var winSaveBtn = Ext.create('Ext.button.Button',{
			text : '保存',
			iconCls : 'fa fa-save',
			reference : 'winSaveBtn',
			hidden: true,
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
                reference : 'winSegmentTypeCombo',
		    	name : 'segmentType',
                fieldLabel: "<span style='color:red'>*</span>编码类型",
                editable:false,
                width:360,
                listeners : {
    				'change' : 'win_doChange'
    			}
		};
		var segmentCategoryCombo = {
 		    	xtype: "combobox",
                reference : 'winSegmentCategoryCombo',
		    	name : 'segmentCategory',
                fieldLabel: "<span style='color:red'>*</span>编码内容",
                editable:false,
                width:360
		};
		var dateFormatCombo = {
				xtype: "combobox",
				reference : 'winDateFormatCombo',
				name : 'dateFormat',
				fieldLabel: "<span style='color:red'>*</span>编码内容",
				editable:false,
				width:360
		};
		var segmentContentText = {
 		    	xtype: "textfield",
                reference : 'winSegmentContentText',
		    	name : 'segmentContent',
                fieldLabel: "<span style='color:red'>*</span>编码内容",
                maxLength:25,
                editable:true,
                enforceMaxLength:true,
                width:360
		};
		var segmentLengthNum = {
				xtype: "numberfield",
				reference : 'winSegmentLengthText',
				name : 'segmentLength',
				fieldLabel: "<span style='color:red'>*</span>位数",
				minValue : 0,
				maxValue : 99,
				allowDecimals : false,
				width:360
		};
		var segmentDescText = {
 		    	xtype: "textfield",
                reference : 'winSegmentDescText',
		    	name : 'segmentDesc',
                fieldLabel: "<span style='color:red'>*</span>编码含义",
                maxLength:25,
                editable:true,
                enforceMaxLength:true,
                width:360
		};
		// 拼装card
		var codeDefDetails = this.codeDefDetails;
		var items = [];
		var num = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'];
		for(var i = 0; i < codeDefDetails.length; i ++){
			var fromItem = [{xtype: 'label', html:'第' + num[i] + '页，共' + num[codeDefDetails.length - 1] + '页'}];
			if(!codeDefDetails[i].segmentType){
				fromItem.push(segmentTypeCombo);
			}else if(codeDefDetails[i].segmentType = 1){
				fromItem.push(segmentTypeCombo);
				fromItem.push(segmentContentText);
				fromItem.push(segmentDescText);
			}else if(codeDefDetails[i].segmentType = 2){
				fromItem.push(segmentTypeCombo);
				fromItem.push(segmentCategoryCombo);
				fromItem.push(segmentDescText);
			}else if(codeDefDetails[i].segmentType = 3){
				fromItem.push(segmentTypeCombo);
				fromItem.push(dateFormatCombo);
				fromItem.push(segmentDescText);
			}else if(codeDefDetails[i].segmentType = 4){
				fromItem.push(segmentTypeCombo);
				fromItem.push(segmentLengthNum);
				fromItem.push(segmentDescText);
			}
			console.log(fromItem);
			var form = Ext.create('Ext.form.Panel',{
				border : false,
				reference : 'winPart' + i,
				defaults: {
			        anchor: '100%'
			    },
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
			items.push(form);
		}
		// 底部按钮组
		this.items = items;
		this.bbar = ['->', {
			    reference: 'prevBtn',
			    text: '&laquo; 上一页',
			    param: 'prev',
			    disabled: true,
			    handler : 'doNavigate'
			},{
				text : '保存',
				iconCls : 'fa fa-save',
				reference : 'winSaveBtn',
				disabled: true,
				handler : 'win_doSave'
			},{
			    reference: 'nextBtn',
			    text: '下一页 &raquo;',
			    param: 'next',
			    handler : 'doNavigate'
			}]
		this.callParent();
	},
	config : {
		
	}
		
});