/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.nodes;

import java.util.Map;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MappingNode extends CollectionNode {
    public MappingNode(final String tag, final Map value, final boolean flowStyle) {
        super(tag,value,flowStyle);
    }
}// MappingNode
