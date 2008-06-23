/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedSequenceEndEvent extends SequenceEndEvent implements Positionable {
    private Position.Range range;

    public PositionedSequenceEndEvent(Position.Range r) {
        assert r != null;
        this.range = r;
    }
    
    public Position getPosition() {
        return range.start;
    }

    public Position.Range getRange() {
        return range;
    }

    public boolean equals(Object other) {
        return this == other || 
            ((other instanceof PositionedSequenceEndEvent) &&
             this.getRange().equals(((PositionedSequenceEndEvent)other).getRange()));
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " position=" + getPosition() + ">";
    }
}// PositionedSequenceEndEvent
