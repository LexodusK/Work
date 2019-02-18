package com.example.fyp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.fyp.MainActivity;

import static android.support.constraint.Constraints.TAG;

public class AddParkingInfo extends BottomSheetDialogFragment {
    //set main activity as the listener
    private BottomSheetListener mListener;
    private TextInputLayout textInputTitle;
    private TextInputLayout textInputDescription;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for the fragment
        View v = inflater.inflate(R.layout.addparkinginfo, container, false);
        textInputTitle = v.findViewById(R.id.Title);
        textInputDescription = v.findViewById(R.id.Description);



//        ImageView img = v.findViewById(R.id.ic_edit);
        Button btn = v.findViewById(R.id.button_confirmAddParkingLot);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //send variables in onButtonClicked(" ") back to main activity, if string then send the string back
                //if onButtonClicked("Button 1 clicked") then it will change the main activity text
//                if(v != null){
//                    mListener.onButtonClicked("Changed this stupid thing"," dadda");

                   confirmInput();
//                }




            }
        });

        return v;

    }

    public interface BottomSheetListener {

        //void onButtonClicked(String text); to pass any string in mListener.onButtonClicked
        //to change to text in Main activity
        void onButtonClicked(String text, String text1);

      }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context is the activity which attaches the dialog
        try{
            mListener = (BottomSheetListener) context;
        }catch (ClassCastException e){
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

//    public void confirmInput(View v){
//        //one vertical bar because both must be called; otherwise first will only be false
//        if(!validateDescription() | !validateTitle()){
//            return;
//        }
//
//        String input = "Title: " + textInputTitle.getEditText().getText().toString();
//        input += "\n";
//        input += "Description" + textInputDescription.getEditText().getText().toString();
//
//        Log.d(TAG, "confirmInput: Saved input" + input);
//
//    }
//
//
    public void confirmInput(){
        //one vertical bar because both must be called; otherwise first will only be false
        if(!validateDescription() | !validateTitle()){

            return;
        }

        String input = "Title: " + textInputTitle.getEditText().getText().toString();
        input += "\n";
        input += "Description" + textInputDescription.getEditText().getText().toString();
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