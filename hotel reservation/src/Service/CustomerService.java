package Service;

import Model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CustomerService {
    HashMap<String, Customer> customersMap;
    Set<Customer> customersSet;
    private static CustomerService customerService = null;

    private CustomerService() {
        customersMap = new HashMap<>();
        customersSet = new HashSet<>();
    }

    public static CustomerService getInstance() {
        if (null == customerService) {
            customerService = new CustomerService();
        }
        return customerService;
    }

    private void addToMap(HashMap<String, Customer> customers, Customer newCustomer) {
        customers.put(newCustomer.getEmail(), newCustomer);
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        addToMap(customersMap, newCustomer);
        customersSet.add(newCustomer);
    }

    public Customer getCustomer(String customerEmail) {
        return customersMap.get(customerEmail);
            }

    public Collection<Customer> getAllCustomers() {
        return customersSet;
    }
}


