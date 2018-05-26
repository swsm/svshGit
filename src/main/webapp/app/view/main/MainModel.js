/**
 * This class is the view model for the Main view of the application.
 */
Ext.define('app.view.main.MainModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.main',
    data: {
        'insert': '新增',
        'update': '修改',
        'delete': '删除',
        'enable': '启用',
        'disable': '禁用',
        'reflush': '刷新',
        'query-title': '查询条件'
    },
    constructor: function () {
        var me = this;
        this.callParent(arguments);
        var product = Ext.decode(productJson, true);
        Ext.apply(me.data, product);
    }
});
