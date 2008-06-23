/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jruby.util.ByteList;

import org.jvyamlb.nodes.Node;
import org.jvyamlb.nodes.PositionedScalarNode;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningComposerImplTest extends YAMLTestCase {
    public PositioningComposerImplTest(String name) {
        super(name);
    }

    protected Node getDocument(String input) {
        Composer composer = new PositioningComposerImpl(new PositioningParserImpl(new PositioningScannerImpl(input)), new ResolverImpl());
        return composer.getNode();
    }

    public void testThatEmptyComposerGeneratesPositionedEnvelope() {
        Node expected = null;
        Node node = getDocument("");
        assertEquals(expected, node);
    }

    public void testThatSimpleScalarGeneratesCorrectPositioning() throws Exception {
        Node expected = new PositionedScalarNode("tag:yaml.org,2002:str", s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1)));
        Node node = getDocument("a");
        assertEquals(expected, node);
    }
}
