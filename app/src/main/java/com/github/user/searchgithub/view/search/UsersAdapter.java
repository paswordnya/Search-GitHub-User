package com.github.user.searchgithub.view.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.user.searchgithub.R;
import com.github.user.searchgithub.model.Users;
import com.github.user.searchgithub.view.detail.DetailUserGithub;

import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<Users> items;
    private Context context;

    // passed into the constructor
    UsersAdapter(Context context, List<Users> items) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_git_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Users item = items.get(position);
        holder.textViewName.setText(item.getLogin());
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(position, item.getLogin());
//                Intent feedViewIntent = new Intent(context,DetailUserGithub.class);
//                context.startActivity(feedViewIntent);
            }
        });
        Glide.with(context).load(item.getAvatarUrl()).asBitmap().centerCrop().placeholder(R.drawable.loading_image).into(new BitmapImageViewTarget(holder.imageViewAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imageViewAvatar.setImageDrawable(circularBitmapDrawable);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textViewBio;
        final TextView textViewName;
        final ImageView imageViewAvatar;
        final ConstraintLayout constraintLayout;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.imageview_userprofilepic);
            textViewName = (TextView) itemView.findViewById(R.id.textview_username);
            textViewBio = (TextView) itemView.findViewById(R.id.textview_user_profile_info);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout);
        }


    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position, String username);
    }
}
