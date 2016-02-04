/*
 * ******************************************************************************
 *   Copyright (c) 2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */
package com.example.yloon.fypandroidapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.model.Group;

import java.util.Collections;
import java.util.List;


public class GroupRecyclerAdapter extends RecyclerView.Adapter<View_Holder_Group> {

    List<Group> list = Collections.emptyList();
    Context context;

    public GroupRecyclerAdapter(List<Group> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder_Group onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_group, parent, false);
        View_Holder_Group holder = new View_Holder_Group(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder_Group holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.description.setText(list.get(position).groupType);


        //animate(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView
    public void insert(int position, Group data) {
        list.add(position, data);
        notifyItemInserted(position);
    }
    // Remove a RecyclerView item containing the Data object
    public void remove(Group data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

 /*   public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.aniticipate_overshoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }*/

}