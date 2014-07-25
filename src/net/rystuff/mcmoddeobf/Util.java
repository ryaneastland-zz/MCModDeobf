package net.rystuff.mcmoddeobf;

import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.rystuff.mcmoddeobf.gui.GuiMain;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.net.URL;
import java.util.List;

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
    public static File outputZip;

    // Output zip ZipFile
    public static ZipFile outputZipFile;

    // Input file
    public static File inputZip;

    // Get MCVersions for config
    public static String[] getMCVersions(JsonRootNode config) {
        List<JsonNode> versionNodes = config.getArrayNode("mcVersions");
        String[] versions = new String[versionNodes.size()];
        for (int i = 0; i < versionNodes.size(); i++) {
            versions[i] = versionNodes.get(i).getStringValue();
            System.out.println(versions[i]);
        }
        return versions;
    }

    // Gets the decompiler download link
    public static String decompilerDownload(JsonRootNode config) {
        String decompiler = config.getStringValue("decompiler");
        return decompiler;
    }

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

        // Get MCVersions from config
        List<JsonNode> versionNodes = MCModDeobf.config.getArrayNode("mcVersions");
        String[] versions = new String[versionNodes.size()];
        for (int i = 0; i < versionNodes.size(); i++) {
            versions[i] = versionNodes.get(i).getStringValue();
            // if csv files doesn't exists then download them
            if (!new File(baseDir + File.separator + versions[i] + File.separator + "fields.csv").exists()) {
                download("http://rystuff.net/downloads/deobf/" + versions[i] + "/fields.csv", baseDir + File.separator + versions[i] + File.separator + "fields.csv");
            }
            if (!new File(baseDir + File.separator + versions[i] + File.separator + "methods.csv").exists()) {
                download("http://rystuff.net/downloads/deobf/" + versions[i] + "/methods.csv", baseDir + File.separator + versions[i] + File.separator + "methods.csv");
            }
            if (!new File(baseDir + File.separator + versions[i] + File.separator + "params.csv").exists()) {
                download("http://rystuff.net/downloads/deobf/" + versions[i] + "/params.csv", baseDir + File.separator + versions[i] + File.separator + "params.csv");
            }
        }
    }

    // This is the download function for use in other places
    public static boolean download(String url, String dest) {
        try {
            System.out.println("Downloading " + url + " to " + dest);
            FileUtils.copyURLToFile(new URL(url), new File(dest));
            System.out.println("Downloaded " + url + " to " + dest);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    // Decompile function
    public static void decompile() {
        System.out.println("Decompiling");
        try {
            String line;
            // runs the decompiler on the selected archive file
            Process p = Runtime.getRuntime().exec("java -jar " + decompilerString + " -jar " + inputZip + " -o " + decompString);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Decompiled!");
    }

    // Deobfuscate function
    public static void deobf() {
        try {
            FileUtils.copyDirectory(decompFile, deobfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Deobfuscating");
        // Gets all files to deobfuscate
        List<File> files = (List<File>) FileUtils.listFiles(deobfFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : files) {
            try {
                System.out.println("Deobfuscating " + file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Fields deobf
            try {
                String csvFile = baseDir + File.separator + GuiMain.mcVersion + File.separator + "fields.csv";
                BufferedReader br = null;
                String line = "";
                String csvSplitBy = ",";
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(csvSplitBy);
                    String content = FileUtils.readFileToString(new File(file.toString()));
                    if (content.contains(split[0])) {
                        FileUtils.writeStringToFile(file, content.replaceAll(split[0], split[1]));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Methods deobf
            try {
                String csvFile = baseDir + File.separator + GuiMain.mcVersion + File.separator + "methods.csv";
                BufferedReader br = null;
                String line = "";
                String csvSplitBy = ",";
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(csvSplitBy);
                    String content = FileUtils.readFileToString(new File(file.toString()));
                    if (content.contains(split[0])) {
                        FileUtils.writeStringToFile(file, content.replaceAll(split[0], split[1]));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Params deobf
            try {
                String csvFile = baseDir + File.separator + GuiMain.mcVersion + File.separator + "params.csv";
                BufferedReader br = null;
                String line = "";
                String csvSplitBy = ",";
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(csvSplitBy);
                    String content = FileUtils.readFileToString(new File(file.toString()));
                    if (content.contains(split[0])) {
                        FileUtils.writeStringToFile(file, content.replaceAll(split[0], split[1]));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Deobfuscated!");
    }

    // Zipping output
    public static void Zip() {
        try {
            if (outputZip.toString().toLowerCase().contains(".zip")){
                outputZipFile = new ZipFile(outputZip);
            } else if (outputZip.toString().toLowerCase().contains(".jar")) {
                outputZipFile = new ZipFile(outputZip);
            } else {
                outputZipFile = new ZipFile(outputZip + ".zip");
            }
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setIncludeRootFolder(false);
            outputZipFile.createZipFileFromFolder(deobfFile + File.separator, parameters, true, 10485760);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
