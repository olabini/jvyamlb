/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.nodes;

import java.util.List;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SequenceNode extends CollectionNode {
    public SequenceNode(final String tag, final List value, final boolean flowStyle) {
        super(tag,value,flowStyle);
    }
}// SequenceNode
