import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.StringTokenizer;

public class TP1 {
    private static InputReader in;
    private static PrintWriter out;

    private static Menu[] menuSpecification;
    private static HashMap<Integer, Customer> totalCustomerSpecification;
    private static Chef[] chefSpecification; // Better use priorityqueue (!)

    private static int menuAmount;

    private static long[] memoPriceNone;
    private static long[] memoPriceA;
    private static long[] memoPriceG;
    private static long[] memoPriceS;
    private static long[] memoPriceAG;
    private static long[] memoPriceAS;
    private static long[] memoPriceGS;
    private static long[] memoPriceAGS;

    private static int airfoodPromoPrice;
    private static int groundfoodPromoPrice;
    private static int seafoodPromoPrice;

    static Chef findChef(int idChef) {
        for (Chef chef : chefSpecification) {
            if (chef.id == idChef) {
                return chef;
            }
        }
        return null;
    }

    // Reference : Muhammad Ruzain
    static long operationD(int start, int end, String whichPackage) {

        long totalPriceNoPackage = menuSpecification[start].price;
        long totalPricePackage;
        
        // There might be a chance that every menu's available was 100.000
        long minimumPricePackage = 100000 * menuAmount; // Worst case scenario

        // Base case : Final element
        if (start == end) {
            return menuSpecification[end].price;
        }

        // Base case : Memo
        if (memoPriceNone[start] != 0 && whichPackage.equals("   ")){
            return memoPriceNone[start];
        }
        if (memoPriceA[start] != 0 && whichPackage.equals("A  ")){
            return memoPriceA[start];
        }
        if (memoPriceG[start] != 0 && whichPackage.equals(" G ")){
            return memoPriceG[start];
        }
        if (memoPriceS[start] != 0 && whichPackage.equals("  S")){
            return memoPriceS[start];
        }
        if (memoPriceAG[start] != 0 && whichPackage.equals("AG ")){
            return memoPriceAG[start];
        }
        if (memoPriceAS[start] != 0 && whichPackage.equals("A S")){
            return memoPriceAS[start];
        }
        if (memoPriceGS[start] != 0 && whichPackage.equals(" GS")){
            return memoPriceGS[start];
        }
        if (memoPriceAGS[start] != 0 && whichPackage.equals("AGS")){
            return memoPriceAGS[start];
        }

        for (int i = start + 1; i <= end; i++) {

            // What if the current menu is not included as package?
            long totalPricePackageNew = totalPriceNoPackage + operationD(i, end, whichPackage);
            
            char packageCreation = menuSpecification[i].name;

            // When meets the "last" same menu, it counted as a package
            if (packageCreation == menuSpecification[start].name && whichPackage.indexOf(String.valueOf(packageCreation)) >= 0) {
                int pricePackage;
                if (packageCreation == 'A') {
                    pricePackage = airfoodPromoPrice;
                } else if (packageCreation == 'G') {
                    pricePackage = groundfoodPromoPrice;
                } else {
                    pricePackage = seafoodPromoPrice;
                }

                // There might be a chance that we can make more package
                long next = 0;

                if (i != end) {
                    // Clear that certain package created
                    next = operationD(i + 1, end, whichPackage.replace(packageCreation, ' '));
                }

                totalPricePackage = (i - start + 1) * (long) pricePackage + next;

                // Get the most minimum price for this certain package
                totalPricePackageNew = Math.min(totalPricePackage, totalPricePackageNew);
            }

            // Every iteration, total price without package also increasing
            totalPriceNoPackage += menuSpecification[i].price;

            // Get the most minimum for this certain iteration
            minimumPricePackage = Math.min(minimumPricePackage, totalPricePackageNew);
        }

        // Get the most minimum available for everything
        long summary = Math.min(totalPriceNoPackage, minimumPricePackage);

        // Saving memo
        if (whichPackage.equals("   ")) {
            memoPriceNone[start] = summary;
        } 
        if (whichPackage.equals("A  ")) {
            memoPriceA[start] = summary;
        } 
        if (whichPackage.equals(" G ")) {
            memoPriceG[start] = summary;
        } 
        if (whichPackage.equals("  S")) {
            memoPriceS[start] = summary;
        }
        if (whichPackage.equals("AG ")) {
            memoPriceAG[start] = summary;
        } 
         if (whichPackage.equals("A S")) {
            memoPriceAS[start] = summary;
        } 
        if (whichPackage.equals(" GS")) {
            memoPriceGS[start] = summary;
        }
        if (whichPackage.equals("   ")) {
            memoPriceAGS[start] = summary;
        }

        return summary;
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Starts here.
        menuAmount = in.nextInt();

        menuSpecification = new Menu[menuAmount + 1];

        for (int i = 0; i < menuAmount; i++) {
            int menuPrice = in.nextInt();
            char menuName = in.next().charAt(0);
            menuSpecification[i + 1] = new Menu(menuName, menuPrice);
        }

        int chefAmount = in.nextInt();

        chefSpecification = new Chef[chefAmount];

        for (int i = 0; i < chefAmount; i++) {
            chefSpecification[i] = new Chef(i + 1, in.next().charAt(0));
        }

        int totalCustomerAmount = in.nextInt();

        totalCustomerSpecification = new HashMap<Integer, Customer>(totalCustomerAmount);

        int roomAvailable = in.nextInt();

        int operationDays = in.nextInt();

        // PER DAY
        for (int i = 0; i < operationDays; i++) {
            
            int customerToday = in.nextInt();

            Deque<Order> orderSpecification = new ArrayDeque<Order>(); // Better use Queue (!)

            int[] totalStatusLine = new int[customerToday];

            // Output customer's condition 0, 1, 2, 3. 0 = +, 1 = qualified, 2 = hunger room, 3 = blacklist
            // Categorize customer's condition
            int roomUsedCounter = 0;

            for (int j = 0; j < customerToday; j++) {

                int tempStatusLine = 0;

                int tempCustomerId = in.nextInt();
                char tempCustomerStatus = in.next().charAt(0);
                int tempCustomerMoney = in.nextInt();

                if (j >= 1) {
                    tempStatusLine = totalStatusLine[j - 1];
                } 
                
                // Advance scanning
                if (tempCustomerStatus == '?') {
                    int range = in.nextInt();
                    int statusRangeScore;

                    if (j == range) {
                        statusRangeScore = totalStatusLine[j - 1];
                    } else {
                        statusRangeScore = totalStatusLine[j - 1] - totalStatusLine[j - 1 - range];     
                    }
                    
                    if (statusRangeScore <= 0) {
                        tempCustomerStatus = '-';
                    } else {
                        tempCustomerStatus = '+';
                    }
                }

                if (tempCustomerStatus == '+') {
                    totalStatusLine[j] = tempStatusLine + 1;
                } else if (tempCustomerStatus == '-') {
                    totalStatusLine[j] = tempStatusLine - 1;
                }
                
                // Checking if it's a new customer or not. Null = New
                Customer tempCustomer = totalCustomerSpecification.get(tempCustomerId);

                // New Customer
                if (tempCustomer == null) {
                    totalCustomerSpecification.put(tempCustomerId, new Customer(tempCustomerId, tempCustomerStatus, tempCustomerMoney));
                    
                } else { // Returner
                    tempCustomer.money = tempCustomerMoney;
                    tempCustomer.status = tempCustomerStatus;

                    if (tempCustomer.isBlacklist == true) {
                        out.print("3 ");
                        continue; // Agar tidak membaca kode di bawahnya
                    } 
                }

                if (tempCustomerStatus == '+') {
                    out.print("0 ");
                } else if (roomUsedCounter >= roomAvailable) {
                    out.print("2 ");
                } else {
                    out.print("1 ");
                    roomUsedCounter += 1;
                }
            }
            
            out.print("\n");

            int operationToday = in.nextInt();
            
            for (int j = 0; j < operationToday; j++) {
                String operationNow = in.next();

                if (operationNow.equals("P")) {
                    int idExecuted = in.nextInt();
                    int foodIndex = in.nextInt();

                    Customer tempCustomer = totalCustomerSpecification.get(idExecuted);
                
                    // Basically findChef by speciality
                    for (Chef chef : chefSpecification) {
                        if (menuSpecification[foodIndex].name == chef.speciality) {
                            orderSpecification.add(new Order(idExecuted, chef.id, menuSpecification[foodIndex]));

                            // Every order, customer's money is decreased automatically
                            tempCustomer.minusMoney(menuSpecification[foodIndex].price); 
                            
                            out.println(chef.id);
                            break;
                        }
                    }

                } else if (operationNow.equals("L")) {
                    Order tempOrder = orderSpecification.pollFirst();
                    Chef tempChef = findChef(tempOrder.idChef);
                    Customer tempCustomer = totalCustomerSpecification.get(tempOrder.idCustomer);

                    tempChef.addServe();

                    Arrays.sort(chefSpecification);

                    out.println(tempCustomer.id);

                } else if (operationNow.equals("B")) {
                    int idExecuted = in.nextInt();

                    Customer tempCustomer = totalCustomerSpecification.get(idExecuted);

                    if (tempCustomer.money < 0) {
                        tempCustomer.isBlacklist = true;
                        out.println(0); 
                    } else {
                        out.println(1);
                    }

                } else if (operationNow.equals("C")) {
                    int lowestAmount = in.nextInt();

                    for (int k = 0; k < lowestAmount; k++) {
                        out.print(chefSpecification[k].id + " ");
                    }

                    out.print("\n");

                } else if (operationNow.equals("D")) {
                    airfoodPromoPrice = in.nextInt();
                    groundfoodPromoPrice = in.nextInt();
                    seafoodPromoPrice = in.nextInt();

                    // Reset memo
                    memoPriceNone = new long[menuAmount + 1];
                    memoPriceA = new long[menuAmount + 1];
                    memoPriceG = new long[menuAmount + 1];
                    memoPriceS = new long[menuAmount + 1];
                    memoPriceAG = new long[menuAmount + 1];
                    memoPriceAS = new long[menuAmount + 1];
                    memoPriceGS = new long[menuAmount + 1];
                    memoPriceAGS = new long[menuAmount + 1];

                    out.println(operationD(1, menuAmount, "AGS"));

                }
            }
        }

        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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

    static class Menu {
        public char name;
        public int price;

        // Constructor
        public Menu(char name, int price) {
            this.name = name;
            this.price = price;
        }
    }

    static class Chef implements Comparable<Chef> {
        public int id;
        public char speciality;
        public int serve;

        // Constructor
        public Chef(int id, char speciality) {
            this.id = id;
            this.speciality = speciality;
            this.serve = 0;
        }

        // Sort by serve, then 
        public int compareTo(Chef other) {
            if (this.serve != other.serve) { // Main sort : Serve point
                return this.serve - other.serve;
            } else if (this.speciality != other.speciality) {
                return Character.valueOf(other.speciality).compareTo(Character.valueOf(this.speciality));
            } else { // Alternate sort : id
                return this.id - other.id;
            }
        }

        public void addServe() {
            this.serve += 1;
        }
    }

    static class Customer {
        public int id;
        public char status;
        public int money;
        public boolean isBlacklist;

        public Customer(int id, char status, int money) {
            this.id = id;
            this.status = status;
            this.money = money;
            this.isBlacklist = false;
        }

        public void minusMoney(int minusBy) {
            this.money -= minusBy;
        }
    }

    static class Order {
        public int idCustomer;
        public int idChef;
        public Menu food;
        public boolean paid;

        public Order(int idCustomer, int idChef, Menu food) {
            this.idCustomer = idCustomer;
            this.idChef = idChef;
            this.food = food;
            this.paid = false;
        }
    }
}