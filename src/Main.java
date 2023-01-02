import javax.swing.*;

public class Main {

    public static void main(String args[]){
        World newWorld = new World ();

        JFrame frame = new JFrame("Chinese Railway Route Determination Program");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Window win = new Window(newWorld);
        frame.getContentPane().add(win);

        frame.pack();
        frame.setVisible(true);


    }
} // class Main

