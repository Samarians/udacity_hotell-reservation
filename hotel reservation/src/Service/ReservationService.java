package Service;

import Model.*;

import java.util.*;



public class ReservationService {
    //public map of room to store created room
    Set<Reservation> reservationsSet;
    Set<IRoom> roomsList;
    private static ReservationService reservationService = null;

    private ReservationService() {
        reservationsSet = new HashSet<>();
        roomsList = new HashSet<>();
    }

    public static ReservationService getInstance() {
        if (null == reservationService) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }

    //put an IRoom object to the map of room by roomId
    public void addRoom(IRoom room) {
        roomsList.add(room);
    }

    //get a IRoom object from map of room by roomId
    public IRoom getARoom(String roomId) {
        for (IRoom room : roomsList) {
            if(Objects.equals(roomId, room.getRoomNumber())) {
                return room;
            }
        }
        return null;
    }

    public Set<IRoom> getAllRooms(){
        return roomsList;
    }

    //Reserve a new room
    public void reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation newReservation =  new Reservation(customer, room, checkInDate, checkOutDate);
        reservationsSet.add(newReservation);
    }

    //Find a list of available rooms by check in and check out date
    public Collection<IRoom> findRooms(Date dateCheckIn, Date dateCheckOut) {
        Collection<IRoom> availRooms = new ArrayList<>();
        if (reservationsSet.isEmpty()) {
            availRooms = roomsList;
        } else {
            for (IRoom room : roomsList) {
                for (Reservation reservation : reservationsSet) {
                    if ((room.getRoomNumber().equals(reservation.getRoom().getRoomNumber()))
                            && (dateCheckOut.before(reservation.getCheckInDate())
                            || dateCheckIn.after(reservation.getCheckOutDate()))
                            || (!reservation.getRoom().getRoomNumber().contains(room.getRoomNumber()))) {
                        availRooms.add(room);
                    }
                }
            }
        }
        return availRooms;
    }

    //Print all reservation by customer
    public Collection<Reservation> getCustomerReservation (Customer customer) {
        Collection<Reservation> getCustomerReservations = new ArrayList<>();
        for (Reservation reservation : reservationsSet) {
            if (reservation.getCustomer().equals(customer)) {
                getCustomerReservations.add(reservation);
            }
        }
        return getCustomerReservations;
    }

    //Print all reservations
    public void printAllReservation() {
        if (!reservationsSet.isEmpty()) {
            for (Reservation reservation : reservationsSet) {
                System.out.println(reservation);
        }
    } else {
        System.out.println("There is no reservations.");}
    }
}






