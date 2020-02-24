/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author stormking
 */
public class WebServlet extends HttpServlet {
    
    private final String test = "test";
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
        
        //Create a JSON object to be returned
        JSONObject json = new JSONObject();
        
        
        
        //Create a DatabaseUtil object representing the test_schema Schema in mySQL
        DatabaseUtils sampleDatabase = new DatabaseUtils(CONNECTION,DBUSER,DBPASS);
        
        //Test connection to database
        String connectionString = "Not Connected";
        if(sampleDatabase.isDatabaseReachable()){
            connectionString = "Connected!";
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            //Get data from test_table
            ArrayList<String[]> dataFromTable = sampleDatabase.getDataFromTable(TABLE);
            
            if(!dataFromTable.isEmpty()){
                for(String[] data : dataFromTable){
                    try {
                        SampleData tableData = new SampleData(data[0],data[1]);
                        json.accumulate("col1", tableData.getKey());
                        json.accumulate("col2", tableData.getString());
                    } catch (JSONException ex) {
                        Logger.getLogger(WebServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            out.print(json);
            out.flush();
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
    
}
