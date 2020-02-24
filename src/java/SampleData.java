/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stormking
 */
public class SampleData {
    private final String primary_Key;
    private final String test_string;
    
    public SampleData(String key, String string){
        primary_Key = key;
        test_string = string;
    }
    
    public String getKey(){
        return primary_Key;
    }
    
    public String getString(){
        return test_string;
    }
    
}
