package masterung.androidthai.in.th.ungfriend.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import masterung.androidthai.in.th.ungfriend.R;
import masterung.androidthai.in.th.ungfriend.utility.MyAlert;
import masterung.androidthai.in.th.ungfriend.utility.MyConstant;
import masterung.androidthai.in.th.ungfriend.utility.ReadAllDataFromServer;

public class MainFragment extends Fragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Register Controller
        registerController();

//        Login Controller
        loginController();


    }   // Main Method

    private void loginController() {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText userEditText = getView().findViewById(R.id.edtUser);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);

                String userString = userEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();

                if (userString.isEmpty() || passwordString.isEmpty()) {
                    MyAlert myAlert = new MyAlert(getActivity());
                    myAlert.normalDialog("Have Space", "Please Fill User and Password");
                } else {
                    checkUserAndPass(userString, passwordString);
                }


            }
        });
    }

    private void checkUserAndPass(String userString, String passwordString) {

        boolean statusUser = true;
        MyConstant myConstant = new MyConstant();
        String[] columnNameStrings = myConstant.getColumnNameStrings();
        String[] loginStrings = new String[columnNameStrings.length];

        try {

            ReadAllDataFromServer readAllDataFromServer = new ReadAllDataFromServer(getActivity());
            readAllDataFromServer.execute(myConstant.getUrlReadAllUser());
            String jsonString = readAllDataFromServer.get();
            Log.d("2AugV1", "JSON ==> " + jsonString);
            MyAlert myAlert = new MyAlert(getActivity());

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i=0; i<jsonArray.length(); i+=1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (userString.equals(jsonObject.getString(columnNameStrings[2]))) {
                    statusUser = false;
                    for (int i1=0;i1<columnNameStrings.length; i1+=1) {
                        loginStrings[i1] = jsonObject.getString(columnNameStrings[i1]);
                    }

                }

            }

            if (statusUser) {
                myAlert.normalDialog("User False", "No " + userString + " in my Database");
            } else if (passwordString.equals(loginStrings[3])) {
                Toast.makeText(getActivity(), "Welcom " + loginStrings[1], Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragment, ServiceFragment.serviceInstant(loginStrings))
                        .commit();
            } else {
                myAlert.normalDialog("Password False", "Please Try Again Password False");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void registerController() {
        TextView textView = getView().findViewById(R.id.txtRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragment, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}
