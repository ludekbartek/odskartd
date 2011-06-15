/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odsdb.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import odsdb.ODSKartException;

/**
 *
 * @author bar
 */
public class MediumImpl implements Medium {
    private List<String> titles;
    

    public MediumImpl(){
        titles = new ArrayList<String>();
    }
    
    @Override
    public List<String> getTitles() {
        return Collections.unmodifiableList(titles);
    }

    @Override
    public void modifyTitle(String newTitle, int position) throws ODSKartException{
        if(titles.size()>=position)throw new ODSKartException("modifyTitle: Neplatne cislo filmu na mediu "+position+".");
            titles.remove(position);
            titles.add(position, newTitle);
            
        }

    @Override
    public String getTitle(int position) throws ODSKartException {
        if(position >=titles.size())throw new ODSKartException("getTitle: Neplatne cislo filmu na mediu "+position+".");
        return titles.get(position);
    }

    @Override
    public void addTitle(String title) {
        titles.add(title);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MediumImpl other = (MediumImpl) obj;
        if (this.titles != other.titles && (this.titles == null || !this.titles.equals(other.titles))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.titles != null ? this.titles.hashCode() : 0);
        return hash;
    }
    
}
