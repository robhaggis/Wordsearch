import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import static javax.swing.BorderFactory.createLineBorder;

public class Wordsearch {

    static final int WINDOWWIDTH = 500;
    static final int WINDOWHEIGHT = 800;

    public static void main(String[] args){

        //Get a window up
        //Internal storage of game
        //G2D for graphics display
        //Word List
        //Selecting tiles
        //Highlighting found

        JFrame frame = new JFrame();
        frame.setTitle("WordSearch");
        frame.setSize(new Dimension(WINDOWWIDTH,WINDOWHEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        populateWindow(frame);
        frame.setVisible(true);
    }

    private static void populateWindow(JFrame frame){
        //Layout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0,0,0,0);

        //Wordsearch Grid
        WordSearchPanel wordSearchPanel = new WordSearchPanel();
        wordSearchPanel.addMouseListener(new Mouse(wordSearchPanel));
        wordSearchPanel.addMouseMotionListener(new MouseMotion(wordSearchPanel));
        wordSearchPanel.setBorder(new LineBorder(Color.BLACK, 1));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        frame.add(wordSearchPanel, c);

        //Word Pane
        JPanel wordList = new JPanel();
        wordList.setBorder(new LineBorder(Color.BLACK, 1));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weighty = 0.4;
        frame.add(wordList, c);

        //Generate Button
        JButton generateButton = new JButton("Generate Puzzle");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        frame.add(generateButton, c);

        //Reset Button
        JButton resetButton = new JButton("Reset Puzzle");
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        frame.add(resetButton, c);


    }
}
