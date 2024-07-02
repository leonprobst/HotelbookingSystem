import java.util.*;

public class RoomAvailability {

    private List<DatumTypeNumberTriple> triples;

    public RoomAvailability() {
        this.triples = new ArrayList<>();
    }

    // Methode zum Hinzufügen oder Aktualisieren eines Tripels
    public void addOrUpdateTriple(String date, String type, int number) {
        // Überprüfen, ob ein Tripel mit dem gegebenen Datum bereits vorhanden ist
        for (DatumTypeNumberTriple triple : triples) {
            if (triple.getDate().equals(date) && triple.getType().equals(type)) {
                // Tripel gefunden, nur die Anzahl aktualisieren
                triple.setNumber(number);
                return;
            }
        }
        // Falls kein entsprechendes Datum gefunden wurde, ein neues Tripel hinzufügen
        DatumTypeNumberTriple newTriple = new DatumTypeNumberTriple(date, type, number);
        triples.add(newTriple);
    }

    // Getter-Methode für das gesamte Array
    public List<DatumTypeNumberTriple> getTriples() {
        return triples;
    }

    // Innere Klasse für das Datum-Typ-Zahl-Tripel
    private static class DatumTypeNumberTriple {
        private String date;
        private String type;
        private int number;

        public DatumTypeNumberTriple(String date, String type, int number) {
            this.date = date;
            this.type = type;
            this.number = number;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    // Beispiel für die Verwendung der Klasse
    public static void main(String[] args) {
        RoomAvailability array = new RoomAvailability();

        // Hinzufügen oder Aktualisieren von Werten
        array.addOrUpdateTriple("01-01-2023", "single", 10);
        array.addOrUpdateTriple("02-01-2023", "double", 5);
        array.addOrUpdateTriple("01-01-2023", "suite", 3); // Datum und Typ bereits vorhanden, Zahl wird aktualisiert

        // Ausgabe der gespeicherten Tripel
        List<DatumTypeNumberTriple> triples = array.getTriples();
        for (DatumTypeNumberTriple triple : triples) {
            System.out.println("Datum: " + triple.getDate() + ", Typ: " + triple.getType() + ", Belegt: " + triple.getNumber());
        }
    }
}
