/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.nodes;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class Node {
    private String tag;
    private Object value;
    private int hash = -1;
    private Object constructed;

    public Node(final String tag, final Object value) {
        this.tag = tag;
        this.value = value;
    }

    public Object getConstructed() {
        return constructed;
    }

    public void setConstructed(Object constructed) {
        this.constructed = constructed;
    }

    public String getTag() {
        return this.tag;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object v) {
        this.value = v;
    }

    public int hashCode() {
        if(hash == -1) {
            hash = 3;
            hash += (null == tag) ? 0 : 3*tag.hashCode();
            hash += (null == value) ? 0 : 3*value.hashCode();
        }
        return hash;
    }

    public boolean equals(final Object oth) {
        if(oth instanceof Node) {
            final Node nod = (Node)oth;
            return ((this.tag != null) ? this.tag.equals(nod.tag) : nod.tag == null) && 
                ((this.value != null) ? this.value.equals(nod.value) : nod.value == null);
        }
        return false;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " (tag=" + getTag() + ", value=" + getValue()+")>";
    }
}// Node
