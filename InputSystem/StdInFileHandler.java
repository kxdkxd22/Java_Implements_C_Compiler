import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class StdInFileHandler implements FileHandler{
    private int curPos=0;
    private String input_buffer="";
    @Override
    public void open() {
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        while(!input.equals("end")){
            input_buffer+=input;
            input=s.nextLine();
        }
        s.close();
    }

    @Override
    public void close() {
        return ;
    }

    @Override
    public int read(byte[] buf, int begin, int len)  {

        if(curPos>=input_buffer.length()){
            return 0;
        }
        int readcnt=0;
        try {
            byte[] input_buf = input_buffer.getBytes("UTF-8");
            while(readcnt<len&&curPos+readcnt<input_buffer.length()){
                buf[begin+readcnt]=input_buf[curPos+readcnt];
                readcnt++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        curPos=readcnt;
        return readcnt;
    }
}
