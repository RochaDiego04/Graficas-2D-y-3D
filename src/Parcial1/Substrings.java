package Parcial1;

public class Substrings {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Parcial1.Substrings <cadena>");
            return;
        }

        String cadena = args[0];

        for (int i = 0; i < cadena.length(); i++) {
            System.out.println(cadena.substring(0, cadena.length() - i));
        }

        for (int j = cadena.length() - 1; j >= 0; j--) {
            System.out.println(cadena.substring(j));
        }
    }
}
