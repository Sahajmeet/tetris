package com.mx85.main;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class TetrisGame extends JFrame {

    private GameLooper gameLooper;
    private Timer timer;

    private int normalSpeed = 200;
    private int highSpeed = 50;

    private JPanel boardPanel = new JPanel();
    private ResultPanel resultPanel = new ResultPanel();

    private JButton[][] cells = new JButton[20][20];

    public TetrisGame() {
        super("Tetris");
        this.setSize(400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
/*
Authors, Github users: mx85, Zhang-Veni
Commented by: Sahajmeet Bhutta
Commented on: Monday March 6th 2017
*/


package com.mx85.main;

// import relevant libraries 
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

//define main class to create the Tetris game and perform all logic in the game
//also creates the board for players to see, movement of pieces on board, and scoring logic
public class TetrisGame extends JFrame {

/*define all relevant variables
* all private classes and variables are private as opposed to public in order to reduce coupling
*/
    private GameLooper gameLooper; //GameLooper type is defined later in this file, gameLooper is an instance of GameLooper allowing repeated update of the board
    private Timer timer; //this variable will be used to speed up and slow down the speed of the falling tiles

    private int normalSpeed = 200; //the normal falling speed of tiles
    private int highSpeed = 50; //quicker flling speed of tiles

    private JPanel boardPanel = new JPanel(); //variable for theactual frame where the board will be seen
    private ResultPanel resultPanel = new ResultPanel(); //will display the score of the game and the next piece

    private JButton[][] cells = new JButton[20][20]; /*each unit on the screen is a cell and will be used to depict whether 
    * or not the cell has a part of a piece on it, or if there are points to be allotted; implemented as a button 
	*/
    //tetris game constructor
    public TetrisGame() {
        super("Tetris");
        this.setSize(400, 500); //set the size of the board
        this.setVisible(true); //make sure the board is visible
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the game when the x button in the top right is clicked
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel()); //make the user interface metallic-looking
        }
        catch(Exception e) {} //catch some kind of exception

        //set layouts of border and grid
        this.setLayout(new GridLayout()); 

        this.setLayout(new BorderLayout());
        this.add(boardPanel, BorderLayout.CENTER); //centre the board within the border
        this.add(resultPanel, BorderLayout.SOUTH); // display the results near the bottom of the border

        /*a listener on keyboard key presses, with left and right keys controlling the direction of movement of a falling piece
        * (referred to as Shape), up to rotate the orientation of the piece, and down to speed up the rate at which the piece falls
        */
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if(e.getID() == KeyEvent.KEY_LAST) {
                            int key = e.getKeyCode();
                            if(!timer.isRunning())
                                timer.start();
                            switch (key) {
                                case KeyEvent.VK_LEFT:
                                    gameLooper.move(com.mx85.main.Shape.DIRECTION.LEFT); //move the tetris piece left upon left key press
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    gameLooper.move(com.mx85.main.Shape.DIRECTION.RIGHT); //move the tetris piece right upon right key press
                                    break;
                                case KeyEvent.VK_UP:
                                    gameLooper.move(com.mx85.main.Shape.DIRECTION.ROTATE); //rotate the tetris piece upon up key press
                                    break;
                                case KeyEvent.VK_DOWN:
                                    timer.setDelay(highSpeed); //speed up the falling of the tetris piece upon down key press
                                    break;
                                case KeyEvent.VK_P: 
                                    timer.stop(); //pause the game when P key is pressed, as all movement is tied to the timer
                                    break;
                            }
                        }
                        return false;
                    }
                });

        //actually constucting the cells of the board in a 2D 20x20 array cells, making the buttons visible, colouring them opaque light grey, and then actually adding the cells to the board
        boardPanel.setLayout(new GridLayout(20,20));
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[i].length; j++) {
                cells[j][i] = new JButton();
                cells[j][i].setEnabled(false);
                cells[j][i].setBackground(Color.lightGray);
                cells[j][i].setOpaque(true);
                boardPanel.add(cells[j][i]);
            }
        }

       //create a GameLooper, which is this game's update method (see below)
       gameLooper = new GameLooper();
       timer = new Timer(normalSpeed, gameLooper);
       timer.start();
    }
    //java label displaying the score and next tetris piece
    private class ResultPanel extends JPanel {

        private JLabel pointsTextLabel = new JLabel("Points: ");
        private JLabel pointsLabel = new JLabel("0");
        private JLabel nextLabel = new JLabel("Next: ");
        private NextPieceType nextType = new NextPieceType();
        private int points = 0;

        public ResultPanel() {
            setPreferredSize(new Dimension(40, 60));

            //add labels to the panel of scores and next pieces
            this.setLayout(new GridLayout(2, 2));
            this.add(pointsTextLabel);
            this.add(pointsLabel);
            this.add(nextLabel);
            this.add(nextType);
        }

        //choose the kind of piece which will be the next piece to fall
        public void setPieceType(com.mx85.main.Shape.PIECE piece) {
            nextType.setPieceType(piece);
        }

        //choose the kind of piece which will be the next piece to fall
        private class NextPieceType extends JComponent {

            private com.mx85.main.Shape.PIECE nextPiece;

            public void setPieceType(com.mx85.main.Shape.PIECE piece) {
                this.nextPiece = piece;
            }

            /*creating the pieces to be displayed dependent on which piece is the next piece
            * the possible pieces are: cube, long, L-shaped, T-shaped, and Z-shaped
            */
            @Override
            protected void paintComponent(Graphics g) {
                switch (nextPiece) {
                    case CUBEPIECE:
                        paintCubePiece(g);
                        break;
                    case LONGPIECE:
                        paintLongPiece(g);
                        break;
                    case LPIECE:
                        paintLPiece(g);
                        break;
                    case TPIECE:
                        paintTPiece(g);
                        break;
                    case ZPIECE:
                        paintZPiece(g);
                        break;
                }
            }

            /*Once the switch-case chooses a piece type based on the nextPiece 
            * these functions actually draw the pieces
            */
            //draw a red L-shaped piece
            private void paintLPiece(Graphics g) {
                g.setColor(Color.red);
                g.fillRect(0,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(0,20,10,10);
                g.fillRect(10,20,10,10);
            }

            //draw a yellow T-shaped piece
            private void paintTPiece(Graphics g) {
                g.setColor(Color.yellow);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(20,0,10,10);
                g.fillRect(10,10,10,10);

            }

            //draw a blue Z-shaped piece
            private void paintZPiece(Graphics g) {
                g.setColor(Color.blue);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(10,10,10,10);
                g.fillRect(20,10,10,10);
            }

            //draw an orange cube piece
            private void paintCubePiece(Graphics g) {
                g.setColor(Color.orange);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(10,10,10,10);
            }

            //draw a green long piece
            private void paintLongPiece(Graphics g) {
                g.setColor(Color.green);
                g.fillRect(0,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(0,20,10,10);
                g.fillRect(0,30,10,10);
            }
        }
            private com.mx85.main.Shape.PIECE nextPiece;

            public void setPieceType(com.mx85.main.Shape.PIECE piece) {
                this.nextPiece = piece;
            }

            @Override
            protected void paintComponent(Graphics g) {
                switch (nextPiece) {
                    case CUBEPIECE:
                        paintCubePiece(g);
                        break;
                    case LONGPIECE:
                        paintLongPiece(g);
                        break;
                    case LPIECE:
                        paintLPiece(g);
                        break;
                    case TPIECE:
                        paintTPiece(g);
                        break;
                    case ZPIECE:
                        paintZPiece(g);
                        break;
                }
            }

            private void paintLPiece(Graphics g) {
                g.setColor(Color.red);
                g.fillRect(0,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(0,20,10,10);
                g.fillRect(10,20,10,10);
            }

            private void paintTPiece(Graphics g) {
                g.setColor(Color.yellow);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(20,0,10,10);
                g.fillRect(10,10,10,10);

            }

            private void paintZPiece(Graphics g) {
                g.setColor(Color.blue);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(10,10,10,10);
                g.fillRect(20,10,10,10);
            }

            private void paintCubePiece(Graphics g) {
                g.setColor(Color.orange);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(10,10,10,10);
            }

            private void paintLongPiece(Graphics g) {
                g.setColor(Color.green);
                g.fillRect(0,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(0,20,10,10);
                g.fillRect(0,30,10,10);
            }
        }

        public void addPoints(int points) {
            this.points += points;
            pointsLabel.setText(Integer.toString(this.points));
        }
    }

    private class GameLooper implements ActionListener {

        private com.mx85.main.Shape currentShape;
        private com.mx85.main.Shape nextShape;

        public GameLooper() {
            currentShape = PieceFactory.createRandomPiece();
            nextShape = PieceFactory.createRandomPiece();
            resultPanel.setPieceType(nextShape.getPieceType());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dropPiece();
        }

        public void dropPiece() {
            move(com.mx85.main.Shape.DIRECTION.DOWN);
            repaint();
        }

        public void move(com.mx85.main.Shape.DIRECTION direction) {
            switch (direction) {
                case DOWN:
                    if(!currentShape.move(com.mx85.main.Shape.DIRECTION.DOWN, cells)) {
                        currentShape = nextShape;
                        nextShape = PieceFactory.createRandomPiece();
                        resultPanel.setPieceType(nextShape.getPieceType());
                        timer.setDelay(normalSpeed);
                        if(isGameOver(currentShape))
                        {
                        	timer.stop();
                        }
                        checkRows();
                    }
                    break;
                case RIGHT:
                    currentShape.move(com.mx85.main.Shape.DIRECTION.RIGHT, cells);
                    break;
                case LEFT:
                    currentShape.move(com.mx85.main.Shape.DIRECTION.LEFT, cells);
                    break;
                case ROTATE:
                    currentShape.move(com.mx85.main.Shape.DIRECTION.ROTATE, cells);
                    break;
                default:
            }
        }

        private void checkRows() {
            for(int y = 0; y < 20; y++) {
                boolean scored = true;
                for(int x = 0; x < 20; x++) {
                   if(cells[x][y].getBackground().equals(Color.lightGray)) {
                       scored = false;
                   }
                }
                if(scored) {
                    deleteRow(y);
                    y = y - 1;
                    resultPanel.addPoints(10);
                }
            }
        }

        private void deleteRow(int start) {
            for(int y = start; y > 0; y--) {
                for(int x = 0; x < 20; x++) {
                    if(y > 0)
                        cells[x][y].setBackground(cells[x][y-1].getBackground());
                    else
                        cells[x][y].setBackground(Color.lightGray);
                }
            }
        }
        public boolean isGameOver(Shape currentShape)
        {
        	
        	for(int i=0;i<4;i++)
        		for (int j=0;j<4;j++)
        	if (!cells[	currentShape.cords[0][i]][currentShape.cords[1][j]].getBackground().equals(Color.lightGray))
        	{
        		return true;
        	}
        	
        
        	return false;
        }
    }
}
