/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.nodes;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class LinkNode extends Node {
    public LinkNode() {
        super(null,null);
    }

    public void setAnchor(Node anchor) {
        setValue(anchor);
    }

    public Node getAnchor() {
        return (Node)getValue();
    }
}// LinkNode
