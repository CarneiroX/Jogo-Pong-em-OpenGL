import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyBoard implements KeyListener{
    private Cena cena;
    
    public KeyBoard(Cena cena){
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_A:
                if (cena.BarraMoviJog > -0.8 && !cena.JogoPausado) {
                    cena.BarraMoviJog = cena.BarraMoviJog - 0.1f;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (cena.BarraMoviJog > -0.8 && !cena.JogoPausado) {
                    cena.BarraMoviJog = cena.BarraMoviJog - 0.1f;
                }
                break;
            case KeyEvent.VK_D:
                if (cena.BarraMoviJog < 0.8 && !cena.JogoPausado) {
                    cena.BarraMoviJog = cena.BarraMoviJog + 0.1f;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (cena.BarraMoviJog < 0.8 && !cena.JogoPausado) {
                    cena.BarraMoviJog = cena.BarraMoviJog + 0.1f;
                }
                break;
            case KeyEvent.VK_P:
                cena.JogoPausado = !cena.JogoPausado;
                break;
            case KeyEvent.VK_S:
                if (cena.Fasejogo == 0) {
                    cena.Fasejogo = 1;
                }
                break;
            case KeyEvent.VK_X:
                if (cena.Fasejogo > 0) {
                    cena.Fasejogo = 0;
                    cena.ReiniciaJogo();
                }
                break;
            case KeyEvent.VK_Y:
                if (cena.Fasejogo == 3) {
                    cena.Fasejogo = 0;
                    cena.ReiniciaJogo();
                }
                break;
            case KeyEvent.VK_K:
                if (cena.Fasejogo == 3) {
                    System.exit(0);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
