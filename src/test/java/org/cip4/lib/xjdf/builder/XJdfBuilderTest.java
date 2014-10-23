/**
 * All rights reserved by
 * 
 * flyeralarm GmbH
 * Alfred-Nobel-Straße 18
 * 97080 Würzburg
 *
 * info@flyeralarm.com
 * http://www.flyeralarm.com
 */
package org.cip4.lib.xjdf.builder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cip4.lib.xjdf.XJdfNodeFactory;
import org.cip4.lib.xjdf.schema.Created;
import org.cip4.lib.xjdf.schema.FileSpec;
import org.cip4.lib.xjdf.schema.GeneralID;
import org.cip4.lib.xjdf.schema.Media;
import org.cip4.lib.xjdf.schema.NodeInfo;
import org.cip4.lib.xjdf.schema.ParameterType;
import org.cip4.lib.xjdf.schema.Part;
import org.cip4.lib.xjdf.schema.PhaseTime;
import org.cip4.lib.xjdf.schema.Product;
import org.cip4.lib.xjdf.schema.Resource;
import org.cip4.lib.xjdf.schema.ResourceType;
import org.cip4.lib.xjdf.schema.RunList;
import org.cip4.lib.xjdf.schema.XJDF;
import org.cip4.lib.xjdf.type.DateTime;
import org.cip4.lib.xjdf.xml.XJdfParser;
import org.cip4.lib.xjdf.xml.internal.JAXBContextFactory;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test case for XJdfBuilder.
 * @author s.meissner
 * @date 29.02.2012
 */
public class XJdfBuilderTest extends AbstractBuilderTest<XJDF> {

	private final String RES_XJDF_GANGJOB = "/org/cip4/lib/xjdf/GangJob.xjdf";

	private final String RES_XJDF_RUNLIST = "/org/cip4/lib/xjdf/builder/runlist.xjdf";

	private XJdfBuilder xJdfBuilder;

	/**
	 * Setup unit test.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		xJdfBuilder = new XJdfBuilder();

		JAXBContextFactory.init();
	}

	/**
	 * Tear down unit test.
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		xJdfBuilder = null;
	}

	/**
	 * Add one Audit Parameter to XJDF Document.
	 * @throws Exception
	 */
	@Test
	public void testAddAudit() throws Exception {

		// arrange
		final String AGENT_NAME = "XJDF Agent";
		final String AGENT_VERSION = "2.0.4";
		final DateTime TIMESTAMP = new DateTime("2013-06-11T23:27:26+02:00");

		// act
		Created created = new XJdfNodeFactory().createCreated(AGENT_NAME, AGENT_VERSION, TIMESTAMP);
		xJdfBuilder.addAudit(created);

		PhaseTime phaseTime = new XJdfNodeFactory().createPhaseTime();
		phaseTime.setStart(new DateTime());
		xJdfBuilder.addAudit(phaseTime);

		// assert
		byte[] bytes = marsahlResult(xJdfBuilder);

		String actualAgentName = getXPathValue(bytes, "/xjdf:XJDF/xjdf:AuditPool/xjdf:Created/@AgentName");
		Assert.assertEquals("AgentName in Created is wrong.", AGENT_NAME, actualAgentName);

		String actualAgentVersion = getXPathValue(bytes, "/xjdf:XJDF/xjdf:AuditPool/xjdf:Created/@AgentVersion");
		Assert.assertEquals("AgentVersion in Created is wrong.", AGENT_VERSION, actualAgentVersion);

		String actualTimeStamp = getXPathValue(bytes, "/xjdf:XJDF/xjdf:AuditPool/xjdf:Created/@TimeStamp");
		Assert.assertEquals("TimeStamp in Created is wrong.", TIMESTAMP, new DateTime(actualTimeStamp));
	}

	/**
	 * Add one GeneralID Parameter to XJDF document.
	 * @throws Exception
	 */
	@Test
	public void testAddGeneralIDParams() throws Exception {
		// arrange
		final String ID_USAGE = UUID.randomUUID().toString();
		final String ID_VALUE = UUID.randomUUID().toString();

		// act
		GeneralID generalId = new XJdfNodeFactory().createGeneralID(ID_USAGE, ID_VALUE);
		xJdfBuilder.addGeneralID(generalId);

		// assert
		byte[] bytes = marsahlResult(xJdfBuilder);

		String actualIDUsage = getXPathValue(bytes, "/xjdf:XJDF/xjdf:GeneralID/@IDUsage");
		Assert.assertEquals("IDUsage in GeneralID is wrong.", ID_USAGE, actualIDUsage);

		String actualIDValue = getXPathValue(bytes, "/xjdf:XJDF/xjdf:GeneralID/@IDValue");
		Assert.assertEquals("IDValue in GeneralID is wrong.", ID_VALUE, actualIDValue);

	}

	/**
	 * Add Multiple GeneralID Parameter to XJDF document.
	 * @throws Exception
	 */
	@Test
	public void testAddGeneralIDParamsMultiple() throws Exception {
		// arrange
		final String ID_USAGE_1 = UUID.randomUUID().toString();
		final String ID_VALUE_1 = UUID.randomUUID().toString();

		final String ID_USAGE_2 = UUID.randomUUID().toString();
		final String ID_VALUE_2 = UUID.randomUUID().toString();

		final String ID_VALUE_3 = "";

		// act
		xJdfBuilder.addGeneralID(new XJdfNodeFactory().createGeneralID(ID_USAGE_1, ID_VALUE_1));
		xJdfBuilder.addGeneralID(new XJdfNodeFactory().createGeneralID(ID_USAGE_2, ID_VALUE_2));

		// assert
		byte[] bytes = marsahlResult(xJdfBuilder);

		String actualIDUsage_1 = getXPathValue(bytes, "/xjdf:XJDF/xjdf:GeneralID[1]/@IDUsage");
		Assert.assertEquals("IDUsage in GeneralID is wrong.", ID_USAGE_1, actualIDUsage_1);

		String actualIDValue_1 = getXPathValue(bytes, "/xjdf:XJDF/xjdf:GeneralID[1]/@IDValue");
		Assert.assertEquals("IDValue in GeneralID is wrong.", ID_VALUE_1, actualIDValue_1);

		String actualIDUsage_2 = getXPathValue(bytes, "/xjdf:XJDF/xjdf:GeneralID[2]/@IDUsage");
		Assert.assertEquals("IDUsage in GeneralID is wrong.", ID_USAGE_2, actualIDUsage_2);

		String actualIDValue_2 = getXPathValue(bytes, "/xjdf:XJDF/xjdf:GeneralID[2]/@IDValue");
		Assert.assertEquals("IDValue in GeneralID is wrong.", ID_VALUE_2, actualIDValue_2);

		String actualIDValue_3 = getXPathValue(bytes, "/xjdf:XJDF/xjdf:GeneralID[3]/@IDValue");
		Assert.assertEquals("IDValue in GeneralID is wrong.", ID_VALUE_3, actualIDValue_3);

	}

	/**
	 * Add Parameter "Product" to XJDF document.
	 * @throws Exception
	 */
	@Test
	public void testAddProductSimple() throws Exception {
		// arrange
		Product product = new XJdfNodeFactory().createProduct();
		product.setAmount(1000);

		// act
		xJdfBuilder.addProduct(product);

		// assert
		byte[] bytes = marsahlResult(xJdfBuilder);

	}

	/**
	 * Add Parameter "RunList" to XJDF document.
	 * @throws Exception
	 */
	@Test
	public void testAddParameterRunList() throws Exception {

		// arrange
		final String URL = "http://www.example.org/w2p/Cover.pdf";

		FileSpec fileSpec = new XJdfNodeFactory().createFileSpec();
		fileSpec.setURL(URL);

		RunList runList = new XJdfNodeFactory().createRunList();
		runList.setFileSpec(fileSpec);

		Part partCover = new XJdfNodeFactory().createPart();
		partCover.setRun("Cover");

		Part partContent = new XJdfNodeFactory().createPart();
		partContent.setRun("Content");

		// act
		xJdfBuilder.addParameter(runList, partCover);

		// assert
		byte[] bytes = marsahlResult(xJdfBuilder);

		String actualName = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet/@Name");
		Assert.assertEquals("Name in ParameterSet is wrong.", "RunList", actualName);

		String actualPartRun = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet/xjdf:Parameter/xjdf:Part/@Run");
		Assert.assertEquals("RunTag in RunList is wrong.", "Cover", actualPartRun);

		String actualUrl = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet/xjdf:Parameter/xjdf:RunList/xjdf:FileSpec/@URL");
		Assert.assertEquals("RunTag in RunList is wrong.", URL, actualUrl);
	}

	/**
	 * Add Multiple Parameter o XJDF document.
	 * @throws Exception
	 */
	@Test
	public void testAddParameterMultiple() throws Exception {

		// arrange
		final String urlContent = "http://www.example.org/w2p/Content.pdf";
		final String urlCover = "http://www.example.org/w2p/Cover.pdf";

		RunList runListCover = new XJdfNodeFactory().createRunList(urlCover);
		RunList runListContent = new XJdfNodeFactory().createRunList(urlContent);

		Part partCover = new XJdfNodeFactory().createPart();
		partCover.setRun("Cover");

		Part partContent = new XJdfNodeFactory().createPart();
		partContent.setRun("Content");

		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setEnd(new DateTime());

		// act
		xJdfBuilder.addParameter(runListCover, partCover);
		xJdfBuilder.addParameter(runListContent, partContent);
		xJdfBuilder.addParameter(nodeInfo);

		// assert
		byte[] bytes = marsahlResult(xJdfBuilder);

		String actualName = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[2]/@Name");
		Assert.assertEquals("Name in ParameterSet is wrong.", "RunList", actualName);

	}

	/**
	 * Add Multiple Parameter o XJDF document.
	 * @throws Exception
	 */
	@Test
	public void testAddParameterList() throws Exception {

		// arrange
		final String urlCover = "http://www.example.org/w2p/Cover.pdf";
		final String urlContent_1 = "http://www.example.org/w2p/Content_1.pdf";
		final String urlContent_2 = "http://www.example.org/w2p/Content_2.pdf";

		List<ParameterType> runLists = new ArrayList<ParameterType>();

		runLists.add(new XJdfNodeFactory().createRunList(urlCover));
		runLists.add(new XJdfNodeFactory().createRunList(urlContent_1));
		runLists.add(new XJdfNodeFactory().createRunList(urlContent_2));

		// act
		xJdfBuilder.addParameter(runLists);

		// assert
		String actual;

		byte[] bytes = marsahlResult(xJdfBuilder);

		actual = getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet)");
		Assert.assertEquals("Number of ParameterSet nodes is wrong.", "1", actual);

		actual = getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet/xjdf:Parameter)");
		Assert.assertEquals("Number of RunList parameters is wrong.", "3", actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet/@ProcessUsage");
		Assert.assertEquals("Value Process Usage is wrong.", "", actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet/@Name");
		Assert.assertEquals("Attribute Name in ParameterSet is wrong.", "RunList", actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[@Name='RunList']/xjdf:Parameter[1]/xjdf:RunList/xjdf:FileSpec/@URL");
		Assert.assertEquals("Attribute FileSpec URL is wrong.", urlCover, actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[@Name='RunList']/xjdf:Parameter[2]/xjdf:RunList/xjdf:FileSpec/@URL");
		Assert.assertEquals("Attribute FileSpec URL is wrong.", urlContent_1, actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[@Name='RunList']/xjdf:Parameter[3]/xjdf:RunList/xjdf:FileSpec/@URL");
		Assert.assertEquals("Attribute FileSpec URL is wrong.", urlContent_2, actual);
	}

	/**
	 * Add Multiple Parameter o XJDF document.
	 * @throws Exception
	 */
	@Test
	public void testAddParameterListProcessUsage() throws Exception {

		// arrange
		XJdfNodeFactory nf = new XJdfNodeFactory();

		final String urlCover = "http://www.example.org/w2p/Cover.pdf";
		final String urlContent_1 = "http://www.example.org/w2p/Content_1.pdf";
		final String urlContent_2 = "http://www.example.org/w2p/Content_2.pdf";
		String processUsage_1 = "PROCESS_USAGE_1";
		String processUsage_2 = "PROCESS_USAGE_2";

		List<ParameterType> runList_1 = new ArrayList<ParameterType>();
		runList_1.add(nf.createRunList(urlCover));

		List<ParameterType> runList_2 = new ArrayList<ParameterType>();
		runList_2.add(nf.createRunList(urlContent_1));
		runList_2.add(nf.createRunList(urlContent_2));

		// act
		xJdfBuilder.addParameter(runList_1, processUsage_1);
		xJdfBuilder.addParameter(runList_2, processUsage_2);

		// assert
		String actual;

		byte[] bytes = marsahlResult(xJdfBuilder);

		actual = getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet)");
		Assert.assertEquals("Number of ParameterSet nodes is wrong.", "2", actual);

		actual = getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet[1]/xjdf:Parameter)");
		Assert.assertEquals("Number of RunList parameters is wrong.", "1", actual);

		actual = getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet[2]/xjdf:Parameter)");
		Assert.assertEquals("Number of RunList parameters is wrong.", "2", actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[1]/@ProcessUsage");
		Assert.assertEquals("Value Process Usage is wrong.", processUsage_1, actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[2]/@ProcessUsage");
		Assert.assertEquals("Value Process Usage is wrong.", processUsage_2, actual);
	}

	/**
	 * Test of the initializing with an XJDF Document.
	 * @throws Exception
	 */
	@Test
	public void testXJDFConstructor() throws Exception {

		// arrange
		InputStream is = XJdfBuilderTest.class.getResourceAsStream(RES_XJDF_GANGJOB);
		XJdfNodeFactory nf = new XJdfNodeFactory();

		final String CUSTOMER_ID = "myCustomerID";

		XJdfParser parser = new XJdfParser();
		XJDF xjdf = parser.parseStream(is);

		// act
		XJdfBuilder xJdfBuilder = new XJdfBuilder(xjdf);
		xJdfBuilder.addParameter(nf.createCustomerInfo(CUSTOMER_ID));

		// assert
		int actual;

		byte[] bytes = marsahlResult(xJdfBuilder);

		actual = Integer.parseInt(getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet[@Name='CustomerInfo'][1]/xjdf:Parameter)"));
		Assert.assertEquals("Number of Parameter Nodes is wrong.", 2, actual);
	}

	/**
	 * Test of the initializing with an XJDF Document with process usage.
	 * @throws Exception
	 */
	@Test
	public void testXJDFConstructorProcessUsage() throws Exception {

		// arrange
		InputStream is = XJdfBuilderTest.class.getResourceAsStream(RES_XJDF_RUNLIST);
		XJdfNodeFactory nf = new XJdfNodeFactory();

		final String FILE_NAME = "./myTestFile.pdf";

		XJdfParser parser = new XJdfParser();
		XJDF xjdf = parser.parseStream(is);

		// act
		XJdfBuilder xJdfBuilder = new XJdfBuilder(xjdf);
		xJdfBuilder.addParameter(nf.createRunList(FILE_NAME), "PROCESS_USAGE_1");

		String actual;

		byte[] bytes = marsahlResult(xJdfBuilder);

		actual = getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet[@Name='RunList' and @ProcessUsage='PROCESS_USAGE_1']/xjdf:Parameter)");
		Assert.assertEquals("Number of Parameter Nodes is wrong.", "2", actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[@Name='RunList' and @ProcessUsage='PROCESS_USAGE_1']/xjdf:Parameter[2]/xjdf:RunList/xjdf:FileSpec/@URL");
		Assert.assertEquals("Filename is wrong.", FILE_NAME, actual);
	}

	/**
	 * Test of the initializing with an XJDF Document with process usage.
	 * @throws Exception
	 */
	@Test
	public void testXJDFConstructorProcessUsageNull() throws Exception {

		// arrange
		InputStream is = XJdfBuilderTest.class.getResourceAsStream(RES_XJDF_RUNLIST);
		XJdfNodeFactory nf = new XJdfNodeFactory();

		final String FILE_NAME = "./myTestFile.pdf";

		XJdfParser parser = new XJdfParser();
		XJDF xjdf = parser.parseStream(is);

		// act
		XJdfBuilder xJdfBuilder = new XJdfBuilder(xjdf);
		xJdfBuilder.addParameter(nf.createRunList(FILE_NAME));

		String actual;

		byte[] bytes = marsahlResult(xJdfBuilder);

		actual = getXPathValue(bytes, "count(/xjdf:XJDF/xjdf:ParameterSet[@Name='RunList' and @ProcessUsage='PROCESS_USAGE_1']/xjdf:Parameter)");
		Assert.assertEquals("Number of Parameter Nodes is wrong.", "1", actual);

		actual = getXPathValue(bytes, "/xjdf:XJDF/xjdf:ParameterSet[1]/xjdf:Parameter/xjdf:RunList/xjdf:FileSpec/@URL");
		Assert.assertEquals("Filename is wrong.", FILE_NAME, actual);
	}

    @Test
    public void testAddResourceMultiple() throws Exception {
        Media media1 = new Media().withMediaQuality("media1");
        Media media2 = new Media().withMediaQuality("media2");
        xJdfBuilder.addResource(media1, null);
        xJdfBuilder.addResource(media2, null);

        byte[] bytes = marsahlResult(xJdfBuilder);
        assertEquals("1", getXPathValue(bytes, "count(//xjdf:ResourceSet)"));
        assertEquals("2", getXPathValue(bytes, "count(//xjdf:ResourceSet/xjdf:Resource)"));
    }

    @Test
    public void testAddResourceMultipleWithDifferentProcessUsage() throws Exception {
        Media media1 = new Media().withMediaQuality("media1");
        Media media2 = new Media().withMediaQuality("media2");
        xJdfBuilder.addResource(media1, null, "a");
        xJdfBuilder.addResource(media2, null, "b");

        byte[] bytes = marsahlResult(xJdfBuilder);
        assertEquals("2", getXPathValue(bytes, "count(//xjdf:ResourceSet)"));
    }
}
