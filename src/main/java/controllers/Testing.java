package controllers;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import models.Customer;
import models.SofaRepository;


public class Testing {

	public static void main(String[] args) {
			HttpClient httpClient;
			try {
				httpClient = new StdHttpClient.Builder().url("http://localhost:5984").username("admin").password("password").build();
				CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
				CouchDbConnector db = dbInstance.createConnector("thing", true);
								
				Customer ektorp = new Customer();
				ektorp.setName("John Doe");
				
				db.create(ektorp);
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}


	}

}
