import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class WordSearchPanel extends JPanel{

    //TODO Set Grid size based on longest word in list
    final int GRIDX = 7;
    final int GRIDY = 7;
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
    boolean[][] isFoundCell = new boolean[GRIDY][GRIDX];
    boolean startSelected = false;
    boolean endSelected = false;
    int sX, sY, eX, eY;

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
        if(failedToAddWords.size() >0){
            System.out.println("Failed to Add:");
            System.out.println(failedToAddWords);
            wordPane.removeUnaddedWords(failedToAddWords);
        }
        fillEmptyGridSpacesWithRandomLetters();
    }

    public void paintComponent(Graphics g){
        //TODO Center letters in cells properly
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

                if(isFoundCell[y][x]){
                    g.setColor(new Color(0x8800ddbb, true));
                }

                if(isCurrentlySelected[y][x]){
                    g.setColor(new Color(0x88dd0000, true));
                }


                int cellX = x*cellWidth;
                int cellY = y*cellHeight;
                g.fillRect(cellX, cellY, cellWidth, cellHeight);

                //draw Cell bounds
                g.setColor(Color.lightGray);
                g.drawRect(cellX, cellY, cellWidth, cellHeight);

                //Draw Letters
                g.setColor(Color.black);
                int fontSize = cellWidth-8;
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, fontSize));
                int xOffset = cellX +8;
                int yOffset = cellY -4 + cellHeight;
                g.drawString(letterGrid[y][x], xOffset, yOffset);
            }
        }
    }

    public void processClick(int x, int y){

        //Only check for clicks once panel is initialised
        if(panelWidth != -1 && panelHeight != -1){
            cellClickedX = x / (panelWidth / GRIDX);
            cellClickedY = y / (panelHeight / GRIDY);
        }

        if(!startSelected){
            isCurrentlySelected[cellClickedY][cellClickedX] = true;
            startSelected = true;
            sX = cellClickedX;
            sY = cellClickedY;
        }
        else if (startSelected && !endSelected) {
            isCurrentlySelected[cellClickedY][cellClickedX] = true;
            endSelected = true;
            eX = cellClickedX;
            eY = cellClickedY;

            String wordPicked = getStringFromGrid(sX, sY, eX, eY);
            if (wordPicked != "") {
                if (wordPane.isAWord(wordPicked)){
                    highlightWord(sX, sY, eX, eY);
                } else {
                    //NO MATCH or Invalid Move
                    System.out.println("No Match");
                }
            }
            resetSelections();
        }
        repaint();
    }

    public void highlightWord(int x1, int y1, int x2, int y2){

        System.out.println("Highlighting");

        //Horizontal
        if(y1==y2){
            if (x2 < x1) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }
            for(int x=x1;x<=x2;x++){
                isFoundCell[y1][x] = true;
            }
        }

        //Vertical
        if(x1==x2){
            if (y2 < y1) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for(int y=y1;y<=y2;y++){
                isFoundCell[y][x1] = true;
            }
        }

        //TODO Diagonal Highlighting


        //Diagonal
    }

    //TODO Ewww so many ifs
    private String getStringFromGrid(int x1,int y1, int x2,int y2){
        String w = new String();
        //L->R
        if (y1==y2 && x2 > x1){
            for(int x = x1;x<=x2;x++){
                w = w.concat(letterGrid[y1][x]);
            }
        }
        //R->L
        else if (y1==y2 && x1 > x2){
            for(int x = x1;x>=x2;x--){
                w = w.concat(letterGrid[y1][x]);
            }
        }
        //T->B
        else if (x1==x2 && y2 > y1){
            for(int y = y1;y<=y2;y++){
                w = w.concat(letterGrid[y][x1]);
            }
        }
        //B->T
        else if (x1==x2 && y1 > y2){
            for(int y = y1;y>=y2;y--){
                w = w.concat(letterGrid[y][x1]);
            }
        }
        //TL->BR
        else if (((x2-x1) == (y2-y1)) && (x2 > x1 && y2 > y1)){
            int xOffs = 0;
            for(int y = y1;y<=y2;y++){
                w = w.concat(letterGrid[y][(x1+xOffs)]);
                xOffs++;
            }
        }

        //BR->TL
        else if (((x2-x1) == (y2-y1)) && (x2 < x1 && y2 < y1)){
            int xOffs = 0;
            for(int y = y1;y>=y2;y--){
                w = w.concat(letterGrid[y][(x1-xOffs)]);
                xOffs++;
            }
        }

        //BL->TR
        else if (((x2-x1) == (y1-y2)) && (x1 < x2 && y2 < y1)){
            int yOffs = 0;
            for(int x = x1;x<=x2;x++){
                w = w.concat(letterGrid[y1-yOffs][x]);
                yOffs++;
            }
        }
        //TR-BL
        else if (((x2-x1) == (y1-y2)) && (x1 > x2 && y2 > y1)){
            int yOffs = 0;
            for(int x = x1;x>=x2;x--){
                w = w.concat(letterGrid[y1+yOffs][x]);
                yOffs++;
            }
        }
        return w;
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
                isFoundCell[y][x] = false;
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
