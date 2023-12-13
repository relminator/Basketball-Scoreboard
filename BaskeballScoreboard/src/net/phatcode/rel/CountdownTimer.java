/********************************************************************
 *  CountdownTimer.java
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
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;

public class CountdownTimer extends JPanel implements ActionListener
{
    /**
     * 
     */
    private static final long serialVersionUID = 1225959267916588816L;
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
    
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton editButton;
    private JButton editShotButton;
    
    private JButton leftButton;
    private JButton rightButton;
    
    private Clip buzzerClip;
    
    private JToolBar toolBar;
    
    private JButton plusButton;
    private JButton minusButton;
    private JToolBar periodToolBar;
    
    private Timer timer;
    private long remainingTime = 1000 * 60 * 10; // QT time () 12 mins.

    private long lastUpdateTime; // When count was last updated
    
    private long elapsed;
    private long shotClockTime; // Quarter time (12 mins)
    
    private int size;
    private Font numberFont;
    private Font calcFont;
    
    private Color textColor = Color.RED;
    private Color numColor = Color.GREEN;
    
    private int possession = 0;
    private int period =  1;
    
    private boolean overtime = false;
    
    private long shotClockDuration;
    private long gameDuration;
    
    private ScoreboardMain.GameType gameType;
    
    public CountdownTimer( int delayPerTick,
                           long duration, 
                           int size )
    {
        this.size = size;
        this.remainingTime = duration;
        playBuzzer();
        
        gameType = ScoreboardMain.GameType.COLLEGE;
        
        lastUpdateTime = System.currentTimeMillis(); 
        
        this.setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 50));
        setMaximumSize(new Dimension(180, 50)); 
        
        
        northPanel = new JPanel();
        westPanel = new JPanel();
        centerPanel = new JPanel();
        eastPanel = new JPanel();
        southPanel = new JPanel();
            
        toolBar = new JToolBar();
        periodToolBar = new JToolBar();
        
        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
        
        initLabels();
        initButtons();
                
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
        //centerPanel.add(Box.createVerticalStrut(64));
        centerPanel.add(periodToolBar);
        periodToolBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        westPanel.add(leftButton);
        eastPanel.add(rightButton);
        
        westPanel.add(lblLeftBall);
        eastPanel.add(lblRightBall);
        
        southPanel.add(toolBar);
        toolBar.add(editShotButton);
        toolBar.add(editButton);
        toolBar.add(resetButton);
        toolBar.add(pauseButton);
        toolBar.add(startButton);
        
        loadFont();
        
        
        newGame();
        
        updateClockLabels();
        
        initTimer( delayPerTick );
        
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
    
    }

    private void initTimer( int delayPerTick )
    {
        timer = new Timer(delayPerTick, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                long now = System.currentTimeMillis(); 
                elapsed = now - lastUpdateTime; // ms elapsed since last update
                remainingTime -= elapsed; 
                shotClockTime -= elapsed;
                lastUpdateTime = now;   //remember this update time
                
                if( shotClockTime < 0 )
                {
                    remainingTime += elapsed;  //fix timer to MULTIPLES OF 24:00
                    shotClockTime = shotClockDuration;
                    
                    updateClockLabels();
                    pause();
                    playBuzzer();
                    
                }
                
                if( remainingTime < 0 ) 
                {
                    remainingTime = 0;
                }
                
                if (remainingTime == 0) 
                {
                    playBuzzer();
                    timer.stop();
                }
                                        
                updateClockLabels();
                
            }
        });
        
        
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
        
        lblGameClock.setForeground(numColor);
        lblShotClock.setForeground(numColor);
        lblPeriodNum.setForeground(numColor); 
        
    }
    public void initButtons()
    {
        
        URL imageUrl = this.getClass().getClassLoader().getResource("assets/play.png");
        ImageIcon start = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/pause.png");
        ImageIcon pause = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/reset.png");
        ImageIcon reset = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/edit.png");
        ImageIcon edit = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/shotclock.png");
        ImageIcon editShot = new ImageIcon(imageUrl);
        
        imageUrl = this.getClass().getClassLoader().getResource("assets/left.png");
        ImageIcon left = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/right.png");
        ImageIcon right = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/plus.png");
        ImageIcon plus = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("assets/minus.png");
        ImageIcon minus = new ImageIcon(imageUrl);
        
        plusButton = new JButton(plus);
        minusButton = new JButton(minus);
        
        periodToolBar.add(minusButton);
        periodToolBar.add(plusButton);
        periodToolBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        startButton = new JButton(start);
        pauseButton = new JButton(pause);
        resetButton = new JButton(reset);
        editButton = new JButton(edit);
        editShotButton = new JButton(editShot);

        leftButton = new JButton(left);
        rightButton = new JButton(right);
        
        startButton.setToolTipText("Starts both the gameclock and shotclock.");
        startButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                resume();
            }
        });
        
        
        pauseButton.setToolTipText("Pauses the Game and Shot clocks. For timeouts, etc.");
        pauseButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                pause();
            }
        });

        resetButton.setToolTipText("Resets the shot clock. For change of possession, etc.");
        resetButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                shotClockTime = shotClockDuration;
                //remainingTime += elapsed;  
                updateClockLabels();
                pause();
            }
        });
        
        editButton.setToolTipText("Edit Game Time");
        editButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                pause();
                SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
                
                String time = df.format(remainingTime);
                String gameClockString = JOptionPane.showInputDialog( 
                        "Please input Gameclock: \n"+
                        "Current clock is:",
                        time);                   
                if( gameClockString != null )
                    remainingTime = timeToMillis( gameClockString );
                
                updateClockLabels();
            }
        });

        editShotButton.setToolTipText("Edit Shot Clock");
        editShotButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                pause();
                SimpleDateFormat df = new SimpleDateFormat("ss:SSS");
                
                String time = df.format(shotClockTime);
               
                String shotClockString = JOptionPane.showInputDialog( 
                        "Please input Shotclock: \n"+
                        "Current clock is:",
                        time);        
                if( shotClockString != null )
                    shotClockTime = shotToMillis( shotClockString );
                
                updateClockLabels();
            }
        });

        leftButton.setToolTipText("Change Ball Possession");
        leftButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                possession = 0;
                updatePossessionArrow();
            }
        });

        rightButton.setToolTipText("Change Ball Possession");
        rightButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                possession = 1;
                updatePossessionArrow();
            }
        });
        
        plusButton.setToolTipText("Add period");
        plusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                period++;
                lblPeriodNum.setText("" + period);
            }
        });
        
                
        
        minusButton.setToolTipText("Subtract Period");
        minusButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if( period > 1 ) period--;
                lblPeriodNum.setText("" + period);                
            }
        });
        

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
    
    private long timeToMillis( String myTime )
    {
        
        String[] timestamps = myTime.split(":");
        
        if( timestamps.length < 3 ) return 0;
        
        long minutes = Long.parseLong(timestamps[0]);
        long seconds = Long.parseLong(timestamps[1]);
        long milliSeconds = Long.parseLong(timestamps[2]);
        
        //limit values
        clamp( minutes, 0, 59 );
        clamp( seconds, 0, 59 );
        clamp( milliSeconds, 0, 999 );
        
        return minutes * 60 * 1000 + seconds * 1000 + milliSeconds; 
        
        
    }
    
    private long shotToMillis( String myTime )
    {
        
        String[] timestamps = myTime.split(":");
        
        if( timestamps.length < 2 ) return 0;
        
        long seconds = Long.parseLong(timestamps[0]);
        long milliSeconds = Long.parseLong(timestamps[1]);
        
        //limit values
        clamp( seconds, 0, 24 );
        clamp( milliSeconds, 0, 999 );
        
        return  seconds * 1000 + milliSeconds; 
        
        
    }
    
    private long clamp( long a, long min, long max )
    {   
        return (a < min) ? min : (a > max) ? max : a;
    }
    
    private String getGameClockLabel()
    {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
        
        String time = df.format(remainingTime);
       
        
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
    
    private void resume() 
    {
      if( !timer.isRunning() )
      {
          lastUpdateTime = System.currentTimeMillis();
          timer.start(); 
      }
    }

    public void pause() 
    {
      if( timer.isRunning() )
      {
          long now = System.currentTimeMillis();
          remainingTime -= (now - lastUpdateTime);
          timer.stop(); 
      }
    }
    
    public void scored()
    {
        shotClockTime = shotClockDuration;
        updateClockLabels();
        pause();
        
        
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
            lblGameClock.setFont(new Font("Monospaced", Font.PLAIN, (int) size));
            lblShotClock.setFont(new Font("Monospaced", Font.PLAIN, (int) size));
            lblPeriodNum.setFont(new Font("Monospaced", Font.PLAIN, (int) size));
        } 
        else
        {
            Font sFont = numberFont.deriveFont(Font.TRUETYPE_FONT, size);
            lblGameClock.setFont(sFont);
            lblShotClock.setFont(sFont);
            lblPeriodNum.setFont(sFont); 
            
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
            lblGameClock.setFont(new Font("Monospaced", Font.PLAIN, (int) size-2));
            lblShotClock.setFont(new Font("Monospaced", Font.PLAIN, (int) size-2));
            lblPeriod.setFont(new Font("Monospaced", Font.PLAIN, (int) size-2));
        } 
        else
        {
            Font sFont = calcFont.deriveFont(Font.TRUETYPE_FONT, size-2);
            lblGame.setFont(sFont);
            lblShot.setFont(sFont);
            lblPeriod.setFont(sFont); 
            
        }


    }
    
    private void playBuzzer()
    {
        //InputStream is = this.getClass().getClassLoader().getResourceAsStresm("assets/buzzer.wav");
        try
        {
            AudioInputStream buzzerAudioInputStream = 
                    AudioSystem.getAudioInputStream(
                            this.getClass().getClassLoader().getResource("assets/buzzer.wav"));
            buzzerClip = AudioSystem.getClip();
            buzzerClip.open(buzzerAudioInputStream);
            buzzerClip.start();
        } 
        catch (UnsupportedAudioFileException ex) 
        {
        System.out.println("The specified audio file is not supported.");
        ex.printStackTrace();
        } 
        catch (LineUnavailableException ex) 
        {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } 
        catch (IOException ex) 
        {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    public void newGame()
    {
        switch(gameType)
        {
        case NBA:
            shotClockDuration = 1000 * 24;
            gameDuration = 1000 * 60 * 12;
            break;
        case FIBA:
            shotClockDuration = 1000 * 24;
            gameDuration = 1000 * 60 * 10;
            break;
        case COLLEGE:
            shotClockDuration = 1000 * 30;
            gameDuration = 1000 * 60 * 20;
            break;
        case HIGH_SCHOOL:
            shotClockDuration = 1000 * 35;
            gameDuration = 1000 * 60 * 16;
            break;
        case THREExTHREE:
            shotClockDuration = 1000 * 12;
            gameDuration = 1000 * 60 * 10;
            break;
        }
        
        shotClockTime = shotClockDuration;
        remainingTime = gameDuration;
        possession = 0;
        overtime = false;
        setPeriodLabel();
        updateClockLabels();
            
    }

    public void overtime()
    {
        switch(gameType)
        {
        case NBA:
            shotClockDuration = 1000 * 24;
            break;
        case FIBA:
            shotClockDuration = 1000 * 24;
            break;
        case COLLEGE:
            shotClockDuration = 1000 * 30;
            break;
        case HIGH_SCHOOL:
            shotClockDuration = 1000 * 35;
            break;
        case THREExTHREE:
            shotClockDuration = 1000 * 12;
            break;
        }
        pause();
        gameDuration = 1000 * 60 * 5;     // 5 minutes
        shotClockTime = shotClockDuration;
        remainingTime = gameDuration;
        overtime = true;
        setPeriodLabel();
        updateClockLabels();
        
    
    }

    private void setPeriodLabel()
    {
        String text = " PERIOD ";        
        if( !overtime )
        {
            switch(gameType)
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
    
    public long getRemainingTime()
    {
        return remainingTime;
    }
    
    

    public long getShotClockTime()
    {
        return shotClockTime;
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

    public int getPossession()
    {
        return possession;
    }

    public int getPeriod()
    {
        return period;
    }

    public boolean isOvertime()
    {
        return overtime;
    }

    public void setOvertime(boolean overtime)
    {
        this.overtime = overtime;
    }

    public ScoreboardMain.GameType getGameType()
    {
        return gameType;
    }

    public void setGameType(ScoreboardMain.GameType gameType)
    {
        this.gameType = gameType;
    }
     
    
    
    
    // getters and setters
    
    
    
    
}