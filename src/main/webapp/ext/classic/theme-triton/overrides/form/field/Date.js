Ext.define('Ext.theme.triton.form.field.Date', {
    override: 'Ext.form.field.Date',
    
    doAlign: function(){
        var me = this,
            picker = me.picker,
            aboveSfx = '-above',
            isAbove;

        // Align to the trigger wrap because the border isn't always on the input element, which
        // can cause the offset to be off
        picker.alignTo(me.triggerWrap, me.pickerAlign, me.pickerOffset);
        // add the {openCls}-above class if the picker was aligned above
        // the field due to hitting the bottom of the viewport
        picker.el.setX(me.inputEl.getX());
        isAbove = picker.el.getY() < me.inputEl.getY();
        me.bodyEl[isAbove ? 'addCls' : 'removeCls'](me.openCls + aboveSfx);
        picker[isAbove ? 'addCls' : 'removeCls'](picker.baseCls + aboveSfx);
    },
});
