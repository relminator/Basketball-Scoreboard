package net.phatcode.rel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class ScoreboardMain extends JFrame
{

    
    
    /**
     * 
     */
    private static final long serialVersionUID = -5050928423447803298L;

    private CenterPanelDisplay centerPanelDisplay;
    private TeamPanelDisplay home;
    private TeamPanelDisplay visitor;
    private DisplayFrame displayFrame;
    
    
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
        
        
        setPreferredSize(new Dimension(1337, 700));
        
        //mainMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        setJMenuBar(mainMenu);
        initMenu();
        
        TeamPanel left = new TeamPanel("HOME");
        TeamPanel right = new TeamPanel("VISITOR");

        add(left, BorderLayout.WEST);
        
        CountdownTimer countdown10Mins = new CountdownTimer(10, 1000* 60 * 10, 64 );
        add(countdown10Mins, BorderLayout.CENTER);
        
        add(right, BorderLayout.EAST);
        
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        pack();
        setVisible(true);
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        //Init Display frame for dual monitor setup
        centerPanelDisplay = new CenterPanelDisplay(countdown10Mins);
        home = new TeamPanelDisplay(left);
        visitor = new TeamPanelDisplay(right);
        
        displayFrame = new DisplayFrame(centerPanelDisplay,
                                        home, visitor);
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
        
        // Game
        game.setMnemonic(KeyEvent.VK_G);

        JMenuItem changeHomeNameItem = new JMenuItem("Change Home Team Name");
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
                
                home.setTeamName(newName);             
            }
            
        });
        
        JMenuItem changeVisitorNameItem = new JMenuItem("Change Visitor Team Name");
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
                
                visitor.setTeamName(newName);             
            }
            
        });
        
        
        // Settings
        
        JMenuItem addScreenItem = new JMenuItem("Add Screen");
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
            }
            
        });
        
        
        // Settings
        
        JMenuItem aboutItem = new JMenuItem("About...");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                JOptionPane.showMessageDialog(frameID,
                        "Basketball Scoreboard \n"+
                        "\n"+
                        "Richard Eric M. Lope\n"+
                        "http://rel.phatcode.net\n"+
                        "Icons by https://icons8.com/ \n"+
                        "Fonts by https://www.fontspace.com/",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
        });
        
        
        // add
        game.add(changeHomeNameItem);
        game.add(changeVisitorNameItem);
     
        settings.add(addScreenItem);
        
        
        help.add(aboutItem);
        
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
    
    
}
