/**
 * 静态工具类
 */
Ext.define('app.utils.statics.MesUtil', {
    statics: {
        // instanceCount : 0,
        factory: function (config) {
            return new this(config);
        },
        /**
         * 配置表格数据列
         * @param columnConfig
         * @returns {___anonymous241_303}
         */
        createGridColumn: function (columnConfig) {
            var c = {
                defaultAlign: 'left',
                defaultSortable: true
            };
            c.renderer = function (value, metaData, record, rowIndex, colIndex,
                                   store, view) {
                if (!Ext.isEmpty(value)) {
                    value = Ext.util.Format.htmlEncode(value);
                    var qtipValue = Ext.util.Format.htmlEncode(value);
                    metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
                    return value;
                }
            };
            Ext.apply(c, columnConfig);
            return c;
        },
        /**
         * 配置编辑表格数据列
         * @param columnConfig
         * @returns {___c0}
         */
        createEditGridColumn: function (columnConfig) {
            var c = columnConfig;
            if (!c.renderer) {
                c.renderer = function (value, metaData, record, rowIndex, colIndex,
                                       store, view) {
                    if (!Ext.isEmpty(value)) {
                        value = Ext.util.Format.htmlEncode(value);
                        var qtipValue = Ext.util.Format.htmlEncode(value);
                        metaData.tdAttr = 'data-qtip="' + qtipValue + '"';
                        return value;
                    }
                };
            }
            if (!c.editor) {
                c.editor = new Ext.form.TextField({
                    maxLength: 100
                });
            }
            return c;
        },
        /**
         * 创建查询按钮以及重置按钮
         * @param handlerConfig
         * @returns {Array}
         */
        createQueryFormDefaultBtn: function (handlerConfig) {
            var queryHandlerName = 'doQuery';
            var resetHandlerName = 'doReset';
            var queryHandler;
            var resetHandler;
            if (handlerConfig) {
                if (handlerConfig.queryHandlerName) {
                    queryHandlerName = handlerConfig.queryHandlerName;
                } else if (Ext.isFunction(handlerConfig.queryHandler)) {
                    queryHandler = handlerConfig.queryHandler;
                }

                if (handlerConfig.resetHandlerName) {
                    resetHandlerName = handlerConfig.resetHandlerName;
                } else if (Ext.isFunction(handlerConfig.resetHandler)) {
                    resetHandler = handlerConfig.resetHandler;
                }
            }
            var btns = [{
                html: '<pre>   </pre>'
            }, {
                text: '查询',
                align: 'right',
                xtype: 'button',
                iconCls: 'fa fa-search',
                handler: queryHandler ? queryHandler : queryHandlerName
            }, {
                html: '<pre>   </pre>'
            }, {
                text: '重置',
                xtype: 'button',
                iconCls: 'fa fa-repeat',
                handler: resetHandler ? resetHandler : resetHandlerName
            }];
            return btns;
        },
        /**
         * 获取字典数据列中的字典显示值
         * @param dictName
         * @param dictValue
         * @param dictValues
         * @returns
         */
        renderDictColumn: function (dictName, dictValue, dictValues) {
            var dictDisplay = Tool.getDictValue(dictName, dictValue, dictValues);
            if (!Ext.isEmpty(dictDisplay)) {
                return dictDisplay;
            } else {
                return dictValue;
            }
        },
        /**
         * 根据查询条件ITEM生成查询条件框，
         * 调用该方法需要修改几个位置，
         * 第一：从第二行开始组件需要设置style:{paddingTop:'5px'}
         * 第二：在引用form的位置需要根据实际情况调整高度
         * @param queryFormItems 所有的查询表单中的组件
         * @param formConfig form表单的特别配置，例如reference属性
         * @returns
         */
        createQueryForm: function (queryFormItems, formConfig) {
            var items = [];
            for (var i = 0; i < queryFormItems.length; i++) {
                items.push(queryFormItems[i]);
            }
            ;
            var length = items.length;
            var queryLength = length - 1;
            var layer = parseInt(queryLength / 3);
            var mode = queryLength % 3;
            var column1Items = [];
            var column2Items = [];
            var column3Items = [];
            var column4Items = [];
            if (mode == 0) {
                if (layer > 0) {
                    for (var i = 0; i < layer; i++) {
                        column1Items.push(items[i * 3]);
                        column2Items.push(items[i * 3 + 1]);
                        column3Items.push(items[i * 3 + 2]);
                        if (i == layer - 1) {
                            column4Items.push(items[queryLength]);
                        } else {
                            column4Items.push({
                                xtype: 'label',
                                html: '<pre style = "margin:0 0 0 0;height:32px;">   </pre>'
                            });
                        }
                    }
                }
            } else {
                if (layer > 0) {
                    for (var i = 0; i < layer; i++) {
                        column1Items.push(items[i * 3]);
                        column2Items.push(items[i * 3 + 1]);
                        column3Items.push(items[i * 3 + 2]);
                    }
                }
                if (mode == 1) {
                    column1Items.push(items[layer * 3]);
                    column2Items.push(items[queryLength]);
                }
                else if (mode == 2) {
                    column1Items.push(items[layer * 3]);
                    column2Items.push(items[layer * 3 + 1]);
                    column3Items.push(items[queryLength]);
                }
            }
            var formItems = [];
            formItems.push({
                xtype: 'container',
                columnWidth: .25,
                layout: 'fit',
                items: column1Items
            });
            formItems.push({
                xtype: 'container',
                columnWidth: .25,
                layout: 'fit',
                items: column2Items
            });
            formItems.push({
                xtype: 'container',
                columnWidth: .25,
                layout: 'fit',
                items: column3Items
            });
            formItems.push({
                xtype: 'container',
                columnWidth: .25,
                layout: 'fit',
                items: column4Items
            });
            var defaultFormConfig = {
                border: true,
                layout: 'column',
                title: '查询条件',
                bodyStyle: {
                    padding: '5px'
                },
                fieldDefaults: {
                    labelAlign: 'right'
                },
                items: formItems
            };
            Ext.apply(defaultFormConfig, formConfig);
            var form = Ext.create('Ext.form.Panel', defaultFormConfig);
            return form;
        },
        /**
         * 格式化日期
         * @param mis 时间的毫秒数
         * @param fmt 格式化
         * @returns 格式化后的时间
         */
        getDateFormat: function (mis, fmt) {
            if (Ext.isEmpty(mis)) {
                return '';
            }
            var date = new Date(mis);
            var o = {
                "m+": date.getMonth() + 1, //月份
                "d+": date.getDate(), //日
                "h+": date.getHours(), //小时
                "i+": date.getMinutes(), //分
                "s+": date.getSeconds(), //秒
                "q+": Math.floor((date.getMonth() + 3) / 3), //季度
                "S": date.getMilliseconds(), //毫秒
            };
            if (/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            return fmt;
        }
    },
    config: {}
});