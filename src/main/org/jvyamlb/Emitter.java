/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.IOException;

import org.jvyamlb.events.Event;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Emitter {
    void emit(final Event event) throws IOException;
}// Emitter
