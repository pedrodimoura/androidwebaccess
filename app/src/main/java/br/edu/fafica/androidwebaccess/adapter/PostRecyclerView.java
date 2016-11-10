package br.edu.fafica.androidwebaccess.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.edu.fafica.androidwebaccess.R;
import br.edu.fafica.androidwebaccess.databinding.FragmentPostItemBinding;
import br.edu.fafica.androidwebaccess.entity.Post;

/**
 * Created by pedrodimoura on 11/10/16.
 */

public class PostRecyclerView extends RecyclerView.Adapter<PostRecyclerView.PostViewHolder> {

    private Post[] mPosts;

    public PostRecyclerView(Post[] posts) {
        this.mPosts = posts;
    }

    public void updateDataSet(Post[] posts) {
        this.mPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        holder.mFragmentPostItemBinding.textViewTitle.setText(this.mPosts[position].getTitle());
        holder.mFragmentPostItemBinding.textViewBody.setText(this.mPosts[position].getBody());

        Log.d("TAG", "Post -> " + this.mPosts[position].toString());


    }

    @Override
    public int getItemCount() {
        return (null != this.mPosts && this.mPosts.length > 0 ? this.mPosts.length : 0);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private FragmentPostItemBinding mFragmentPostItemBinding;

        public PostViewHolder(View itemView) {
            super(itemView);
            mFragmentPostItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}
