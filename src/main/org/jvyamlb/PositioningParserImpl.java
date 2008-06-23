/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import org.jvyamlb.events.PositionedStreamEndEvent;
import org.jvyamlb.events.PositionedStreamStartEvent;
import org.jvyamlb.tokens.Token;
import org.jvyamlb.events.StreamStartEvent;
import org.jvyamlb.events.StreamEndEvent;
import org.jvyamlb.events.DocumentStartEvent;
import org.jvyamlb.events.PositionedDocumentStartEvent;
import org.jvyamlb.events.PositionedDocumentEndEvent;
import java.util.Map;
import org.jvyamlb.events.DocumentEndEvent;
import org.jvyamlb.events.PositionedDocumentEndEvent;
import org.jvyamlb.events.ScalarEvent;
import org.jvyamlb.events.PositionedScalarEvent;
import org.jruby.util.ByteList;
import org.jvyamlb.events.MappingStartEvent;
import org.jvyamlb.events.MappingEndEvent;
import org.jvyamlb.events.PositionedMappingStartEvent;
import org.jvyamlb.events.PositionedMappingEndEvent;
import org.jvyamlb.events.SequenceStartEvent;
import org.jvyamlb.events.SequenceEndEvent;
import org.jvyamlb.events.PositionedSequenceStartEvent;
import org.jvyamlb.events.PositionedSequenceEndEvent;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningParserImpl extends ParserImpl implements PositioningParser {
    protected static class PositioningProductionEnvironment extends ProductionEnvironment {
        public PositioningProductionEnvironment(final YAMLConfig cfg) {
            super(cfg);
        }

        protected StreamStartEvent getStreamStart(final Token t) {
            return new PositionedStreamStartEvent(((Positionable)t).getRange());
        }

        protected StreamEndEvent getStreamEnd(final Token t) {
            return new PositionedStreamEndEvent(((Positionable)t).getRange());
        }

        protected DocumentStartEvent getDocumentStart(final boolean explicit, final int[] version, final Map tags, final Token t) {
            return new PositionedDocumentStartEvent(explicit, version, tags, new Position.Range(((Positionable)t).getPosition()));
        }

        protected DocumentEndEvent getDocumentEndImplicit(final Token t) {
            return new PositionedDocumentEndEvent(false, new Position.Range(((Positionable)t).getPosition()));
        }

        protected DocumentEndEvent getDocumentEndExplicit(final Token t) {
            return new PositionedDocumentEndEvent(true, new Position.Range(((Positionable)t).getPosition()));
        }

        protected ScalarEvent getScalar(final String anchor, final String tag, final boolean[] implicit, final ByteList value, final char style, final Token t) {
            return new PositionedScalarEvent(anchor, tag, implicit, value, style, ((Positionable)t).getRange());
        }

        protected MappingStartEvent getMappingStart(final String anchor, final String tag, final boolean implicit, final boolean flowStyle, final Token t) {
            return new PositionedMappingStartEvent(anchor, tag, implicit, flowStyle, new Position.Range(((Positionable)t).getPosition()));
        }

        protected MappingEndEvent getMappingEnd(final Token t) {
            return new PositionedMappingEndEvent(new Position.Range(((Positionable)t).getPosition()));
        }

        protected SequenceStartEvent getSequenceStart(final String anchor, final String tag, final boolean implicit, final boolean flowStyle, final Token t) {
            return new PositionedSequenceStartEvent(anchor, tag, implicit, flowStyle, new Position.Range(((Positionable)t).getPosition()));
        }

        protected SequenceEndEvent getSequenceEnd(final Token t) {
            return new PositionedSequenceEndEvent(new Position.Range(((Positionable)t).getPosition()));
        }
    }
    
    public PositioningParserImpl(final PositioningScanner scanner) {
        super(scanner);
    }

    public PositioningParserImpl(final PositioningScanner scanner, final YAMLConfig cfg) {
        super(scanner, cfg);
    }

    public Position getPosition() {
        return ((PositioningScanner)scanner).getPosition();
    }

    public Position.Range getRange() {
        return new Position.Range(((PositioningScanner)scanner).getPosition());
    }

    protected ProductionEnvironment getEnvironment(YAMLConfig cfg) {
        return new PositioningProductionEnvironment(cfg);
    }
}
