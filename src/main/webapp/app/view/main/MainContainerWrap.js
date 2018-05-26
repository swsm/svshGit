Ext.define('app.view.main.MainContainerWrap', {
    extend: 'Ext.container.Container',
    xtype: 'maincontainerwrap',
    requires : [
        'Ext.layout.container.HBox'
    ],
	autoScroll:true,
    scrollable: 'y',
    layout: {
        type: 'hbox',
        align: 'stretchmax',
        animate: true,
        animatePolicy: {
            x: true,
            width: true
        }
    },

    beforeLayout : function() {
          var me = this;
          var  height = Ext.Element.getViewportHeight() - 115;  // offset by topmost toolbar height
            // We use itemId/getComponent instead of "reference" because the initial
            // layout occurs too early for the reference to be resolved
//          a = me.getCmp('main-view-detail-wrap');
//			a =	me.getComponent('main-view-detail-wrap');
//			console.log(me.getComponent('navigationTreeList'));
			if (me.getComponent('navigationTreeList')) {
				return;
			}
           var navTree = me.getComponent('main-view-detail-wrap').down("treelist"); 
//			navTree = me.lookupReference('navigationTreeList');
//			navTree = me.getItemId('navigationTreeList');
        me.minHeight = height;
        navTree.setStyle({
            'min-height': height + 'px',
        });
        me.callParent(arguments);
    }
});
