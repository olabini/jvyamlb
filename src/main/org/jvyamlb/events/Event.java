/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class Event {
    public String toString() {
        return "#<" + this.getClass().getName() + ">";
    }
}// Event
