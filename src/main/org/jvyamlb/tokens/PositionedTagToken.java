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
public class PositionedTagToken extends TagToken implements Positionable {
    private Position.Range range;

    public PositionedTagToken(final ByteList[] value, final Position.Range range) {
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
        if(!ret && (other instanceof PositionedTagToken)) {
            PositionedTagToken o = (PositionedTagToken)other;
            ret = 
                Arrays.equals(this.getValue(), o.getValue()) &&
                this.getRange().equals(o.getRange());
        }
        return ret;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String sep = "";
        for(int i=0;i<getValue().length;i++) {
            sb.append(sep).append("\"").append(getValue()[i]).append("\"");
            sep = ", ";
        }

        return "#<" + this.getClass().getName() + " value=[" + sb + "] range=" + getRange() + ">";
    }
}// PositionedScalarToken
