package Simplex;

import java.text.DecimalFormat;
import javax.swing.JComboBox;

public class MetodoSimplex {

    double Matriz[][] = null;
    int restricoes = 0, variaveis = 0, iter = 0, iteracao = 0;
    Object array[] = null;
    Object EtiquetaX[] = null, EtiquetaY[] = null;
    static DecimalFormat df = new DecimalFormat("#.##");
    private String menorIgual = "<=";
    private String maiorIgual = ">=";
    private String igual = "=";

    // MÉTODOS
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
        Double razao = null;
        int pos = 0;
        for (int i = 0; i < restricoes; i++) {
            double colunaPiv = Matriz[i][coluna];
            double colunaSoluc = Matriz[i][variaveis + restricoes];
            if ((colunaPiv > 0 && colunaSoluc > 0) || (colunaPiv < 0 && colunaSoluc < 0)) {
                temp = colunaSoluc / colunaPiv;
                if ((razao == null) || temp < razao) {
                    razao = temp;
                    pos = i;
                }
            }

        }
        return pos;
    }

    public void novaTabela(int Fila, int Coluna) {

        double pivo = Matriz[Fila][Coluna], temp = 0;
        System.out.println("pivo [" + Fila + "]" + "[" + Coluna + "]=" + pivo);
        for (int i = 0; i < restricoes + variaveis + 1; i++) {
            Matriz[Fila][i] = Matriz[Fila][i] / pivo;
        }
        for (int i = 0; i < restricoes + 1; i++) {
            temp = Matriz[i][Coluna];
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
