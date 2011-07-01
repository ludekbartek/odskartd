/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.bar.odsdb.entities;

import java.util.List;
import cz.muni.fi.bar.odsdb.ODSKartException;

/**
 *
 * @author bar
 */
public interface Medium {
    List<String> getTitles();
    void modifyTitle(String newTitle,int position)throws ODSKartException;
    String getTitle(int position)throws ODSKartException;
    void addTitle(String title);
    
}
