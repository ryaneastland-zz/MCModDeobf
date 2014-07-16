package net.rystuff.mcmoddeobf;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Util {

    // System Temp directory
    public static String tempDir = System.getProperty("java.io.tmpdir");

    // MCModDeobf base temp directory
    public static String baseDir = tempDir + "MCModDeobf";
    public static File baseDirFile = new File(baseDir);

    // Decompile Temp directory
    public static String decompString = baseDir + File.separator + "decomp";
    public static File decompFile = new File(decompString);

    // Deobf Temp directory
    public static String deobfString = baseDir + File.separator + "deobf";
    public static File deobfFile = new File(deobfString);

    // Path to Decompiler
    public static String decompilerString = baseDir + File.separator + "decompiler.jar";
    public static File decompilerFile = new File(decompilerString);

    // Output zip
    public static File outputZipFile;

    // Input file
    public static File inputZipFile;

    // Initialization function
    public static void init() {
        // Prints out MCModDeobf temp directory location
        System.out.println("MCModDeobf temp directory location: " + baseDir);

        // Checks if decompFile exists and is a directory
        if (decompFile.exists() && decompFile.isDirectory()) {
            try {
                FileUtils.deleteDirectory(decompFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Checks if deobfFile exists and is a directory
        if (deobfFile.exists() && deobfFile.isDirectory()) {
            try {
                FileUtils.deleteDirectory(deobfFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Creates temp directories
        if (!baseDirFile.exists()) {
            baseDirFile.mkdir();
        }
        decompFile.mkdir();
        deobfFile.mkdir();
    }

    // This is the download function for use in other places
    public static boolean download(String url, String dest)
    {
        try
        {
            System.out.println("Downloading " + url + " to " + dest);
            FileUtils.copyURLToFile(new URL(url), new File(dest));
            System.out.println("Downloaded " + url + " to " + dest);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    // Decompile function
    public static void decompile() throws Exception {
        System.out.println("Decompiling");
        try {
            String line;
            // runs the decompiler on the selected archive file
            Process p = Runtime.getRuntime().exec("java -jar " + decompilerString + " -jar " + inputZipFile + " -o " + decompString);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null){
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Decompiled!");
    }

    public static void deobf() {

    }
}
