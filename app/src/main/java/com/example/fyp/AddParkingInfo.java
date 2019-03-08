package com.example.fyp;

import android.content.Context;
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


import static android.support.constraint.Constraints.TAG;

public class AddParkingInfo extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    //set main activity as the listener
    private BottomSheetListener mListener;
    private TextInputLayout textInputTitle;
    private TextInputLayout textInputDescription;
    private RadioGroup radioInputParkingLotType;
    private RadioButton radioButton;
    private Spinner spinnerInputParkingSpaces;
    private String text;
    private RadioButton radButOne;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for the fragment
        View v = inflater.inflate(R.layout.addparkinginfo, container, false);
         textInputTitle = v.findViewById(R.id.Title);
         textInputDescription = v.findViewById(R.id.Description);
         radioInputParkingLotType = v.findViewById(R.id.ParkingLotType);
         spinnerInputParkingSpaces = v.findViewById(R.id.ParkingLotSpaces);
         radButOne = v.findViewById(R.id.radio_one);



        Button mConfirmParkingAdd = v.findViewById(R.id.button_confirmAddParkingLot);
        ImageView mCancelParkingAdd = v.findViewById(R.id.button_cancelAddParkingLot);
//        Fragment fragment = v.findViewById(R.id.addParkingDetailsForm);


//        radButOne.setText("Free Parking Lot");
//        Toast.makeText(getActivity(), "" + radButOne.isChecked(), Toast.LENGTH_SHORT).show();

//        Toast.makeText(getActivity(), "" + radioButton.getText(), Toast.LENGTH_SHORT).show();
        radioInputParkingLotType.check(R.id.radio_one);
        radButOne.setChecked(true);
        radioButton = radButOne;


//        Toast.makeText(getActivity(), ""+ radioInputParkingLotType.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreateView: " + radioInputParkingLotType.getCheckedRadioButtonId());


        radioInputParkingLotType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                int radioId = radioInputParkingLotType.getCheckedRadioButtonId();
//                radioButton = view.findViewById(radioId);
//                if (radioButton.getText() != null){
//                    radioButton = radButOne;
//                }
//                radioGroup.check(checkedId);

                radioButton = (RadioButton) radioGroup.findViewById(checkedId);

                Log.d(TAG, "onCheckedChanged: " + radioInputParkingLotType.getCheckedRadioButtonId());



                Toast.makeText(getActivity(), ""+ radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                                    R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInputParkingSpaces.setAdapter(adapter);
        spinnerInputParkingSpaces.setOnItemSelectedListener(this);


        // change text
        // save GPS and proceed to open fragment to save details of parking
        mConfirmParkingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need longlat from mainactivity then save it to the database along
                // with additional details

                confirmInput();
                if (!validateTitle() | !validateDescription()){
                 return;
                }
                mListener.onButtonClicked(textInputTitle.getEditText().getText().toString(),
                                          textInputDescription.getEditText().getText().toString(),
                                          radioButton.getText().toString(),
                                          text);




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
        void onButtonClicked(String t, String d, String type, String spaces);

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
        String TitleInput = textInputTitle.getEditText().getText().toString().trim();

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
        String DescriptionInput = textInputDescription.getEditText().getText().toString().trim();

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

        String input = "Title: " + textInputTitle.getEditText().getText().toString();
        input += "\n";
        input += "Description: " + textInputDescription.getEditText().getText().toString();
        Toast.makeText(getActivity(), input, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "confirmInput: Saved input" + input);
        dismiss();
    }














}



//    Button btn = (Button) findViewById(R.id.button_confirmAddParkingLot);
//
//                    btn.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//
//        if(!validateDescription() | !validateTitle()){
//        return;
//        }
//
//        String input = "Title: " + textInputTitle.getEditText().getText().toString();
//        input += "\n";
//        input += "Description" + textInputDescription.getEditText().getText().toString();
//        Toast.makeText(MainActivity.this, input, Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "confirmInput: Saved input" + input);
//
//
//        }
//        });
