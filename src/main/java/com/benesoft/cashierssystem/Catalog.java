package com.benesoft.cashierssystem;

//imports
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author benedict
 */
public class Catalog extends javax.swing.JFrame implements Runnable {
    @Override
    public void run() {
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }*/
        masterPopulate();
    }

    //global initializations
    ConnectionClass cls = new ConnectionClass();

    /**
     * Creates new form AllProducts
     */
    public Catalog() {
        initComponents();
    }

    /*--------------------------------
    Master method to popyulate table
    ---------------------------------*/
    public void masterPopulate() {
        if (cls.getConnection() == true) {
            populateProductsTable();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to establish connection to the server");
        }
    }

    /*--------------------------------------------------
    get all products 
    --------------------------------------------------*/
    private void populateProductsTable() {
        DefaultTableModel tb = (DefaultTableModel) catalogtable.getModel();
        try {
            String query = "SELECT * FROM products";
            PreparedStatement ps = cls.conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString("productCode").toLowerCase();
                String name = rs.getString("productName").toLowerCase();
                String category = rs.getString("productCategory").toLowerCase();
                String quantity = rs.getString("productQuantity").toLowerCase();
                String size = rs.getString("productSize").toLowerCase();
                String price = rs.getString("productPrice").toLowerCase();
                String[] tbdata = {code, name, category, quantity, size, price};
                //populate the table
                tb.addRow(tbdata);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong with fetching data");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Oops, something went wrong");
        }
    }

    
    
    /*------------------------------------------------------------------------
    Get the product code for the selected row or item by reading from jtable
    ------------------------------------------------------------------------*/
    protected String getproductCode(){
        //get data from table        
        String prodcode = catalogtable.getValueAt(catalogtable.getSelectedRow(), 0).toString();
        try {
            String productdata = prodcode.trim();
            return productdata;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Oops, selection did not fetch the code");
            return null;
        }
    }

    //the table filter method
    protected void productsFilter(String str) {
        DefaultTableModel tbmodel = (DefaultTableModel) catalogtable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tbmodel);
        catalogtable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(str));      
    }

    
    //SEARCH THE PRODUCT using either "find buttn" or or clicking on the enter button
    protected void searchProduct() {
        String searched = searcharea.getText();
        DefaultTableModel tbl = (DefaultTableModel) catalogtable.getModel();
        if (cls.getConnection() == true) {
            try {
                String query = "SELECT * FROM products WHERE productCode LIKE ? || productName LIKE ? || productCategory LIKE ?";
                PreparedStatement ps = cls.conn.prepareStatement(query);
                ps.setString(1, "%" + searched.trim() + "%");
                ps.setString(2, "%" + searched.trim() + "%");
                ps.setString(3, "%" + searched.trim() + "%");
                ResultSet rs = ps.executeQuery();
                //check if table has data
                if (catalogtable.getRowCount() > 0) {
                    tbl.setRowCount(0);
                }
                //populate the table now
                while (rs.next()) {     
                    String code = rs.getString("productCode");
                    String name = rs.getString("productName");
                    String category = rs.getString("productCategory");
                    String quantity = rs.getString("productQuantity");
                    String size = rs.getString("productSize");
                    String price = rs.getString("productPrice");
                    String[] tbdata = {code, name, category, quantity, size, price};
                    //populate the table
                    tbl.addRow(tbdata);                    
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Oops, failed to search. something went wrong");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Failed to establish connection to the server");
        }
    }

    
    
    
    //check if search area is empty. if yes, call the populate nmethod
    protected void searchIsEmpty() {
        if (searcharea.getText().isEmpty()) {
            DefaultTableModel tbl = (DefaultTableModel) catalogtable.getModel();
            if (tbl.getRowCount() > 0) {
                tbl.setRowCount(0);
            }
            masterPopulate();
        }
    }

  
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        catalogtable = new javax.swing.JTable();
        searcharea = new javax.swing.JTextField();
        find = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        copycode = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Products catalog");
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(980, 501));
        setResizable(false);

        catalogtable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        catalogtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODE", "PROD NAME", "CATEGORY", "INSTOCK", "SIZE ", "PRICE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        catalogtable.setToolTipText("Click an item to get its code.");
        catalogtable.setRowHeight(35);
        catalogtable.setShowGrid(true);
        catalogtable.setShowVerticalLines(false);
        catalogtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                catalogtableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(catalogtable);
        if (catalogtable.getColumnModel().getColumnCount() > 0) {
            catalogtable.getColumnModel().getColumn(0).setResizable(false);
            catalogtable.getColumnModel().getColumn(0).setPreferredWidth(30);
            catalogtable.getColumnModel().getColumn(1).setResizable(false);
            catalogtable.getColumnModel().getColumn(2).setResizable(false);
            catalogtable.getColumnModel().getColumn(2).setPreferredWidth(60);
            catalogtable.getColumnModel().getColumn(3).setResizable(false);
            catalogtable.getColumnModel().getColumn(3).setPreferredWidth(20);
            catalogtable.getColumnModel().getColumn(4).setResizable(false);
            catalogtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            catalogtable.getColumnModel().getColumn(5).setResizable(false);
            catalogtable.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        searcharea.setBackground(new java.awt.Color(255, 255, 204));
        searcharea.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        searcharea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchareaKeyReleased(evt);
            }
        });

        find.setText("Find");
        find.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findActionPerformed(evt);
            }
        });

        jLabel2.setText("Enter product name/code to filter");

        copycode.setText("Copy selected code");
        copycode.setEnabled(false);
        copycode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copycodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searcharea, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(find)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(copycode, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searcharea, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(find, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(copycode, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void catalogtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_catalogtableMouseClicked
        //First, check which button is clikced
        if(evt.getButton() == 1){
            //if right click, enable the copy selected code button
            copycode.setEnabled(true);
        }
    }//GEN-LAST:event_catalogtableMouseClicked

    private void searchareaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchareaKeyReleased
        //search if enter key is pressed        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchProduct();
        }
        searchIsEmpty();
        productsFilter(searcharea.getText().trim());
    }//GEN-LAST:event_searchareaKeyReleased

    private void findActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findActionPerformed
        // search product
        searchProduct();
    }//GEN-LAST:event_findActionPerformed

    private void copycodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copycodeActionPerformed
        //copy the value
        getproductCode();
    }//GEN-LAST:event_copycodeActionPerformed

    
    
    
    //the main method begins here
    public static void main(String args[]) {
        new ThemeClass().theme();

        
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Catalog all = new Catalog();
                all.setVisible(true);
                new Thread(all).start();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable catalogtable;
    private javax.swing.JButton copycode;
    private javax.swing.JButton find;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searcharea;
    // End of variables declaration//GEN-END:variables
}
