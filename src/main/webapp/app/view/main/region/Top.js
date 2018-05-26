Ext.define('app.view.main.region.Top', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.top',
    border: '0 0 1 0',
    style: "background:url(resources/style/default/images/linkmes/head_bg.png) 70% center no-repeat rgb(3,119,116);",
    initComponent: function () {
        this.items = [{
            xtype: 'image',
            width: 175,
            height: 48,
            margin: '0 0 0 15',
            bind: {
                src: '{logoUrl}'
            }
        },

            {
                xtype: 'label',
                bind: {
                    text: '{vendorName}'
                },
                style: 'width:240px;height:25px; line-height:25px;float:left; border-left:1px #999 solid; font-family: "Microsoft YaHei"; font:"Arial Black", Gadget, sans-serif; padding-left:15px; color:#FFF; margin-top:12px; font-size:20px;'
            }, '->', '->',
//				{
//					xtype : 'label',
//					iconCls : 'fa fa-lock',
//					text : organNames,
//					style : 'height:25px; line-height:25px; float:right; font:"Arial Black", Gadget, sans-serif; padding-left:15px; color:#FFF; margin-top:12px; font-size:15px;cursor:pointer;'
//				},{
//					xtype : 'label',
//					text : '|',
//					id : 'divder1',
//					width : 5,
//					style : 'height:25px; line-height:20px; float:left; color:#fff;padding-left:5px;margin-top:12px;cursor:pointer;'
//				},
            {
                xtype: 'button',
                //iconCls : 'fa fa-user',
                text: currentLoginName,
                icon: 'resources/style/default/images/linkmes/user_gray.png',
                id: 'userName',
                //	focusCls : '',
                //cls : 'icon-xiugaimima',
                style: 'cursor: default;height:25px; line-height:25px;background:none;border:none;border-height:5px;float:left; padding-right:5px; color:#FFF;font-size:15px;',
                listeners: {
                    render: function () {
                        document.getElementById('userName-btnInnerEl').style.color = "#e9e9e9";
                        document.getElementById('userName-btnInnerEl').style.height = "22px";
                        document.getElementById('userName').style.margin = "10 0 0 0";
                        document.getElementById('userName-btnInnerEl').style.fontSize = "15px";
                        document.getElementById('userName-btnInnerEl').style.fontFamily = "Microsoft YaHei";
                        document.getElementById('userName-btnInnerEl').style.font = "Arial Black";
                        document.getElementById('userName-btnIconEl').style.color = "#e9e9e9";
                        document.getElementById('userName-btnIconEl').style.fontSize = "15px";
                        document.getElementById('userName-btnIconEl').style.height = "22px";
                    }
                }
            }, {
                xtype: 'label',
                text: '|',
                id: 'divder2',
                width: 5,
                style: 'height:25px; line-height:20px; float:left; color:#fff;padding-left:3px;margin-top:12px;cursor:pointer;'
            }, {
                text: '修改密码',
                xtype: 'button',
                //iconCls : 'fa fa-lock',
                icon: 'resources/style/default/images/linkmes/password_gray.png',
                id: 'updatePW',
                //focusCls : 'this.blur()',
                style: 'height:25px;line-height:25px;float:left; background:none;border:none;border-height:5px; color:#fff;font-size:15px;cursor:pointer;',
                listeners: {
                    render: function () {
                        document.getElementById('updatePW-btnInnerEl').style.color = "#e9e9e9";
                        document.getElementById('updatePW-btnInnerEl').style.height = "22px";
                        document.getElementById('updatePW-btnInnerEl').style.fontSize = "15px";
                        document.getElementById('updatePW-btnInnerEl').style.fontFamily = "Microsoft YaHei";
                        document.getElementById('updatePW-btnInnerEl').style.font = "Arial Black";
                        document.getElementById('updatePW-btnIconEl').style.color = "#e9e9e9";
                        document.getElementById('updatePW-btnIconEl').style.fontSize = "15px";
                        document.getElementById('updatePW-btnIconEl').style.height = "22px";
                    },
                    mouseover: function (th) {
                        th.setIcon('resources/style/default/images/linkmes/password_white.png');
                        document.getElementById('updatePW-btnInnerEl').style.color = "white";
                    }, mouseout: function (th) {
                        th.setIcon('resources/style/default/images/linkmes/password_gray.png');
                        document.getElementById('updatePW-btnInnerEl').style.color = "#e9e9e9";
                    }
                },
                cls: 'icon-xiugaimima',
                handler: function () {
                    if (Ext.getCmp('id_top_updatePasswordWin') == undefined) {
                        // 不存在
                        updatePasswordWin = Ext.create("Ext.window.Window", {
                            id: 'id_top_updatePasswordWin',
                            title: "修改密码",
                            modal: true,
                            width: 370,
                            height: 250,
                            layout: 'fit',
                            constrain: true,
                            resizable: true,
                            closeAction: 'hide',
                            closeToolText: '关闭',
                            border: false,
                            listeners: {
                                hide: function (win, options) {
                                    win.hide();
                                }
                            },
                            items: [{
                                xtype: "container",
                                layout: {
                                    type: 'table',
                                    columns: 2,
                                    tableAttrs: {
                                        padding: '5px',
                                        style: "border:true"
                                    }
                                },
                                items: [{
                                    xtype: "textfield",
                                    fieldLabel: "<span style='color:red'>*</span>原始密码",
                                    id: "id_oldPassword",
                                    labelAlign: 'right',
                                    enforceMaxLength: true,
                                    maxLength: 25,
                                    inputType: "password",
                                    labelSeparator: '：',
                                    margin: '15 0 10 0',
                                    colspan: 2,
                                    width: '92.5%'
                                }, {
                                    xtype: "textfield",
                                    fieldLabel: "<span style='color:red'>*</span>新密码",
                                    id: "id_newPassword",
                                    labelAlign: 'right',
                                    enforceMaxLength: true,
                                    maxLength: 25,
                                    inputType: "password",
                                    labelSeparator: '：',
                                    colspan: 2,
                                    width: '92.5%'
                                }, {
                                    xtype: "textfield",
                                    fieldLabel: "<span style='color:red'>*</span>确认密码",
                                    id: "id_confirmNewPassword",
                                    labelAlign: 'right',
                                    enforceMaxLength: true,
                                    maxLength: 25,
                                    inputType: "password",
                                    labelSeparator: '：',
                                    colspan: 2,
                                    width: '92.5%'
                                }]
                            }],
                            bbar: [{
                                xtype: "tbfill"
                            }, {
                                xtype: "button",
                                iconCls: 'fa fa-check',
                                text: '确认',
                                handler: function () {
                                    console.info(123);
                                    var oldPassword = Ext.getCmp('id_oldPassword').getValue();
                                    var newPassword = Ext.getCmp('id_newPassword').getValue();
                                    var confirmPassword = Ext.getCmp('id_confirmNewPassword').getValue();
                                    var flag = true;
                                    if (Ext.isEmpty(oldPassword)) {
                                        Tool.alert(Tool.str.msgConfirmTitle, '请输入“原始密码”！');
                                        flag = false;
                                        return;
                                    }
                                    if (Ext.isEmpty(newPassword)) {
                                        Tool.alert(Tool.str.msgConfirmTitle, '请输入“新密码”！');
                                        flag = false;
                                        return;
                                    }
                                    if (Ext.isEmpty(confirmPassword)) {
                                        Tool.alert(Tool.str.msgConfirmTitle, '请输入“确认密码”！');
                                        flag = false;
                                        return;
                                    }
                                    if (confirmPassword != newPassword) {
                                        Tool.alert(Tool.str.msgConfirmTitle, '新密码与确认密码不一致！');
                                        flag = false;
                                        return;
                                    }
                                    //修改密码与原密码不能一样
                                    if (oldPassword == newPassword) {
                                        Tool.alert(Tool.str.msgConfirmTitle, '新密码不能和原始密码相同！', {width: 260});
                                        flag = false;
                                        return;
                                    }
                                    if (flag) {
                                        Ext.Ajax.request({
                                            url: 'main/system/updatePassword.mvc',
                                            method: 'post',
                                            params: {
                                                oldPassword: Ext
                                                    .getCmp('id_oldPassword')
                                                    .getValue(),
                                                newPassword: Ext
                                                    .getCmp('id_newPassword')
                                                    .getValue()
                                            },
                                            success: function (response,
                                                               options) {
                                                var o = Ext.util.JSON
                                                    .decode(response.responseText);
                                                if (o.message == 'success') {
                                                    Tool.toast(Tool.str.msgConfirmTitle, '密码修改成功！');
                                                    Ext.Ajax.request({
                                                        url: 'main/loginOut.mvc',
                                                        method: 'post',
                                                        params: {},
                                                        success: function (response, options) {
                                                            window.parent.location.href = "forwardPage.mvc?page=login";
                                                        }
                                                    });
                                                } else if (o.message == 'fail') {
                                                    Tool.alert(Tool.str.msgConfirmTitle, '原密码输入错误！');
                                                }
                                                return
                                            },
                                            failure: function () {
                                                Tool.alert(Tool.str.msgConfirmTitle, '修改密码失败！');
                                            }
                                        });
                                    }
                                },
                                listeners: {
                                    afterrender: function () {
                                        updatePasswordWin.getKeyMap().on(13, function (win, options) {
                                            win.hide();
                                        });
                                    }
                                }

                            }, {
                                xtype: "button",
                                iconCls: 'fa fa-close',
                                text: '取消',
                                style: 'background-color:none !important;',
                                handler: function () {
                                    updatePasswordWin.hide();
                                }
                            }]
                        });
                        //var userView = this.getView();
                        setTimeout(function () {
                            //userView.add(updatePasswordWin);
                            updatePasswordWin.show();
                        }, 30);
                    }
                    setTimeout(function () {
                        Ext.getCmp('id_oldPassword').setValue(null);
                        Ext.getCmp('id_newPassword').setValue(null);
                        Ext.getCmp('id_confirmNewPassword').setValue(null);
                        updatePasswordWin.show();
                    }, 30);
                }

            }, {
                xtype: 'label',
                text: '|',
                id: 'divder3',
                width: 5,
                style: 'height:25px; line-height:20px; float:left; color:#fff;padding-left:3px;margin-top:12px;cursor:pointer;'
            }, {
                text: '',
                xtype: 'button',
                id: 'help',
                icon: 'resources/style/default/images/linkmes/help_gray.png',
                style: 'background:none !important;border:none !important;',
                handler: function () {
                    var center = viewport.down('center');
                    var t = center.getActiveTab();
                    if (Ext.isEmpty(t)) {
                        Tool.alert(Tool.str.msgConfirmTitle, '当前页面没有上传帮助文档！');
                        return;
                    }
                    var loadMarsk = new Ext.LoadMask(center, {
                        msg: "操作中.."
                    });
                    loadMarsk.show();
                    Ext.Ajax.request({
                        url: 'main/helpDocumentAction/getHelpDocumentByPageCode.mvc',
                        method: 'post',
                        params: {
                            pageCode: t.resCode
                        },
                        success: function (response, options) {
                            loadMarsk.hide();
                            var o = Ext.decode(response.responseText);
                            if (o.message == 'success') {
                                var swfFileName = o.swfFileName;
                                // 操作成功
                                var seeWin = Ext.create("Ext.window.Window", {
                                    title: "帮助文档", modal: true, width: 900, height: 860, layout: 'fit', constrain: true,
                                    resizable: true, closeToolText: '关闭', closeAction: 'hide', border: false,
                                    html: '<iframe src="app/flexpaper/readFile.jsp" width="100%" height="100%"></iframe>',
                                    listeners: {
                                        close: function (win, options) {
                                            loadMarsk.show();
                                            Ext.Ajax.request({
                                                url: 'main/helpDocumentAction/deleteSwfFile.mvc',
                                                method: 'post',
                                                params: {
                                                    swfFileName: swfFileName
                                                },
                                                success: function (response, options) {
                                                    loadMarsk.hide();
                                                    seeWin.destroy();
                                                }
                                            });
                                        }
                                    }
                                });
                                seeWin.show();
                            } else if (o.message == 'failed') {
                                Tool.alert(Tool.str.msgConfirmTitle, "当前页面没有上传帮助文档！");
                            }
                        }
                    });
                },
                listeners: {
                    render: function () {
                        document.getElementById('help-btnWrap').setAttribute("title", "帮助");
                        document.getElementById('help-btnIconEl').style.fontSize = '20px';
                        document.getElementById('help-btnIconEl').style.color = '#fff';
                    },
                    mouseover: function (th) {
                        th.setIcon('resources/style/default/images/linkmes/help_white.png');
                    },
                    mouseout: function (th) {
                        th.setIcon('resources/style/default/images/linkmes/help_gray.png');
                    }
                }
            }, {
                xtype: 'label',
                text: '|',
                id: 'divder4',
                width: 5,
                style: 'height:25px; line-height:20px; float:left; color:#fff;padding-left:3px;margin-top:12px;cursor:pointer;'
            }, {
                text: '',
                xtype: 'button',
                id: 'cancel',
                icon: 'resources/style/default/images/linkmes/exit_gray.png',
                style: 'background:none !important;border:none !important;',
                handler: function () {
                    Ext.Msg.confirm(Tool.str.msgConfirmTitle, "</br>是否注销该用户？", function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                url: 'main/loginOut.mvc',
                                async: true,
                                success: function (response) {
                                    window.parent.location.href = "forwardPage.mvc?page=login";
                                }
                            });
                        }
                    })

                },

                listeners: {
                    render: function () {
                        document.getElementById('cancel-btnWrap').setAttribute("title", "注销");
                        document.getElementById('cancel-btnIconEl').style.fontSize = '20px';
                        document.getElementById('cancel-btnIconEl').style.color = '#fff';
                    },
                    mouseover: function (th) {
                        th.setIcon('resources/style/default/images/linkmes/exit_white.png');
                    },
                    mouseout: function (th) {
                        th.setIcon('resources/style/default/images/linkmes/exit_gray.png');
                    }
                }
            }];
        this.callParent(arguments);
    }
});