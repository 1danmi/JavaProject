package com.foodie.app.ui;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.constants.Constants;
import com.foodie.app.entities.Business;
import com.github.jorgecastilloprz.FABProgressCircle;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailsFragment extends Fragment {

    private static final String TAG = "BusinessDetailsFragment";

    private String mName, mAddress, mPhone, mWebsite, mEmail;
    protected boolean mEditMode;
    private TextView mNameText, mAddressText, mPhoneText, mWebsiteText, mEmailText;
    private RelativeLayout mNameLayout, mAddresslayout, mPhoneLayout, mWebsiteLayout, mEmailLayout;
    private CoordinatorLayout rootLayout;
    private FABProgressCircle addFAB, editFAB;
    private FloatingActionButton addButton, editButton;
    protected static Business businessItem;
    private static final String BUSINESS_ID = "businessId";
    private static final String EDIT_MODE = "mEditKey";
    private CardView businessLogoCardView;
    private ImageView businessLogoHeader;
    private View parent;
    private View snackBarView;


    //Fragment requires empty public constructor
    public BusinessDetailsFragment() {
        // Required empty public constructor
//        setEnterTransition(R.anim.slide_up);
//        setExitTransition(R.anim.slide_down);
    }

    //Called when the fragment initializes.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_business_details, container, false);



        initializeViews(rootView);

        inflateData();

        setFABs(rootView);

        parent = getActivity().findViewById(R.id.activities_activity_layout);

        businessLogoHeader = (ImageView) parent.findViewById(R.id.business_header_image);

        businessLogoCardView = (CardView) parent.findViewById(R.id.business_header_card_view);

        businessLogoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditMode) {
                    pickImage();
                }
            }
        });




        return rootView;

    }

    public void pickImage() {
        Log.d(TAG, "pickImage: starts");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.PICK_PHOTO);
    }

    //Inflates business data from the bundle and from the database.
    public void inflateData() {
        String businessID = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessID = bundle.getString(BUSINESS_ID,"");
            String edit = bundle.getString(EDIT_MODE, "false");
            if (edit.equals("true")) {
                mEditMode = true;
            } else {
                mEditMode = false;
            }
        }

        businessItem = ((ActivitiesActivity) getActivity()).businessItem;

        if (businessItem != null && !businessItem.get_ID().equals("")) {
            mNameText.setText(businessItem.getBusinessName());
            mAddressText.setText(businessItem.getBusinessAddress());
            mPhoneText.setText(businessItem.getBusinessPhoneNo());
            mWebsiteText.setText(businessItem.getBusinessWebsite());
            mEmailText.setText(businessItem.getBusinessEmail());
        }
    }

    //Configure the edit and confirm buttons
    private void setFABs(final View rootView) {
        addFAB = (FABProgressCircle) rootView.findViewById(R.id.add_fab);
        editFAB = (FABProgressCircle) rootView.findViewById(R.id.edit_fab);
        addButton = (FloatingActionButton) rootView.findViewById(R.id.add_fab_button);
        editButton = (FloatingActionButton) rootView.findViewById(R.id.edit_fab_button);


        // Setup up active buttons
        if (mEditMode) {
            addFAB.setVisibility(View.GONE);
            editFAB.setVisibility(View.GONE);

        } else {
            addFAB.setVisibility(View.GONE);
            editFAB.setVisibility(View.GONE);
        }

//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mEditMode = true;
//                addFAB.setVisibility(View.VISIBLE);
//                editFAB.setVisibility(View.GONE);
//            }
//        });
//
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mEditMode = false;
//                if (inputCheck(rootLayout)) {
//                    if (businessItem.equals("")) {
//                        //businessItem.set_ID(Business.businessID + 1);
//                        //Business.businessID++;
//                        CallBack<Business> callBack = new CallBack<Business>() {
//                            @Override
//                            public void run(DataStatus status, List<Business> data) {
//                                DebugHelper.Log("Business insert callBack finish with status: " + status);
//                            }
//                        };
//                        (new AsyncData<Business>(getContext(), Business.getURI(), DataManagerType.Insert, callBack)).execute(businessItem.toContentValues());
//
//                        addFAB.setVisibility(View.GONE);
//                        editFAB.setVisibility(View.VISIBLE);
//                    } else {
//                        CallBack<Business> callBack = new CallBack<Business>() {
//                            @Override
//                            public void run(DataStatus status, List<Business> data) {
//                                DebugHelper.Log("Business insert callBack finish with status: " + status);
//                            }
//                        };
//                        (new AsyncData<Business>(getContext(), Business.getURI(), DataManagerType.Update, callBack)).execute(businessItem.toContentValues());
//
//                        addFAB.setVisibility(View.GONE);
//                        editFAB.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        });
    }

    protected void setSnackBarView(View view){
        snackBarView = view;

    }

    protected boolean inputCheck() {

        if (((ActivitiesActivity) getActivity()).isPhotoChanged) {
            if (businessItem.getBusinessName().length() > 0) {
                if (businessItem.getBusinessAddress().length() > 0) {
                    if (businessItem.getBusinessEmail().length() > 0) {
                        if (businessItem.getBusinessPhoneNo().length() > 0) {
                            if (businessItem.getBusinessWebsite()
                                    .length() > 0) {
                                return true;
                            } else {
                                Snackbar.make(snackBarView, "You have to set a business website", Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Snackbar.make(snackBarView, "You have to set a business phone number", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(snackBarView, "You have to set a business email", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(snackBarView, "You have to set a business address", Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(snackBarView, "You have to set a business name", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(snackBarView, "You have to choose an image", Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    //Initialize the views
    public void initializeViews(View rootView) {
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.root_business_details_layout);
        mNameLayout = (RelativeLayout) rootView.findViewById(R.id.name_layout);
        mNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName(view);
            }
        });
        mAddresslayout = (RelativeLayout) rootView.findViewById(R.id.address_layout);
        mAddresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddress(v);
            }
        });
        mPhoneLayout = (RelativeLayout) rootView.findViewById(R.id.phone_layout);
        mPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhone(v);
            }
        });
        mWebsiteLayout = (RelativeLayout) rootView.findViewById(R.id.website_layout);
        mWebsiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWebsite(v);
            }
        });
        mEmailLayout = (RelativeLayout) rootView.findViewById(R.id.email_layout);
        mEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmail(v);
            }
        });
        mNameText = (TextView) rootView.findViewById(R.id.set_name);
        mAddressText = (TextView) rootView.findViewById(R.id.set_address);
        mPhoneText = (TextView) rootView.findViewById(R.id.set_phone);
        mWebsiteText = (TextView) rootView.findViewById(R.id.set_website);
        mEmailText = (TextView) rootView.findViewById(R.id.set_email);
    }

    // On clicking name button
    public void setName(final View v) {
        if (mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business name:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            mName = mNameText.getText().toString().trim();
            if (mName != null) {
                input.setText(mName);
            }

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mName = input.getText().toString().trim();
                            if (mName.length() > 0) {
                                mNameText.setText(mName);
                                businessItem.setBusinessName(mName);
                                ActivitiesActivity activitiesActivity = (ActivitiesActivity) getActivity();
                                ((TextView) activitiesActivity.findViewById(R.id.business_header_name)).setText(mName);
                            } else {
                                Snackbar snackbar = Snackbar.make(snackBarView, "Business name must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Try again",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                setName(v);
                                            }
                                        });
                                snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                snackbar.show();

                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        }
    }

    // On clicking address button
    public void setAddress(final View v) {
        if (mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business address:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            mAddress = mAddressText.getText().toString().trim();
            final EditText input = new EditText(getContext());
            mAddress = mAddressText.getText().toString().trim();
            if (mAddress != null) {
                input.setText(mAddress);
            }

            input.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            input.setTextColor(getResources().getColor(R.color.white));
            input.setHorizontallyScrolling(false);


            input.setLines(2);

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mAddress = input.getText().toString().trim();
                            if (mAddress.length() > 0) {
                                mAddressText.setText(mAddress);
                                businessItem.setBusinessAddress(mAddress);
                            } else {
                                Snackbar snackbar = Snackbar.make(snackBarView, "Address name must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Try again",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                setAddress(v);
                                            }
                                        });
                                snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                snackbar.show();

                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + mAddressText.getText().toString()));
            startActivity(intent);
        }
    }

    // On clicking address button
    public void setPhone(final View v) {
        if (mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business phone number:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            mPhone = mPhoneText.getText().toString().trim();
            if (mPhone != null) {
                input.setText(mPhone);
            }

            input.setInputType(InputType.TYPE_CLASS_PHONE);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mPhone = input.getText().toString().trim();
                            if (mPhone.length() > 0) {
                                Pattern pattern =
                                        Pattern.compile("^(0\\d{1,2}-?\\d{7})$$");
                                Matcher matcher =
                                        pattern.matcher(mPhone);
                                if (matcher.find()) {
                                    mPhoneText.setText(mPhone);
                                    businessItem.setBusinessPhoneNo(mPhone);
                                } else {
                                    Snackbar snackbar = Snackbar.make(snackBarView, "Wrong phone number!", Snackbar.LENGTH_LONG).setAction("Try again",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setPhone(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                    snackbar.show();
                                }
                            } else {

                                Snackbar snackbar = Snackbar.make(snackBarView, "Phone number must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Try again",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                setName(v);
                                            }
                                        });
                                snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                snackbar.show();

                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mPhoneText.getText().toString()));
            startActivity(intent);
        }
    }

    // On clicking website button
    public void setWebsite(final View v) {
        if (mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business website:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            mWebsite = mWebsiteText.getText().toString().trim().toLowerCase();
            if (mWebsite != null) {
                input.setText(mWebsite);
            }

            input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mWebsite = input.getText().toString().trim().toLowerCase();
                            if (mWebsite.length() > 0) {
                                Pattern pattern =
                                        Pattern.compile("^((?:https\\:\\/\\/)|(?:http\\:\\/\\/)|(?:www\\.))?([a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{1,3}(?:\\??)[a-zA-Z0-9\\-\\._\\?\\,\\'\\/\\\\\\+&%\\$#\\=~]+)$");
                                Matcher matcher =
                                        pattern.matcher(mWebsite);
                                if (matcher.find()) {
                                    mWebsiteText.setText(mWebsite);
                                    businessItem.setBusinessWebsite(mWebsite);
                                } else {
                                    Snackbar snackbar = Snackbar.make(snackBarView, "Wrong website address!", Snackbar.LENGTH_LONG).setAction("Try again",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setWebsite(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                    snackbar.show();
                                }
                            } else {

                                Snackbar snackbar = Snackbar.make(snackBarView, "Website address must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Try again",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                setName(v);
                                            }
                                        });
                                snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                snackbar.show();

                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        } else {
            if (mWebsiteText.getText().toString().length() > 0) {
                String url = mWebsiteText.getText().toString();
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(getResources().getColor(R.color.primary)).setShowTitle(true);
                builder.setStartAnimations(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(getActivity(), R.anim.slide_in_left, R.anim.slide_out_right);
                builder.setCloseButtonIcon(
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
            }
        }
    }

    // On clicking email button
    public void setEmail(final View v) {
        if (mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business email:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            mEmail = mEmailText.getText().toString().trim().toLowerCase();
            if (mEmail != null) {
                input.setText(mEmail);
            }

            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mEmail = input.getText().toString().trim().toLowerCase();
                            if (mEmail.length() > 0) {
                                Pattern pattern =
                                        Pattern.compile("^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$");
                                Matcher matcher =
                                        pattern.matcher(mEmail);
                                if (matcher.find()) {
                                    mEmailText.setText(mEmail);
                                    businessItem.setBusinessEmail(mEmail);
                                } else {
                                    Snackbar snackbar = Snackbar.make(snackBarView, "Wrong email address!", Snackbar.LENGTH_LONG).setAction("Try again",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setName(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                    snackbar.show();
                                }
                            } else {

                                Snackbar snackbar = Snackbar.make(snackBarView, "Email address must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Try again",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                setEmail(v);
                                            }
                                        });
                                snackbar.setActionTextColor(getResources().getColor(R.color.primary));
                                snackbar.show();

                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        } else {
            String emailAddress = mEmailText.getText().toString().toLowerCase();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Snackbar.make(snackBarView, "No picture was selected", Snackbar.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                bmp = Bitmap.createScaledBitmap(bmp, 256, 256, true);
                businessLogoHeader.setImageBitmap(bmp);
                businessLogoHeader.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] logo = stream.toByteArray();
                businessItem.setBusinessLogo(logo);
                ((ActivitiesActivity) getActivity()).isPhotoChanged = true;
//                isPhotoChanged = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}
