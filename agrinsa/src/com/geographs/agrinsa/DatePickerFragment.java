package com.geographs.agrinsa;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	Fragmentvisita fv;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		//return new DatePickerDialog(getActivity(), this, year, month, day);
		return new DatePickerDialog(getActivity(), fv, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {

		
	}

	public void setFv(Fragmentvisita fv) {
		this.fv = fv;
	}
	
	
	
}