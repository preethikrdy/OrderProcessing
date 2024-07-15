package processor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Order implements Runnable{
	private TreeMap<String, Integer> customerOrders;
	private File orderFile;
	private TreeMap<String, Double> prices;
	
	public Order(String fileName, TreeMap<String, Double> prices) {
		this.orderFile = new File(fileName);
		this.prices = prices;
		customerOrders = new TreeMap<String, Integer>();
	}

	@Override
	public void run() {
		try {
			//Reading the contents of orderFile
			Scanner sc = new Scanner(orderFile);
			sc.next();
			//Assinging the id to the customer
			System.out.println("Reading order for client with id: " + sc.next());
			
			while(sc.hasNext()) {
				//Updating the customerOrders map accordingly
				String id = sc.next();
				if(!customerOrders.containsKey(id)) { customerOrders.put(id, 1); }
				else { customerOrders.put(id, customerOrders.get(id) + 1); }
				sc.next();
			}
			sc.close();
		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		}
	}
	
	public double getTotal() {
		//Iterating over the customersOrders and multiplying by the quantity
		//of each product ordered by its price from the prices map
		double totalPrice = 0;
		for(Map.Entry<String, Integer> entry: customerOrders.entrySet()) {
			totalPrice += customerOrders.get(entry.getKey()).doubleValue() * prices.get(entry.getKey());
		}
		return totalPrice;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    Set<String> orderedProducts = new TreeSet<>(customerOrders.keySet());
	    for (String s : orderedProducts) {
	        int quantity = customerOrders.get(s);
	        if (quantity > 0) {
	            double cost = quantity * prices.get(s);
	            sb.append(String.format("Item's name: %s, Cost per item: $%.2f, Quantity: %d, Cost: $%.2f\n", s, prices.get(s), quantity, cost));
	        }
	    }
	    sb.append(String.format("Order Total: $%.2f\n", this.getTotal()));
	    return sb.toString();
	}
	
	public Integer get(String product) { 
		if(customerOrders.containsKey(product)) {
			return customerOrders.get(product);
		} else {
			return 0;
		}
	}
}