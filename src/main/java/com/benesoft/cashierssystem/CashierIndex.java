package com.benesoft.cashierssystem;
//imports
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*
import java.io.File;  // Import the File class
import java.io.IOException;
*/


/**
 *
 * @author dev
 */

public class CashierIndex extends javax.swing.JFrame {    
    int errBtn = JOptionPane.ERROR_MESSAGE;
    int warnBtn = JOptionPane.WARNING_MESSAGE;
    int okBtn = JOptionPane.OK_OPTION;
    int infoBtn = JOptionPane.INFORMATION_MESSAGE;
    ConnectionClass cls = new ConnectionClass();
    int saleCode;
    
    
    /*===============================================================
    the documentation
    ===============================================================*/
    /*
    //methods
    see if search area is empty
    see if entry is a number
    retrieve products data
    populate sales table
    calculate subtotals
    calculate totals
    insert the sale
    check if product exists
    */
    
    
    //the constructor
    public CashierIndex() {
        initComponents();
        populateTransactionCode();
        searchedItem.requestFocus();
        this.setExtendedState(this.MAXIMIZED_BOTH);
    }
    
    /*===================================================
    MENU ITEMS ACTIONS
    ===================================================*/
    //ABOUT
    public void aboutSoftware(){
        String desc = "VERSION: 1.0\nUSERS: Medium and Small scale supermarkets\n"
                + "DEVELOPER: benesoft developers ke\nWEBSITE: www.benesoftke.co.ke";
        JOptionPane.showMessageDialog(null, desc, "About Software", infoBtn);
    }
    
    
    //USER DATA JOPTION
    public void getUserData(){
        String username = userLog.getText();
        //String email = getUserEmail();
        String email = "benemutua@gmail.com";
        String fullname = "Benedict Mutua";
        //String fullName = getUserFullName();
        String phone = "07******95";
        //String phone = getUserPhone();
        String role = "Cashier";
        //String role = getUserRole();
        String userDesc = "USER NAMES: "+fullname +"\nLOGIN NAME: "+username+"\n\nEMAIL ADDRESS: "+email+"\nMOBILE NO: "+phone;
        JOptionPane.showMessageDialog(null, userDesc, "User Information", infoBtn);
    }
    
    //OPEN NEW TRANSACTION
    public void startTransaction(){
        String value = subTotal.getText();
        if(value.equals("0.00") || value.equals("0.0")){
            this.hide();
            new CashierIndex().setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Seems like you have an uncompleted transaction. #access denied", "Denied", warnBtn);
        }
    }
    
    //LOGOUT
        public void logout(){
        String value = subTotal.getText();
        if(value.equals("0.00") || value.equals("0.0")){
            System.exit(0);
        }else{
            JOptionPane.showMessageDialog(null, "Got unfinished business. not possible to logout at this moment", "Denied", warnBtn);
        }
    }
    
    
    /*--------finished the menu items methods-------------*/
    
  
        
    
    //populate the transaction code
    protected void populateTransactionCode(){
        Double tCode = Math.random() * 1000000;
        int code = (int)Math.round(tCode);
        String userData = userLog.getText().toUpperCase();
        char first = userData.charAt(0);
        char third = userData.charAt(2);
        String usr = String.valueOf(first) + String.valueOf(third);
        transactionCode.setText(usr + String.valueOf(code));
    }
    
    //THE SALE CODE
    protected int getSaleCode(){
        // It will generate 6 digit random Number between 0 -999999.
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return Integer.parseInt(String.format("%06d", number));
    }
    
    
    //FIND IF SEARCH IS EMPTY
    protected String searchIsEmpty(){
        if(searchedItem.getText().isEmpty()){
            return "Please type in something on the search are to proceed";
        }else{
            return null;
        }
    }
    
    
    //CHECK IF SEARCH INPUT IS A NUMBER
    protected String searchIsaNumber(){
        String searched = searchedItem.getText();
        try{
            int number = Integer.parseInt(searched);
            return null;            
        }catch(NumberFormatException ex){
            return "Please search product using product code";
        }
    }
    
    
    //METHOD TO CHECK IF PRODUCT EXISTS
    protected String productExists(){        
        String searched = searchedItem.getText();
            try{
                String existsQuery = "SELECT productCode FROM products WHERE productCode = ?";
                PreparedStatement exists = cls.conn.prepareStatement(existsQuery);
                exists.setString(1, searched);
                ResultSet rs = exists.executeQuery();
                if(rs.next()){
                    return null;
                }else{
                    return "Failed to match that product on database. If this is not the case, try the \"advanced search\" option.";
                }                
            }catch(Exception e){
                return "There was an error in searching product existence";
            }   
    }
    
    
    
    //METHOD TO RETURN ALL PRODUCT DATA FOR SPECIFIED PRODUCT CODE
    protected String[] getProductData(){
        String searched = searchedItem.getText();
            try{
                String productQuery = "SELECT * FROM products WHERE productCode = ?";
                PreparedStatement getProduct = cls.conn.prepareStatement(productQuery);
                getProduct.setInt(1, Integer.parseInt(searched));
                ResultSet rst = getProduct.executeQuery();
                        if(rst.next()){
                            String prodid = rst.getString("productId");
                            String prodcode = rst.getString("productCode");
                            String prodname = rst.getString("productName");
                            String prodcategory = rst.getString("productCategory");
                            String prodquantity = rst.getString("productQuantity");
                            String prodprice = rst.getString("productPrice");
                            String prodsize = rst.getString("productSize");
                            String prodImageName = rst.getString("image");
                            String[] productsData = {prodid, prodcode, prodname, prodcategory, prodquantity, prodprice, prodsize, prodImageName};                    
                            return productsData;                    
                        }else{
                            return null;
                        }  
            }catch(SQLException ex){
                return null;
            }
            catch(Exception e){
                return null;
            }            
    }
    
    
    //METHOD TO POPULATE SALES TABLE
    protected void populateSalesTable(){
        DefaultTableModel tbl = (DefaultTableModel)salesTable.getModel();
        //populateTextboxes();
        String[] tableData = {getProductData()[1], String.valueOf(saleCode),getProductData()[2], "x1", "Kes "+getProductData()[5]};
        //add row
        tbl.addRow(tableData);
    }

    
    //POPULATE THE TEXTFIELDS JUST BELOW THE SEARCHED ITEM BOX
    protected void populateTextboxes(){
        pcode.setText(getProductData()[1]);
        pname.setText(getProductData()[2]);
        pcategory.setSelectedItem(getProductData()[3]);
        psize.setText(getProductData()[6]);
    }

    protected void getImageName(){
        String image = getProductData()[7];
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(306, 300, Image.SCALE_DEFAULT));
         imageFileView.setIcon(imageIcon);//now set the icon
    }
    
    
    //METHOD TO GET THE LAST ROW OF SALES TABLE AND REMOVING IT
    protected void removeLastRow(){
        //get the total length of all rows
        DefaultTableModel tbl = (DefaultTableModel)salesTable.getModel();
        int allRows = tbl.getRowCount();        
        //the last row
        int lastRow = allRows - 1;        
        //now remove the last row
        tbl.removeRow(lastRow);
    }
    
    
    
    //METHOD TO ADD SUBTOTALS
    protected void calculateSubTotals(){
        String selectedItemPrice = getProductData()[5];
        String currentSubtotal = subTotal.getText();        
        double subtotals = Double.parseDouble(currentSubtotal) + Double.parseDouble(selectedItemPrice);
        subTotal.setText(String.valueOf(subtotals));
    }
    
    //METHOD TO CALCULATE NET TOTALS
    protected void calculateNetTotals(){
        String discounts = discountLabel.getText();
        String subtotals = subTotal.getText();
        double netTotal = Double.parseDouble(subtotals) - Double.parseDouble(discounts);
        totals.setText(String.valueOf(netTotal));
    }    
    

    //METHOD TO ADD SALE TO DATABASE SALES TABLE AND IF IT FAILS REMOVE LAST ROW OF SALESTABLE 
    protected void launchSale(){
        String tCode = transactionCode.getText(); String userName = userLog.getText();
            //insert sale to db
            try{
                String insertQuery = "INSERT INTO sales(transactionCode, productCode, productPrice, soldBy, saleCode) VALUES(?,?,?,?,?)";
                PreparedStatement insert = cls.conn.prepareStatement(insertQuery);
                insert.setString(1, tCode);
                insert.setInt(2, Integer.parseInt(getProductData()[1]));
                insert.setInt(3, Integer.parseInt(getProductData()[5]));
                insert.setString(4, userName);
                insert.setInt(5, saleCode);                
                //insert now using transactions
                cls.conn.setAutoCommit(false);
                    try{
                        insert.executeUpdate();
                        //update stock of the specific product
                        String updateQuery = "UPDATE products SET productQuantity = (productQuantity - 1) WHERE productCode = ?";
                        PreparedStatement pst = cls.conn.prepareStatement(updateQuery);
                        pst.setInt(1, Integer.parseInt(searchedItem.getText()));                        
                        pst.executeUpdate();  
                        
                        //THE ITEMS BILLING PART
                        calculateSubTotals();
                        calculateNetTotals();
                        populateTextboxes();
                        cls.conn.commit();//commit the transaction
                        
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null,"Failed to create sale. Dont worry, transaction has been rolled back. Please try again"+ex,"Transaction Error" ,errBtn);
                        removeLastRow();
                        cls.conn.rollback();
                        
                    }//end the inner try catch                
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null, "Server command to insert sale failed to execute, please try again", "Sale Failed", errBtn);
                removeLastRow();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Oops, an undefined error occured while saving the sale, please contact support", "Undefined Error", errBtn);
                removeLastRow();
            }            
    }
    

    
    
    //THE MASTER METHOD TO CALL ALL OTHER METHODS
    protected void makeSale(){
        if(searchIsEmpty() != null){//if the searched item has no value, error out
            JOptionPane.showMessageDialog(null, searchIsEmpty(), "Empty Search", errBtn);
            searchedItem.requestFocus();
            
        }else if(searchIsaNumber() != null){//if not a number
            JOptionPane.showMessageDialog(null, searchIsaNumber(), "Not a Number", errBtn);
            
        }
        else if(cls.getConnection() == true){
            if(productExists() != null){
                JOptionPane.showMessageDialog(null, productExists());
            }else{
                populateSalesTable();
                launchSale();
                getImageName();
                searchedItem.setText("");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Failed to establish connection to the server");
        }
    }
    
    
    
  
    //METHOD TO REMOVE SALE
    protected void removeSaleData(){        
        DefaultTableModel tbl = (DefaultTableModel)salesTable.getModel();
        int thisRow = salesTable.getSelectedRow();
        String productCode = (String.valueOf(salesTable.getValueAt(thisRow, 0))).trim();
        int pCode = Integer.parseInt(productCode);
        String transCode = transactionCode.getText();
        String saleNo = (String.valueOf(salesTable.getValueAt(thisRow, 1))).trim();
        int sCode = Integer.parseInt(saleNo);
        String price = String.valueOf(salesTable.getValueAt(thisRow, 4)).trim();
        int totalLength = (String.valueOf(salesTable.getValueAt(thisRow, 4))).length();
        String pprice = price.substring(3, totalLength);    
                if(cls.getConnection() == true){
                    //delete the sale
                    try{
                        String saleQuery = "UPDATE sales SET status = ? WHERE productCode = ? && transactionCode = ? && saleCode = ?";
                        PreparedStatement sale = cls.conn.prepareStatement(saleQuery);
                        sale.setString(1, "reversed");
                        sale.setInt(2, pCode);
                        sale.setString(3, transCode);
                        sale.setInt(4, sCode);
                            
                            cls.conn.setAutoCommit(false);
                            try{
                                //delete the sale
                                sale.executeUpdate();
                                
                                //Re-add stock by 1
                                String updateStock = "UPDATE products SET productQuantity = (productQuantity + 1) WHERE productCode = ?";
                                PreparedStatement pst = cls.conn.prepareStatement(updateStock);
                                pst.setInt(1, pCode);
                                pst.executeUpdate();
                                
                                //remove the row
                                tbl.removeRow(thisRow);
                                
                                //deduct subtotals
                                double newSubTotals = Double.parseDouble(subTotal.getText()) - Double.parseDouble(pprice);
                                subTotal.setText(String.valueOf(newSubTotals));
                                calculateNetTotals();                            
                                cls.conn.commit();
                                
                                JOptionPane.showMessageDialog(null, "The selected sale has been removed succesfully", "Removed", infoBtn);
                                
                            }catch(SQLException ex){
                                JOptionPane.showMessageDialog(null, "Oops, something went wrong in removing the sale, try again","Remove Error", errBtn);
                                cls.conn.rollback();                                
                            }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null, "Undefined error in removing the sale","Undefined Error", errBtn);
                        
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Failed to establish connection to the server", "Connection Error", errBtn);
                }
    }        
    
    
    
    
    /*===========================================================
    COMPLETE TRANSACTION ACTIONS
    ===========================================================*/
    protected void openPayments(){
          //check if jtable has data
        if(salesTable.getRowCount() == 0){
            JOptionPane.showMessageDialog(null, "We cant open payments since no transaction has happened");
        }else{
             //open payments
        PaymentsPanel ppl = new PaymentsPanel();
        ppl.setVisible(true);
        ppl.payable.setText(totals.getText());
        } 
    }
    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        searchedItem = new javax.swing.JTextField();
        searchProduct = new javax.swing.JButton();
        advancedSearch = new javax.swing.JButton();
        pcode = new javax.swing.JTextField();
        pname = new javax.swing.JTextField();
        psize = new javax.swing.JTextField();
        pcategory = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        userLog = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        transactionCode = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        subTotal = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        discountLabel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        totals = new javax.swing.JTextField();
        completeTransaction = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        imageFileView = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        main = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        help = new javax.swing.JMenuItem();
        openTrans = new javax.swing.JMenuItem();
        mytrans = new javax.swing.JMenu();
        userdata = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        logout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cashiers pos");

        jPanel1.setBackground(java.awt.Color.black);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 25)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("BM SUPERMARKETS KENYA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ITEMCODE:", "SKU", "ITEM NAME:", "QUANTITY:", "ITEM PRICE:"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        salesTable.setToolTipText("Tap on the product you want to remove and menu will appear");
        salesTable.setRowHeight(35);
        salesTable.setShowGrid(true);
        salesTable.setShowVerticalLines(false);
        salesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(salesTable);

        searchedItem.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        searchedItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchedItemKeyReleased(evt);
            }
        });

        searchProduct.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        searchProduct.setForeground(new java.awt.Color(102, 153, 255));
        searchProduct.setText("SEARCH");
        searchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchProductActionPerformed(evt);
            }
        });

        advancedSearch.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        advancedSearch.setForeground(new java.awt.Color(102, 153, 255));
        advancedSearch.setText("ADVANCED");
        advancedSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedSearchActionPerformed(evt);
            }
        });

        pcode.setEditable(false);
        pcode.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
        pcode.setForeground(new java.awt.Color(0, 153, 204));
        pcode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pcode.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        pname.setEditable(false);
        pname.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
        pname.setForeground(new java.awt.Color(0, 153, 204));
        pname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pname.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        psize.setEditable(false);
        psize.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
        psize.setForeground(new java.awt.Color(0, 153, 204));
        psize.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        psize.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        pcategory.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
        pcategory.setForeground(new java.awt.Color(0, 153, 204));
        pcategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-select-", "Electronics", "Beverages", "Drinks", "Alcoholics", "Snacks", "Home Consumables", "Mechanical" }));
        pcategory.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(searchedItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(advancedSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pcode, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pname, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(psize, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchedItem, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(advancedSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pcode, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pname, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(psize, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
        );

        jPanel5.setBackground(java.awt.Color.black);

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 204));
        jLabel4.setText("STAFF:");

        userLog.setForeground(java.awt.Color.white);
        userLog.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userLog.setText("*");

        jLabel9.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 153, 204));
        jLabel9.setText("TRANSACTION  NO:");

        transactionCode.setForeground(java.awt.Color.white);
        transactionCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        transactionCode.setText("*");

        jLabel7.setForeground(java.awt.Color.white);
        jLabel7.setText("SUB TOTALS:");

        subTotal.setEditable(false);
        subTotal.setFont(new java.awt.Font("Ubuntu", 1, 28)); // NOI18N
        subTotal.setForeground(new java.awt.Color(0, 153, 255));
        subTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        subTotal.setText("0.00");
        subTotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel10.setForeground(java.awt.Color.white);
        jLabel10.setText("DISCOUNTS");

        discountLabel.setEditable(false);
        discountLabel.setFont(new java.awt.Font("Ubuntu", 1, 28)); // NOI18N
        discountLabel.setForeground(new java.awt.Color(0, 153, 255));
        discountLabel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        discountLabel.setText("0.00");
        discountLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel11.setForeground(java.awt.Color.white);
        jLabel11.setText("TO PAY:");

        totals.setEditable(false);
        totals.setFont(new java.awt.Font("Ubuntu", 1, 28)); // NOI18N
        totals.setForeground(new java.awt.Color(0, 153, 255));
        totals.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        totals.setText("0.00");
        totals.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        completeTransaction.setFont(new java.awt.Font("Ubuntu", 1, 28)); // NOI18N
        completeTransaction.setText("COMPLETE");
        completeTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                completeTransactionMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transactionCode, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totals, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(completeTransaction)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userLog, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transactionCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totals, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(completeTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ITEM IMAGE");

        imageFileView.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageFileView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(imageFileView, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imageFileView, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        main.setText("System");
        main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainActionPerformed(evt);
            }
        });

        jMenuItem8.setText("Help?");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        main.add(jMenuItem8);

        help.setText("About");
        help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        main.add(help);

        openTrans.setText("New transaction");
        openTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openTransActionPerformed(evt);
            }
        });
        main.add(openTrans);

        jMenuBar1.add(main);

        mytrans.setText("My Profile");
        mytrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mytransActionPerformed(evt);
            }
        });

        userdata.setText(" User Data");
        userdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userdataActionPerformed(evt);
            }
        });
        mytrans.add(userdata);

        jMenuItem7.setText("My transactions");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        mytrans.add(jMenuItem7);

        logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        logout.setText("Logout?");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        mytrans.add(logout);

        jMenuBar1.add(mytrans);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductActionPerformed
        //make the sale
        saleCode = getSaleCode();
        makeSale();
    }//GEN-LAST:event_searchProductActionPerformed

    private void searchedItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchedItemKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                saleCode = getSaleCode();
                makeSale();
        }
    }//GEN-LAST:event_searchedItemKeyReleased
  
    private void advancedSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedSearchActionPerformed
        //call the products advabnced panel
        Catalog ctg = new Catalog();
        ctg.setVisible(true);
        ctg.run();
    }//GEN-LAST:event_advancedSearchActionPerformed
     
    private void salesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesTableMouseClicked
        if(evt.getButton() == 1){
            String user = userLog.getText();
            String ok = JOptionPane.showInputDialog("Please type your 'username/remove' to allow remove ");
            if(ok.equals(user+"/remove")){
                //remove sale from db and sales table
                removeSaleData();
            }else{
                JOptionPane.showMessageDialog(null, "Please follow instructions to have the sale removed. Thank you");
            }
        }
    }//GEN-LAST:event_salesTableMouseClicked

    private void helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpActionPerformed
        // about software
        aboutSoftware();
    }//GEN-LAST:event_helpActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        //open the help frame
        new HelpPanel().setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void mainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainActionPerformed

    }//GEN-LAST:event_mainActionPerformed

    private void openTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openTransActionPerformed
        //close system
        startTransaction();
    }//GEN-LAST:event_openTransActionPerformed

    private void userdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userdataActionPerformed
        // display user data
        getUserData();
    }//GEN-LAST:event_userdataActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        //EXIT THE SYSTEM
        logout();
    }//GEN-LAST:event_logoutActionPerformed

    private void mytransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mytransActionPerformed
        // open my transactions window        
    }//GEN-LAST:event_mytransActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        new MyTransactions().setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void completeTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_completeTransactionMouseClicked
      openPayments();      
    }//GEN-LAST:event_completeTransactionMouseClicked

  
    
   
    public static void main(String args[]) {
        //the leaflaf theme
        new ThemeClass().theme();
        
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CashierIndex().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton advancedSearch;
    private javax.swing.JButton completeTransaction;
    private javax.swing.JTextField discountLabel;
    private javax.swing.JMenuItem help;
    private javax.swing.JLabel imageFileView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem logout;
    private javax.swing.JMenu main;
    private javax.swing.JMenu mytrans;
    private javax.swing.JMenuItem openTrans;
    private javax.swing.JComboBox<String> pcategory;
    public javax.swing.JTextField pcode;
    private javax.swing.JTextField pname;
    private javax.swing.JTextField psize;
    protected javax.swing.JTable salesTable;
    private javax.swing.JButton searchProduct;
    public javax.swing.JTextField searchedItem;
    protected javax.swing.JTextField subTotal;
    private javax.swing.JTextField totals;
    private javax.swing.JLabel transactionCode;
    public javax.swing.JLabel userLog;
    private javax.swing.JMenuItem userdata;
    // End of variables declaration//GEN-END:variables
}
