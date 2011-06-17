/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.bar.odsdb;

import java.util.List;
import cz.muni.fi.bar.odsdb.entities.Medium;
import org.jopendocument.dom.spreadsheet.Sheet;

/**
 *
 * @author bar
 */
public interface DBManager {
    List<Medium> getMedia(String type);
    List<Medium> getAllMedia();
    List<Medium> getMediumByTitle(String title);
    List<Medium> getMediumByTitle(Sheet sheet, String title);
    List<String> getMediaTypes();
    void addMedium(String type,Medium data)throws ODSKartException;
}
