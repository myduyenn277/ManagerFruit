package Lap7.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import Lap7.common.Validation;
import Lap7.model.Fruit;
import Lap7.view.Menu;

public class Program  extends Menu<String>  {
    
 static String[] opsList = {"Create Product", "View Order", "Shopping","Exit"};       
    List<Fruit> fruit = new ArrayList<>();
    Hashtable<String, ArrayList<Fruit>> orders = new Hashtable<>();
    Validation validation = new Validation();
        static Scanner sc = new Scanner(System.in);
    
        public Program() {
            super("Program Fruit Menu", opsList);
            fruit = new ArrayList<>();
        }
    
        @Override
        public void execute(int n) {
            switch (n) {
                case 1: {
                   createProduct();
                    break;
                }
                case 2: {
                    viewOrder();
                   break;
                }
                case 3:{
                 shopping();
                break;
                }
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Please, enter again!");
            }
        }


    public Fruit getFruitById(String id) {
        for (Fruit fruit : fruit) {
            if (id.equals(fruit.getFruitId())) {
                return fruit;
            }
        }
        return null;
    }

    public void createProduct() {
        while (true) {
            String fruitId = validation.inputString("Enter id:", ".+");

            if (getFruitById(fruitId) != null) {
                System.out.println("ID is existed");
                continue;
            }
            String fruitName = validation.inputString("Enter name:", "[A-Za-z\\s]+");
            double price = validation.inputDouble("Enter price:", 1, Double.MAX_VALUE);
            int quantity = validation.inputInt("Enter quantity:", 1, Integer.MAX_VALUE);
            String origin = validation.inputString("Enter origin:", "[A-Za-z\\s]+");
            fruit.add(new Fruit(fruitId, fruitName, price, quantity, origin));
            if (!validation.checkInputYN("Do you want to continue(Y/N)?")) {
                break;
            }
        }
    }

    public int displayListFruit() {
        int countItem = 0;
        if (fruit.isEmpty()) {
            return -1;
        }
        for (Fruit fruit : fruit) {
            if (fruit.getQuantity() != 0) {
                countItem++;
                if (countItem == 1) {
                    System.out.printf("%-10s%-20s%-20s%-15s\n", "Item", "Fruit name", "Origin", "Price");
                }
                System.out.printf("%-10d%-20s%-20s%-15.0f$\n", countItem,
                        fruit.getFruitName(), fruit.getOrigin(),
                        fruit.getPrice());
            }
        }
        if (countItem == 0) {
            return -1;
        }
        int item = validation.inputInt("Enter item:", 1, countItem);
        return item;

    }

    public Fruit getFruit(int item) {
        int count = 0;
        for (Fruit fruit : fruit) {
            if (fruit.getQuantity() != 0) {
                count++;
            }
            if (item == count) {
                return fruit;
            }
        }
        return null;
    }

    public Fruit checkFruitInOrder(ArrayList<Fruit> listOrder, String id) {
        for (Fruit fruit : listOrder) {
            if (fruit.getFruitId().equalsIgnoreCase(id)) {
                return fruit;
            }
        }
        return null;
    }
    public void displayListOrder(ArrayList<Fruit> listOrder) {
        double total = 0;

        System.out.printf("%15s%15s%15s%15s\n", "Product", "Quantity", "Price", "Amount");
        for (Fruit fruit : listOrder) {
            System.out.printf("%15s%15d%15.0f$%15.0f$\n", fruit.getFruitName(),
                    fruit.getQuantity(), fruit.getPrice(),
                    fruit.getPrice() * fruit.getQuantity());
            total += fruit.getPrice() * fruit.getQuantity();
        }
        System.out.println("Total: " + total);
    }

    public void viewOrder() {
        if (orders.isEmpty()) {
            System.out.println("No orders");
            return;
        }
        for (String name : orders.keySet()) {
            System.out.println("Customer: " + name.split("#")[0]);
            ArrayList<Fruit> listOrder = orders.get(name);
            displayListOrder(listOrder);
        }

    }

     public void shopping() {
        ArrayList<Fruit> listOrder = new ArrayList<>();
        while (true) {
            int item = displayListFruit();
            if (item == -1) {
                System.out.println("Out of stock.");
                return;
            }
            Fruit fruit = getFruit(item);
            System.out.println("You selected:" + fruit.getFruitName());
            int quantity = validation.inputInt("Enter quantity:", 0, fruit.getQuantity());
            fruit.setQuantity(fruit.getQuantity() - quantity);
            Fruit fruitInOrder = checkFruitInOrder(listOrder, fruit.getFruitId());
            if (fruitInOrder != null) {
                fruitInOrder.setQuantity(fruitInOrder.getQuantity() + quantity);
            } else {
                if (quantity != 0) {
                    listOrder.add(new Fruit(fruit.getFruitId(), fruit.getFruitName(), fruit.getPrice(), quantity, fruit.getOrigin()));
                }

            }
            if (!validation.checkInputYN("Do you want to continue(Y/N)?")) {
                break;
            }
        }
        if (listOrder.isEmpty()) {
            System.out.println("No orders");
        } else {
            displayListOrder(listOrder);
            String name = setName();
            orders.put(name, listOrder);
        }

    }
     public String setName() {
        String name = validation.inputString("Enter name:", "[A-Za-z\\s]+");
        int count = 0;
        for (String name_key : orders.keySet()) {
            String real_name = name_key.split("#")[0];
            if (name.equals(real_name)) {
                count++;
            }
        }
        return name + "#" + count;
    }
}
