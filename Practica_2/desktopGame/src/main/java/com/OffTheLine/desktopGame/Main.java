package com.OffTheLine.desktopGame;

import com.OffTheLine.desktopEngine.Graphics;
import com.OffTheLine.logic.Logic;

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

        Graphics ventana = new Graphics("Paint");
        if (!ventana.init(640, 480, path))
            // Ooops. Ha fallado la inicialización.
            return;

        Logic logic;

        // Si tuvieramos un mecanismo para acabar limpiamente, tendríamos
        // que liberar el BufferStrategy.
        // strategy.dispose();

    } // main
}
