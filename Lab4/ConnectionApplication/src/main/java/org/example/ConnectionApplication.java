package org.example;

import java.sql.*;

public class ConnectionApplication {

    private static Connection localConnect = null;
    private static Connection remoteConnect = null;
    private static final String localUrl = "jdbc:mysql://localhost:3306/Order_Management";
    private static final String remoteUrl = "jdbc:mysql://34.123.30.198/Inventory_Management";

    public static void main(String[] args) {

        try {

            //Create connections
            localConnect = DriverManager.getConnection(localUrl, "root", "Assam@1873");
            remoteConnect = DriverManager.getConnection(remoteUrl, "root", "Assam@1873");

            //fetch inventory from remote db
            String fetchQuery = "SELECT id, item_name, available_quantity FROM Inventory";
            Statement fetchStatement = remoteConnect.createStatement();
            ResultSet resultSet = fetchStatement.executeQuery(fetchQuery);

            while (resultSet.next()) {

                String itemName = resultSet.getString("item_name");
                int availableQuantity = resultSet.getInt("available_quantity");

                int quantity = 3;

                // create order in local db
                String createOrderQuery = "INSERT INTO Order_info (order_id, user_id, item_name, quantity, order_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement createOrderStatement = localConnect.prepareStatement(createOrderQuery);
                createOrderStatement.setInt(1, 1);
                createOrderStatement.setInt(2, 100);
                createOrderStatement.setString(3, itemName);
                createOrderStatement.setInt(4, quantity);
                createOrderStatement.setDate(5, new Date(System.currentTimeMillis()));
                createOrderStatement.executeUpdate();

                // update inventory in remote db
                int updatedQuantity = availableQuantity - quantity;
                String updateQuantityQuery = "UPDATE Inventory SET available_quantity = ? WHERE item_name = ?";
                PreparedStatement updateQuantityStatement = remoteConnect.prepareStatement(updateQuantityQuery);
                updateQuantityStatement.setInt(1, updatedQuantity);
                updateQuantityStatement.setString(2, itemName);
                updateQuantityStatement.executeUpdate();

            }

            // Close connections
            remoteConnect.close();
            localConnect.close();

        } catch(Exception e) {
            System.out.println(e);
        }
    }
}