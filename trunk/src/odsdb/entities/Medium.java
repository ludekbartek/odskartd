/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odsdb.entities;

import java.util.List;
import odsdb.ODSKartException;

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
