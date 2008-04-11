/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.exceptions;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SerializerException extends YAMLException {
    public SerializerException(final String msg) {
        super(msg);
    }

    public SerializerException(final Throwable thr) {
        super(thr);
    }
}// SerializerException
