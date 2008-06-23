/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedStreamStartToken extends StreamStartToken implements Positionable {
    private Position.Range range;

    public PositionedStreamStartToken(Position.Range r) {
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
            ((other instanceof PositionedStreamStartToken) &&
             this.getRange().equals(((PositionedStreamStartToken)other).getRange()));
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " position=" + getPosition() + ">";
    }
}// PositionedStreamStartToken
