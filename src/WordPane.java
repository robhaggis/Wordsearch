import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordPane extends JPanel {
    ArrayList<String> words = new ArrayList<String>();
    String[] puzzles = {"fruit", "ukcities", "icecream", "cocktails", "fourletters", "fiveletters", "elevenletters"};
    String title = new String();
    public WordPane(){
        //TODO Multiple Text Files
        loadNewPuzzle();
    }

    public void loadNewPuzzle(){
        repaint();
        Random r = new Random();
        String randomFile = puzzles[r.nextInt(puzzles.length)];
        String file = "src/"+randomFile+".txt";
        loadFileToWordList(file, 20);
    }

    public void removeUnaddedWords(ArrayList<String> wordsToRemove){
        for(String w: wordsToRemove){
            words.remove(w);
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //TODO Dynamic Columns based on num words in list
        //Draw Theme
        g.setFont(new Font("Mono", Font.BOLD, 20));
        g.drawString(title,10,30);

        //Draw Word List
        g.setFont(new Font("Mono", Font.PLAIN, 12));
        if(words.size()>0){
            Collections.sort((words));
            g.setColor(Color.BLACK);

            int wordsPerColumn = (int)words.size() / 5;
            for(int i=0;i< wordsPerColumn;i++){
                g.drawString(words.get(i),10, 50+(12*i));
            }

            for(int i=wordsPerColumn;i< wordsPerColumn*2;i++){
                g.drawString(words.get(i),150, 50+(12*(i-wordsPerColumn)));
            }

            for(int i=wordsPerColumn*2;i< wordsPerColumn*3;i++){
                g.drawString(words.get(i),300, 50+(12*(i-wordsPerColumn*2)));
            }

            for(int i=wordsPerColumn*3;i< wordsPerColumn*4;i++){
                g.drawString(words.get(i),450, 50+(12*(i-wordsPerColumn*3)));
            }

            for(int i=wordsPerColumn*4;i< words.size();i++){
                g.drawString(words.get(i),600, 50+(12*(i-wordsPerColumn*4)));
            }
        }
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

