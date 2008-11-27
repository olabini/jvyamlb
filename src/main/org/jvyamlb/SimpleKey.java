/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
class SimpleKey {
    private int tokenNumber;
    private boolean required;
    private int index;
    private int line;
    private int column;

    public SimpleKey(final int tokenNumber, final boolean required, final int index, final int line, final int column) {
        this.tokenNumber = tokenNumber;
        this.required = required;
        this.index = index;
        this.line = line;
        this.column = column;
    }

    public boolean isRequired() {
        return required;
    }

    public int getTokenNumber() {
        return this.tokenNumber;
    }

    public int getColumn() {
        return this.column;
    }

    public Position getPosition() {
        return new Position(line, column, index);
    }
}// SimpleKey
