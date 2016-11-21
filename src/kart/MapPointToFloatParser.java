/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kart;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eivind Fugledal
 */
public class MapPointToFloatParser {
    
    private String degreesDecimalMinutes;
    private List<Float> coordinates;
    
    public MapPointToFloatParser(String degreesDecimalMinutes)
    {
        this.degreesDecimalMinutes = degreesDecimalMinutes;
        this.coordinates = new ArrayList<>();
    }
    
    public List<Float> parseMapPoint()
    {
        String northSouthCoordinate = degreesDecimalMinutes.substring(0, 10);
        String westEastCoordinate = degreesDecimalMinutes.substring(12, 23);
        
        String northSouth = degreesDecimalMinutes.substring(10, 11);
        String westEast = degreesDecimalMinutes.substring(23, 24);
        
        String north = northSouthCoordinate.substring(0, 2) + northSouthCoordinate.substring(3, 10);
        String east = westEastCoordinate.substring(0, 3) + westEastCoordinate.substring(4, 11);
          
        float ns = Float.parseFloat(north);
        float we = Float.parseFloat(east);
          
        if(northSouth.toLowerCase().equals("s"))
            ns = -ns;
        if(westEast.toLowerCase().equals("w"))
            we = -we;
        
        coordinates.add(ns);
        coordinates.add(we);
        
        return coordinates;
    }
    
}
