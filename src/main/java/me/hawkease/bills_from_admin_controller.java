package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.HashMap;

public class bills_from_admin_controller {

    @FXML
    private TextField tranx_id;

    @FXML
    private TextField amount;

    @FXML
    private Text massage;

    @FXML
    void enter_bill(MouseEvent event) {
        bills_from_admin_sql trans = new bills_from_admin_sql();
        if(trans.insert(tranx_id.getText(),amount.getText())) massage.setText("Bill Inserted Successfully");
        else massage.setText("Bill Insertion Failed");
    }

    @FXML
    void process_bills(MouseEvent event) {
        try {
            // Create database handler instances outside the loop for reuse
            bills_from_admin_sql billsFromAdminSQL = new bills_from_admin_sql();
            bills_from_seller_sql billsFromSellerSQL = new bills_from_seller_sql();
            rent_to_be_paid_sql rentToBePaidSQL = new rent_to_be_paid_sql();

            // Get all bills from admin database
            HashMap<String, Integer> bills = billsFromAdminSQL.get_bills();
            System.out.println("Total bills to process: " + bills.size());

            int successCount = 0;
            int failCount = 0;

            // Process each bill
            for (String billId : bills.keySet()) {
                System.out.println("Processing bill: " + billId);

                try {
                    // Check if bill exists in seller database and can be deleted from admin
                    if (billsFromSellerSQL.check(billId) && billsFromAdminSQL.delete_bill(billId)) {
                        System.out.println(billId + " - Bill deleted successfully from admin database");

                        // Get location information and rent amount
                        Pair<String, location_info> info = billsFromSellerSQL.get_location(billId);
                        int rent = bills.get(billId);

                        // Update the rent to be paid database and delete from seller database
                        String sellerId = info.getKey();
                        location_info location = info.getValue();

                        if (rentToBePaidSQL.update_bill(sellerId, location.getLatitude(),
                                location.getLongitude(), rent) && billsFromSellerSQL.delete(billId)) {
                            System.out.println(billId + " - Bill updated successfully in rent database");
                            successCount++;
                        } else {
                            System.out.println(billId + " - Bill update failed in rent database");
                            failCount++;
                        }
                    } else {
                        System.out.println(billId + " - Bill check failed or couldn't be deleted from admin");
                        failCount++;
                    }
                } catch (Exception e) {
                    System.err.println("Error processing bill " + billId + ": " + e.getMessage());
                    failCount++;
                }
            }

            // Display summary message
            if (failCount == 0) {
                massage.setText("All bills processed successfully (" + successCount + " bills)");
            } else {
                massage.setText("Bills processed with some issues: " +
                        successCount + " successful, " + failCount + " failed");
            }

        } catch (Exception e) {
            System.err.println("Error in bill processing: " + e.getMessage());
            e.printStackTrace();
            massage.setText("Error processing bills: " + e.getMessage());
        } finally {
            // Close resources if your SQL classes have close methods
            // billsFromAdminSQL.close();
            // billsFromSellerSQL.close();
            // rentToBePaidSQL.close();
        }
    }

}
