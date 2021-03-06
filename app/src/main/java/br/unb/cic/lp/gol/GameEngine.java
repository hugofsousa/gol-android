package br.unb.cic.lp.gol;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um ambiente (environment) do jogo GameOfLife.
 * 
 * Essa implementacao eh nao inifinita, ou seja, nem todas as celulas possuem
 * oito celulas vizinhas. Por exemplo, a celula de coordenada (0,0) possui
 * apenas tres celulas vizinhas, (0,1), (1,0) e (1,1).
 * 
 * Um ambiente eh representado como um array bidimensional de celulas, com
 * altura (height) e comprimento (width).
 * 
 * @author rbonifacio
 */
public abstract class GameEngine {
	protected int height;
	protected int width;
	protected Cell[][] cells;
	protected Statistics statistics;

	private List<Cell[][]> historyCells = new ArrayList<>();
	private List<Statistics> historyStatistics = new ArrayList<>();

	public final static int CONWAY = 0;
	public final static int HIGH_LIFE = 1;
	public final static int LIVE_FREE_OR_DIE = 2;

	/**
	 * Construtor da classe Environment.
	 * 
	 * @param height
	 *            dimensao vertical do ambiente
	 * @param width
	 *            dimentsao horizontal do ambiente
	 */
	public GameEngine(int height, int width, Statistics statistics) {
		this.height = height;
		this.width = width;

		cells = new Cell[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				cells[i][j] = new Cell();
			}
		}
		
		this.statistics = statistics;
    }

	/**
	 * Calcula uma nova geracao do ambiente. Essa implementacao utiliza o
	 * algoritmo do Conway, ou seja:
	 * 
	 * a) uma celula morta com exatamente tres celulas vizinhas vivas se torna
	 * uma celula viva.
	 * 
	 * b) uma celula viva com duas ou tres celulas vizinhas vivas permanece
	 * viva.
	 * 
	 * c) em todos os outros casos a celula morre ou continua morta.
	 */
	public void nextGeneration() {
        historyCells.add(cloneCells(cells));
        historyStatistics.add(Statistics.clone(statistics));

		List<Cell> mustRevive = new ArrayList<Cell>();
		List<Cell> mustKill = new ArrayList<Cell>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (shouldRevive(i, j)) {
					mustRevive.add(cells[i][j]);
				} 
				else if ((!shouldKeepAlive(i, j)) && cells[i][j].isAlive()) {
					mustKill.add(cells[i][j]);
				}
			}
		}
		
		for (Cell cell : mustRevive) {
			cell.revive();
			statistics.recordRevive();
		}
		
		for (Cell cell : mustKill) {
			cell.kill();
			statistics.recordKill();
		}
	}

	/**
	 * Método para clonar a matriz de celulas
	 *
	 * @param cells matriz que deve ser clonada
	 */
	public Cell[][] cloneCells(Cell[][] cells) {
		if (cells == null)
			return null;
		Cell[][] result = new Cell[height][width];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				result[i][j] = cells[i][j].clone();
			}
		}
		return result;
	}

	/**
	 * Retorna uma quantidade específica de gerações
	 *
	 * @param quantity quantidade de gerações à retornar
	 */
	public void returnGenerations(int quantity){
		for (int i = 0 ; i < quantity ; i++){
            cells = historyCells.remove(historyCells.size() - 1);
            statistics = historyStatistics.remove(historyStatistics.size() - 1);
		}

    }
	
	/**
	 * Torna a celula de posicao (i, j) viva
	 * 
	 * @param i posicao vertical da celula
	 * @param j posicao horizontal da celula
	 * 
	 * @throws InvalidParameterException caso a posicao (i, j) nao seja valida.
	 */
	public void makeCellAlive(int i, int j) throws InvalidParameterException {
		if(validPosition(i, j)) {
			cells[i][j].revive();
			statistics.recordRevive();
		}
		else {
			new InvalidParameterException("Invalid position (" + i + ", " + j + ")" );
		}
	}
	
	/**
	 * Verifica se uma celula na posicao (i, j) estah viva.
	 * 
	 * @param i Posicao vertical da celula
	 * @param j Posicao horizontal da celula
	 * @return Verdadeiro caso a celula de posicao (i,j) esteja viva.
	 * 
	 * @throws InvalidParameterException caso a posicao (i,j) nao seja valida. 
	 */
	public boolean isCellAlive(int i, int j) throws InvalidParameterException {
		if(validPosition(i, j)) {
			return cells[i][j].isAlive();
		}
		else {
			throw new InvalidParameterException("Invalid position (" + i + ", " + j + ")" );
		}
	}

	/**
	 * Retorna o numero de celulas vivas no ambiente. 
	 * Esse metodo eh particularmente util para o calculo de 
	 * estatisticas e para melhorar a testabilidade.
	 * 
	 * @return  numero de celulas vivas.
	 */
	public int numberOfAliveCells() {
		int aliveCells = 0;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(isCellAlive(i,j)) {
					aliveCells++;
				}
			}
		}
		return aliveCells;
	}

	/* verifica se uma celula deve ser mantida viva */
	protected abstract boolean shouldKeepAlive(int i, int j);

	/* verifica se uma celula deve (re)nascer */
	protected abstract boolean shouldRevive(int i, int j);

	/*
	 * Computa o numero de celulas vizinhas vivas, dada uma posicao no ambiente
	 * de referencia identificada pelos argumentos (i,j).
	 */
	protected int numberOfNeighborhoodAliveCells(int i, int j) {
		int alive = 0;
		for (int line = i - 1; line <= i + 1; line++) {
            for (int column = j - 1; column <= j + 1; column++) {
                int x = line;
                int y = column;
                if (line == -1) {
					x = height - 1;
				}
                else if (line == height) {
					x = 0;
				}
                if (column == -1) {
					y = width - 1;
				}
                else if (column == width) {
					y = 0;
				}
                if (validPosition(x, y) && (!(line == i && column == j)) && (cells[x][y].isAlive())) {
                    alive++;
                }
            }
        }
		return alive;
	}

	/*
	 * Verifica se uma posicao (a, b) referencia uma celula valida no tabuleiro.
	 */
	protected boolean validPosition(int a, int b) {
		return a >= 0 && a < height && b >= 0 && b < width;
	}

	public List<Cell> generateCellsList(){
		List<Cell> cellsList = new ArrayList<>();
		for(int i = 0 ; i < height ; i++)
			for (int j = 0 ; j < width ; j++)
				cellsList.add(cells[i][j]);

		return cellsList;
	}

	/* Metodos de acesso as propriedades height e width */
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

    public int historySize(){
        return historyCells.size();
    }

    public void displayStatistics(){
        statistics.display();
    }
}
