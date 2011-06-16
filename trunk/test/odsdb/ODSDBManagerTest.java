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
import javax.swing.table.DefaultTableModel;
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
    private List<Medium> expResultAll = new ArrayList<Medium>();
    
    public ODSDBManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        String[][] data={{"1","a","b"}};
        String[][] nodata={{"","",""}};
        String[] names={"id","Film 1", "Film 2"};
        TableModel model =new DefaultTableModel(data, names);
        TableModel emptyModel = new DefaultTableModel(nodata,names);
        SpreadSheet.createEmpty(model).saveAs(new File("test.ods"));
        SpreadSheet.createEmpty(emptyModel).saveAs(new File("empty.ods"));
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() throws IOException {
        String[][] data={{"1","a","b"}};
        String[] nonames={"","",""};
        String[][] nodata={{"","",""}};
        String[] names={"id","Film 1", "Film 2"};
        TableModel model =new DefaultTableModel(nodata, nonames);
        SpreadSheet file1 = SpreadSheet.createEmpty(model);
        
        /*file1.addSheet(0,"a");
        Sheet sheetA = file1.getSheet("a");*/
        Sheet sheetB = file1.getSheet(0);
        sheetB.setName("b");
        sheetB.setColumnCount(3);
        sheetB.setRowCount(2);
        Medium medium = new MediumImpl();
        for(int i=0;i<names.length;i++){
            sheetB.setValueAt(names[i], i, 0);
            
        }
        file1.saveAs(new File("empty.ods"));
        for(int i=0;i<data[0].length;i++){
            sheetB.setValueAt(names[i],i,0);
            if(i>0)
                sheetB.setValueAt("b" + data[0][i], i, 1);
            else 
                sheetB.setValueAt(data[0][i],i,1);
            if(i>0)
                medium.addTitle("b" + data[0][i]);
        }
        expResultAll.add(medium);
        
        medium = new MediumImpl();
        
        file1.saveAs(new File("test.ods"));
        Sheet sheetC = file1.addSheet(1,"c");
        sheetC.setName("c");
        sheetC.setColumnCount(3);
        sheetC.setRowCount(2);
        for(int i=0 ; i<sheetC.getColumnCount() ; i++){
            sheetC.setValueAt(names[i], i, 0);
            if(i>0)
                sheetC.setValueAt("c" + data[0][i], i, 1);
            else
                sheetC.setValueAt(data[0][i],i,1);
            if(i>0)
                medium.addTitle("c" + data[0][i]);
        }
        expResultAll.add(medium);
        file1.saveAs(new File("long.ods"));
        
        
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
        String type = "b";
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
        medium.addTitle("ba");
        medium.addTitle("bb");
        expResult.add(medium);
        List<Medium> result = instance.getMedia(type);
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
        DBManager instance = new ODSDBManager("long.ods");
        List<Medium> result = instance.getAllMedia();
        assertEquals(expResultAll, result);
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
        expResult.add("b");
        List<String> result = instance.getMediaTypes();
        assertEquals(expResult, result);
        instance = new ODSDBManager("long.ods");
        expResult.add("c");
        result = instance.getMediaTypes();
        assertEquals(expResult,result);
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
        DBManager instance = new ODSDBManager("empty.ods");
        List<Medium> expResult = instance.getMedia("b");
        instance.addMedium("b", data);
        expResult.add(data);
        List<Medium> result = instance.getMedia("b");
        assertTrue(result.contains(data));
        
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
