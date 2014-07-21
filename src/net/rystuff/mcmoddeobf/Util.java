package net.rystuff.mcmoddeobf;

import argo.jdom.*;
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
    public static File outputZipFile;

    // Input file
    public static File inputZipFile;

    public static String[] getMCVersions(JsonRootNode config) {
        List<JsonNode> versionNodes = config.getArrayNode("mcVersions");
        String[] versions = new String[versionNodes.size()];
        for (int i = 0; i < versionNodes.size(); i++) {
            versions[i] = versionNodes.get(i).getStringValue();
            System.out.println(versions[i]);
        }
        return versions;
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

        List<JsonNode> versionNodes = MCModDeobf.config.getArrayNode("mcVersions");
        String[] versions = new String[versionNodes.size()];
        for (int i = 0; i < versionNodes.size(); i++) {
            versions[i] = versionNodes.get(i).getStringValue();
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
    public static void decompile() throws Exception {
        System.out.println("Decompiling");
        try {
            String line;
            // runs the decompiler on the selected archive file
            Process p = Runtime.getRuntime().exec("java -jar " + decompilerString + " -jar " + inputZipFile + " -o " + decompString);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Decompiled!");
    }

    public static void deobf() throws IOException {
        System.out.println("Getting all files in " + decompFile.getCanonicalPath() + " including those in subdirectories");
        List<File> files = (List<File>) FileUtils.listFiles(decompFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : files) {
            System.out.println("file: " + file.getCanonicalPath());
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
    }
}
