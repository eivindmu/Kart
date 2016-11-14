/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kart;

import com.esri.core.geometry.CoordinateConversion;
import com.esri.map.JMap;
import com.esri.map.MapOverlay;
import java.awt.event.MouseEvent;

/**
 *
 * @author Eivind Fugledal
 */
public class MouseClickedOverlay extends MapOverlay
{
    private JMap map;

    public MouseClickedOverlay(JMap map) 
    {
        this.map = map;
    }
    
    @Override
        public void onMouseClicked(MouseEvent arg0) 
        {
            try 
            {
                System.out.println("Inne i MouseClickedOverlay");
                /*if(map != null)
                {
                    if (!map.isReady()) 
                    {
                        return;
                    }
                }*/
      
                java.awt.Point screenPoint = arg0.getPoint();
                com.esri.core.geometry.Point mapPoint = map.toMapPoint(screenPoint.x, screenPoint.y);
        
                String decimalDegrees = "Decimal Degrees: " + CoordinateConversion.pointToDecimalDegrees(mapPoint, map.getSpatialReference(), 4);
            }  
            finally 
            {
                super.onMouseClicked(arg0);
            }
        }
}
