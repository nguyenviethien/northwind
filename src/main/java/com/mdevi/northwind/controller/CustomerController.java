package com.mdevi.northwind.controller;

import com.mdevi.northwind.model.Customer;
import com.mdevi.northwind.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping()
    public String getCustomersList(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers";
    }

    @RequestMapping(value = "/CustomerById", method = RequestMethod.GET)
    public String getCustomerByID(Model model, @RequestParam("id") String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        model.addAttribute("theCustomer", customer);
        return "customer";
    }

    //DONE Create a controller method to select customers by name, country, city

    @RequestMapping(value = "/CustomersByName", method = RequestMethod.GET)
    public String getCustomersByName(Model model, @RequestParam("name") String customerName) {
        model.addAttribute("customers", customerService.getCustomersByName(customerName));
        return "customers";
    }

    @RequestMapping(value = "/CustomersByFilter/{params}", method = RequestMethod.GET)
    public String getCustomersByFilter(Model model, @MatrixVariable(pathVar = "params") Map<String, List<String>> filterParams) {
        model.addAttribute("customers", customerService.getCustomersByFilter(filterParams));
        return "customers";
    }

    //DONE Create a controller method to delete customer by its id.
    @RequestMapping(value = "/delete")
    public String deleteCustomerByID(@RequestParam("id") String customerId) {
        customerService.deleteCustomerByID(customerId);
        return "redirect:/customers";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAddNewCustomerForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("theCustomer", customer);
        return "customer";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddNewCustomerForm(@ModelAttribute("theCustomer") @Valid Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            return "customer";
        }
        String[] suppressedFields = result.getSuppressedFields();
        if (suppressedFields.length > 0) {
            throw new RuntimeException("Attempting to bind disallowed fields:" + StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }
        customerService.save(customer);
        return "redirect:/customers";
    }

    @InitBinder
    public void initializeBinder(WebDataBinder binder) {
        binder.setAllowedFields("customerid", "companyname", "contactname",
                "contacttitle", "address", "city", "region", "postalcode", "country");
    }
}
