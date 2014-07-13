package net.rystuff.mcmoddeobf;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.rystuff.mcmoddeobf.gui.GuiMain;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Util {
    public static String tempDir = System.getProperty("java.io.tmpdir");
    public static File Deobf = new File(tempDir + "Deobf");
    public static String Deobf2 = Deobf.getAbsolutePath();
    public static File fernflower = new File(tempDir + "fernflower");
    public static File outputFile = new File(tempDir + "deobfOut");
    public static String outputFile1 = outputFile.toString();
    public static File outputZip;
    public static ZipFile zipFile;
    public static String Fernflower = Util.tempDir + File.separator + "fernflower" + File.separator + "fernflower-" + GuiMain.mcVersion + ".jar";

    public static void preDeobf() {
        System.out.println(Deobf);
        try {
            FileUtils.deleteDirectory(Deobf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Deobf.mkdir();
        System.out.println("Temp Directory created at: " + Deobf2);
    }

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

    public static void unZip(String zip) {
        try
        {
            ZipFile zipFile = new ZipFile(zip);
            zipFile.extractAll(Deobf2);
        }
        catch (ZipException e)
        {
            e.printStackTrace();
        }
    }

    public static void Deobf() throws Exception
    {
        outputFile.mkdir();
        try {
            Process p = Runtime.getRuntime().exec(
                    "cmd /c start " + tempDir.toString() + "fernflower" + File.separator + "fernflower-" + GuiMain.mcVersion + ".jar " + Deobf + " " + outputFile1);
            System.out.println("Deobfuscating using fernflower");
            p.waitFor();
            System.out.println("FernFlower Deobfuscating to: " + outputFile);
        } catch (Exception e) {
            System.out.println("Error using fernflower to deobfucate: " + e);
        }
    }

    public static void Zip() {
        try {
            if (outputZip.toString().toLowerCase().contains(".zip")){
                zipFile = new ZipFile(outputZip);
            } else if (outputZip.toString().toLowerCase().contains(".jar")) {
                zipFile = new ZipFile(outputZip);
            } else {
                zipFile = new ZipFile(outputZip + ".zip");
            }
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setIncludeRootFolder(false);
            zipFile.createZipFileFromFolder(outputFile1, parameters, true, 10485760);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
