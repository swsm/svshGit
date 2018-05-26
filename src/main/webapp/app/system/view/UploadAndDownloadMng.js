Ext.define('app.system.view.UploadAndDownloadMng', {
	init : function() {
		var form;
		return {
			createView : function(obj) {
				this.createForm();
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
							items : [
								Ext.create('Ext.form.Panel', {
						            title : '文件上传',
						            width : 600,
						            bodyPadding : 5,
						            height : 300,
						            frame : true,
						            items : [ {
						                xtype : 'filefield',
						                name : '文件',
						                id : 'id_fileUpload',
						                fieldLabel : 'File',
						                labelWidth : 50,
						                msgTarget : 'side',
						                allowBlank : false,
						                anchor : '95%',
						                buttonText : '请选择文件...',
						                buttonConfig: {
						                    iconCls: 'fa fa-folder-open'
						                },
						                listeners : {
						                	change : function( filefield, value, eOpts ) {
						                		var index = value.lastIndexOf('\\');
						                		value = value.substring(index + 1, value.length);
						                		filefield.setRawValue(value);
						                	}
						                }
						            }, {
								        xtype: 'label',
								        forId: 'myFieldId',
								        text: 'ScanPort_1465365111314.exe',
								        margin: '0 0 0 27',
								        listeners : {
								        	click: {
									            element: 'el', //bind to the underlying el property on the panel
									            fn: function() {
									            	var fileName = this.getHtml();
									            	//window.open('bmes/main/testDown.mvc?fileName='+encodeURI(encodeURI(this.getHtml())));
									            	Ext.Ajax.request( {
												    	url : 'main/system/testFileExists.mvc',
												    	method : 'post',
												    	params : {
												    		fileName : this.getHtml()
												    	},
												    	success : function(response, options) {
													    	var o = Ext.util.JSON.decode(response.responseText);
													    	if(o.message == 'success') {
													    		window.location.href = 'main/system/testDown.mvc?fileName='+encodeURI(encodeURI(fileName));
													    	} else if(o.message == 'fileNotExists') {
													    		Tool.alert('系统提示', '文件不存在！');
													    		return;
													    	}
												    	},
												    	failure : function() {
												    	}
												    });
									            }
									        }
								        }
						            } , {
								        xtype: 'label',
								        forId: 'myFieldId',
								        text: '熊军_1465368552905.jpg',
								        margin: '0 0 0 27',
								        listeners : {
								        	click: {
									            element: 'el', //bind to the underlying el property on the panel
									            fn: function() {
									            	var fileName = this.getHtml();
									            	//window.open('bmes/main/testDown.mvc?fileName='+encodeURI(encodeURI(this.getHtml())));
									            	Ext.Ajax.request( {
												    	url : 'main/system/testFileExists.mvc',
												    	method : 'post',
												    	params : {
												    		fileName : this.getHtml()
												    	},
												    	success : function(response, options) {
													    	var o = Ext.util.JSON.decode(response.responseText);
													    	if(o.message == 'success') {
													    		window.location.href = 'main/system/testDown.mvc?fileName='+encodeURI(encodeURI(fileName));
													    	} else if(o.message == 'fileNotExists') {
													    		Tool.alert('系统提示', '文件不存在！');
													    		return;
													    	}
												    	},
												    	failure : function() {
												    	}
												    });
									            	//window.location.href = 'main/testDown.mvc?fileName='+encodeURI(encodeURI(this.getHtml()));
									            }
									        }
								        }
						            }],
						            buttons : [{
						            	iconCls:'fa fa-upload' ,
						                text : '上传',
						                handler : function() {
						                    var form = this.up('form').getForm();
						                    if (form.isValid()) {
						                    	//取控件DOM对象   
											    var field = document.getElementById('id_fileUpload');  
											    //取控件中的input元素   
											    var inputs = field.getElementsByTagName('input');  
											    var fileInput = null;  
											    var il = inputs.length;  
											    //取出input 类型为file的元素   
											    for(var i = 0; i < il; i ++){  
											        if(inputs[i].type == 'file'){  
											            fileInput = inputs[i];  
											            break;  
											        }  
											    } 
						                    	//上传文件不能大于4M
						                    	if (fileInput.files[0].size > 4194304) {
						                    		Tool.alert('系统提示', '上传文件超过4M！');
						                    		return;
						                    	}
						                        form.submit({
						                            url : 'main/system/testUpload.mvc',
						                            waitMsg : '正在上传文件中...',
						                            success : function(fp, o) {
						                                Tool.alert('系统提示', '上传文件成功！');
						                            },
						                            failure : function(response, options) {
			    										Tool.alert('系统提示', '上传文件失败！');
			    									}
						                        });
						                    } else {
						                    	alert("请选择文件！");
						                    }
						                }
						            }]
						        })
							]
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
		}
	}
});