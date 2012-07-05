
package org.cip4.lib.xjdf.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for or complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="or">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.CIP4.org/JDFSchema_2_0}TermType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{http://www.CIP4.org/JDFSchema_2_0}TermType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "or", propOrder = {
    "termTypes"
})
public class Or
    extends TermType
    implements Serializable
{

    @XmlElementRef(name = "TermType", namespace = "http://www.CIP4.org/JDFSchema_2_0", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends TermType>> termTypes;

    /**
     * Gets the value of the termTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the termTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTermTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link DurationEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link MatrixEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link EnumEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link NumberEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link DateTimeEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link XYPairEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link TermType }{@code >}
     * {@link JAXBElement }{@code <}{@link StringEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link IsPresentEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link IntegerEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link Or }{@code >}
     * {@link JAXBElement }{@code <}{@link Test }{@code >}
     * {@link JAXBElement }{@code <}{@link And }{@code >}
     * {@link JAXBElement }{@code <}{@link RectangleEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link Xor }{@code >}
     * {@link JAXBElement }{@code <}{@link Not }{@code >}
     * {@link JAXBElement }{@code <}{@link ShapeEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link TestRef }{@code >}
     * {@link JAXBElement }{@code <}{@link NameEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link BooleanEvaluation }{@code >}
     * {@link JAXBElement }{@code <}{@link PDFPathEvaluation }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends TermType>> getTermTypes() {
        if (termTypes == null) {
            termTypes = new ArrayList<JAXBElement<? extends TermType>>();
        }
        return this.termTypes;
    }

}