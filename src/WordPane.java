import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordPane extends JPanel {
    ArrayList<String> words = new ArrayList<String>();
    String[] puzzles = {"fruit", "ukcities", "icecream", "cocktails", "fourletters", "fiveletters", "elevenletters"};
    String title = new String();
    boolean[] wordsToStrikeThrough;

    public WordPane(){
        loadNewPuzzle();
    }

    public void loadNewPuzzle(){
        repaint();
        Random r = new Random();
        String randomFile = puzzles[r.nextInt(puzzles.length)];
        //String file = "src/wordlists/"+randomFile+".txt";
        String file = "src/wordlists/fourletters.txt";
        loadFileToWordList(file, 4);
        Collections.sort((words));
        wordsToStrikeThrough = new boolean[words.size()];
        for(int i=0;i<wordsToStrikeThrough.length;i++){
            wordsToStrikeThrough[i] = false;
        }
    }

    public void removeUnaddedWords(ArrayList<String> wordsToRemove){
        for(String w: wordsToRemove){
            words.remove(w);
        }
    }
    public boolean isAWord(String word){
       String wordRev = (new StringBuilder(word).reverse().toString());
        boolean isInTheGrid = (words.contains(word) || words.contains(wordRev));

        if(isInTheGrid){
           wordsToStrikeThrough[words.indexOf(word)] = true;
        }


        return isInTheGrid;


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //TODO Dynamic Columns based on num words in list
        //Draw Theme
        g.setFont(new Font("Mono", Font.BOLD, 20));
        g.drawString(title,10,30);

        //Draw Word List
        g.setFont(new Font("Mono", Font.PLAIN, 12));
        int wordsPerColumn = 6;
        int xOffset = 50;
        int yOffset = 50;
        for(int i=0;i<words.size();i++){
            if(wordsToStrikeThrough[i]){
                g.setColor(Color.RED);
            }else{
                g.setColor(Color.BLACK);
            }
            yOffset = yOffset+(15);
            g.drawString(words.get(i), xOffset, yOffset);


            if(i%wordsPerColumn==0){
                xOffset+=150;
                yOffset = 50;
            }
        }
        repaint();
    }

    public ArrayList<String> getWords(){
        return words;
    }

    public void loadFileToWordList(String filepath, int num){
        //TODO Discard Words based on grid size
        ArrayList<String> complete = new ArrayList<String>();
        complete.clear();
        words.clear();
        File file = new File(filepath);
        try {
            Scanner s = new Scanner(file);
            while(s.hasNextLine()){
                String data = s.nextLine();
                data = data.toUpperCase(Locale.ROOT);
                complete.add(data);
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        title = complete.get(0);
        complete.remove(0);
        Collections.shuffle((complete));
        for(int i=0;i<num;i++){
            words.add(complete.get(i));
        }
    }

}

