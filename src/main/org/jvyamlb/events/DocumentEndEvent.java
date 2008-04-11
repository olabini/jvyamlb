/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DocumentEndEvent extends Event {
    private boolean explicit;

    public DocumentEndEvent(final boolean explicit) {
        this.explicit = explicit;
    }

    public boolean getExplicit() {
        return explicit;
    }
}// DocumentEndEvent
