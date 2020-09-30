import java.io.UnsupportedEncodingException;

public class InputSystem {

    private Input input = new Input();
    public void runStdinExample(){
        input.ii_newfile(null);

        input.ii_mark_start();
        printWord();
        input.ii_mark_end();
        input.ii_mark_prev();

        input.ii_mark_start();
        printWord();
        input.ii_mark_end();

        System.out.println("prev input:"+input.ii_ptext());
        System.out.println("cur input:"+input.ii_text());
    }

    private void printWord()  {
        byte c;
        while((c=input.ii_advance())!=' '){
            byte[] buf = new byte[1];
            buf[0] = c;
            try {
                String s = new String(buf,"UTF-8");
                System.out.print(s);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        System.out.println("");
    }


    public static void main(String[] args) {
        InputSystem inputSystem = new InputSystem();
        inputSystem.runStdinExample();
    }
}
