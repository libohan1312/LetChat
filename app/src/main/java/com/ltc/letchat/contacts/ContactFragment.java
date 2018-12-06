package com.ltc.letchat.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ltc.letchat.MyApplication;
import com.ltc.letchat.R;
import com.ltc.letchat.chat.ChatActivity;


public class ContactFragment extends Fragment {

    ContactListAdapter adapter;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ContactListAdapter();
        adapter.setContactItemClickListener(userId -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("name",userId);
            intent.putExtra("userId",userId);
            startActivity(intent);
        });

        MyApplication.getChatManager().getContacts(contacts -> adapter.setDatas(contacts));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_contact, container, false);

        RecyclerView contactList = (RecyclerView) view.findViewById(R.id.contactlist);
        contactList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        contactList.setAdapter(adapter);
        return view;
    }


}
