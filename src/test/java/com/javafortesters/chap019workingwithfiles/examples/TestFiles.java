package com.javafortesters.chap019workingwithfiles.examples;

import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class TestFiles {

    @Test
    public void createTempFile() throws IOException {
        File createdFile = File.createTempFile("file", ".dmg");
        System.out.println(createdFile.getAbsolutePath());
        assertThat(createdFile.exists(), is(true));
        createdFile.delete();
        assertThat(createdFile.exists(), is(false));
    }

    @Test
    public void displayRoots() {
        File[] roots = File.listRoots();
        for (File aFile : roots) {
            System.out.println(aFile.getAbsolutePath());
        }
        for (int i = 0; i < roots.length; i++) {
            System.out.println(roots[i].getAbsolutePath());
        }
    }

    @Test
    public void createTempFileWithCustomCode() throws IOException {
        Long fileNamePart = System.currentTimeMillis();
        File customCodeFile = new File("/var/folders/f1/qw1wk0757zs7kvvklsngtq580000gn/T/",
                (fileNamePart.toString() + ".io"));

        File anotherCustomFile = File.createTempFile((fileNamePart.toString()), ".io");

        assertThat(customCodeFile.exists(), is(false));
        assertThat(anotherCustomFile.exists(), is(true));
        System.out.println(customCodeFile.getAbsolutePath());
        System.out.println(anotherCustomFile.getAbsolutePath());

        Properties sys = System.getProperties();
        String tempDir = sys.getProperty("java.io.tmpdir");
        System.out.println(tempDir);
        assertThat(customCodeFile.getAbsolutePath().contains(tempDir), is(true));
        assertThat(customCodeFile.getAbsolutePath().contains(fileNamePart.toString()), is(true));
        assertThat(anotherCustomFile.getAbsolutePath().contains(tempDir), is(true));
        assertThat(anotherCustomFile.getAbsolutePath().contains(fileNamePart.toString()), is(true));
        customCodeFile.delete();
        anotherCustomFile.delete();
        assertThat(customCodeFile.exists(), is(false));
        assertThat(anotherCustomFile.exists(), is(false));
    }

    @Test
    public void checkCanonicalPath() throws IOException {
        Properties sys = System.getProperties();
        String tempDir = sys.getProperty("java.io.tmpdir");

        Path path = Paths.get(tempDir, "1/2/3/4/5/../../../..");
        Path anotherPath = Paths.get(tempDir, "1/2/../../1");
        System.out.println(path.toFile());

        File createdFileOne = path.toFile();
        File createdFileTwo = anotherPath.toFile();

        System.out.println(createdFileOne.getCanonicalPath());
        System.out.printf(createdFileTwo.getCanonicalPath());

        assertEquals(createdFileOne.getCanonicalPath(), createdFileTwo.getCanonicalPath());
        createdFileOne.deleteOnExit();
        createdFileTwo.deleteOnExit();
    }

    @Test
    public void checkTempDirectory() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        assertThat(tempDir.isFile(), is(false));
        assertThat(tempDir.isDirectory(), is(true));
    }

    @Test
    public void printWriterAndAppend() throws IOException {
        File tempFile = new File(System.getProperty("java.io.tmpdir"), "text.txt");
        System.out.println(tempFile.getCanonicalPath());

        FileWriter writer = new FileWriter(tempFile);
        BufferedWriter buffer = new BufferedWriter(writer);
        PrintWriter print = new PrintWriter(buffer);

        String textToWrite = "it's written in text now";

        print.println(textToWrite);
        print.close();

        writer = new FileWriter(tempFile, true);
        buffer = new BufferedWriter(writer);
        print = new PrintWriter(buffer);

        print.println("===========================");
        print.close();
        tempFile.deleteOnExit();
    }

    @Test
    public void calculateFileLength() throws IOException {
        File aFile = new File(System.getProperty("java.io.tmpdir"),"aFile");
        System.out.println(aFile.length());
        assertEquals(aFile.length(),0);

        FileWriter writer = new FileWriter(aFile);
        BufferedWriter buffer = new BufferedWriter(writer);
        PrintWriter print = new PrintWriter(buffer);

        String textToWrite = "it's written in text now";


        print.println(textToWrite);
        print.close();

        BufferedReader reader = new BufferedReader(new FileReader(aFile));
        try{
            String line;
            while((line = reader.readLine())!=null){
                System.out.println(line);
            }
        }finally{
                reader.close();
            }

        String ignoredChars = System.lineSeparator();
        int ignored = ignoredChars.length();
        assertEquals(aFile.length(),textToWrite.length()+ignored);
        System.out.println(ignoredChars.length());;
        System.out.println(aFile.getUsableSpace());
        System.out.println(aFile.getTotalSpace());
        System.out.println(aFile.getFreeSpace());

        aFile.delete();
        }
    }


