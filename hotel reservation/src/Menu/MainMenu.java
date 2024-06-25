package Menu;

import API.HotelResource;
import API.IMenu;
import Model.IRoom;
import Model.Reservation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class MainMenu implements IMenu {
    public Collection<IRoom> rooms = new ArrayList<>();
    private final Collection<String> registeredEmail = new ArrayList<>();

    private final IMenu adminMenu;
    private final HotelResource hotelResource;
    private final Scanner scanner;
    public static IMenu mainMenu = null;

    public MainMenu(IMenu adminMenu, HotelResource hotelResource, Scanner scanner) {
        this.scanner = scanner;
        this.adminMenu = adminMenu;
        this.hotelResource = hotelResource;
    }

    public static IMenu getInstance(IMenu adminMenu,
                                    HotelResource hotelResource,
                                    Scanner scanner) {
        if (null == mainMenu) {
            mainMenu = new MainMenu(adminMenu, hotelResource, scanner);
        }
        return mainMenu;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("""
                    Main Menu
                    --------------------------
                    1. Find and reserve a room
                    2. See my reservation
                    3. Create an account
                    4. Admin
                    5. Exit
                    --------------------------
                    Please select a number for the menu option""");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> optionOne();
                case "2" -> optionTwo();
                case "3" -> optionThree();
                case "4" -> adminMenu.displayMenu();
                case "5" -> {
                    System.out.println("See you again.");
                    System.exit(0);
                }
                default -> System.out.println("Please choose option 1 to 5");
            }
        }
    }

    private void optionOne() {
        List<String> availableRooms = new ArrayList<>();
        System.out.println(availableRooms);
        Date checkIn = null;
        Date checkOut = null;
        boolean validCheckIn = false;
        boolean validCheckOut = false;
        boolean outAfterIn = false;

        while (!validCheckIn) {
            System.out.println("Enter check-in date: mm-dd-yyyy");
            String date1 = scanner.nextLine();
            try {
                checkIn = new SimpleDateFormat("MM-dd-yyyy").parse(date1);
                validCheckIn = true;
            } catch (ParseException ex) {
                System.out.println("Invalid date.");
            }
        }

        while (!validCheckOut || !outAfterIn) {
            System.out.println("Enter check-out date: mm-dd-yyyy");
            String date2 = scanner.nextLine();
            try {
                checkOut = new SimpleDateFormat("MM-dd-yyyy").parse(date2);
                validCheckOut = true;
                if (checkOut.after(checkIn)) {
                    outAfterIn = true;
                } else {
                    System.out.println("Check out date must be after check in date.");
                }
            } catch (ParseException ex) {
                System.out.println("Invalid date.");
            }
        }

        rooms = hotelResource.findARoom(checkIn, checkOut);
        if (!availableRooms.isEmpty()) {
            for (IRoom room : rooms) {
                System.out.println(room);
                availableRooms.add(room.getRoomNumber());
            }
            reserveARoom(checkIn, checkOut, availableRooms);
        } else {
            System.out.println("There is no available room from " + checkIn + " to " + checkOut + "\n");
            suggestion(checkIn, checkOut, availableRooms);
        }
    }

    private void optionTwo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your email (name@domain.com):");
        String email = scanner.nextLine();
        if (!registeredEmail.contains(email)) {
            System.out.println("Email does not exists\n");
        } else {
            Collection<Reservation> reservationList = hotelResource.getCustomerReservations(email);
            if (reservationList.isEmpty()) {
                System.out.println("Customer has no reservation.\n");
            } else {
                for (Reservation reservation : reservationList) {
                    System.out.println(reservation);
                }

            }
        }
    }

    private void optionThree() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account is created successfully\n");
            registeredEmail.add(email);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            optionThree();
        }
    }


    private void reserveARoom(Date checkIn, Date checkOut, List<String> availableRooms) {
        boolean haveAccount;
        String email;
        while (true) {
            if (!isReserve()) {
                break;
            } else {
                haveAccount = haveAccount();
            }
            if (!haveAccount) {
                break;}
            else {
                email = validEmail();
            }
            if (email != null) {
                chooseRoom(email, checkIn, checkOut, availableRooms);
                break;
            }
        }
    }


    private boolean isReserve() {
        String answer;
        boolean isReserve = false;

        do {
            System.out.println("Would you like to reserve a room? (Y/N)");
            answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y")) {
                isReserve = true;
                break;
            } else if (answer.equals("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter Y (Yes) or N (No)");
            }
        } while (true);

        return isReserve;
    }

    private boolean haveAccount() {
        String answer;
        boolean haveAccount = false;

        do {
            System.out.println("Do you have an account with us? y/n");
            answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y")) {
                haveAccount = true;
                break;
            } else if (answer.equals("n")) {
                System.out.println("Please register an account.\n");
                break;
            } else {
                System.out.println("Invalid input. Please enter Y (Yes) or N (No)");
            }
        } while (true);
        return haveAccount;
    }

    private String validEmail() {
        String validEmail = null;
        String answer;
        int n = 0;
        do {
            System.out.println("Enter your email (name@domain.com)");
            answer = scanner.nextLine().trim();

            if (!registeredEmail.contains(answer)) {
                System.out.println("Email does not exist.");
                n += 1;
            } else {
                validEmail = answer;
                break;
            }
        } while (n < 3);
        return validEmail;
    }

    private void chooseRoom(String email, Date checkIn, Date checkOut, List<String> availableRooms) {
        do {
            System.out.println("Which room would you like to reserve?");
            System.out.println("Available rooms " + availableRooms);
            String roomNumber = scanner.nextLine();
            if (availableRooms.contains(roomNumber)) {
                hotelResource.bookARoom(email, hotelResource.getRoom(roomNumber), checkIn, checkOut);
                availableRooms.remove(roomNumber);
                System.out.println(availableRooms);
                break;
            } else {
                System.out.println("This room is not available on your chosen time.");
            }
        } while (true);
    }

    private void suggestion(Date checkIn, Date checkOut, List<String> availableRooms) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(checkIn);
        c2.setTime(checkOut);
        c1.add(Calendar.DATE, 7);
        c2.add(Calendar.DATE, 7);
        checkIn = c1.getTime();
        checkOut = c2.getTime();

        Collection<IRoom> rooms = hotelResource.findARoom(checkIn, checkOut);
        if (!rooms.isEmpty()) {
            System.out.println("There are available rooms on the next 7 days, from " + checkIn + " to " + checkOut +"\n");

            for (IRoom room : rooms) {
                availableRooms.add(room.getRoomNumber());
                System.out.println(room);
            }
            reserveARoom(checkIn, checkOut, availableRooms);
        }
    }
}


