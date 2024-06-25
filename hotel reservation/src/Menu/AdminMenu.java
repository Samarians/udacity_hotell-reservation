package Menu;

import API.AdminResource;
import API.IMenu;
import Model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static Menu.MainMenu.mainMenu;

public final class AdminMenu implements IMenu {
    static List<String> roomNumberList = new ArrayList<>();
    static String roomNumber;
    static RoomType roomType;
    static double roomPrice;

    private final Scanner scanner;
    private final AdminResource adminResource;
    private static IMenu adminMenu = null;


    public AdminMenu(AdminResource adminResource, Scanner scanner) {
//        this.mainMenu = mainMenu;
        this.adminResource = adminResource;
        this.scanner = scanner;
    }

    public static IMenu getInstance(AdminResource adminResource, Scanner scanner){
        if (null == adminMenu) {
            adminMenu = new AdminMenu(adminResource, scanner);
        }
        return adminMenu;
    }

    @Override
    public void displayMenu() {
        boolean display = false;
        while (!display) {
            System.out.println("""
                    Admin Menu
                    --------------------------
                    1. See all Customers
                    2. See all rooms
                    3. See all reservations
                    4. Add a room
                    5. Back to main menu
                    --------------------------
                    Please select a number for the menu option""");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    Collection<Customer> customersList = adminResource.getAllCustomers();
                    for (Customer customer : customersList) {
                        System.out.println(customer);
                    }
                }
                case "2" -> {
                    Collection<IRoom> roomsList = adminResource.getAllRooms();
                    if (roomsList.isEmpty()) {
                        System.out.println("There is no room\n");
                    }
                    for (IRoom room : roomsList) {
                        System.out.print(room + "\n");
                    }
                }
                case "3" -> adminResource.displayAllReservations();
                case "4" -> addRooms();
                case "5" -> {
                    display = true;
                    mainMenu.displayMenu();
                }

                case null, default -> System.out.println("Please choose option 1 to 5");

            }
        }
    }

    public void addRooms() {
        validateRoomNumber();
        validateRoomPrice();
        validateRoomType();

        Collection<IRoom> newRooms = new ArrayList<>();
        IRoom room;
        if (roomPrice > 0.0) {
            room = new Room(roomNumber, roomPrice, roomType);
        } else {
            room = new FreeRoom(roomNumber, 0.0, roomType);
        }
        newRooms.add(room);
        adminResource.addRoom(newRooms);
        addAnotherRoom();
    }

    public void addAnotherRoom() {
        boolean inValidAnswer = false;

        while (!inValidAnswer) {
            System.out.println("Would you like to add another room? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y")) {
                addRooms();
            } else if (answer.equalsIgnoreCase("n")) {
                inValidAnswer = true;
                displayMenu();
            } else {
                System.out.println("Please enter Y (Yes) or N (No)");
            }
        }
    }

    public void validateRoomNumber() {
        boolean isExisted = false;

        while (!isExisted) {
            System.out.println("Please enter a room number");
            roomNumber = scanner.nextLine().trim();
            if (roomNumberList.contains(roomNumber)) {
                System.out.println("This room number was registered");
            } else if (roomNumber.isBlank()) {
                System.out.println("Invalid room number.");
            } else {
                roomNumberList.add(roomNumber);
                isExisted = true;
            }
        }
    }

    public void validateRoomType() {
        System.out.println("Please enter a room type : 1 for single bed room, 2 for double bed room");
        String roomTypeInput = scanner.nextLine();
        switch (roomTypeInput) {
            case "1":
                roomType = RoomType.SINGLE;
                break;
            case "2":
                roomType = RoomType.DOUBLE;
                break;
            default:
                System.out.println("Invalid input.");
                validateRoomType();
        }
    }

    public void validateRoomPrice() {
        boolean validPrice = false;
        while (!validPrice) {
            try {
                System.out.println("Please enter a room price");
                roomPrice = Double.parseDouble(scanner.nextLine());
                validPrice = true;
            } catch (Exception ex) {
                System.out.println("Invalid input");
            }
        }
    }


}
