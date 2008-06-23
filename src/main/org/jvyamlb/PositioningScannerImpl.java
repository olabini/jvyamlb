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
import org.jvyamlb.tokens.PositionedBlockMappingStartToken;
import org.jvyamlb.tokens.PositionedBlockEndToken;
import org.jvyamlb.tokens.PositionedKeyToken;
import org.jvyamlb.tokens.PositionedValueToken;
import org.jvyamlb.tokens.PositionedBlockSequenceStartToken;
import org.jvyamlb.tokens.PositionedBlockEntryToken;
import org.jvyamlb.tokens.PositionedFlowMappingStartToken;
import org.jvyamlb.tokens.PositionedFlowMappingEndToken;
import org.jvyamlb.tokens.PositionedFlowSequenceStartToken;
import org.jvyamlb.tokens.PositionedFlowSequenceEndToken;
import org.jvyamlb.tokens.PositionedFlowEntryToken;
import org.jvyamlb.tokens.PositionedTagToken;
import org.jvyamlb.tokens.PositionedAliasToken;
import org.jvyamlb.tokens.PositionedAnchorToken;
import org.jvyamlb.tokens.PositionedDirectiveToken;
import org.jvyamlb.exceptions.PositionedScannerException;

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
    private Position possible = null;

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

    protected void possibleEnd() {
        this.possible = getPosition();
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
        return new PositionedBlockEndToken(new Position.Range(getPosition()));
    }

    protected BlockSequenceStartToken getBlockSequenceStart() {
        return new PositionedBlockSequenceStartToken(new Position.Range(getPosition()));
    }

    protected BlockEntryToken getBlockEntry() {
        return new PositionedBlockEntryToken(new Position.Range(getPosition()));
    }

    protected KeyToken getKey() {
        return new PositionedKeyToken(new Position.Range(getPosition()));
    }

    protected KeyToken getKey(SimpleKey key) {
        return new PositionedKeyToken(new Position.Range(key.getPosition()));
    }

    protected ValueToken getValue() {
        return new PositionedValueToken(new Position.Range(getPosition()));
    }

    protected BlockMappingStartToken getBlockMappingStart() {
        return new PositionedBlockMappingStartToken(new Position.Range(getPosition()));
    }

    protected BlockMappingStartToken getBlockMappingStart(SimpleKey key) {
        return new PositionedBlockMappingStartToken(new Position.Range(key.getPosition()));
    }

    protected FlowSequenceStartToken getFlowSequenceStart() {
        return new PositionedFlowSequenceStartToken(new Position.Range(getPosition()));
    }

    protected FlowMappingStartToken getFlowMappingStart() {
        return new PositionedFlowMappingStartToken(new Position.Range(getPosition()));
    }

    protected FlowSequenceEndToken getFlowSequenceEnd() {
        return new PositionedFlowSequenceEndToken(new Position.Range(getPosition()));
    }

    protected FlowMappingEndToken getFlowMappingEnd() {
        return new PositionedFlowMappingEndToken(new Position.Range(getPosition()));
    }

    protected FlowEntryToken getFlowEntry() {
        return new PositionedFlowEntryToken(new Position.Range(getPosition()));
    }

    protected TagToken getTag(final ByteList[] args) {
        return new PositionedTagToken(args, new Position.Range(getStartPosition(), getPosition()));
    }

    protected AliasToken getAlias() {
        return new PositionedAliasToken(new Position.Range(getPosition()));
    }

    protected AnchorToken getAnchor() {
        return new PositionedAnchorToken(new Position.Range(getPosition()));
    }

    protected Token finalizeAnchor(Token t) {
        if(t instanceof PositionedAliasToken) {
            return new PositionedAliasToken((String)((AliasToken)t).getValue(), new Position.Range(((PositionedAliasToken)t).getPosition(), getPosition()));
        } else if(t instanceof PositionedAnchorToken) {
            return new PositionedAnchorToken((String)((AnchorToken)t).getValue(), new Position.Range(((PositionedAnchorToken)t).getPosition(), getPosition()));
        }

        return t;
    }

    protected DirectiveToken getDirective(String name, String[] value) {
        return new PositionedDirectiveToken(name, value, new Position.Range(getStartPosition(), getPosition()));
    }

    protected ScalarToken getScalar(ByteList value, boolean plain, char style) {
        Position p = possible;
        if(p == null) {
            p = getPosition();
        } else {
            possible = null;
        }

        return new PositionedScalarToken(value, plain, style, new Position.Range(getStartPosition(), p));
    }

    protected SimpleKey getSimpleKey(final int tokenNumber, final boolean required, final int index, final int line, final int column) {
        final Position p = getPosition();
        return new SimpleKey(tokenNumber, required, p.offset, p.line, column);
    }

    protected void scannerException(String when, String what, String note) {
        throw new PositionedScannerException(when, what, note, new Position.Range(getPosition()));
    }
}// PositioningScannerImpl
