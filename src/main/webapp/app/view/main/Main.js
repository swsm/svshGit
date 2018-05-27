Ext.define('app.view.main.Main', {
    extend: 'Ext.container.Viewport',
    xtype: 'app-main',

    requires: ['app.view.main.region.Top',
        'app.view.main.region.Left',
        'app.view.main.MainController',
        'app.view.main.MainModel',
        'app.view.main.region.Center'],
    controller: 'main',
    viewModel: 'main',
    id: 'appviewport',
    border: false,
    ui: 'navigation',
    layout: {
        type: 'border'
    },
    showOrHiddenToolbar: null,
    initComponent: function () {
        viewport = this;
        this.callParent(arguments); // 调用父类的初始化方法
    },
    createActiveTab: function (resCode, title, params, flag) {
        //resCode 資源code  title 標題  params 參數集合 flag 是否需要刷新頁面
        var center = viewport.down('center');
        var isOpen = false;
        center.items.each(function (item) {
            if (item.resCode == resCode) {
                if (flag) {
                    center.remove(item);
                } else {
                    center.setActiveTab(item);
                    isOpen = true;
                }
                return;
            }
        });
        if (!isOpen) {
            var obj = Ext.create(resCode).init();
            var panel = obj.createView({
                border: false,
                closable: true,
                title: title,
                resCode: resCode,
                params: params
            });
            var p = center.add(panel);
            center.setActiveTab(p);
        }
    },
    createIqcActiveTab: function (resCode, sheetId, inspectionStatus, sheetNo, inspectionResult, flag, scrope) {
        //resCode 資源code  title 標題  params 參數集合 flag 是否需要刷新頁面
        var center = viewport.down('center');
        center.items.each(function (item) {
            if (item.resCode == resCode) {
                center.remove(item);
            }
        });
        var obj = Ext.create(resCode).init(sheetId, inspectionStatus, scrope);
        var panel = obj.createView({
            border: false,
            closable: true,
            title: 'IQC检验-' + sheetNo,
            sheetId: sheetId,
            resCode: resCode,
            sheetNo: sheetNo,
            inspectionStatus: inspectionStatus,
            inspectionResult: inspectionResult,
            flag: flag,
        });
        var p = center.add(panel);
        center.setActiveTab(p);
    },
    items: [{
        xtype: 'top',
        reference: 'top',
        height: 60,
        border: true,
        region: 'north'
    }, {
        xtype: 'left',
        width: 240,
        border: true,
        reference: 'left',
        region: 'west'
    }, {
        xtype: 'center',
        border: true,
        reference: 'center',
        maximizable: true,
        region: 'center'
    }]

});
