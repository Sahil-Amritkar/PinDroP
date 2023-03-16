package com.example.pindrop.classes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pindrop.R

private const val TAG = "GroupsAdapter"
class GroupsAdapter(val context: Context, val UserGroups: MutableList<Group>, val onClickListener: OnClickListener) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_group_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userGroup = UserGroups[position]
        holder.itemView.setOnClickListener{
            Log.i(TAG, "Tapped on position $position")
            onClickListener.onItemClick(position)
        }
        val textViewTitle = holder.itemView.findViewById<TextView>(R.id.tvGroupName)
        textViewTitle.text = userGroup.name
    }

    override fun getItemCount() = UserGroups.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}