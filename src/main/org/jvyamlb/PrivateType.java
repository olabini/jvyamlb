/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PrivateType {
    private String tag;
    private Object value;

    public PrivateType(final String tag, final Object value) {
        this.tag = tag;
        this.value = value;
    }

    public String toString() {
        return "#<PrivateType tag: " + tag + " value: " + value + ">";
    }
}// PrivateType
