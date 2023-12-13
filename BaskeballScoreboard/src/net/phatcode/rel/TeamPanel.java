/********************************************************************
 *  TeamPanel.java
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class TeamPanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 5414400442679755763L;
    private String teamName;
    private int score;
    private int fouls;
    private int timeOuts;
    
    
    private AnimatedLabel nameLabel;
    private JLabel scoreLabel;
    private JLabel foulsLabel;
    private JLabel foulsLabelNum;
    private JLabel timeOutsLabel; 
    private JLabel timeOutsLabelNum; 
    
    private JButton plusButton;
    private JButton minusButton;
    private JToolBar buttonToolBar;
    
    private JButton foulsPlusButton;
    private JButton foulsMinusButton;
    private JToolBar foulsButtonToolBar;
    
    private JButton timeOutsPlusButton;
    private JButton timeOutsMinusButton;
    private JToolBar timeOutsButtonToolBar;
    
    
    private int size;
    private Font numberFont;
    private Font calcFont;
       
    private Color textColor = Color.YELLOW;
    private Color numColor = Color.CYAN;
    
    public TeamPanel( String teamName )
    {
        
        this.teamName = teamName;
        score = 0;
        fouls = 0;
        timeOuts = 0;
        size = 64;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(350, 50));
        setMaximumSize(new Dimension(350, 50)); 
        
        initLabels();
        initButtons();
        addComponents();
        loadFont();
                
    }
    
    private void initLabels()
    {
        
        nameLabel = new AnimatedLabel(500, teamName);
        scoreLabel = new JLabel("0");
        foulsLabel = new JLabel("Fouls");
        foulsLabelNum = new JLabel("0");
        timeOutsLabel = new JLabel("Timeouts");
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
    
    private void initButtons()
    {
    
        buttonToolBar = new JToolBar();
        foulsButtonToolBar = new JToolBar();
        timeOutsButtonToolBar = new JToolBar();
        
        URL imageUrl = this.getClass().getClassLoader().getResource("assets/plus.png");
        ImageIcon plus = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/minus.png");
        ImageIcon minus = new ImageIcon(imageUrl);
        
        plusButton = new JButton(plus);
        minusButton = new JButton(minus);
        
        buttonToolBar.add(minusButton);
        buttonToolBar.add(plusButton);
        
        buttonToolBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        foulsPlusButton = new JButton(plus);
        foulsMinusButton = new JButton(minus);
        
        foulsButtonToolBar.add(foulsMinusButton);
        foulsButtonToolBar.add(foulsPlusButton);
        
        foulsButtonToolBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        timeOutsPlusButton = new JButton(plus);
        timeOutsMinusButton = new JButton(minus);
        
        timeOutsButtonToolBar.add(timeOutsMinusButton);
        timeOutsButtonToolBar.add(timeOutsPlusButton);
        
        timeOutsButtonToolBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        plusButton.setToolTipText("Add Score");
        plusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                score++;
                scoreLabel.setText("" + score);
            }
        });
        
                
        
        minusButton.setToolTipText("Subtract Score");
        minusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if( score > 0 ) score--;
                scoreLabel.setText("" + score);                
            }
        });
        
        
        foulsPlusButton.setToolTipText("Add Fouls");
        foulsPlusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                fouls++;
                foulsLabelNum.setText("" + fouls);
            }
        });
        
        foulsMinusButton.setToolTipText("Subtract Fouls");
        foulsMinusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if( fouls > 0 ) fouls--;
                foulsLabelNum.setText("" + fouls);
            }
        });
    
        timeOutsPlusButton.setToolTipText("Add Timeouts");
        timeOutsPlusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                timeOuts++;
                timeOutsLabelNum.setText("" + timeOuts);
            }
        });
        
        timeOutsMinusButton.setToolTipText("Subtract Timeouts");
        timeOutsMinusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if( timeOuts > 0 ) timeOuts--;
                timeOutsLabelNum.setText("" + timeOuts);
            }
        });
        
    }
    
    private void addComponents()
    {
        add(nameLabel);
        add(Box.createVerticalStrut(16));
        add(scoreLabel);
        add(buttonToolBar);
        add(Box.createVerticalStrut(16));
        add(foulsLabel);
        add(foulsLabelNum);
        add(foulsButtonToolBar);
        add(Box.createVerticalStrut(16));
        add(timeOutsLabel);
        add(timeOutsLabelNum);
        add(timeOutsButtonToolBar);
        
        
    }
    
    
    private void loadFont()
    {
     // load font
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/scientificcalculator.ttf");

        try
        {
            Font firaFont = Font.createFont(Font.TRUETYPE_FONT, is);
            numberFont = firaFont.deriveFont(Font.TRUETYPE_FONT, size);

        } catch (FontFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (numberFont == null)
        {
            scoreLabel.setFont(new Font("Monospaced", Font.PLAIN, (int) (size * 1.5)));
            foulsLabelNum.setFont(new Font("Monospaced", Font.PLAIN, (int) size));
            timeOutsLabelNum.setFont(new Font("Monospaced", Font.PLAIN, (int) size));
        } 
        else
        {
            Font sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, (int)(size * 1.5));
            scoreLabel.setFont(sFont);
            sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, size);
            foulsLabelNum.setFont(sFont);
            timeOutsLabelNum.setFont(sFont);
        }

     // load font
        is = this.getClass().getClassLoader().getResourceAsStream("assets/pocketcalculator.ttf");

        try
        {
            Font firaFont = Font.createFont(Font.TRUETYPE_FONT, is);
            calcFont = firaFont.deriveFont(Font.TRUETYPE_FONT, size);

        } catch (FontFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (calcFont == null)
        {
            nameLabel.setFont(new Font("Monospaced", Font.PLAIN, (int) size-2));
            foulsLabel.setFont(new Font("Monospaced", Font.PLAIN, (int) size-2));
            timeOutsLabel.setFont(new Font("Monospaced", Font.PLAIN, (int) size-2));} 
        else
        {
            Font sFont = calcFont.deriveFont(Font.TRUETYPE_FONT, size-2);
            nameLabel.setFont(sFont);
            foulsLabel.setFont(sFont);
            timeOutsLabel.setFont(sFont);
        }


    }

    public String getTeamName()
    {
        return teamName;
    }

    public int getScore()
    {
        return score;
    }

    public int getFouls()
    {
        return fouls;
    }

    public int getFontSize()
    {
        return size;
    }

    public Font getNumberFont()
    {
        return numberFont;
    }

    public Font getCalcFont()
    {
        return calcFont;
    }

    public Color getTextColor()
    {
        return textColor;
    }

    public Color getNumColor()
    {
        return numColor;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
        nameLabel.setMyText(teamName);
    }

    public int getTimeOuts()
    {
        return timeOuts;
    }
 
    
    // Getters and setters
    
    
}
