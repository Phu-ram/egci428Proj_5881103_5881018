package com.example.project_personaldoctor.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_personaldoctor.Model.Medicines
import com.example.project_personaldoctor.R
//import com.example.project_personaldoctor.Interface.ItemClicklistener

class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)/*,View.OnClickListener,View.OnLongClickListener*/ {

    var txtName: TextView
    var txtCommercial: TextView
    var txtDiagonose: TextView

//    private var itemClickListener: ItemClickListener? = null
//
//    fun setItemClickListener(itemClickListener: ItemClickListener){
//        this.itemClickListener = itemClickListener
//    }
//
//
//    override fun onClick(v: View?) {
//        itemClickListener!!.onClick(v,adapterPosition,false)
//    }
//
//    override fun onLongClick(v: View?): Boolean {
//        itemClickListener!!.onClick(v,adapterPosition,true)
//        return true
//    }

    init {

        txtName = itemView.findViewById(R.id.txtName)
        txtCommercial = itemView.findViewById(R.id.txtCommercial)
        txtDiagonose = itemView.findViewById(R.id.txtDiag)


    }



    class FeedAdapter(private val drugsObject: List<Medicines>, private val mContext: Context) :
        RecyclerView.Adapter<FeedViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {

            val itemView = inflater.inflate(R.layout.row, parent, false)
            return FeedViewHolder(itemView)
        }

        private val inflater: LayoutInflater

        init {
            inflater = LayoutInflater.from(mContext)
        }


        override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
            holder.txtName.text= drugsObject[position].Name
            holder.txtCommercial.text = drugsObject[position].Commercial
            holder.txtDiagonose.text = drugsObject[position].Detail


        }

        override fun getItemCount(): Int {
            return drugsObject.size
        }


    }
}
