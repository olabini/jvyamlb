/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import org.jvyamlb.nodes.PositionedScalarNode;
import org.jvyamlb.nodes.Node;
import org.jruby.util.ByteList;
import org.jvyamlb.events.Event;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningComposerImpl extends ComposerImpl implements PositioningComposer {
    public PositioningComposerImpl(final PositioningParser parser, final Resolver resolver) {
        super(parser, resolver);
    }

    public Position getPosition() {
        return ((PositioningParser)parser).getPosition();
    }

    public Position.Range getRange() {
        return ((PositioningParser)parser).getRange();
    }

    protected Node getScalar(final String tag, final ByteList value, final char style, final Event e) {
        return new PositionedScalarNode(tag,value,style, ((Positionable)e).getRange());
    }
}
