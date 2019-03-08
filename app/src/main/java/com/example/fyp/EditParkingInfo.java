package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.fyp.MainActivity;
import com.google.firebase.firestore.GeoPoint;


import org.w3c.dom.Text;

import java.lang.reflect.GenericArrayType;

import static android.support.constraint.Constraints.TAG;

public class EditParkingInfo extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    //set main activity as the listener
    private BottomSheetListener mListener;
    private TextInputEditText textInputTitle;
    private TextInputEditText textInputDescription;
    private RadioGroup radioInputParkingLotType;
    private RadioButton radioButton;
    private Spinner spinnerInputParkingSpaces;
    private String text;
    private RadioButton radButOne;
    private RadioButton radButTwo;
    private RadioButton radButThree;
    private RadioButton radButFour;
    private RadioButton radButFive;
    private ParkingDetails mParkingDetails;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for the fragment
        View v = inflater.inflate(R.layout.editparkinginfo, container, false);
        textInputTitle = (TextInputEditText) v.findViewById(R.id.TitleEdit);
        textInputDescription = (TextInputEditText)  v.findViewById(R.id.DescriptionEdit);
        radioInputParkingLotType = v.findViewById(R.id.ParkingLotTypeEdit);
        spinnerInputParkingSpaces = v.findViewById(R.id.ParkingLotSpacesEdit);
        radButOne = v.findViewById(R.id.radio_oneEdit);
        radButTwo = v.findViewById(R.id.radio_twoEdit);
        radButThree = v.findViewById(R.id.radio_threeEdit);
        radButFour = v.findViewById(R.id.radio_fourEdit);
        radButFive = v.findViewById(R.id.radio_fiveEdit);



        mParkingDetails = (ParkingDetails) getArguments().getSerializable("edit");

        Button mConfirmParkingEdit = v.findViewById(R.id.button_confirmEditParkingLot);
        ImageView mCancelParkingAdd = v.findViewById(R.id.button_cancelEditParkingLot);

        textInputTitle.setText(mParkingDetails.getTitle());
        textInputDescription.setText(mParkingDetails.getDescription());
//        mTextPSpaces.setText(mParkingDetails.getNumberofParkingspots());
//        mTextPType.setText(mParkingDetails.getTypeofParking());
//        Fragment fragment = v.findViewById(R.id.addParkingDetailsForm);


//        radButOne.setText("Free Parking Lot");
//        Toast.makeText(getActivity(), "" + radButOne.isChecked(), Toast.LENGTH_SHORT).show();

//        Toast.makeText(getActivity(), "" + radioButton.getText(), Toast.LENGTH_SHORT).show();
        radioInputParkingLotType.check(R.id.radio_oneEdit);
        radioInputParkingLotType.check(R.id.radio_twoEdit);
        radioInputParkingLotType.check(R.id.radio_threeEdit);
        radioInputParkingLotType.check(R.id.radio_fourEdit);
        radioInputParkingLotType.check(R.id.radio_fiveEdit);
        switch (mParkingDetails.getTypeofParking()){
            case "Free Parking lot":
                radButOne.setChecked(true);
                radioButton=radButOne;
                break;
            case "Paid Parking lot":
                radButTwo.setChecked(true);
                radioButton=radButTwo;
                break;
            case "Multistorey Parking lot":
                radButThree.setChecked(true);
                radioButton=radButThree;
                break;
            case "Disabled Parking":
                radButFour.setChecked(true);
                radioButton=radButFour;
                break;
            case "Electric vehicle lot":
                radButFive.setChecked(true);
                radioButton=radButFive;
                break;

        }


//        radButOne.setChecked(true);
//        radioButton = radButOne;


//        Toast.makeText(getActivity(), ""+ radioInputParkingLotType.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onCreateView: " + radioInputParkingLotType.getCheckedRadioButtonId());


        radioInputParkingLotType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                radioButton = (RadioButton) radioGroup.findViewById(checkedId);

                Log.d(TAG, "onCheckedChanged: " + radioInputParkingLotType.getCheckedRadioButtonId());



                Toast.makeText(getActivity(), ""+ radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInputParkingSpaces.setAdapter(adapter);

        spinnerInputParkingSpaces.setSelection(adapter.getPosition(mParkingDetails.getNumberofParkingspots()));
        spinnerInputParkingSpaces.setOnItemSelectedListener(this);
        Log.d(TAG, "onCreateView: parking spot" + mParkingDetails.getNumberofParkingspots());


//        ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) spinnerInputParkingSpaces.getAdapter();
//        spinnerInputParkingSpaces.setSelection(arrayAdapter.getPosition());

/*
        if (mParkingDetails.getNumberofParkingspots().equals("1 - 3")){
            Log.d(TAG, "onCreateView: 1 - 3");
            spinnerInputParkingSpaces.setOnItemSelectedListener(this);
        }else if(mParkingDetails.getNumberofParkingspots().equals("4 - 10")){
            Log.d(TAG, "onCreateView: 4 - 10");
            spinnerInputParkingSpaces.setOnItemSelectedListener(this);
        }else if(mParkingDetails.getNumberofParkingspots().equals("11 - 50")){
            Log.d(TAG, "onCreateView: 11 - 50");
            spinnerInputParkingSpaces.setOnItemSelectedListener(this);
        }else if(mParkingDetails.getNumberofParkingspots().equals("51 – 100")){
            Log.d(TAG, "onCreateView: 51 - 100");
            spinnerInputParkingSpaces.setOnItemSelectedListener(this);
        }else if(mParkingDetails.getNumberofParkingspots().equals("100+")){
            Log.d(TAG, "onCreateView: 100+");
            spinnerInputParkingSpaces.setOnItemSelectedListener(this);
        }*/


//        Resources res = getResources();
//        String [] input = res.getStringArray(R.array.numbers);

       /* switch (mParkingDetails.getNumberofParkingspots()) {
            case  "":
                spinnerInputParkingSpaces.setOnItemSelectedListener(this);
                break;
            case "4 - 10":
                spinnerInputParkingSpaces.setOnItemSelectedListener(this);
                break;
            case "11 - 50":
                spinnerInputParkingSpaces.setOnItemSelectedListener(this);
                break;
            case "51 – 100":
                spinnerInputParkingSpaces.setOnItemSelectedListener(this);
                break;
            case "100+":
                spinnerInputParkingSpaces.setOnItemSelectedListener(this);
                break;
        }*/



        // change text
        // save GPS and proceed to open fragment to save details of parking
        mConfirmParkingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need longlat from mainactivity then save it to the database along
                // with additional details

                confirmInput();
                if (!validateTitle() | !validateDescription()){
                    return;
                }
                mListener.onButtonClickedEdit(textInputTitle.getEditableText().toString(),
                        textInputDescription.getEditableText().toString(),
                        radioButton.getText().toString(),
                        text,
                        mParkingDetails.getMarker_id(),
                        mParkingDetails.getGeo_point());




                //after confirm input, the details saved to that marker will have to go in the array list
                //the array list is passed to the firebase with longlat, parking info, title, description
                //if marker is deleted, it is removed from array list



//                Toast.makeText(getActivity(), "addddd", Toast.LENGTH_SHORT).show();
            }
        });

        // cancel the choice
        mCancelParkingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
//                Toast.makeText(getActivity(), "222222", Toast.LENGTH_SHORT).show();
            }
        });

        return v;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        text = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(adapterView.getContext(), "" + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface BottomSheetListener {
        //void onButtonClicked(String text); to pass any string in mListener.onButtonClicked
        //to change to text in Main activity
        void onButtonClickedEdit(String t, String d, String type, String spaces, String id, GeoPoint point);

    }

    /*//declare interface
    public interface OnDataPass{
         void onDataPass(String data);
    }

    //connect class implementation to interface of fragment to onAttach
    OnDataPass dataPasser;

    //handle passing of data by calling dataPasser object
    public void passData(String data){
        dataPasser.onDataPass(data);
    }*/

    //987-991 MainActivity


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        dataPasser = (OnDataPass) context;
        //context is the activity which attaches the dialog
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }


    private boolean validateTitle(){
        String TitleInput = textInputTitle.getEditableText().toString().trim();

        if(TitleInput.isEmpty()){
            textInputTitle.setError("Field can't be empty");
            return false;
        }else if(TitleInput.length()>25){
            textInputTitle.setError("Title too long");
            return false;
        }
        else{
            textInputTitle.setError(null);
            return true;
        }

    }

    private boolean validateDescription(){
        String DescriptionInput = textInputDescription.getEditableText().toString().trim();

        if(DescriptionInput.isEmpty()){
            textInputDescription.setError("Field can't be empty");
            return false;
        }else{
            textInputDescription.setError(null);
            return true;
        }
    }

    public void confirmInput(){
        //one vertical bar because both must be called; otherwise first will only be false
        if(!validateDescription() | !validateTitle()){

            return;
        }

        String input = "Title: " + textInputTitle.getEditableText().toString();
        input += "\n";
        input += "Description: " + textInputDescription.getEditableText().toString();
        Toast.makeText(getActivity(), input, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "confirmInput: Saved input" + input);
        dismiss();
    }

}


