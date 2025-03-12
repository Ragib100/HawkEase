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
        admin_bill_sql trans = new admin_bill_sql();
        if(trans.insert(tranx_id.getText(),amount.getText())) massage.setText("Bill Inserted Successfully");
        else massage.setText("Bill Insertion Failed");
    }

    @FXML
    void process_bills(MouseEvent event) {
        try {
            admin_bill_sql billsFromAdminSQL = new admin_bill_sql();
            seller_bill_sql billsFromSellerSQL = new seller_bill_sql();
            rent_to_be_paid_sql rentToBePaidSQL = new rent_to_be_paid_sql();

            HashMap<String, Integer> bills = billsFromAdminSQL.get_bills();
            System.out.println("Total bills to process: " + bills.size());

            int successCount = 0;
            int failCount = 0;

            for (String billId : bills.keySet()) {
                System.out.println("Processing bill: " + billId);

                try {
                    if (billsFromSellerSQL.check(billId) && billsFromAdminSQL.delete_bill(billId)) {
                        System.out.println(billId + " - Bill deleted successfully from admin database");

                        Pair<String, location_info> info = billsFromSellerSQL.get_location(billId);
                        int rent = bills.get(billId);

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

            massage.setText("Error processing bills: ");
        }
    }

}
