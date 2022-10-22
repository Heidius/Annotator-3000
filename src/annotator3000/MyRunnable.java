/*
 */
package annotator3000;

import javax.swing.SwingUtilities;

/**
 *
 * @author Batlaptop4.0
 */
public class MyRunnable {

    public static void launch() {
        new Annotator();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launch());
    }
}
