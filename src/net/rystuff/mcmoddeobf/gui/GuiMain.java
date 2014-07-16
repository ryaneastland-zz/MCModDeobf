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


    public static String[] mcVersions = {"1.7.10", "1.7.2", "1.6.4"};
    public static String mcVersion;

    public static JFileChooser outputFolder = new JFileChooser();

    private JButton source = new JButton("Select source file zip/jar");
    private JButton run = new JButton("Deobfuscate");
    private JButton output = new JButton("Output zip");
    public static JComboBox<String> versionDropDown = new JComboBox<String>(mcVersions);

    public GuiMain(JFrame frame) {
        JPanel panel = new JPanel();
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.source) {
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileNameExtensionFilter("Archive files" , "zip", "jar"));
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == 0) {
                Util.inputZipFile = chooser.getSelectedFile();
            }
        }
        if (e.getSource() == this.run) {
            mcVersion = versionDropDown.getSelectedItem().toString();
            if (!Util.decompilerFile.exists())
            {
               Util.download("https://bitbucket.org/mstrobel/procyon/downloads/procyon-decompiler-0.5.25.jar", Util.decompilerString);
            } else {

            }
            Util.init();
            try {
                Util.decompile();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                Util.deobf();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == this.output) {
            outputFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            outputFolder.setAcceptAllFileFilterUsed(false);
            outputFolder.setFileFilter(new FileNameExtensionFilter("Archive files" , "zip", "jar"));
            int returnVal = outputFolder.showSaveDialog(null);
            if (returnVal == 0) {
                Util.outputZipFile = outputFolder.getSelectedFile();
                System.out.println(Util.outputZipFile);
            }
        }
    }
}