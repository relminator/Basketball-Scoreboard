package net.phatcode.rel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    private Timer timer;
    private long remainingTime; // QT time () 12 mins.

    private long lastUpdateTime; // When count was last updated
    
    private long elapsed;
    private long shotClockTime; // Quarter time (12 mins)
    
    private int size;
    private Font numberFont;
    private Font calcFont;
    
    private Color textColor = Color.RED;
    private Color numColor = Color.GREEN;
    
    
    public CountdownTimer( int delayPerTick,
                           long duration, 
                           int size )
    {
        this.size = size;
        this.remainingTime = duration;
        
        shotClockTime = (1000 * 24);
        this.setLayout(new BorderLayout());
        
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
                    shotClockTime = (1000 * 24);
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
                    timer.stop();
                }
                                        
                updateClockLabels();
                
            }
        });
        
        northPanel = new JPanel();
        westPanel = new JPanel();
        centerPanel = new JPanel();
        eastPanel = new JPanel();
        southPanel = new JPanel();
            
        toolBar = new JToolBar();
        
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
        
        updateClockLabels();
        
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
    
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
        lblLeftBall.setVisible(false);
        
        
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
                shotClockTime = (1000 * 24);
                remainingTime += elapsed;  //fix timer to MULTIPLES OF 24:00
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
                lblLeftBall.setVisible(true);
                lblRightBall.setVisible(false);
            }
        });

        rightButton.setToolTipText("Change Ball Possession");
        rightButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                lblRightBall.setVisible(true);
                lblLeftBall.setVisible(false);
            }
        });

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
      // Restore the time we're counting down from and restart the timer.
      lastUpdateTime = System.currentTimeMillis();
      timer.start(); // Start the timer
    }

    private void pause() 
    {
      // Subtract elapsed time from the remaining time and stop timing
      long now = System.currentTimeMillis();
      remainingTime -= (now - lastUpdateTime);
      timer.stop(); // Stop the timer
    }
    
    public void scored()
    {
        shotClockTime = (1000 * 24);
        remainingTime += elapsed;  //fix timer to MULTIPLES OF 24:00
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
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/buzzer.wav");
        try
        {
            AudioInputStream buzzerAudioInputStream = AudioSystem.getAudioInputStream(is);
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
     
    
    
    
    
}