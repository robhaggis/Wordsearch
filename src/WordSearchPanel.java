import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class WordSearchPanel extends JPanel{

    final int GRIDX = 20;
    final int GRIDY = 20;
    int panelWidth = -1;
    int panelHeight = -1;

    int cellClickedX = -1;
    int cellClickedY = -1;
    int cellMouseOverX = -1;
    int cellMouseOverY = -1;

    String[][] letterGrid = new String[GRIDY][GRIDX];

    boolean[][] isCurrentlySelected = new boolean[GRIDY][GRIDX];
    boolean startSelected = false;
    boolean endSelected = false;

    int guessStartX, guessStartY, guessEndX, guessEndY;

    public WordSearchPanel(){
        fillGridSpacesWithRandomLetters();

        for(int y = 0; y< GRIDY; y++) {
            for (int x = 0; x < GRIDX; x++) {
                isCurrentlySelected[y][x] = false;
            }
        }
    }

    //Called on every mouse click
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        panelWidth = super.getWidth();
        panelHeight = super.getHeight();
        int cellWidth = panelWidth / GRIDX;
        int cellHeight = panelHeight / GRIDY;

        for(int y = 0; y< GRIDY; y++) {
            for (int x = 0; x < GRIDX; x++) {

                //Draw Cells
                g.setColor(Color.white);
                if(cellMouseOverX ==x & cellMouseOverY == y){
                    g.setColor(new Color(0x88ff0000, true));
                }

                if(isCurrentlySelected[y][x]){
                    g.setColor(Color.cyan);
                }

                int cellX = x*cellWidth;
                int cellY = y*cellHeight;
                g.fillRect(cellX, cellY, cellWidth, cellHeight);

                //Draw Letters
                g.setColor(Color.black);
                g.setFont(new Font("Mono", Font.PLAIN, cellWidth-4));
                int xOffset = cellX + 4;
                int yOffset = cellY - 4 + cellHeight;
                g.drawString(letterGrid[y][x], xOffset, yOffset);
            }
        }
    }

    public void processClick(int x, int y){
        if(panelWidth != -1 && panelHeight != -1){
            cellClickedX = x / (panelWidth / GRIDX);
            cellClickedY = y / (panelHeight / GRIDY);
        }

        if(!startSelected){
            isCurrentlySelected[cellClickedY][cellClickedX] = true;
            startSelected = true;
            guessStartX = cellClickedX;
            guessStartY = cellClickedY;
        }
        else if (startSelected && !endSelected){
            isCurrentlySelected[cellClickedY][cellClickedX] = true;
            endSelected = true;
            guessEndX = cellClickedX;
            guessEndY = cellClickedY;
            if(checkForValidMove()){
                checkForWordMatch();
            }
            else
            {
                System.out.println("Invalid move");
                resetSelections();
            }
        }

        repaint();
    }

    public boolean checkForValidMove(){
        //Horizontal and Vertical
        return (guessStartY == guessEndY || guessStartX == guessEndX);
    }

    public void resetSelections(){
        for(int y = 0; y< GRIDY; y++) {
            for (int x = 0; x < GRIDX; x++) {
                isCurrentlySelected[y][x] = false;
            }
        }
        startSelected = false;
        endSelected = false;
    }

    private void checkForWordMatch(){
        System.out.println("No Match");
        resetSelections();
        //repaint();
    }

    public void setMousePos(int x, int y){
        if(panelWidth != -1 && panelHeight != -1){
            cellMouseOverX = x / (panelWidth / GRIDX);
            cellMouseOverY = y / (panelHeight / GRIDY);
        }
        repaint();
    }

    private void fillGridSpacesWithRandomLetters(){
        Random rng = new Random();

        letterGrid[0][0] =  "A";
        letterGrid[GRIDX-1][GRIDY-1] =  "Z";
        for(int y = 0; y< GRIDY; y++) {
            for (int x = 0; x < GRIDX; x++) {
                char c = (char)(rng.nextInt(26)+ 'A');

                if(letterGrid[y][x] == null){
                    letterGrid[y][x] = Character.toString(c);
                }

            }
        }
    }
}
