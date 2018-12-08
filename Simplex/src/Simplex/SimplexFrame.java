package Simplex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class SimplexFrame extends JFrame {

    private JButton btnAceitar;
    private JTable JTabelaUm;
    private JScrollPane jScrollPane1;
    private JTextField txtVariaveis;
    private JTextField txtRestricoes;
    private JLabel lblVariaveis;
    private JLabel lblRestricoes;
    private JComboBox tipoProblema;
    private JPanel pnlNorth;
    private JLabel lblTipoProblema;
    private JTable jTabelaSolucao;
    private JScrollPane jScrollPane2;

    double Matriz[][] = null;
    int restricoes = 0, variaveis = 0, iter = 0, iteracao = 0;
    Object array[] = null;
    Object EtiquetaX[] = null, EtiquetaY[] = null;
    static DecimalFormat df = new DecimalFormat("#.##");
    private JComboBox comboBox;
    private String menorIgual = "<=";
    private String maiorIgual = ">=";
    private String igual = "=";
    private boolean opc;

    public SimplexFrame() {
        super("Método Simplex");
        super.setSize(new Dimension(800, 600));
        super.setLayout(new BorderLayout());
        super.setResizable(false);
        super.setLocationRelativeTo(null);
        pnlNorth = new JPanel(new FlowLayout());

        lblRestricoes = new JLabel("Número de Restrições: ");
        lblVariaveis = new JLabel("Número de Variaveis: ");
        lblTipoProblema = new JLabel("Tipo de problema: ");
        txtRestricoes = new JTextField(3);
        txtVariaveis = new JTextField(3);
        tipoProblema = new javax.swing.JComboBox();
        tipoProblema.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Max", "Min"}));

        JButton gerarQuadro = new JButton();
        gerarQuadro.setText("Montar Quadro");
        gerarQuadro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {

                    restricoes = Integer.parseInt(txtRestricoes.getText());
                    variaveis = Integer.parseInt(txtVariaveis.getText());

                    DefaultTableModel modelo = new DefaultTableModel();
                    modelo.setRowCount(restricoes + 1);
                    modelo.setColumnCount(variaveis + restricoes + 3);
                    array = new Object[variaveis + restricoes + 2];
                    Object[] array2 = new Object[variaveis + restricoes + 3];
                    array[0] = "";
                    array2[0] = "";
                    EtiquetaX = new Object[variaveis + restricoes];
                    for (int i = 1; i < array.length - 1; i++) {
                        if (i < variaveis + 1) {
                            array[i] = "X" + i;
                            array2[i] = "X" + i;
                            EtiquetaX[i - 1] = "X" + i;
                        } else {
                            array[i] = "F" + (i - variaveis);
                            array2[i] = "F" + (i - variaveis);
                            EtiquetaX[i - 1] = "F" + (i - variaveis);
                        }
                    }
                    // ----------------------
                    array[array.length - 1] = "B";
                    array2[array2.length - 2] = "Símbolo";
                    array2[array2.length - 1] = "B";
                    modelo.setColumnIdentifiers(array2);
                    // ---------------------
                    EtiquetaY = new Object[restricoes + 1];
                    for (int i = 0; i < restricoes; i++) {
                        modelo.setValueAt("F" + (i + 1), i, 0);
                        modelo.setValueAt(menorIgual, i, modelo.getColumnCount() - 2);
                        EtiquetaY[i] = "F" + (i + 1);
                    }
                    modelo.setValueAt("Z", restricoes, 0);
                    EtiquetaY[restricoes] = "Z";

                    JTabelaUm.setModel(modelo);
                    comboBox = new JComboBox();
                    comboBox.addItem(maiorIgual);
                    comboBox.addItem(menorIgual);
                    comboBox.addItem(igual);

                    TableColumn sinal = JTabelaUm.getColumnModel().getColumn(JTabelaUm.getColumnCount() - 2);
                    sinal.setCellEditor(new DefaultCellEditor(comboBox));

                    for (int i = 0; i < restricoes; i++) {
                        JTabelaUm.removeColumn(JTabelaUm.getColumnModel().getColumn(variaveis + 1));
                    }
                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(SimplexFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                            null, e);
                }
            }
        });
        JButton calcular = new JButton();
        calcular.setText("Calcular");
        calcular.setVisible(true);
        calcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    opc = tipoProblema.getSelectedIndex() == 0;

                    for (int i = 0; i < JTabelaUm.getModel().getRowCount(); i++) {

                        for (int j = 0; j < restricoes; j++) {
                            JTabelaUm.getModel().setValueAt("0", i, variaveis + 1 + j);
                        }

                        if (i != JTabelaUm.getModel().getRowCount() - 1) {
                            Object o = JTabelaUm.getModel().getValueAt(i, JTabelaUm.getModel().getColumnCount() - 2);
                            if (o.equals(menorIgual) || o.equals(maiorIgual)) {
                                JTabelaUm.getModel().setValueAt("1", i, variaveis + 1 + i);
                            } else {
                                JTabelaUm.getModel().setValueAt("0", i, variaveis + 1 + i);
                            }
                        }
                    }

                    JTabelaUm.getModel().setValueAt("0", JTabelaUm.getModel().getRowCount() - 1,
                            JTabelaUm.getModel().getColumnCount() - 1);

                    DefaultTableModel modeloSolucao = new DefaultTableModel();
                    Matriz = new double[restricoes + 1][restricoes + variaveis + 1];
                    for (int i = 0; i < (restricoes + 1); i++) {
                        boolean sw = ((i == restricoes) && opc)
                                || JTabelaUm.getModel().getValueAt(i, JTabelaUm.getModel().getColumnCount() - 2) != null
                                && (JTabelaUm.getModel()
                                        .getValueAt(i, JTabelaUm.getModel().getColumnCount() - 2)
                                        .equals(maiorIgual));

                        for (int j = 0; j < (restricoes + variaveis + 1); j++) {
                            double d;
                            if (j == (restricoes + variaveis)) {// para saltarse el sinal
                                d = convert(JTabelaUm.getModel().getValueAt(i, j + 2).toString());
                                Matriz[i][j] = sw ? d * -1 : d;
                            } else {
                                d = convert(JTabelaUm.getModel().getValueAt(i, j + 1).toString());
                                Matriz[i][j] = sw ? d * -1 : d;
                            }

                        }
                    }

                    while (checarResultado() != true) {
                        imprimirMatriz();
                        EtiquetaY[fila()] = EtiquetaX[colunaPivo()];
                        novaTabela(fila(), colunaPivo());
                        modeloSolucao.setColumnCount(restricoes + variaveis + 2);
                        modeloSolucao.setRowCount(restricoes + 1);
                        // --------------------------
                        modeloSolucao.setColumnIdentifiers(array);
                        // ---------------------------
                        for (int i = 0; i < (restricoes + 1); i++) {
                            modeloSolucao.setValueAt(EtiquetaY[i], i, 0);
                            for (int j = 0; j < (restricoes + variaveis + 1); j++) {
                                modeloSolucao.setValueAt(Matriz[i][j], i, j + 1);
                            }
                        }

                        jTabelaSolucao.setModel(modeloSolucao);

                        SimplexFrame.this.repaint();
                        iteracao++;
                    }
                    imprimirMatriz();

                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(SimplexFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                            null, e);
                }
            }
        });

        pnlNorth.add(lblVariaveis);
        pnlNorth.add(txtVariaveis);
        pnlNorth.add(lblRestricoes);
        pnlNorth.add(txtRestricoes);
        pnlNorth.add(gerarQuadro);
        pnlNorth.add(lblTipoProblema);
        pnlNorth.add(tipoProblema);
        pnlNorth.add(calcular);
        pnlNorth.setBackground(Color.WHITE);

        JTabelaUm = new JTable();

        jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(JTabelaUm);
        btnAceitar = new JButton();
        jTabelaSolucao = new JTable();
        jScrollPane2 = new JScrollPane();
        jScrollPane2.setViewportView(jTabelaSolucao);
        jScrollPane1.setBackground(Color.white);
        jScrollPane2.setBackground(Color.yellow);
        SimplexFrame.this.add(jScrollPane2, BorderLayout.SOUTH);
        super.add(pnlNorth, BorderLayout.NORTH);
        super.add(jScrollPane1, BorderLayout.CENTER);
        super.setVisible(true);
    }

    // MÉTODOS SIMPLEX
    // MÉTODOS
    private void imprimirMatriz() {
        System.out.println("\n\n\n");
        for (int i = 0; i < restricoes + 1; i++) {
            for (int j = 0; j < restricoes + variaveis + 1; j++) {
                System.out.print(Matriz[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public static double convert(String s) {
        return Double.parseDouble(s);
    }

    public int colunaPivo() {
        int pos = 0;
        double aux = Matriz[restricoes][0];
        for (int i = 1; i < restricoes + variaveis; i++) {
            if (aux > Matriz[restricoes][i]) {
                aux = Matriz[restricoes][i];
                pos = i;
            }
        }
        return pos;
    }

    public int fila() {
        int coluna = colunaPivo();
        double temp = 0;
        Double razon = null;
        int pos = 0;
        for (int i = 0; i < restricoes; i++) {
            double columPiv = Matriz[i][coluna];
            double columSoluc = Matriz[i][variaveis + restricoes];
            if ((columPiv > 0 && columSoluc > 0) || (columPiv < 0 && columSoluc < 0)) {
                temp = columSoluc / columPiv;
                if ((razon == null) || temp < razon) {
                    razon = temp;
                    pos = i;
                }
            }

        }
        return pos;
    }

    public void novaTabela(int Fila, int Columna) {
        double pivo = Matriz[Fila][Columna], temp = 0;// --
        System.out.println("pivo [" + Fila + "]" + "[" + Columna + "]=" + pivo);
        for (int i = 0; i < restricoes + variaveis + 1; i++) {
            Matriz[Fila][i] = Matriz[Fila][i] / pivo;
        }
        for (int i = 0; i < restricoes + 1; i++) {
            temp = Matriz[i][Columna];
            for (int j = 0; j < variaveis + restricoes + 1; j++) {
                if (i != Fila) {
                    Matriz[i][j] = Matriz[i][j] - temp * Matriz[Fila][j];
                } else {
                    break;
                }
            }
        }
    }

    public boolean checarResultado() {
        boolean result = true;
        for (int i = 0; i < restricoes; i++) {
            if (((Matriz[restricoes][i] < 0))) {
                result = false;
                break;
            }
        }
        return result;
    }

}
