package br.com.douglimar.surpresinhadiadesorte;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Douglimar Moraes on 02/12/17.
 *
 *
 */

class Surpresinha {

    public String generateDiaDeSorteGame() {

        /* Regra do Jogo:
         * O apostador pode escolher 7 numeros entre 31 numeros disponíveis
         * além de 1 Mês da Sorte
         */

        Random random = new Random();

        String[] meses = {"JANEIRO","FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO",
                "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"};

        String mes = meses[random.nextInt(12)];

        int[] numsDiaDeSorte = new int[7];

        int indice;
        StringBuilder Retorno = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            indice = random.nextInt(32);

            for (int k = 0; k < 31; k++) {
                if (consisteJogo(numsDiaDeSorte, indice) || indice == 0) {
                    indice = random.nextInt(32);
                }
            }
            numsDiaDeSorte[i] = indice;
        }

        Arrays.sort(numsDiaDeSorte);

        for (int i = 0; i < 7; i++) {

            if (numsDiaDeSorte[i] < 10)
                Retorno.append(" 0").append(numsDiaDeSorte[i]);
            else
                Retorno.append(" ").append(numsDiaDeSorte[i]);
        }

        return Retorno.toString() + "\n\n" + "MÊS DA SORTE:\n" + mes;

    }

    private boolean consisteJogo(int[] pArray, int PNumero) {

        boolean Retorno = false;

        for (int aPArray : pArray) {
            if (aPArray == PNumero) {
                Retorno = true;
                break;
            }
        }

        return Retorno;
    }

}
