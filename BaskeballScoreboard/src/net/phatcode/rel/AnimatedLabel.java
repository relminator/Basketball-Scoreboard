/********************************************************************
 *  AnimatedLabel.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;


public class AnimatedLabel extends JLabel implements ActionListener
{
    /**
     * 
     */
    private static final long serialVersionUID = 1225959267916588816L;
    private Timer timer;
    private boolean blinkMe = true;
    private String myText;
    private String doubleText;
    private int scroller;
    
    public AnimatedLabel( int delay, String text )
    {
        this.myText = text + "   ";
        this.doubleText = this.myText + this.myText;
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
    
        blinkMe = !blinkMe;
        scroller += 1;
        if( scroller >= myText.length() ) scroller = 0;
            setText(doubleText.substring(scroller, scroller+myText.length()-1));
        
        repaint();
    }

    public void setMyText(String myText)
    {
        this.myText = myText + "   ";
        this.doubleText = this.myText + this.myText;
    }
    
    
}