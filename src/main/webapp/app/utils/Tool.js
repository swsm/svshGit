Tool = function() {
	return {
		page:{
			limit:15,
			start:0
		},
		/**
		 * panel高设置
		 */
		formheight: {
			//三行且有一行是textarea
			threeLines: 218,
			//查询，一行
			aLine: 90,
			//查询，一行
			fourLines: 215,
		},
		/**
		 * 用户输入框长度控制
		 */
		fieldmaxlength:{
			N2:2,
			N4:4,
			V50:15,
			V100:33,
			V200:66,
			V400:100,
			V500:160,
			V1000:330,
			V2000:660,
			V4000:1000
		},
		str : {
			msgOptSelected : '请选择需要操作的记录！',
			msgConfirmDel : "是否确认删除所选择的记录？",
			msgConfirmSave : "是否保存记录？",
			msgConfirmTitle : '提示信息',
			msgSingleSel : '请选择单条记录！',
			msgDelRelation : "所选记录中存在\"{0}\"，不允许删除！",
			msgDel : "所选记录中存在不允许删除的记录！",
			msgUnique : "需要保存的记录中\"{0}\"已存在！",
			msgNotNull : "\"{0}\"不允许为空！",
			msgOptSuccess:'操作成功！',
			msgOptFail:'操作失败！',
			msgLoadMarsk:'保存中,请稍候...',
			msgConfirmFinish : "是否确认完成？"
		},
		
		/**
		 * 判断是否有权限
		 * @param recCode
		 * @returns
		 */
		hasAuthor : function(recCode) {
			Ext.Ajax.request({
				url : 'main/system/hasAuthor.mvc',
				async : true,
				success : function(response) {
					var hasAuthor = response.responseText;
					return hasAuthor;
				}
			});
		},
		/**
		 * 常见字典Store
		 * @param config
		 * @param flag
		 * @returns
		 */
		createDictStore : function(config, flag) {
			var modelCode="app.system.model.DictRecord";
			var store=Ext.create('Ext.data.Store', {
				autoLoad : false,
				pageSize: 100,
				listeners : {
					'beforeload' : function() {
						if(config){
							Ext.apply(this.proxy.extraParams, config);
						}
					},
					 'load' : function(store, records, successful, operation){
					    if(flag == 1){
					        store.insert(0, {
					            dictKey : '',
					            dictValue : '不限',
					            remark : '不限'
					        });
					    }
					 }
				},
				proxy : {
					type : 'ajax',
					url : 'main/system/getDict.mvc',
					reader : {
						type : 'json',
						rootProperty : "rows",
						totalProperty : "total"
					}
				},
				model:Ext.create(modelCode)
			});
			return store;
		},
		/**
		 * 检验某一字段的唯一性。
		 * checkUnique方法中参数：
		 * 		grid：前台的grid，
		 * 		uniqueField：要检测唯一性的字段，
		 * 		ajaxUrl:ajax请求的url，
		 * 		callBack：验证通过后的操作，
		 * @return {}
		 */
		mes : function() {
			var parsorRecordArray = function(arr) {
				var data = [];
				for (var i = 0; i < arr.length; i++) {
					data.push(Ext.encode(arr[i].data));
				}
				if (data.length > 0) {
					return Ext.encode(data);
				}
			};
			var createAjaxAndRequest = function(ajaxUrl, params, callBack) {
				Ext.Ajax.request({
					method: 'post',
					url: ajaxUrl,
					params: params,
					success: function(response, options){
						causeMaps=Ext.decode(response.responseText);
						var rlt = causeMaps.message;
						if(rlt === "failed"){
							if(params['msg']){
								var width=300;
								if(params['width']){
									width=params['width'];
								}
								Tool.alert("提示信息",params['msg'],{width:width});
							}else{
								Tool.alert("提示信息","唯一字段已存在，请重新填写！");
							}
						} else {
							callBack.call();
						}
					}
				});
			};
			return {
				checkUnique : function(grid, uniqueField, ajaxUrl, msg,callBack,width) {
					if (!grid) {
						alert("grid is undefined!");
						return;
					}
					if (!uniqueField) {
						alert("uniqueField is undefined!");
						return;
					}
					if (!ajaxUrl) {
						alert("ajaxUrl is undefined!");
						return;
					}
					grid.plugins[0].completeEdit();
					var store = grid.getStore();
					var deleteRecords = store.getRemovedRecords();
					var updateRecords = store.getUpdatedRecords();
					var insertRecords = store.getNewRecords();
					var del = parsorRecordArray(deleteRecords);
					var add = parsorRecordArray(insertRecords);
					var update = parsorRecordArray(updateRecords);
					var params = {
						"uniqueField" : uniqueField,
						"delete" : del,
						"insert" : add,
						"update" : update,
						'msg': msg,
						'width': width
					};
					createAjaxAndRequest(ajaxUrl, params, callBack);
				}
			};
		},
		/**
		 * 提示框 (删除 修改 更新 等 操作)
		 * title : 提示框标题
		 * message :　提示框内容
		 * cfg : 更新的设置 (如提示框宽度等)
		 */
		alert : function(title, message, cfg) {
			var obj = {
				title:title,    
			    msg:"</br>" + message,    
			    width:230,
			    height : 160,
			    closable:false,    
			    buttons:Ext.MessageBox.OK,    
			    icon:Ext.MessageBox.WARNING
			}
			if (cfg) {
				obj = Ext.apply(obj, cfg);
			}
			Ext.MessageBox.show(obj);	
		},
		/**
		 * 自动消失提示框 (删除 保存 等 操作)
		 * title : 提示框标题
		 * message :　提示框内容
		 * cfg : 自定义提示框属性
		 */
		toast : function(title, message,cfg) {
			var obj = {
					html : message,
					 title:title,
					closable:false,
					align:'br',
					slideInDuration:300,
					minWidth:250,
					minHeight:100,
					iconCls:'fa fa-info-circle',
					autoCloseDelay:1000
			};
			var newCfg = Ext.apply(obj, cfg);
			var html = '<table style="width:100%;htight:100%;"><tr align="center"><td><p>' +message + '</p></td></tr></table>';
			newCfg.html = html;
			Ext.toast(newCfg);
		},
		/**
		 * 判断是否有权限
		 * @param resCode
		 * @returns
		 */
		hasNotAuthor : function(resCode){
			var msg=false;
			Ext.Ajax.request({
				method: 'post',
				url: 'main/system/hasAuthor.mvc',
				params: {resCode:resCode},
				async : false,
				success: function(response, options){
					var text=response.responseText;
					if("true"==text){
						msg = true;
					}else{
						msg = false;
					}
				}
			});
			return !msg;
		},
		/**
		 * 根据字典名称，字典值以及字典集合获取字典值对应的字典显示值
		 * dictMap为字典值对象集合,例如:[{1:是},{0:否}]
		 */
		getDictValue:function(dictName,dictKey,dictValues){
			var dictDisplay=null;
			if(dictValues && Ext.isArray(dictValues)){
				if(dictValues.length!=0){
					Ext.each(dictValues,function(item,index){
						for(var p in item){
							if(p==dictKey){
								dictDisplay=item[dictKey];
								return false;
							}
						}
					});
				}
			}
			return dictDisplay;
		},
		/**
		 * 根据dictName获取dictValue结合，返回类型为数组，数组中的元素为键值对对象，其中key为dictKey，value为dictValue
		 * @param dictName 字典名称
		 * @returns
		 */
		getDicts:function(dictName){
			var dicts=[];
			Ext.Ajax.request({
				method: 'post',
				url: 'main/system/getDict.mvc',
				params: {parentKey:dictName},
				async : false,
				success: function(response, options){
					var gridJsonStr=response.responseText;
					var models=Ext.decode(gridJsonStr).rows;
					Ext.each(models,function(item){
						var dictKey=item['dictKey'];
						var dictValue=item['dictValue'];
						var dict={};
						dict[dictKey]=dictValue;
						dicts.push(dict);
					});
				}
			});
			return dicts;
		},
		displayInnerGrid : function(childModuleId, parentRecord, parentGrid, rowIndex, innerStoreFields, innerGridColums, innerDivId, storeParams) {
			var renderId = parentRecord.get(innerDivId);
		  
		  	var childStore=new Ext.data.Store({
				autoLoad:false,
		      	proxy:{
		          	type:"ajax",
		          	url:childModuleId,
		          	actionMethods:{
			            read: "POST"
					},
		          	reader : {
						type : 'json',
						rootProperty : "rows",
						totalProperty : "total"
					},
		          	listeners:{
		              	exception:function(proxy,response,operation,options){
		      				Wb.except(response.responseText);
		      			}
		            }
		       }
		       //,
		      //fields:innerStoreFields
		    });
		  	childStore.on('load', function(store,options){
			});
		  	childStore.on('beforeload', function(store,options){
		  		if (storeParams != undefined) {
		  			Ext.apply(store.proxy.extraParams, storeParams);
		  		} else {
		  			Ext.apply(store.proxy.extraParams, {
		  				"parentId":parentRecord.get('id')
		  			});
		  		}
			});
		  
		  	childStore.load();
		    var innerGrid = Ext.create('Ext.grid.Panel', {
		        store: childStore,
		        id:'innerGrid_'+renderId,
		      	columns:innerGridColums,
		        columnLines: false,
		        width: window.innerWidth+10,
		        forceFit:true,
		        viewConfig:{enableTextSelection:true},
		        renderTo: renderId
		    });
		    //嵌套表格加载完数据后再将对象存放到公共数组中，on可以用来绑定对象事件
		    innerGrid.store.on('load', function(store,options){
		    	//设置外部表格的高度 44为表头+滚动条，每行高度20
		        //parentGrid.doLayout();
		        parentGrid.updateLayout();
			});
		    innerGrid.getEl().swallowEvent([
		        'mousedown', 'mouseup', 'click',
		        'contextmenu', 'mouseover', 'mouseout',
		        'dblclick', 'mousemove','headerCtMouseMove'
		    ]);
		},
	
		//销毁一个内嵌gridpanel
		destroyInnerGrid : function(parentRecord, parentGrid, rowIndex,innerDivId) {
			var renderId = "innerGrid_" + parentRecord.get(innerDivId);
			var innerGrid = Ext.getCmp(renderId);
		  	//var innerCount =  Ext.getCmp(renderId).store.count();
			if(innerGrid){
				//innerGrid.removeAll(true);
		    	innerGrid.close();
		    	innerGrid = null;
			}
		    var parent = document.getElementById(parentRecord.get(innerDivId));
		    //删除关联对象数组中对应项目id的内嵌gridPanel
		    var child = parent.firstChild;
		    while (child) {
		        child.parentNode.removeChild(child);
		        child = child.nextSibling;
		    }
		  	//重置外部表格的高度 44为表头+滚动条，每行高度20
		  	//parentGrid.setHeight(parentGrid.getHeight() - 44 - innerCount * 20);
		  	parentGrid.setHeight(parentGrid.getHeight() - 44 - 2 * 20);
		  	//parentGrid.doLayout();
		  	parentGrid.updateLayout();
		},
		getErp:function(){
			var flag = false;
			Ext.Ajax.request({
				method: 'post',
				url: 'main/erpAction/getErpFlag.mvc',
				async : false,
				success: function(response, options){
					var o = Ext.decode(response.responseText);
					if (o.erpFlag) {
						flag = true;
					}
				}
			});
			return flag;
		},
		getErpRequiredField:function(pageId){
			var array = {};
			var flag = false;
			Ext.Ajax.request({
				method: 'post',
				url: 'main/erpAction/getErpRequiredField.mvc',
				params: {pageId:pageId},
				async : false,
				success: function(response, options){
					array = Ext.decode(response.responseText);
				}
			});
			return array;
		}
	};
}();