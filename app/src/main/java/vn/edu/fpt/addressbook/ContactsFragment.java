package vn.edu.fpt.addressbook;

import android.net.Uri;

import androidx.fragment.app.Fragment;

public class ContactsFragment extends Fragment {
    public interface ContactsFragmentListener {
        void onContactSelected(Uri contactUri);
        void onAddContact();
    }
}
