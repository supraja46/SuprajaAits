import java.util.*;
class MenuItem {
    int id;
    String name;
    double price;
    int stock;

    MenuItem(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

enum OrderStatus {
    RECEIVED, PREPARING, READY, SERVED;
}

class Order {
    int tableNumber;
    List<MenuItem> items = new ArrayList<>();
    double total = 0.0;
    OrderStatus status = OrderStatus.RECEIVED;
    String feedback = "";
    boolean isPaid = false; // NEW FLAG

    void addItem(MenuItem item) {
        items.add(item);
        total += item.price;
        item.stock--;
    }

    void printBill() {
        System.out.println("Order for Table: " + tableNumber);
        for (MenuItem item : items) {
            System.out.println("- " + item.name + ": $" + item.price);
        }
        System.out.println("Total Bill: $" + total);
    }
}

public class RestaurantManagementSystem {
    static Scanner sc = new Scanner(System.in);
    static List<MenuItem> menu = new ArrayList<>();
    static List<Integer> reservedTables = new ArrayList<>();
    static List<Order> orderHistory = new ArrayList<>();
    static Queue<Order> orderQueue = new LinkedList<>();
    static LinkedList<String> staffList = new LinkedList<>();
    static HashSet<String> staffSet = new HashSet<>();
    static HashMap<String, String> staffRoles = new HashMap<>();
    static List<String> feedbackList = new ArrayList<>();
    static List<Double> expenses = new ArrayList<>();
    static HashMap<String, String> customerAccounts = new HashMap<>();
    static int tableCapacity = 10;
    static int itemIdCounter = 1;

    public static void main(String[] args) {
        initializeMenu();
        initializeStaff();

        boolean runApp = true;
        while (runApp) {
            System.out.println("\n--- Welcome to Digital Restaurant System ---");
            System.out.println("1. Register as Customer");
            System.out.println("2. Login and View Menu");
            System.out.println("3. Admin Access");
            System.out.println("4. Exit");
            System.out.print("Select Option: ");
            int choice = getValidatedIntInput();

            switch (choice) {
                case 1 -> registerCustomer();
                case 2 -> {
                    if (customerLogin()) {
                        customerMenu();
                    }
                }
                case 3 -> securityAccessPanel();
                case 4 -> runApp = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void customerMenu() {
        boolean customerActive = true;
        Order currentOrder = null;

        while (customerActive) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Reserve Table");
            System.out.println("4. Provide Feedback");
            System.out.println("5. Pay Bill");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = getValidatedIntInput();

            switch (choice) {
                case 1 -> displayMenu();
                case 2 -> {
                    currentOrder = placeOrder();
                }
                case 3 -> reserveTable();
                case 4 -> {
                    sc.nextLine();
                    System.out.print("Enter feedback: ");
                    String fb = sc.nextLine();
                    feedbackList.add(fb);
                    System.out.println("Thank you for your feedback!");
                }
                case 5 -> payBill(currentOrder);
                case 6 -> {
                    if (currentOrder != null && !currentOrder.isPaid) {
                        System.out.println("You must pay your bill before exiting.");
                    } else {
                        customerActive = false;
                        System.out.println("Logged out successfully.");
                    }
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void payBill(Order order) {
        if (order != null && !order.isPaid) {
            order.printBill();
            System.out.print("Pay the bill amount of $" + order.total + " (Enter 'yes' to pay): ");
            String input = sc.next();
            if (input.equalsIgnoreCase("yes")) {
                order.isPaid = true;
                System.out.println("Payment successful. Thank you!");
            } else {
                System.out.println("Payment failed. You cannot exit without paying.");
            }
        } else {
            System.out.println("No pending bill to pay.");
        }
    }

    static void registerCustomer() {
        System.out.print("Create Username: ");
        String username = sc.next();
        if (customerAccounts.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }
        System.out.print("Create Password: ");
        String password = sc.next();4
        customerAccounts.put(username, password);
        System.out.println("Registration successful!");
    }

    static boolean customerLogin() {
        System.out.print("Enter Username: ");
        String username = sc.next();
        System.out.print("Enter Password: ");
        String password = sc.next();

        if (customerAccounts.containsKey(username) && customerAccounts.get(username).equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid credentials.");
            return false;
        }
    }

    static void initializeMenu() {
        menu.add(new MenuItem(itemIdCounter++, "Idli", 2, 10));
        menu.add(new MenuItem(itemIdCounter++, "Dosa", 1, 30));
        menu.add(new MenuItem(itemIdCounter++, "Chicken Biryani", 8, 3));
        menu.add(new MenuItem(itemIdCounter++, "Veg Soup", 5, 10));
        menu.add(new MenuItem(itemIdCounter++, "Kheema Curry", 6, 13));
        menu.add(new MenuItem(itemIdCounter++, "Mutton Biryani", 10, 3));
        menu.add(new MenuItem(itemIdCounter++, "Veg Fried Rice", 4, 3));
        menu.add(new MenuItem(itemIdCounter++, "Indian Thali", 6, 5));
        menu.add(new MenuItem(itemIdCounter++, "Coke", 1, 30));
        menu.add(new MenuItem(itemIdCounter++, "Blue Curacao", 2, 3));
        menu.add(new MenuItem(itemIdCounter++, "Ice Cream", 2, 20));
    }

    static void initializeStaff() {
        addStaff("Chef Sana", "Chef");
        addStaff("Waiter Nandu6", "Waiter");
        addStaff("Manager Suppi", "Manager");
    }

    static void displayMenu() {
        System.out.println("\n--- Menu ---");
        for (MenuItem item : menu) {
            System.out.print(item.id + ". " + item.name + " - $" + item.price + " (Stock: " + item.stock + ")");
            if (item.stock < 3) {
                System.out.print(" - [LOW STOCK ALERT]");
            }
            System.out.println();
        }
    }

    static Order placeOrder() {
        displayMenu();
        Order order = new Order();
        System.out.print("Enter Table Number (1-10): ");
        order.tableNumber = getValidatedIntInput();
        if (order.tableNumber < 1 || order.tableNumber > tableCapacity) {
            System.out.println("Invalid table number.");
            return null;
        }

        boolean ordering = true;
        while (ordering) {
            System.out.print("Enter item number to add (0 to finish): ");
            int itemId = getValidatedIntInput();
            if (itemId == 0) {
                ordering = false;
            } else {
                Optional<MenuItem> itemOpt = menu.stream().filter(i -> i.id == itemId).findFirst();
                if (itemOpt.isPresent()) {
                    MenuItem item = itemOpt.get();
                    if (item.stock > 0) {
                        order.addItem(item);
                        System.out.println(item.name + " added.");
                    } else {
                        System.out.println("Sorry, " + item.name + " is out of stock.");
                    }
                } else {
                    System.out.println("Item not found.");
                }
            }
        }

        orderQueue.add(order);
        orderHistory.add(order);
        System.out.println("Order received. It is now in the queue.");
        order.printBill();
        return order;
    }

    static void reserveTable() {
        System.out.print("Enter table number to reserve (1-10): ");
        int tableNo = getValidatedIntInput();
        if (tableNo < 1 || tableNo > tableCapacity || reservedTables.contains(tableNo)) {
            System.out.println("Table reservation failed.");
        } else {
            reservedTables.add(tableNo);
            System.out.println("Table " + tableNo + " reserved successfully.");
        }
    }

    static void securityAccessPanel() {
        System.out.print("Enter Admin Access Code: ");
        String password = sc.next();
        if (!password.equals("logic404")) {
            System.out.println("Access Denied!");
            return;
        }

        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println("\n--- Admin Access Panel ---");
            System.out.println("1. View Orders");
            System.out.println("2. View Reserved Tables");
            System.out.println("3. View Total Sales");
            System.out.println("4. View Feedback");
            System.out.println("5. Update Order Status");
            System.out.println("6. Add/Remove Menu Items");
            System.out.println("7. Inventory & Expense Report");
            System.out.println("8. View/Add/Remove Staff");
            System.out.println("9. Exit Panel");
            System.out.print("Choose an option: ");
            int choice = getValidatedIntInput();

            switch (choice) {
                case 1 -> orderHistory.forEach(Order::printBill);
                case 2 -> System.out.println("Reserved Tables: " + reservedTables);
                case 3 -> {
                    double totalSales = orderHistory.stream().mapToDouble(o -> o.total).sum();
                    System.out.println("Total Sales: $" + totalSales);
                }
                case 4 -> feedbackList.forEach(f -> System.out.println("- " + f));
                case 5 -> updateOrderStatus();
                case 6 -> menuManagement();
                case 7 -> {
                    System.out.println("Expenses: $" + expenses.stream().mapToDouble(Double::doubleValue).sum());
                    displayMenu();
                }
                case 8 -> staffManagement();
                case 9 -> adminRunning = false;
                default -> System.out.println("Invalid Option.");
            }
        }
    }

    static void updateOrderStatus() {
        for (int i = 0; i < orderHistory.size(); i++) {
            System.out.println(i + 1 + ". Table " + orderHistory.get(i).tableNumber + " - Current Status: " + orderHistory.get(i).status);
        }
        System.out.print("Select order to update: ");
        int idx = getValidatedIntInput() - 1;
        if (idx >= 0 && idx < orderHistory.size()) {
            System.out.println("Choose new status: 1. PREPARING 2. READY 3. SERVED");
            int s = getValidatedIntInput();
            switch (s) {
                case 1 -> orderHistory.get(idx).status = OrderStatus.PREPARING;
                case 2 -> orderHistory.get(idx).status = OrderStatus.READY;
                case 3 -> orderHistory.get(idx).status = OrderStatus.SERVED;
                default -> System.out.println("Invalid status.");
            }
        }
    }

    static void menuManagement() {
        System.out.println("1. Add Item 2. Remove Item");
        int ch = getValidatedIntInput();
        sc.nextLine();
        if (ch == 1) {
            System.out.print("Enter item name: ");
            String name = sc.nextLine();
            System.out.print("Enter price: ");
            double price = getValidatedDoubleInput();
            System.out.print("Enter stock: ");
            int stock = getValidatedIntInput();
            menu.add(new MenuItem(itemIdCounter++, name, price, stock));
            System.out.println("Item added.");
        } else if (ch == 2) {
            System.out.print("Enter item id to remove: ");
            int id = getValidatedIntInput();
            menu.removeIf(i -> i.id == id);
            System.out.println("Item removed.");
        }
    }

    static void staffManagement() {
        System.out.println("1. View Staff 2. Add Staff 3. Remove Staff");
        int ch = getValidatedIntInput();
        sc.nextLine();
        if (ch == 1) {
            for (String staff : staffList) {
                System.out.println("- " + staff + " (" + staffRoles.get(staff) + ")");
            }
        } else if (ch == 2) {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter role: ");
            String role = sc.nextLine();
            addStaff(name, role);
        } else if (ch == 3) {
            System.out.print("Enter name to remove: ");
            String name = sc.nextLine();
            staffList.remove(name);
            staffSet.remove(name);
            staffRoles.remove(name);
            System.out.println("Staff removed.");
        }
    }

    static void addStaff(String name, String role) {
        if (!staffSet.contains(name)) {
            staffList.add(name);
            staffSet.add(name);
            staffRoles.put(name, role);
            System.out.println(name + " added as " + role);
        } else {
            System.out.println(name + " already exists.");
        }
    }

    static int getValidatedIntInput() {
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Enter a number:");
            sc.next();
        }
        return sc.nextInt();
    }

    static double getValidatedDoubleInput() {
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Enter a number:");
            sc.next();
        }
        return sc.nextDouble();
    }
}
