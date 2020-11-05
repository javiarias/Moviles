package com.OffTheLine.desktop;

import java.awt.Graphics;

public class Main {

    //--------------------------------------------------------------------
    //                          Métodos estáticos
    //--------------------------------------------------------------------

    /**
     * Programa principal. Crea la ventana y la configura para usar renderizado
     * activo. Luego entra en el bucle principal de la aplicación que ejecuta
     * el ciclo continuo de actualización/dibujado. Aproximadamente cada segundo
     * escribe en la salida estándar el número de fotogramas por segundo.
     *
     * La aplicación resultante no es interactiva. El usuario no puede hacer
     * nada salvo cerrarla.
     */
    public static void main(String[] args) {


        String path = "desktopGame/assets/";

        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Paint ventana = new Paint("Paint", path);
        if (!ventana.init(640, 480))
            // Ooops. Ha fallado la inicialización.
            return;

        // Vamos a usar renderizado activo. No queremos que Swing llame al
        // método repaint() porque el repintado es continuo en cualquier caso.
        ventana.setIgnoreRepaint(true);

        // Hacemos visible la ventana.
        ventana.setVisible(true);

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                ventana.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        else {
            // En "modo debug" podríamos querer escribir esto.
            //System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }

        // Obtenemos el Buffer Strategy que se supone que acaba de crearse.
        java.awt.image.BufferStrategy strategy = ventana.getBufferStrategy();

        // Vamos allá.
        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;
        // Bucle principal
        while(true) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            ventana.update(elapsedTime);
            // Informe de FPS
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;
            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    Graphics graphics = strategy.getDrawGraphics();
                    try {
                        ventana.render(graphics);
                    }
                    finally {
                        graphics.dispose();
                    }
                } while(strategy.contentsRestored());
                strategy.show();
            } while(strategy.contentsLost());
			/*
			// Posibilidad: cedemos algo de tiempo. es una medida conflictiva...
			try {
				Thread.sleep(1);
			}
			catch(Exception e) {}
			*/
        } // while

        // Si tuvieramos un mecanismo para acabar limpiamente, tendríamos
        // que liberar el BufferStrategy.
        // strategy.dispose();

    } // main
}
