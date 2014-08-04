package net.rystuff.mcmoddeobf;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import net.rystuff.mcmoddeobf.gui.GuiMain;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MCModDeobf extends JFrame {

    public static JsonRootNode config;
    // Sets the GuiMain class to a variable
    public GuiMain guimain;

    public MCModDeobf() {
        if (new File(Util.baseDir + File.separator + "config.json").exists()) {
            // new File(Util.baseDir + File.separator + "config.json").delete();
        }
        Util.download("http://rystuff.net/downloads/deobf/config.json", Util.baseDir + File.separator + "config.json");
        try {
            config = new JdomParser().parse(new FileReader(new File(Util.baseDir + File.separator + "config.json")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
        // Creates a new JFrame
        guimain = new GuiMain(this);
        setTitle("MC Mod Deobf");
        setSize(550, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(guimain);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MCModDeobf();
    }
}
