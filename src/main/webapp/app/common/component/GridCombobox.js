Ext.define('app.basic.factory.component.GridCombobox', {

    extend: 'Ext.form.field.ComboBox',

    multiSelect: true,
    border: false,

    createPicker: function () {
        var me = this;

        var picker = Ext.create('Ext.grid.Panel', {
            store: me.store,
            resizable: false,
            frame: true,
            columns: me.columns,
            selModel: {
                mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'
            },
            floating: true,
            hidden: true,
            width: me.gridWidth,
            height: me.gridHeight,
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

    onItemClick: function (picker, record) {
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

    matchFieldWidth: false,

    onListSelectionChange: function (list, selectedRecords) {
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
        console.log(me.getValue());
    },

    doAutoSelect: function () {
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
    }

});