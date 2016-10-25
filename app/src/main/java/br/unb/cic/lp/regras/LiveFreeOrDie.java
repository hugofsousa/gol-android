package br.unb.cic.lp.regras;

import br.unb.cic.lp.gol.*;

public class LiveFreeOrDie extends GameEngine{

    public LiveFreeOrDie(int height, int width, Statistics statistics){
        super(height, width, statistics);
    }

    protected boolean shouldRevive(int i, int j) {
        return (!cells[i][j].isAlive())
                && ((numberOfNeighborhoodAliveCells(i, j) == 2));
    }



    protected boolean shouldKeepAlive(int i, int j) {
        return (cells[i][j].isAlive())
                && (numberOfNeighborhoodAliveCells(i, j) == 0);
    }

}