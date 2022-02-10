import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

    WordPane wordPane;

    String[][] letterGrid = new String[GRIDY][GRIDX];
    ArrayList<String> masterWordList;
    ArrayList<String> failedToAddWords = new ArrayList<String>();
    ArrayList<String> successfullyAddedWords = new ArrayList<String>();
    ArrayList<String> wordsToFind = new ArrayList<String>();

    boolean[][] isCurrentlySelected = new boolean[GRIDY][GRIDX];
    boolean startSelected = false;
    boolean endSelected = false;
    int guessStartX, guessStartY, guessEndX, guessEndY;

    Random rng = new Random();

    public WordSearchPanel(WordPane panelWords){
        this.wordPane = panelWords;
        masterWordList = panelWords.getWords();
        initGrid();
    }

    private void initGrid(){
        resetGrid();
        addWordListToGrid(masterWordList);
        int attempts = 0;
        while(failedToAddWords.size() > 0 && attempts<100){
            ArrayList<String> wordsToReAdd = new ArrayList<String>();
            for(String w : failedToAddWords){
                wordsToReAdd.add(w);
            }
            addWordListToGrid(wordsToReAdd);
            attempts++;
        }
        attempts = 0;


        wordsToFind = successfullyAddedWords;
        wordPane.setWordsToFind(wordsToFind);


        //fillGridSpacesWithRandomLetters();
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
                    g.setColor(new Color(0x8800bb55, true));
                }

                if(isCurrentlySelected[y][x]){
                    g.setColor(new Color(0x8800ddbb, true));
                }

                //draw Cell bounds

                int cellX = x*cellWidth;
                int cellY = y*cellHeight;
                g.fillRect(cellX, cellY, cellWidth, cellHeight);

                //draw Cell bounds
                g.setColor(Color.lightGray);
                g.drawRect(cellX, cellY, cellWidth, cellHeight);

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

    public void resetGrid(){
        repaint();
        for(int y=0;y<GRIDY;y++){
            for(int x=0;x<GRIDX;x++){
                letterGrid[y][x] = "";
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

    private ArrayList<String> addWordListToGrid(ArrayList<String> wordList) {
        failedToAddWords.clear();
        for (String word : wordList) {
            if (!(addWordToGrid(word))) {
                failedToAddWords.add(word);
            }
            else{
                successfullyAddedWords.add(word);
            }
        }
        return failedToAddWords;
    }


    private boolean addWordToGrid(String word) {
        int len = word.length();
        if (!(len > GRIDX) || !(len > GRIDY))
        {
            int dir = rng.nextInt(4);
            String[][] copyOfGrid = copyGrid();

            int wordX = 0;
            int wordY = 0;
            int lettersAdded = 0;
            switch(dir) {
                case 0:
                    wordX = rng.nextInt(0, GRIDX - len);
                    wordY = rng.nextInt(0, GRIDY);
                    lettersAdded = 0;
                    for (int i = 0; i < len; i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX + i, wordY, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;
                case 1:
                    wordX = rng.nextInt(0, GRIDX);
                    wordY = rng.nextInt(0, GRIDY-len);
                    lettersAdded = 0;
                    for (int i = 0; i < len; i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX, wordY+i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 2:
                    wordX = rng.nextInt(GRIDX-len, GRIDX);
                    wordY = rng.nextInt(0, GRIDY);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX-i, wordY, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 3:
                    wordX = rng.nextInt(0, GRIDX);
                    wordY = rng.nextInt(GRIDY-len, GRIDY);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX, wordY-i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;
                }


            if (lettersAdded == len) {
                letterGrid = copyOfGrid;
                return true;
            }
        }
        return false;
    }

    private boolean addLetterToCell(int x, int y, String letter, String[][] grid){
        if(grid[y][x].equals("") || grid[y][x].equals(letter)){
            grid[y][x] = letter;
            return true;
        }else{
            return false;
        }
    }

    private void fillGridSpacesWithRandomLetters() {

        for (int y = 0; y < GRIDY; y++) {
            for (int x = 0; x < GRIDX; x++) {
                char c = (char) (rng.nextInt(26) + 'A');

                if (letterGrid[y][x] == "") {
                    letterGrid[y][x] = Character.toString(c);
                }

            }
        }
    }

    private String[][] copyGrid(){
        String [][] newGrid = new String[GRIDY][GRIDX];

        for(int y=0;y<GRIDY;y++){
            for(int x=0;x<GRIDX;x++){
                newGrid[y][x] = letterGrid[y][x];
            }
        }
        return newGrid;
    }
}
