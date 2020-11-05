package com.OffTheLine.desktop;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.io.InputStream;
import java.io.FileInputStream;

/**
 * Prueba de concepto de renderizado activo con Java.
 *
 * La clase incluye el main y hereda de JFrame, incluyendo toda
 * la funcionalidad de la "aplicación". En condiciones normales
 * (una aplicación más compleja) la implementación se distribuiría
 * en más clases y se haría más versátil.
 */
public class Paint extends JFrame {

    /**
     * Constructor.
     *
     * @param title Texto que se utilizará como título de la ventana
     * que se creará.
     */
    public Paint(String title, String assetsPath) {
        super(title);
        _assetsPath = assetsPath;
    }

    //--------------------------------------------------------------------

    /**
     * Realiza la inicialización del objeto (inicialización en dos pasos).
     * Se configura el tamaño de la ventana, se habilita el cierre de la
     * aplicación al cerrar la ventana, y se carga la fuente que se usará
     * en la ventana.
     *
     * Debe ser llamado antes de mostrar la ventana (con setVisible()).
     *
     * @return Cierto si todo fue bien y falso en otro caso (se escribe una
     * descripción del problema en la salida de error).
     */
    public boolean init(int width, int height) {

        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cargamos la fuente del fichero .ttf.
        Font baseFont;
        try (InputStream is = new FileInputStream(_assetsPath + "Bangers-Regular.ttf")) {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch (Exception e) {
            // Ouch. No está.
            System.err.println("Error cargando la fuente: " + e);
            return false;
        }

        // baseFont contiene el tipo de letra base en tamaño 1. La
        // usamos como punto de partida para crear la nuestra, más
        // grande y en negrita.
        _font = baseFont.deriveFont(Font.BOLD, 40);

        return true;

    } //  init

    //--------------------------------------------------------------------

    /**
     * Realiza la actualización de "la lógica" de la aplicación. En particular,
     * desplaza el rótulo a su nueva posición en su deambular de izquierda
     * a derecha.
     *
     * @param deltaTime Tiempo transcurrido (en segundos) desde la invocación
     * anterior (frame anterior).
     */
    public void update(double deltaTime) {
        int maxX = getWidth() - 300; // 300 : longitud estimada en píxeles del rótulo

        _x += _incX * deltaTime;
        while(_x < 0 || _x > maxX) {
            // Vamos a pintar fuera de la pantalla. Rectificamos.
            if (_x < 0) {
                // Nos salimos por la izquierda. Rebotamos.
                _x = -_x;
                _incX *= -1;
            }
            else if (_x > maxX) {
                // Nos salimos por la derecha. Rebotamos
                _x = 2*maxX - _x;
                _incX *= -1;
            }
        } // while
    }  // update

    //--------------------------------------------------------------------

    /**
     * Dibuja en pantalla el estado actual de la aplicación.
     *
     * @param g Objeto usado para enviar los comandos de dibujado.
     */
    public void render(Graphics g) {

        // Borramos el fondo.
        g.setColor(Color.yellow.darker());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Ponemos el rótulo (si conseguimos cargar la fuente)
        if (_font != null) {
            g.setColor(Color.WHITE);
            g.setFont(_font);
            g.drawString("RENDERIZADO ACTIVO", (int)_x, 100);
        }

    } // render

    //--------------------------------------------------------------------
    //                    Atributos protegidos/privados
    //--------------------------------------------------------------------

    /**
     * Fuente usada para escribir el texto que se muestra moviéndose de lado a lado.
     */
    protected Font _font;

    /**
     * Posición x actual del texto (lado izquierdo). Es importante
     * que sea un número real, para acumular cambios por debajo del píxel si
     * la velocidad de actualización es mayor que la del desplazamiento.
     */
    protected double _x = 0;

    /**
     * Velocidad de desplazamiento en píxeles por segundo.
     */
    protected int _incX = 50;

    protected String _assetsPath;


} // class Paint
