/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class StreamStartToken extends Token {
    public boolean equals(Object other) {
        return this == other || (other instanceof StreamStartToken);
    }
}// StreamStartToken
