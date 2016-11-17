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
    
    private String decimalDegrees;
    private List<Float> coordinates;
    
    public MapPointToFloatParser(String decimalDegrees)
    {
        this.decimalDegrees = decimalDegrees;
        this.coordinates = new ArrayList<>();
    }
    
    public List<Float> parseMapPoint()
    {
        String northSouthCoordinate = decimalDegrees.substring(0, 7);
        String westEastCoordinate = decimalDegrees.substring(9, 17);
        String northSouth = decimalDegrees.substring(7, 8);
        String westEast = decimalDegrees.substring(17, 18);
          
        float ns = Float.parseFloat(northSouthCoordinate);
        float we = Float.parseFloat(westEastCoordinate);
          
        if(northSouth.toLowerCase().equals("s"))
            ns = -ns;
        if(westEast.toLowerCase().equals("w"))
            we = -we;
        
        coordinates.add(ns);
        coordinates.add(we);
          
        /*System.out.println(decimalDegrees);
          
        System.out.println(northSouthCoordinate + " " + westEastCoordinate);
        System.out.println(ns + " " + we);
        System.out.println(northSouth + " " + westEast);*/
        
        return coordinates;
    }
    
}
