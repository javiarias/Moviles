package com.OffTheLine.desktopGame;

import com.OffTheLine.desktopEngine.Engine;
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

        String path = "assets/";

        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Engine _engine = new Engine(path);

        _engine.init();
        _engine.update();

        //Logic logic = new Logic(_engine);

        //logic.update();

        // Si tuvieramos un mecanismo para acabar limpiamente, tendríamos
        // que liberar el BufferStrategy.
        // strategy.dispose();

    } // main
}
