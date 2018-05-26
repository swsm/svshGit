Ext.define('Ext.ux.form.MonthField', {
    extend: 'Ext.form.field.Picker',
    alias: 'widget.monthfield',
    requires: ['Ext.picker.Month'],
   // alternateClassName: ['Ext.form.MonthField', 'Ext.form.Month'],
    format: "Y-m",
    

    altFormats: "m/y|m/Y|m-y|m-Y|my|mY|y/m|Y/m|y-m|Y-m|ym|Ym",

    triggerCls: Ext.baseCSSPrefix + 'form-date-trigger',

    matchFieldWidth: false,
    
    startDay: new Date(),

    initComponent: function () {
        var me = this;
        me.disabledDatesRE = null;
        me.callParent();
    },

    initValue: function () {
        var me = this,
        value = me.value;

        if (Ext.isString(value)) {
            me.value = Ext.Date.parse(value, this.format);
        }
        if (me.value)
            me.startDay = me.value;
        me.callParent();
    },

    rawToValue: function (rawValue) {
        return Ext.Date.parse(rawValue, this.format) || rawValue || null;
    },

    valueToRaw: function (value) {
        return this.formatDate(value);
    },



    formatDate: function (date) {
        return Ext.isDate(date) ? Ext.Date.dateFormat(date, this.format) : date;
    },


    createPicker: function () {
        var me = this,
            format = Ext.String.format;
        var pick=Ext.create('Ext.picker.Month', {
            pickerField: me,
            ownerCt: me.ownerCt,
            renderTo: document.body,
            floating: true,
            shadow: false,
            listeners: {
                scope: me,
                cancelclick: me.onCancelClick,
                okclick: me.onOkClick,
                yeardblclick: me.onOkClick,
                monthdblclick: me.onOkClick
            }
        });
        pick.el.on('mousedown',  me.onMouseDown, pick);
        return pick;
    },
    onMouseDown: function(e) {
        e.preventDefault();
    },
    onExpand: function () {
    	//Ext.getCmp('id_month').blur();
    	this.picker.el.focus();
    	this.picker.show();
    	this.picker.setValue(this.startDay);
        
       
    },
    
    onOkClick: function (picker, value) {
        var me = this,
            month = value[0],
            year = value[1],
            date = new Date(year, month, 1);
        me.startDay = date;
        me.setValue(date);
        me.isExpanded = false;
        this.picker.hide();
        //this.blur();
    },

    onCancelClick: function () {
        this.picker.hide();
        this.isExpanded = false;
        //this.blur();
    }
//    ,onBlur :function(){
//    	alert(12);
//    },
    

    
});