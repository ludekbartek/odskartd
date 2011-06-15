/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odsdb;

import odsdb.entities.MediumImpl;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelListener;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import java.util.List;
import javax.swing.table.TableModel;
import odsdb.entities.Medium;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bar
 */
public class ODSDBManagerTest {
    
    private SpreadSheet database;
    
    public ODSDBManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() throws IOException {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMedia method, of class ODSDBManager.
     */
    @Test()
    public void testGetMedia() {
        System.out.println("getMedia");
        String type = "";
        DBManager instance=null;
        try {
            instance = new ODSDBManager("test.ods");
        } catch (ODSKartException ex) {
            fail("Neocekavana vyjimka " +ex);
        } catch (FileNotFoundException ex) {
            fail("Neocekavana vyjimka " + ex);
        }
        
        List<Medium> expResult = new ArrayList<Medium>();
        Medium medium = new MediumImpl();
        medium.addTitle("pokus1");
        medium.addTitle("pokus2");
        expResult.add(medium);
        List<Medium> result = instance.getMedia("a");
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of getAllMedia method, of class ODSDBManager.
     */
    @Test
    public void testGetAllMedia() throws ODSKartException, FileNotFoundException {
        System.out.println("getAllMedia");
        DBManager instance = new ODSDBManager("test.ods");
        List<Medium> expResult = new ArrayList<Medium>();
        Medium med1 = new MediumImpl();
        Medium med2 = new MediumImpl();
        med1.addTitle("pokus1");
        med1.addTitle("pokus2");
        med2.addTitle("pokus3");
        med2.addTitle("pokus4");
        expResult.add(med1);
        expResult.add(med2);
        List<Medium> result = instance.getAllMedia();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of getMediaTypes method, of class ODSDBManager.
     */
    @Test
    public void testGetMediaTypes() throws ODSKartException, FileNotFoundException {
        System.out.println("getMediaTypes");
        DBManager instance = new ODSDBManager("test.ods");
        List<String> expResult = new ArrayList<String>();
        expResult.add("a");
        expResult.add("b");
        List<String> result = instance.getMediaTypes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
      //  fail("The test case is a prototype.");
    }

    /**
     * Test of addMedium method, of class ODSDBManager.
     */
    @Test
    public void testAddMedium() throws Exception {
        System.out.println("addMedium");
        String type = "";
        Medium data = new MediumImpl();
        data.addTitle("test1");
        data.addTitle("test2");
        DBManager instance = new ODSDBManager("test.ods");
        instance.addMedium("a", data);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMediumByTitle method, of class ODSDBManager.
     */
    @Test
    public void testGetMediumByTitle_Sheet_String() {
        System.out.println("getMediumByTitle");
        Sheet sheet = null;
        String title = "";
        ODSDBManager instance = null;
        List expResult = null;
        List result = instance.getMediumByTitle(sheet, title);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMediumByTitle method, of class ODSDBManager.
     */
    @Test
    public void testGetMediumByTitle_String() {
        System.out.println("getMediumByTitle");
        String title = "";
        ODSDBManager instance = null;
        List expResult = null;
        List result = instance.getMediumByTitle(title);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
