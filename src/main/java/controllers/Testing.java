package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.travja.utils.utils.IOUtils;

import models.Customer;


public class Testing {

	public static void main(String[] args) {
			HttpClient httpClient;
			String path = IOUtils.promptForString("What is the name of the database you would like to connect to? (One will be created if it doesn't exist): ");
			try {
				httpClient = new StdHttpClient.Builder().url("http://localhost:5984").username("admin").password("password").build();
				CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
				CouchDbConnector db = dbInstance.createConnector(path, true);
				System.out.println("\nSuccessfully Connected\n\n");
				
//				List<Customer> s = getCustomersFromFile("person.json");
//				System.out.println(s);
				
				
				
				bulkUpload("person.json", db);
//				deleteAll(dbInstance, path); 
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

	}
	
	public static void bulkUpload(String fileName, CouchDbConnector db) {
		List<Customer> customers = getCustomersFromFile(fileName);
		db.executeBulk(customers);
	}
	
	public static void deleteAll(CouchDbInstance dbInstance, String path) {
		dbInstance.deleteDatabase(path);
	}
	
	public static List<Customer> getCustomersFromFile(String fileName) {
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
