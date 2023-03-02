package vn.edu.fpt.addressbook;

import android.net.Uri;

import androidx.fragment.app.Fragment;

public class AddEditFragment extends Fragment {
    public interface AddEditFragmentListener {
        void onAddEditCompleted(Uri contactUri);
    }
}
