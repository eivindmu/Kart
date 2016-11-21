/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kart;

import com.esri.core.geometry.CoordinateConversion;
import com.esri.core.geometry.Polyline;
import com.esri.core.gps.FileGPSWatcher;
import com.esri.core.gps.IGPSWatcher;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.map.GPSLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.LayerList;
import com.esri.map.MapOptions;
import com.esri.map.MapOverlay;
import com.esri.toolkit.overlays.DrawingCompleteEvent;
import com.esri.toolkit.overlays.DrawingCompleteListener;
import com.esri.toolkit.overlays.DrawingOverlay;
import com.esri.toolkit.overlays.NavigatorOverlay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

/**
 *
 * @author Eivind Fugledal
 */
public class GUI extends javax.swing.JFrame {
    
    private JMap map;
    private GraphicsLayer graphicsLayer;
    private int numberOfWaypoints = 0;
    private DrawingOverlay drawingOverlay;
    private HashMap<String, List<Float>> waypointCoordinates;
    private boolean canSetWaypoints = false;
    private MouseClickedOverlay mco;
    private Symbol waypoint;
    private Symbol routeLine;
    private GPSLayer gpsLayer;
    private IGPSWatcher gpsWatcher;
    private LayerList layers;
    private final int northCoordinate = 0;
    private final int eastCoordinate = 1;
    private PrintWriter fileWriter;

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        try {
            waypointCoordinates = new HashMap<>();
            mco = new MouseClickedOverlay();
            waypoint = new SimpleMarkerSymbol(Color.RED, 15, SimpleMarkerSymbol.Style.CIRCLE);
            routeLine = new SimpleLineSymbol(Color.RED, 5, SimpleLineSymbol.Style.SOLID);
            fileWriter = new PrintWriter("route.txt");
            
            this.setStartGPSButtonActionListener();
            this.setWaypointsButtonActionListener();
            this.setResetWaypointsButtonActionListener();
            this.addMapToUI();
            
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private JComponent addMapToUI() throws Exception
    {
        MapPanel.setLayout(new BorderLayout());
        
        this.createMap();
        
        MapPanel.add(map, BorderLayout.CENTER);
        
        return MapPanel;
    }
    
    private void createMap()
    {
        MapOptions mapOptions = new MapOptions(MapOptions.MapType.TOPO, 62.4698, 6.2365, 14);
        map = new JMap(mapOptions);
        
        NavigatorOverlay navigator = new NavigatorOverlay();
        map.addMapOverlay(navigator);
        
        graphicsLayer = new GraphicsLayer();
        
        layers = map.getLayers();
        layers.add(graphicsLayer);
        
        drawingOverlay = new DrawingOverlay();
        map.addMapOverlay(drawingOverlay);
        drawingOverlay.setActive(true);
        drawingOverlay.addDrawingCompleteListener(new DrawingCompleteListener()
        {
            @Override
            public void drawingCompleted(DrawingCompleteEvent arg0) 
            {
                Graphic graphic = (Graphic) drawingOverlay.getAndClearFeature();
                graphicsLayer.addGraphic(graphic);
                
                if (graphic.getAttributeValue("type").equals("Waypoints")) 
                {
                    graphicsLayer.addGraphic(new Graphic(graphic.getGeometry(), new TextSymbol(10, String.valueOf(numberOfWaypoints), Color.WHITE), 1));
                    
                    fileWriter.println("$GPGGA,174655.00," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(northCoordinate) + ",N," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(eastCoordinate) + ",E,8,07,0,0,M,0,M,,*5F");
                    fileWriter.println("$GPVTG,0,T,270,M,000.5,N,000.9,K,A*2F");
                    fileWriter.println("$GPGSV,2,1,07,09,56,267,48,02,56,188,46,10,15,126,,17,30,053,43*7E");
                    fileWriter.println("$GPGSV,2,2,07,28,15,112,32,12,32,314,38,04,66,083,49,,,,*4E");
                    fileWriter.println("$GLGSV,2,1,08,82,44,274,47,65,09,055,,66,08,106,,79,75,243,42*6C");
                    fileWriter.println("$GLGSV,2,2,08,81,53,006,45,80,37,324,42,88,11,046,,78,23,167,*66");
                    fileWriter.println("$GNGSA,M,3,04,09,02,17,,,,,,,,,3.5,1.6,3.1*2A");
                    fileWriter.println("$GNGSA,M,3,79,81,82,,,,,,,,,,3.5,1.6,3.1*2E");
                    fileWriter.println("$GPRMC,174655.00,A," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(northCoordinate) + ",N," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(eastCoordinate) + ",E,0,0,021111,0.0,E,A*2E");
                    fileWriter.println("$GPGGA,174655.00," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(northCoordinate) + ",N," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(eastCoordinate) + ",E,8,07,0,0,M,0,M,,*5F");
                    fileWriter.println("$GPVTG,0,T,270,M,000.5,N,000.9,K,A*2F");
                    fileWriter.println("$GPGSV,2,1,07,09,56,267,48,02,56,188,46,10,15,126,,17,30,053,43*7E");
                    fileWriter.println("$GPGSV,2,2,07,28,15,112,32,12,32,314,38,04,66,083,49,,,,*4E");
                    fileWriter.println("$GLGSV,2,1,08,82,44,274,47,65,09,055,,66,08,106,,79,75,243,42*6C");
                    fileWriter.println("$GLGSV,2,2,08,81,53,006,45,80,37,324,42,88,11,046,,78,23,167,*66");
                    fileWriter.println("$GNGSA,M,3,04,09,02,17,,,,,,,,,3.5,1.6,3.1*2A");
                    fileWriter.println("$GNGSA,M,3,79,81,82,,,,,,,,,,3.5,1.6,3.1*2E");
                    fileWriter.println("$GPRMC,174655.00,A," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(northCoordinate) + ",N," + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(eastCoordinate) + ",E,0,0,021111,0.0,E,A*2E");
                    
                    /*if(numberOfWaypoints > 1)
                    {
                        Polyline polyline = new Polyline();
                        polyline.startPath(waypointCoordinates.get("Waypoint " + (numberOfWaypoints - 1)).get(1), waypointCoordinates.get("Waypoint " + (numberOfWaypoints - 1)).get(0));
                        polyline.lineTo(waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(1), waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(0));
                        
                        System.out.println(waypointCoordinates.get("Waypoint " + (numberOfWaypoints - 1)).get(1) + " " + waypointCoordinates.get("Waypoint " + (numberOfWaypoints - 1)).get(0));
                        System.out.println(waypointCoordinates.get("Waypoint " + (numberOfWaypoints)).get(1) + " " + waypointCoordinates.get("Waypoint " + (numberOfWaypoints)).get(0));
                        
                        System.out.println("Polyline length: " + polyline.calculateLength2D());
                        
                        Graphic lineGraphic = new Graphic(polyline, routeLine, 1);
                        graphicsLayer.addGraphic(lineGraphic);
                        System.out.println(graphicsLayer.getNumberOfGraphics());
                    }*/
                }
            }
        });
    }
    
    private void startGPS()
    {
        gpsWatcher = new FileGPSWatcher("route.txt", 1000, false);
        gpsLayer = new GPSLayer(gpsWatcher);
        gpsLayer.setMode(GPSLayer.Mode.OFF);
        gpsLayer.setNavigationPointHeightFactor(0.3);
        gpsLayer.setTrackPointSymbol(new SimpleMarkerSymbol(new Color(200, 0, 0, 200), 10, SimpleMarkerSymbol.Style.CIRCLE));
                
        layers.add(gpsLayer);
    }
    
    private void setStartGPSButtonActionListener()
    {
        startGPSButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e)
            {
                startGPS();
            }
        });
    }
    
    private void setWaypointsButtonActionListener()
    {
        waypointsButton.addActionListener(new ActionListener() {

         public void actionPerformed(ActionEvent e) 
         {
            JToggleButton tBtn = (JToggleButton) e.getSource();
            HashMap<String, Object> attributes = new HashMap<>();
            
            
            if (tBtn.isSelected()) 
            {
                canSetWaypoints = true;
                attributes.put("type", "Waypoints");
                drawingOverlay.setUp(DrawingOverlay.DrawingMode.POINT, waypoint, attributes);
                map.addMapOverlay(mco);
            }
            else
            {
                canSetWaypoints = false;
                attributes.clear();
                drawingOverlay.setUp(DrawingOverlay.DrawingMode.NONE, waypoint, attributes);
                map.removeMapOverlay(mco);
                fileWriter.close();
            }
         }
      });
    }
    
    private void setResetWaypointsButtonActionListener()
    {
        resetWaypointsButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e)
            {
                numberOfWaypoints = 0;
                graphicsLayer.removeAll();
                waypointCoordinates.clear();
                //gpsLayer.removeAll();
                gpsLayer.setShowTrail(false);
            }
        });
    }
    
    private class MouseClickedOverlay extends MapOverlay {

    @Override
    public void onMouseClicked(MouseEvent e) {
      try {
        if (!map.isReady()) {
          return;
        }
        
        else if(canSetWaypoints)
        {
            numberOfWaypoints++;
        
            java.awt.Point screenPoint = e.getPoint();
            com.esri.core.geometry.Point mapPoint = map.toMapPoint(screenPoint.x, screenPoint.y);
        
            String degreesDecimalMinutes = CoordinateConversion.pointToDegreesDecimalMinutes(mapPoint, map.getSpatialReference(), 4);
          
            MapPointToFloatParser parser = new MapPointToFloatParser(degreesDecimalMinutes);
            waypointCoordinates.put("Waypoint " + numberOfWaypoints, parser.parseMapPoint());
        
            System.out.println("Waypoint " + numberOfWaypoints + " "+ waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(northCoordinate) 
                + "N " + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(eastCoordinate) + "E");
        }

      } finally {
        super.onMouseClicked(e);
      }
    }
  }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MapPanel = new javax.swing.JPanel();
        ControlPanel = new javax.swing.JPanel();
        waypointsButton = new javax.swing.JToggleButton();
        resetWaypointsButton = new javax.swing.JButton();
        startGPSButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout MapPanelLayout = new javax.swing.GroupLayout(MapPanel);
        MapPanel.setLayout(MapPanelLayout);
        MapPanelLayout.setHorizontalGroup(
            MapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        MapPanelLayout.setVerticalGroup(
            MapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ControlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Controls", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18))); // NOI18N

        waypointsButton.setText("Set Waypoints");

        resetWaypointsButton.setText("Reset Waypoints");

        startGPSButton.setText("Start GPS");

        javax.swing.GroupLayout ControlPanelLayout = new javax.swing.GroupLayout(ControlPanel);
        ControlPanel.setLayout(ControlPanelLayout);
        ControlPanelLayout.setHorizontalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startGPSButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(waypointsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(resetWaypointsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        ControlPanelLayout.setVerticalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startGPSButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waypointsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetWaypointsButton)
                .addContainerGap(396, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JPanel MapPanel;
    private javax.swing.JButton resetWaypointsButton;
    private javax.swing.JButton startGPSButton;
    private javax.swing.JToggleButton waypointsButton;
    // End of variables declaration//GEN-END:variables
}
