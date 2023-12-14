/********************************************************************
 *  TeamPanelDisplay.java
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

import java.awt.Color;
import java.awt.Component;
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
    private int timeouts;
    
    private JLabel nameLabel;
    private JLabel scoreLabel;
    private JLabel foulsLabel;
    private JLabel foulsLabelNum;
    private JLabel timeOutsLabel; 
    private JLabel timeOutsLabelNum; 
    
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
        //setPreferredSize(new Dimension(350, 50));
        //setMaximumSize(new Dimension(350, 50)); 
        
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
        timeouts = teamPanel.getTimeOuts();
        scoreLabel.setText("" + score);
        foulsLabelNum.setText("" + fouls);
        timeOutsLabelNum.setText("" + timeouts);
            
    }
    
    private void initLabels()
    {
        
        nameLabel = new JLabel(teamName);
        scoreLabel = new JLabel("0");
        foulsLabel = new JLabel("Fouls");
        foulsLabelNum = new JLabel("0");
        timeOutsLabel = new JLabel("    Timeouts    ");
        timeOutsLabelNum = new JLabel("0");
        
        nameLabel.setForeground(textColor);
        scoreLabel.setForeground(numColor);
        foulsLabel.setForeground(textColor);
        foulsLabelNum.setForeground(numColor);
        timeOutsLabel.setForeground(textColor);
        timeOutsLabelNum.setForeground(numColor);
        
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        foulsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        foulsLabelNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeOutsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeOutsLabelNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
    }
    
    
    private void addComponents()
    {
        add(nameLabel);
        add(Box.createVerticalStrut(64));
        add(scoreLabel);
        add(Box.createVerticalStrut(64));
        add(foulsLabel);
        add(foulsLabelNum);
        add(Box.createVerticalStrut(64));
        add(timeOutsLabel);
        add(timeOutsLabelNum);
        
    }
    
    
    private void initFont()
    {
            Font sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, (int)(fontSize * 1.5));
            scoreLabel.setFont(sFont);
            sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, fontSize);
            foulsLabelNum.setFont(sFont);            
            timeOutsLabelNum.setFont(sFont);            
            
            sFont = calcFont.deriveFont(Font.TRUETYPE_FONT, fontSize-2);
            nameLabel.setFont(sFont);
            foulsLabel.setFont(sFont);
            timeOutsLabel.setFont(sFont);

    }


    public String getTeamName()
    {
        return teamName;
    }


    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
        teamPanel.setTeamName(teamName);
        nameLabel.setText(teamName);
    }

    

}
