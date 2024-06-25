package Model;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, double price, RoomType roomType) {
        super(roomNumber, 0.0, roomType);
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public String toString() {
        return "FreeRoom [room number: " + roomNumber + ", price $:" + price + ", room type: " + roomType + "]";
    }
}

