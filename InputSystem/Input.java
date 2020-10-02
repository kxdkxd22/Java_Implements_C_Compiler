
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Input {

    public static final byte EOF = 0;
    public int MAXLEX = 1024;
    public int MAXLOOK = 16;
    public int BUFSIZE = (MAXLEX*3)+(MAXLOOK*2);
    public int END = BUFSIZE;
    public int Danger = END-MAXLOOK;
    public int Next = END;
    public int endBuf = END;
    private final byte[] startBuf = new byte[BUFSIZE];
    public int pMark = END;
    public int sMark = END;
    public int eMark = END;

    public int lineno = 1;

    public int Mline = 1;

    public int pLineno = 0;
    public int pLength = 0;
    private boolean Eof_read = false;
    private FileHandler fileHandler;

    public boolean noMoreChars(){
        return (Eof_read&&Next>=endBuf);
    }

    public FileHandler getFileHandler(String filename){
        if(filename!=null){
            return new DiskFileHandler(filename);
        }else{
            return new StdInFileHandler();
        }
    }

    public void ii_newfile(String filename){
        fileHandler = getFileHandler(filename);
        fileHandler.open();
    }

    public String ii_text(){
        byte[] ii_text = Arrays.copyOfRange(startBuf,sMark,sMark+ii_length());
        return new String(ii_text, StandardCharsets.UTF_8);
    }

    public int ii_length(){
        return eMark-sMark;
    }

    public int ii_lineno(){
        return lineno;
    }

    public String ii_ptext(){
        byte[] ii_ptext = Arrays.copyOfRange(startBuf,pMark,pMark+ii_plength());
        return new String(ii_ptext,StandardCharsets.UTF_8);
    }

    public int ii_plength(){
        return pLength;
    }

    public int ii_plineno(){
        return pLineno;
    }

    public int ii_mark_start(){
        Mline=lineno;
        eMark=sMark=Next;
        return sMark;
    }

    public int ii_mark_end(){
        Mline=lineno;
        eMark=Next;
        return eMark;
    }

    public int ii_mark_prev(){
        pMark=sMark;
        pLineno = lineno;
        pLength = eMark - sMark;
        return pMark;
    }

    public int ii_to_mark(){
        lineno = Mline;
        Next = eMark;
        return Next;
    }

    public byte ii_advance(){
        if(noMoreChars()){
            return 0;
        }

        if(Eof_read==false&&ii_flush(false)<0){
            return -1;
        }

        if(startBuf[Next]=='\n'){
            lineno++;
        }

        return startBuf[Next++];
    }

    public final int NO_MORE_CHARS_TO_READ = 0;
    public final int FLUSH_OK = 1;
    public final int FLUSH_FAIL = -1;

    public int ii_flush(boolean force){
        int copy_amt,shift_amt,left_edge;

        if(noMoreChars()){
            return NO_MORE_CHARS_TO_READ;
        }
        if(Eof_read){
            return FLUSH_OK;
        }

        if(Next > Danger || force){
            left_edge=pMark>sMark?sMark:pMark;
            shift_amt=left_edge;
            if(shift_amt<MAXLEX){
                if(!force){
                    return FLUSH_FAIL;
                }
                left_edge = ii_mark_start();
                ii_mark_prev();
                shift_amt = left_edge;
            }

            copy_amt = endBuf - left_edge;
            System.arraycopy(startBuf,0,startBuf,left_edge,copy_amt);

            if(ii_fillbuf(copy_amt)==0){
                System.err.println("Internal error : buffer is full can not read");
            }
            if(pMark!=0){
                pMark-=shift_amt;
            }

            sMark-=shift_amt;
            eMark-=shift_amt;
            Next-=shift_amt;
        }

        return FLUSH_OK;
    }

    public int ii_fillbuf(int starting_at)  {

        int need;
        int got=0;
        need = ((END-starting_at)/MAXLEX)*MAXLEX;
        if(need < 0){
            System.err.println("internal error: bad read request address");
        }
        if(need == 0){
            return 0;
        }
        try {
            if((got=fileHandler.read(startBuf,starting_at,need))==-1){
                System.err.println("Can't read input file");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        endBuf=starting_at+got;
        if(got < need){
            Eof_read = true;
        }
        return got;
    }

    public void ii_pushback(int n){
        while(--n >= 0 && Next > sMark){
            if(startBuf[--Next]=='\n'||startBuf[Next]=='\0'){
                --lineno;
            }
        }

        if(Next < eMark){
            eMark = Next;
            Mline = lineno;
        }
        return ;
    }

    public byte ii_lookahead(int n){
        byte p = startBuf[Next+n-1];
        if(Eof_read&&Next+n-1>=endBuf){
            return EOF;
        }
        return (Next+n-1<0||Next+n-1>=endBuf)?0:p;
    }

}
