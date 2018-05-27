Ext.define('app.view.main.region.Left', {
    extend: 'app.view.main.MainContainerWrap',
    height: '100%',
    id: 'left',
    alias: 'widget.left',
    layout: 'border',
    reference: 'left',
    collapsible: true,
    border: false,
    title: '功能菜单',
    initComponent: function () {
        var treeStore = Ext.create('Ext.data.TreeStore', {
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'main/getResource.mvc',
                reader: {
                    type: 'json',
                    rootProperty: 'rows'
                }
            },
            fields: ['text', 'id', 'resUrl'],
            listeners: {
                'beforeexpand': function (node, eOpts) {
                    this.proxy.extraParams.id = node.raw.id;
                },
                'beforeload': function (s, o) {
                }
            }
        });
        this.items = [{
            xtype: 'panel',
            width: 250,
            region: 'north',
            title: '功能菜单',
            id: 'main-north-panel',
            border: true,
            items: [{
                text: '',
                xtype: 'button',
                id: 'main-navigation-btn',
                iconCls: 'x-fa fa-navicon',
                focusCls: 'this.blur()',
                style: 'background:none !important;border:none !important;',
                handler: 'show'
            }]
        }, {
            xtype: 'maincontainerwrap',
            id: 'main-view-detail-wrap',
            reference: 'mainContainerWrap',
            flex: 1,
            items: [{
                xtype: 'treelist',
                reference: 'navigationTreeList',
                itemId: 'navigationTreeList',
                store: treeStore,
                width: 250,
                region: 'center',
                containerScroll: true,
                frame: false,
                enableDD: true,
                rootVisible: false,
                useArrows: true,
                viewConfig: {
                    loadMask: false
                },
                autoScroll: true,
                expanderFirst: false,
                expanderOnly: false,
                listeners: {
                    selectionchange: 'onTreeNodeItemclick'
                }
            }]
        }]

        this.callParent();
    }
});