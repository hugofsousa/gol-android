package br.unb.cic.lp.gol;

import br.unb.cic.lp.regras.Conway;
import br.unb.cic.lp.regras.HighLife;

public class Main {

	public static void main(String args[]) {
		GameController controller = new GameController();
		
		Statistics statistics = new Statistics();

		GameEngine engine = new HighLife(10, 10, statistics);
		
		GameView board = new GameView(controller, engine);
		
		controller.setBoard(board);
		controller.setEngine(engine);
//		controller.setStatistics(statistics);
		
		controller.start();
	}
}
