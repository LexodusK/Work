package com.example.fyp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class parkinginfo_with_edit_delete extends BottomSheetDialogFragment {
    //set main activity as the listener
    private BottomSheetListener mListener;
    private ParkingDetails mParkingDetails;
    //    private TextInputLayout textInputTitle;
//    private TextInputLayout textInputDescription;

    /*-----------    This is to call the addparkinginfo window to add details      ---------------*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for the fragment
        View v = inflater.inflate(R.layout.parkinginfo_with_edit_delete, container, false);
//        textInputTitle = v.findViewById(R.id.Title);
//        textInputDescription = v.findViewById(R.id.Description);
        Button mEditParking = v.findViewById(R.id.button_editparkinglotinfo);
        Button mDeleteParking = v.findViewById(R.id.button_deleteparkinglotinfo);
        TextView mText = v.findViewById(R.id.varying_parkinglottitle);

//        Fragment fragment = v.findViewById(R.id.addParkingDetailsForm);
        mParkingDetails = (ParkingDetails) getArguments().getSerializable("key");


        mText.setText(mParkingDetails.getTitle());

        // change text
        // save GPS and proceed to open fragment to save details of parking
        mEditParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need longlat from mainactivity then save it to the database along
                // with additional details

                dismiss();
            }
        });

        // cancel the choice
        mDeleteParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
//                Toast.makeText(getActivity(), "222222", Toast.LENGTH_SHORT).show();
            }
        });

        return v;

    }

    public interface BottomSheetListener {

        //void onButtonClicked(String text); to pass any string in mListener.onButtonClicked
        //to change to text in Main activity
//        void onButtonClicked3();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context is the activity which attaches the dialog
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }
}