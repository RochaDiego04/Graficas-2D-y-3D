package parcial1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculadora extends JFrame {
    private JTextArea txtArea;

    public Calculadora() {
        setTitle("Calculadora");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        txtArea = createTextArea();
        JPanel panelBotones = createBotonesPanel();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(txtArea, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        getContentPane().add(panelBotones, BorderLayout.CENTER);

        pack(); // Ajusta el tamaño de la ventana para que se ajuste al contenido
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        return textArea;
    }

    private JPanel createBotonesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] labels = {
                "C", "/", "*", "-",
                "7", "8", "9", "+",
                "4", "5", "6", "",
                "1", "2", "3", "=",
                "0", ".", ""
        };

        int gridx = 0;
        int gridy = 0;

        for (String label : labels) {
            JButton btn = createButton(label);

            if (label.equals("0")) { // Elementos que abarcan 2 espacios horizontal
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
            } else if (label.equals("+") || label.equals("=")) { // Elementos que abarcan 2 espacios vertical
                gbc.gridwidth = 1;
                gbc.gridheight = 2;
                gbc.fill = GridBagConstraints.VERTICAL;
            } else {
                gbc.gridwidth = 1;
                gbc.gridheight = 1;
                gbc.fill = GridBagConstraints.BOTH;
            }

            gbc.gridx = gridx;
            gbc.gridy = gridy;

            panel.add(btn, gbc);

            gridx += gbc.gridwidth;

            if (gridx > 3) {
                gridx = 0;
                gridy++;
            }
        }

        return panel;
    }

    private JButton createButton(String label) {
        JButton btn = new JButton(label);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonText = ((JButton) e.getSource()).getText();
                txtArea.append(buttonText);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        new Calculadora();
    }
}
