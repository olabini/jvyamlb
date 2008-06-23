/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import org.jvyamlb.nodes.PositionedScalarNode;
import org.jvyamlb.nodes.PositionedMappingNode;
import org.jvyamlb.nodes.PositionedSequenceNode;
import org.jvyamlb.nodes.Node;
import org.jvyamlb.exceptions.PositionedComposerException;
import org.jruby.util.ByteList;
import org.jvyamlb.events.Event;
import java.util.Map;
import java.util.List;

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

    protected Node createMapping(final String tag, final Map value, final boolean flowStyle, final Event e) {
        return new PositionedMappingNode(tag,value,flowStyle, new Position.Range(((Positionable)e).getPosition()));
    }

    protected void finalizeMapping(final Node node, final Event e) {
        ((PositionedMappingNode)node).setRange(((Positionable)node).getRange().withEnd(((Positionable)e).getRange().end));
    }

    protected Node createSequence(final String tag, final List value, final boolean flowStyle, final Event e) {
        return new PositionedSequenceNode(tag,value,flowStyle, new Position.Range(((Positionable)e).getPosition()));
    }

    protected void finalizeSequence(final Node node, final Event e) {
        ((PositionedSequenceNode)node).setRange(((Positionable)node).getRange().withEnd(((Positionable)e).getRange().end));
    }

    protected void composerException(final String when, final String what, final String note, final Event e) {
        throw new PositionedComposerException(when, what, note, ((Positionable)e).getRange());
    }
}
