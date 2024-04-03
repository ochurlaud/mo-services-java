/**
 * 
 */
package esa.mo.mal.encoder.http.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;

import org.ccsds.moims.mo.mal.MALContextFactory;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALHelper;
import org.ccsds.moims.mo.mal.structures.Blob;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.InteractionType;
import org.ccsds.moims.mo.mal.structures.QoSLevel;
import org.ccsds.moims.mo.mal.structures.Subscription;
import org.ccsds.moims.mo.mal.structures.SubscriptionFilter;
import org.ccsds.moims.mo.mal.structures.UInteger;
import org.ccsds.moims.mo.mal.structures.ULong;
import org.ccsds.moims.mo.mal.structures.UOctet;
import org.ccsds.moims.mo.mal.structures.UShort;
import org.ccsds.moims.mo.mal.structures.Union;
import org.ccsds.moims.mo.mal.structures.UpdateHeader;
import org.ccsds.moims.mo.mal.structures.UpdateHeaderList;
import org.ccsds.moims.mo.malprototype.MALPrototypeHelper;
import org.ccsds.moims.mo.malprototype.structures.IPTestDefinition;
import org.ccsds.moims.mo.malprototype.structures.TestPublishRegister;
import org.ccsds.moims.mo.malprototype.structures.TestPublishUpdate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import esa.mo.mal.encoder.http.HTTPXMLElementInputStream;

/**
 * @author rvangijlswijk
 *
 */
public class HTTPXMLElementInputTest {

  XMLTestHelper helper = new XMLTestHelper();

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    MALContextFactory.getElementsRegistry().registerElementsForArea(MALHelper.MAL_AREA);
    MALContextFactory.getElementsRegistry().registerElementsForArea(MALPrototypeHelper.MALPROTOTYPE_AREA);
  }

  @Test
  public void testDecodeRegisterMessage() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<Subscription malxml:type=\"281475027043305\">"
          + "<Identifier malxml:type=\"281475027042310\">"
            + "<Identifier>Demo</Identifier>"
          + "</Identifier>"
          + "<IdentifierList malxml:type=\"281475010265082\">"
            + "<Identifier>Test</Identifier>"
            + "<Identifier>Domain0</Identifier>"
          + "</IdentifierList>"
          + "<IdentifierList malxml:type=\"281475010265082\">"
            + "<Identifier>SomeKey</Identifier>"
          + "</IdentifierList>"
          + "<SubscriptionFilterList malxml:type=\"281475010264086\">"
            + "<SubscriptionFilter malxml:type=\"281474993488874\">"
              + "<Identifier malxml:type=\"281475027042310\">"
                + "<Identifier>TestFilter</Identifier>"
              + "</Identifier>"
              + "<AttributeList>"
                + "<Blob malxml:type=\"281475027042305\"><Blob>5465737456616c7565</Blob></Blob>"
                + "<Union malxml:type=\"281475027042306\"><Boolean>true</Boolean></Union>"
              + "</AttributeList>"
            + "</SubscriptionFilter>"
          + "</SubscriptionFilterList>"
        + "</Subscription>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    Subscription subscription = (Subscription) eis.readElement(new Subscription(), null);

    assertNotNull(subscription);

    // Subscription Id
    assertEquals("Demo", subscription.getSubscriptionId().getValue());

    // Domain
    assertEquals(2, subscription.getDomain().size());
    assertEquals("Test", subscription.getDomain().get(0).getValue());
    assertEquals("Domain0", subscription.getDomain().get(1).getValue());

    // Subscription filter list
    assertEquals(1, subscription.getFilters().size());
    SubscriptionFilter subscriptionFilter = subscription.getFilters().get(0);
    assertEquals("TestFilter", subscriptionFilter.getName().getValue());
    assertEquals(2, subscriptionFilter.getValues().size());
    assertArrayEquals("TestValue".getBytes(), ((Blob) subscriptionFilter.getValues().get(0)).getValue());
    assertEquals(true, ((Union) subscriptionFilter.getValues().get(1)).getBooleanValue());
  }

  @Test
  public void testDecodeNotifyMessage() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<Identifier><Identifier>Demo</Identifier></Identifier>"
        + "<UpdateHeaderList>"
          + "<UpdateHeader>"
            + "<source><Identifier>SomeURI</Identifier></source>"
            + "<domain xsi:nil=\"true\" />"
            + "<keyValues xsi:nil=\"true\" />"
          + "</UpdateHeader>"
        + "</UpdateHeaderList>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    Identifier id = (Identifier) eis.readElement(new Identifier(), null);
    UpdateHeaderList uhl = (UpdateHeaderList) eis.readElement(new UpdateHeaderList(), null);

    assertNotNull(id);
    assertEquals("Demo", id.getValue());

    assertNotNull(uhl);
    assertEquals(1, uhl.size());
    assertNotNull(uhl.get(0));
    UpdateHeader uh = uhl.get(0);
    assertEquals("SomeURI", uh.getSource().getValue());
    assertNull(uh.getDomain());
    assertNull(uh.getKeyValues());
  }

  @Test
  public void testDecodeEmptyBody() throws MALException {

    // decode with non-null input stream without body content
    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    Identifier el = (Identifier) eis.readElement(new Identifier(), null);

    assertNull(el);

    // decode with 0-byte input stream
    InputStream bais2 = new ByteArrayInputStream(new byte[0]);
    HTTPXMLElementInputStream eis2 = new HTTPXMLElementInputStream(bais2);

    Object el2 = eis2.readElement(null, null);

    assertNull(el2);
  }

  @Test
  public void testDecodeMessageWithUnion() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<UInteger><UInteger>65549</UInteger></UInteger>"
        + "<Union><String></String></Union>"
        + "<Union><String>Foobar</String></Union>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    UInteger int1 = (UInteger) eis.readElement(new UInteger(), null);
    Union el = (Union) eis.readElement(new Union(""), null);
    Union el2 = (Union) eis.readElement(new Union(""), null);

    assertNotNull(int1);
    assertEquals(65549, int1.getValue());
    assertNotNull(el);
    assertEquals("", el.getStringValue());
    assertNotNull(el2);
    assertEquals("Foobar", el2.getStringValue());

  }

  @Test
  public void testDecodeIPTestDefinition() throws Exception {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<IPTestDefinition>"
          + "<procedureName><String>1</String></procedureName>"
          + "<consumerURI><URI>malhttp://172.28.16.54:56294/1259037869</URI></consumerURI>"
          + "<authenticationId><Blob>0001</Blob></authenticationId>"
          + "<Qos><UOctet>0</UOctet></Qos>"
          + "<Priority><UInteger>1</UInteger></Priority>"
          + "<Domain>"
            + "<Identifier><Identifier>Test</Identifier></Identifier>"
            + "<Identifier><Identifier>Domain0</Identifier></Identifier>"
          + "</Domain>"
          + "<networkZone><Identifier>NetworkZone</Identifier></networkZone>"
          + "<Session><UOctet>1</UOctet></Session>"
          + "<sessionName><Identifier>S1</Identifier></sessionName>"
          + "<supplements></supplements>"
          + "<transitions>"
            + "<IPTestTransition>"
              + "<Type><UOctet>0</UOctet></Type>"
              + "<errorCode xsi:nil=\"true\"/>"
            + "</IPTestTransition>"
          + "</transitions>"
          + "<timestamp><Time>2017-12-06T12:40:21.597</Time></timestamp>"
        + "</IPTestDefinition>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    IPTestDefinition iptest = (IPTestDefinition) eis.readElement(new IPTestDefinition(), null);

    assertNotNull(iptest);
    Node ipTestNode = helper.queryXPath(testXml, "/Body/IPTestDefinition").item(0);

    assertEquals("IPTestDefinition", ipTestNode.getNodeName());
    NodeList ipTestNodeChildren = helper.queryXPath(testXml, "/Body/IPTestDefinition/*");
    assertEquals(12, ipTestNodeChildren.getLength());
    assertNotNull(ipTestNodeChildren.item(0));
    assertEquals("procedureName", ipTestNodeChildren.item(0).getNodeName());
    assertNotNull(ipTestNodeChildren.item(1));
    assertEquals("consumerURI", ipTestNodeChildren.item(1).getNodeName());
    assertNotNull(ipTestNodeChildren.item(3));
    assertEquals("Qos", ipTestNodeChildren.item(3).getNodeName());
    assertNotNull(ipTestNodeChildren.item(3).getChildNodes().item(0));
    assertEquals("0", ipTestNodeChildren.item(3).getChildNodes().item(0).getTextContent());
    assertNotNull(ipTestNodeChildren.item(5));
    assertEquals("Domain", ipTestNodeChildren.item(5).getNodeName());
    assertNotNull(ipTestNodeChildren.item(8));
    assertEquals("sessionName", ipTestNodeChildren.item(8).getNodeName());
    assertNotNull(ipTestNodeChildren.item(9));
    assertEquals("supplements", ipTestNodeChildren.item(9).getNodeName());
    assertNotNull(ipTestNodeChildren.item(11));
    assertEquals("timestamp", ipTestNodeChildren.item(11).getNodeName());

  }

  @Test
  public void testDecodeUnionMessage() throws IllegalArgumentException, MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<UInteger><UInteger>999</UInteger></UInteger>"
        + "<Union><String>No error</String></Union>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    UInteger uint1 = (UInteger) eis.readElement(null, null);
    Union un1 = (Union) eis.readElement(null, null);

    assertNotNull(uint1);
    assertEquals(999, uint1.getValue());
    assertNotNull(un1);
    assertEquals("No error", un1.getStringValue());
  }

  @Test
  public void testDecodeComplexComposite() throws Exception {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<org.ccsds.moims.mo.malprototype.structures.TestPublishUpdate>"
          + "<org.ccsds.moims.mo.malprototype.structures.TestPublish>"
            + "<Qos malxml:type=\"\"><UOctet>1</UOctet></Qos>"
            + "<Priority malxml:type=\"\"><UInteger>1</UInteger></Priority>"
            + "<domain malxml:type=\"\">"
              + "<Identifier><Identifier>Test</Identifier></Identifier>"
              + "<Identifier><Identifier>Domain0</Identifier></Identifier>"
            + "</domain>"
            + "<networkZone type=\"\"><Identifier>NetworkZone</Identifier></networkZone>"
            + "<Session malxml:type=\"\"><UOctet>0</UOctet></Session>"
            + "<sessionName malxml:type=\"\"><Identifier>LIVE</Identifier></sessionName>"
            + "<testMultiType malxml:type=\"\"><Boolean>false</Boolean></testMultiType>"
          + "</org.ccsds.moims.mo.malprototype.structures.TestPublish>"
          + "<updateHeaders>"
            + "<UpdateHeader>"
              + "<source><Identifier></Identifier></source>"
              + "<domain xsi:nil=\"true\" />"
              + "<keyValues xsi:nil=\"true\" />"
            + "</UpdateHeader>"
          + "</updateHeaders>"
          + "<updates>"
            + "<TestUpdate>"
              + "<Counter><Integer>0</Integer></Counter>"
            + "</TestUpdate>"
          + "</updates>"
          + "<keyValues xsi:nil=\"true\" />"
          + "<errorCode><UInteger>65550</UInteger></errorCode>"
          + "<isException><Boolean>true</Boolean></isException>"
          + "<failedEntityKeys xsi:nil=\"true\" />"
        + "</org.ccsds.moims.mo.malprototype.structures.TestPublishUpdate>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    TestPublishUpdate publishUpdate = new TestPublishUpdate();
    publishUpdate = (TestPublishUpdate) eis.readElement(publishUpdate, null);

    assertNotNull(publishUpdate);
  }

  @Test
  public void testDecodeMultiPartMessage() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<UOctet><UOctet>255</UOctet></UOctet>"
        + "<UShort><UShort>65535</UShort></UShort>"
        + "<UInteger><UInteger>4294967295</UInteger></UInteger>"
        + "<ULong><ULong>18446744073709551615</ULong></ULong>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    UOctet uoct = (UOctet) eis.readElement(new UOctet(), null);
    UShort ushort = (UShort) eis.readElement(new UShort(), null);
    UInteger uint = (UInteger) eis.readElement(new UInteger(), null);
    ULong ulong = (ULong) eis.readElement(null, null);

    assertNotNull(uoct);
    assertNotNull(ushort);
    assertNotNull(uint);
    assertNotNull(ulong);

    assertEquals(new BigInteger("18446744073709551615"), ulong.getValue());
  }

  @Test
  public void testDecodeErrorMessageWithUnion() throws IllegalArgumentException, MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<UInteger><UInteger>70001</UInteger></UInteger>"
        + "<Union><String>18446744073709551615</String></Union>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    UInteger uint1 = (UInteger) eis.readElement(new UInteger(), null);
    Union union = (Union) eis.readElement(new Union(""), null);

    assertNotNull(uint1);
    assertNotNull(union);
    assertEquals(70001L, uint1.getValue());
    assertEquals("18446744073709551615", union.getStringValue());
  }

  @Test
  public void testDecodeEnumeration() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<InteractionType malxml:type=\"281475027042405\"><InteractionType>SEND</InteractionType></InteractionType>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    InteractionType it = (InteractionType) eis.readElement(null, null);

    assertNotNull(it);
    assertEquals(0, it.getOrdinal());
  }

  @Test
  public void testDecodeUnionString() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<UInteger malxml:type=\"281475027042316\"><UInteger>65549</UInteger></UInteger>"
        + "<Union malxml:type=\"281475027042319\"><String>4294967295</String></Union>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    UInteger uint = (UInteger) eis.readElement(null, null);
    Union union = (Union) eis.readElement(null, null);

    assertNotNull(uint);
    assertEquals(65549, uint.getValue());
    assertNotNull(union);
    assertEquals("4294967295", union.getStringValue());
  }

  @Test
  public void testDecodeUnionBoolean() throws MALException {

    String testXml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<Union malxml:type=\"281475027042306\"><Boolean>true</Boolean></Union>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    Union union = (Union) eis.readElement(null, null);

    assertTrue(union.getBooleanValue());
  }

  @Test
  public void testDecodeQoS() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<Qos malxml:type=\"281475027042407\"><Qos>QUEUED</Qos></Qos>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    QoSLevel qos = (QoSLevel) eis.readElement(null, null);

    assertNotNull(qos);
    assertEquals(QoSLevel.QUEUED, qos);
  }

  /**
   * Caused a numberformatexception while decoding value uint 4294967295 as a short.
   * @throws MALException
   */
  @Test
  public void testDecodePublishRegister() throws MALException {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<TestPublishRegister malxml:type=\"28147497687842823\">"
          + "<org.ccsds.moims.mo.malprototype.structures.TestPublish malxml:type=\"28147497687842823\">"
            + "<Qos malxml:type=\"281475027042407\"><Qos>QUEUED</Qos></Qos>"
            + "<Priority malxml:type=\"281474993487884\"><UInteger>4294967295</UInteger></Priority>"
            + "<domain malxml:type=\"281475010265082\">"
              + "<Identifier malxml:type=\"281475027042310\"><Identifier>Identifier test</Identifier></Identifier>"
              + "<Identifier malxml:type=\"281475027042310\"><Identifier>Identifier test</Identifier></Identifier>"
              + "<Identifier malxml:type=\"281475027042310\"><Identifier>Identifier test</Identifier></Identifier>"
              + "<Identifier malxml:type=\"281475027042310\"><Identifier>Identifier test</Identifier></Identifier>"
            + "</domain>"
            + "<networkZone malxml:type=\"281475027042310\"><Identifier>Identifier test</Identifier></networkZone>"
            + "<Session malxml:type=\"281474993487892\"><UOctet>1</UOctet></Session>"
            + "<sessionName malxml:type=\"281475027042310\"><Identifier>Identifier test</Identifier></sessionName>"
            + "<testMultiType><Boolean>false</Boolean></testMultiType>"
          + "</org.ccsds.moims.mo.malprototype.structures.TestPublish>"
          + "<keyNames xsi:nil=\"true\"/>"
          + "<keyTypes xsi:nil=\"true\"/>"
          + "<errorCode malxml:type=\"281474993487884\"><UInteger>4294967295</UInteger></errorCode>"
        + "</TestPublishRegister>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    TestPublishRegister register = (TestPublishRegister) eis.readElement(null, null);

    assertNotNull(register);
    assertEquals(QoSLevel.QUEUED, register.getQos());
    assertEquals(new UInteger(4294967295L), register.getPriority());
  }

  @Test
  public void testNasaEncodedMessage() throws Exception {

    String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<malxml:Body xmlns:malxml=\"http://www.ccsds.org/schema/malxml/MAL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
        + "<TestPublishRegister malxml:type=\"28147497687842823\">"
          + "<QoSLevel><QoSLevel>QUEUED</QoSLevel></QoSLevel>"
          + "<UInteger><UInteger>4294967295</UInteger></UInteger>"
          + "<IdentifierList>"
            + "<Identifier><Identifier>Identifier test</Identifier></Identifier>"
            + "<Identifier><Identifier>Identifier test</Identifier></Identifier>"
            + "<Identifier><Identifier>Identifier test</Identifier></Identifier>"
            + "<Identifier><Identifier>Identifier test</Identifier></Identifier>"
          + "</IdentifierList>"
          + "<Identifier><Identifier>Identifier test</Identifier></Identifier>"
          + "<SessionType><SessionType>SIMULATION</SessionType></SessionType>"
          + "<Identifier><Identifier>Identifier test</Identifier></Identifier>"
          + "<Boolean><Boolean>false</Boolean></Boolean>"
          + "<Element xsi:nil=\"true\"/>"
          + "<Element xsi:nil=\"true\"/>"
          + "<UInteger><UInteger>4294967295</UInteger></UInteger>"
        + "</TestPublishRegister>"
      + "</malxml:Body>";

    helper.assertAgainstSchema(testXml);

    InputStream bais = new ByteArrayInputStream(testXml.getBytes());
    HTTPXMLElementInputStream eis = new HTTPXMLElementInputStream(bais);

    TestPublishRegister register = (TestPublishRegister) eis.readElement(null, null);

    assertNotNull(register);
    assertEquals(QoSLevel.QUEUED, register.getQos());
    assertEquals(new UInteger(4294967295L), register.getPriority());
  }
}
