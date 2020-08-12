package models;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class SofaRepository extends CouchDbRepositorySupport<Customer> {

    public SofaRepository(CouchDbConnector db) {
        super(Customer.class, db);
    }

}
