package br.unb.cic.lp.gol_android;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.unb.cic.lp.gol.Cell;
import br.unb.cic.lp.gol.GameController;
import br.unb.cic.lp.gol.GameEngine;
import br.unb.cic.lp.gol.GameView;
import br.unb.cic.lp.gol.Statistics;
import br.unb.cic.lp.regras.HighLife;

public class PerformanceActivity extends AppCompatActivity
        implements CellsGridAdapter.CellClickListener, GameViewListener {

    private GridView mCellsGrid;

    private GameController controller;
    private Statistics statistics;
    private GameEngine engine;
    private GameView board;

    private int width;
    private int height;

    private boolean canReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        Button nextGenerationButton = (Button) findViewById(R.id.next_generation);

        nextGenerationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.nextGeneration();
            }
        });

        Button returnGenerationButton = (Button) findViewById(R.id.return_generation);

        returnGenerationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(board.canReturn()) controller.returnGenerations(1);
                else Toast.makeText(PerformanceActivity.this,
                        "Não é possível retornar para geração anterior", Toast.LENGTH_LONG).show();
            }
        });

        Button autoButton = (Button) findViewById(R.id.auto);

        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                board.auto = true;
//                TODO CORRIGIR AUTO
//                controller.nextGeneration();
                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                controller.nextGeneration();
                            }
                        });
                    }
                }, 0, 1000);
            }
        });

        Bundle bundle = getIntent().getExtras();
        width = bundle.getInt("WIDTH");
        height = bundle.getInt("HEIGHT");

        controller = new GameController();
        statistics = new Statistics();
        engine = new HighLife(width, height, statistics);
        board = new GameView(controller, engine);

        board.setListener(this);

        controller.setBoard(board);
        controller.setEngine(engine);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mCellsGrid = (GridView) findViewById(R.id.cells_grid);
        mCellsGrid.setColumnWidth(size.x / width);
        mCellsGrid.setNumColumns(width);

        CellsGridAdapter adapter = new CellsGridAdapter(this);
        adapter.setCells(engine.generateCellsList());
        adapter.setCellClickListener(this);

        mCellsGrid.setAdapter(adapter);
    }

    @Override
    public void onCheckCell(int position, boolean checked) {
        if(checked) controller.makeCellAlive((int)position/width, position%width);
    }

    @Override
    public void gameViewUpdate() {
        ((CellsGridAdapter) (mCellsGrid.getAdapter())).setCells(engine.generateCellsList());
    }
}
