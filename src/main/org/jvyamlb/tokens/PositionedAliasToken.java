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
public class PositionedAliasToken extends AliasToken implements Positionable {
    private Position.Range range;

    public PositionedAliasToken(final Position.Range range) {
        super();
        assert range != null;
        this.range = range;
    }

    public PositionedAliasToken(final String value, final Position.Range range) {
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
        if(!ret && (other instanceof PositionedAliasToken)) {
            PositionedAliasToken o = (PositionedAliasToken)other;
            ret = 
                this.getValue().equals(o.getValue()) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value=\""+getValue()+"\" range=" + getRange() + ">";
    }
}// PositionedAliasToken
