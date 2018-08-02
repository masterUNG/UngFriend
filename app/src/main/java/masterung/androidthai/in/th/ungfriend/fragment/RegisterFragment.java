package masterung.androidthai.in.th.ungfriend.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import masterung.androidthai.in.th.ungfriend.MainActivity;
import masterung.androidthai.in.th.ungfriend.R;
import masterung.androidthai.in.th.ungfriend.utility.MyAlert;
import masterung.androidthai.in.th.ungfriend.utility.MyConstant;
import masterung.androidthai.in.th.ungfriend.utility.UploadNewUserToServer;

public class RegisterFragment extends Fragment {

    private Uri uri;
    private ImageView imageView;
    private boolean aBoolean = true;
    private String nameString, userString, passwordString, namePhotoString;
    private String pathString = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Photo Controller
        photoController();


    }   // Main Method

    public class UploadListener implements FTPDataTransferListener{
        @Override
        public void started() {
            Toast.makeText(getActivity(), "Start Upload", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void transferred(int i) {
            Toast.makeText(getActivity(), "Process Upload", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void completed() {
            Toast.makeText(getActivity(), "Success Upload", Toast.LENGTH_SHORT).show();
            updateTextToServer();
        }

        @Override
        public void aborted() {

        }

        @Override
        public void failed() {
            Toast.makeText(getActivity(), "Cannot Upload", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTextToServer() {

//        Find Name of Photo
        namePhotoString = pathString.substring(pathString.lastIndexOf("/"));
        namePhotoString = "http://androidthai.in.th/pae/UngPhoto" + namePhotoString;
        Log.d("2AugV1", "namePhoto ==> " + namePhotoString);

        try {

            MyConstant myConstant = new MyConstant();
            UploadNewUserToServer uploadNewUserToServer = new UploadNewUserToServer(getActivity());
            uploadNewUserToServer.execute(nameString, userString, passwordString, namePhotoString,
                    myConstant.getUrlAddUserString());
            String resultString = uploadNewUserToServer.get();
            Log.d("2AugV1", "Result ==> " + resultString);

            if (Boolean.parseBoolean(resultString)) {
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getActivity(), "Error Cannot Register", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemUpload) {
            uploadProcess();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void uploadProcess() {

        MyAlert myAlert = new MyAlert(getActivity());
        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText userEditText = getView().findViewById(R.id.edtUser);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);

        nameString = nameEditText.getText().toString().trim();
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();


//        Check Image and Text
        if (aBoolean) {
//            Non Choose Image
            myAlert.normalDialog("Non Choose Photo", "Please Choose Photo");
        } else if (nameString.isEmpty() || userString.isEmpty() || passwordString.isEmpty()) {
//            Have Space
            myAlert.normalDialog("Have Space", "Please Fill All Blank");
        } else {
//            No Space
            uploadPhotoToServer();
        }


    }   // uploadProcess

    private void uploadPhotoToServer() {

//        Find Path Photo

        String[] strings = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, strings,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            pathString = cursor.getString(index);
        } else {
            pathString = uri.getPath();
        }

        Log.d("2AugV1", "PathPhoto ==> " + pathString);

//        Change Policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        Upload Photo to Server
        FTPClient ftpClient = new FTPClient();
        File file = new File(pathString);
        MyConstant myConstant = new MyConstant();


        try {

            ftpClient.connect(myConstant.getHostFtpString(), myConstant.getPortFtpAnInt());
            ftpClient.login(myConstant.getUserFtpString(), myConstant.getPasswordFtpString());
            ftpClient.setType(FTPClient.TYPE_BINARY);
            ftpClient.changeDirectory("UngPhoto");
            ftpClient.upload(file, new UploadListener());

        } catch (Exception e) {
            e.printStackTrace();
            try {
                ftpClient.disconnect(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }





    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_register, menu);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            uri = data.getData();
            aBoolean = false;

            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));

                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 800, 600, true);

                imageView.setImageBitmap(bitmap1);


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(getActivity(), "Please Choose Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void photoController() {
        imageView = getView().findViewById(R.id.imvPhoto);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose App"), 1);
            }
        });
    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Register");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Please Fill All Every Blank");
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });

        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}
