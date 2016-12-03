/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kart;

import java.util.Calendar;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.TalkerId;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.nmea.util.Time;
import net.sf.marineapi.nmea.util.Date;

/**
 *
 * @author Eivind Fugledal
 */
public class NMEASentenceGenerator {
    
    private SentenceFactory sf;
    
    public NMEASentenceGenerator()
    {
        sf = SentenceFactory.getInstance();
    }
    
    /**
     * Generates a GPGGA NMEA sentence
     * @param latitude Latitude in decimal degrees
     * @param longitude Longitude in decimal degrees
     * @param time Current time
     */
    public String generateGPGGASentence(double latitude, double longitude, Time time)
    {
        GGASentence gga = (GGASentence) sf.createParser(TalkerId.GP, "GGA");
        Position pos = new Position(latitude, longitude);
        
        gga.setTime(time);
        gga.setPosition(pos);
        
        System.out.println(gga.toSentence());
        
        return gga.toSentence();
    }
    
    /**
     * Generates a GPRMC NMEA sentence
     * @param latitude Latitude in decimal degrees
     * @param longitude Longitude in decimal degrees
     * @param time Current time
     */
    public String generateGPRMCSentence(double latitude, double longitude, Time time)
    {
        RMCSentence rmc = (RMCSentence) sf.createParser(TalkerId.GP, "RMC");
        Position pos = new Position(latitude, longitude);
        Date date = new Date(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DATE));
        
        rmc.setTime(time);
        rmc.setPosition(pos);
        rmc.setDate(date);
        
        System.out.println(rmc.toSentence());
        
        return rmc.toSentence();
    }
    
}
