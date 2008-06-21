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
    private Position position;

    public PositionedStreamStartToken(Position p) {
        this.position = p;
    }
    
    public Position getPosition() {
        return position;
    }

    public boolean equals(Object other) {
        return this == other || 
            ((other instanceof PositionedStreamStartToken) &&
             this.getPosition().equals(((PositionedStreamStartToken)other).getPosition()));
    }
}// PositionedStreamStartToken
