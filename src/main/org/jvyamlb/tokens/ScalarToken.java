/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ScalarToken extends Token {
    private ByteList value;
    private boolean plain;
    private char style;

    public ScalarToken(final ByteList value, final boolean plain) {
        this(value,plain,(char)0);
    }

    public ScalarToken(final ByteList value, final boolean plain, final char style) {
        this.value = value;
        this.plain = plain;
        this.style = style;
    }

    public boolean getPlain() {
        return this.plain;
    }

    public ByteList getValue() {
        return this.value;
    }

    public char getStyle() {
        return this.style;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " value=\"" + new String(value.bytes()) + "\">";
    }
}// ScalarToken
