package me.jiangmanhua.suixin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ContactsFragment extends ListFragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private List<String> data;
	
	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types of parameters
	public static ContactsFragment newInstance(String param1, String param2) {
		ContactsFragment fragment = new ContactsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ContactsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

		data=getLocalPhoneContact();
		// TODO: Change Adapter to display your content
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				data));
	}

	private ArrayList<String> getLocalPhoneContact() {
		Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
        String proj1[] = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER , ContactsContract.Contacts.LOOKUP_KEY};

        Cursor curContacts = getActivity().getContentResolver().query(contactsUri, proj1, null, null, null);
        ArrayList<String> ret = new ArrayList<String>();
        if (curContacts.getCount() > 0) {
            while (curContacts.moveToNext()) {
                String allPhoneNo =curContacts.getString(0) ;
                if (curContacts.getInt(1) > 0) {
                    Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    String[] proj2 = { ContactsContract.CommonDataKinds.Phone.NUMBER };
                    // using lookUp key to search the phone numbers
                    String selection = ContactsContract.Data.LOOKUP_KEY + "=?";

                    Cursor cur = getActivity().getContentResolver().query(phoneUri, proj2, selection,
                            new String[] { curContacts.getString(2) }, null);
                    while (cur.moveToNext()) {
                    	allPhoneNo+= " " +(cur.getString(0));
                    }
                }
                ret.add(allPhoneNo);
            }
        }
		return ret;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			mListener
					.onContactsFragmentInteraction(data.get(position).split(" ")[1]);
		}
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onContactsFragmentInteraction(String id);
	}

}
