/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DirectiveToken extends Token {
    private String name;
    private String[] value;

    public DirectiveToken(final String name, final String[] value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String[] getValue() {
        return this.value;
    }
}// DirectiveToken
