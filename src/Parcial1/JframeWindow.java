package Parcial1;

import javax.swing.*;
public class JframeWindow {
        public static void main(String[] args) {
            JFrame frame = new JFrame("Window");

            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setVisible(true);
        }
}
