package com.example.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.fyp.MainActivity;
import com.example.fyp.Login;

import static android.support.constraint.Constraints.TAG;

public class AddCancelParkingBottomSheet extends BottomSheetDialogFragment {
    //set main activity as the listener
    private BottomSheetListener mListener;
    private MarkerLocation mMarkerRemove;
    //    private TextInputLayout textInputTitle;
//    private TextInputLayout textInputDescription;

/*-----------    This is to call the addparkinginfo window to add details      ---------------*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for the fragment
        View v = inflater.inflate(R.layout.addcancelparkingbottomsheet, container, false);
//        textInputTitle = v.findViewById(R.id.Title);
//        textInputDescription = v.findViewById(R.id.Description);
        RelativeLayout mAddParking = v.findViewById(R.id.bottom_sheet_add_parking);
        RelativeLayout mCancelParking = v.findViewById(R.id.bottom_sheet_cancel);

        mMarkerRemove = (MarkerLocation) getArguments().getSerializable("marker");

//        Fragment fragment = v.findViewById(R.id.addParkingDetailsForm);

        // change text
        // save GPS and proceed to open fragment to save details of parking
        mAddParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need longlat from mainactivity then save it to the database along
                // with additional details
                AddParkingInfo bottomSheet2 = new AddParkingInfo();
                bottomSheet2.show(getFragmentManager(), "BottomSheetAdd");
                mListener.onDismiss(false);
                dismiss();
            }
        });

        // cancel the choice
        mCancelParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onDismiss(true);
                mMarkerRemove.getMarker().remove();
                dismiss();



//                Toast.makeText(getActivity(), "222222", Toast.LENGTH_SHORT).show();
            }
        });

        return v;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
        mListener.onDismiss(true);
        mMarkerRemove.getMarker().remove();

    }

    public interface BottomSheetListener {
         void onDismiss(boolean bool);


        //void onButtonClicked(String text); to pass any string in mListener.onButtonClicked
        //to change to text in Main activity
//        void onButtonClicked2();

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

