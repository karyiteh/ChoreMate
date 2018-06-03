package com.example.teh_k.ChoreMate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecurringTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RecurringTaskFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    // UI elements
    private NumberPicker amountOfTime;
    private Spinner unitOfTime;

    private String spinnerOption;

    public RecurringTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reccuring_task, container, false);
    }

    /**
     * Does final initializing of the items on the fragment.
     * Sets up listeners for items in the fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Gets UI elements from the view.
        amountOfTime = (NumberPicker) getView().findViewById(R.id.number_of_day);
        unitOfTime = (Spinner) getView().findViewById(R.id.unit_of_time);

        // Set minimum and maximum values for the number picker
        amountOfTime.setMinValue(1);
        amountOfTime.setMaxValue(31);

        // Set up listener for NumberPicker object
        amountOfTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                amountOfTime.setValue(picker.getValue());
            }
        });

        // Set up listener for spinner object.
        unitOfTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the item selected.
                spinnerOption = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void getRecurringOptions();
    }

    public int getAmountOfTime() {
        return amountOfTime.getValue();
    }

    public String getSpinnerOption() {
        return spinnerOption;
    }
}
