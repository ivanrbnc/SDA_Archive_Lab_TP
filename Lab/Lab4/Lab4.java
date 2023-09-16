import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi Class Lantai
// Node
class Lantai {
    Lantai prev;
    Lantai next;
    int whichFloor;

    public Lantai(Lantai prev, Lantai next, int whichFloor) {
        this.prev = prev;
        this.next = next;
        this.whichFloor = whichFloor;
    }
}

// Linked list
// TODO: Lengkapi Class Gedung
class Gedung {
    Lantai headLantai;
    Lantai tailLantai;
    String name;
    int whichBuilding; // index
    int removeCounter;

    public Gedung(String name, int whichBuilding) {
        this.name = name;
        this.whichBuilding = whichBuilding;
        this.removeCounter = 0;
    }
}

// Iterator
// TODO: Lengkapi Class Karakter
class Karakter {
    Gedung buildingNow;
    Lantai floorNow;
    char direction; // + : Going up, - : Going down

    public Karakter(Gedung buildingNow, Lantai floorNow, char direction) {
        this.buildingNow = buildingNow;
        this.floorNow = floorNow;
        this.direction = direction;
    }

}

public class Lab4 {

    private static InputReader in;
    static PrintWriter out;

    static Karakter denji;
    static Karakter iblis;
    static Gedung[] komplek;
    static int meetUp = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt();
        komplek = new Gedung[jumlahGedung];

        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            // TODO: Inisiasi gedung pada kondisi awal
            komplek[i] = new Gedung(namaGedung, i);

            // Initiate Floors for every Building
            for (int j = 0; j < jumlahLantai; j++) {
                Lantai temp = new Lantai(null, null, j);
                if (j == 0) {
                    komplek[i].headLantai = temp;
                    komplek[i].tailLantai = temp;
                } else {
                    komplek[i].tailLantai.next = temp;
                    temp.prev = komplek[i].tailLantai;
                    komplek[i].tailLantai = temp;
                }
            }
        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji

        for (int i = 0; i < komplek.length; i++) {
            if (komplek[i].name.equals(gedungDenji)) {
                for (Lantai lantai = komplek[i].headLantai; lantai != null; lantai = lantai.next) {
                    if (lantai.whichFloor == lantaiDenji - 1) { // Because of whichFloor starts from 0
                        denji = new Karakter(komplek[i], lantai, '+');
                        break;
                    }   
                }
                break;
            }
        }

        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis

        for (int i = 0; i < komplek.length; i++) {
            if (komplek[i].name.equals(gedungIblis)) {
                for (Lantai lantai = komplek[i].headLantai; lantai != null; lantai = lantai.next) {
                    if (lantai.whichFloor == lantaiIblis - 1) { // Because of whichFloor starts from 0
                        iblis = new Karakter(komplek[i], lantai, '-');
                        break;
                    }   
                }
                break;
            }
        }


        int Q = in.nextInt();

        for (int i = 0; i < Q; i++) {

            String command = in.next();

            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
        }

        out.close();
    }

    // TODO: Implemen perintah GERAK
    static void gerak() {

        // Denji Move Set
        if (denji.direction == '+') { // Upstair
            Lantai temp = denji.floorNow.next;
            if (temp == null) { // Highest floor
                int indexBuilding = denji.buildingNow.whichBuilding;

                if (indexBuilding == komplek.length - 1) { // Most right building
                    denji.buildingNow = komplek[0];
                } else {
                    denji.buildingNow = komplek[indexBuilding + 1];
                }

                denji.floorNow = denji.buildingNow.tailLantai;
                denji.direction = '-';

            } else {
                denji.floorNow = temp;
            }

            if (denji.floorNow.equals(iblis.floorNow)) {
                meetUp += 1;
            }

        } else { // Downstair
            Lantai temp = denji.floorNow.prev;
            if (temp == null) { // Lowest floor
                int indexBuilding = denji.buildingNow.whichBuilding;

                if (indexBuilding == komplek.length - 1) { // Most right building
                    denji.buildingNow = komplek[0];
                } else {
                    denji.buildingNow = komplek[indexBuilding + 1];
                }

                denji.floorNow = denji.buildingNow.headLantai;
                denji.direction = '+';

            } else {
                denji.floorNow = temp;

            }

            if (denji.floorNow.equals(iblis.floorNow)) {
                meetUp += 1;
            }
        }

        // Iblis Move Set
        if (iblis.direction == '+') { // Upstair
            Lantai temp = iblis.floorNow.next;
            if (temp == null) { // First move, highest floor
                int indexBuilding = iblis.buildingNow.whichBuilding;

                if (indexBuilding == komplek.length - 1) { // Most right building
                    iblis.buildingNow = komplek[0];
                } else {
                    iblis.buildingNow = komplek[indexBuilding + 1];
                }

                iblis.floorNow = iblis.buildingNow.tailLantai;
                iblis.direction = '-';

            } else {
                iblis.floorNow = temp;

            }

            if (iblis.direction == '+') { // Second move, Upstair
                Lantai tempSecond = iblis.floorNow.next;
                if (tempSecond == null) { // Second move, highest floor
                    int indexBuilding = iblis.buildingNow.whichBuilding;

                    if (indexBuilding == komplek.length - 1) { // Most right building
                        iblis.buildingNow = komplek[0];
                    } else {
                        iblis.buildingNow = komplek[indexBuilding + 1];
                    }

                    iblis.floorNow = iblis.buildingNow.tailLantai;
                    iblis.direction = '-';

                } else {
                    iblis.floorNow = tempSecond;

                }
            } else {
                Lantai tempSecond = iblis.floorNow.prev;
                if (tempSecond == null) { // Second move, lowest floor
                    int indexBuilding = iblis.buildingNow.whichBuilding;

                    if (indexBuilding == komplek.length - 1) { // Most right building
                        iblis.buildingNow = komplek[0];
                    } else {
                        iblis.buildingNow = komplek[indexBuilding + 1];
                    }

                    iblis.floorNow = iblis.buildingNow.headLantai;
                    iblis.direction = '+';

                } else {
                    iblis.floorNow = tempSecond;

                }
            }

            if (denji.floorNow.equals(iblis.floorNow)) {
                meetUp += 1;
            }

        } else { // Downstair
            Lantai temp = iblis.floorNow.prev;
            if (temp == null) { // First move, lowest floor
                int indexBuilding = iblis.buildingNow.whichBuilding;

                if (indexBuilding == komplek.length - 1) { // Most right building
                    iblis.buildingNow = komplek[0];
                } else {
                    iblis.buildingNow = komplek[indexBuilding + 1];
                }

                iblis.floorNow = iblis.buildingNow.headLantai;
                iblis.direction = '+';

            } else {
                iblis.floorNow = temp;

            }

            if (iblis.direction == '+') { // Second move, Upstair
                Lantai tempSecond = iblis.floorNow.next;
                if (tempSecond == null) { // Second move, highest floor
                    int indexBuilding = iblis.buildingNow.whichBuilding;

                    if (indexBuilding == komplek.length - 1) { // Most right building
                        iblis.buildingNow = komplek[0];
                    } else {
                        iblis.buildingNow = komplek[indexBuilding + 1];
                    }

                    iblis.floorNow = iblis.buildingNow.tailLantai;
                    iblis.direction = '-';

                } else {
                    iblis.floorNow = tempSecond;

                }
            } else {
                Lantai tempSecond = iblis.floorNow.prev;
                if (tempSecond == null) { // Second move, lowest floor
                    int indexBuilding = iblis.buildingNow.whichBuilding;

                    if (indexBuilding == komplek.length - 1) { // Most right building
                        iblis.buildingNow = komplek[0];
                    } else {
                        iblis.buildingNow = komplek[indexBuilding + 1];
                    }

                    iblis.floorNow = iblis.buildingNow.headLantai;
                    iblis.direction = '+';

                } else {
                    iblis.floorNow = tempSecond;

                }
            }

            if (denji.floorNow.equals(iblis.floorNow)) {
                meetUp += 1;
            }
        }

        // whichFloor starts from 0 & removeCounter indicates it's edit
        out.println(denji.buildingNow.name + " " + (denji.floorNow.whichFloor + 1 - denji.buildingNow.removeCounter) 
                    + " " + iblis.buildingNow.name + " " + (iblis.floorNow.whichFloor + 1 - iblis.buildingNow.removeCounter)
                    + " " + meetUp);
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {
        Lantai temp = denji.floorNow.prev;

        // whichFloor starts from 0 & removeCounter indicates it's edit
        if (temp == null || temp.equals(iblis.floorNow)) {
            out.println(denji.buildingNow.name + " " + -1);
        } else {
            if (denji.floorNow.whichFloor > iblis.floorNow.whichFloor && denji.buildingNow.equals(iblis.buildingNow)) {
                // Both of them are on the same building, but denji is above iblis
                iblis.floorNow = iblis.floorNow.next;
            }

            out.println(denji.buildingNow.name + " " + (denji.floorNow.whichFloor - denji.buildingNow.removeCounter));
            
            // Delete the head & add removeCounter for next print purposes
            denji.buildingNow.headLantai.next.prev = null;
            denji.buildingNow.headLantai = denji.buildingNow.headLantai.next;

            denji.buildingNow.removeCounter += 1;
        }
    }

    // TODO: Implemen perintah TAMBAH
    static void tambah() {        

        // Inserting new floor on the back
        Lantai temp = new Lantai(null, null, iblis.buildingNow.tailLantai.whichFloor + 1);
        iblis.buildingNow.tailLantai.next = temp;
        temp.prev = iblis.buildingNow.tailLantai;
        iblis.buildingNow.tailLantai = temp;

        // whichFloor starts from 0 & removeCounter indicates it's edit
        out.println(iblis.buildingNow.name + " " + (iblis.floorNow.whichFloor + 1 - iblis.buildingNow.removeCounter));

        // Move denji and/or iblis one step upstair
        if (denji.floorNow.whichFloor < iblis.floorNow.whichFloor && denji.buildingNow.equals(iblis.buildingNow)) {
            // Both of them are on the same building, but denji is below iblis
            iblis.floorNow = iblis.floorNow.next;
        } else if (denji.floorNow.equals(iblis.floorNow) && denji.buildingNow.equals(iblis.buildingNow)) {
            // Both of them are on the same building & floor
            denji.floorNow = denji.floorNow.next;
            iblis.floorNow = iblis.floorNow.next;
        } else if (denji.floorNow.whichFloor > iblis.floorNow.whichFloor && denji.buildingNow.equals(iblis.buildingNow)) {
            // Both of them are on the same building, but denji is above iblis
            denji.floorNow = denji.floorNow.next;
            iblis.floorNow = iblis.floorNow.next;
        } else { // Both of them are on the different building
            iblis.floorNow = iblis.floorNow.next;
        }
    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {
        int indexBuilding = denji.buildingNow.whichBuilding;

        if (indexBuilding == komplek.length - 1) { // Most right building
            denji.buildingNow = komplek[0];
        } else {
            denji.buildingNow = komplek[indexBuilding + 1];
        }

        if (denji.direction == '+') { // Initially upstair
            denji.floorNow = denji.buildingNow.headLantai;
        } else {
            denji.floorNow = denji.buildingNow.tailLantai;
        }

        if (denji.floorNow.equals(iblis.floorNow)) {
            meetUp += 1;
        }

        // whichFloor starts from 0 & removeCounter indicates it's edit
        out.println(denji.buildingNow.name + " " + (denji.floorNow.whichFloor + 1 - denji.buildingNow.removeCounter));
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}