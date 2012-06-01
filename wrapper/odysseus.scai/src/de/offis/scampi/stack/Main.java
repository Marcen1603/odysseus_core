/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Claas
 */
public class Main {

    //private  Stack _scaiStack = new Stack();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Stack _scaiStack = new Stack();

        try {
//            //BufferedReader fr = new BufferedReader(new FileReader("./de/offis/scai/test/sensorData.txt"));
//            BufferedReader fr = new BufferedReader(new FileReader("./src/de/offis/scampi/test/MWcreateTest.xml"));
//            String temp;
//            String text="";
//            for(int i=0; (temp = fr.readLine())!=null; i++)
//            {
//                text+=temp+"\n";
//            }
//            String text = "t01:mdstream ts=2011-03-29T17:08:54 n=Ardunio_01 dn=OFFIS ds=(pa=\"movment\" da=\"1023\");";
            String text ="t01:rl_sensr id=aaa;";

            Object result = _scaiStack.parsePacket(text);
            System.out.println(""+result.getClass());
            System.out.println("Response:"+(String)result);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
