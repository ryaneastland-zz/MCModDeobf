package net.rystuff.mcmoddeobf.gui;

import net.rystuff.mcmoddeobf.MCModDeobf;
import net.rystuff.mcmoddeobf.Util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiMain extends JPanel implements ActionListener {


    public static String[] mcVersions = {"1.6.4", "1.7.2", "1.7.10"};
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
                MCModDeobf.source = chooser.getSelectedFile();
            }
        }
        if (e.getSource() == this.run) {
            Util.fernflower.mkdir();
            mcVersion = versionDropDown.getSelectedItem().toString();
            Util.download("http://rystuff.net/downloads/fernflower/fernflower-" + mcVersion + ".jar", Util.tempDir + "fernflower-" + mcVersion + ".jar");
            Util.preDeobf();
            Util.unZip(MCModDeobf.source.toString());
            try {
                Util.Deobf();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Util.Zip();
        }
        if (e.getSource() == this.output) {
            outputFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            outputFolder.setAcceptAllFileFilterUsed(false);
            outputFolder.setFileFilter(new FileNameExtensionFilter("Archive files" , "zip", "jar"));
            int returnVal = outputFolder.showSaveDialog(null);
            if (returnVal == 0) {
                Util.outputZip = outputFolder.getSelectedFile();
                System.out.println(Util.outputZip);
            }
        }
    }
}