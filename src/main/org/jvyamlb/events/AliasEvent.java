/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AliasEvent extends NodeEvent {
    public AliasEvent(final String anchor) {
        super(anchor);
    }
}// AliasEvent
