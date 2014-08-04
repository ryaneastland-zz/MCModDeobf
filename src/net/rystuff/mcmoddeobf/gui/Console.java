package net.rystuff.mcmoddeobf.gui;

import argo.jdom.JsonNode;
import net.rystuff.mcmoddeobf.MCModDeobf;
import to.uk.ilexiconn.jpastebin.pastebin.Pastebin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class Console extends WindowAdapter implements ITask, WindowListener, ActionListener, Runnable
{
    public JFrame frame;
    public JTextArea textArea;
    public Thread reader0;
    public Thread reader1;
    public boolean quit;

    public PipedInputStream inputStream0 = new PipedInputStream();
    public PipedInputStream inputStream1 = new PipedInputStream();

    public JButton pastebin;

    public String getTaskName()
    {
        return "Starting console...";
    }

    public void run(JsonNode config)
    {
        frame = new JFrame("JurassicLauncher Console");
        frame.setSize(560, 300);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width / 2 - 560 - 128;
        int y = screen.height / 2 - 300 - 64;
        frame.setLocation(x, y);

        textArea = new JTextArea();
        textArea.setEditable(false);

        pastebin = new JButton("Export to Pastebin");
        pastebin.setEnabled(false);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.getContentPane().add(pastebin, BorderLayout.SOUTH);
        frame.setVisible(true);

        frame.addWindowListener(this);
        pastebin.addActionListener(this);

        try
        {
            PipedOutputStream pout = new PipedOutputStream(inputStream0);
            System.setOut(new PrintStream(pout, true));
        }
        catch (java.io.IOException io)
        {
            textArea.append("Couldn't redirect STDOUT to this console\n" + io.getMessage());
        }
        catch (SecurityException se)
        {
            textArea.append("Couldn't redirect STDOUT to this console\n" + se.getMessage());
        }

        try
        {
            PipedOutputStream pout2 = new PipedOutputStream(inputStream1);
            System.setErr(new PrintStream(pout2, true));
        }
        catch (java.io.IOException io)
        {
            textArea.append("Couldn't redirect STDERR to this console\n" + io.getMessage());
        }
        catch (SecurityException se)
        {
            textArea.append("Couldn't redirect STDERR to this console\n" + se.getMessage());
        }

        quit = false;

        reader0 = new Thread(this);
        reader0.setDaemon(true);
        reader0.start();
        reader1 = new Thread(this);
        reader1.setDaemon(true);
        reader1.start();
    }

    public synchronized void windowClosed(WindowEvent evt)
    {
        quit = true;
        notifyAll();
        try
        {
            reader0.join(1000);
            inputStream0.close();
        }
        catch (Exception ignored)
        {
        }
        try
        {
            reader1.join(1000);
            inputStream1.close();
        }
        catch (Exception ignored)
        {
        }
    }

    public synchronized void windowClosing(WindowEvent evt)
    {
        if (!MCModDeobf.instance.isVisible()) JurassicLauncher.instance.setVisible(true);
        frame.setVisible(false);
        frame.dispose();
    }

    public synchronized void actionPerformed(ActionEvent evt)
    {
        try
        {
            System.out.println("Here's the link: " + Pastebin.newPaste("8afcfd7341c9fd9d87a52783657f5759", textArea.getText(), "JurassicLauncher Log").paste().getLink());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void run()
    {
        try
        {
            while (Thread.currentThread() == reader0)
            {
                try
                {
                    wait(100);
                }
                catch (InterruptedException ignored)
                {
                }
                if (inputStream0.available() != 0)
                {
                    String input = readLine(inputStream0);
                    int link = textArea.getDocument().getLength();
                    textArea.append(input);
                    textArea.setCaretPosition(link + 1);
                    if (!pastebin.isEnabled()) pastebin.setEnabled(true);
                }
                if (quit) return;
            }

            while (Thread.currentThread() == reader1)
            {
                try
                {
                    wait(100);
                }
                catch (InterruptedException ignored)
                {
                }
                if (inputStream1.available() != 0)
                {
                    String input = readLine(inputStream1);
                    int link = textArea.getDocument().getLength();
                    textArea.append(input);
                    textArea.setCaretPosition(link + 1);
                    if (!pastebin.isEnabled()) pastebin.setEnabled(true);
                }
                if (quit) return;
            }
        }
        catch (Exception e)
        {
            textArea.append("\nConsole reports an Internal error.");
            textArea.append("The error is: " + e);
        }

    }

    public synchronized String readLine(PipedInputStream in) throws IOException
    {
        String input = "";
        do
        {
            int available = in.available();
            if (available == 0) break;
            byte b[] = new byte[available];
            in.read(b);
            input = input + new String(b, 0, b.length);
        }
        while (!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
        return input;
    }
}