import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public class WordPane extends JPanel {
    ArrayList<String> words = new ArrayList<String>();

    public WordPane(){
        loadFileToWordList("src/fruit.txt", 20);
        this.revalidate();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //TODO Dynamic Columns based on num words in list
        if(words.size()>0){
            g.setColor(Color.BLACK);
            int numColumns = (int)words.size() / 10;
            int wordsPerColumn = (int)words.size() / numColumns;

            for(int i=0;i< wordsPerColumn;i++){
                g.drawString(words.get(i),10, 12+(12*i));
                System.out.println(words.get(i));
            }

            for(int i=wordsPerColumn;i< wordsPerColumn*2;i++){
                g.drawString(words.get(i),150, 12+(12*(i-wordsPerColumn)));
                System.out.println(words.get(i));
            }

            for(int i=wordsPerColumn*2;i< words.size();i++){
                g.drawString(words.get(i),300, 12+(12*(i-wordsPerColumn*2)));
                System.out.println(words.get(i));
            }
        }
    }

    public ArrayList<String> getWords(){
        return words;
    }

    public void loadFileToWordList(String filepath, int num){
        ArrayList<String> complete = new ArrayList<String>();
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

        Collections.shuffle((complete));
        for(int i=0;i<num;i++){
            words.add(complete.get(i));
        }
    }

}

