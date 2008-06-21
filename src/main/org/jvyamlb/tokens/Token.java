/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.tokens;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class Token {
    public final static Token DOCUMENT_START = new DocumentStartToken();
    public final static Token DOCUMENT_END = new DocumentEndToken();
    public final static Token BLOCK_MAPPING_START = new BlockMappingStartToken();
    public final static Token BLOCK_SEQUENCE_START = new BlockSequenceStartToken();
    public final static Token BLOCK_ENTRY = new BlockEntryToken();
    public final static Token BLOCK_END = new BlockEndToken();
    public final static Token FLOW_ENTRY = new FlowEntryToken();
    public final static Token FLOW_MAPPING_END = new FlowMappingEndToken();
    public final static Token FLOW_MAPPING_START = new FlowMappingStartToken();
    public final static Token FLOW_SEQUENCE_END = new FlowSequenceEndToken();
    public final static Token FLOW_SEQUENCE_START = new FlowSequenceStartToken();
    public final static Token KEY = new KeyToken();
    public final static Token VALUE = new ValueToken();
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
