/**
 * All rights reserved by
 *
 * flyeralarm GmbH
 * Alfred-Nobel-Straße 18
 * 97080 Würzburg
 *
 * Email: info@flyeralarm.com
 * Website: http://www.flyeralarm.com
 */
package org.cip4.lib.xjdf.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test case for XJdfUnpackager
 *
 * @author s.meissner
 */
public class XJdfUnpackagerTest {

    private final static String RES_PGK = "/org/cip4/lib/xjdf/testPackage.zip";
    private final static String RES_PGK_WITH_SPACES = "/org/cip4/lib/xjdf/package with spaces.zip";

    /**
     * Setup unit test.
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Tear down unit test.
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.cip4.lib.xjdf.xml.internal.AbstractXmlUnpackager#unpackageZip(java.lang.String,
     * java.lang.String)}.
     *
     * @throws IOException
     */
    @Test
    public void testUnpackageZip() throws IOException, URISyntaxException {

        // arrange
        String pathZip = XJdfUnpackagerTest.class.getResource(RES_PGK).toURI().getPath();

        String tmpDir = FileUtils.getTempDirectoryPath();
        String targetDir = FilenameUtils.concat(tmpDir, "XJdfLib_UnpackageTest_" + System.currentTimeMillis());

        // assert
        XJdfUnpackager unpackager = new XJdfUnpackager(pathZip);
        unpackager.unpackageXJdf(targetDir);

        // act
        File targetFile = new File(targetDir);
        Assert.assertTrue("Target destination does not exist.", targetFile.exists());

        String pathPtk = FilenameUtils.concat(targetDir, "testPackage.xjdf");
        Assert.assertTrue("File does not exist.", new File(pathPtk).exists());

        String pathArtwork = FilenameUtils.concat(targetDir, "artwork/testArtwork.pdf");
        Assert.assertTrue("Artwork does not exist.", new File(pathArtwork).exists());

        String pathPreview = FilenameUtils.concat(targetDir, "preview/testPreview.pdf");
        Assert.assertTrue("Preview does not exist.", new File(pathPreview).exists());

        FileUtils.deleteDirectory(targetFile); // clean up
        Assert.assertFalse("Target destination was not removed.", targetFile.exists());
    }

    @Test
    public void testUnpackageZipWithSpaces() throws IOException, URISyntaxException {

        // arrange
        URI pathZip = XJdfUnpackagerTest.class.getResource(RES_PGK_WITH_SPACES).toURI();

        String tmpDir = FileUtils.getTempDirectoryPath();
        String targetDir = FilenameUtils.concat(tmpDir, "XJdfLib_UnpackageTest_" + System.currentTimeMillis());

        // assert
        XJdfUnpackager unpackager = new XJdfUnpackager(pathZip.getPath());
        unpackager.unpackageXJdf(targetDir);

        // act
        File targetFile = new File(targetDir);
        Assert.assertTrue("Target destination does not exist.", targetFile.exists());

        String pathPtk = FilenameUtils.concat(targetDir, "testPackage.xjdf");
        Assert.assertTrue("File does not exist.", new File(pathPtk).exists());

        String pathArtwork = FilenameUtils.concat(targetDir, "artwork/testArtwork.pdf");
        Assert.assertTrue("Artwork does not exist.", new File(pathArtwork).exists());

        String pathPreview = FilenameUtils.concat(targetDir, "preview/testPreview.pdf");
        Assert.assertTrue("Preview does not exist.", new File(pathPreview).exists());

        FileUtils.deleteDirectory(targetFile); // clean up
        Assert.assertFalse("Target destination was not removed.", targetFile.exists());
    }

    /**
     * Test method for {@link org.cip4.lib.xjdf.xml.internal.AbstractXmlUnpackager#unpackageZip(java.lang.String,
     * java.lang.String)}.
     *
     * @throws IOException
     */
    @Test
    public void testUnpackageZipTempDir() throws IOException, URISyntaxException {

        // arrange
        String pathZip = XJdfUnpackagerTest.class.getResource(RES_PGK).toURI().getPath();

        // assert
        XJdfUnpackager unpackager = new XJdfUnpackager(pathZip);
        String targetDir = FilenameUtils.getFullPath(unpackager.unpackageXJdfTemp());

        // act
        File targetFile = new File(targetDir);
        Assert.assertTrue("Target destination does not exist.", targetFile.exists());

        String pathPtk = FilenameUtils.concat(targetDir, "testPackage.xjdf");
        Assert.assertTrue("File does not exist.", new File(pathPtk).exists());

        String pathArtwork = FilenameUtils.concat(targetDir, "artwork/testArtwork.pdf");
        Assert.assertTrue("Artwork does not exist.", new File(pathArtwork).exists());

        String pathPreview = FilenameUtils.concat(targetDir, "preview/testPreview.pdf");
        Assert.assertTrue("Preview does not exist.", new File(pathPreview).exists());

        FileUtils.deleteDirectory(targetFile); // clean up
        Assert.assertFalse("Target destination was not removed.", targetFile.exists());
    }

    /**
     * Test method for {@link org.cip4.lib.xjdf.xml.XJdfPackageManager#getXJdfDocument()}.
     *
     * @throws FileNotFoundException
     */
    @Test
    public void testGetXJdfDocument() throws Exception {

        // arrange
        String path = XJdfUnpackagerTest.class.getResource(RES_PGK).toURI().getPath();
        XJdfUnpackager unpackager = new XJdfUnpackager(path);

        // act
        XJdfNavigator nav = unpackager.getXJdfDocument();

        String pathPreview = nav.evaluateString(
            "/xjdf:XJDF/xjdf:ResourceSet[@Name='Preview']/xjdf:Resource/xjdf:Preview/xjdf:FileSpec/@URL"
        );
        byte[] bytes = unpackager.extractFile(pathPreview);

        // assert
        Assert.assertEquals("XJDF ID is wrong.", "95733854-01", nav.evaluateString(XJdfNavigator.JOB_ID));
        Assert.assertNotEquals("Preview is null.", 0, bytes.length);
    }

    @Test
    public void getXjdfDocumentIsNamespaceAware() throws Exception{

        // arrange
        String path = XJdfUnpackagerTest.class.getResource(RES_PGK).toURI().getPath();
        XJdfUnpackager unpackager = new XJdfUnpackager(path);

        // act
        XJdfNavigator nav = unpackager.getXJdfDocument(true);

        Assert.assertTrue(nav != null);
        Assert.assertTrue(nav.extractNode("//xjdf:XJDF") != null);
    }
}
