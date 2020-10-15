import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
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

        _time = System.nanoTime();
    }

    public void draw() {
        
    }
	
	public void paint(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        //super.paint(g);

		if(_logo != null) {

            long currentTime = System.nanoTime();
            long delta = currentTime - _time;
            _time = currentTime;


            g.drawImage(_logo, (int)_x, (int)_y, null);
            _x += ((double)_incX) * (delta / 1.0E9);
            _y += ((double)_incY) * (delta / 1.0E9);

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

        try {
            Thread.sleep(15);
        }
        catch (Exception e) {
        }

        repaint();
	}

    //mains en otra clase!!!!!!!!!!!
    public static void main(String[] args){
        Paint win = new Paint("poggers");

        win.init();
        win.setVisible(true);
    }

    String _path = "./";
    String _img = "babie.png";

    double _x;
    double _y;
    int _incX = 40; //px / sec
    int _incY = 30; //px / sec


    Image _logo;
    int _imageWidth;
    int _imageHeight;

    long _time;
}