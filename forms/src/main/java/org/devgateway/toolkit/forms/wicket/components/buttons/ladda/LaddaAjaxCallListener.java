package org.devgateway.toolkit.forms.wicket.components.buttons.ladda;

import org.apache.wicket.ajax.attributes.AjaxCallListener;

public class LaddaAjaxCallListener extends AjaxCallListener {
    public LaddaAjaxCallListener() {
        this.onBeforeSend("var $this = jQuery('#'+attrs.c); var l = Ladda.create($this[0]); l.start(); $this.data('ladda', l)");
        this.onComplete("var $this = jQuery('#'+attrs.c); var l = $this.data('ladda'); if (l) {l.stop(); $this.removeData('ladda');}");
    }
}
