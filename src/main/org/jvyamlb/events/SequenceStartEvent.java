/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.events;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SequenceStartEvent extends CollectionStartEvent {
    public SequenceStartEvent(final String anchor, final String tag, final boolean implicit, final boolean flowStyle) {
        super(anchor,tag,implicit,flowStyle);
    }
 }// SequenceStartEvent
