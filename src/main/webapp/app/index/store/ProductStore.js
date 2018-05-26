Ext.define('app.index.store.ProductStore', {
    extend: 'Ext.data.Store',
    alias: 'store.product',
    fields: ['vendorName', 'logoUrl', 'sysName', 'sysVersion', 'sysMaking'],
    proxy: {
        type: 'ajax',
        url: 'main/getTopInfo.mvc',
        reader: {
            type: 'json',
            root: "topics",
            totalProperty: "totalCount"
        },
        extraParams: {
            form: ""
        },
        actionMethods: {
            read: 'POST'
        }
    }
});
