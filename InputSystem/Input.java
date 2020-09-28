public class Input {

    public int MAXLEX = 1024;
    public int MAXLOOK = 16;
    public int BUFSIZE = (MAXLEX*3)+(MAXLOOK*2);
    public int END = BUFSIZE;
    public int Danger = END;
    public int Next = END;
    public int endBuf = END;
    public Byte[] startBuf = new Byte[BUFSIZE];
    public int pMark = END;
    public int sMark = END;
    public int eMark = END;

    public int lineno = 1;

    public int Mline;

    public FileHandler getFileHandler(String filename){
        if(filename!=null){
            return new DiskFileHandler();
        }else{
            return new StdInFileHandler();
        }
    }



}
