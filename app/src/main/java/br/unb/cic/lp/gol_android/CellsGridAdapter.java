package br.unb.cic.lp.gol_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import br.unb.cic.lp.gol.Cell;

/**
* Created by Hugo on 17/10/16.
*/

public class CellsGridAdapter extends BaseAdapter {
    private Context context;
    private List<Cell> cells;
    private CellClickListener listener;

    public CellsGridAdapter(Context context) {
        this.context = context;
    }

    public void setCellClickListener(CellClickListener listener){
        this.listener = listener;
    }

    public void setCells( List<Cell> cells){
        this.cells = cells;
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cell, null);
        }

        final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.cell_checkbox);

        Cell cell = cells.get(position);
        checkbox.setChecked(cell.isAlive());

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.onCheckCell(position, b);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return cells.size();
    }

    @Override
    public Object getItem(int position) {
        return cells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface CellClickListener{
        void onCheckCell(int position, boolean checked);
    }
}
