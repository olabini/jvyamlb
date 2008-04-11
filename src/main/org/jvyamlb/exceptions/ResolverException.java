/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.exceptions;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ResolverException extends YAMLException {
    public ResolverException(final String msg) {
        super(msg);
    }

    public ResolverException(final Throwable thr) {
        super(thr);
    }
}// ResolverException
