Ext.define('app.system.view.UserMng', {
    requires: ['app.system.controller.UserController', 'app.system.viewmodel.UserListModel', 'Ext.ux.TreePicker', 'app.utils.statics.MesUtil'],

    init: function () {
        var Util = app.utils.statics.MesUtil;
        var ENABLED_DICT_NAME = "BOOL_FLAG";
        var enabledDictValues = Tool.getDicts(ENABLED_DICT_NAME);
        var queryFormDefaultBtns = Util.createQueryFormDefaultBtn();
        var userNameText = {
            xtype: 'textfield',
            maxWidth: 300,
            fieldLabel: '用户名',
            labelSeparator: '：',
            name: 'username',
            id: 'username'
        };
        var trueNameText = {
            xtype: 'textfield',
            maxWidth: 300,
            fieldLabel: '姓名',
            labelSeparator: '：',
            name: 'truename',
            id: 'truename'
        };
        var organTree = {
            xtype: 'textfield',
            maxWidth: 300,
            fieldLabel: '所属机构',
            labelSeparator: '：',
            name: 'organName',
            id: 'id_organTree'
        };
        var workNoText = {
            xtype: 'textfield',
            maxWidth: 300,
            fieldLabel: '工号',
            labelSeparator: '：',
            name: 'workNo',
            style: {paddingTop: '5px'},
            id: 'id_UserMng_workNo'
        };
        var roleNameText = {
            xtype: 'textfield',
            maxWidth: 300,
            fieldLabel: '角色',
            labelSeparator: '：',
            name: 'roleName',
            style: {paddingTop: '5px'},
            id: 'id_UserMng_roleName'
        };
        var userForm = Util.createQueryForm([userNameText, trueNameText, organTree, workNoText, roleNameText, {
            columnWidth: .25,
            align: 'right',
            layout: 'column',
            style: {paddingTop: '5px'},
            items: queryFormDefaultBtns,
            listeners: {
                afterrender: function () {
                    userForm.getKeyMap().on(13, function () {
                        userGrid.getStore().loadPage(1);
                    });
                }
            }
        }]);
        var store = Ext.create('Ext.data.Store', {
            requires: ['app.system.model.UserRecord'],
            pageSize: Tool.page.limit,
            listeners: {
                'beforeload': function () {
                    Ext.apply(this.proxy.extraParams, {
                        username: Ext.getCmp('username').getValue(),
                        truename: Ext.getCmp('truename').getValue(),
                        organName: Ext.getCmp('id_organTree').getValue(),
                        workNo: Ext.getCmp('id_UserMng_workNo').getValue(),
                        roleName: Ext.getCmp('id_UserMng_roleName').getValue()
                    });
                }
            },
            proxy: {
                type: 'ajax',
                url: 'main/system/getUser.mvc',
                actionMethods: {
                    read: "POST"
                },
                reader: {
                    type: 'json',
                    rootProperty: "rows",
                    totalProperty: "total"
                }
            }
        });
        var userGrid = Ext.create('Ext.grid.GridPanel', {
            plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
                clicksToEdit: 1
            })],
            id: 'id_userList',
            reference: 'userList',
            store: store,
            bbar: Ext.create('app.utils.PagingToolbar', {
                store: store,
                displayInfo: true
            }),
            viewConfig: {
                enableTextSelection: true,
                selModel: Ext.create(
                    'Ext.selection.CheckboxModel', {})
            },
            bind: {
                title: '{title}'
            },
            listeners: {
                'render': 'doRender'
            },
            border: true,
            columns: [
                {
                    header: '序号',
                    width: 60,
                    xtype: "rownumberer",
                    align: 'center',
                    sortable: false
                },
                Util.createGridColumn({header: '工号', dataIndex: 'workNo', width: 100}),
                Util.createGridColumn({header: '姓名', dataIndex: 'truename', width: 100}),
                Util.createGridColumn({header: '用户名', dataIndex: 'username', width: 100}),
                Util.createGridColumn({header: '所属机构', width: 120, dataIndex: 'organName'}),
                Util.createGridColumn({header: '角色', width: 130, dataIndex: 'roleNameStr'}),
                Util.createGridColumn({
                    header: '是否启用', width: 80, dataIndex: 'enabled', renderer: function (value, record, store) {
                        return Util.renderDictColumn(ENABLED_DICT_NAME, value, enabledDictValues);
                    }
                }),
                Util.createGridColumn({header: '手机', dataIndex: 'mobile', width: 120}),
                Util.createGridColumn({header: '联系电话', width: 120, dataIndex: 'telephone'}),
                Util.createGridColumn({header: '联系地址', width: 110, dataIndex: 'address'}),
                Util.createGridColumn({header: '微信名', width: 120, dataIndex: 'wechatName'}),
                Util.createGridColumn({header: '邮箱地址', width: 150, dataIndex: 'email'}),
                Util.createGridColumn({header: '创建日期', width: 150, dataIndex: 'createDate'}),
                Util.createGridColumn({header: '备注', width: 150, dataIndex: 'remark'})
            ],
            tbar: [{
                bind: {
                    text: '{insert}'
                },
                xtype: 'button',
                hidden: Tool.hasNotAuthor("app.system.view.UserMng.insert"),
                iconCls: 'fa fa-plus-circle',
                handler: 'doInsertRecord'
            }, {
                bind: {
                    text: '{update}'
                },
                xtype: 'button',
                hidden: Tool.hasNotAuthor("app.system.view.UserMng.update"),
                iconCls: 'fa fa-edit',
                handler: 'doUpdateRecord'
            }, {
                bind: {
                    text: '{delete}'
                },
                xtype: 'button',
                hidden: Tool.hasNotAuthor("app.system.view.UserMng.delete"),
                iconCls: 'fa fa-minus-circle',
                handler: 'doDelete'
            }, {
                bind: {
                    text: '{disable}'
                },
                xtype: 'button',
                iconCls: 'fa fa-times-circle',
                hidden: Tool.hasNotAuthor("app.system.view.UserMng.disable"),
                handler: 'doDisable'
            }, {
                bind: {
                    text: '{enable}'
                },
                xtype: 'button',
                iconCls: 'fa fa-check-circle',
                hidden: Tool.hasNotAuthor("app.system.view.UserMng.enable"),
                handler: 'doEnable'
            }, {
                bind: {
                    text: '导出'
                },
                xtype: 'button',
                iconCls: 'fa fa-file-excel-o',
                hidden: Tool.hasNotAuthor("app.system.view.UserMng.export"),
                handler: 'doExport'
            }, {
                bind: {
                    text: '{reflush}'
                },
                xtype: 'button',
                iconCls: 'fa fa-refresh',
                handler: 'doReflush'
            }]
        });
        return {
            createView: function (obj) {
                var userController = Ext.create('app.system.controller.UserController');
                var userViewModel = Ext.create("app.system.viewmodel.UserListModel");
                var user = Ext.create('Ext.Panel', {
                    padding: '5',
                    layout: {
                        type: 'border'
                    },
                    bodyStyle: 'border-width : 0 0 0 0 !important;background:#FFFFFF',
                    controller: userController,
                    viewModel: userViewModel,
                    items: [{
                        region: 'north',
                        height: 128,
                        layout: 'fit',
                        padding: '0 0 5 0',
                        xtype: 'container',
                        items: [userForm]
                    }, {
                        region: 'center',
                        layout: 'fit',
                        xtype: 'container',
                        items: [userGrid]
                    }]
                });
                Ext.apply(user, obj);
                return user;
            },
            loadData: function () {
                store.load({params: {start: Tool.page.start, limit: Tool.page.limit}});
            }
        };
    }
});