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
import cz.muni.fi.bar.odsdb.entities.MediumImpl;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bar
 */
public class ODSKart extends javax.swing.JFrame {

    private class AddMediumAction extends AbstractAction{

        private javax.swing.JTable table;
        
        public AddMediumAction(String caption,javax.swing.JTable table){
            super(caption);
            this.table = table;
        }
        @Override
        public void actionPerformed(ActionEvent ae) {
            ODSKartTableModel model = (ODSKartTableModel) table.getModel();
            model.addMedium();
        }
        
    }
    
    private class AddMovieAction extends AbstractAction {

        
        
        public AddMovieAction(String string) {
            super(string);
            
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(jTable1.getSelectedRows().length>0)
            {
                jDialog1.setVisible(true);
                Medium medium = null;
                if(filmOk){
                    System.err.println("Pridavam film");
                    ODSKartTableModel tableModel = (ODSKartTableModel) jTable1.getModel();
                    try{
                        int index = jTable1.getSelectedRow();
                        medium = tableModel.getMedium(index);
                    }catch(ODSKartException ex){
                      logger.log(Level.SEVERE, "Nelze ziskat medium pro pridani filmu: {0}", ex);   
                    }
                    if(medium!=null){
                        //jDialog1.setVisible(fal);
                        medium.addTitle(jTextField1.getText());
                        tableModel.updateView();
                    }
                }
            }else{
                jDialog2.setVisible(true);
            }
            
        }

        
    }

    private boolean filmOk = false;
    private class ComboBoxAction extends AbstractAction {

        public ComboBoxAction() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ODSKartTableModel model = (ODSKartTableModel) jTable1.getModel();
            String type = (String)jComboBox1.getSelectedItem();
            videotekModel.setType(type);
            videotekModel.reload();
           // System.err.println("Type:" + type);
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
                jMenu2.setEnabled(true);
                jMenu3.setEnabled(true);
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
           // System.err.println("Medii: " + rowCount);
            return rowCount;
        }

        @Override
        public int getColumnCount() {
            int maxTitles = 5;
            if(media!=null && media.size()>0){
                for(Medium medium:media){
                    int count = medium.getTitles().size();
                    if(maxTitles < count)maxTitles = count;
                }
            }
            return maxTitles;
            //return dataManager == null?0:dataManager.getMaxTitlesCount();
        }
        
        @Override
        public Class<?> getColumnClass(int column){
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int row, int column){
            return row<media.size() && column < media.get(row).getTitles().size();
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
                if(row<media.size()){
                    Medium medium = media.get(row);
                    if(column<medium.getTitles().size())
                        title = medium.getTitle(column);
                }
                
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
        
        public Medium getMedium(int index) throws ODSKartException{
            if(index>media.size()||index<0)throw new ODSKartException("Invalid medium number "+index+". There are " + media.size()+" media.");
            return media.get(index);
        }

        private void addMedium() {
            media.add(new MediumImpl());
            fireTableRowsInserted(0, media.size());
        }

        private void updateView() {
            fireTableCellUpdated(0,media.size());
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
    private Logger logger;
    /** Creates new form ODSKart */
    public ODSKart() {
        comboBoxModel1 = (ComboBoxModel) new TypeComboBoxModel();
        videotekModel = new ODSKartTableModel();
        initComponents();
        try {
            logger = Logger.getLogger(ODSKart.class.getName());
            logger.addHandler(new StreamHandler(new FileOutputStream("ODSKart.log"),new SimpleFormatter()));
            
        } catch (FileNotFoundException ex) {
            System.err.println("Exception while creating logger:"+ex);
            logger = Logger.getAnonymousLogger();
        }
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
        jDialog1 = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
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
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        jDialog1.setTitle("Přidání filmu");
        jDialog1.setModal(true);
        jDialog1.setName("filmDialog"); // NOI18N
        jDialog1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jDialog1.setSize(jDialog1.getPreferredSize());
        jDialog1.pack();

        jLabel1.setText("Jméno filmu:");
        jLabel1.setName("movieTitle"); // NOI18N
        jDialog1.getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 26, -1, -1));

        jTextField1.setColumns(25);
        jTextField1.setName("movieTitleField"); // NOI18N
        jDialog1.getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 20, -1, -1));

        jButton3.setText("Ok");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(448, 21, 86, -1));

        jButton4.setText("Cancel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(448, 55, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Nejdříve je nutné zvolit médium.");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 11, -1, -1));

        jButton5.setText("OK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 34, -1, -1));

        org.jdesktop.layout.GroupLayout jDialog2Layout = new org.jdesktop.layout.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

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
        jMenu2.setEnabled(false);

        jMenuItem4.setAction(new AddMediumAction("Přidat",jTable1));
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Změnit");
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Titul");
        jMenu3.setEnabled(false);

        jMenuItem6.setAction(new AddMovieAction("Přidat médium"));
        jMenuItem6.setText("Přidat titul");
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jDialog1.setVisible(false);
        filmOk = true;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jDialog1.setVisible(false);
        filmOk = false;
   
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jDialog2.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
