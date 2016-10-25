package br.unb.cic.lp.regras;

import br.unb.cic.lp.gol.*;

public class Conway extends GameEngine{

    public Conway(int height, int width, Statistics statistics){
        super(height, width, statistics);
    }

    protected boolean shouldRevive(int i, int j) {
        return (!cells[i][j].isAlive())
                && (numberOfNeighborhoodAliveCells(i, j) == 3);
    }



    protected boolean shouldKeepAlive(int i, int j) {
        return (cells[i][j].isAlive())
                && (numberOfNeighborhoodAliveCells(i, j) == 2 || numberOfNeighborhoodAliveCells(i, j) == 3);
    }

}