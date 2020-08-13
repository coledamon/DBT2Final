package controllers;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.travja.utils.menu.Menu;
import me.travja.utils.menu.MenuOption;
import me.travja.utils.utils.IOUtils;
import models.Couch;
import models.Customer;
import models.Order;



public class CouchConnect {
	private String path;
	private CouchDbInstance dbInstance;
	private CouchDbConnector db;
	private DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	
	private Menu menu = new Menu("What would you like to do?",
			new MenuOption("Create", () -> {
				Customer c = new Customer();
				c.setName(IOUtils.promptForString("Enter the Customer's Name: "));
				c.setBirthday(df.format(IOUtils.promptForDate("Enter the Customer's birthday in mm/dd/yyyy format: ")));
				List<Order> oList = new ArrayList<>();
				while(IOUtils.promptForBoolean("Would you like to add (another) order?(y/n)", "y", "n")) {
					oList.add(makeOrder());
				}
				c.setOrders(oList);
				db.create(c);
			}),
			new MenuOption("Create From File (Bulk Create)", () -> {
				String filename = IOUtils.promptForString("What is the name of the json file you would like to load?");
				try {
					bulkUpload(filename);
					System.out.println("Customers created!");
				} catch (Exception e) {
					try {
						Thread.sleep(500);
					} catch (Exception e1) {
					}
					System.out.println("\n\nThat file could not be found in the directory");
				}
			}),
			new MenuOption("Show all Files", () -> {
				List<String> allIds = db.getAllDocIds();
				for(String id : allIds) {
					System.out.println(db.get(Customer.class, id));
				}
			}),
			new MenuOption("Show all IDs", () -> {
				List<String> allIds = db.getAllDocIds();
				for(String s : allIds) {
					System.out.println(s + ",");
				}
			}),
			new MenuOption("Search (Read)", () -> {
				Customer c = db.find(Customer.class, IOUtils.promptForString("Enter the ID of the item you would like to find: "));
				if(c != null) {
					System.out.println(c);
				} else {
					System.out.println("There is no customer with that ID in the database.");
				}
			}),
			new MenuOption("Update", () -> {
				Customer c = db.get(Customer.class, IOUtils.promptForString("Enter the ID of the item you would like to update: "));
				List<String> validFieldNames = Arrays.asList("name", "birthday", "orders", "orders.date", "orders.couches", "orders.couches.brand", "orders.couches.couchType", "orders.couches.couchColor", "orders.price", "orders.tax");
				do {
					int orderSelection = 0;
					Order o = new Order();
					int couchSelection = 0;
					Couch co = new Couch();
					String updateField = IOUtils.promptForString("Enter the name of the field you would like to update (to access a field within orders or couches, use JSON object dot notation (orders.price): ");
					if(updateField.contains("orders.") && validFieldNames.contains(updateField)) {
						if(c.getOrders().size() > 0) {
							if(c.getOrders().size() > 1) {
								orderSelection = ConsoleIO.promptForMenuSelection("Which order would you like to update?", c.getOrders().stream().map(order -> order.toString()).collect(Collectors.toList()), false) - 1;
							}
							o = c.getOrders().get(orderSelection);
							if(updateField.contains(".couches.")) {
								if(c.getOrders().get(orderSelection).getCouches().size() > 1) {
									couchSelection = ConsoleIO.promptForMenuSelection("Which couch would you like to update in that order?", o.getCouches().stream().map(couch -> couch.toString()).collect(Collectors.toList()), false) - 1;
								}
								co = o.getCouches().get(couchSelection);
							}
						}
						else {
							System.out.println("You can't update a single field inside an order if no orders have been made. Update the orders field first.");
						}
					}
					switch(updateField) {
					case "name":
						c.setName(IOUtils.promptForString("Enter the Customer's Name: "));
						break;
					case "birthday":
						c.setBirthday(df.format(IOUtils.promptForDate("Enter the Customer's birthday in mm/dd/yyyy format: ")));
						break;
					case "orders":
						List<Order> oList = new ArrayList<>();
						do {
							oList.add(makeOrder());
						} while(IOUtils.promptForBoolean("Would you like to add (another) order?(y/n)", "y", "n"));
						c.setOrders(oList);
						break;
					case "orders.date":
						o.setDate(df.format(IOUtils.promptForDate("Enter the order date in mm/dd/yyyy format: ")));
						break;
					case "orders.couches":
						List<Couch> couches = new ArrayList<>();
						do {
							couches.add(makeCouch());
						} while(IOUtils.promptForBoolean("Would you like to add another couch?(y/n)", "y", "n"));
						o.setCouch(couches);
						break;
					case "orders.couches.brand":
						co.setBrand(IOUtils.promptForString("What brand is the couch?"));
						break;
					case "orders.couches.couchType":
						co.setCouchType(IOUtils.promptForString("What type is the couch? (Futon, Loveseat, etc.)" ));
						break;
					case "orders.couches.couchColor":
						co.setCouchColor(IOUtils.promptForString("What color is the couch?"));
						break;
					case "orders.price": 
						o.setPrice(IOUtils.promptForFloat("How much was the order before taxes?", 30, 2000));
						o.setTotal(o.getPrice() * (1 + o.getTax()));
						break;
					case "orders.tax":
						o.setTax(IOUtils.promptForFloat("What was the tax rate (as a decimal)?", 0.02f, 0.1f));
						o.setTotal(o.getPrice() * (1 + o.getTax()));
						break;
					default:
						System.out.println("Invalid field name, valid field names are " + validFieldNames);
					}
				} while(IOUtils.promptForBoolean("Would you like to update another field?(y/n)", "y", "n"));
				db.update(c);
			}),
			new MenuOption("Delete", () -> {
				Customer c = db.get(Customer.class, IOUtils.promptForString("Enter the ID of the item you would like to delete: "));
				db.delete(c);
				System.out.println("Customer Deleted");
			}),
			new MenuOption("Delete All", () -> {
				deleteAll();
				db.createDatabaseIfNotExists();
				System.out.println("All Customers Deleted");
			})
		);

	public void run() {
			HttpClient httpClient;
			path = IOUtils.promptForString("What is the name of the database you would like to connect to? (One will be created if it doesn't exist): ");
			try {
				httpClient = new StdHttpClient.Builder().url("http://localhost:5984").username("admin").password("password").build();
				dbInstance = new StdCouchDbInstance(httpClient);
				db = dbInstance.createConnector(path, true);
				System.out.println("\nSuccessfully Connected\n");
				
//				List<Customer> s = getCustomersFromFile("person.json");
//				System.out.println(s);
				
				menu.open(true);
								
//				bulkUpload("person.json");
//				deleteAll(path); 
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

	}
	
	
	
	public void bulkUpload(String fileName) {
		List<Customer> customers = getCustomersFromFile(fileName);
		db.executeBulk(customers);
	}
	
	public void deleteAll() {
		dbInstance.deleteDatabase(path);
	}
	
	public List<Customer> getCustomersFromFile(String fileName) {
		ObjectMapper mapper = new ObjectMapper();

		URL url = null;
		Customer[] peopleArray = null;

		try {
			url = new URL("file:" + fileName + (fileName.endsWith(".json") ? "" : ".json"));
		} catch (MalformedURLException e) {
			System.out.println("Invalid file name\n");
		}
		try {
			peopleArray = mapper.readValue(url, Customer[].class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		List<Customer> customers = new ArrayList<>(Arrays.asList(peopleArray));
		for(Customer c : customers) {
			c.setId(null);
			c.setRevision(null);
		}

		return customers;
	}
	
	public Couch makeCouch() {
		Couch c = new Couch();
		c.setBrand(IOUtils.promptForString("What brand is the couch?"));
		c.setCouchType(IOUtils.promptForString("What type is the couch? (Futon, Loveseat, etc.)" ));
		c.setCouchColor(IOUtils.promptForString("What color is the couch?"));
		return c;
	}
	
	public Order makeOrder() {
		Order o = new Order();
		o.setDate(df.format(IOUtils.promptForDate("Enter the order date in mm/dd/yyyy format: ")));
		System.out.println("For the couches in the order:\n");
		List<Couch> couches = new ArrayList<>();
		do {
			couches.add(makeCouch());
		} while(IOUtils.promptForBoolean("Would you like to add another couch?(y/n)", "y", "n"));
		o.setCouch(couches);
		o.setPrice(IOUtils.promptForFloat("How much was the order before taxes?", 30, 2000));
		o.setTax(IOUtils.promptForFloat("What was the tax rate (as a decimal)?", 0.02f, 0.1f));
		o.setTotal(o.getPrice()*(1+o.getTax()));
		return o;
	}

}
