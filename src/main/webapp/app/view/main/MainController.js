/**
 * This class is the controller for the main view for the application. It is
 * specified as the "controller" of the Main view class.
 * 
 * TODO - Replace this content of this view to suite the needs of your
 * application.
 */
Ext.define('app.view.main.MainController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.main',
	onTreeNodeItemclick : function(v, r) {
		console.log(r.data);
		if (r && r.data.leaf) {
			var dt = r.data;
			var resCode = dt['resCode'];
			var center = this.lookupReference('center');
			var isOpen = false;
			center.items.each(function(item) {
				if (item.resCode == r.data.resCode) {
					center.setActiveTab(item);
					isOpen = true;
					return;
				}
			});
			if (!isOpen) {
				if(resCode.indexOf(".mvc")>0){
					panel=Ext.create('Ext.Panel',{
						border : false,
						closable : true,
						title : dt['text'],
						resCode : dt['resCode'],
						html:'<iframe width="100%" height="100%" src="'+dt['resCode']+'"></iframe>',
						initEvents: function () {  
	                        var me = this;  
	                        var id = 'tab-' + (me.id.split('-')[1]-1+2) + '-btnWrap';  
	                        Ext.getDom(id).ondblclick  = function () {  
	                        	 window.open(me.resCode);
	                        };
	                    },  
					});
					console.log(Ext.Date.now());
					var p=center.add(panel);
					center.setActiveTab(p);
				}else{
					var dobj=Ext.create(resCode);
					var panel={};
					var obj=dobj.init();
					panel=obj.createView({
						border : false,
						closable : true,
						title : dt['text'],
						resCode : dt['resCode'],
						tools:[{
						    type:'refresh',
						    tooltip: 'Refresh form Data',
						    // hidden:true,
						    handler: function(event, toolEl, panelHeader) {
						        // refresh logic
						    }
						},
						{
						    type:'help',
						    tooltip: 'Get Help',
						    callback: function(panel, tool, event) {
						        // show help here
						    }
						}]
					});
					var p=center.add(panel);
					center.setActiveTab(p);
					if(obj.loadData){
						obj.loadData();
					}
				}
				Ext.Ajax.request({
					url : 'main/log/insertAccessLog.mvc',
					method : 'post',
			    	params : {
			    		modual : dt['text'],
						logConten : '访问'+dt['text']
			    	},
					success : function(response) {
						
					}
				});
			}
		}
		
	},
	show: function () {
        var me = this;
         refs = me.getReferences(),
         leftPanel = me.lookupReference('left'),
         wrapContainer = refs.mainContainerWrap,
         navigationList = refs.navigationTreeList,
         collapsing = !navigationList.getMicro(),
         new_width = collapsing ? 35 : 250;
            console.log(navigationList.width);
            console.log(navigationList);
            console.log(collapsing);
        if (Ext.isIE9m || !Ext.os.is.Desktop) {
            Ext.suspendLayouts();
            refs.senchaLogo.setWidth(new_width);
            navigationList.setWidth(new_width);
            navigationList.setMicro(collapsing);
            Ext.resumeLayouts(); 
            wrapContainer.layout.animatePolicy = wrapContainer.layout.animate = null;
            wrapContainer.updateLayout();  
        }
        else {
            if (!collapsing) {
                navigationList.setMicro(false);
                collapsing = false;
            }
            leftPanel.animate({dynamic: true, to: {width: new_width}});
            navigationList.width = new_width;
            wrapContainer.updateLayout({isRoot: true});
            if (collapsing) {
                navigationList.on({
                    afterlayoutanimation: function () {
                        navigationList.setMicro(true);
                    },
                    single: true
                });
            }
            console.log(navigationList.width);
        }
      },
      removeTabPanel: function(t, p){
      	  var resCode=p.resCode;
      	  var modul=resCode.substring(resCode.lastIndexOf(".")+1);
      	  viewport[modul]=null;
      	  this.lookupReference('navigationTreeList').setSelection(false);
      }
});
