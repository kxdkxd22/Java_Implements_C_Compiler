import java.io.UnsupportedEncodingException;

public interface FileHandler {
    public void open();

    public void close();

    public int read(byte[] buf,int begin,int len) throws UnsupportedEncodingException;
}
