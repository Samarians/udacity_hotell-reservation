package API;

import Model.Reservation;
import Service.CustomerService;
import Service.ReservationService;
import Model.Customer;
import Model.IRoom;
import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private  final CustomerService customerService;
    private  final ReservationService reservationService;
    private static HotelResource hotelResource = null;



    public HotelResource(CustomerService customerService,
                         ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public static HotelResource getInstance(CustomerService customerService,
                                            ReservationService reservationService){
        if (null == hotelResource) {
            hotelResource = new HotelResource(customerService, reservationService);
        }
        return hotelResource;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public void bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        reservationService.reserveARoom(customerService.getCustomer(customerEmail),
                room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) {
        return reservationService.getCustomerReservation(getCustomer(customerEmail));
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }
}
