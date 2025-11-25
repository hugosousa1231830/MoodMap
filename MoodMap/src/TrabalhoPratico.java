import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TrabalhoPratico {
    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader("src/MoodMap.txt");
            BufferedReader reader = new BufferedReader(fileReader);

            String description = reader.readLine();

            String[] matrixSizes = reader.readLine().trim().split("\\s+");
            int numRows = Integer.parseInt(matrixSizes[0]);
            int numColumns = Integer.parseInt(matrixSizes[1]);

            int[][] moodMap = readAndParseMoodMap(reader, numRows, numColumns);

            printA(moodMap, description, numRows, numColumns);
            printB(moodMap, numRows, numColumns);
            double[] avgMoodPerDay = printC(moodMap, numRows, numColumns);
            printD(moodMap, numRows);
            printE(avgMoodPerDay);
            printF(moodMap, numRows, numColumns);
            int[] maxEmoDaysPerPerson = printG(moodMap, numRows, numColumns);
            printH(moodMap, numRows, numColumns);
            printI(maxEmoDaysPerPerson);
            printJ(moodMap,numRows,numColumns);

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Verify the file path or URL.");
        } catch (IOException e) {
            System.out.println("I/O error occurred. Check your network or file access.");
        }
    }

    private static void printA(int[][] moodMap, String description, int numRows, int numColumns) {
        System.out.println("a) " + description);
        System.out.println(numRows + " " + numColumns);
        printMatrixWithoutBracketsOrCommas(moodMap);
        printSpacingBetweenEx();
    }

    private static void printB(int[][] moodMap, int numRows, int numColumns) {
        System.out.println("b) Mood (level/day/person");
        System.out.println(getNumberOfDaysHeader(numColumns));
        for (int i = 0; i < numRows; i++) {
            System.out.println(getMoodLevelPerDay(i, moodMap[i]));
        }
        printSpacingBetweenEx();
    }

    private static double[] printC(int[][] moodMap, int numRows, int numColumns) {
        StringBuilder resultToPrint = new StringBuilder();
        resultToPrint.append("c) Average mood each day:");
        resultToPrint.append("\n" + getNumberOfDaysHeader(numColumns));
        resultToPrint.append("\nmood          ");

        int[] arrayToAverage = new int[numRows];
        double[] avgPerDay = new double[numColumns];

        for (int i = 0; i < numColumns; i++){
            for (int j = 0; j < numRows; j++){
                arrayToAverage[j] = moodMap[j][i];
            }
            avgPerDay[i] = getAverage(arrayToAverage);
            resultToPrint.append(avgPerDay[i] + "  ");
        }
        System.out.println(resultToPrint);
        printSpacingBetweenEx();
        return avgPerDay;
    }

    private static void printD(int[][] moodMap, int numRows) {
        System.out.println("d) Average of each person's mood");
        for (int i = 0; i < numRows; i++) {
            System.out.println("Person #" + i + "  :  " + getAverage(moodMap[i]));
        }
        printSpacingBetweenEx();
    }

    private static void printE (double[] avgPerDay) {
        double highestMood = avgPerDay[0];
        int[] highestDaysArray = {0};
        int highestDayIndex = 0;

        for (int i = 1; i < avgPerDay.length; i++) {
            if (avgPerDay[i] > highestMood) {
                highestMood = avgPerDay[i];
                highestDaysArray = new int[1];
                highestDaysArray[0] = i;
                highestDayIndex = 0;
            } else if (avgPerDay[i] == highestMood) {
                highestDayIndex++;
                highestDaysArray = increaseArraySize(highestDaysArray);
                highestDaysArray[highestDayIndex] = i;
            }
        }

        StringBuilder resultToPrint = new StringBuilder();
        resultToPrint.append("e) Days with the highest average mood (")
                .append(highestMood)
                .append("): ");

        for (int element : highestDaysArray) {
            resultToPrint.append(element).append(" ");
        }

        System.out.println(resultToPrint);
        printSpacingBetweenEx();
    }

    private static void printF (int[][] moodMap, int numRows, int numColumns) {
        int[] occurrancesByLevel = new int[6];
        int moodLevel;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                moodLevel = moodMap[i][j];
                occurrancesByLevel[moodLevel]++;
            }
        }

        double totalElements = numRows*numColumns;

        StringBuilder resultString = new StringBuilder();
        resultString.append("f) Percentage of mood levels:");

        for (int o = occurrancesByLevel.length - 1; o != 0; o--) {
            double percentageLevel = Math.round((occurrancesByLevel[o] * 100 / totalElements) * 10) / 10.0;
            resultString.append("\nMood #" + o + ": " + percentageLevel + "%");
        }

        System.out.println(resultString);
        printSpacingBetweenEx();
    }

    private static int[] printG (int[][] moodMap, int numRows, int numColumns) {
        // Considera-se emotional disorder se low mood (1 e 2) durante 2+ dias

        System.out.println("g) People with emotional disorders:");

        int[] maxEmoDaysPerPerson = new int [numRows];

        for (int i = 0; i < numRows; i++){
            int countEmoDays = 0;
            int maxEmoDays = 0;
            for (int j = 0; j < numColumns; j++){
                int mood = moodMap[i][j];
                if (mood > 2) {
                    countEmoDays = 0;
                    continue;
                }
                countEmoDays++;
                if (countEmoDays > maxEmoDays) {
                    maxEmoDays = countEmoDays;
                }
            }
            maxEmoDaysPerPerson[i] = maxEmoDays;
            if (maxEmoDays > 1) {
                System.out.println("Person #" + i + " : " + maxEmoDays + " consecutive days");
            }
        }
        printSpacingBetweenEx();
        return maxEmoDaysPerPerson;
    }

    private static void printH(int[][] moodMap, int numRows, int numColumns) {
        System.out.println("h) People's Mood Level Charts:");

        // para cada user
        for (int i = 0; i < numRows; i++) {

            // ver o maior nivel, para saber se se imprime todas as rows (ex person 2 so precisa de 2 niveis)
            int maxLevel = checkMaxMood(moodMap[i]);
            System.out.println("Person #" + i + ":");

            // Para cada level, vamos percorrer o array de resultados da pessoa à procura de matches com aquele level, se encontrar põe um *, caso contrário um espaço. Esta forma é pouco eficiente porque vamos estar a correr a mesma array 5 vezes, 1 por cada level
            for (int level = maxLevel; level > 0; level--) {
                StringBuilder levelRow = new StringBuilder("  " + level + "  |");
                for (int o = 0; o < numColumns; o++) {
                    if (moodMap[i][o] != level) {
                        levelRow.append(" ");
                    } else {
                        levelRow.append("*");
                    }
                }
                System.out.println(levelRow);
            }

            // Constroi e imprime a moodBar. Adiciona "-" dinâmicamente consoante o numero de colunas
            StringBuilder moodRow = new StringBuilder();
            moodRow.append("Mood +");
            moodRow.append("-".repeat(Math.max(0, numColumns)));
            System.out.println(moodRow);

            StringBuilder dayRow = new StringBuilder();
            dayRow.append("      ");

            // Como esta row so tem multiplos de 5, vamos percorrer o numero de colunas e caso seja multiplo, imprime, caso não, coloca espaço
            for (int h = 0; h < numColumns; h++) {
                dayRow.append(isMultipleOf5(h) ? h : " ");
            }
            System.out.println(dayRow);
            printSpacingBetweenEx();
        }
    }

    private static void printI (int[] maxEmoDaysPerPerson) {
        System.out.print("i) Recommended therapy:");
        for (int i = 0; i < maxEmoDaysPerPerson.length; i++) {
            int maxEmoDays = maxEmoDaysPerPerson[i];
            if (maxEmoDays > 1 && maxEmoDays < 5) {
                System.out.print("Person #" + i + "  : listen to music");
            }
            if (maxEmoDays >=5) {
                System.out.println("Person #" + i + "  : psychological support");
            }
            printSpacingBetweenEx();
        }
    }

    private static void printJ(int[][] moodMap, int numRows, int numColumns) {

        int bestScore = -1;             // Guarda o maior número de dias iguais entre um par de pessoas
        int[] bestPeople = new int[0];  // Array que vai guardar os índices das pessoas que obtiverem o score acima (pode ser mais que 2 por isso usamos array)

        for (int a = 0; a < numRows; a++) { // Percorre cada pessoa 'a'
            for (int b = a + 1; b < numRows; b++) { // Percorre cada pessoa 'b' depois de 'a', para comparar pares

                int same = 0; // Contador de dias iguais entre a e b
                for (int d = 0; d < numColumns; d++) { // Percorre todos os dias
                    if (moodMap[a][d] == moodMap[b][d]) // Se o humor da pessoa 'a' e 'b' no dia 'd' for igual
                        same++; // Incrementa o contador de dias iguais
                }

                if (same > bestScore) {           // Se este par tem mais dias iguais que o melhor até agora
                    bestScore = same;             // Atualiza o recorde de dias iguais
                    bestPeople = new int[0];      // Limpa o array de pessoas anteriores
                    bestPeople = increaseArraySize(bestPeople); // Aumenta o tamanho do array em 1
                    bestPeople[0] = a;            // Guarda a primeira pessoa do novo melhor par
                    bestPeople = increaseArraySize(bestPeople); // Aumenta novamente para guardar a segunda pessoa
                    bestPeople[1] = b;            // Guarda a segunda pessoa do novo melhor par
                }

                /*
                O else if em baixo está a ver se o same é igual a um bestScore. Isto significa que o par que está a ser
                avaliado e teve same, tem que ser adicionado à lista do bestPeople. Isto acontece quando temos por exemplo,
                person1 e person3 como bestPeople, mas depois há outro par com exactamente o mesmo numero de dias, então
                a lista tem que ser actualizada.
                 */
                else if (same == bestScore) {
                    if (!doesArrayContainValue(bestPeople, a)) { // Se a pessoa 'a' ainda não está no array
                        bestPeople = increaseArraySize(bestPeople); // Aumenta o array
                        bestPeople[bestPeople.length - 1] = a;      // Adiciona a pessoa 'a'
                    }
                    if (!doesArrayContainValue(bestPeople, b)) { // Se a pessoa 'b' ainda não está no array
                        bestPeople = increaseArraySize(bestPeople); // Aumenta o array
                        bestPeople[bestPeople.length - 1] = b;      // Adiciona a pessoa 'b'
                    }
                }
            }
        }

        System.out.println("j) People with the most similar moods: "); // Mensagem inicial
        for (int i = 0; i < bestPeople.length; i++) {   // Percorre todas as pessoas do array
            if (i > 0) {
                if (i == bestPeople.length - 1) {
                    System.out.print(" and "); // "and" antes do último
                } else {
                    System.out.print(", ");   // vírgula entre os anteriores
                }
            }
            System.out.print("Person #" + bestPeople[i]); // Imprime cada pessoa
        }
        System.out.println(" have the same mood on " + bestScore + " days"); // Mostra o número de dias iguais
    }

    private static String getNumberOfDaysHeader(int numColumns) {
        StringBuilder numberOfDays = new StringBuilder();

        numberOfDays.append("day        :");

        for (int i = 0; i < numColumns; i++) {
            numberOfDays.append("  ").append(i);
        }
        numberOfDays.append("\n----------------");
        numberOfDays.append("---".repeat(numColumns));

        numberOfDays.append("|");

        return numberOfDays.toString();
    }


    private static String getMoodLevelPerDay(int personNumber, int[] personLevels){
        StringBuilder moodPerDay = new StringBuilder();
        moodPerDay.append("Person #" + personNumber + "  :");
        for (int day : personLevels) {
            moodPerDay.append("  ").append(day);
        }
        return moodPerDay.toString();
    }

    private static void printMatrixWithoutBracketsOrCommas(int[][] moodMap){
        for (int[] ints : moodMap) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    private static double getAverage(int[] array) {
        int sum = 0;
        for (int j : array) {
            sum += j;
        }
        return Math.round(((double) sum / array.length) * 10) / 10.0;
    }

    private static void printSpacingBetweenEx() {
        System.out.println(" ");
    }

    private static int[] increaseArraySize(int[] array) {
        int[] newArray = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    private static int[][]readAndParseMoodMap (BufferedReader reader, int numRows, int numColumns) throws IOException {
        int[][] moodMap = new int[numRows][numColumns];
        for (int i = 0; i < numRows; i++ ) {
            String[] stringArray = reader.readLine().trim().split("\\s+");
            for (int o = 0; o < numColumns; o++) {
                moodMap[i][o] = Integer.parseInt(stringArray[o]);
            }
        }
        return moodMap;
    }

    private static int checkMaxMood(int[] moodArray) {
        int maxMood = moodArray[0];
        for (int mood : moodArray){
            if (mood > maxMood) {
                maxMood = mood;
            }
        }
        return maxMood;
    }

    private static boolean isMultipleOf5 (int num) {
        return num % 5 == 0;
    }

    private static boolean doesArrayContainValue(int[] arr, int value) {
        for (int i : arr) if (i == value) return true;
        return false;
    }
}