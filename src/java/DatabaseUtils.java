/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A class consisting of useful methods for connecting, reading, and updating
 * databases.
 */
public class DatabaseUtils
{
    private boolean canConnect;
    private final String connectionString;
    private final String dbUsername;
    private final String dbPassword;
     
    public DatabaseUtils(String connection, String usr, String pass)
    {
        connectionString = connection;
        dbUsername = usr;
        dbPassword = pass;
        
        try
        {
            // Use the JDBC Driver Class for Connections
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establish Connection to the database
            Connection con = DriverManager.getConnection(
                    connectionString, dbUsername, dbPassword);
            
            canConnect = true;
            con.close();
        } 
        catch(Exception e)
        {
            System.out.println(e);
            canConnect = false;
        }         
    }
    
    /**
     * Returns the connection status of the database.
     * @return true if database connection established, false otherwise
     */
    public boolean isDatabaseReachable()
    {
        return canConnect;
    }
    
    /**
     * Search the database and if any data is contained then return it
     * @param tableName the name of the table to search for data
     * @return data contained in the table
     */
    public ArrayList<String[]> getDataFromTable(String tableName)
    {
        ArrayList<String[]> dataFound = new ArrayList<>();
        
        try
        {
            // Use the JDBC Driver Class for Connections
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establish Connection to the database
            Connection con = DriverManager.getConnection(
            connectionString, dbUsername, dbPassword);
            
            Statement stmt = con.createStatement();
            
            // Select all from the table specified
            String searchQuery = "SELECT * FROM " + tableName;
            
            ResultSet rs = stmt.executeQuery(searchQuery);
            
            // If the result set contains information, add to data found
            while(rs.next())
            {
                String[] rowOfData = new String[2];
                Integer primaryKey = (Integer)rs.getInt(1);
                rowOfData[0] = primaryKey.toString();
                rowOfData[1] = rs.getString(2);
                dataFound.add(rowOfData);
            }
            
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return dataFound;
    }

    /**
     * Method to check if the specified database contains any rows.
     * @param dbToCheck Name of the database to check.
     * @return true if database is empty, false otherwise
     */
    public boolean isDatabaseEmpty(String dbToCheck)
    {
        boolean isEmpty = true;
        
        try
        {
            // Use the JDBC Driver Class for Connections
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establish Connection to the database
            Connection con = DriverManager.getConnection(
            connectionString, dbUsername, dbPassword);
            
            // Select all from the LoginCredentials table where user and pass match
            String searchQuery = "SELECT * FROM " + dbToCheck;
            
            PreparedStatement stmnt = con.prepareStatement(searchQuery);
            
            ResultSet rs = stmnt.executeQuery();
            
            // If the result set contains an item, set isEmpty to false
            if(rs.next() == true)
            {
                isEmpty = false;
            }
            
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        
        return isEmpty;
    }
    
    /**
     * Convenience method for writing rows to a database by just passing 
     *  the following parameters
     * @param dbToAddRecords Name of the database to access
     * @param colNames Name of the columns in the order they appear in the db
     * @param colTypes Data types of columns in the order they appear in the db.
     *  Values can be "int", "String", or "Date"
     * @param rowToAdd The raw data of the row being added. Can be obtained by using 
     *  the object's toString() method.
     * @return indication of success/fail when writing the data
     */
    public boolean writeDataToDatabase(String dbToAddRecords, String[] colNames,
            String[] colTypes, String rowToAdd)
    {
        boolean isSuccessful = true;
        try
        {
            // Use the JDBC Driver Class for Connections
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establish Connection to the database
            Connection con = DriverManager.getConnection(
            connectionString, dbUsername, dbPassword);
            
            // Create an insert statement to add a row to the table
            String insertQuery = buildInsertQueryString(dbToAddRecords, colNames);
            
            // Prepare the statemnt by converting it into MySQL Data Types
            PreparedStatement preparedStmt = buildInsertPreparedStatement(con, 
                    insertQuery, colTypes, rowToAdd);
            
            // Execute the Prepared Statement
            preparedStmt.execute();
            preparedStmt.close();

            con.close();
        }
        catch (Exception e)
        {
            isSuccessful = false;
            System.out.println(e);
        }
        return isSuccessful;
    }
    
    /**
     * Method to delete one row from a specified table containing one Primary Key
     * @param dbToAccess Table name to access
     * @param primaryKeyName Name of the primary key column
     * @param keyValue Value of the primary key
     * @return true if successful, false otherwise
     */
    public boolean deleteRowOnePK(String dbToAccess, String primaryKeyName, String keyValue)
    {
        boolean isSuccessful = true;
        
        try
        {
            // Use the JDBC Driver Class for Connections
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establish Connection to the database
            Connection con = DriverManager.getConnection(
            connectionString, dbUsername, dbPassword);
            
            // Select all from the LoginCredentials table where user and pass match
            String deleteQuery = "DELETE FROM " + dbToAccess + " WHERE " + primaryKeyName + "='" + keyValue + "'";
            
            PreparedStatement stmnt = con.prepareStatement(deleteQuery);
            stmnt.executeUpdate(deleteQuery);
        }
        catch(Exception e)
        {
            isSuccessful = false;
            System.out.println(e);
        }
        return isSuccessful;
    }
    
    /**
     * A helper function to build an insert query statement given a db name and column names.
     * @param dbToAddRecords the name of the db to add a record to.
     * @param colNames the name of the columns in the db.
     * @return The insert query to add to the db specified.
     */
    private String buildInsertQueryString(String dbToAddRecords, String[] colNames)
    {
        String query = "INSERT into " + dbToAddRecords + " (";
        StringBuilder sb = new StringBuilder(query);

        for (String column : colNames) 
        {
            sb.append(column);
            sb.append(", ");
        }

        sb.delete(sb.length()-2, sb.length());
        sb.append(") values (");

        for (String column : colNames) 
        {
            sb.append("?, ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        
        query = sb.toString();
        return query;
    }
    
    /**
     * A helper function used to build the prepared statement to execute when
     *  writing to the database
     * @param con The database connection established
     * @param insertQuery The insert query to prepare
     * @param colTypes The column types of the database
     * @param rowToAdd The data of the row to add obtained with the .toString()
     *  method of the object being added.
     * @return A prepared statement that is ready for execution
     * @throws SQLException if any prepared statement method fails.
     */
    private PreparedStatement buildInsertPreparedStatement(Connection con, String insertQuery, 
            String[] colTypes, String rowToAdd) throws SQLException
    {
        PreparedStatement preparedStmt = con.prepareStatement(insertQuery);
        int colIndex = 1;
        String[] dataToAdd = rowToAdd.split(",");
        
        for(String type : colTypes)
        {
            switch (type) 
            {
                case "int":
                    preparedStmt.setInt(colIndex, Integer.parseInt(dataToAdd[colIndex - 1]));
                    colIndex++;
                    break;
                case "String":
                    preparedStmt.setString(colIndex, dataToAdd[colIndex - 1]);
                    colIndex++;
                    break;
                case "Date":
                    // String object representing a date in in the format "yyyy-[m]m-[d]d". 
                    //The leading zero for mm and dd may also be omitted
                    preparedStmt.setDate(colIndex, java.sql.Date.valueOf(dataToAdd[colIndex - 1]));
                    colIndex++;
                    break;
                default:
                    System.out.println("Invalid Column Type Entered");
                    break;
            }
        }
        
        return preparedStmt;
    }
    
}
