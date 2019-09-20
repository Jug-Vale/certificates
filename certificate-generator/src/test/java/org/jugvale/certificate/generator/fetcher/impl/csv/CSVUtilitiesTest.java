package org.jugvale.certificate.generator.fetcher.impl.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CSVUtilitiesTest {
    
    String name = "Antonio Silva";
    String withoutQuotes = "test without quotes";
    String email  = "antonio@email.com";
    long longValue = 1l;
    long longValue2 = 2l;
    
    String line = longValue + ", "+withoutQuotes+","+longValue2+",\""+name+"\",\""+email+"\",true    ,\"true\"";
    
    @Test
    public void testParsing() {
        String columns[]  = CSVUtilities.columns(line);
        assertEquals(7, columns.length);
        
        assertEquals(longValue, CSVUtilities.getLongValue(0, columns));
        assertEquals(withoutQuotes, CSVUtilities.getStringValue(1, columns));
        assertEquals(longValue2, CSVUtilities.getLongValue(2, columns));
        assertEquals(name, CSVUtilities.getStringValue(3, columns));
        assertEquals(email, CSVUtilities.getStringValue(4, columns));
        assertTrue(CSVUtilities.getBooleanValue(5, columns));
        assertTrue(CSVUtilities.getBooleanValue(6, columns));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> CSVUtilities.getStringValue(100, columns));
        
        
    }

}
