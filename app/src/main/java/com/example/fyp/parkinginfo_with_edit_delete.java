package com.example.fyp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import static android.support.constraint.Constraints.TAG;


public class parkinginfo_with_edit_delete extends BottomSheetDialogFragment  {
    //set main activity as the listener
    Context context;
    private BottomSheetListener mListener;
    private ParkingDetails mParkingDetails;
    private MarkerLocation mMarkerRemove;
    private FirebaseFirestore mDb;

    //    private TextInputLayout textInputTitle;
//    private TextInputLayout textInputDescription;

    /*-----------    This is to call the addparkinginfo window to add details      ---------------*/

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for the fragment
        View v = inflater.inflate(R.layout.parkinginfo_with_edit_delete, container, false);
        Button mEditParking = v.findViewById(R.id.button_editparkinglotinfo);
        Button mDeleteParking = v.findViewById(R.id.button_deleteparkinglotinfo);
        TextView mTexttitle = v.findViewById(R.id.varying_parkinglottitle);
        TextView mTextDescription = v.findViewById(R.id.varying_parkinglotinfo);
        TextView mTextPSpaces = v.findViewById(R.id.varying_parkinglotspaces);
        TextView mTextPType = v.findViewById(R.id.varying_parkinglottype);
        mDb = FirebaseFirestore.getInstance();
//        Spinner spinner = v.findViewById(R.id.ParkingLotSpaces);


//        Bundle the data from mainactivity and pass to this bottomsheetlistener
        mParkingDetails = (ParkingDetails) getArguments().getSerializable("key");
        mMarkerRemove = (MarkerLocation) getArguments().getSerializable("marker");

        final Bundle bundle = new Bundle();
        bundle.putSerializable("edit", mParkingDetails);

        //set the details into the bottomsheetlistener textview
        mTexttitle.setText(mParkingDetails.getTitle());
        mTextDescription.setText(mParkingDetails.getDescription());
        mTextPSpaces.setText(mParkingDetails.getNumberofParkingspots());
        mTextPType.setText(mParkingDetails.getTypeofParking());
        Log.d(TAG, "onClick: " + mDb.collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid())
                .collection(getString(R.string.collection_parking_details))
                .document(mParkingDetails.getMarker_id()));

       final CollectionReference acoll = mDb.collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid())
                .collection(getString(R.string.collection_parking_details));


        // edit details of clicked parking marker
        mEditParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditParkingInfo bottomSheet = new EditParkingInfo();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getFragmentManager(), "exampleBottomSheet");


                dismiss();
            }
        });

        // delete the marker
        mDeleteParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prompt dialog to really delete, if yes then delete. no then cancel
                   AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setIcon(R.drawable.ic_warning);
                     dialog.setTitle("Are you sure?");
                      dialog.setMessage("This marker will be removed.");

              dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     Toast.makeText(context, "Marker Deleted", Toast.LENGTH_SHORT).show();
                    //delete marker from database
                     Log.d(TAG, "onClick: asd" + mParkingDetails.getTitle() );
                     Log.d(TAG, "onClick: asd" + mParkingDetails.getMarker_id());

                     acoll.document(mParkingDetails.getMarker_id()).delete();
                     mListener.onDelete();

                 }
             });

              dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {

                  }
              });

                   dialog.show();

                dismiss();

            }
        });

        return v;

    }

    public interface BottomSheetListener {
    void onDelete();
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