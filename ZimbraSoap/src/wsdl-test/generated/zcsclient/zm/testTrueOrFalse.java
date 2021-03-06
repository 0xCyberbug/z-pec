
package generated.zcsclient.zm;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for trueOrFalse.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="trueOrFalse">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="true"/>
 *     &lt;enumeration value="false"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "trueOrFalse")
@XmlEnum
public enum testTrueOrFalse {

    @XmlEnumValue("true")
    TRUE("true"),
    @XmlEnumValue("false")
    FALSE("false");
    private final String value;

    testTrueOrFalse(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static testTrueOrFalse fromValue(String v) {
        for (testTrueOrFalse c: testTrueOrFalse.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
