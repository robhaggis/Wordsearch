import javax.swing.*;
import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class WordSearchPanel extends JPanel{

    final int GRIDX = 25;
    final int GRIDY = 25;
    int panelWidth = -1;
    int panelHeight = -1;

    int cellClickedX = 0;
    int cellClickedY = 0;
    int cellMouseOverX = 0;
    int cellMouseOverY = 0;

    WordPane wordPane;

    String[][] letterGrid = new String[GRIDY][GRIDX];
    ArrayList<String> failedToAddWords = new ArrayList<String>();

    boolean[][] isCurrentlySelected = new boolean[GRIDY][GRIDX];
    boolean startSelected = false;
    boolean endSelected = false;
    int guessStartX, guessStartY, guessEndX, guessEndY;

    Random rng = new Random();

    public WordSearchPanel(WordPane panelWords){
        this.wordPane = panelWords;
        resetGrid();
    }

    private void initGrid(){
        addWordListToGrid(wordPane.getWords());
        int attempts = 0;
        while(failedToAddWords.size() > 0 && attempts<1000){
            ArrayList<String> wordsToReAdd = new ArrayList<String>();
            for(String w : failedToAddWords){
                wordsToReAdd.add(w);
            }
            addWordListToGrid(wordsToReAdd);
            attempts++;
        }
        System.out.println("Failed to Add:");
        System.out.println(failedToAddWords);
        //TODO Remove failed to add words from displayed list
        //fillEmptyGridSpacesWithRandomLetters();
    }

    public void paintComponent(Graphics g){
            //TODO Center letters in cells
        super.paintComponent(g);
        if(panelWidth==-1 || panelHeight==-1){
            panelWidth = super.getWidth();
            panelHeight = super.getHeight();
        }
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
                String wordPicked = new String();
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
        for(int y=0;y<GRIDY;y++) {
            for (int x = 0; x < GRIDX; x++) {
                letterGrid[y][x] = "";
                isCurrentlySelected[y][x] = false;
            }
        }
        startSelected = false;
        endSelected = false;
        initGrid();
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
                //successfullyAddedWords.add(word);
            }
        }
        return failedToAddWords;
    }


    private boolean addWordToGrid(String word) {

//        /System.out.println("addWordToGrid(" + word + ")");
        //TODO encourage more crossover words by biasing the wordX and wordY to
        //favour letters already on the grid
        int len = word.length();
        if (!(len > GRIDX) || !(len > GRIDY))
        {
            int dir = rng.nextInt(8);
            String[][] copyOfGrid = copyGrid();

            //TODO Abstract repeated code in switch statements to its own function
            int wordX = 0;
            int wordY = 0;
            int lettersAdded = 0;
            switch(dir) {
                case 0: //LEFT TO RIGHT
                    wordX = rng.nextInt(0, GRIDX - len);
                    wordY = rng.nextInt(0, GRIDY-1);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i < len; i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX + i, wordY, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;
                case 1://TOP TO BOTTOM
                    wordX = rng.nextInt(0, GRIDX-1);
                    wordY = rng.nextInt(0, GRIDY-len);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i < len; i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX, wordY+i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 2://RIGHT TO LEFT
                    wordX = rng.nextInt(GRIDX-(GRIDX-len), GRIDX-1);
                    wordY = rng.nextInt(0, GRIDY-1);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX-i, wordY, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 3://BOTTOM TO TOP
                    wordX = rng.nextInt(0, GRIDX-1);
                    wordY = rng.nextInt(GRIDY-(GRIDY-len), GRIDY);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX, wordY-i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 4://DOWN AND RIGHT
                    wordX = rng.nextInt(0, GRIDX-len);
                    wordY = rng.nextInt(0, GRIDY-len);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX+i, wordY+i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 5://DOWN AND LEFT
                    wordX = rng.nextInt(GRIDX-(GRIDX-len), GRIDX);
                    wordY = rng.nextInt(0, GRIDY-len);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX-i, wordY+i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 6://UP AND RIGHT
                    wordX = rng.nextInt(0, GRIDX-len);
                    wordY = rng.nextInt(GRIDY-(GRIDY-len), GRIDY);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX+i, wordY-i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;

                case 7://UP AND LEFT
                    wordX = rng.nextInt(GRIDX-(GRIDX-len), GRIDX);
                    wordY = rng.nextInt(GRIDY-(GRIDY-len), GRIDY);
                    //System.out.println("Len: " + len + ", X: " + wordX + ", Y: " + wordY + ", dir: " + dir);
                    lettersAdded = 0;
                    for (int i = 0; i <len;  i++) {
                        String letterToAdd = word.substring(i, i + 1);
                        if (addLetterToCell(wordX-i, wordY-i, letterToAdd, copyOfGrid)) {
                            lettersAdded++;
                        }
                    }
                    break;
                }

            if (lettersAdded == len) {
                letterGrid = copyOfGrid;
                //System.out.println(word + " added successfully");
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

    private void fillEmptyGridSpacesWithRandomLetters() {
        //TODO only use letters present in word list

        for (int y = 0; y < GRIDY; y++) {
            for (int x = 0; x < GRIDX; x++) {
                char c = (char) (rng.nextInt(26) + 'A');

                if (letterGrid[y][x] == "") {
                    letterGrid[y][x] = Character.toString(c);
                }

            }
        }
    }

    public void loadNewPuzzle(){
        wordPane.loadNewPuzzle();
        resetGrid();
        System.out.println("Reset Called");

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
