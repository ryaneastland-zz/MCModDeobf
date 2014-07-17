package net.rystuff.mcmoddeobf.gui;

import net.rystuff.mcmoddeobf.MCModDeobf;
import net.rystuff.mcmoddeobf.Util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GuiMain extends JPanel implements ActionListener {

    // String of version of Minecraft to select from
    public static String[] mcVersions = Util.getMCVersions(MCModDeobf.config);

    // The select Minecraft version
    public static String mcVersion;

    // File Chooser for the input archive
    public static JFileChooser inputFile = new JFileChooser();

    // File Chooser for the output archive
    public static JFileChooser outputFile = new JFileChooser();

    // Dropdown menu for Mineraft versions
    public static JComboBox<String> versionDropDown = new JComboBox<String>(mcVersions);

    // Buttons
    private JButton source = new JButton("Select source file zip/jar");
    private JButton run = new JButton("Deobfuscate");
    private JButton output = new JButton("Output zip");

    // main Gui function
    public GuiMain(JFrame frame) {
        // Creates new JPanel
        JPanel panel = new JPanel();

        // Sets features and options for the JPanel
        panel.setLayout(new FlowLayout());
        add(new JLabel(
                "This is a program that I made used for deobfuscating minecraft mods"));
        add(this.source);
        source.addActionListener(this);
        add(versionDropDown);
        add(this.output);
        output.addActionListener(this);
        add(this.run);
        run.addActionListener(this);
    }

    @Override
    // Action Handler
    public void actionPerformed(ActionEvent e) {
        // Gets if the source select button was clicked
        if (e.getSource() == this.source) {
            // Sets the accepted file types
            inputFile.setAcceptAllFileFilterUsed(false);
            inputFile.setFileFilter(new FileNameExtensionFilter("Archive files", "zip", "jar"));

            // Opens the file chooser
            int returnVal = inputFile.showOpenDialog(null);

            // Save the selected file path to inputZipFile variable
            if (returnVal == 0) {
                Util.inputZipFile = inputFile.getSelectedFile();
            }
        }
        if (e.getSource() == this.run) {
            // Gets the selected Minecraft version from the dropdown
            mcVersion = versionDropDown.getSelectedItem().toString();

            // Checks if the decompiler exists
            if (!Util.decompilerFile.exists()) {
                // Downloads the decompiler if it doesn't exists
                Util.download("https://bitbucket.org/mstrobel/procyon/downloads/procyon-decompiler-0.5.25.jar", Util.decompilerString);
            }

            // Runs the Initialization function
            Util.init();

            // Tries to run the Decompile function
            try {
                Util.decompile();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            // Tries to run the Deobf function
            try {
                Util.deobf();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == this.output) {
            // Sets the accepted file types
            outputFile.setAcceptAllFileFilterUsed(false);
            outputFile.setFileFilter(new FileNameExtensionFilter("Archive files", "zip", "jar"));

            // Opens the file chooser
            int returnVal = outputFile.showSaveDialog(null);

            // Saves the selected file path to outputZipFile variable
            if (returnVal == 0) {
                Util.outputZipFile = outputFile.getSelectedFile();
                System.out.println(Util.outputZipFile);
            }
        }
    }
}