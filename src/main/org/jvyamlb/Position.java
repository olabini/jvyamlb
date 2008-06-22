/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Position {
    public static class Range {
        public final Position start;
        public final Position end;

        public Range(final Position start) {
            this(start, start);
        }

        public Range(final Position start, final Position end) {
            this.start = start;
            this.end = end;
        }

        public Range withStart(final Position start) {
            return new Range(start, this.end);
        }

        public Range withEnd(final Position end) {
            return new Range(this.start, end);
        }
  
        public boolean equals(Object other) {
            boolean res = this == other;
            if(!res && (other instanceof Range)) {
                Range o = (Range)other;
                res = 
                    this.start.equals(o.start) &&
                    this.end.equals(o.end);
            }
            return res;
        }

        public String toString() {
            return "#<Range " + start + ":" + end + ">";
        }
    }

    public final int line;
    public final int column;
    public final int offset;

    public Position(final int line, final int column, final int offset) {
        this.line = line;
        this.column = column;
        this.offset = offset;
    }

    public Position withLine(final int line) {
        return new Position(line, this.column, this.offset);
    }

    public Position withColumn(final int column) {
        return new Position(this.line, column, this.offset);
    }

    public Position withOffset(final int offset) {
        return new Position(this.line, this.column, offset);
    }
  
    public boolean equals(Object other) {
        boolean res = this == other;
        if(!res && (other instanceof Position)) {
            Position o = (Position)other;
            res = 
                this.line == o.line &&
                this.column == o.column &&
                this.offset == o.offset;
        }
        return res;
    }

    public String toString() {
        return "[" + line + ":" + column + "(" + offset + ")]";
    }
}// Position
