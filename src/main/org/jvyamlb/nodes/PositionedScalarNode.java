/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.nodes;

import org.jruby.util.ByteList;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedScalarNode extends ScalarNode implements Positionable {
    private Position.Range range;

    public PositionedScalarNode(final String tag, final ByteList value, final char style, final Position.Range range) {
        super(tag, value, style);
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
        if(!ret && (other instanceof PositionedScalarNode)) {
            PositionedScalarNode o = (PositionedScalarNode)other;
            ret = 
                this.getValue().equals(o.getValue()) &&
                (null == this.getTag() ? null == o.getTag() : this.getTag().equals(o.getTag())) &&
                this.getStyle() == o.getStyle() &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value=\"" + getValue() + "\" tag="+getTag()+" style="+getStyle()+" range=" + getRange() + ">";
    }
}// PositionedScalarEvent
