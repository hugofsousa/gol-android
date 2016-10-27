package br.unb.cic.lp.gol_android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Calendar;

/**
 * Created by Hugo on 26/10/16.
 */


public class CellsSizePickerDialog extends DialogFragment {

    private static final int MAX_SIZE = 15;
    private SizePickerListener listener;

    public void setListener(SizePickerListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.cells_size_picker_dialog, null);
        final NumberPicker width = (NumberPicker) dialog.findViewById(R.id.width_picker);
        final NumberPicker height = (NumberPicker) dialog.findViewById(R.id.height_picker);

        width.setMinValue(1);
        width.setMaxValue(MAX_SIZE);
        width.setValue(10);

        height.setMinValue(1);
        height.setMaxValue(MAX_SIZE);
        height.setValue(10);

        builder.setView(dialog)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onChooseSize(height.getValue(), width.getValue());
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CellsSizePickerDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface SizePickerListener{
        void onChooseSize(int width, int height);
    }
}
