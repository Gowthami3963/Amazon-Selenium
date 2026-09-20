package org.example.utilities;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "loginData")
    public Object[][] loginData(){
        return new Object[][]{
                {"testuser@gmail.com", "password123", "success"},
                {"invalid@gmail.com", "wrongpassword", "failure"},
                {"", "password123", "failure"},
                {"testuser@gmail.com", "", "failure"}
        };
    }

    @DataProvider(name = "searchData")
    public Object[][] searchData(){

        return new Object[][]{
                {"iPhone 15", "success"},
                {"Samsung Galaxy S24", "success"},
                {"xyz@#$invalid", "no_results"},
                {"", "empty_search"}
        };
    }

    @DataProvider(name = "cartData")
    public Object[][] cartData() {
        return new Object[][]{
                {"iPhone 15", "1", "29999"},
                {"Samsung Phone", "2", "59998"},
                {"iPad", "1", "45999"}
        };
    }

    @DataProvider(name = "paymentData")
    public Object[][] paymentData() {
        return new Object[][]{
                {"Credit Card", "Visa", "success"},
                {"Debit Card", "Mastercard", "success"},
                {"UPI", "GooglePay", "success"}
        };
    }


    @DataProvider(name = "addressData")
    public Object[][] addressData() {
        return new Object[][]{
                {"Home", "success"},
                {"Office", "success"},
                {"Other", "success"}
        };
    }
}
