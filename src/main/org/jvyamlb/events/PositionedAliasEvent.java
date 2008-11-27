/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedAliasEvent extends AliasEvent implements Positionable {
    private Position.Range range;

    public PositionedAliasEvent(final String value, final Position.Range range) {
        super(value);
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
        if(!ret && (other instanceof PositionedAliasEvent)) {
            PositionedAliasEvent o = (PositionedAliasEvent)other;
            ret = 
                this.getAnchor().equals(o.getAnchor()) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value="+getAnchor()+" range=" + getRange() + ">";
    }
}// PositionedAliasEvent
