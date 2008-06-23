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
public class PositionedScalarToken extends ScalarToken implements Positionable {
    private Position.Range range;

    public PositionedScalarToken(final ByteList value, final boolean plain, final Position.Range range) {
        this(value,plain,(char)0, range);
    }

    public PositionedScalarToken(final ByteList value, final boolean plain, final char style, final Position.Range range) {
        super(value, plain, style);
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
        if(!ret && (other instanceof PositionedScalarToken)) {
            PositionedScalarToken o = (PositionedScalarToken)other;
            ret = 
                this.getValue().equals(o.getValue()) &&
                this.getPlain() == o.getPlain() &&
                this.getStyle() == o.getStyle() &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value=\"" + new String(getValue().bytes()) + "\" range=" + getRange() + ">";
    }
}// PositionedScalarToken
