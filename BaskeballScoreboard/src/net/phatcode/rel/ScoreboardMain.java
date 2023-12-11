package net.phatcode.rel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class ScoreboardMain extends JFrame
{

    private CountdownTimer countdown10Mins;
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -5050928423447803298L;

    public ScoreboardMain()
    {
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
        //setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        
        //TestLabel = new AnimatedLabel(100,"Basketball");
       // add(TestLabel, BorderLayout.PAGE_START );
        
        JPanel top = new JPanel();
        TeamPanel left = new TeamPanel("HOME");
        TeamPanel right = new TeamPanel("VISITOR");

        add(top, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        countdown10Mins = new CountdownTimer(10, 1000* 60 * 10, 64 );
        countdown10Mins.setPreferredSize(new Dimension(150, 50));
        countdown10Mins.setMaximumSize(new Dimension(150, 50)); // set max = pref
        add(countdown10Mins, BorderLayout.CENTER);
        
        add(right, BorderLayout.EAST);
        
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        
    }
    
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        new ScoreboardMain();
    }

    

}
