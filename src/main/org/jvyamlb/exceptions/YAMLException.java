/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.exceptions;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class YAMLException extends RuntimeException {
    public YAMLException(final String message) {
        super(message);
    }

    public YAMLException(final Throwable cause) {
        super(cause);
    }
}// YAMLException
