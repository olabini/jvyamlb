/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class YAMLDumpTest extends YAMLTestCase {
    public YAMLDumpTest(final String name) {
        super(name);
    }

    public void testBasicStringDump() {
        assertEquals(ByteList.create("--- str\n"), YAML.dump(ByteList.create("str")));
    }

    public void testBasicHashDump() {
        Map ex = new HashMap();
        ex.put(ByteList.create("a"),ByteList.create("b"));
        assertEquals(ByteList.create("--- \na: b\n"), YAML.dump(ex));
    }

    public void testBasicListDump() {
        List ex = new ArrayList();
        ex.add(ByteList.create("a"));
        ex.add(ByteList.create("b"));
        ex.add(ByteList.create("c"));
        assertEquals(ByteList.create("--- \n- a\n- b\n- c\n"), YAML.dump(ex));
    }

    public void testVersionDumps() {
        assertEquals(ByteList.create("--- !!int 1\n"), YAML.dump(new Integer(1),YAML.config().explicitTypes(true)));
        assertEquals(ByteList.create("--- !int 1\n"), YAML.dump(new Integer(1),YAML.config().version("1.0").explicitTypes(true)));
    }

    public void testMoreScalars() {
        assertEquals(ByteList.create("--- \"1.0\"\n"), YAML.dump(ByteList.create("1.0")));
    }

    public void testPrefersQuotesToExplicitTag() {
        assertEquals(ByteList.create("--- \"30\"\n"), YAML.dump(ByteList.create("30")));
    }

    public void testEmptyList() {
        assertEquals(ByteList.create("--- []\n\n"), YAML.dump(new ArrayList()));
    }

    public void testEmptyMap() {
        assertEquals(ByteList.create("--- {}\n\n"), YAML.dump(new HashMap()));
    }

    public void testEmptyListAsKey() {
        Map m = new HashMap();
        m.put(new ArrayList(), "");
        assertEquals(ByteList.create("--- \n? []\n\n: \"\"\n"), YAML.dump(m));
    }

    public void testEmptyMapAsKey() {
        Map m = new HashMap();
        m.put(new HashMap(), "");
        assertEquals(ByteList.create("--- \n? {}\n\n: \"\"\n"), YAML.dump(m));
    }

    public void testDumpJavaBean() {
        final TestBean2 toDump = new TestBean2(ByteList.create("Ola Bini"), 24);
        Object v = YAML.dump(toDump);
        String s = v.toString();
        assertTrue("something is wrong with: \"" + v + "\"",
ByteList.create("--- !java/object:org.jvyamlb.TestBean2 \nname: Ola Bini\nage: 24\n").equals(v) ||
ByteList.create("--- !java/object:org.jvyamlb.TestBean2 \nage: 24\nname: Ola Bini\n").equals(v)
                   );
    }
}// YAMLDumpTest
