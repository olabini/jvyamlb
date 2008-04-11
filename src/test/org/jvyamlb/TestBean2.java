/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class TestBean2 {
    private ByteList name;
    private int age;

    public TestBean2() {
    }

    public TestBean2(final ByteList name, final int age) {
        this.name = name;
        this.age = age;
    }
    
    public ByteList getName() {
        return this.name;
    }

    public int getAge() {
        return age;
    }

    public void setName(final ByteList name) {
        this.name = name;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public boolean equals(final Object other) {
        boolean ret = this == other;
        if(!ret && other instanceof TestBean2) {
            TestBean2 o = (TestBean2)other;
            ret = 
                this.name == null ? o.name == null : this.name.equals(o.name) &&
                this.age == o.age;
        }
        return ret;
    }

    public int hashCode() {
        int val = 3;
        val += 3 * (name == null ? 0 : name.hashCode());
        val += 3 * age;
        return val;
    }

    public String toString() {
        return "#<org.jvyamlb.TestBean2 name=\"" + name + "\" age=" + age + ">";
    }
}// TestBean2
