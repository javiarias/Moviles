import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



class Listener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        
        System.out.println(":)");
    }
}

public class HolaSwing extends JFrame
{

    //final es como const, no se puede editar
    private static final String msg = "hjdgjsdhghsjgjhgsdjhgfhjgsd";


    //final es como const, no se puede editar
    private String _title;

    class Listener1 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            
            System.out.println(":(");
        }
    }


    public HolaSwing(String name) {

        //super(name) es el : JFrame(name)
        super(name);
        _title = name;
    }

    public void init(String texto) {

        String texto2 = ":>";
        
        //esto no compila puesto que java reconoce que se referencia en una subclase, y es tonto
//      texto2 = "poggers";

        class Listener2 implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                
                //esto solo guarda una copia de texto en ese momento, no refleja cambios posteriores
                System.out.println(texto);
            }
        }
        class Listener3 implements ActionListener {
    
            public void actionPerformed(ActionEvent e) {
                
                //esto solo guarda una copia de texto2 en ese momento, no refleja cambios posteriores
                System.out.println(texto2);
            }
        }
        
        //esto no compila puesto que java reconoce que no va a verse el cambio
//      texto2 = "poggers";
        
        
        setSize(400, 400);

        setLayout(new java.awt.GridLayout(2, 2));

        //setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.PINK);

        JButton bot = new JButton(":)");
        bot.setBackground(new Color(110, 200, 180));
        bot.addActionListener(new Listener());

        add(bot);

        JButton bot1 = new JButton(":(");
        bot1.setBackground(new Color(200, 110, 180));
        bot1.addActionListener(new Listener1());

        add(bot1);

        JButton bot2 = new JButton(":<");
        bot2.setBackground(new Color(180, 200, 110));
        bot2.addActionListener(new Listener2());

        add(bot2);

        JButton bot3 = new JButton(":>");
        bot3.setBackground(new Color(200, 180, 110));
        bot3.addActionListener(new Listener3());

        add(bot3);
    }

    //mains en otra clase!!!!!!!!!!!
    public static void main(String[] args){
        HolaSwing win = new HolaSwing("poggers");

        win.init(":<");

        //setvisible FUERA DEL CONSTRUCTOR AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        win.setVisible(true);


        System.out.println("hee hoo");
    }
}