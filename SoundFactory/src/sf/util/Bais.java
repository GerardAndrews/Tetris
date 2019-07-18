package sf.util;

import java.io.ByteArrayInputStream;

/**
 * This class allows the the byte array input stream to rewind without
 * having to reload it.
 * @author Ron Coleman
 */
public class Bais extends ByteArrayInputStream {
    /**
     * Constructor
     * @param buf Buffer
     * @param offset Offset in buffer
     * @param len Length of buffered data
     */
    public Bais(byte[] buf,int offset,int len) {
        super(buf,offset,len);
    }
    
    /**
     * Rewinds the input position to the beginning.
     */
    public void rewind() {
        super.pos = 0;
    }
    
}
