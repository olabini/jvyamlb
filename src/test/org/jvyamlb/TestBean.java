/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import org.joda.time.DateTime;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class TestBean {
    private ByteList name;
    private int age;
    private DateTime born;

    public TestBean() {
    }

    public TestBean(final ByteList name, final int age, final DateTime born) {
        this.name = name;
        this.age = age;
        this.born = born;
    }
    
    public ByteList getName() {
        return this.name;
    }

    public int getAge() {
        return age;
    }

    public DateTime getBorn() {
        return born;
    }

    public void setName(final ByteList name) {
        this.name = name;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public void setBorn(final DateTime born) {
        this.born = born;
    }

    public boolean equals(final Object other) {
        boolean ret = this == other;
        if(!ret && other instanceof TestBean) {
            TestBean o = (TestBean)other;
            ret = 
                this.name == null ? o.name == null : this.name.equals(o.name) &&
                this.age == o.age &&
                this.born == null ? o.born == null : this.born.equals(o.born);
        }
        return ret;
    }

    public int hashCode() {
        int val = 3;
        val += 3 * (name == null ? 0 : name.hashCode());
        val += 3 * age;
        val += 3 * (born == null ? 0 : born.hashCode());
        return val;
    }

    public String toString() {
        return "#<org.jvyamlb.TestBean name=\"" + name + "\" age=" + age + " born=\"" + born + "\">";
    }
}// TestBean
