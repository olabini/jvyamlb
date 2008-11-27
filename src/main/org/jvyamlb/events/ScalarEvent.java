/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ScalarEvent extends NodeEvent {
    private String tag;
    private char style;
    private ByteList value;
    private boolean[] implicit;

    public ScalarEvent(final String anchor, final String tag, final boolean[] implicit, final ByteList value, final char style) {
        super(anchor);
        this.tag = tag;
        this.implicit = implicit;
        this.value = value;
        this.style = style;
    }

   public String getTag() {
       return this.tag;
    }

    public char getStyle() {
        return this.style;
    }

    public ByteList getValue() {
        return this.value;
    }

    public boolean[] getImplicit() {
        return this.implicit;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value=\"" + new String(value.bytes()) + "\">";
    }
}// ScalarEvent
