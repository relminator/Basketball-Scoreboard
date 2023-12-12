package net.phatcode.rel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TeamPanelDisplay extends JPanel implements ActionListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 6750725708006446266L;

    private TeamPanel teamPanel;
    
    private String teamName;
    private int score;
    private int fouls;
    
    private AnimatedLabel nameLabel;
    private JLabel scoreLabel;
    private JLabel foulsLabel;
    private JLabel foulsLabelNum;
    
    private int fontSize;
    private Font numberFont;
    private Font calcFont;
       
    private Color textColor = Color.YELLOW;
    private Color numColor = Color.CYAN;
    
    private Timer timer;
    
    public TeamPanelDisplay( TeamPanel teamPanel )
    {
        this.teamPanel = teamPanel; 
        
        this.teamName = teamPanel.getTeamName();
        this.score = teamPanel.getScore();
        this.fouls = teamPanel.getFouls();
        
        this.fontSize = teamPanel.getFontSize();
        this.numberFont = teamPanel.getNumberFont();
        this.calcFont = teamPanel.getCalcFont();
           
        this.textColor = teamPanel.getTextColor();
        this.numColor = teamPanel.getNumColor();
            
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(350, 50));
        setMaximumSize(new Dimension(350, 50)); 
        
        initLabels();
        addComponents();
        initFont();
        
        timer = new Timer(10, this);
        timer.start();
        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        score = teamPanel.getScore();
        fouls = teamPanel.getFouls();
        scoreLabel.setText("" + score);
        foulsLabelNum.setText("" + fouls);
            
    }
    
    private void initLabels()
    {
        
        nameLabel = new AnimatedLabel(500, teamName);
        scoreLabel = new JLabel("0");
        foulsLabel = new JLabel("Fouls");
        foulsLabelNum = new JLabel("0");
        
        nameLabel.setForeground(textColor);
        scoreLabel.setForeground(numColor);
        foulsLabel.setForeground(textColor);
        foulsLabelNum.setForeground(numColor);
        
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        foulsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        foulsLabelNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
    }
    
    
    private void addComponents()
    {
        add(nameLabel);
        add(Box.createVerticalStrut(34));
        add(scoreLabel);
        add(Box.createVerticalStrut(12));
        add(Box.createVerticalStrut(64));
        add(foulsLabel);
        add(foulsLabelNum);
        
        
    }
    
    
    private void initFont()
    {
            Font sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, (int)(fontSize * 1.5));
            scoreLabel.setFont(sFont);
            sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, fontSize);
            foulsLabelNum.setFont(sFont);            
            
            sFont = calcFont.deriveFont(Font.TRUETYPE_FONT, fontSize-2);
            nameLabel.setFont(sFont);
            foulsLabel.setFont(sFont);

    }


    public String getTeamName()
    {
        return teamName;
    }


    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
        teamPanel.setTeamName(teamName);
        nameLabel.setMyText(teamName);
    }

    

}
