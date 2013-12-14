/**
 * FILE: CodeCrackerGamePanel.java
 * AUTHORS: Clara Smith and Cali Stenson
 * DATE: Dec 3 2013
 * MODIFIED: Dec 3 2013
 * 
 * CodeCrackerGamePanel creates the panel from which the user will play the game.
 */

// import declarations
import java.util.*;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.image.BufferedImage; 
import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;


public class CodeCrackerGamePanel extends JPanel {
  
  // instance vars
  private CodeCracker game;
  private Levels graph;
  // for top
  private JLabel headerLabel;
  // for left panel
  private JPanel leftPanel, labelPane, l1Pane, l2Pane, l3Pane, l4Pane;
  private JLabel levelsLabel;
  private JButton romeButton, tuscanyButton, veniceButton, quadButton, tupeloButton, hoopButton, tunnelsButton, clappButton;
  // for right panel
  private JPanel rightPanel, instructPane, messagePane, imagePane, submitPane;
  private JScrollPane storyPane;
  private JLabel instructLabel, messageLabel;
  private JTextField submitText;
  private JTextArea storyText;
  private JButton submitButton;
  private BufferedImage detectiveImage;
  private JLabel detectiveLabel;
  // preferred sizes
  private final int WIDTH = 950, HEIGHT = 750;
  
  // constructor
  public CodeCrackerGamePanel (CodeCracker c, Levels l) {
    
    game = c;
    graph = l;
    
    // sets up visuals
    setLayout(new BorderLayout()); // sets layout to Border Layout
    
    this.setBackground(new Color(76, 168, 194)); // sets background color
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // sets the size of the panel
    
    // starts with the top panel
    headerLabel = new JLabel("Welcome to Wendy Wellesley: Code Cracker!");
    headerLabel.setFont(new Font("Sans Serif", Font.PLAIN, 40));
    
    // creates left panel
    // nests Box Layout within panes of Flow Layout
    leftPanel = new ImagePanel(new ImageIcon("Images/map.png").getImage());
    leftPanel.setPreferredSize(new Dimension(423, 700));
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 4));
    leftPanel.setBackground(new Color(76, 168, 194));
    // creates panes
    //labelPane = new JPanel();
    //labelPane.setOpaque(false);
    //labelPane.setSize(new Dimension(415, 40));
    l1Pane = new JPanel();
    l1Pane.setOpaque(false);
    l2Pane = new JPanel();
    l2Pane.setOpaque(false);
    l3Pane = new JPanel();
    l3Pane.setOpaque(false);
    l4Pane = new JPanel();
    l4Pane.setOpaque(false);
    // creates componenets for left panel
    levelsLabel = new JLabel(" ");
    levelsLabel.setPreferredSize(new Dimension(415, 60));
    levelsLabel.setFont(new Font("Sans Serif", Font.PLAIN, 60));
    levelsLabel.setForeground(new Color(76, 168, 194));
    //System.out.println("LEVELS LABEL: " + levelsLabel.toString());
    romeButton = new JButton("Ancient Rome");
    romeButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    romeButton.setPreferredSize(new Dimension(200, 100));
    tuscanyButton = new JButton("Tuscany");
    tuscanyButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    tuscanyButton.setPreferredSize(new Dimension(150, 100));
    veniceButton = new JButton("Venice");
    veniceButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    veniceButton.setPreferredSize(new Dimension(150, 100));
    quadButton = new JButton("<html>The Quad</html>");
    quadButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    quadButton.setPreferredSize(new Dimension(90, 100));
    tupeloButton = new JButton("<html>Tupelo Lane</html>");
    tupeloButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    tupeloButton.setPreferredSize(new Dimension(90, 100));
    hoopButton = new JButton("<html>Cafe Hoop</html>");
    hoopButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    hoopButton.setPreferredSize(new Dimension(90, 100));
    tunnelsButton = new JButton("<html>The Tunnels</html>");
    tunnelsButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    tunnelsButton.setPreferredSize(new Dimension(90, 100));
    clappButton = new JButton("Clapp Library");
    clappButton.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    clappButton.setPreferredSize(new Dimension(200, 100));
    // creates and adds listener to buttons
    CodeCrackerGamePanelListener listener = new CodeCrackerGamePanelListener();
    romeButton.addActionListener(listener);
    tuscanyButton.addActionListener(listener);
    veniceButton.addActionListener(listener);
    quadButton.addActionListener(listener);
    tupeloButton.addActionListener(listener);
    hoopButton.addActionListener(listener);
    tunnelsButton.addActionListener(listener);
    clappButton.addActionListener(listener);
    // adds panes
    //labelPane.add(levelsLabel);
    l1Pane.add(romeButton);
    l2Pane.add(tuscanyButton);
    l2Pane.add(veniceButton);
    l3Pane.add(quadButton);
    l3Pane.add(tupeloButton);
    l3Pane.add(hoopButton);
    l3Pane.add(tunnelsButton);
    l4Pane.add(clappButton);
    // add all to right panel
    leftPanel.add(levelsLabel);
    leftPanel.add(l1Pane);
    leftPanel.add(l2Pane);
    leftPanel.add(l3Pane);
    leftPanel.add(l4Pane);
    
    // creates right panel
    // nests Box Layout within panes of Flow Layout
   
    rightPanel = new JPanel();
    rightPanel.setPreferredSize(new Dimension(523, 700));
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setBorder(BorderFactory.createLineBorder(new Color(186, 186, 186), 4));
    rightPanel.setBackground(new Color(76, 168, 194));
    // creates componenets for right panel
    instructPane = new JPanel();
    
    
    instructLabel = new JLabel("<html><strong>Current level: " + game.getCurrentLevel().getName() + "</strong>. Read the storyline and click on the " 
                                 + game.getCurrentLevel().getCipher().getType() + " button in the instructions pane " 
                                 + "for more info on how to decrypt the cipher.</html>");
    instructLabel.setPreferredSize(new Dimension(509, 50));
    instructPane.add(instructLabel);
    
    storyText = new JTextArea(7, 42);
    try {
      String content = new Scanner(graph.getRome().getStoryLine()).useDelimiter("\\A").next();
      storyText.setText(content);
      storyText.setLineWrap(true);
    } catch (IOException e) {
      System.out.println("***ALERT***  Could not find or read from file! Error: " + e);
    }
    storyPane = new JScrollPane(storyText);
    storyText.setEditable(false);
    storyPane.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));
    
    messagePane = new JPanel();
    messageLabel = new JLabel("<html><strong>Encrypted message</strong>: " + graph.getRome().getCipher().encrypt(graph.getRome().getMessage()) + "</html>");
    messagePane.add(messageLabel);
    
    imagePane = new JPanel();
    try {
      detectiveImage = ImageIO.read(new File("Images/submitwoman.png"));
      detectiveLabel = new JLabel(new ImageIcon(detectiveImage));
      imagePane.add(detectiveLabel);
    } catch (IOException e) {
      System.out.println("***ALERT***  Could not read or display image! Error: " + e);
    }
      
    submitPane = new JPanel();
    submitText = new JTextField(40);
    submitButton = new JButton("Submit");
    submitButton.addActionListener(listener);
    submitPane.add(submitText);
    submitPane.add(submitButton);
    
    instructPane.setOpaque(false);
    storyPane.setOpaque(false);
    messagePane.setOpaque(false);
    imagePane.setOpaque(false);
    submitPane.setOpaque(false);
    
    //instructPane.setPreferredSize(new Dimension(509, 70));
    storyPane.setSize(new Dimension(509, 150));
    //messagePane.setPreferredSize(new Dimension(509, 20));
        
    rightPanel.add(instructPane);
    rightPanel.add(storyPane);
    rightPanel.add(messagePane);
    rightPanel.add(imagePane);
    rightPanel.add(submitPane);
    
    
    // adds everything to GUI
    this.add(headerLabel, BorderLayout.PAGE_START);
    this.add(leftPanel, BorderLayout.LINE_START);
    this.add(rightPanel, BorderLayout.CENTER);
  }
  
  
  private class CodeCrackerGamePanelListener implements ActionListener {
    
    public void actionPerformed(ActionEvent event) {
      JButton b = new JButton();
      b = (JButton)event.getSource();
      if (b == submitButton) {
        System.out.println("Wooh! You submitted!");
        if (game.playLevel(submitText.getText())) {
          nextLevel(game.current);
          JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                        game.current.getName());
        }
        else JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
      } else if (b == romeButton) {
        System.out.println("Okay, you may now begin! You are now in ancient Rome");
      } else {
        String message = (String)JOptionPane.showInputDialog("Please input the decrypted message from the previous level");
        if (b == tuscanyButton) {
          if (checkMessage(message, graph.getRome())) {
            if (game.chooseLevelFromPause(graph.getRome(), graph.getTuscany(), message)) {
              nextLevel(graph.getTuscany());
              JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                            game.current.getName());
            }
          } else {
            JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
          }
        } else if (b == veniceButton) {
          if (checkMessage(message, graph.getTuscany())) {
            if (game.chooseLevelFromPause(graph.getTuscany(), graph.getVenice(), message)) {
              nextLevel(graph.getVenice());
              JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                            game.current.getName());
            }
          } else {
            JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
          }
        } else if (b == quadButton) {
          if (checkMessage(message, graph.getVenice())) {
            if (game.chooseLevelFromPause(graph.getVenice(), graph.getQuad(), message)) {
              nextLevel(graph.getQuad());
              JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                            game.current.getName());
            }
          } else {
            JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
          } 
        } else if (b == tupeloButton) {
          if (checkMessage(message, graph.getQuad())) {
            if (game.chooseLevelFromPause(graph.getQuad(), graph.getTupelo(), message)) {
              nextLevel(graph.getTupelo());
              JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                            game.current.getName());
            }
          } else {
            JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
          } 
        } else if (b == hoopButton) {
          if (checkMessage(message, graph.getTupelo())) {
            if (game.chooseLevelFromPause(graph.getTupelo(), graph.getHoop(), message)) {
              nextLevel(graph.getHoop());
              JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                            game.current.getName());
            }
          } else {
            JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
          } 
        } else if (b == tunnelsButton) {
          if (checkMessage(message, graph.getHoop())) {
            if (game.chooseLevelFromPause(graph.getHoop(), graph.getTunnels(), message)) {
              nextLevel(graph.getTunnels());
              JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                            game.current.getName());
            }
          } else {
            JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
          }  
        } else if (b == clappButton) {
          if (checkMessage(message, graph.getTunnels())) {
            if (game.chooseLevelFromPause(graph.getTunnels(), graph.getClapp(), message)) {
              nextLevel(graph.getClapp());
              JOptionPane.showMessageDialog(null, "Congrats! You have completed this level and can now continue onto the next level: " +
                                            game.current.getName());
            }
          } else {
            JOptionPane.showMessageDialog(null, "Sorry, your message was incorrect. Please try again.");
          } 
        } 
      }
      
    }
    
    // checks message to make sure it's correct
    // returns true if so, false if not
    public boolean checkMessage(String message) {
      return (message.equals(game.getCurrentLevel().getMessage()));
    }
    
    public boolean checkMessage(String message, Level l) {
      return (message.equals(l.getMessage()));
    }
    
    
    // sets center panel to next level's info
    public void nextLevel(Level l) {
      System.out.println("Setting center (viewed as right) panel to next level's info.");
      try {
        String content = new Scanner (l.getStoryLine()).useDelimiter("\\A").next();
        storyText.setText(content);
      } catch (IOException e) {
        System.out.print("***ATTENTION***: COULD NOT READ NEXT LEVEL'S STORY LINE FROM FILE. \n ERROR: " + e);
      }
      submitText.setText("");
    }
    
    
  }
  
}