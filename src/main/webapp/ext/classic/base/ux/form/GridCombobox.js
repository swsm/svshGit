/**
 * 下拉GridPanel的Combobox控件。
 * eg：
 * var combo = Ext.create('app.basic.factory.component.GridCombobox', {
			fieldLabel : '名称',
			labelSeparator: '：',
			width: 300, 
		    emptyText: '请选择...',
		    displayField: 'name',
		    valueField: 'id',
		    multiSelect: true,//设置是否需要多选
			store: store,  //需要绑定一个store
		    queryParam: 'name',//用于查询参数名
		    columns: cols,//grid中需要展示的列，eg:[{text: 'ID' ,dataIndex: 'id'}, {text: '名称' ,dataIndex: 'name'}]
			gridWidth:360, //设置下拉出grid的宽
			gridHeight:520, //设置下拉出grid的高
		    pageFlag: false //是否需要分页，true:需要分页；false:不需要分页
		});
 * 
 * eg:在grid中使用
 * MesUtil.createGridColumn({
			header : '所属厂区',
			dataIndex : 'factoryName',
			width : 150,
			editor : Ext.create('Ext.ux.form.GridCombobox', {
				gridWidth:260, //设置下拉出grid的宽
				gridHeight:400, //设置下拉出grid的高
				store:factoryStore,  //相应的store
			    queryParam:'factoryCode',//用于查询参数名
			    columns: cols,//grid中需要展示的列，eg:[{text: 'ID' ,dataIndex: 'id'}, {text: '名称' ,dataIndex: 'name'}]
			    pageFlag: false,//是否需要分页，true:需要分页；false:不需要分页
			    displayField: 'factoryName',
			    valueField: 'id',
				autoHeight : true,
				listeners:{
					select:function(combo, record, index){
						var row = grid.getSelectionModel().getSelection()[0];
						row.set('factoryId',record.id);
					},
					expand:function(){
						factoryStore.load();
					}
				}
			})
		}),
 * 
 */
Ext.define('Ext.ux.form.GridCombobox', {
	extend : 'Ext.form.field.ComboBox',  

    border: false,
    multiSelect : false,
    hideTrigger : true, 
    createPicker : function(){  
        var me = this;  
          
        var picker = Ext.create('Ext.grid.Panel', { 
            store: me.store,  
            frame : true,  
            border: false,
            resizable : false, 
            columns : me.columns,  
            selModel: {  
                mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'  
            },  
            floating: true,  
            hidden: true,  
            width : me.gridWidth,  
            height: me.gridHeight,
            columnLines : true,  
            focusOnToFront: false,
            bbar : me.pageFlag ?  Ext.create('app.utils.PagingToolbar', {store: me.store, displayInfo: true}) : null
        });  
        
        me.mon(picker, 'itemclick', me.onItemClick, me);  
        me.mon(picker, 'refresh', me.onListRefresh, me);  
        
        me.mon(picker.getSelectionModel(), 'beforeselect', me.onBeforeSelect, me);  
        me.mon(picker.getSelectionModel(), 'beforedeselect', me.onBeforeDeselect, me);  
        me.mon(picker.getSelectionModel(), 'selectionchange', me.onListSelectionChange, me); 
        
        this.picker = picker;  
        return picker;  
    }, 
    
    onItemClick: function(picker, record){  
        /* 
         * If we're doing single selection, the selection change events won't fire when 
         * clicking on the selected element. Detect it here. 
         */  
        var me = this,  
            selection = me.picker.getSelectionModel().getSelection(),  
            valueField = me.valueField;  
  
        if (!me.multiSelect && selection.length) {  
            if (record.get(valueField) === selection[0].get(valueField)) {  
                // Make sure we also update the display value if it's only partial  
                me.displayTplData = [record.data];  
                me.setRawValue(me.getDisplayValue());  
                me.collapse();  
            }  
        }  
    },  
      
    matchFieldWidth : false,  
      
    onListSelectionChange: function(list, selectedRecords) {  
        var me = this,  
        isMulti = me.multiSelect,  
        hasRecords = selectedRecords.length > 0;  
	    // Only react to selection if it is not called from setValue, and if our list is  
	    // expanded (ignores changes to the selection model triggered elsewhere)  
	    if (!me.ignoreSelection && me.isExpanded) {  
	        if (!isMulti) {  
	            Ext.defer(me.collapse, 1, me);  
	        }  
	        /* 
	         * Only set the value here if we're in multi selection mode or we have 
	         * a selection. Otherwise setValue will be called with an empty value 
	         * which will cause the change event to fire twice. 
	         */  
	        if (isMulti || hasRecords) {  
	            me.setValue(selectedRecords, false);  
	        }  
	        if (hasRecords) {  
	            me.fireEvent('select', me, selectedRecords);  
	        }  
	        me.inputEl.focus();  
	    }  
    }, 
      
    doAutoSelect: function() {  
        var me = this,  
            picker = me.picker,  
            lastSelected, itemNode;  
        if (picker && me.autoSelect && me.store.getCount() > 0) {  
            // Highlight the last selected item and scroll it into view  
            lastSelected = picker.getSelectionModel().lastSelected;  
            itemNode = picker.view.getNode(lastSelected || 0);  
            if (itemNode) {  
                picker.view.highlightItem(itemNode);  
                picker.view.el.scrollChildIntoView(itemNode, false);  
            }  
        }  
    },
    

    initEvents : function() {
		var me = this;
		me.callParent();
		// setup keyboard handling
		me.mon(me.inputEl, 'keyup', me.onKeyUp, me);
	},
	
	onKeyUp : function(e, t) {
		var me = this, key = e.getKey();
		if (!me.readOnly && !me.disabled && me.editable) {
			me.lastKey = key; 
			me.doQueryTask.cancel();
			console.log(key);
			if (key == '40') {
				var me = this;
				var length = me.getPicker().getStore().data.length;
				if(length == 0){
					return;
				}
				var postion = me.getPicker().getSelectionModel().getCurrentPosition();
					
				if (!postion || postion.rowIdx+1 == length) {
					me.getPicker().getSelectionModel().select(0);
					return;
				}
				me.getPicker().getSelectionModel().selectNext();
			} else if (key == '38'){
				var me = this;
				var length = me.getPicker().getStore().data.length;
				if(length == 0){
					return;
				}
				var postion = me.getPicker().getSelectionModel().getCurrentPosition();
					
				if (!postion || postion.rowIdx == 0) {
					me.getPicker().getSelectionModel().select(length-1);
					return;
				}
				me.getPicker().getSelectionModel().selectPrevious();
			} else if (key == e.ENTER) {
				if (me.isExpanded && me.getPicker().getSelectionModel().getCurrentPosition()) {
					 me.getPicker().fireEvent('itemclick',me.getPicker(),me.getPicker().getSelectionModel().getCurrentPosition().record);
					return;
				}
			}
			if(me.getRawValue() == '' ){
				return;
			} else {
				this.doQuery(this.getRawValue(), true);
			}
			
		}
	},
  });
