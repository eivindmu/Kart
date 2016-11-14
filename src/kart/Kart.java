/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kart;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import com.esri.core.gps.FileGPSWatcher;
import com.esri.core.gps.IGPSWatcher;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GPSLayer;
import com.esri.map.GPSLayer.Mode;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.LayerList;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;
import com.esri.map.MapOptions;
import com.esri.map.MapOptions.MapType;
import com.esri.runtime.ArcGISRuntime;
import com.esri.toolkit.overlays.DrawingCompleteEvent;
import com.esri.toolkit.overlays.DrawingCompleteListener;
import com.esri.toolkit.overlays.DrawingOverlay;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author Eivind Fugledal
 */
public class Kart {

    private JMap map;
    private GraphicsLayer graphicsLayer;
    private DrawingOverlay myDrawingOverlay;
    private int numStops = 0;
    private NAFeaturesAsFeature stops = new NAFeaturesAsFeature();
    
    public Kart()
    {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
        
      /*SwingUtilities.invokeLater(new Runnable() {
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
    });*/
    }
    
    public JComponent createUI() throws Exception
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 5));
        
        // create the map using MapOptions
        map = this.createMap();
        
        contentPanel.add(map, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JMap createMap()
    {
        JMap jMap = new JMap();
        // Sentrerer kartet i 62.4698, 6.2365 (Nørvasundet)
        // Sentreringen bør byttes til avlest posisjon fra GPS
        jMap.setExtent(new Envelope(new Point(6.2365, 62.4698), 0.025, 0.025));
        
        // add base layer
        ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/ESRI_StreetMap_World_2D/MapServer");
        // Waypoints layer
        graphicsLayer = new GraphicsLayer();
        
        LayerList layers = jMap.getLayers();
        layers.add(tiledLayer);
        layers.add(graphicsLayer);

        return jMap;
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
