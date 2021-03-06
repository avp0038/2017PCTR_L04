package p012;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("serial")
public class Billiards extends JFrame {

	private static ExecutorService e;
	
	private int contaBolas=0;
	
	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;

	private final int N_BALL = 4 + 3;
	private Ball[] balls;

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
		
	}

	/**
	 * inicializa el array de bolas.
	 * 
	 */
	private void initBalls() {
		balls=new Ball[N_BALL];
		for (int i = 0; i < N_BALL; i++) {
			balls[i]=new Ball();
		}
	}

	/**
	 * clase que se ejecuta al accionar el boton lanzar.
	 *
	 */
	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			e=Executors.newCachedThreadPool();
			if(contaBolas < N_BALL){
				for(int i=0;i<N_BALL;i++){
					e.submit(new Hilo(balls[i]));
					contaBolas ++;
				}
			}else{
				System.out.println("no se pueden lanzar mas bolas");
			}
		}
	}

	/**
	 * Clase que contiene los hilos que se van a lanzar.
	 * 
	 * @author Daniel Delgado Santamria
	 * @author Adrian Varona Puente
	 *
	 */
	private class Hilo implements Runnable{
		private Ball mibola;
		
		public Hilo(Ball bola){
			mibola=bola;
		}
		
		/**
		 * mueve las bolas.
		 *   
		 */
		@Override
		public void run() {
			boolean bandera=true;
			while(bandera){
				mibola.move();
				mibola.reflect();
				board.setBalls(balls);
				board.repaint();
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					bandera=false;
					System.out.println("Hilo detenido");
					contaBolas=0;
				}
			}
		}
	}
	
	/**
	 * se ejecuta cuando pulsas el boton de parar.
	 * 
	 */
	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when stop button is pushed
			
			e.shutdownNow();
		}
	}

	/**
	 * inicia el billar.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Billiards();
	}
}