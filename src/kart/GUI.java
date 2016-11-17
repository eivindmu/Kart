/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kart;

import com.esri.core.geometry.CoordinateConversion;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
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
import java.text.DecimalFormat;
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
    private NAFeaturesAsFeature waypoints = new NAFeaturesAsFeature();
    private DrawingOverlay drawingOverlay;
    private HashMap<String, List<Float>> waypointCoordinates;

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        try {
            waypointCoordinates = new HashMap<>();
            
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
        
        map.addMapOverlay(new NavigatorOverlay());
        
        graphicsLayer = new GraphicsLayer();
        
        LayerList layers = map.getLayers();
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
                    //numberOfWaypoints++;
                    waypoints.addFeature(graphic);
                    graphicsLayer.addGraphic(new Graphic(graphic.getGeometry(), new TextSymbol(10, String.valueOf(numberOfWaypoints), Color.WHITE), 1));
                }
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
                attributes.put("type", "Waypoints");
                drawingOverlay.setUp(DrawingOverlay.DrawingMode.POINT, new SimpleMarkerSymbol(Color.BLUE, 15, SimpleMarkerSymbol.Style.CIRCLE), attributes);
                map.addMapOverlay(new MouseClickedOverlay());
            }
            else
            {
                attributes.clear();
                drawingOverlay.setUp(DrawingOverlay.DrawingMode.NONE, new SimpleMarkerSymbol(Color.BLUE, 15, SimpleMarkerSymbol.Style.CIRCLE), attributes);
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
            }
        });
    }
    
    private class MouseClickedOverlay extends MapOverlay {
    private static final long serialVersionUID = 1L;

    @Override
    public void onMouseClicked(MouseEvent e) {
      try {
        if (!map.isReady()) {
          return;
        }
        
        numberOfWaypoints++;
        
        java.awt.Point screenPoint = e.getPoint();
        com.esri.core.geometry.Point mapPoint = map.toMapPoint(screenPoint.x, screenPoint.y);
        
        String decimalDegrees = CoordinateConversion.pointToDecimalDegrees(mapPoint, map.getSpatialReference(), 4);
          
        MapPointToFloatParser parser = new MapPointToFloatParser(decimalDegrees);
        waypointCoordinates.put("Waypoint " + numberOfWaypoints, parser.parseMapPoint());
        
        System.out.println("Waypoint " + numberOfWaypoints + " "+ waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(0) 
                + "N " + waypointCoordinates.get("Waypoint " + numberOfWaypoints).get(1) + "E");

      } finally {
        super.onMouseMoved(e);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout MapPanelLayout = new javax.swing.GroupLayout(MapPanel);
        MapPanel.setLayout(MapPanelLayout);
        MapPanelLayout.setHorizontalGroup(
            MapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 602, Short.MAX_VALUE)
        );
        MapPanelLayout.setVerticalGroup(
            MapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ControlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Controls", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18))); // NOI18N

        waypointsButton.setText("Set Waypoints");

        resetWaypointsButton.setText("Reset Waypoints");

        javax.swing.GroupLayout ControlPanelLayout = new javax.swing.GroupLayout(ControlPanel);
        ControlPanel.setLayout(ControlPanelLayout);
        ControlPanelLayout.setHorizontalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(waypointsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
            .addComponent(resetWaypointsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ControlPanelLayout.setVerticalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlPanelLayout.createSequentialGroup()
                .addComponent(waypointsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetWaypointsButton)
                .addContainerGap(440, Short.MAX_VALUE))
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
    private javax.swing.JToggleButton waypointsButton;
    // End of variables declaration//GEN-END:variables
}
