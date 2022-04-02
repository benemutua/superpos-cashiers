package com.benesoft.cashierssystem;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;



public class ThemeClass {

void theme(){
    try {
        UIManager.setLookAndFeel( new FlatLightLaf() );
    } catch(Exception ex ) {
        System.err.println( "Failed to initialize LaF" );
    }        
}
}
