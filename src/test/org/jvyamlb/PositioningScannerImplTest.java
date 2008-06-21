/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jvyamlb.tokens.PositionedStreamStartToken;
import org.jvyamlb.tokens.PositionedStreamEndToken;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningScannerImplTest extends YAMLTestCase {
    public PositioningScannerImplTest(String name) {
        super(name);
    }

    protected List getScan(String input) {
        Scanner s = new PositioningScannerImpl(input);
        List output = new ArrayList();
        for(Iterator iter = s.iterator(); iter.hasNext();) {
            output.add(iter.next());
        }
        return output;
    }

    public void testThatEmptyScannerGeneratesPositionedEnvelope() {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(new Position(0,0,0,0)));
        expected.add(new PositionedStreamEndToken(new Position(0,0,0,0)));

        List tokens = getScan("");
        assertEquals(expected, tokens);
    }
}// PositioningScannerImplTest
