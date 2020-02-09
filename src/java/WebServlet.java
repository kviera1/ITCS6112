/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author stormking
 */
public class WebServlet extends HttpServlet {
    
    private final String CONNECTION = "jdbc:mysql://localhost:3306/test_schema";
    private final String DBUSER = "root";
    private final String DBPASS = "Familyof7!";
    private final String TABLE = "test_table";
    

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //Create a DatabaseUtil object representing the test_schema Schema in mySQL
        DatabaseUtils sampleDatabase = new DatabaseUtils(CONNECTION,DBUSER,DBPASS);
        
        //Test connection to database
        String connectionString = "Not Connected";
        if(sampleDatabase.isDatabaseReachable()){
            connectionString = "Connected!";
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<h1>Welcome to my first servlet application in Netbeans</h1>");
            out.println("<p>Database Connection Status: " + connectionString + "</p>");
            
            //Print beginning data from test_table
            out.println("<p>Running sql query: SELECT * FROM test_table</p>");
            ArrayList<String[]> dataFromTable = sampleDatabase.getDataFromTable(TABLE);
            printTable(out, dataFromTable);           
        }
   
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private void printTable(PrintWriter out, ArrayList<String[]> dataFromTable){
        out.println("<p>Printing contents of test_table... " 
                + dataFromTable.size() + " rows of data found.</p>");
        out.println("+---------------+--------------+");
        out.println("<br /> | primary_key | test_string  |");
        out.println("<br />+---------------+--------------+");
        if(!dataFromTable.isEmpty()){
            for(String[] data : dataFromTable){
                out.println("<br /><pre>|     " + data[0] + "    |     " + 
                        data[1] + "  |</pre>");
                out.println("+---------------+--------------+");
            }
        }
        else{
            out.println("<p>Table " + TABLE + " Empty!</p>");
        }
    }
    
}
