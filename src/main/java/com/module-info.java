/**
 * 
 */
/**
 * 
 */

module compagnie_aerienne {
    requires java.sql;
    requires java.desktop;
    
    exports compagnie_aerienne;
    exports com.compagnieaerienne.dao;
    exports enumeration;
    exports model;
    exports view;
    exports test;
    
    opens compagnie_aerienne;
    opens com.compagnieaerienne.dao;
    opens enumeration;
    opens model;
    opens view;
    opens test;
}
