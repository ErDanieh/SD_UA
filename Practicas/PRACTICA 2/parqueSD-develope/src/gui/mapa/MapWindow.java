package gui.mapa;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import sc.visitantes.FWQ_Visitor;

public class MapWindow {

	public JFrame point;
	private final JPanel gui = new JPanel(new BorderLayout(0, 0));
	private JButton[][] mapBoardSquares = new JButton[20][20];
	private JPanel mapBoard;
	private final JLabel message = new JLabel("Bienvenido a Disneymal!");
	FWQ_Visitor visitante;

	public MapWindow(FWQ_Visitor visitante) {
		this.visitante = visitante;
		initializeGui();

	}

	public final void initializeGui() {

		mapBoard = new JPanel(new GridLayout(0, 21));
		mapBoard.setBorder(new LineBorder(Color.BLUE));
		gui.add(mapBoard);

		String[][] mapaVisitante = visitante.getMap();

		Insets buttonMargin = new Insets(10, 10, 10, 10);
		for (int ii = 0; ii < mapBoardSquares.length; ii++) {
			for (int jj = 0; jj < mapBoardSquares[ii].length; jj++) {
				JButton b = new JButton();
				b.setMargin(buttonMargin);
				if (mapaVisitante[ii][jj] == "-") {
					b.setBackground(Color.WHITE);
				} else {
					b.setText(mapaVisitante[ii][jj]);
				}

				mapBoardSquares[jj][ii] = b;

			}
		}

		mapBoard.add(new JLabel(""));

		for (int ii = 0; ii < 20; ii++) {
			mapBoard.add(new JLabel("" + ii, SwingConstants.CENTER));
		}

		for (int ii = 0; ii < 20; ii++) {
			mapBoard.add(new JLabel("" + ii, SwingConstants.CENTER));
			for (int jj = 0; jj < 20; jj++) {
				mapBoard.add(mapBoardSquares[jj][ii]);
			}
		}
	}

	public final JComponent getChessBoard() {
		return mapBoard;
	}

	public final JComponent getGui() {
		return gui;
	}

	/**
	 * Devuelve el color del fondo del mapa segun la posicion
	 * 
	 * @param fila Fila
	 * @param col  Columna
	 * @return El color del fondo que corresponde a esa casilla
	 */
	public Color backgroundColorMap(Integer fila, Integer col) {
		Color res;

		if (fila < 10 && col < 10) {
			res = new Color(218, 255, 219);
		} else if (fila < 10 && col >= 10) {
			res = new Color(218, 245, 255);
		} else if (fila >= 10 && col < 10) {
			res = new Color(253, 245, 255);
		} else {
			res = new Color(236, 236, 236);
		}

		return res;
	}

	public void actualizaMapas(FWQ_Visitor visitante, JFrame frame) {

		for (;;) {

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			frame.remove(this.mapBoard);
			frame.add(this.mapBoard);
			frame.revalidate();
			frame.repaint();

			System.out.println("Borrando el Jpanel anterior");
			String[][] mapaVisitante = visitante.getMap();

			for (int ii = 0; ii < mapBoardSquares.length; ii++) {
				for (int jj = 0; jj < mapBoardSquares.length; jj++) {
					this.mapBoardSquares[ii][jj].setBackground(this.backgroundColorMap(ii, jj));
					this.mapBoardSquares[ii][jj].setText(mapaVisitante[ii][jj]);

				}
			}

		}

	}

	public void mostrarVentana(FWQ_Visitor visitante) {

		Runnable r = new Runnable() {

			@Override
			public void run() {
				MapWindow cb = new MapWindow(visitante);

				JFrame f = new JFrame("DisneyMalFrame");
				JToolBar tools = new JToolBar();
				JButton salirParque = new JButton("Salir del parque!");

				tools.setFloatable(true);
				tools.add(salirParque); // TODO - add functionality!

				tools.addSeparator();
				tools.add(message);
				tools.addSeparator();

				f.add(tools);

				cb.point = f;
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setLocationByPlatform(true);

				f.pack();
				f.setMinimumSize(f.getSize());
				f.setVisible(true);

				Thread threadActMap = new Thread() {
					public void run() {

						actualizaMapas(visitante, f);
						try {
							TimeUnit.SECONDS.sleep(3);
						} catch (InterruptedException e) { // TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};

				ActionListener salirParqueListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("BOTON CLICKADO");
						salirParque.setText("Boton clickado!");
						visitante.salirParque();

						System.exit(0);
					}
				};

				f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						visitante.salirParque();
					}
				});

				threadActMap.start();
				salirParque.addActionListener(salirParqueListener);
				;

			}
		};
		SwingUtilities.invokeLater(r);
	}

}