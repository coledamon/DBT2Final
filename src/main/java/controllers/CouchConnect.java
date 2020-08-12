package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.travja.utils.menu.Menu;
import me.travja.utils.menu.MenuOption;
import me.travja.utils.utils.IOUtils;
import models.*;


public class CouchConnect {
	private String path;
	private CouchDbInstance dbInstance;
	private CouchDbConnector db;
	
	private Menu menu = new Menu("What would you like to do?",
			new MenuOption("Create", () -> {
				Customer c = new Customer();
				c.setName(IOUtils.promptForString("Enter the Customer's Name: "));
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				c.setBirthday(df.format(IOUtils.promptForDate("Enter the Customer's birthday in mm/dd/yyyy format: ")));
				List<Order> oList = new ArrayList<>();
				while(IOUtils.promptForBoolean("Would you like to add (another) order?(y/n)", "y", "n")) {
					Order o = new Order();
					o.setDate(df.format(IOUtils.promptForDate("Enter the order date in mm/dd/yyyy format: ")));
					System.out.println("For the couches in the order:\n");
					List<Couch> couches = new ArrayList<>();
					do {
						Couch co = new Couch();
						co.setBrand(IOUtils.promptForString("What brand is the couch?"));
						co.setCouchType(IOUtils.promptForString("What type is the couch? (Futon, Loveseat, etc.)" ));
						co.setCouchColor(IOUtils.promptForString("What color is the couch?"));
						couches.add(co);
					} while(IOUtils.promptForBoolean("Would you like to add another couch?(y/n)", "y", "n"));
					o.setCouch(couches);
					o.setPrice(IOUtils.promptForFloat("How much was the order before taxes?", 30, 2000));
					o.setTax(IOUtils.promptForFloat("What was the tax rate (as a decimal)?", 0.02f, 0.1f));
					o.setTotal(o.getPrice()*(1+o.getTax()));
					oList.add(o);
				}
				c.setOrders(oList);
				db.create(c);
			}),
			new MenuOption("Bulk Create", () -> {
				String filename = IOUtils.promptForString("What is the name of the json file you would like to load?");
				bulkUpload(filename);
				System.out.println("Customers created!");
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
				System.out.println(c);
			}),
			new MenuOption("Update", () -> {

			}),
			new MenuOption("Delete", () -> {
				
			}),
			new MenuOption("Delete All", () -> {
				deleteAll();
				db.createDatabaseIfNotExists();
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
								
				bulkUpload("person.json");
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

}
