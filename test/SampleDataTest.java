/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author stormking
 */
public class SampleDataTest {
    
    SampleData data;

    /**
     * Test of getKey method, of class SampleData.
     */
    @Test
    public void testGetkey() {
        System.out.println("getkey");
        
        //Mock the SampleData class using Mockito
        data = mock(SampleData.class);
        when(data.getKey()).thenReturn("1");
               
        SampleData instance = new SampleData("1", "Hi");
        String expResult = data.getKey();
        String result = instance.getKey();
        assertEquals(expResult, result);
    }
    
}
