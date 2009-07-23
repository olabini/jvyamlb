/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class TagToken extends Token {
    private ByteList[] value;
    public TagToken(final ByteList[] value) {
        this.value = value;
    }
    
    public ByteList[] getValue() {
        return this.value;
    }

    public String toString() {
        return "" + value[0] + value[1];
    }
}// TagToken
