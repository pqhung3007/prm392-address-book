package vn.edu.fpt.addressbook;

import android.net.Uri;

import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {
    public interface DetailFragmentListener {
        void onEditContact(Uri contactUri);
        void onContactDeleted();
    }
}
