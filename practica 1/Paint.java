import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

public class Paint extends JFrame
{

    public Paint(String name) {
        super(name);
    }

    public void init() {
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            _logo = javax.imageio.ImageIO.read((new java.io.File(_path + _img)));
            _imageWidth = _logo.getWidth(null);
            _imageHeight = _logo.getHeight(null);
        }
        catch (Exception e) {
            System.out.println(e);
        }

        _x = 0;
        _y = 0;
    }

    public void draw() {
        
    }
	
	public void paint(Graphics g) {

        try {
            Thread.sleep(15);
        }
        catch (Exception e) {
        }

        repaint();
    }
    
    public void update(double delta) {
        if (_logo != null){
            _x += ((double)_incX) * delta;
            _y += ((double)_incY) * delta;

            if(_x < 0) {
                _x = -_x;
                _incX *= -1;
            }
            else if (_x >= (getWidth() - _imageWidth)) {
                _x = 2*(getWidth() - _imageWidth) - _x;
                _incX *= -1;
            }

            if(_y < 0) {
                _y = -_y;
                _incY *= -1;
            }
            else if (_y >= (getHeight() - _imageHeight)) {
                _y = 2*(getHeight() - _imageHeight) - _y;
                _incY *= -1;
            }
        }
    }
    
    public void render(Graphics g) {

        //limpiar pantalla
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        //dibujar imagen
        if(_logo != null) {
            g.drawImage(_logo, (int)_x, (int)_y, null);
        }
    }

    //mains en otra clase!!!!!!!!!!!
    public static void main(String[] args){
        Paint win = new Paint("poggers");

        win.init();
        win.setIgnoreRepaint(true);
        win.setVisible(true);


        win.createBufferStrategy(2);

        BufferStrategy strat = win.getBufferStrategy();
        
        long _time = System.nanoTime();

        while (true) {

            //calculamos deltaTime. Está en ns así que se hace / 1.0E9
            long currentTime = System.nanoTime();
            double delta = (currentTime - _time) / (1.0E9);
            _time = currentTime;

            win.update(delta);

            do {
                // The following loop ensures that the contents of the drawing buffer
                // are consistent in case the underlying surface was recreated
                do {
                    // Get a new graphics context every time through the loop
                    // to make sure the strategy is validated
                    Graphics g = strat.getDrawGraphics();
            
                            
                    try {
                        win.render(g);
                    }
                    finally {
                        g.dispose();
                    }
                } while (strat.contentsRestored());
       
                // Display the buffer
                strat.show();
       
                // Repeat the rendering if the drawing buffer was lost
            } while (strat.contentsLost());
        }
    }

    String _path = "./";
    String _img = "babie.png";

    double _x;
    double _y;
    int _incX = 350; //px / sec
    int _incY = 270; //px / sec


    Image _logo;
    int _imageWidth;
    int _imageHeight;
}