/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ODSKart.java
 *
 * Created on 17.6.2011, 19:47:45
 */
package cz.muni.fi.bar.gui;

import cz.muni.fi.bar.odsdb.DBManager;
import cz.muni.fi.bar.odsdb.ODSDBManager;
import cz.muni.fi.bar.odsdb.ODSKartException;
import cz.muni.fi.bar.odsdb.entities.Medium;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bar
 */
public class ODSKart extends javax.swing.JFrame {

    private class ComboBoxAction extends AbstractAction {

        public ComboBoxAction() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ODSKartTableModel model = (ODSKartTableModel) jTable1.getModel();
            String type = (String)jComboBox1.getSelectedItem();
            videotekModel.setType(type);
            videotekModel.reload();
            System.err.println("Type:" + type);
        }
    }

    private class OpenAction extends AbstractAction {

        private JFrame parent; 
        public OpenAction(JFrame frame) {
            this.parent = frame;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(manager!=null)manager.store();
            if(jFileChooser1.showOpenDialog(parent)==JFileChooser.APPROVE_OPTION){
                try {
                    manager = new ODSDBManager(jFileChooser1.getSelectedFile().getAbsolutePath());
                } catch (ODSKartException ex) {
                    Logger.getLogger(ODSKart.class.getName()).log(Level.SEVERE, null, ex);
                    manager = null;
                } catch (FileNotFoundException ex) {
                    manager = null;
                }
                ((TypeComboBoxModel)comboBoxModel1).setDBManager(manager);
                videotekModel.setDataManager(manager);
                jMenuItem1.setEnabled(true);
                videotekModel.reload();
            }
        }

    }

    private class ODSKartTableModel extends AbstractTableModel {

        private DBManager dataManager = null; 
        private String type = null;
        private List<Medium> media = new ArrayList<Medium>();
        public ODSKartTableModel() {
        }
        
        public void setType(String type){
            this.type = type;
        }

        public void setDataManager(DBManager dbManager)
        {
            this.dataManager = dbManager;
        }
        
        @Override
        public int getRowCount() {
            int rowCount = media.size();
            System.err.println("Medii: " + rowCount);
            return rowCount;
        }

        @Override
        public int getColumnCount() {
            int maxTitles =10;
          /*  for(Medium medium:media){
                int count = medium.getTitles().size();
                if(maxTitles < count)maxTitles = count;
            }*/
            return maxTitles;
            //return dataManager == null?0:dataManager.getMaxTitlesCount();
        }
        
        @Override
        public Class<?> getColumnClass(int column){
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int row, int column){
            return true;
        }

        @Override
        public Object getValueAt(int row, int column) {
            /*if(row == 0){
                return "Titul" + column;
            }*/
            if(row>media.size())return "";
            List<String> titles = media.get(row).getTitles();
            if(column>titles.size())return "";
            String title="";
            try{
             title = media.get(row).getTitle(column);
            }catch(ODSKartException ex){
                System.err.println(ex);
            }
            return title;
        }
        
        @Override
        public String getColumnName(int column){
            return "Film "+(column+1);
        }

        private void reload() {
            if(type==null)
                media = dataManager.getAllMedia();
            else media = dataManager.getMedia(type);
           // System.err.println("Media ("+type+"): " + media);
            fireTableDataChanged();
        }
        
        @Override
        public void setValueAt(Object value, int row, int column){
            String title = (String)value;
            //Medium medium = media.get(row);
            try {
                manager.replaceTitle(type,row,column+1, title);
            } catch (ODSKartException ex) {
                Logger.getLogger(ODSKart.class.getName()).log(Level.SEVERE, "Unable to set the title:", ex);
            }
        }
    }
    
    private class ExitAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(manager!=null)manager.store();
            System.exit(0);
        }
        
    }

    private DBManager manager;
    private ComboBoxModel comboBoxModel1;
    private ODSKartTableModel videotekModel;
    /** Creates new form ODSKart */
    public ODSKart() {
        comboBoxModel1 = (ComboBoxModel) new TypeComboBoxModel();
        videotekModel = new ODSKartTableModel();
        initComponents();
        jTable1.setModel(videotekModel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        jButton1.setAction(new OpenAction(this));
        jButton1.setText("Otevřít");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton2.setText("Uložit");
        jButton2.setEnabled(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jComboBox1.setModel(comboBoxModel1);
        jComboBox1.setAction(new ComboBoxAction());
        jToolBar1.add(jComboBox1);

        jTable1.setModel(new ODSKartTableModel());
        jScrollPane1.setViewportView(jTable1);

        jMenu1.setText("Kartotéka");

        jMenuItem3.setAction(new OpenAction(this));
        jMenuItem3.setText("Otevřít");
        jMenu1.add(jMenuItem3);

        jMenuItem1.setText("Uložit");
        jMenuItem1.setEnabled(false);
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAction(new ExitAction());
        jMenuItem2.setText("Konec");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Médium");

        jMenuItem4.setText("Přidat");
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Změnit");
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 317, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ODSKart().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
