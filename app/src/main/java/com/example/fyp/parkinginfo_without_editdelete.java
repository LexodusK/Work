package com.example.fyp;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


import static android.support.constraint.Constraints.TAG;


public class parkinginfo_without_editdelete extends BottomSheetDialogFragment  {
    //set main activity as the listener
    Context context;
    private BottomSheetListener mListener;
    private ParkingDetails mParkingDetails;

    //    private TextInputLayout textInputTitle;
//    private TextInputLayout textInputDescription;

    /*-----------    This is to call the addparkinginfo window to add details      ---------------*/

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for the fragment
        View v = inflater.inflate(R.layout.parkinginfo_without_editdelete, container, false);
        TextView mTexttitle = v.findViewById(R.id.varying_parkinglottitlewo);
        TextView mTextDescription = v.findViewById(R.id.varying_parkinglotinfowo);
        TextView mTextPSpaces = v.findViewById(R.id.varying_parkinglotspaceswo);
        TextView mTextPType = v.findViewById(R.id.varying_parkinglottypewo);

        mParkingDetails = (ParkingDetails) getArguments().getSerializable("key");

        //set the details into the bottomsheetlistener textview
        mTexttitle.setText(mParkingDetails.getTitle());
        mTextDescription.setText(mParkingDetails.getDescription());
        mTextPSpaces.setText(mParkingDetails.getNumberofParkingspots());
        mTextPType.setText(mParkingDetails.getTypeofParking());

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
        this.context=context;

        //context is the activity which attaches the dialog
        try {
            mListener = (BottomSheetListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }
}