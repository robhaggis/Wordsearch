import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotion implements MouseMotionListener {

    WordSearchPanel wordSearchPanel;
    public MouseMotion(WordSearchPanel wsp){
        this.wordSearchPanel = wsp;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        wordSearchPanel.setMousePos(e.getX(), e.getY());
    }
}
