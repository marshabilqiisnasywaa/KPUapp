package com.example.kpuapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kpuapp.database.Note
import com.example.kpuapp.databinding.ItemListBinding

class voterAdapter(
    private val voterList: List<Note>,
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit,
    private val onViewClick: (Int) -> Unit
) : RecyclerView.Adapter<voterAdapter.VoterViewHolder>() {

    class VoterViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            voter: Note,
            position: Int,
            onDeleteClick: (Int) -> Unit,
            onEditClick: (Int) -> Unit,
            onViewClick: (Int) -> Unit
        ) {
            with(binding) {
                itemNumber.text = (position + 1).toString() // Binds position number to itemNumber
                itemName.text = voter.namaVoter // Binds voter name to itemName
                deleteIcon.setOnClickListener { onDeleteClick(position) } // Set delete click
                editIcon.setOnClickListener { onEditClick(position) } // Set edit click
                viewIcon.setOnClickListener { onViewClick(position) } // Set view click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoterViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return voterList.size
    }

    override fun onBindViewHolder(holder: VoterViewHolder, position: Int) {
        holder.bind(voterList[position], position, onDeleteClick, onEditClick, onViewClick)
    }
}
