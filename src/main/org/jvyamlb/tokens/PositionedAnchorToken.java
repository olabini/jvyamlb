/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

import org.jruby.util.ByteList;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedAnchorToken extends AnchorToken implements Positionable {
    private Position.Range range;

    public PositionedAnchorToken(final Position.Range range) {
        super();
        assert range != null;
        this.range = range;
    }

    public PositionedAnchorToken(final String value, final Position.Range range) {
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
        if(!ret && (other instanceof PositionedAnchorToken)) {
            PositionedAnchorToken o = (PositionedAnchorToken)other;
            ret = 
                this.getValue().equals(o.getValue()) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value=\""+getValue()+"\" range=" + getRange() + ">";
    }
}// PositionedAnchorToken
