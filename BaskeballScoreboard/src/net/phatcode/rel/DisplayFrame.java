package net.phatcode.rel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class DisplayFrame extends JFrame
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4959667461593559757L;
    
    //private CenterPanelDisplay centerPanel;
    //private TeamPanelDisplay home;
    //private TeamPanelDisplay visitor;
    
    
    public DisplayFrame( CenterPanelDisplay centerPanel,
                         TeamPanelDisplay home,
                         TeamPanelDisplay visitor )
    {
        //this.centerPanel = centerPanel;
        //this.home = home;
        //this.visitor = visitor;
        
        
        setPreferredSize(new Dimension(1337, 700));
        add(home, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(visitor, BorderLayout.EAST);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        
    }
    

    
    
    
}
