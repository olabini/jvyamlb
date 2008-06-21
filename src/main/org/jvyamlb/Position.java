/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Position {
    public final int line;
    public final int column;
    public final int offset;
    public final int length;

    public Position(final int line, final int column, final int offset) {
        this(line, column, offset, 0);
    }
    
    public Position(final int line, final int column, final int offset, final int length) {
        this.line = line;
        this.column = column;
        this.offset = offset;
        this.length = length;
    }

    public boolean equals(Object other) {
        boolean res = this == other;
        if(!res && (other instanceof Position)) {
            Position o = (Position)other;
            res = 
                this.line == o.line &&
                this.column == o.column &&
                this.offset == o.offset &&
                this.length == o.length;
        }
        return res;
    }
}// Position
