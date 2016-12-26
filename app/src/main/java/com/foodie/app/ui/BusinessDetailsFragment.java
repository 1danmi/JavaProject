package com.foodie.app.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.entities.Business;
import com.github.jorgecastilloprz.FABProgressCircle;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailsFragment extends Fragment {

    private String mName, mAddress, mPhone, mWebsite, mEmail;
    private boolean mEditMode;
    private TextView mNameText, mAddressText, mPhoneText, mWebsiteText, mEmailText;
    RelativeLayout mNameLayout, mAddresslayout, mPhoneLayout, mWebsiteLayout, mEmailLayout, rootLayout;
    private FABProgressCircle addFAB, editFAB;
    FloatingActionButton addButton, editButton;
    private static Business businessItem;
    private static final String BUSINESS_ID = "businessId";
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
        final View rootView = inflater.inflate(R.layout.fragment_business_details, container, false);

        initializeViews(rootView);

        mEditMode = false;

        setFABs(rootView);
        int businessID = 0;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessID = bundle.getInt(BUSINESS_ID, 0);
        }

        if (businessID != 0) {
            for (Business business : BusinessActivity.businessList) {
                if (business.get_ID() == businessID) {
                    businessItem = business;
                    break;
                }
            }
        }
        if (businessItem != null) {
            mNameText.setText(businessItem.getBusinessName());
            mAddressText.setText(businessItem.getBusinessAddress());
            mPhoneText.setText(businessItem.getBusinessPhoneNo());
            mWebsiteText.setText(businessItem.getBusinessWebsite());
            mEmailText.setText(businessItem.getBusinessEmail());
        }


        return rootView;

    }


    private void setFABs(View rootView) {
        addFAB = (FABProgressCircle) rootView.findViewById(R.id.add_fab);
        editFAB = (FABProgressCircle) rootView.findViewById(R.id.edit_fab);
        addButton = (FloatingActionButton) rootView.findViewById(R.id.add_fab_button);
        editButton = (FloatingActionButton) rootView.findViewById(R.id.edit_fab_button);


        // Setup up active buttons
        if (mEditMode) {
            addFAB.setVisibility(View.VISIBLE);
            editFAB.setVisibility(View.GONE);

        } else {
            addFAB.setVisibility(View.GONE);
            editFAB.setVisibility(View.VISIBLE);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootLayout, "I\'m editFAB", Snackbar.LENGTH_LONG).show();
                addFAB.setVisibility(View.VISIBLE);
                editFAB.setVisibility(View.GONE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootLayout, "I\'m editFAB", Snackbar.LENGTH_LONG).show();
                addFAB.setVisibility(View.GONE);
                editFAB.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initializeViews(View rootView) {
        rootLayout = (RelativeLayout) rootView.findViewById(R.id.root_business_details_layout);
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
            if (mName != null) {
                input.setText(mName);
            }

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (input.getText().toString().length() != 0) {
                                mName = input.getText().toString().trim();
                                mNameText.setText(mName);
                            } else {
                                if (mNameText.getText().toString().length() == 0) {
                                    Snackbar snackbar = Snackbar.make(v, "Business name must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Fix",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setName(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.red));
                                    snackbar.show();
                                } else {
                                    Snackbar.make(v, "Business name must contains at least one character!", Snackbar.LENGTH_LONG).show();
                                }
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
        if(mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business address:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            if (mAddress != null) {
                input.setText(mAddress);
            }

            input.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (input.getText().toString().length() != 0) {
                                mAddress = input.getText().toString().trim();
                                mAddressText.setText(mAddress);
                            } else {
                                if (mAddressText.getText().toString().length() == 0) {
                                    Snackbar snackbar = Snackbar.make(v, "Business address must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Fix",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setName(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.red));
                                    snackbar.show();
                                } else {
                                    Snackbar.make(v, "Business address must contains at least one character!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        }else{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + mAddressText.getText().toString()));
            startActivity(intent);
        }
    }

    // On clicking address button
    public void setPhone(final View v) {
        if(mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business phone number:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            if (mPhone != null) {
                input.setText(mPhone);
            }

            input.setInputType(InputType.TYPE_CLASS_PHONE);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (input.getText().toString().length() != 0) {
                                mPhone = input.getText().toString().trim();
                                mPhoneText.setText(mPhone);
                            } else {
                                if (mPhoneText.getText().toString().length() == 0) {
                                    Snackbar snackbar = Snackbar.make(v, "Business phone number must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Fix",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setName(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.red));
                                    snackbar.show();
                                } else {
                                    Snackbar.make(v, "Business phone number must contains at least one character!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        }else{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mPhoneText.getText().toString()));
            startActivity(intent);
        }
    }

    public void setWebsite(final View v) {
        if(mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business website:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            if (mWebsite != null) {
                input.setText(mWebsite);
            }

            input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (input.getText().toString().length() != 0) {
                                mWebsite = input.getText().toString().trim();
                                mWebsiteText.setText(mWebsite);
                            } else {
                                if (mWebsiteText.getText().toString().length() == 0) {
                                    Snackbar snackbar = Snackbar.make(v, "Business website must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Fix",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setName(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.red));
                                    snackbar.show();
                                } else {
                                    Snackbar.make(v, "Business website must contains at least one character!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        }else{
            if(mWebsiteText.getText().toString().length()>0){
                String url = mWebsiteText.getText().toString();
                if (!url.startsWith("https://") && !url.startsWith("http://")){
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

//                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
//                builder.setToolbarColor(Color.parseColor(TOOLBAR_COLOR)).setShowTitle(true);
//                prepareMenuItems(builder);
//                prepareActionButton(builder);
//                prepareBottombar(builder);
            }
        }
    }

    public void setEmail(final View v) {
        if(mEditMode) {
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            alert.setTitle("Enter business email:");
            alert.setPositiveButton("OK", null);
            alert.setNegativeButton("Cancel", null);
            // Create EditText box to input repeat number
            final EditText input = new EditText(getContext());
            if (mEmail != null) {
                input.setText(mEmail);
            }

            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            input.setTextColor(getResources().getColor(R.color.white));

            alert.setView(input, 60, 0, 60, 0);
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (input.getText().toString().length() != 0) {
                                mEmail = input.getText().toString().trim();
                                mEmailText.setText(mEmail);
                            } else {
                                if (mEmailText.getText().toString().length() == 0) {
                                    Snackbar snackbar = Snackbar.make(v, "Business email must contains at least one character!", Snackbar.LENGTH_LONG).setAction("Fix",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setName(v);
                                                }
                                            });
                                    snackbar.setActionTextColor(getResources().getColor(R.color.red));
                                    snackbar.show();
                                } else {
                                    Snackbar.make(v, "Business email must contains at least one character!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            alert.show();
        }else{
            String emailAddress = mEmailText.getText().toString().toLowerCase();
            Intent emailIntent = new Intent (Intent.ACTION_SEND );
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailAddress});
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }


}
