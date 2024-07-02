public class Booking {
    private String name;
    private String roomType;
    private String date;
    private int personCount;

    public Booking(String name, String roomType, String date, int personCount) {
        this.name = name;
        this.roomType = roomType;
        this.date = date;
        this.personCount = personCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    @Override
    public String toString() {
        return name + " - Room Type: " + roomType + " - Date: " + date + " - Persons: " + personCount;
    }
}