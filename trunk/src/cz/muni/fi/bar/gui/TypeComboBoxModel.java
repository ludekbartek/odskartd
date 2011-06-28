/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.bar.gui;

import cz.muni.fi.bar.odsdb.DBManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author bar
 */
class TypeComboBoxModel extends DefaultComboBoxModel{

    private List<String> types = new ArrayList<String>();
    private DBManager manager;
    private Object selecteItem;

    public TypeComboBoxModel() {
       
    }

    public void setDBManager(DBManager manager){
        this.manager = manager;
        types.addAll(this.manager.getMediaTypes());
    }
    @Override
    public int getSize() {
        return types.size();
    }

    @Override
    public Object getElementAt(int i) {
        return types.get(i);
    }
}
