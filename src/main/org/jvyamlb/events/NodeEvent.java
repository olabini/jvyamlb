/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class NodeEvent extends Event {
    private String anchor;
    public NodeEvent(final String anchor) {
        this.anchor = anchor;
    }

    public String getAnchor() {
        return this.anchor;
    }
}// NodeEvent
