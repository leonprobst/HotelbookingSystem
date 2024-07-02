public class Room {
    private String type;
    // Anzahl verfügbarer Personen
    private int capacity;
    private int price;
    // Anzahl verfügbarer Räume
    private int number;

    public Room(String type, int capacity, int price, int number) {
        this.type = type;
        this.capacity = capacity;
        this.number = number;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPrice() {
        return price;
    }

    public int getNumber() { return number; }

    public void setNumber(int number) { this.number = number; }

    @Override
    public String toString() {
        return "Type: " + type + " - Capacity: " + capacity
                + " - Price: $" + price + " - Available: " + number;}
}
