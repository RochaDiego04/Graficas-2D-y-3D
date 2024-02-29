package Parcial1;

import java.util.Random;

public class RandomNumbers {

        public static void main(String[] args) {
            Random random = new Random();
            int numeroAleatorio1 = random.nextInt();
            int numeroAleatorio2 = random.nextInt();
            int numeroMayor;

            if (numeroAleatorio1 > numeroAleatorio2) {
                numeroMayor = numeroAleatorio1;
            } else {
                numeroMayor = numeroAleatorio2;
            }

            System.out.println("Número aleatorio 1: " + numeroAleatorio1);
            System.out.println("Número aleatorio 2: " + numeroAleatorio2);
            System.out.println("El numero mayor es: " + numeroMayor);
        }
}
