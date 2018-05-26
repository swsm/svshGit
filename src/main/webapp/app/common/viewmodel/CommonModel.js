Ext.define('app.common.viewmodel.CommonModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.commonmodel',
    data: {},
    constructor: function () {
        this.callParent(arguments);
        Ext.apply(this.data, {
            'insert': '新增',
            'save': '保存',
            'update': '修改',
            'delete': '删除',
            'enable': '启用',
            'disable': '禁用',
            'reflush': '刷新',
            'query-title': '查询条件'
        });


    }
});
