import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WordPane extends JPanel {


    ArrayList<String> words = new ArrayList<String>();

    public WordPane(){

        String[] wordTemp = {"LEEDS", "HULL", "LONDON", "EDINBURGH", "BELFAST", "LEEK", "BRAY", "OSLO", "PARIS", "BIRMINGHAM"};

        for(String s: wordTemp){
            words.add(s);
        }

    }

    void PaintComponent(Graphics g){

    }

    public ArrayList<String> getWords(){
        return words;
    }

    public void setWordsToFind(ArrayList<String> wordsToFind) {

        System.out.println(wordsToFind);
    }
}

