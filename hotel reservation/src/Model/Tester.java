package Model;

import java.util.Scanner;

public class Tester {
    public static void main(String[] args) {
        boolean isRun = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (isRun) {
                try {
                    System.out.println("Please enter your first name: ");
                    String firstName = scanner.nextLine();
                    System.out.println("Please enter your last name: ");
                    String lastName = scanner.nextLine();
                    System.out.println("Please enter your email: ");
                    String email = scanner.nextLine();
                    Customer customer = new Customer(firstName, lastName, email);
                    System.out.println(customer);
                    isRun = false;
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }
    }
}