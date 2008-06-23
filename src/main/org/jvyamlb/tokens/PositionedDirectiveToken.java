/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

import org.jruby.util.ByteList;

import org.jvyamlb.Position;
import java.util.Arrays;
import org.jvyamlb.Positionable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositionedDirectiveToken extends DirectiveToken implements Positionable {
    private Position.Range range;

    public PositionedDirectiveToken(final String name, final String[] value, final Position.Range range) {
        super(name, value);
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
        if(!ret && (other instanceof PositionedDirectiveToken)) {
            PositionedDirectiveToken o = (PositionedDirectiveToken)other;
            ret = 
                this.getName().equals(o.getName()) &&
                Arrays.equals(this.getValue(),o.getValue()) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " name=\""+getName()+"\" range=" + getRange() + ">";
    }
}// PositionedDirectiveToken
