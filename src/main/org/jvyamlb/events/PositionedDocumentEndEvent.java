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
public class PositionedDocumentEndEvent extends DocumentEndEvent implements Positionable {
    private Position.Range range;

    public PositionedDocumentEndEvent(final boolean explicit, final Position.Range range) {
        super(explicit);
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
        if(!ret && (other instanceof PositionedDocumentEndEvent)) {
            PositionedDocumentEndEvent o = (PositionedDocumentEndEvent)other;
            ret = 
                this.getExplicit() == o.getExplicit() &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " explicit="+getExplicit()+" range=" + getRange() + ">";
    }
}// PositionedDocumentEndEvent
