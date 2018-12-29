package View;

import javax.swing.*;
import java.awt.*;

public class Styliser {


    /**
     * Used to apply the style of all project Swing components
     */
    public static void applyStyle() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                // don't set nimbus on mac - it is not painting some elements correctly
                if ("Nimbus".equals(info.getName()) && !System.getProperty("os.name").equals("Mac OS X")){////System.out.println.getProperty("os.name").equals("Mac OS X")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

    /**
     * Used to style the JComponent submitted
     * @param component != null
     */
    public static void componentStyler(JComponent component) {
        component.setPreferredSize(new Dimension(160, 40));
        component.setSize(new Dimension(160, 40));
        component.setMaximumSize(new Dimension(160, 40));
        component.setFocusable(false);
    }

    /**
     * Used to stylise the JPanel submitted
     * @param panel != null
     */
    public static void panelStyler(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    }



}
