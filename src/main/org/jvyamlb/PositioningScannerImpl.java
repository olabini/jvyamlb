/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jruby.util.ByteList;
import org.jvyamlb.tokens.AliasToken;
import org.jvyamlb.tokens.AnchorToken;
import org.jvyamlb.tokens.BlockEndToken;
import org.jvyamlb.tokens.BlockEntryToken;
import org.jvyamlb.tokens.BlockMappingStartToken;
import org.jvyamlb.tokens.BlockSequenceStartToken;
import org.jvyamlb.tokens.DirectiveToken;
import org.jvyamlb.tokens.DocumentEndToken;
import org.jvyamlb.tokens.DocumentStartToken;
import org.jvyamlb.tokens.FlowEntryToken;
import org.jvyamlb.tokens.FlowMappingEndToken;
import org.jvyamlb.tokens.FlowMappingStartToken;
import org.jvyamlb.tokens.FlowSequenceEndToken;
import org.jvyamlb.tokens.FlowSequenceStartToken;
import org.jvyamlb.tokens.KeyToken;
import org.jvyamlb.tokens.PositionedDocumentEndToken;
import org.jvyamlb.tokens.PositionedDocumentStartToken;
import org.jvyamlb.tokens.PositionedScalarToken;
import org.jvyamlb.tokens.PositionedStreamEndToken;
import org.jvyamlb.tokens.PositionedStreamStartToken;
import org.jvyamlb.tokens.ScalarToken;
import org.jvyamlb.tokens.StreamEndToken;
import org.jvyamlb.tokens.StreamStartToken;
import org.jvyamlb.tokens.TagToken;
import org.jvyamlb.tokens.Token;
import org.jvyamlb.tokens.ValueToken;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningScannerImpl extends ScannerImpl implements PositioningScanner {
    public PositioningScannerImpl(final InputStream stream) {
        super(stream);
    }

    public PositioningScannerImpl(final ByteList stream) {
        super(stream);
    }

    public PositioningScannerImpl(final String stream) {
        super(stream);
    }    

    private int line = 0;
    private int offset = 0;
    private List started = new ArrayList();

    protected void forward() {
        final int lastPointer = pointer;
        super.forward();
        // Forwarding one character always sets the column to zero on
        // a new line, or sets column to a non zero value
        if(column == 0) {
            line++;
        }
        offset += (pointer - lastPointer);
    }

    protected void forward(final int length) {
        // This implementation is not as efficient as it could
        // be. This makes it simpler to reason about new lines,
        // though, so it is worth it.
        for(int i = 0; i < length; i++) {
            forward();
        }
    }

    public Position getPosition() {
        return new Position(line, column, offset);
    }

    public Position getStartPosition() {
        return (Position)started.remove(0);
    }

    public Position.Range getRange() {
        return new Position.Range(getStartPosition(), getPosition());
    }

    protected void startingItem() {
        started.add(0, getPosition());
    }

    protected StreamStartToken getStreamStart() {
        return new PositionedStreamStartToken(new Position.Range(getPosition()));
    }

    protected StreamEndToken getStreamEnd() {
        return new PositionedStreamEndToken(new Position.Range(getPosition()));
    }

    protected DocumentStartToken getDocumentStart() {
        final Position p = getPosition();
        return new PositionedDocumentStartToken(new Position.Range(p, p.withOffset(p.offset + 3).withColumn(p.column + 3)));
    }

    protected DocumentEndToken getDocumentEnd() {
        final Position p = getPosition();
        return new PositionedDocumentEndToken(new Position.Range(p, p.withOffset(p.offset + 3).withColumn(p.column + 3)));
    }
    
    protected BlockEndToken getBlockEnd() {
        return Token.BLOCK_END;
    }

    protected BlockSequenceStartToken getBlockSequenceStart() {
        return Token.BLOCK_SEQUENCE_START;
    }

    protected BlockEntryToken getBlockEntry() {
        return Token.BLOCK_ENTRY;
    }

    protected KeyToken getKey() {
        return Token.KEY;
    }

    protected ValueToken getValue() {
        return Token.VALUE;
    }

    protected BlockMappingStartToken getBlockMappingStart() {
        return Token.BLOCK_MAPPING_START;
    }

    protected FlowSequenceStartToken getFlowSequenceStart() {
        return Token.FLOW_SEQUENCE_START;
    }

    protected FlowMappingStartToken getFlowMappingStart() {
        return Token.FLOW_MAPPING_START;
    }

    protected FlowSequenceEndToken getFlowSequenceEnd() {
        return Token.FLOW_SEQUENCE_END;
    }

    protected FlowMappingEndToken getFlowMappingEnd() {
        return Token.FLOW_MAPPING_END;
    }

    protected FlowEntryToken getFlowEntry() {
        return Token.FLOW_ENTRY;
    }

    protected TagToken getTag(final ByteList[] args) {
        return new TagToken(args);
    }

    protected AliasToken getAlias() {
        return new AliasToken();
    }

    protected AnchorToken getAnchor() {
        return new AnchorToken();
    }

    protected DirectiveToken getDirective(String name, String[] value) {
        return new DirectiveToken(name, value);
    }

    protected ScalarToken getScalar(ByteList value, boolean plain, char style) {
        return new PositionedScalarToken(value, plain, style, getRange());
    }
}// PositioningScannerImpl
