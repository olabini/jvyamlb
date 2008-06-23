/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

import org.jruby.util.ByteList;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedMappingStartEvent extends MappingStartEvent implements Positionable {
    private Position.Range range;

    public PositionedMappingStartEvent(final String anchor, final String tag, final boolean implicit, final boolean flowStyle, final Position.Range range) {
        super(anchor, tag, implicit, flowStyle);
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
        if(!ret && (other instanceof PositionedMappingStartEvent)) {
            PositionedMappingStartEvent o = (PositionedMappingStartEvent)other;
            ret = 
                this.getImplicit() == o.getImplicit() &&
                this.getFlowStyle() == o.getFlowStyle() &&
                (null == this.getAnchor() ? null == o.getAnchor() : this.getAnchor().equals(o.getAnchor())) &&
                (null == this.getTag() ? null == o.getTag() : this.getTag().equals(o.getTag())) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " anchor="+getAnchor()+" tag="+getTag()+" implicit="+getImplicit()+" flowStyle="+getFlowStyle()+" range=" + getRange() + ">";
    }
}// PositionedMappingStartEvent
