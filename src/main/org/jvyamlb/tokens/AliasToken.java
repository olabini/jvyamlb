/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AliasToken extends Token {
    private String value;

    public AliasToken() {
        super();
    }
    public AliasToken(final String value) {
        this.value = value;
    }

    public void setValue(final Object value) {
        this.value = (String)value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "*" + value;
    }
}// AliasToken
