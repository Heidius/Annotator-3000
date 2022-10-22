/*
 */
package annotator3000;

/**
 * @author Batlaptop4.0
 */
import java.awt.*;

import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Annotator extends JComponent implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;

    private int opIndex;
    private BufferedImage bImage;
    private int w, h;
    private JComboBox<String> formats;
    private double x, y;
    public ArrayList<Point> coordinateList = new ArrayList<>();

    public Annotator() {
       
        addMouseListener(this);
        //  Welcome panel 
        JOptionPane.showMessageDialog(null, "Please, choose an image!", "Annotator 3000+", JOptionPane.INFORMATION_MESSAGE, null);

        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "jpeg", "gif", "png", "png", "tif", "tiff", "wbmp", "bmp");
        file.addChoosableFileFilter(filter);

        int result = file.showOpenDialog(null);
        String mPath = null;

        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                mPath = file.getSelectedFile().getAbsolutePath();
                break;
            case JFileChooser.CANCEL_OPTION:
                System.exit(0);
                return;
        }
//      Creating the frame 
        JFrame f = new JFrame("Annotator 3000+");

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JPanel panel = new JPanel();
        System.setProperty("user.home", mPath);

        try {
            bImage = ImageIO.read(new File(mPath));
            w = bImage.getWidth();
            h = bImage.getHeight();

        } catch (Exception e) {

//          It shows a message to the user awaring about the format of the image
            System.out.println("That was not the right format! Please, try again!");
            e.printStackTrace();
            System.exit(0);
        }
//      Different components of the JFrame
        JComboBox<String> formats = new JComboBox<String>(getFormats());
        formats.setActionCommand("Formats");
        formats.addActionListener(this);

        setFormatComponent(formats);

        panel.add(new JLabel("Save As"));
        panel.add(formats);
        f.add("Center", this);
        f.add("South", panel);

        f.pack();
        f.setVisible(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }

    void setOpIndex(int i) {
        opIndex = i;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g = g.create();
        g.drawImage(bImage, 0, 0, w, h, null);
        g.setColor(Color.red);
        g.setFont(new Font("arial", Font.BOLD, 16));
        for (Point singlepoint : coordinateList) {
            g.drawOval(singlepoint.x, singlepoint.y, 10, 10);
            g.drawString("Veni, vidi, vici", singlepoint.x, singlepoint.y + 25);
        }
    }

//  Return the formats sorted alphabetically and in lower case
    public String[] getFormats() {
        String[] formats = ImageIO.getWriterFormatNames();
        TreeSet<String> formatSet = new TreeSet<String>();
        for (String s : formats) {
            formatSet.add(s.toLowerCase());
        }
        return formatSet.toArray(new String[0]);
    }

    public void actionPerformed(ActionEvent e) {
//      Browser
        String command = e.getActionCommand();
       

        if (command.equalsIgnoreCase("browse")) {
            JFileChooser file = new JFileChooser();
            file.setCurrentDirectory(new File(System.getProperty("user.home")));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "jpeg", "gif", "png", "png", "tif", "tiff", "wbmp", "bmp");
            file.addChoosableFileFilter(filter);

            int result = file.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = file.getSelectedFile();
                String Path = selectedFile.getAbsolutePath();
                try {
                    bImage = ImageIO.read(new File(Path));
                } catch (IOException e1) {
              
                }
            } else if (result == JFileChooser.CANCEL_OPTION) {
                System.out.println("You did not choose any image :(");
            }

        } else if (command.equalsIgnoreCase("Formats")) {

            int opt = JOptionPane.showConfirmDialog(null, "Where are you going to save it?", "Save image", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
            switch (opt) {
                case JOptionPane.CANCEL_OPTION -> {
                    return;
                }
            }

//          Save the image in the selected format. 
//          The selected format will be the name of the item to use
            String format = (String) formats.getSelectedItem();

            File sImage = new File("savedimage." + format);
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(sImage);
            int rval = chooser.showSaveDialog(formats);
            if (rval == JFileChooser.APPROVE_OPTION) {
                sImage = chooser.getSelectedFile();

                saveImage(bImage, format, sImage, coordinateList);
            }
        }
    }

// This draws all the points to the passed Graphics object
    public static void drawPoints(Graphics2D g, ArrayList<Point> points) {
        g.setColor(Color.red);
        g.setFont(new Font("arial", Font.BOLD, 16));
        for (Point singlepoint : points) {
            g.drawOval(singlepoint.x, singlepoint.y, 10, 10);
            g.drawString("Veni, vidi, vici", singlepoint.x, singlepoint.y + 25);
        }
    }
// This method saves the new image
    public void saveImage(BufferedImage bf2, String format, File file, ArrayList<Point> points) {
        try {
            Graphics2D graphics2D = bf2.createGraphics();
            drawPoints(graphics2D, points);
            ImageIO.write(bf2, format, file);
            graphics2D.dispose();
        } catch (Exception e) {
        }
    }
 
    protected void setFormatComponent(final JComboBox<String> formats) {
        this.formats = formats;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("The mouse clicked here! \n" + "X: " + e.getX() + ", Y: " + e.getY());

        Point oval = new Point();
        oval.x = e.getX();
        oval.y = e.getY();
        coordinateList.add(oval);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
