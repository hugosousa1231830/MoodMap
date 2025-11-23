//import java.util.Scanner;
//
//public class Main {
//
//    /* Objetivo do programa:
//     1. Ler vários números do utilizador e armazená-los num array. Parar de ler quando o utilizador insere um número negativo.
//     2. Encontrar o menor número no array.
//     3. Contar quantas vezes esse menor número aparece.
//
//    Nesta resolução fiz uma abordagem diferente. Como vimos na resolução de hoje, simplesmente criar um array com
//    um numero fixo de elementos faz com que as casas que não são "ocupadas" se tornem zeros. Ora isto chateia um bocado,
//    porque como estamos a tentar encontrar o numero mais pequeno, esses zeros são contabilizados como se fossem o user a inserir.
//
//    Para resolver este problema criei um metodo abaixo chamado ampliarArray, que basicamente recebe uma array e copia
//    o conteúdo para uma nova array com +1 de tamanho. Isto permite-nos dinâmicamente ampliar a array como precisamos.
//    E como vamos chamar este metodo N vezes, mais vale o código estar num metodo à parte.
//
//    Ou seja, se o utilizador inserir 5 numeros, a cada for loop vai ser criada um array novo tipo isto:
//    - new int[0]
//    - new int[1]
//    - new int[2]
//    - new int[3]
//    - new int[4]
//
//    Uma nota importante que pode confundir: Quando estamos a criar um array, o numero que colocamos dentro dos [] é o
//    número de casas que queremos que o array tenha:
//    - new int[0] -> 0 casas
//    - new int[1] -> 1 casas
//    - new int[2] -> 2 casas
//    - new int[3] -> 3 casas
//    - new int[4] -> 4 casas
//
//   Neste exemplo o programa parte porque não tem casas disponíveis:
//
//   int[] numeros = new int[0]
//   numeros[0] = 5       (Programa parte)
//
//   Para conseguirmos utilizar o index 0 de um array temos de criar um array com 1 casa, ou seja isto já funcionaria:
//
//   int[] numeros = new int[1]
//   numeros[0] = 5       (OK)
//   numeros[1] = 7       (Programa parte de novo)
//
//   Espero que faça sentido!
//     */
//
//
//    public static void main(String[] args) {
//        Scanner input = new Scanner(System.in);
//
//        // começamos por pedir o primeiro numero e guardamos no novoNumero
//        System.out.println("APP: Please enter a few numbers. Once you are happy, just insert a negative one. I'll tell you the minimum and how often it appeared!");
//
//        int novoNumero = input.nextInt();
//
//        // Criamos inicialmente um array vazio
//        int[] listaNumeros = new int[0];
//        int index = 0;
//
//        /* Continuamos a pedir numeros enquanto o número for não-negativo. Para cada número, o programa vai pegar no
//        array de numeros existente e amplia 1 casa, para poder receber o novo numero. O index é usado para percebermos
//        quantas casas já ocupamos
//         */
//        while (novoNumero >= 0) {
//            listaNumeros = ampliarArray(listaNumeros);
//            listaNumeros[index] = novoNumero;
//            index++;
//            novoNumero = input.nextInt();
//        }
//
//        // Inicializamos o menor número com o primeiro elemento
//        int min = listaNumeros[0];
//        int numeroOcorrencias = 1;
//
//        // Percorremos a lista de numeros e vemos os 2 casos presents no if
//        for (int i = 1; i < listaNumeros.length; i++){
//            if (listaNumeros[i] < min) {      // Caso o numero novo seja inferior ao min, o min torna-se o numero novo
//                min = listaNumeros[i];
//                numeroOcorrencias = 1;
//            }
//            if (listaNumeros[i] == min) {     // Caso o numero novo seja igual ao min, aumentamos o contador de ocorrencia desse numero
//                numeroOcorrencias++;
//            }
//        }
//
//        System.out.printf("Minimum=%d%nOccurrences=%d%n", min, numeroOcorrencias);
//    }
//
//    /*
//     * Metodo para aumentar dinamicamente o tamanho de um array
//     * Recebe um array e retorna um novo array com tamanho +1,
//     * copiando os elementos do array antigo.
//     */
//    public static int[] ampliarArray(int[] arrayParaAmpliar) {
//        int[] arrayAmpliada = new int[arrayParaAmpliar.length + 1];   // criamos um array novo com +1 casa que o antigo
//
//        for (int i = 0; i < arrayParaAmpliar.length; i++) {    // vamos index a index de cada array copiando dum lado para o outro.
//            arrayAmpliada[i] = arrayParaAmpliar[i];
//        }
//        return arrayAmpliada;
//    }
//}