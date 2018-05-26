Ext.define('app.utils.PagingToolbar', {
    extend: 'Ext.PagingToolbar',

    initComponent: function () {
        var combo = Ext.create('Ext.form.ComboBox', {
            store: new Ext.data.ArrayStore({
                fields: ['text', 'value'],
                data: [['15', 15], ['50', 50], ['100', 100], ['200', 200]]
            }),
            valueField: 'value',
            displayField: 'text',
            editable: false,
            emptyText: 15,
            width: 77,

            listeners: {
                'select': function (comboBox) {
                    var pagingToolbar = comboBox.up('pagingtoolbar');
                    pagingToolbar.pagesize = parseInt(comboBox.getValue());
                    itemsPerPage = parseInt(comboBox.getValue());
                    var grid = pagingToolbar.up('grid');
                    grid.getStore().pageSize = itemsPerPage;
                    grid.getStore().loadPage(1, {
                        params:
                            {
                                start: 0,
                                limit: itemsPerPage
                            }
                    });
                }
            }
        });
        this.items = [combo];
        this.callParent();
    }

});