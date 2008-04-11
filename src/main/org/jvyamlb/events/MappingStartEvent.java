/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MappingStartEvent extends CollectionStartEvent {
    public MappingStartEvent(final String anchor, final String tag, final boolean implicit, final boolean flowStyle) {
        super(anchor,tag,implicit,flowStyle);
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " anchor=\"" + getAnchor() + "\" tag=\"" + getTag() + "\">";
    }
}// MappingStartEvent
