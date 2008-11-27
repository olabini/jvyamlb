/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.exceptions;

import org.jvyamlb.Position;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedParserException extends ParserException implements Positionable {
    private Position.Range range;
    
    public PositionedParserException(final String when, final String what, final String note, final Position.Range range) {
        super(when, what, note);
        assert range != null;
        this.range = range;
    }

    public PositionedParserException(final Throwable thr, final Position.Range range) {
        super(thr);
        assert range != null;
        this.range = range;
    }

    public Position getPosition() {
        return range.start;
    }

    public Position.Range getRange(){
        return range;
    }

    public String toString() {
        final StringBuffer lines = new StringBuffer();
        lines.append(super.toString());
        lines.append(" at ").append(getPosition());
        return lines.toString();
    }
}// PositionedParserException
