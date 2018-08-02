package masterung.androidthai.in.th.ungfriend.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import masterung.androidthai.in.th.ungfriend.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    private Context context;
    private List<String> iconStringsList, nameStringList, postStringList;
    private LayoutInflater layoutInflater;

    public FriendAdapter(Context context,
                         List<String> iconStringsList,
                         List<String> nameStringList,
                         List<String> postStringList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.iconStringsList = iconStringsList;
        this.nameStringList = nameStringList;
        this.postStringList = postStringList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.recycler_view_friend, viewGroup, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);

        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder friendViewHolder, int i) {

        String iconString = iconStringsList.get(i);
        String nameString = nameStringList.get(i);
        String postString = postStringList.get(i);

//        Show Text
        friendViewHolder.nameTextView.setText(nameString);
        friendViewHolder.postTextView.setText(postString);

//        Show Image
        Picasso.get()
                .load(iconString)
                .resize(150, 150)
                .into(friendViewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return iconStringsList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView nameTextView, postTextView;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imvIcon);
            nameTextView = itemView.findViewById(R.id.txtName);
            postTextView = itemView.findViewById(R.id.txtPost);


        }
    }


}   // Main Class
