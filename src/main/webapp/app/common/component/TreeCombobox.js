
Ext.define('app.basic.factory.component.TreeCombobox',{
	 
    extend : 'Ext.form.field.ComboBox',  
      
    multiSelect : true,  
    border: false,
      
    createPicker : function(){  
        var me = this;  
          
        var picker = Ext.create('Ext.tree.Panel', {  
            store: me.store,  
            rootVisible: false,  
			selModel: {  
				mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'  
			},  
            floating: true,  
            hidden: true,  
            width : me.treeWidth, 
            height: me.treeHeight,
            displayField: me.displayField,
            focusOnToFront: false  
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
    }  
      
});