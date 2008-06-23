/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class Token {
    public final static DocumentStartToken DOCUMENT_START = new DocumentStartToken();
    public final static DocumentEndToken DOCUMENT_END = new DocumentEndToken();
    public final static BlockMappingStartToken BLOCK_MAPPING_START = new BlockMappingStartToken();
    public final static BlockSequenceStartToken BLOCK_SEQUENCE_START = new BlockSequenceStartToken();
    public final static BlockEntryToken BLOCK_ENTRY = new BlockEntryToken();
    public final static BlockEndToken BLOCK_END = new BlockEndToken();
    public final static FlowEntryToken FLOW_ENTRY = new FlowEntryToken();
    public final static FlowMappingEndToken FLOW_MAPPING_END = new FlowMappingEndToken();
    public final static FlowMappingStartToken FLOW_MAPPING_START = new FlowMappingStartToken();
    public final static FlowSequenceEndToken FLOW_SEQUENCE_END = new FlowSequenceEndToken();
    public final static FlowSequenceStartToken FLOW_SEQUENCE_START = new FlowSequenceStartToken();
    public final static KeyToken KEY = new KeyToken();
    public final static ValueToken VALUE = new ValueToken();
    public final static StreamEndToken STREAM_END = new StreamEndToken();
    public final static StreamStartToken STREAM_START = new StreamStartToken();

    public Token() {
    }

    public void setValue(final Object value) {
    }

    public String toString() {
        return "#<" + this.getClass().getName() + ">";
    }
}// Token
