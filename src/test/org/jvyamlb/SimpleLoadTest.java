/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.jruby.util.ByteList;

import org.joda.time.DateTime;

import org.jvyamlb.exceptions.ScannerException;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SimpleLoadTest extends YAMLTestCase {
    public SimpleLoadTest(final String name) {
        super(name);
    }

    public void testStrings() throws Exception {
        assertLoad(s("str"),"!!str str");
        assertLoad(s("str"),"--- str");
        assertLoad(s("str"),"---\nstr");
        assertLoad(s("str"),"--- \nstr");
        assertLoad(s("str"),"--- \n str");
        assertLoad(s("str"),"str");
        assertLoad(s("str")," str");
        assertLoad(s("str"),"\nstr");
        assertLoad(s("str"),"\n str");
        assertLoad(s("str"),"\"str\"");
        assertLoad(s("str"),"'str'");
        assertLoad(s("str")," --- 'str'");
        assertLoad(s("1.0"),"!!str 1.0");

        assertLoad10(s("str"),"!str str");
        assertLoad10(s("1.0"),"!str 1.0");
    }

    public void testArrays() throws Exception {
        List expected = new ArrayList();
        expected.add(s("a"));
        expected.add(s("b"));
        expected.add(s("c"));

        assertLoad(expected, "--- \n- a\n- b\n- c\n");
        assertLoad(expected, "--- [a, b, c]");
        assertLoad(expected, "[a, b, c]");
    }

    public void testEmpty() throws Exception {
        assertLoad(null, "---\n!!str");      // this doesn't match Ruby's way of doing things
        assertLoad10(null, "---\n!str");
    }

    public void testStrange() throws Exception {
        assertLoad(s("---"), "--- ---\n");
        assertLoad(s("---"), "---");
    }
    
    public void testShared() throws Exception {
        ByteList inp = s("abcde");
        inp.begin = 2;
        inp.realSize = 3;
        assertLoad(s("cde"),inp);
    }

    public void testDate() throws Exception {
        assertEquals(ByteList.class, ((Map)load("{a: 2007-01-01 01:12:34}")).get(s("a")).getClass());
        assertEquals(ByteList.class, load("2007-01-01 01:12:34").getClass());
        assertEquals(ByteList.class, load("2007-01-01 01:12:34.0").getClass());
        assertEquals(DateTime.class, load("2007-01-01 01:12:34 +00:00").getClass());
        assertEquals(DateTime.class, load("2007-01-01 01:12:34.0 +00:00").getClass());
        assertEquals(ByteList.class, ((Map)load("{a: 2007-01-01 01:12:34}")).get(s("a")).getClass());
    }

    public void testObjectIdentity() throws Exception {
        List val = (List)load("---\n- foo\n- foo\n- [foo]\n- [foo]\n- {foo: foo}\n- {foo: foo}\n");
        assertNotSame(val.get(0), val.get(1));
        assertNotSame(val.get(2), val.get(3));
        assertNotSame(val.get(4), val.get(5));
    }

    public void testStrangeNesting() throws Exception {
        Map expected = new HashMap();
        Map internal = new HashMap();
        internal.put(s("bar"), null);
        expected.put(s("foo"), internal);
        assertLoad(expected, "---\nfoo: { bar }\n");

        expected = new HashMap();
        List internal2 = new ArrayList();
        internal2.add(s("a"));
        expected.put(s("default"), internal2);

        expected = new HashMap();
        internal = new HashMap();
        internal.put(s("bar"),null);
        internal.put(s("qux"),null);
        expected.put(s("foo"), internal);
        assertLoad(expected, "---\nfoo: {bar, qux}");

        // This looks like bad YAML to me... But MRI supports it
        //        assertLoad(expected, "---\ndefault: -\n- a\n");
    }

    public void testStrangeCharacters() throws Exception {
        assertLoad(s(",a"), "--- \n,a"); 

        Map expected = new HashMap();
        expected.put(s("foobar"), s(">= 123"));
        assertLoad(expected, "foobar: >= 123");

        expected = new HashMap();
        expected.put(s("foo"), s("bar"));
        assertLoad(expected, "---\nfoo: \tbar");
    }

    public void testLoadOfAsterisk() throws Exception {
        assertLoad(s("*.rb"), "--- \n*.rb"); 
        assertLoad(s("*.rb"), "--- \n'*.rb'");

        assertLoad(s("&.rb"), "--- \n&.rb"); 
        assertLoad(s("&.rb"), "--- \n'&.rb'");
        
        try {
            assertLoad(null, "--- \n*r.b");
            assertTrue(false);
        } catch(ScannerException e) {
            assertTrue(true);
        }

        try {
            assertLoad(null, "--- \n&r.b");
            assertTrue(false);
        } catch(ScannerException e) {
            assertTrue(true);
        }
    }
}// SimpleLoadTest
