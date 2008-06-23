/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

import org.jruby.util.ByteList;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;
import java.util.Arrays;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedScalarEvent extends ScalarEvent implements Positionable {
    private Position.Range range;

    public PositionedScalarEvent(final String anchor, final String tag, final boolean[] implicit, final ByteList value, final char style, final Position.Range range) {
        super(anchor, tag, implicit, value, style);
        assert range != null;
        this.range = range;
    }

    public Position getPosition() {
        return range.start;
    }

    public Position.Range getRange() {
        return range;
    }

    public boolean equals(Object other) {
        boolean ret = this == other;
        if(!ret && (other instanceof PositionedScalarEvent)) {
            PositionedScalarEvent o = (PositionedScalarEvent)other;
            ret = 
                this.getValue().equals(o.getValue()) &&
                (null == this.getTag() ? null == o.getTag() : this.getTag().equals(o.getTag())) &&
                this.getStyle() == o.getStyle() &&
                Arrays.equals(this.getImplicit(), o.getImplicit()) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value=\"" + getValue() + "\" tag="+getTag()+" style="+getStyle()+" implicit="+getImplicit()[0]+","+getImplicit()[1]+" range=" + getRange() + ">";
    }
}// PositionedScalarEvent
