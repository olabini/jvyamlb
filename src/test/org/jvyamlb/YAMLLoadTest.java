/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.jvyamlb.exceptions.ParserException;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class YAMLLoadTest extends YAMLTestCase {
    public YAMLLoadTest(final String name) {
        super(name);
    }

    public void testBasicStringScalarLoad() throws Exception {
        ByteList str = s("str");
        assertLoad(str,"--- str");
        assertLoad(str,"---\nstr");
        assertLoad(str,"--- \nstr");
        assertLoad(str,"--- \n str");
        assertLoad(str,"str");
        assertLoad(str," str");
        assertLoad(str,"\nstr");
        assertLoad(str,"\n str");
        assertLoad(str,"\"str\"");
        assertLoad(str,"'str'");
        assertLoad(s("\u00fc"),"---\n\"\\xC3\\xBC\"");
    }
    
    public void testBasicIntegerScalarLoad() throws Exception {
        assertLoad(new Long(47),"47");
        assertLoad(new Long(0),"0");
        assertLoad(new Long(-1),"-1");
    }

    public void testBlockMappingLoad() throws Exception {
        Map expected = new HashMap();
        expected.put(s("a"),s("b"));
        expected.put(s("c"),s("d"));
        assertLoad(expected,"a: b\nc: d");
        assertLoad(expected,"c: d\na: b\n");
    }

    public void testFlowMappingLoad() throws Exception {
        Map expected = new HashMap();
        expected.put(s("a"),s("b"));
        expected.put(s("c"),s("d"));
        assertLoad(expected,"{a: b, c: d}");
        assertLoad(expected,"{c: d,\na: b}");
    }

    public void testInternalChar() throws Exception {
        Map expected = new HashMap();
        expected.put(s("bad_sample"),s("something:("));
        assertLoad(expected,"--- \nbad_sample: something:(\n");
    }

    public void testBuiltinTag() throws Exception {
        assertLoad(s("str"),"!!str str");
        assertLoad(s("str"),"%YAML 1.1\n---\n!!str str");
        assertLoad(s("str"),"%YAML 1.0\n---\n!str str");

        assertLoad10(s("str"),"---\n!str str");
        assertLoad10(new Long(123),"---\n!int 123");
        assertLoad10(new Long(123),"%YAML 1.1\n---\n!!int 123");
    }

    public void testDirectives() throws Exception {
        assertLoad(s("str"),"%YAML 1.1\n--- !!str str");
        assertLoad(s("str"),"%YAML 1.1\n%TAG !yaml! tag:yaml.org,2002:\n--- !yaml!str str");
        try {
            YAML.load(s("%YAML 1.1\n%YAML 1.1\n--- !!str str"));
            fail("should throw exception when repeating directive");
        } catch(final ParserException e) {
            assertTrue(true);
        }
    }

    public void testJavaBeanLoad() throws Exception {
        org.joda.time.DateTime dt = new org.joda.time.DateTime(1982,5,3,0,0,0,0);
        final TestBean expected = new TestBean(s("Ola Bini"), 24, dt);
        assertLoad(expected, "--- !java/object:org.jvyamlb.TestBean\nname: Ola Bini\nage: 24\nborn: 1982-05-03\n");
    }

    // JRUBY-2754
    public void testJavaBeanLoadRefs() throws Exception {
        org.joda.time.DateTime dt = new org.joda.time.DateTime(1982,5,3,0,0,0,0);
        final TestBean bean = new TestBean(s("Ola Bini"), 24, dt);
        List result = (List)YAML.load(s("--- \n- &id001 !java/object:org.jvyamlb.TestBean\n  name: Ola Bini\n  age: 24\n  born: 1982-05-03\n- *id001"));
        assertEquals(bean, result.get(0));
        assertSame(result.get(0), result.get(1));
    }

    public void testFlowSequenceWithFlowMappingLoad() throws Exception {
        List expectedOuter = new ArrayList();
        Map expectedInner = new HashMap();

        expectedInner.put(s("a"), s("b"));
        expectedOuter.add(expectedInner);

        assertLoad(expectedOuter, "[{a: b}]");
    }

    public void testNestedFlowMappingLoad() throws Exception {
        Map expectedInner = new HashMap();
        expectedInner.put(s("b"), s("c"));
      
        Map expectedOuter = new HashMap();
        expectedOuter.put(s("a"), expectedInner);

        assertLoad(expectedOuter, "{a: {b: c}}");
    }

    public void testNestedFlowSequenceFlowMappingLoad() throws Exception {
        List expectedOuterList = new ArrayList();

        Map expectedInnerMap = new HashMap();
        expectedInnerMap.put(s("b"), s("c"));
        Map expectedOuterMap = new HashMap();
        expectedOuterMap.put(s("a"), expectedInnerMap);

        expectedOuterList.add(expectedOuterMap);

        assertLoad(expectedOuterList, "[{a: {b: c}}]");
    }
}// YAMLLoadTest
