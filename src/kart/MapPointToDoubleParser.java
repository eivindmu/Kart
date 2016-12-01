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
public class MapPointToDoubleParser {
    
    private String decimalDegrees;
    private List<Double> coordinates;
    
    public MapPointToDoubleParser(String decimalDegrees)
    {
        this.decimalDegrees = decimalDegrees;
        this.coordinates = new ArrayList<>();
    }
    
    /**
     * Parses a map point to decimal degrees
     * @return A list containing two doubles of decimal degrees
     */
    public List<Double> parseMapPoint()
    {
        String northSouthCoordinate = decimalDegrees.substring(0, 7);
        String westEastCoordinate = decimalDegrees.substring(10, 17);
        
        String northSouth = decimalDegrees.substring(7, 8);
        String westEast = decimalDegrees.substring(17, 18);
          
        double ns = Double.parseDouble(northSouthCoordinate);
        double we = Double.parseDouble(westEastCoordinate);
          
        if(northSouth.toLowerCase().equals("s"))
            ns = -ns;
        if(westEast.toLowerCase().equals("w"))
            we = -we;
        
        coordinates.add(ns);
        coordinates.add(we);
        
        return coordinates;
    }
    
}
