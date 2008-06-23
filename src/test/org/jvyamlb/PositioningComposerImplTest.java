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
import org.jvyamlb.nodes.PositionedMappingNode;
import org.jvyamlb.nodes.PositionedSequenceNode;
import java.util.HashMap;
import java.util.Map;
import org.jvyamlb.exceptions.ComposerException;
import org.jvyamlb.exceptions.PositionedComposerException;

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

    public void testThatAKeyValuePairGetsCorrectPositioning() throws Exception {
        Map value = new HashMap();
        value.put(
                  new PositionedScalarNode("tag:yaml.org,2002:str", s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1))),
                  new PositionedScalarNode("tag:yaml.org,2002:str", s("b"), (char)0, new Position.Range(new Position(0,3,3), new Position(0,4,4)))
                  );
        Node expected = new PositionedMappingNode("tag:yaml.org,2002:map", value, false, new Position.Range(new Position(0,0,0), new Position(0,4,4)));
        Node node = getDocument("a: b");
        assertEquals(expected, node);
    }

    public void testThatASimpleSequenceGetsCorrectPositioning() throws Exception {
        List value = new ArrayList();
        value.add(
                  new PositionedScalarNode("tag:yaml.org,2002:str", s("a"), (char)0, new Position.Range(new Position(0,2,2), new Position(0,3,3)))
                  );
        Node expected = new PositionedSequenceNode("tag:yaml.org,2002:seq", value, false, new Position.Range(new Position(0,0,0), new Position(0,3,3)));
        Node node = getDocument("- a");
        assertEquals(expected, node);
    }

    public void testComposerExceptionIncludesPositioningInformation() throws Exception {
        try {
            getDocument(" *foobar");
            assertTrue("composing should throw an exception", false);
        } catch(ComposerException e) {
            assertTrue("Exception should be instance of PositionedComposerException", e instanceof PositionedComposerException);
            assertEquals("Position should be correct for exception", new Position.Range(new Position(0,1,1),new Position(0,8,8)), ((PositionedComposerException)e).getRange());
        }
    }
}
