Ext.define('app.system.view.ComboboxGridMng', {
	init : function() {
		var form;
		var comboboxGrid;
		var comboboxGridPanel;
		var flag;
		return {
			createMenu : function() {
				if (!comboboxGrid) {
					var comboboxStore = Ext.create('Ext.data.Store', {
						fields : ['id','resName','resType','resCode'],
						autoLoad : true,
						pageSize : 10,
						listeners : {
							'beforeload' : function() {
								Ext.apply(this.proxy.extraParams, {
									resName:Ext.getCmp('comboboxGrid_combobox').getValue()
								});
							}
						},
						proxy : {
							type : 'ajax',
							url : 'main/system/queryResource.mvc',
							actionMethods:{
					            read: "POST"
							},
							reader : {
								type : 'json',
								rootProperty : "rows",
								totalProperty : "total"
							}
						}
					});
					var resTypeStore=Tool.createDictStore({parentKey:'SYS_ZYLBDM'});
					resTypeStore.load();
					comboboxGrid = Ext.create('Ext.grid.GridPanel', {
						reference : 'comboboxList',
						store : comboboxStore,
						viewConfig : {
							enableTextSelection : true,
							loadMask : false
						},
						height:365,
						width:485,
						border:false,
						columns : [ {
							header : '资源名称',
							dataIndex : 'resName',
							width:180,
							sortable : false,
							hideable: false,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							}
						}, {
							header : '资源代码',
							dataIndex : 'resCode',
							width:205,
							sortable : false,
							hideable: false,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							}
						}, {
							header : '资源类别',
							dataIndex : 'resType',
							width:100,
							sortable : false,
							hideable: false,
							loadMask : false,
							renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
								for(var i=0;i<resTypeStore.getCount();i++) {
				    				if(resTypeStore.getAt(i).get('dictKey') == value){
				    					value = resTypeStore.getAt(i).get('dictValue');
				    				}
				    			}
								value=Ext.util.Format.htmlEncode(value);
								var qtipValue=Ext.util.Format.htmlEncode(value);
								metaData.tdAttr='data-qtip="'+qtipValue+'"';
								return value;
							}
						} ],
						listeners:{
							itemclick:function(view,record,item,index,event,options){
								flag = 'enter';
								if (record.get('resName')==Ext.getCmp('comboboxGrid_combobox').getValue()) {
			                		flag="change";
			                	}
								Ext.getCmp('comboboxGrid_combobox').setValue(record.get('resName'));
								comboboxGridPanel.hide();
							},
							cellkeydown:function (grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
				                if (e.getKey() == 13) { 
				                	flag = 'enter';
				                	if (record.get('resName')==Ext.getCmp('comboboxGrid_combobox').getValue()) {
				                		flag="change";
				                	}
									Ext.getCmp('comboboxGrid_combobox').setValue(record.get('resName'));
									comboboxGridPanel.hide();
				                }
			                }
						}
					});
					comboboxGridPanel = Ext.create("Ext.menu.Menu", {
						listeners:{
							mouseover : function (obj,e) {
								Ext.getCmp('comboboxGrid_combobox').focus(false);
								comboboxGridPanel.showBy(Ext.getCmp('comboboxGrid_combobox'));
		                    }
						},
						items : [ comboboxGrid ]
					});
				}
			},
			
			createView : function(obj) {
				this.createForm();
				this.createMenu();
				var panel = Ext.create('Ext.Panel', {
					padding : '5',
					layout : {
						type : 'border'
					},
					bodyStyle:'border-width : 0 0 0 0 !important;background:#FFFFFF',
					items : [ {
						region : 'north',
						layout : 'fit',
						padding : '0 0 5 0',
						xtype : 'container',
						items : [ form ]
					}]
				});
				Ext.apply(panel, obj);
				return panel;
			},
			
			createForm : function() {
				if (!form) {
					var container = Ext.create('Ext.container.Container', {
						anchor : '100%',
						layout : 'column',
						border:false,
						items : [{
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [ {
								xtype : 'textfield',
								name : 'combobox',
								id : 'comboboxGrid_combobox',
								anchor : '60%',
								editable:true,
								listeners:{
									change:function(comboBox,newValue,oldValue){
										 if(flag == 'enter'){
											 flag="change";
										 }else{
											 Ext.fly(this.el).fireEvent('click');
										 }
									},
					                specialkey : function(field,e){
						                if (e.getKey() == 40) {
						                	comboboxGrid.getView().focusRow(0);
											comboboxGrid.getSelectionModel().select(0);
						                }
		                            },  
									render : function() {
								        Ext.fly(this.el).on('click', function(e, t) {
								        	var me=Ext.getCmp("comboboxGrid_combobox");
								        	if (!Ext.isEmpty(me.getValue())) {
								        		comboboxGridPanel.showBy(me);
								        		comboboxGrid.getStore().load({
						                    		callback: function(records, options, success){ 
						                    			me.focus(false);
						                    			if(comboboxGridPanel.isHidden()){
										        			comboboxGridPanel.showBy(me);
										        		}
						                    		}
						                    	} );
								        	}else{
								        		comboboxGridPanel.hide();
								        	}
							            });
									}
								}
							} ]
						}]
					});
					form = Ext.create('Ext.form.Panel', {
						border : false,
						reference : 'form',
						bodyStyle : {
							padding : '5px'
						},
						fieldDefaults : {
							labelAlign : 'right',
							msgTarget : 'side'
						},
						items : [ container ]
					});
				}
			}
		};
	}
});