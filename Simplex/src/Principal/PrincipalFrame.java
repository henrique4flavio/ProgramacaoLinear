package Principal;

import Simplex.SimplexFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class PrincipalFrame extends JFrame {

    private JButton btnIniciar;
    private JPanel pnlExtra;
    private JLabel txt;

    public PrincipalFrame() {
        super("Método Simplex");
        super.setSize(450, 100);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setLayout(new BorderLayout());
        super.setJMenuBar(createMenu());

        pnlExtra = new JPanel();

        btnIniciar = new JButton("Iniciar");
        txt = new JLabel("Trabalho de Programação Linear: Jonathas Flavio e Lais ");

        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                SimplexFrame sf = new SimplexFrame();
                sf.setVisible(true);

            }

        });

        pnlExtra.add(txt);
        pnlExtra.add(btnIniciar);

        super.add(pnlExtra, BorderLayout.CENTER);
        super.setVisible(true);
        super.setResizable(false);
        super.setLocationRelativeTo(null);
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();

        return menu;
    }
}
