/********************************************************************
 *  ScoreboardMain.java
 *  Entry point/main class
 * 
 *  Richard Eric Lope BSN RN
 *  http://rel.phatcode.net
 *  
 * License MIT: 
 * Copyright (c) 2023 Richard Eric Lope 

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. (As clarification, there is no
 * requirement that the copyright notice and permission be included in binary
 * distributions of the Software.)

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 *******************************************************************/

package net.phatcode.rel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class ScoreboardMain extends JFrame
{

    
    
    /**
     * 
     */
    private static final long serialVersionUID = -5050928423447803298L;

    enum GameType
    {
        NBA,
        FIBA,
        COLLEGE,
        HIGH_SCHOOL,
        THREExTHREE,
    }
    
    private CountdownTimer countdownTimer;
    private CenterPanelDisplay centerPanelDisplay;
    private TeamPanelDisplay home;
    private TeamPanelDisplay visitor;
    private DisplayFrame displayFrame;
    
    private AnimatedLabel titleLabel;
    private String title;
    private JMenuBar mainMenu = new JMenuBar();
    private JMenu game = new JMenu("Game");
    private JMenu settings = new JMenu("Settings");
    private JMenu help = new JMenu("Help");
    
    private JFrame frameID; 
    
    public ScoreboardMain()
    {
        frameID = this;
        try
        {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            SwingUtilities.updateComponentTreeUI(this);

        } catch (Exception ex)
        {
            //System.err.println("Failed to initialize theme. Using fallback.");
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        System.setProperty("flatlaf.useWindowDecorations", "true");
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        setTitle("Scorebord Plus");
        
        
        //setPreferredSize(new Dimension(1337, 700));
        
        //mainMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        setJMenuBar(mainMenu);
        initMenu();
        
        
        TeamPanel left = new TeamPanel("   HOME     ");
        TeamPanel right =new TeamPanel("   VISITOR  ");

        add(left, BorderLayout.WEST);
        
        countdownTimer = new CountdownTimer(10, 1000* 60 * 10, 64 );
        add(countdownTimer, BorderLayout.CENTER);
        
        
        add(right, BorderLayout.EAST);
        
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        pack();
        setVisible(true);
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        //Init Display frame for dual monitor setup
        centerPanelDisplay = new CenterPanelDisplay(countdownTimer);
        home = new TeamPanelDisplay(left);
        visitor = new TeamPanelDisplay(right);
        
        displayFrame = new DisplayFrame(centerPanelDisplay,
                                        home, visitor);
        JPanel northPanel = new JPanel();
        title = " #AnyaBasic Invitational Cup ";
        titleLabel = new AnimatedLabel(300, title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        northPanel.add(titleLabel);
        countdownTimer.setLabelFont(titleLabel, 70);
        displayFrame.add(northPanel, BorderLayout.NORTH);
        //displayFrame.setVisible(true);
        displayFrame.setVisible(false);
        
    }
    
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        new ScoreboardMain();
    }

    
    private void initMenu()
    {
        game.setForeground(Color.CYAN);
        settings.setForeground(Color.MAGENTA);
        help.setForeground(Color.YELLOW);
        
        mainMenu.add(game);
        add(Box.createHorizontalStrut(64));
        mainMenu.add(settings);
        add(Box.createHorizontalStrut(64));
        mainMenu.add(help);
       
        //Images
        URL imageUrl = this.getClass().getClassLoader().getResource("assets/close-24.png");
        ImageIcon exit = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/basketball-24.png");
        ImageIcon newGame = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/player-24.png");
        ImageIcon team = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/about-24.png");
        ImageIcon about = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/overtime-24.png");
        ImageIcon overtime = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/monitors-24.png");
        ImageIcon checkDisplays = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/scoreboard-24.png");
        ImageIcon project = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/tv-24.png");
        ImageIcon tv = new ImageIcon(imageUrl);
        
        
        
        // Game
        game.setMnemonic(KeyEvent.VK_G);

        JMenu newGameItem = new JMenu("New Game...");
        newGameItem.setMnemonic(KeyEvent.VK_N);
        newGameItem.setToolTipText("Start a new game...");
        newGameItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                            
            }
            
        });
        
        String[] cbmiItems = { "NBA", "FIBA", "College", "High School", "3x3" };
        JMenuItem[] subItems = new JMenuItem[cbmiItems.length];
        
        for( int i = 0; i < cbmiItems.length; i++ )
        {
            subItems[i] = new JMenuItem(cbmiItems[i], newGame);
            newGameItem.add(subItems[i]);
        }
        
        subItems[0].setToolTipText("Start an NBA game.");
        subItems[0].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                countdownTimer.setGameType(GameType.NBA);
                countdownTimer.newGame();
                countdownTimer.pause();
                centerPanelDisplay.setPeriodLabel();
            }
            
        });
        
        subItems[1].setToolTipText("Start a FIBA game.");
        subItems[1].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                countdownTimer.setGameType(GameType.FIBA);
                countdownTimer.newGame();
                countdownTimer.pause();
                centerPanelDisplay.setPeriodLabel();
            }
            
        });
        
        
        subItems[2].setToolTipText("Start a College game.");
        subItems[2].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                countdownTimer.setGameType(GameType.COLLEGE);
                countdownTimer.newGame();
                countdownTimer.pause();
                centerPanelDisplay.setPeriodLabel();
            }
            
        });
        
        subItems[3].setToolTipText("Start a High School game.");
        subItems[3].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                countdownTimer.setGameType(GameType.HIGH_SCHOOL);
                countdownTimer.newGame();
                countdownTimer.pause();
                centerPanelDisplay.setPeriodLabel();
            }
            
        });
        
        subItems[4].setToolTipText("Start a 3x3 game.");
        subItems[4].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                countdownTimer.setGameType(GameType.THREExTHREE);
                countdownTimer.newGame();
                countdownTimer.pause();
                centerPanelDisplay.setPeriodLabel();
            }
            
        });
        
        
        JMenuItem changeHomeNameItem = new JMenuItem("Change Home Team Name", team);
        changeHomeNameItem.setMnemonic(KeyEvent.VK_H);
        changeHomeNameItem.setToolTipText("Change the name of the Home team.");
        changeHomeNameItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String newName = JOptionPane.showInputDialog( 
                        "Please enter home team name:",
                        home.getTeamName()); 
                if( newName.length() > 10 ) newName = left(newName, 10); 
                home.setTeamName(newName);           
            }
            
        });
        
        JMenuItem changeVisitorNameItem = new JMenuItem("Change Visitor Team Name", team);
        changeVisitorNameItem.setMnemonic(KeyEvent.VK_V);
        changeVisitorNameItem.setToolTipText("Change the name of the Visitor team.");
        changeVisitorNameItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String newName = JOptionPane.showInputDialog( 
                        "Please enter visitor team name:",
                        visitor.getTeamName());                   
                if( newName.length() > 10 ) newName = left(newName, 10); 
                visitor.setTeamName(newName);             
            }
            
        });
        
        JMenuItem startOverTimeItem = new JMenuItem("Start Overtime", overtime);
        startOverTimeItem.setMnemonic(KeyEvent.VK_O);
        startOverTimeItem.setToolTipText("Starts an overtime game.");
        startOverTimeItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                countdownTimer.overtime();
                centerPanelDisplay.setPeriodLabel();
            }
            
        });
        
        JMenuItem exitItem = new JMenuItem("Exit", exit);
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.setToolTipText("Closes the app.");
        exitItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                int n = JOptionPane.showConfirmDialog( frameID, 
                        "Would you like to exit the application?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION );
                
                if (n == JOptionPane.YES_OPTION) System.exit(0);
            }
            
        });
        
        
        // Settings
        
        JMenuItem addScreenItem = new JMenuItem("Project to second screen", project);
        addScreenItem.setMnemonic(KeyEvent.VK_S);
        addScreenItem.setToolTipText("Show a cleaner display on another screen.");
        addScreenItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                displayFrame.setVisible(true);
                showOnScreen(0,frameID);
                showOnScreenMaximized(1,displayFrame);
                //showOnScreen(1,displayFrame);
            }
            
        });
        
        JMenuItem checkExtScreenItem = new JMenuItem("Check External Displays", checkDisplays);
        checkExtScreenItem.setMnemonic(KeyEvent.VK_E);
        checkExtScreenItem.setToolTipText("Check of there is an external display.");
        checkExtScreenItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if( canExtendScreen() )
                {
                    addScreenItem.setEnabled(true);
                }
            }
            
        });
        
        JMenuItem changeTopTitleItem = new JMenuItem("Change Main Title", tv);
        changeTopTitleItem.setMnemonic(KeyEvent.VK_E);
        changeTopTitleItem.setToolTipText("Chang the text of the top title.");
        changeTopTitleItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String newTitle = JOptionPane.showInputDialog( 
                        "Please input new title: \n", 
                        title);
                
                if( newTitle != null ) 
                {
                    if( newTitle.length() > 30 ) newTitle = left(newTitle, 30); 
                    titleLabel.setMyText(newTitle);
                }
                
            }
            
        });
        
        JMenuItem changeBottomTitleItem = new JMenuItem("Change Bottom Title", tv);
        changeBottomTitleItem.setMnemonic(KeyEvent.VK_E);
        changeBottomTitleItem.setToolTipText("Chang the text of the bottom title.");
        changeBottomTitleItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String newTitle = JOptionPane.showInputDialog( 
                        "Please input new title: \n", 
                        centerPanelDisplay.getTitle());
                
                if( newTitle != null ) 
                {
                    if( newTitle.length() > 18 ) newTitle = left(newTitle, 18); 
                    centerPanelDisplay.setTitle(newTitle);
                }
                
            }
            
        });
        
        
        // Help
        
        JMenuItem aboutItem = new JMenuItem("About...", about);
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                JOptionPane.showMessageDialog(frameID,
                        "Basketball Scoreboard 1.1.0\n"+
                        "\n"+
                        "Richard Eric M. Lope\n"+
                        "http://rel.phatcode.net\n"+
                        "https://github.com/relminator\n"+
                        "Icons by https://icons8.com/ \n"+
                        "Fonts by https://www.fontspace.com/ \n"+
                        "Theme by https://www.formdev.com/flatlaf/ \n"+
                        "Java Runtime by https://www.microsoft.com/openjdk \n"+
                        "Installer package by https://jrsoftware.org/isinfo.php \n"+
                        "Java to exe by https://launch4j.sourceforge.net/index.html \n",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
        });
        
        
        // add
        game.add(newGameItem);
        game.add(changeHomeNameItem);
        game.add(changeVisitorNameItem);
        game.addSeparator();
        game.add(startOverTimeItem);
        game.addSeparator();
        game.add(changeTopTitleItem);
        game.add(changeBottomTitleItem);
        game.addSeparator();
        game.add(exitItem);
        
        settings.add(checkExtScreenItem);
        settings.add(addScreenItem);
        
        
        help.add(aboutItem);
        
        // Disable if we don't have an external screen
        addScreenItem.setEnabled(false);
        if( canExtendScreen() )
        {
            addScreenItem.setEnabled(true);
        }
        
    }
    
    private boolean canExtendScreen() 
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        GraphicsDevice mainScreen = gd[0];
        
        if( gd.length == 1 ) return false;
        for( int i = 1; i < gd.length; i++ )
        {
            GraphicsDevice currentScreen = gd[i];
            if( currentScreen.toString().equals(mainScreen.toString()) )
            {
                return false;
            }
            //System.out.println("Screen:  " + gd.length);
        }
        return true;
    }

    // A nice little function to show a JFrame on the second screen
    //  by Joseph Gordon, ryvantage. and Gerardo Cignarella Godoy 
    private void showOnScreen( int screen, JFrame frame  ) 
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        GraphicsDevice graphicsDevice;
        if( screen > -1 && screen < gd.length ) 
        {
            graphicsDevice = gd[screen];
        } 
        else if( gd.length > 0 ) 
        {
            graphicsDevice = gd[0];
        } 
        else 
        {
            throw new RuntimeException( "No Screens Found" );
        }
        Rectangle bounds = graphicsDevice.getDefaultConfiguration().getBounds();
        int screenWidth = graphicsDevice.getDisplayMode().getWidth();
        int screenHeight = graphicsDevice.getDisplayMode().getHeight();
        frame.setLocation(bounds.x + (screenWidth - frame.getPreferredSize().width) / 2,
                bounds.y + (screenHeight - this.getPreferredSize().height) / 2);
        frame.setVisible(true);
    }
    
    // A nice little function to show a JFrame on the second screen
    //  by Joseph Gordon 
    private void showOnScreenMaximized( int screen, JFrame frame )
    {
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        if( screen > -1 && screen < gs.length )
        {
            gs[screen].setFullScreenWindow( frame );
        }
        else if( gs.length > 0 )
        {
            gs[0].setFullScreenWindow( frame );
        }
        else
        {
            throw new RuntimeException( "No Screens Found" );
        }
        frame.setVisible(true);
            
    }
    
    private String left( String orig, int len)
    {
        return orig.substring(0, Math.min(len, orig.length()));
    }
}
