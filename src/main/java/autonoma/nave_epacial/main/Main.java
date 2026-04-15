package autonoma.nave_epacial.main;

import autonoma.nave_epacial.gui.Window;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("INICIANDO PROGRAMA 🚀");
            Window w = new Window();
            w.start();
        } catch (Exception e) {
            System.err.println("ERROR FATAL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}