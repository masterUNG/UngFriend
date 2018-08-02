package masterung.androidthai.in.th.ungfriend.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import masterung.androidthai.in.th.ungfriend.MainActivity;
import masterung.androidthai.in.th.ungfriend.R;
import masterung.androidthai.in.th.ungfriend.utility.FriendAdapter;
import masterung.androidthai.in.th.ungfriend.utility.MyConstant;
import masterung.androidthai.in.th.ungfriend.utility.ReadAllDataFromServer;

public class ServiceFragment extends Fragment {

    private String[] loginStrings;

    public static ServiceFragment serviceInstant(String[] loginStrings) {
        ServiceFragment serviceFragment = new ServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("Login", loginStrings);
        serviceFragment.setArguments(bundle);
        return serviceFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginStrings = getArguments().getStringArray("Login");
        Log.d("2AugV2", "Name ==> " + loginStrings[1]);

//        Create Toolbar
        createToolbar();

//        Create RecyclerView
        createRecyclerView();

//        Post Controller
        postController();

    }   // Main Method

    private void postController() {
        Button button = getView().findViewById(R.id.btnPost);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void createRecyclerView() {

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewFriend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        MyConstant myConstant = new MyConstant();
        String[] columnNameStrings = myConstant.getColumnNameStrings();

        try {

            ReadAllDataFromServer readAllDataFromServer = new ReadAllDataFromServer(getActivity());
            readAllDataFromServer.execute(myConstant.getUrlReadAllUser());
            String jsonString = readAllDataFromServer.get();
            Log.d("2AugV2", "JSON at Service ==> " + jsonString);

            ArrayList<String> iconStringArrayList = new ArrayList<>();
            ArrayList<String> nameStringArrayList = new ArrayList<>();
            ArrayList<String> postStringArrayList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                iconStringArrayList.add(jsonObject.getString(columnNameStrings[4]));
                nameStringArrayList.add(jsonObject.getString(columnNameStrings[1]));
                postStringArrayList.add(jsonObject.getString(columnNameStrings[5]));

                Log.d("2AugV3", "icon ==> " + iconStringArrayList.toString());
                Log.d("2AugV3", "name ==> " + nameStringArrayList.toString());
                Log.d("2AugV3", "post ==> " + postStringArrayList.toString());


            }

            FriendAdapter friendAdapter = new FriendAdapter(getActivity(), iconStringArrayList,
                    nameStringArrayList, postStringArrayList);
            recyclerView.setAdapter(friendAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarService);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Service");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Welcome " + loginStrings[1]);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        return view;
    }
}
