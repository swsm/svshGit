Ext.define('Ext.theme.triton.grid.Panel', {
    override: 'Ext.grid.Panel',
    config: {
        /**
         * @cfg {Ext.data.Model} selection
         * The selected model. Typically used with {@link #bind binding}.
         */
        selection: null,

        /**
         * @cfg {Boolean} [headerBorders=`true`]
         * To show no borders around grid headers, configure this as `false`.
         */
        headerBorders: true,
        
        listeners : {
			'containerclick' : function(grid, e, eOpts){ 
				grid.focus(); 
			},
			'validateedit' : function(editor, e){
		        if(e.value != e.originalValue){
		          e.grid.getStore().data.sorted=false;
//		          e.grid.getStore().data.setSorters(null);
		        }
		      },
		      'edit' : function(editor, e){
		          for (var i=0;i<e.grid.columns.length;i++) {
		            if (!Ext.isEmpty(e.grid.columns[i].sortState)) {
		              e.grid.getStore().data.sorted=true;
		              return;
		            }
		          }
		        }
		},
        
    },
    
	initComponent: function(){
		var me=this;
		Ext.apply(me.viewConfig,{
			listeners : {
				'scroll' : function(myGrid){
					myGrid.grid.focus(); 
				}
			}
	});
		me.callParent(arguments);
	}
});