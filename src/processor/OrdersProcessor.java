package processor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Map;
import java.util.Scanner;

public class OrdersProcessor {

	public static void main(String[] args) {
	
			Scanner sc = new Scanner(System.in);
			 
			System.out.println("Enter item's data file name: ");
			String fileName = sc.next();
			
			TreeMap<String, Double> itemCosts = new TreeMap<String, Double>();
			Scanner scanner;
			try {
				//Reading the item data file and inserting values into itemCosts
				scanner = new Scanner(new File(fileName));
				while(scanner.hasNextLine()) {
					String temp = scanner.next();
					Double prices = scanner.nextDouble();
					itemCosts.put(temp, prices);
				}
				scanner.close();
			} catch (FileNotFoundException e) { e.printStackTrace(); }
			
			//Determine if multithreading should be used based on the users input
			System.out.println("Enter 'y' for multiple threads, any other character otherwise: ");
			String s = sc.next();
			boolean multithreaded = (s.equalsIgnoreCase("y")) ? true : false;
		
			System.out.println("Enter number of orders to process: ");
			int ordereNum = sc.nextInt();
			
			System.out.println("Enter order's base filename: ");
			String data = sc.next();
			
			System.out.println("Enter result's filename: ");
			String resultName = sc.next();
			
			long startTime = System.currentTimeMillis();
			
			sc.close();
			//Storing the customer data
			TreeMap<Integer, Order> customers = new TreeMap<Integer, Order>();
			
			if(multithreaded) {
				//Processing the orders using multithreading
				multithreaded(customers, data, ordereNum, itemCosts);
			} else {
				//Processing the orders sequentially 
				notMultithreaded(customers, data, ordereNum, itemCosts);
			}
			writeToFile(customers, itemCosts, resultName);
			
			long endTime = System.currentTimeMillis();
			System.out.println("Processing time (msec): " + (endTime - startTime));
			
			System.out.println("Results can be found in the file: " + resultName);
			
	}
	
	private static void multithreaded(TreeMap<Integer, Order> customers, String file, int id, TreeMap<String, Double> prices) {
		Thread[] threads = new Thread[id];
		
		//Synchronize acccess to customers map to prevent modifications
		synchronized(customers) {
			for(int i = 1; i <= id; i++) {
				try {
					Scanner sc = new Scanner(new File(file + Integer.toString(i) + ".txt"));
					sc.next();
					
					//Creating the Order object for each other
					Order order = new Order(file + Integer.toString(i) + ".txt", prices);
					
					//Starting a new thread for each other
					Thread t = new Thread(order);
					threads[i-1] = t;
					
					t.start();
					
					//Adding the other to the customers map
					customers.put(sc.nextInt(), order);
					sc.close();
				} catch (FileNotFoundException e) { e.printStackTrace(); }
			}			
		}
		//Wait for all of the threads to finish executing
		for(Thread thread: threads) {
			try { thread.join(); } 
			catch (InterruptedException e) { e.printStackTrace(); } 
		}
	}

	private static void notMultithreaded(TreeMap<Integer, Order> customers, String file, int id, TreeMap<String, Double> prices) {
		//Using a look for single threading
   		for(int i = 1; i <= id; i++) {
			try {
				
				Scanner sc = new Scanner(new File(file + Integer.toString(i) + ".txt"));
				sc.next();
				
				//Creating a new Order for each order
				Order order = new Order(file + Integer.toString(i) + ".txt", prices);
				order.run();
				
				//Adding the others to the map
				customers.put(sc.nextInt(), order);
			} catch (FileNotFoundException e) { e.printStackTrace(); }
		}
	}
	
	private static void writeToFile(TreeMap<Integer, Order> customers, TreeMap<String, Double> itemCosts, String targetFile) {
	    StringBuilder sb = new StringBuilder();
	    FileWriter fileWriter;
	    	
	        try {
	        	fileWriter = new FileWriter(targetFile);
	            for (Map.Entry<Integer, Order> entry : customers.entrySet()) {
	            	
	            	//Printing the customer id
	                sb.append("----- Order details for client with Id: ").append(entry.getKey()).append(" -----\n");
	                sb.append(entry.getValue().toString());
	            }
	            sb.append("***** Summary of all orders *****\n");

	            //Adding the products to a treeMap to order them alphabetically
	            double total = 0;
	            TreeMap<String, Integer> orderedProducts = new TreeMap<>();
	            for (String product : itemCosts.keySet()) {
	                int count = 0;
	                for (Integer key : customers.keySet()) {
	                    count += customers.get(key).get(product);
	                }
	                orderedProducts.put(product, count);
	            }

	            for (Map.Entry<String, Integer> entry : orderedProducts.entrySet()) {
	                String product = entry.getKey();
	                int productCount = entry.getValue();
	                if (productCount > 0) {
	                    sb.append("Summary - Item's name: ").append(product).append(", Cost per item: $");
	                    sb.append(String.format("%.2f", itemCosts.get(product))).append(", Number sold: ");
	                    sb.append(productCount).append(", Item's Total: $");
	                    sb.append(String.format("%,.2f", productCount * itemCosts.get(product))).append("\n");
	                }
	                total += productCount * itemCosts.get(product);
	            }

	            sb.append("Summary Grand Total: $").append(String.format("%,.2f", total)).append("\n");

	            fileWriter.write(sb.toString());
	            fileWriter.close();
	            
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}