package API;

import Service.CustomerService;
import Service.ReservationService;
import Model.Customer;
import Model.IRoom;
import java.util.Collection;

public class AdminResource {
    private final CustomerService customerService;
    private final ReservationService reservationService;
    private static AdminResource adminResource = null;

    public AdminResource(CustomerService customerService,
                         ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public static AdminResource getInstance(CustomerService customerService,
                                            ReservationService reservationService){
        if (null == adminResource) {
            adminResource = new AdminResource(customerService, reservationService);
        }
        return adminResource;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(Collection<IRoom> rooms) {
        for (IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }

    public Collection<Customer> getAllCustomers (){
        return customerService.getAllCustomers();
    }
}
