Ext.define('app.view.main.region.Center', {
	extend : 'Ext.tab.Panel',
	requires : [ 'Ext.tip.QuickTipManager',
	             'Ext.tab.Panel',
	             'Ext.ux.TabScrollerMenu',
	             'Ext.ux.TabReorderer',
	             'Ext.ux.TabCloseMenu',
	             'Ext.ux.GroupTabPanel'],
	activeTab : 0, 
	alias : 'widget.center',
	border:false,
	id:'main-center-table',
	plugins: Ext.create('Ext.ux.TabCloseMenu', {
        closeTabText: '关闭当前',
        closeOthersTabsText: '关闭其他',
        closeAllTabsText: '关闭所有',
        listeners: {
            aftermenu: function () {
                currentItem = null;
            }
        }
    }),
	listeners:{
		beforeremove: 'removeTabPanel'
	}
});
