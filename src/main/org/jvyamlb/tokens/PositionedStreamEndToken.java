/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedStreamEndToken extends StreamEndToken implements Positionable {
    private Position position;

    public PositionedStreamEndToken(Position p) {
        this.position = p;
    }

    public Position getPosition() {
        return position;
    }

    public boolean equals(Object other) {
        return this == other || 
            ((other instanceof PositionedStreamEndToken) &&
             this.getPosition().equals(((PositionedStreamEndToken)other).getPosition()));
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " position=" + getPosition() + ">";
    }
}// PositionedStreamEndToken
