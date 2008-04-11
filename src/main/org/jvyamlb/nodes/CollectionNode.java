/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.nodes;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class CollectionNode extends Node {
    private boolean flowStyle;
    public CollectionNode(final String tag, final Object value, final boolean flowStyle) {
        super(tag,value);
        this.flowStyle = flowStyle;
    }

    public boolean getFlowStyle() {
        return flowStyle;
    }
}// CollectionNode
