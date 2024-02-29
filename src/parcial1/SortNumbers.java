package parcial1;

import java.util.Arrays;

public class SortNumbers {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No hay numeros a ordenar");
            return;
        }

        double[] numbers = new double[args.length];
        for (int i = 0; i < args.length; i++) {
            numbers[i] = Double.parseDouble(args[i]);
        }

        Arrays.sort(numbers);

        System.out.println("Números ordenados de menor a mayor:");
        for (double num : numbers) {
            System.out.println(num);
        }
    }
}
