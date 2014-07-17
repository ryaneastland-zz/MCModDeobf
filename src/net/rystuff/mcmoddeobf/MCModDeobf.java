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

public class MCModDeobf extends JFrame{

    // Sets the GuiMain class to a variable
    public GuiMain guimain;

    public static JsonRootNode config;

    public MCModDeobf() {
        try {
            config = new JdomParser().parse(new FileReader(new File("C:\\Users\\ryan\\Documents\\GitHub\\MCModDeobf\\config.json")));
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
