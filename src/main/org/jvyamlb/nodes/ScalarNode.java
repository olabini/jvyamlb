/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.nodes;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ScalarNode extends Node {
    private char style;
    public ScalarNode(final String tag, final ByteList value, final char style) {
        super(tag,value);
        this.style = style;
    }

    public char getStyle() {
        return style;
    }
}// ScalarNode
