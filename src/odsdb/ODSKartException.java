/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odsdb;

import java.io.IOException;

/**
 *
 * @author bar
 */
public class ODSKartException extends Exception {

    public ODSKartException(String string, IOException ex) {
        super(string,ex);
    }

    public ODSKartException(String string) {
        super(string);
    }
    
    public ODSKartException(Throwable cause){
        super(cause);
    }
}
