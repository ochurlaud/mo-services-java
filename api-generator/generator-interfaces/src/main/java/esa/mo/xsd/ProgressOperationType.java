//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.02.15 at 03:22:23 PM CET 
//


package esa.mo.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Element represents a PROGRESS operation.
 * 
 * <p>Java class for ProgressOperationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProgressOperationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ccsds.org/schema/ServiceSchema}OperationType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="messages"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="progress" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
 *                   &lt;element name="acknowledgement" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
 *                   &lt;element name="update" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
 *                   &lt;element name="response" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="errors" type="{http://www.ccsds.org/schema/ServiceSchema}OperationErrorList" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProgressOperationType", namespace = "http://www.ccsds.org/schema/ServiceSchema", propOrder = {
    "messages",
    "errors"
})
public class ProgressOperationType
    extends OperationType
{

    @XmlElement(namespace = "http://www.ccsds.org/schema/ServiceSchema", required = true)
    protected ProgressOperationType.Messages messages;
    @XmlElement(namespace = "http://www.ccsds.org/schema/ServiceSchema")
    protected OperationErrorList errors;

    /**
     * Gets the value of the messages property.
     * 
     * @return
     *     possible object is
     *     {@link ProgressOperationType.Messages }
     *     
     */
    public ProgressOperationType.Messages getMessages() {
        return messages;
    }

    /**
     * Sets the value of the messages property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProgressOperationType.Messages }
     *     
     */
    public void setMessages(ProgressOperationType.Messages value) {
        this.messages = value;
    }

    /**
     * Gets the value of the errors property.
     * 
     * @return
     *     possible object is
     *     {@link OperationErrorList }
     *     
     */
    public OperationErrorList getErrors() {
        return errors;
    }

    /**
     * Sets the value of the errors property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationErrorList }
     *     
     */
    public void setErrors(OperationErrorList value) {
        this.errors = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="progress" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
     *         &lt;element name="acknowledgement" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
     *         &lt;element name="update" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
     *         &lt;element name="response" type="{http://www.ccsds.org/schema/ServiceSchema}AnyTypeReference"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progress",
        "acknowledgement",
        "update",
        "response"
    })
    public static class Messages {

        @XmlElement(namespace = "http://www.ccsds.org/schema/ServiceSchema", required = true)
        protected AnyTypeReference progress;
        @XmlElement(namespace = "http://www.ccsds.org/schema/ServiceSchema", required = true)
        protected AnyTypeReference acknowledgement;
        @XmlElement(namespace = "http://www.ccsds.org/schema/ServiceSchema", required = true)
        protected AnyTypeReference update;
        @XmlElement(namespace = "http://www.ccsds.org/schema/ServiceSchema", required = true)
        protected AnyTypeReference response;

        /**
         * Gets the value of the progress property.
         * 
         * @return
         *     possible object is
         *     {@link AnyTypeReference }
         *     
         */
        public AnyTypeReference getProgress() {
            return progress;
        }

        /**
         * Sets the value of the progress property.
         * 
         * @param value
         *     allowed object is
         *     {@link AnyTypeReference }
         *     
         */
        public void setProgress(AnyTypeReference value) {
            this.progress = value;
        }

        /**
         * Gets the value of the acknowledgement property.
         * 
         * @return
         *     possible object is
         *     {@link AnyTypeReference }
         *     
         */
        public AnyTypeReference getAcknowledgement() {
            return acknowledgement;
        }

        /**
         * Sets the value of the acknowledgement property.
         * 
         * @param value
         *     allowed object is
         *     {@link AnyTypeReference }
         *     
         */
        public void setAcknowledgement(AnyTypeReference value) {
            this.acknowledgement = value;
        }

        /**
         * Gets the value of the update property.
         * 
         * @return
         *     possible object is
         *     {@link AnyTypeReference }
         *     
         */
        public AnyTypeReference getUpdate() {
            return update;
        }

        /**
         * Sets the value of the update property.
         * 
         * @param value
         *     allowed object is
         *     {@link AnyTypeReference }
         *     
         */
        public void setUpdate(AnyTypeReference value) {
            this.update = value;
        }

        /**
         * Gets the value of the response property.
         * 
         * @return
         *     possible object is
         *     {@link AnyTypeReference }
         *     
         */
        public AnyTypeReference getResponse() {
            return response;
        }

        /**
         * Sets the value of the response property.
         * 
         * @param value
         *     allowed object is
         *     {@link AnyTypeReference }
         *     
         */
        public void setResponse(AnyTypeReference value) {
            this.response = value;
        }

    }

}
