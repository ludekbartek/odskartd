/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.bar.odsdb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import cz.muni.fi.bar.odsdb.entities.Medium;
import cz.muni.fi.bar.odsdb.entities.MediumImpl;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet; 

/**
 *
 * @author bar
 */
public class ODSDBManager implements DBManager {
    private SpreadSheet database;
    private File input;
    private static final Logger logger = Logger.getLogger(ODSDBManager.class.getName());
    
    public ODSDBManager(String path)throws ODSKartException, FileNotFoundException{
        
        logger.addHandler(new StreamHandler(new FileOutputStream("ODSDBManager.log"),new SimpleFormatter()));
        input = new File(path);
        try {
            database = SpreadSheet.createFromFile(input);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Nelze otevrit soubor s db ("+path+").", ex);
            throw new ODSKartException("Nelze nacist data.",ex);
        }
    }
    
    @Override
    public List<Medium> getMedia(String type) {
        Sheet sheet;
        List<Medium> mediaList= new ArrayList<Medium>();
        for(int i=0;i<database.getSheetCount();i++){
            sheet = database.getSheet(i);
            if(type.equalsIgnoreCase(sheet.getName()))
            {
                for(int row=1;row<sheet.getRowCount();row++){
                    Medium medium = getMedium(sheet,row);
                    if(medium!=null){
                        mediaList.add(getMedium(sheet,row));
                    }
                }
            }
        }
        return mediaList;
    }

    

    @Override
    public List<Medium> getAllMedia() {
        Sheet sheet;
        List<Medium> mediaList = new ArrayList<Medium>();
        int sheetCount = database.getSheetCount();
        for(int i=0;i<sheetCount;i++){
            sheet = database.getSheet(i);
            System.out.println(sheet.getName());
            mediaList.addAll(getMedia(sheet.getName()));
        }    
        return mediaList;
    }

    @Override
    public List<String> getMediaTypes() {
        List<String> types = new ArrayList<String>();
        for(int i=0;i<database.getSheetCount();i++){
            types.add(database.getSheet(i).getName());
        }
        return types;
    }

    private Medium getMedium(Sheet sheet, int row) {
        Medium medium=new MediumImpl();
        for(int column=1;column<sheet.getColumnCount();column++){
            String header = sheet.getValueAt(column,0).toString();
            if(header.contains("Film")){
                String title = sheet.getValueAt(column,row).toString();
                if(title.length()>0)
                    medium.addTitle(title);
            }
        }
        return (medium.getTitles().size()>0?medium:null);
    }

    @Override
    public void addMedium(String type, Medium data)throws ODSKartException {
        Sheet sheet = getSheet(type);
        if(sheet!=null){
            sheet.setRowCount(sheet.getRowCount()+1);
            sheet.setValueAt(sheet.getColumnCount(), 0, sheet.getRowCount()-1);
            int column = 1;
            int row = sheet.getRowCount()-1;
            for(String title:data.getTitles()){
                System.err.println("RowCount: " + sheet.getRowCount());
                sheet.setValueAt(title, column, row);
                column++;
            }
        }
        try {
            database.saveAs(input);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE,"addMedium: Vystupni soubor "+input.getName()+" nenalezen",ex);
            throw new ODSKartException("Soubor "+input.getName()+" nenalezen",ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "addMedium: Vstup-vystupni chyba: ", ex);
            throw new ODSKartException(ex);
        }
        
    }

    private Sheet getSheet(String type) {
        Sheet sheet = null;
        for(int i=0;i<database.getSheetCount();i++){
            Sheet tmpSheet = database.getSheet(i);
            if(type.equals(tmpSheet.getName()))sheet = tmpSheet;
        }
        return sheet;
    }

    @Override
    public List<Medium> getMediumByTitle(Sheet sheet, String title) {
        List<Medium> mediaList = new ArrayList<Medium>();
        for(int row = 1;row<sheet.getRowCount();row++)
        {
            Medium line = getMedium(sheet,row);
            if(line.getTitles().contains(title)){
                mediaList.add(line);
            }
        }
        return mediaList;
    }
    
    @Override
    public List<Medium> getMediumByTitle(String title) {
        List<Medium> mediaList = new ArrayList<Medium>();
        for(int i=0;i<database.getSheetCount();i++){
            Sheet sheet=database.getSheet(i);
            List<Medium> tmpList = getMediumByTitle(sheet, title);
            if(tmpList.size()>0){
                mediaList.addAll(tmpList);
            }
        }
        return mediaList;
    }

    @Override
    public int getMaxTitlesCount() {
        int maxCount = 0;
        
        for(Medium medium:getAllMedia()){
            int count = medium.getTitles().size();
            if(count>maxCount)maxCount=count;
        }
        System.err.println("Max Titles cout:" + maxCount);
        return maxCount;
    }

    @Override
    public void store() {
        try {
            database.saveAs(input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ODSDBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ODSDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void replaceTitle(String type, int row, int column, String title) throws ODSKartException {
        if(column<1)throw new ODSKartException("Title index must be 1 at least ");
        Sheet sheet = getSheet(type);
        sheet.setValueAt(title, row, column);
    }

}
