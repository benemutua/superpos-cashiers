package com.benesoft.cashierssystem;
//imports

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dev
 */
public class MyTransactions extends javax.swing.JFrame {

    //globals
    ConnectionClass cls = new ConnectionClass();
    CashierIndex csx = new CashierIndex();

    /**
     * Creates new form MyTransactions
     */
    public MyTransactions() {
        initComponents();
        mysalePanel.setVisible(false);
    }

    //Get logged in username
    protected String userName() {
        return csx.userLog.getText();
    }

    //get category being fetched
    protected String getSelected() {
        return (String) selectValue.getSelectedItem();
    }

    //RETURN THE TRANSACTIONS
    protected void getTrans() {
        String yr = (String)year.getSelectedItem();
        String mon = (String)month.getSelectedItem();
        String comb = yr+"-"+mon;
        try {
            
            String deletedQuery = "SELECT * FROM sales AS sl "
                    + "LEFT JOIN products AS pd "
                    + "ON sl.productCode=pd.productCode "
                    + "WHERE sl.soldBy=? && sl.status=? && sl.saleDate LIKE ?";
            PreparedStatement pst = cls.conn.prepareStatement(deletedQuery);
            pst.setString(1, userName());
            pst.setString(2, getSelected());
            pst.setString(3, "%"+comb+"%");
            ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String transcode = rs.getString("sl.transactionCode");
                    String prodcode = rs.getString("sl.productCode");
                    String prodprice = rs.getString("sl.productPrice");
                    String soldAt = rs.getString("sl.saleDate");
                    String prodname = rs.getString("pd.productName");
                    String[] data = {transcode, prodcode,prodname, prodprice, soldAt};
                    //populate the table
                    DefaultTableModel tbl = (DefaultTableModel) mysaleTable.getModel();
                    tbl.addRow(data);
                }            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Server error occured. Could not retrieve data");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Oops, something went wrong. Could not retrieve data");
        }
    }

    //GET THE VALUE OF THE PRICES AND SUM THEM UP
    protected int sumPrices(){
       DefaultTableModel tb = (DefaultTableModel) mysaleTable.getModel();
       //get all rows
       int totalRows = mysaleTable.getRowCount();
       int sum = 0;
       
       //get the price at first row
       for(int i=0; i < totalRows; i++){
           String price = tb.getValueAt(i, 3).toString();
           sum += Integer.parseInt(price);
       }
       return sum;
    }
    
    //get the returned sum to know if reciords are found
    protected void dataReturned(){
        if(sumPrices() == 0){
            JOptionPane.showMessageDialog(null, "Oops, seems like there are no matching records");
        }
    }
    
    
    
    
    //master method
    protected void masterMethod() {
        if (cls.getConnection() == true) {
            mysalePanel.setVisible(true);
            getTrans();
        } else {
            JOptionPane.showMessageDialog(null, "Oops, something went wrong. Could not retrieve data");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        worth = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        selectValue = new javax.swing.JComboBox<>();
        fetch = new javax.swing.JButton();
        mysalePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mysaleTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        month = new javax.swing.JComboBox<>();
        year = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        worth.setEditable(false);
        worth.setBackground(new java.awt.Color(255, 255, 204));
        worth.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        worth.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        worth.setText("0.00");
        worth.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setText("Worth (KES):");

        selectValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Completed", "Reversed" }));

        fetch.setText("Fetch");
        fetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fetchActionPerformed(evt);
            }
        });

        mysaleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "T NUMBER:", "ITEM CODE:", "ITEM NAME:", "ITEM PRICE:", "TIME:"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mysaleTable.setRowHeight(35);
        mysaleTable.setShowGrid(true);
        jScrollPane1.setViewportView(mysaleTable);

        javax.swing.GroupLayout mysalePanelLayout = new javax.swing.GroupLayout(mysalePanel);
        mysalePanel.setLayout(mysalePanelLayout);
        mysalePanelLayout.setHorizontalGroup(
            mysalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
        );
        mysalePanelLayout.setVerticalGroup(
            mysalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
        );

        jLabel2.setText("Category:");

        jLabel3.setText("Month:");

        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        year.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2022", "2021" }));

        jLabel4.setText("Year:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mysalePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(year, 0, 114, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(month, 0, 99, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectValue, 0, 191, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fetch)
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(worth, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(worth, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(selectValue, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fetch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mysalePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fetchActionPerformed
        // populate table but check if it has data. if yes, clear it
        DefaultTableModel tb = (DefaultTableModel) mysaleTable.getModel();
        if(tb.getRowCount() > 0){
            tb.setRowCount(0);
        }
        
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                masterMethod();
                worth.setText(String.valueOf(sumPrices()));
                dataReturned();
            }
        });
    }//GEN-LAST:event_fetchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ThemeClass().theme();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyTransactions().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton fetch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> month;
    private javax.swing.JPanel mysalePanel;
    private javax.swing.JTable mysaleTable;
    private javax.swing.JComboBox<String> selectValue;
    private javax.swing.JTextField worth;
    private javax.swing.JComboBox<String> year;
    // End of variables declaration//GEN-END:variables
}
