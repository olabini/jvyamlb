/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.InputStream;
import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jvyamlb.exceptions.ParserException;

import org.jvyamlb.events.*;
import org.jvyamlb.tokens.*;
import org.jvyamlb.util.IntStack;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ParserImpl implements Parser {
    // Memnonics for the production table
    private final static int P_STREAM = 0;
    private final static int P_STREAM_START = 1; // TERMINAL
    private final static int P_STREAM_END = 2; // TERMINAL
    private final static int P_IMPLICIT_DOCUMENT = 3;
    private final static int P_EXPLICIT_DOCUMENT = 4;
    private final static int P_DOCUMENT_START = 5;
    private final static int P_DOCUMENT_START_IMPLICIT = 6;
    private final static int P_DOCUMENT_END = 7;
    private final static int P_BLOCK_NODE = 8;
    private final static int P_BLOCK_CONTENT = 9;
    private final static int P_PROPERTIES = 10;
    private final static int P_PROPERTIES_END = 11;
    private final static int P_FLOW_CONTENT = 12;
    private final static int P_BLOCK_SEQUENCE = 13;
    private final static int P_BLOCK_MAPPING = 14;
    private final static int P_FLOW_SEQUENCE = 15;
    private final static int P_FLOW_MAPPING = 16;
    private final static int P_SCALAR = 17;
    private final static int P_BLOCK_SEQUENCE_ENTRY = 18;
    private final static int P_BLOCK_MAPPING_ENTRY = 19;
    private final static int P_BLOCK_MAPPING_ENTRY_VALUE = 20;
    private final static int P_BLOCK_NODE_OR_INDENTLESS_SEQUENCE = 21;
    private final static int P_BLOCK_SEQUENCE_START = 22;
    private final static int P_BLOCK_SEQUENCE_END = 23;
    private final static int P_BLOCK_MAPPING_START = 24;
    private final static int P_BLOCK_MAPPING_END = 25;
    private final static int P_INDENTLESS_BLOCK_SEQUENCE = 26;
    private final static int P_BLOCK_INDENTLESS_SEQUENCE_START = 27;
    private final static int P_INDENTLESS_BLOCK_SEQUENCE_ENTRY = 28;
    private final static int P_BLOCK_INDENTLESS_SEQUENCE_END = 29;
    private final static int P_FLOW_SEQUENCE_START = 30;
    private final static int P_FLOW_SEQUENCE_ENTRY = 31;
    private final static int P_FLOW_SEQUENCE_END = 32;
    private final static int P_FLOW_MAPPING_START = 33;
    private final static int P_FLOW_MAPPING_ENTRY = 34;
    private final static int P_FLOW_MAPPING_END = 35;
    private final static int P_FLOW_INTERNAL_MAPPING_START = 36;
    private final static int P_FLOW_INTERNAL_CONTENT = 37;
    private final static int P_FLOW_INTERNAL_VALUE = 38;
    private final static int P_FLOW_INTERNAL_MAPPING_END = 39;
    private final static int P_FLOW_ENTRY_MARKER = 40;
    private final static int P_FLOW_NODE = 41;
    private final static int P_FLOW_MAPPING_INTERNAL_CONTENT = 42;
    private final static int P_FLOW_MAPPING_INTERNAL_VALUE = 43;
    private final static int P_ALIAS = 44;
    private final static int P_EMPTY_SCALAR = 45;

    private final static String[] productionNames = new String[46];

    static {
        productionNames[0]="P_STREAM";
        productionNames[1]="P_STREAM_START";
        productionNames[2]="P_STREAM_END";
        productionNames[3]="P_IMPLICIT_DOCUMENT";
        productionNames[4]="P_EXPLICIT_DOCUMENT";
        productionNames[5]="P_DOCUMENT_START";
        productionNames[6]="P_DOCUMENT_START_IMPLICIT";
        productionNames[7]="P_DOCUMENT_END";
        productionNames[8]="P_BLOCK_NODE";
        productionNames[9]="P_BLOCK_CONTENT";
        productionNames[10]="P_PROPERTIES";
        productionNames[11]="P_PROPERTIES_END";
        productionNames[12]="P_FLOW_CONTENT";
        productionNames[13]="P_BLOCK_SEQUENCE";
        productionNames[14]="P_BLOCK_MAPPING";
        productionNames[15]="P_FLOW_SEQUENCE";
        productionNames[16]="P_FLOW_MAPPING";
        productionNames[17]="P_SCALAR";
        productionNames[18]="P_BLOCK_SEQUENCE_ENTRY";
        productionNames[19]="P_BLOCK_MAPPING_ENTRY";
        productionNames[20]="P_BLOCK_MAPPING_ENTRY_VALUE";
        productionNames[21]="P_BLOCK_NODE_OR_INDENTLESS_SEQUENCE";
        productionNames[22]="P_BLOCK_SEQUENCE_START";
        productionNames[23]="P_BLOCK_SEQUENCE_END";
        productionNames[24]="P_BLOCK_MAPPING_START";
        productionNames[25]="P_BLOCK_MAPPING_END";
        productionNames[26]="P_INDENTLESS_BLOCK_SEQUENCE";
        productionNames[27]="P_BLOCK_INDENTLESS_SEQUENCE_START";
        productionNames[28]="P_INDENTLESS_BLOCK_SEQUENCE_ENTRY";
        productionNames[29]="P_BLOCK_INDENTLESS_SEQUENCE_END";
        productionNames[30]="P_FLOW_SEQUENCE_START";
        productionNames[31]="P_FLOW_SEQUENCE_ENTRY";
        productionNames[32]="P_FLOW_SEQUENCE_END";
        productionNames[33]="P_FLOW_MAPPING_START";
        productionNames[34]="P_FLOW_MAPPING_ENTRY";
        productionNames[35]="P_FLOW_MAPPING_END";
        productionNames[36]="P_FLOW_INTERNAL_MAPPING_START";
        productionNames[37]="P_FLOW_INTERNAL_CONTENT";
        productionNames[38]="P_FLOW_INTERNAL_VALUE";
        productionNames[39]="P_FLOW_INTERNAL_MAPPING_END";
        productionNames[40]="P_FLOW_ENTRY_MARKER";
        productionNames[41]="P_FLOW_NODE";
        productionNames[42]="P_FLOW_MAPPING_INTERNAL_CONTENT";
        productionNames[43]="P_FLOW_MAPPING_INTERNAL_VALUE";
        productionNames[44]="P_ALIAS";
        productionNames[45]="P_EMPTY_SCALAR";
    }

    private final static DocumentEndEvent DOCUMENT_END_TRUE = new DocumentEndEvent(true);
    private final static DocumentEndEvent DOCUMENT_END_FALSE = new DocumentEndEvent(false);
    private final static MappingEndEvent MAPPING_END = new MappingEndEvent();
    private final static SequenceEndEvent SEQUENCE_END = new SequenceEndEvent();
    private final static StreamEndEvent STREAM_END = new StreamEndEvent();
    private final static StreamStartEvent STREAM_START = new StreamStartEvent();

    protected static class ProductionEnvironment {
        private List tags;
        private List anchors;
        private List tagTokens;
        private List anchorTokens;
        private Map tagHandles;
        private int[] yamlVersion;
        private int[] defaultYamlVersion;

        public ProductionEnvironment(final YAMLConfig cfg) {
            this.tags = new LinkedList();
            this.anchors = new LinkedList();
            this.tagTokens = new LinkedList();
            this.anchorTokens = new LinkedList();
            this.tagHandles = new HashMap();
            this.yamlVersion = null;
            this.defaultYamlVersion = new int[2];
            this.defaultYamlVersion[0] = Integer.parseInt(cfg.version().substring(0,cfg.version().indexOf('.')));
            this.defaultYamlVersion[1] = Integer.parseInt(cfg.version().substring(cfg.version().indexOf('.')+1));
        }

        public List getTags() {
            return this.tags;
        }

        public List getAnchors() {
            return this.anchors;
        }

        public List getTagTokens() {
            return this.tagTokens;
        }

        public List getAnchorTokens() {
            return this.anchorTokens;
        }

        public Map getTagHandles() {
            return this.tagHandles;
        }

        public int[] getYamlVersion() {
            return this.yamlVersion;
        }

        public int[] getFinalYamlVersion() {
            if(null == this.yamlVersion) {
                return this.defaultYamlVersion;
            }
            return this.yamlVersion;
        }

        public void setYamlVersion(final int[] yamlVersion) {
            this.yamlVersion = yamlVersion;
        }

        protected StreamStartEvent getStreamStart(final Token t) {
            return STREAM_START;
        }

        protected StreamEndEvent getStreamEnd(final Token t) {
            return STREAM_END;
        }

        protected DocumentStartEvent getDocumentStart(final boolean explicit, final int[] version, final Map tags, final Token t) {
            return new DocumentStartEvent(explicit, version, tags);
        }

        protected DocumentEndEvent getDocumentEndImplicit(final Token t) {
            return DOCUMENT_END_FALSE;
        }

        protected DocumentEndEvent getDocumentEndExplicit(final Token t) {
            return DOCUMENT_END_TRUE;
        }

        protected ScalarEvent getScalar(final String anchor, final String tag, final boolean[] implicit, final ByteList value, final char style, final Token t, final Token anchorT, final Token tagT) {
            return new ScalarEvent(anchor, tag, implicit, value, style);
        }

        protected MappingStartEvent getMappingStart(final String anchor, final String tag, final boolean implicit, final boolean flowStyle, final Token t, final Token anchorT, final Token tagT) {
            return new MappingStartEvent(anchor, tag, implicit, flowStyle);
        }

        protected MappingEndEvent getMappingEnd(final Token t) {
            return MAPPING_END;
        }

        protected SequenceStartEvent getSequenceStart(final String anchor, final String tag, final boolean implicit, final boolean flowStyle, final Token t, final Token anchorT, final Token tagT) {
            return new SequenceStartEvent(anchor, tag, implicit, flowStyle);
        }

        protected SequenceEndEvent getSequenceEnd(final Token t) {
            return SEQUENCE_END;
        }

        protected AliasEvent getAlias(final String value, final Token t) {
            return new AliasEvent(value);
        }

        protected void setEmptyToken(Token t) {
        }

        protected Token getEmptyToken(Scanner scanner) {
            return scanner.peekToken();
        }

        protected void parserException(final String when, final String what, final String note, final Token t) {
            throw new ParserException(when, what, note);
        }

        public Event produce(final int current, final IntStack parseStack, final Scanner scanner) {
            switch(current) {
            case P_STREAM: {
                parseStack.push(P_STREAM_END);
                parseStack.push(P_EXPLICIT_DOCUMENT);
                parseStack.push(P_IMPLICIT_DOCUMENT);
                parseStack.push(P_STREAM_START);
                return null;
            }
            case P_STREAM_START: {
                return getStreamStart(scanner.getToken());
            }
            case P_STREAM_END: {
                return getStreamEnd(scanner.getToken());
            }
            case P_IMPLICIT_DOCUMENT: {
                final Token curr = scanner.peekToken();
                if(!(curr instanceof DirectiveToken || curr instanceof DocumentStartToken || curr instanceof StreamEndToken)) {
                    parseStack.push(P_DOCUMENT_END);
                    parseStack.push(P_BLOCK_NODE);
                    parseStack.push(P_DOCUMENT_START_IMPLICIT);
                }
                return null;
            }
            case P_EXPLICIT_DOCUMENT: {
                if(!(scanner.peekToken() instanceof StreamEndToken)) {
                    parseStack.push(P_EXPLICIT_DOCUMENT);
                    parseStack.push(P_DOCUMENT_END);
                    parseStack.push(P_BLOCK_NODE);
                    parseStack.push(P_DOCUMENT_START);
                }
                return null;
            }
            case P_DOCUMENT_START: {
                Token tok = scanner.peekToken();
                final Object[] directives = processDirectives(this,scanner);
                if(!(scanner.peekToken() instanceof DocumentStartToken)) {
                    parserException(null,"expected '<document start>', but found " + tok.getClass().getName(),null,scanner.peekToken());
                }
                scanner.getToken();
                return getDocumentStart(true,(int[])directives[0],(Map)directives[1], tok);
            }
            case P_DOCUMENT_START_IMPLICIT: {
                Token tok = scanner.peekToken();
                final Object[] directives = processDirectives(this,scanner);
                return getDocumentStart(false,(int[])directives[0],(Map)directives[1], tok);
            }
            case P_DOCUMENT_END: {
                Token tok = scanner.peekToken();
                boolean explicit = false;
                while(scanner.peekToken() instanceof DocumentEndToken) {
                    tok = scanner.getToken();
                    explicit = true;
                }
                return explicit ? getDocumentEndExplicit(tok) : getDocumentEndImplicit(tok);
            }
            case P_BLOCK_NODE: {
                final Token curr = scanner.peekToken();
                if(curr instanceof DirectiveToken || curr instanceof DocumentStartToken || curr instanceof DocumentEndToken || curr instanceof StreamEndToken) {
                    parseStack.push(P_EMPTY_SCALAR);
                } else {
                    if(curr instanceof AliasToken) {
                        parseStack.push(P_ALIAS);
                    } else {
                        parseStack.push(P_PROPERTIES_END);
                        parseStack.push(P_BLOCK_CONTENT);
                        parseStack.push(P_PROPERTIES);
                    }
                }
                return null;
            }
            case P_BLOCK_CONTENT: {
                final Token tok = scanner.peekToken();
                if(tok instanceof BlockSequenceStartToken) {
                    parseStack.push(P_BLOCK_SEQUENCE);
                } else if(tok instanceof BlockMappingStartToken) {
                    parseStack.push(P_BLOCK_MAPPING);
                } else if(tok instanceof FlowSequenceStartToken) {
                    parseStack.push(P_FLOW_SEQUENCE);
                } else if(tok instanceof FlowMappingStartToken) {
                    parseStack.push(P_FLOW_MAPPING);
                } else if(tok instanceof ScalarToken) {
                    parseStack.push(P_SCALAR);
                } else {
                    // Part of solution for JRUBY-718
                    boolean[] implicit = new boolean[]{false,false};
                    return getScalar((String)this.getAnchors().get(0),(String)this.getTags().get(0),implicit,new ByteList(new byte[0],false),'\'', tok, (Token)this.getAnchorTokens().get(0), (Token)this.getTagTokens().get(0));
                }
                return null;
            }
            case P_PROPERTIES: {
                String anchor = null;
                Object tag = null;
                Token anchorToken = null;
                Token tagToken = null;
                if(scanner.peekToken() instanceof AnchorToken) {
                    anchorToken = scanner.getToken();
                    anchor = ((AnchorToken)anchorToken).getValue();
                    if(scanner.peekToken() instanceof TagToken) {
                        tagToken = scanner.getToken();
                        tag = ((TagToken)tagToken).getValue();
                    }
                } else if(scanner.peekToken() instanceof TagToken) {
                    tagToken = scanner.getToken();
                    tag = ((TagToken)tagToken).getValue();
                    if(scanner.peekToken() instanceof AnchorToken) {
                        anchorToken = scanner.getToken();
                        anchor = ((AnchorToken)anchorToken).getValue();
                    }
                }
                if(tag != null && !tag.equals("!")) {
                    final String handle = ScannerImpl.into(((ByteList[])tag)[0]);
                    String suffix = ScannerImpl.into(((ByteList[])tag)[1]);
                    int ix = -1;
                    if((ix = suffix.indexOf("^")) != -1) {
                        suffix = suffix.substring(0,ix) + suffix.substring(ix+1);
                    }
                    if(handle != null) {
                        if(!this.getTagHandles().containsKey(handle)) {
                            parserException("while parsing a node","found undefined tag handle " + handle,null,tagToken);
                        }
                        if((ix = suffix.indexOf("/")) != -1) {
                            String before = suffix.substring(0,ix);
                            String after = suffix.substring(ix+1);
                            if(ONLY_WORD.matcher(before).matches()) {
                                tag = "tag:" + before + ".yaml.org,2002:" + after;
                            } else {
                                if(before.startsWith("tag:")) {
                                    tag = before + ":" + after;
                                } else {
                                    tag = "tag:" + before + ":" + after;
                                }
                            }
                        } else {
                            tag = ((String)this.getTagHandles().get(handle)) + suffix;
                        }
                    } else {
                        tag = suffix;
                    }
                }
                this.getAnchors().add(0,anchor);
                this.getTags().add(0,tag);
                this.getAnchorTokens().add(0,anchorToken);
                this.getTagTokens().add(0,tagToken);
                return null;
            }
            case P_PROPERTIES_END: {
                this.getAnchors().remove(0);
                this.getTags().remove(0);
                this.getAnchorTokens().remove(0);
                this.getTagTokens().remove(0);
                return null;
            }
            case P_FLOW_CONTENT: {
                final Token tok = scanner.peekToken();
                if(tok instanceof FlowSequenceStartToken) {
                    parseStack.push(P_FLOW_SEQUENCE);
                } else if(tok instanceof FlowMappingStartToken) {
                    parseStack.push(P_FLOW_MAPPING);
                } else if(tok instanceof ScalarToken) {
                    parseStack.push(P_SCALAR);
                } else {
                    parserException("while scanning a flow node","expected the node content, but found " + tok.getClass().getName(),null,tok);
                }
                return null;
            }
            case P_BLOCK_SEQUENCE: {
                parseStack.push(P_BLOCK_SEQUENCE_END);
                parseStack.push(P_BLOCK_SEQUENCE_ENTRY);
                parseStack.push(P_BLOCK_SEQUENCE_START);
                return null;
            }
            case P_BLOCK_MAPPING: {
                parseStack.push(P_BLOCK_MAPPING_END);
                parseStack.push(P_BLOCK_MAPPING_ENTRY);
                parseStack.push(P_BLOCK_MAPPING_START);
                return null;
            }
            case P_FLOW_SEQUENCE: {
                parseStack.push(P_FLOW_SEQUENCE_END);
                parseStack.push(P_FLOW_SEQUENCE_ENTRY);
                parseStack.push(P_FLOW_SEQUENCE_START);
                return null;
            }
            case P_FLOW_MAPPING: {
                parseStack.push(P_FLOW_MAPPING_END);
                parseStack.push(P_FLOW_MAPPING_ENTRY);
                parseStack.push(P_FLOW_MAPPING_START);
                return null;
            }
            case P_SCALAR: {
                final ScalarToken tok = (ScalarToken)scanner.getToken();
                boolean[] implicit = null;
                if((tok.getPlain() && this.getTags().get(0) == null) || "!".equals(this.getTags().get(0))) {
                    implicit = new boolean[]{true,false};
                } else if(this.getTags().get(0) == null) {
                    implicit = new boolean[]{false,true};
                } else {
                    implicit = new boolean[]{false,false};
                }
                return getScalar((String)this.getAnchors().get(0),(String)this.getTags().get(0),implicit,tok.getValue(),tok.getStyle(), tok, (Token)this.getAnchorTokens().get(0), (Token)this.getTagTokens().get(0));
            }
            case P_BLOCK_SEQUENCE_ENTRY: {
                if(scanner.peekToken() instanceof BlockEntryToken) {
                    scanner.getToken();
                    if(!(scanner.peekToken() instanceof BlockEntryToken || scanner.peekToken() instanceof BlockEndToken)) {
                        parseStack.push(P_BLOCK_SEQUENCE_ENTRY);
                        parseStack.push(P_BLOCK_NODE);
                    } else {
                        parseStack.push(P_BLOCK_SEQUENCE_ENTRY);
                        parseStack.push(P_EMPTY_SCALAR);
                    }
                }
                return null;
            }
            case P_BLOCK_MAPPING_ENTRY: {
                if(scanner.peekToken() instanceof KeyToken || scanner.peekToken() instanceof ValueToken) {
                    if(scanner.peekToken() instanceof KeyToken) {
                        scanner.getToken();
                        final Token curr = scanner.peekToken();
                        if(!(curr instanceof KeyToken || curr instanceof ValueToken || curr instanceof BlockEndToken)) {
                            parseStack.push(P_BLOCK_MAPPING_ENTRY);
                            parseStack.push(P_BLOCK_MAPPING_ENTRY_VALUE);
                            parseStack.push(P_BLOCK_NODE_OR_INDENTLESS_SEQUENCE);
                        } else {
                            parseStack.push(P_BLOCK_MAPPING_ENTRY);
                            parseStack.push(P_BLOCK_MAPPING_ENTRY_VALUE);
                            parseStack.push(P_EMPTY_SCALAR);
                        }
                    } else {
                        parseStack.push(P_BLOCK_MAPPING_ENTRY);
                        parseStack.push(P_BLOCK_MAPPING_ENTRY_VALUE);
                        parseStack.push(P_EMPTY_SCALAR);
                    }
                }

                return null;
            }
            case P_BLOCK_MAPPING_ENTRY_VALUE: {
                if(scanner.peekToken() instanceof KeyToken || scanner.peekToken() instanceof ValueToken) {
                    if(scanner.peekToken() instanceof ValueToken) {
                        final Token value = scanner.getToken();
                        final Token curr = scanner.peekToken();
                        if(!(curr instanceof KeyToken || curr instanceof ValueToken || curr instanceof BlockEndToken)) {
                            if(curr instanceof ScalarToken && scanner.peekToken(1) instanceof BlockEntryToken) {
                                //                                System.err.println("warning, possibly invalid YAML, eating token: " + curr);
                                scanner.getToken();
                            }
                            parseStack.push(P_BLOCK_NODE_OR_INDENTLESS_SEQUENCE);
                        } else {
                            setEmptyToken(value);
                            parseStack.push(P_EMPTY_SCALAR);
                        }
                    } else {
                        parseStack.push(P_EMPTY_SCALAR);
                    }
                }
                return null;
            }
            case P_BLOCK_NODE_OR_INDENTLESS_SEQUENCE: {
                if(scanner.peekToken() instanceof AliasToken) {
                    parseStack.push(P_ALIAS);
                } else {
                    if(scanner.peekToken() instanceof BlockEntryToken) {
                        parseStack.push(P_INDENTLESS_BLOCK_SEQUENCE);
                        parseStack.push(P_PROPERTIES);
                    } else {
                        parseStack.push(P_BLOCK_CONTENT);
                        parseStack.push(P_PROPERTIES);
                    }
                }
                return null;
            }
            case P_BLOCK_SEQUENCE_START: {
                final boolean implicit = this.getTags().get(0) == null || this.getTags().get(0).equals("!");
                return getSequenceStart((String)this.getAnchors().get(0), (String)this.getTags().get(0), implicit,false,scanner.getToken(), (Token)this.getAnchorTokens().get(0), (Token)this.getTagTokens().get(0));
            }
            case P_BLOCK_SEQUENCE_END: {
                Token tok = null;
                if(!(scanner.peekToken() instanceof BlockEndToken)) {
                    tok = scanner.peekToken();
                    parserException("while scanning a block collection","expected <block end>, but found " + tok.getClass().getName(),null,tok);
                }
                return getSequenceEnd(scanner.getToken());
            }
            case P_BLOCK_MAPPING_START: {
                final boolean implicit = this.getTags().get(0) == null || this.getTags().get(0).equals("!");
                return getMappingStart((String)this.getAnchors().get(0), (String)this.getTags().get(0), implicit,false,scanner.getToken(), (Token)this.getAnchorTokens().get(0), (Token)this.getTagTokens().get(0));
            }
            case P_BLOCK_MAPPING_END: {
                Token tok = null;
                if(!(scanner.peekToken() instanceof BlockEndToken)) {
                    tok = scanner.peekToken();
                    parserException("while scanning a block mapping","expected <block end>, but found " + tok.getClass().getName(),null,tok);
                }
                return getMappingEnd(scanner.getToken());
            }
            case P_INDENTLESS_BLOCK_SEQUENCE: {
                parseStack.push(P_BLOCK_INDENTLESS_SEQUENCE_END);
                parseStack.push(P_INDENTLESS_BLOCK_SEQUENCE_ENTRY);
                parseStack.push(P_BLOCK_INDENTLESS_SEQUENCE_START);
                return null;
            }
            case P_BLOCK_INDENTLESS_SEQUENCE_START: {
                final boolean implicit = this.getTags().get(0) == null || this.getTags().get(0).equals("!");
                return getSequenceStart((String)this.getAnchors().get(0), (String)this.getTags().get(0), implicit, false, scanner.peekToken(), (Token)this.getAnchorTokens().get(0), (Token)this.getTagTokens().get(0));
            }
            case P_INDENTLESS_BLOCK_SEQUENCE_ENTRY: {
                if(scanner.peekToken() instanceof BlockEntryToken) {
                    scanner.getToken();
                    final Token curr = scanner.peekToken();
                    if(!(curr instanceof BlockEntryToken || curr instanceof KeyToken || curr instanceof ValueToken || curr instanceof BlockEndToken)) {
                        parseStack.push(P_INDENTLESS_BLOCK_SEQUENCE_ENTRY);
                        parseStack.push(P_BLOCK_NODE);
                    } else {
                        parseStack.push(P_INDENTLESS_BLOCK_SEQUENCE_ENTRY);
                        parseStack.push(P_EMPTY_SCALAR);
                    }
                }
                return null;
            }
            case P_BLOCK_INDENTLESS_SEQUENCE_END: {
                return getSequenceEnd(scanner.peekToken());
            }
            case P_FLOW_SEQUENCE_START: {
                final boolean implicit = this.getTags().get(0) == null || this.getTags().get(0).equals("!");
                return getSequenceStart((String)this.getAnchors().get(0), (String)this.getTags().get(0), implicit,true,scanner.getToken(), (Token)this.getAnchorTokens().get(0), (Token)this.getTagTokens().get(0));
            }
            case P_FLOW_SEQUENCE_ENTRY: {
                if(!(scanner.peekToken() instanceof FlowSequenceEndToken)) {
                    if(scanner.peekToken() instanceof KeyToken) {
                        parseStack.push(P_FLOW_SEQUENCE_ENTRY);
                        parseStack.push(P_FLOW_ENTRY_MARKER);
                        parseStack.push(P_FLOW_INTERNAL_MAPPING_END);
                        parseStack.push(P_FLOW_INTERNAL_VALUE);
                        parseStack.push(P_FLOW_INTERNAL_CONTENT);
                        parseStack.push(P_FLOW_INTERNAL_MAPPING_START);
                    } else {
                        parseStack.push(P_FLOW_SEQUENCE_ENTRY);
                        parseStack.push(P_FLOW_NODE);
                        parseStack.push(P_FLOW_ENTRY_MARKER);
                    }
                }
                return null;
            }
            case P_FLOW_SEQUENCE_END: {
                return getSequenceEnd(scanner.getToken());
            }
            case P_FLOW_MAPPING_START: {
                final boolean implicit = this.getTags().get(0) == null || this.getTags().get(0).equals("!");
                return getMappingStart((String)this.getAnchors().get(0), (String)this.getTags().get(0), implicit,true, scanner.getToken(), (Token)this.getAnchorTokens().get(0), (Token)this.getTagTokens().get(0));
            }
            case P_FLOW_MAPPING_ENTRY: {
                if(!(scanner.peekToken() instanceof FlowMappingEndToken)) {
                    if(scanner.peekToken() instanceof KeyToken) {
                        parseStack.push(P_FLOW_MAPPING_ENTRY);
                        parseStack.push(P_FLOW_ENTRY_MARKER);
                        parseStack.push(P_FLOW_MAPPING_INTERNAL_VALUE);
                        parseStack.push(P_FLOW_MAPPING_INTERNAL_CONTENT);
                    } else {
                        parseStack.push(P_FLOW_MAPPING_ENTRY);
                        parseStack.push(P_EMPTY_SCALAR);
                        parseStack.push(P_FLOW_NODE);
                        parseStack.push(P_FLOW_ENTRY_MARKER);
                    }
                }
                return null;
            }
            case P_FLOW_MAPPING_END: {
                return getMappingEnd(scanner.getToken());
            }
            case P_FLOW_INTERNAL_MAPPING_START: {
                return getMappingStart(null,null,true,true, scanner.getToken(), null, null);
            }
            case P_FLOW_INTERNAL_CONTENT: {
                final Token curr = scanner.peekToken();
                if(!(curr instanceof ValueToken || curr instanceof FlowEntryToken || curr instanceof FlowSequenceEndToken)) {
                    parseStack.push(P_FLOW_NODE);
                } else {
                    parseStack.push(P_EMPTY_SCALAR);
                }
                return null;
            }
            case P_FLOW_INTERNAL_VALUE: {
                if(scanner.peekToken() instanceof ValueToken) {
                    scanner.getToken();
                    if(!((scanner.peekToken() instanceof FlowEntryToken) || (scanner.peekToken() instanceof FlowSequenceEndToken))) {
                        parseStack.push(P_FLOW_NODE);
                    } else {
                        parseStack.push(P_EMPTY_SCALAR);
                    }
                } else {
                    parseStack.push(P_EMPTY_SCALAR);
                }
                return null;
            }
            case P_FLOW_INTERNAL_MAPPING_END: {
                return getMappingEnd(scanner.peekToken());
            }
            case P_FLOW_ENTRY_MARKER: {
                if(scanner.peekToken() instanceof FlowEntryToken) {
                    scanner.getToken();
                }
                return null;
            }
            case P_FLOW_NODE: {
                if(scanner.peekToken() instanceof AliasToken) {
                    parseStack.push(P_ALIAS);
                } else {
                    parseStack.push(P_PROPERTIES_END);
                    parseStack.push(P_FLOW_CONTENT);
                    parseStack.push(P_PROPERTIES);
                }
                return null;
            }
            case P_FLOW_MAPPING_INTERNAL_CONTENT: {
                final Token curr = scanner.peekToken();
                if(!(curr instanceof ValueToken || curr instanceof FlowEntryToken || curr instanceof FlowMappingEndToken)) {
                    scanner.getToken();
                    parseStack.push(P_FLOW_NODE);
                } else {
                    parseStack.push(P_EMPTY_SCALAR);
                }
                return null;
            }
            case P_FLOW_MAPPING_INTERNAL_VALUE: {
                if(scanner.peekToken() instanceof ValueToken) {
                    scanner.getToken();
                    if(!(scanner.peekToken() instanceof FlowEntryToken || scanner.peekToken() instanceof FlowMappingEndToken)) {
                        parseStack.push(P_FLOW_NODE);
                    } else {
                        parseStack.push(P_EMPTY_SCALAR);
                    }
                } else {
                    parseStack.push(P_EMPTY_SCALAR);
                }
                return null;
            }
            case P_ALIAS: {
                final AliasToken tok = (AliasToken)scanner.getToken();
                return getAlias(tok.getValue(), tok);
            }
            case P_EMPTY_SCALAR: {
                return getScalar(null,null,new boolean[]{true,false},new ByteList(ByteList.NULL_ARRAY),(char)0, getEmptyToken(scanner), null, null);
            }
            }

            return null;
        }
    }

    private final static Map DEFAULT_TAGS_1_0 = new HashMap();
    private final static Map DEFAULT_TAGS_1_1 = new HashMap();
    static {
        DEFAULT_TAGS_1_0.put("!","tag:yaml.org,2002:");
        DEFAULT_TAGS_1_0.put("!!","");

        DEFAULT_TAGS_1_1.put("!","!");
        DEFAULT_TAGS_1_1.put("!!","tag:yaml.org,2002:");
    }
    private final static Pattern ONLY_WORD = Pattern.compile("^\\w+$");

    private static Object[] processDirectives(final ProductionEnvironment env, final Scanner scanner) {
        while(scanner.peekToken() instanceof DirectiveToken) {
            final DirectiveToken tok = (DirectiveToken)scanner.getToken();
            if(tok.getName().equals("YAML")) {
                if(env.getYamlVersion() != null) {
                    env.parserException(null,"found duplicate YAML directive",null,tok);
                }
                final int major = Integer.parseInt(tok.getValue()[0]);
                final int minor = Integer.parseInt(tok.getValue()[1]);
                if(major != 1) {
                    env.parserException(null,"found incompatible YAML document (version 1.* is required)",null,tok);
                }
                env.setYamlVersion(new int[]{major,minor});
            } else if(tok.getName().equals("TAG")) {
                final String handle = tok.getValue()[0];
                final String prefix = tok.getValue()[1];
                if(env.getTagHandles().containsKey(handle)) {
                    env.parserException(null,"duplicate tag handle " + handle,null,tok);
                }
                env.getTagHandles().put(handle,prefix);
            }
        }
        Object[] value = new Object[2];
        value[0] = env.getFinalYamlVersion();

        if(!env.getTagHandles().isEmpty()) {
            value[1] = new HashMap(env.getTagHandles());
        }

        final Map baseTags = ((int[])value[0])[1] == 0 ? DEFAULT_TAGS_1_0 : DEFAULT_TAGS_1_1;
        for(final Iterator iter = baseTags.keySet().iterator();iter.hasNext();) {
            final Object key = iter.next();
            if(!env.getTagHandles().containsKey(key)) {
                env.getTagHandles().put(key,baseTags.get(key));
            }
        }
        return value;
    }

    protected Scanner scanner = null;
    private YAMLConfig cfg = null;

    public ParserImpl(final Scanner scanner) {
        this(scanner, YAML.config());
    }

    public ParserImpl(final Scanner scanner, final YAMLConfig cfg) {
        this.scanner = scanner;
        this.cfg = cfg;
    }

    private Event currentEvent = null;

    public boolean checkEvent(final Class[] choices) {
        parseStream();
        if(this.currentEvent == null) {
            this.currentEvent = parseStreamNext();
        }
        if(this.currentEvent != null) {
            if(choices.length == 0) {
                return true;
            }
            for(int i=0,j=choices.length;i<j;i++) {
                if(choices[i].isInstance(this.currentEvent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Event peekEvent() {
        parseStream();
        if(this.currentEvent == null) {
            this.currentEvent = parseStreamNext();
        }
        return this.currentEvent;
    }

    public Event getEvent() {
        parseStream();
        if(this.currentEvent == null) {
            this.currentEvent = parseStreamNext();
        }
        final Event value = this.currentEvent;
        this.currentEvent = null;
        return value;
    }

    private class EventIterator implements Iterator {
        public boolean hasNext() {
            return null != peekEvent();
        }

        public Object next() {
            return getEvent();
        }

        public void remove() {
        }
    }

    public Iterator eachEvent() {
        return new EventIterator();
    }

    public Iterator iterator() {
        return eachEvent();
    }

    private IntStack parseStack = null;
    private ProductionEnvironment pEnv = null;

    protected ProductionEnvironment getEnvironment(YAMLConfig cfg) {
        return new ProductionEnvironment(cfg);
    }

    public void parseStream() {
        if(null == parseStack) {
            this.parseStack = new IntStack();
            this.parseStack.push(P_STREAM);
            this.pEnv = getEnvironment(cfg);
        }
    }

    public Event parseStreamNext() {
        while(!parseStack.isEmpty()) {
            final Event value = this.pEnv.produce(this.parseStack.pop(), this.parseStack, this.scanner);
            if(null != value) {
                return value;
            }
        }
        this.pEnv = null;
        return null;
    }

    public static void tmainx(final String[] args) throws Exception {
        final String filename = args[0];
        System.out.println("Reading of file: \"" + filename + "\"");

        final ByteList input = new ByteList(1024);
        final InputStream reader = new FileInputStream(filename);
        byte[] buff = new byte[1024];
        int read = 0;
        while(true) {
            read = reader.read(buff);
            input.append(buff,0,read);
            if(read < 1024) {
                break;
            }
        }
        reader.close();
        final long before = System.currentTimeMillis();
        for(int i=0;i<1;i++) {
            final Parser pars = new ParserImpl(new ScannerImpl(input));
            for(final Iterator iter = pars.eachEvent();iter.hasNext();iter.next()) {
            }
        }
        final long after = System.currentTimeMillis();
        final long time = after-before;
        final double timeS = (after-before)/1000.0;
        System.out.println("Walking through the events for the file: " + filename + " took " + time + "ms, or " + timeS + " seconds"); 
    }

    public static void tmain(final String[] args) throws Exception {
        final String filename = args[0];
        System.out.println("Reading of file: \"" + filename + "\"");

        final InputStream reader = new FileInputStream(filename);
        final long before = System.currentTimeMillis();
        for(int i=0;i<1;i++) {
            final Parser pars = new ParserImpl(new ScannerImpl(reader));
            for(final Iterator iter = pars.eachEvent();iter.hasNext();iter.next()) {
            }
        }
        reader.close();
        final long after = System.currentTimeMillis();
        final long time = after-before;
        final double timeS = (after-before)/1000.0;
        System.out.println("Walking through the events for the file: " + filename + " took " + time + "ms, or " + timeS + " seconds"); 
    }

    public static void main(final String[] args) throws Exception {
        final String filename = args[0];
        final Parser pars = new ParserImpl(new ScannerImpl(new FileInputStream(filename)));
        for(final Iterator iter = pars.eachEvent();iter.hasNext();) {
            System.out.println(iter.next());
        }
    }
}// ParserImpl
