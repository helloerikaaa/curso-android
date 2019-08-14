package com.tec.android.navigationdrawer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeContentFragment extends Fragment {


  private static final String TEXT = "text";

  public static HomeContentFragment newInstance(String text) {
    HomeContentFragment frag = new HomeContentFragment();

    Bundle args = new Bundle();
    args.putString(TEXT, text);
    frag.setArguments(args);

    return frag;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
          Bundle savedInstanceState) {
    View layout = inflater.inflate(R.layout.home_fragment, container, false);

    if (getArguments() != null) {
      ((TextView) layout.findViewById(R.id.text)).setText(getArguments().getString(TEXT));
    }

    return layout;
  }
}

