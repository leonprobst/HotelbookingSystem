public class Room {
    private String type;
    private int capacity;
    private int price;

    public Room(String type, int capacity, int price) {
        this.type = type;
        this.capacity = capacity;
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

    @Override
    public String toString() {
        return "Type: " + type + " - Capacity: " + capacity + " - Price: $" + price;
    }
}
