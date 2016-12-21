package com.foodie.app.ui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodie.app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailsFragment extends Fragment {


    private TextView mNameText, mAddressText, mPhoneText, mWebsiteText, mEmailText;
    private FloatingActionButton addFAB, editFAB;
    private long mRepeatTime;
    //private Business mBusiness;
    private String mName;
    private String mAddress;
    private String mPhone;
    private String mWebsite;
    private String mEmail;
    private String mEditMode;


    // Values for orientation change
    private static final String KEY_NAME = "name_key";
    private static final String KEY_ADDRESS = "address_key";
    private static final String KEY_PHONE = "phone_key";
    private static final String KEY_WEBSITE = "website_key";
    private static final String KEY_EMAIL = "email_key";
    private static final String KEY_MODE = "MODE";


    public BusinessDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_business_details, container, false);

        mNameText = (TextView) rootView.findViewById(R.id.set_name);
        mAddressText = (TextView) rootView.findViewById(R.id.set_address);
        mPhoneText = (TextView) rootView.findViewById(R.id.set_phone);
        mWebsiteText = (TextView) rootView.findViewById(R.id.set_website);
        mEmailText = (TextView) rootView.findViewById(R.id.set_email);

        addFAB = (FloatingActionButton) rootView.findViewById(R.id.add_fab);
        editFAB = (FloatingActionButton) rootView.findViewById(R.id.edit_fab);

        mEditMode = "false";
        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedName = savedInstanceState.getString(KEY_NAME);
            mNameText.setText(savedName);
            mName = savedName;

            String savedAddress = savedInstanceState.getString(KEY_ADDRESS);
            mAddressText.setText(savedAddress);
            mAddress = savedAddress;

            String savedPhone = savedInstanceState.getString(KEY_PHONE);
            mPhoneText.setText(savedPhone);
            mPhone = savedPhone;

            String saveWebsite = savedInstanceState.getString(KEY_WEBSITE);
            mWebsiteText.setText(saveWebsite);
            mWebsite = saveWebsite;

            String savedEmail = savedInstanceState.getString(KEY_EMAIL);
            mEmailText.setText(savedEmail);
            mEmail = savedEmail;

            mEditMode = savedInstanceState.getString(KEY_MODE);
        }

//        // Setup up active buttons
//        if (mEditMode.equals("true")) {
//            addFAB.setVisibility(View.VISIBLE);
//            editFAB.setVisibility(View.GONE);
//
//        } else if (mEditMode.equals("false")) {
//            addFAB.setVisibility(View.GONE);
//            editFAB.setVisibility(View.VISIBLE);
//        }


        return rootView;

    }


//    // On clicking repeat interval button
//    public void setRepeatNo(View v){
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("Enter Number");
//
//        // Create EditText box to input repeat number
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        alert.setView(input);
//        alert.setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                        if (input.getText().toString().length() == 0) {
//                            mRepeatNo = Integer.toString(1);
//                            mRepeatNoText.setText(mRepeatNo);
//                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
//                        }
//                        else {
//                            mRepeatNo = input.getText().toString().trim();
//                            mRepeatNoText.setText(mRepeatNo);
//                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
//                        }
//                    }
//                });
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // do nothing
//            }
//        });
//        alert.show();
//    }


}
