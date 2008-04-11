/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.exceptions;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class EmitterException extends YAMLException {
    public EmitterException(final String msg) {
        super(msg);
    }

    public EmitterException(final Throwable thr) {
        super(thr);
    }
}// EmitterException
