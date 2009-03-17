/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.IOException;

import java.text.MessageFormat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.jvyamlb.events.AliasEvent;
import org.jvyamlb.events.DocumentEndEvent;
import org.jvyamlb.events.DocumentStartEvent;
import org.jvyamlb.events.ScalarEvent;
import org.jvyamlb.events.MappingEndEvent;
import org.jvyamlb.events.MappingStartEvent;
import org.jvyamlb.events.SequenceEndEvent;
import org.jvyamlb.events.SequenceStartEvent;
import org.jvyamlb.events.StreamEndEvent;
import org.jvyamlb.events.StreamStartEvent;

import org.jvyamlb.exceptions.SerializerException;

import org.jvyamlb.nodes.Node;
import org.jvyamlb.nodes.LinkNode;
import org.jvyamlb.nodes.CollectionNode;
import org.jvyamlb.nodes.MappingNode;
import org.jvyamlb.nodes.ScalarNode;
import org.jvyamlb.nodes.SequenceNode;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SerializerImpl implements Serializer {
    private Emitter emitter;
    private Resolver resolver;
    private YAMLConfig options;
    private boolean useExplicitStart;
    private boolean useExplicitEnd;
    private int[] useVersion;
    private boolean useTags;
    private String anchorTemplate;
    private Map serializedNodes;
    private Map anchors;
    private int lastAnchorId;
    private boolean closed;
    private boolean opened;

    public SerializerImpl(final Emitter emitter, final Resolver resolver, final YAMLConfig opts) {
        this.emitter = emitter;
        this.resolver = resolver;
        this.options = opts;
        this.useExplicitStart = opts.explicitStart();
        this.useExplicitEnd = opts.explicitEnd();
        int[] version = new int[2];
        if(opts.useVersion()) {
            final String v1 = opts.version();
            final int index = v1.indexOf('.');
            version[0] = Integer.parseInt(v1.substring(0,index));
            version[1] = Integer.parseInt(v1.substring(index+1));
        } else {
            version = null;
        }
        this.useVersion = version;
        this.useTags = opts.useHeader();
        this.anchorTemplate = opts.anchorFormat() == null ? "id{0,number,000}" : opts.anchorFormat();
        this.serializedNodes = new IdentityHashMap();
        this.anchors = new IdentityHashMap();
        this.lastAnchorId = 0;
        this.closed = false;
        this.opened = false;
    }

    protected boolean ignoreAnchor(final Node node) {
        return false;
    }

    public void open() throws IOException {
        if(!closed && !opened) {
            this.emitter.emit(new StreamStartEvent());
            this.opened = true;
        } else if(closed) {
            throw new SerializerException("serializer is closed");
        } else {
            throw new SerializerException("serializer is already opened");
        }
    }

    public void close() throws IOException {
        if(!opened) {
            throw new SerializerException("serializer is not opened");
        } else if(!closed) {
            this.emitter.emit(new StreamEndEvent());
            this.closed = true;
            this.opened = false;
        }
    }

    public void serialize(final Node node) throws IOException {
        if(!this.closed && !this.opened) {
            throw new SerializerException("serializer is not opened");
        } else if(this.closed) {
            throw new SerializerException("serializer is closed");
        }
        this.emitter.emit(new DocumentStartEvent(this.useExplicitStart,this.useVersion,null));
        anchorNode(node);
        serializeNode(node,null,null);
        this.emitter.emit(new DocumentEndEvent(this.useExplicitEnd));
        this.serializedNodes = new IdentityHashMap();
        this.anchors = new IdentityHashMap();
        this.lastAnchorId = 0;
    }

    private void anchorNode(Node node) {
        while(node instanceof LinkNode) {
            node = ((LinkNode)node).getAnchor();
        }
        if(!ignoreAnchor(node)) {
            if(this.anchors.containsKey(node)) {
                String anchor = (String)this.anchors.get(node);
                if(null == anchor) {
                    anchor = generateAnchor(node);
                    this.anchors.put(node,anchor);
                }
            } else {
                this.anchors.put(node,null);
                if(node instanceof SequenceNode) {
                    for(final Iterator iter = ((List)node.getValue()).iterator();iter.hasNext();) {
                        anchorNode((Node)iter.next());
                    }
                } else if(node instanceof MappingNode) {
                    final Map value = (Map)node.getValue();
                    for(final Iterator iter = value.entrySet().iterator();iter.hasNext();) {
                        final Map.Entry me = (Map.Entry)iter.next();
                        anchorNode((Node)me.getKey());
                        anchorNode((Node)me.getValue());
                    }
                }
            }
        }
    }

    private String generateAnchor(final Node node) {
        this.lastAnchorId++;
        return new MessageFormat(this.anchorTemplate).format(new Object[]{new Integer(this.lastAnchorId)});
    }

    private void serializeNode(Node node, final Node parent, final Object index) throws IOException {
        while(node instanceof LinkNode) {
            node = ((LinkNode)node).getAnchor();
        }
        String tAlias = (String)this.anchors.get(node);
        if(this.serializedNodes.containsKey(node) && tAlias != null) {
            this.emitter.emit(new AliasEvent(tAlias));
        } else {
            this.serializedNodes.put(node,null);
            this.resolver.descendResolver(parent,index);
            if(node instanceof ScalarNode) {
                final String detectedTag = this.resolver.resolve(ScalarNode.class,(ByteList)node.getValue(),new boolean[]{true,false});
                final String defaultTag = this.resolver.resolve(ScalarNode.class,(ByteList)node.getValue(),new boolean[]{false,true});
                final boolean[] implicit = new boolean[] {false,false};
                if(!options.explicitTypes()) {
                    implicit[0] = node.getTag().equals(detectedTag) || node.getTag().startsWith(detectedTag+":");
                    implicit[1] = node.getTag().equals(defaultTag) || node.getTag().startsWith(defaultTag+":");
                }
                char style = ((ScalarNode)node).getStyle();
                if(!implicit[0] && implicit[1] && node.getTag().equals(YAML.DEFAULT_SCALAR_TAG)) {
                    style = '"';
                }
                this.emitter.emit(new ScalarEvent(tAlias,node.getTag(),implicit,(ByteList)node.getValue(),style));
            } else if(node instanceof SequenceNode) {
                final boolean implicit = !options.explicitTypes() && (node.getTag().equals(this.resolver.resolve(SequenceNode.class,null,new boolean[]{true,true})));
                this.emitter.emit(new SequenceStartEvent(tAlias,node.getTag(),implicit,((CollectionNode)node).getFlowStyle()));
                int ix = 0;
                for(final Iterator iter = ((List)node.getValue()).iterator();iter.hasNext();) {
                    serializeNode((Node)iter.next(),node,new Integer(ix++));
                }
                this.emitter.emit(new SequenceEndEvent());
            } else if(node instanceof MappingNode) {
                final boolean implicit = !options.explicitTypes() && (node.getTag().equals(this.resolver.resolve(MappingNode.class,null,new boolean[]{true,true})));
                this.emitter.emit(new MappingStartEvent(tAlias,node.getTag(),implicit,((CollectionNode)node).getFlowStyle()));
                final Map value = (Map)node.getValue();
                for(final Iterator iter = value.entrySet().iterator();iter.hasNext();) {
                    final Map.Entry entry = (Map.Entry)iter.next();
                    final Node key = (Node)entry.getKey();
                    final Node val = (Node)entry.getValue();
                    serializeNode(key,node,null);
                    serializeNode(val,node,key);
                }
                this.emitter.emit(new MappingEndEvent());
            }
        }
    }
}// SerializerImpl
