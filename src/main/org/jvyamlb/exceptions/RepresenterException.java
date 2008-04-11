/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.exceptions;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class RepresenterException extends YAMLException {
    public RepresenterException(final String msg) {
        super(msg);
    }

    public RepresenterException(final Throwable thr) {
        super(thr);
    }
}// RepresenterException
