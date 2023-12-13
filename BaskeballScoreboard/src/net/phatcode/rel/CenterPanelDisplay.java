/********************************************************************
 *  CenterPanelDisplay.java
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CenterPanelDisplay extends JPanel implements ActionListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 6775927793150487890L;

    CountdownTimer countDownTimer;
    
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel centerPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    
    private JLabel lblGameClock;  
    private JLabel lblShotClock; 
    private JLabel lblGame;  
    private JLabel lblShot; 
    
    private JLabel lblLeftBall;
    private JLabel lblRightBall;
    
    
    private JLabel lblPeriod; 
    private JLabel lblPeriodNum; 
    
    
    private String title;
    private AnimatedLabel lblTitle; 
    
    private int fontSize;
    private Font numberFont;
    private Font calcFont;
    
    private Color textColor = Color.RED;
    private Color numColor = Color.GREEN;
    
    private long remainingTime; // QT time () 12 mins.
    private long shotClockTime; // Quarter time (12 mins)
    
    private int possession = 0;
    private int period = 1;
    
    private Timer timer;
    
    
    public CenterPanelDisplay( CountdownTimer countDownTimer )
    {
        this.countDownTimer = countDownTimer;
        
        this.fontSize = countDownTimer.getFontSize();
        this.numberFont = countDownTimer.getNumberFont();
        this.calcFont = countDownTimer.getCalcFont();
        this.textColor = countDownTimer.getTextColor();
        this.numColor = countDownTimer.getNumColor();
        
        timer = new Timer(10, this);
        timer.start();
        
        
        this.setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 50));
        setMaximumSize(new Dimension(180, 50)); 
        
        title = " #AnyaBasic Invitational ";
        lblTitle = new AnimatedLabel(300, title);
        
        northPanel = new JPanel();
        westPanel = new JPanel();
        centerPanel = new JPanel();
        eastPanel = new JPanel();
        southPanel = new JPanel();
            
        
        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
        
        initLabels();
                
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        
        
        northPanel.add(lblShot);
        northPanel.add(lblShotClock);
        lblShot.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblShotClock.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalStrut(64));
        centerPanel.add(lblGame);
        centerPanel.add(lblGameClock);
        lblGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGameClock.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalStrut(64));
        centerPanel.add(lblPeriod);
        lblPeriod.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblPeriodNum);
        lblPeriod.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPeriodNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalStrut(64));
        centerPanel.add(lblTitle);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Only for alignment
        westPanel.setPreferredSize(new Dimension(68, 50));
        westPanel.setMaximumSize(new Dimension(68, 50)); 
        westPanel.setMinimumSize(new Dimension(68, 50)); 
        eastPanel.setPreferredSize(new Dimension(68, 50));
        eastPanel.setMaximumSize(new Dimension(68, 50)); 
        eastPanel.setMinimumSize(new Dimension(68, 50)); 
        
        westPanel.add(lblLeftBall);
        eastPanel.add(lblRightBall);
        
        
        initFont();
        
        updateClockLabels();
        
        
    }

    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        remainingTime = countDownTimer.getRemainingTime();
        shotClockTime = countDownTimer.getShotClockTime();
        possession = countDownTimer.getPossession();
        period = countDownTimer.getPeriod();
        lblPeriodNum.setText("" + period);
        updateClockLabels();
        updatePossessionArrow();
        
    }

    
    
    private void initLabels()
    {
        
        URL imageUrl = this.getClass().getClassLoader().getResource("assets/ball.png");
        ImageIcon ball = new ImageIcon(imageUrl);
        
        lblGameClock = new JLabel();  
        lblShotClock = new JLabel(); 
        lblGame = new JLabel(" GAME ");  
        lblShot = new JLabel(" SHOT "); 
        
        lblPeriod = new JLabel(" PERIOD "); 
        lblPeriodNum = new JLabel("1"); 
        
        lblLeftBall = new JLabel(ball); 
        lblRightBall = new JLabel(ball); 
        lblRightBall.setVisible(false);
        lblLeftBall.setVisible(true);
        
        
        lblGame.setForeground(textColor);
        lblShot.setForeground(textColor);
        lblPeriod.setForeground(textColor);
        lblTitle.setForeground(Color.WHITE);
        
        lblGameClock.setForeground(numColor);
        lblShotClock.setForeground(numColor);
        lblPeriodNum.setForeground(numColor); 
        
    }
            
    private void initFont()
    {
        Font sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, fontSize);
        lblGameClock.setFont(sFont);
        lblShotClock.setFont(sFont);
        lblPeriodNum.setFont(sFont); 
        lblTitle.setFont(sFont);        
        
        sFont = calcFont.deriveFont(Font.TRUETYPE_FONT, fontSize-2);
        lblGame.setFont(sFont);
        lblShot.setFont(sFont);
        lblPeriod.setFont(sFont);        
        lblTitle.setFont(sFont);        
        
    }
        
    private String getGameClockLabel()
    {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
        
        String time = df.format(remainingTime);
       
        lblGameClock.setForeground(Color.GREEN);
        if( remainingTime <  (1000 * 60 * 10))    // < 10 minutes
        {
            if( remainingTime <=  (1000 * 60 * 2) )    // < 2 minutes
            {
                if( remainingTime <=  (shotClockTime) )    // < 24 secs remaining game time
                {
                   lblGameClock.setForeground(Color.MAGENTA);    
                }
                else
                {
                    lblGameClock.setForeground(Color.ORANGE);
                }
                return time.substring(1,time.length()-2);
            }
            return time.substring(1,time.length()-4);
        }
       
        return time.substring(0,time.length()-4);
    }
    
    private String getShotClockLabel()
    {
        SimpleDateFormat df = new SimpleDateFormat("ss:SSS");
        
        String time = df.format(shotClockTime);
        
        if( remainingTime <=  (shotClockTime) )    // < 24 secs remaining game time
        {
           lblShotClock.setForeground(Color.MAGENTA);    
           return "--";    // shot clock off
        }
        
        if( shotClockTime <=  5000)   // 5 seconds
        {
            lblShotClock.setForeground(Color.MAGENTA);    
            return time.substring(1,time.length()-2);
        }
        lblShotClock.setForeground(numColor);
        return time.substring(0,time.length()-4);   
    }
    
    private void updateClockLabels()
    {
        lblGameClock.setText(getGameClockLabel());
        lblShotClock.setText(getShotClockLabel());
        
        lblGameClock.repaint();
        lblShotClock.repaint();

    }

    private void updatePossessionArrow()
    {
        if( possession == 0 )
        {
            lblRightBall.setVisible(false);
            lblLeftBall.setVisible(true);
        }
        else
        {
            lblRightBall.setVisible(true);
            lblLeftBall.setVisible(false);
        }
        
    }
    
    public void setPeriodLabel()
    {
        String text = " PERIOD ";        
        if( !countDownTimer.isOvertime() )
        {
            switch(countDownTimer.getGameType())
            {
            case NBA:
                text = " PERIOD ";
                break;
            case FIBA:
                text = " PERIOD ";
                break;
            case COLLEGE:
                text = "  HALF  ";
                break;
            case HIGH_SCHOOL:
                text = "  HALF  ";
                break;
            case THREExTHREE:
                text = " PERIOD ";
                break;
            }
        }
        else
        {
            text = "OVERTIME";
        }
        
        
        lblPeriod.setText(text); 
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
        lblTitle.setMyText(title);
    }
    
    

}
