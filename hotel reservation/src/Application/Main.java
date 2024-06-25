package Application;
import API.AdminResource;
import API.HotelResource;
import API.IMenu;
import Menu.AdminMenu;
import Menu.MainMenu;
import Service.CustomerService;
import Service.ReservationService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Starting application...");
            CustomerService customerService = CustomerService.getInstance();
            ReservationService reservationService = ReservationService.getInstance();
            HotelResource hotelResource = HotelResource.getInstance(customerService, reservationService);
            AdminResource adminResource = AdminResource.getInstance(customerService, reservationService);
            IMenu adminMenu = AdminMenu.getInstance(adminResource, scanner);
            IMenu mainMenu = MainMenu.getInstance(adminMenu, hotelResource, scanner);

            mainMenu.displayMenu();
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Application finished.");
    }
}
