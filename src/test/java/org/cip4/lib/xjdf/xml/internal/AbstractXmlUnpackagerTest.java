package org.cip4.lib.xjdf.xml.internal;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractXmlUnpackagerTest {

    private final String PACKAGE = AbstractXmlUnpackagerTest.class.getResource("../package.zip").getFile();
    private final String INDEX_FILE = "index.xml";
    private final String RESOURCE_FILE = "foo.txt";
    private final String RESOURCE_CONTENT = "bar";

    private class ConcreteXmlUnpackager extends AbstractXmlUnpackager {

        /**
         * Custom constructor. Accepting a package path for initializing.
         *
         * @param pathPackage Path of the package.
         * @throws IOException Is thrown in case an IOExcetion occurs.
         */
        public ConcreteXmlUnpackager(final String pathPackage) throws IOException {
            super(pathPackage);
        }

        @Override
        protected String[] getMasterExtension() {
            return new String[]{"xml"};
        }
    }

    @Test
    public void unpackageZipTempNoArg() throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        String index = unpackager.unpackageZipTemp();
        Path indexPath = Paths.get(index);
        try {
            assertTrue(Files.exists(indexPath));
            assertTrue(Files.exists(indexPath.getParent().resolve(RESOURCE_FILE)));
            assertTrue(indexPath.endsWith(INDEX_FILE));
            assertTrue(indexPath.getParent().getFileName().toString().startsWith("cip4_"));
        } finally {
            deleteDirectory(indexPath.getParent());
        }
    }

    @Test
    public void unpackageZipTemp() throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        String index = unpackager.unpackageZipTemp("foo");
        Path indexPath = Paths.get(index);
        try {
            assertTrue(Files.exists(indexPath));
            assertTrue(Files.exists(indexPath.getParent().resolve(RESOURCE_FILE)));
            assertTrue(indexPath.endsWith(INDEX_FILE));
            assertTrue(indexPath.getParent().getFileName().toString().startsWith("foo_"));
        } finally {
            deleteDirectory(indexPath.getParent());
        }
    }

    @Test
    public void unpackageZip(@TempDir Path targetDir) throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        String index = unpackager.unpackageZip(targetDir.toString());
        Path indexPath = Paths.get(index);
        assertTrue(Files.exists(indexPath));
        assertTrue(Files.exists(indexPath.getParent().resolve(RESOURCE_FILE)));
        assertTrue(indexPath.endsWith(INDEX_FILE));
        assertEquals(targetDir, indexPath.getParent());
    }

    @Test
    public void unpackageZipWithBadEntry(@TempDir Path targetDir) throws Exception {
        Path zipPath = targetDir.resolve("badZip.zip");
        try (
            OutputStream out = Files.newOutputStream(zipPath);
            ZipOutputStream zout = new ZipOutputStream(out)
        ) {
            zout.putNextEntry(new ZipEntry("../../bad-file.txt"));
            zout.write("apple-pie".getBytes());
        }

        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(zipPath.toString());
        assertThrows(
            ZipException.class,
            () -> unpackager.unpackageZip(targetDir.toString()),
            "Bad zip entry"
        );
    }


    @Test
    public void unpackageZipMultipleTimes(@TempDir Path target) throws Exception {
        Path target1 = target.resolve("folder1");
        Path target2 = target.resolve("folder2");

        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        unpackager.unpackageZip(target1.toString());

        String index = unpackager.unpackageZip(target2.toString());

        Path indexPath = Paths.get(index);
        assertTrue(Files.exists(indexPath));
        assertTrue(Files.exists(indexPath.getParent().resolve(RESOURCE_FILE)));
        assertTrue(indexPath.endsWith(INDEX_FILE));
        assertEquals(target2, indexPath.getParent());
    }

    @Test
    public void findMasterDocumentPath() throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        assertEquals(INDEX_FILE, unpackager.findMasterDocumentPath());
    }

    @Test
    public void findMasterDocument() throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        String INDEX_CONTENT = "<?xml version=\"1.0\"?>";
        assertEquals(INDEX_CONTENT, new String(unpackager.findMasterDocument()));
    }

    @Test
    public void extractFile() throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        assertEquals(RESOURCE_CONTENT, new String(unpackager.extractFile(RESOURCE_FILE)));
    }

    @Test
    public void extractFileMultipleTimes() throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        assertEquals(RESOURCE_CONTENT, new String(unpackager.extractFile(RESOURCE_FILE)));
        assertEquals(RESOURCE_CONTENT, new String(unpackager.extractFile(RESOURCE_FILE)));
    }

    @Test
    public void getMasterExtension() throws Exception {
        AbstractXmlUnpackager unpackager = new ConcreteXmlUnpackager(PACKAGE);
        assertArrayEquals(new String[]{"xml"}, unpackager.getMasterExtension());
    }

    private void deleteDirectory(final Path directory) throws IOException {
        FileUtils.deleteDirectory(directory.toFile());
    }
}