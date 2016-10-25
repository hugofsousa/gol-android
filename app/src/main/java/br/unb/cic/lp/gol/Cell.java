package br.unb.cic.lp.gol;

public class Cell {
    private boolean alive;

    public Cell() {
	alive = false;
    }

    public Cell(boolean alive) {
	this.alive = alive; 
    }

    public boolean isAlive() {
	return alive;
    }

    public void kill() {
	this.alive = false;
    }
	
    public void revive() {
	    this.alive = true;
    }

    public Cell clone(){
        return new Cell(this.alive);
    }
}
