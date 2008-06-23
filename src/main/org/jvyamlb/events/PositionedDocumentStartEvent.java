/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

import org.jruby.util.ByteList;

import org.jvyamlb.Position;
import java.util.Arrays;
import org.jvyamlb.Positionable;
import java.util.Map;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedDocumentStartEvent extends DocumentStartEvent implements Positionable {
    private Position.Range range;

    public PositionedDocumentStartEvent(final boolean explicit, final int[] version, final Map tags, final Position.Range range) {
        super(explicit, version, tags);
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
        if(!ret && (other instanceof PositionedDocumentStartEvent)) {
            PositionedDocumentStartEvent o = (PositionedDocumentStartEvent)other;
            ret = 
                this.getExplicit() == o.getExplicit() &&
                Arrays.equals(this.getVersion(), o.getVersion()) &&
                (null == this.getTags() ? null == o.getTags() : this.getTags().equals(o.getTags())) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " explicit="+getExplicit()+" version="+getVersion()[0]+"."+getVersion()[1]+" tags="+getTags()+" range=" + getRange() + ">";
    }
}// PositionedDocumentStartEvent
