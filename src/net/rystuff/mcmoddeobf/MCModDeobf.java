// trying to fix git
package net.rystuff.mcmoddeobf;

import net.rystuff.mcmoddeobf.gui.GuiMain;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MCModDeobf extends JFrame{

    public GuiMain guimain;
    public static File source;

    public MCModDeobf() {
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
