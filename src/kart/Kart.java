/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kart;

import com.esri.map.JMap;
import com.esri.map.MapOptions;
import com.esri.map.MapOptions.MapType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Eivind Fugledal
 */
public class Kart {
    
    private JMap map;
    
    public Kart()
    {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          // instance of this application
          Kart mapExtentApp = new Kart();
          // create the UI, including the map, for the application.
          JFrame appWindow = mapExtentApp.createWindow();
          appWindow.add(mapExtentApp.createUI());
          appWindow.setVisible(true);
        } catch (Exception e) {
          // on any error, display the stack trace.
          e.printStackTrace();
        }
      }
    });
    }
    
    public JComponent createUI() throws Exception
    {
        JPanel contentPane = new JPanel(new BorderLayout());
        
        // map options: topographic map, centered at lat-lon 62.4698, 6.2365 (Nørvasundet), zoom level 12
        // sentreringen bør byttes til posisjon fra GPS
        MapOptions mapOptions = new MapOptions(MapType.TOPO, 62.4698, 6.2365, 13);
        
        // create the map using MapOptions
        map = new JMap(mapOptions);
        contentPane.add(map);
        
        return contentPane;
    }
    
    private JFrame createWindow()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        
        JFrame window = new JFrame("Kart");
        window.setSize((int) width, (int) height);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setLayout(new BorderLayout());
        window.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            super.windowClosing(windowEvent);
            map.dispose();
        }
        });
        return window;
    }
    
}
