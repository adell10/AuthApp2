package com.example.authapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.authapp.data.Contact
import com.example.mycontact.databinding.ItemContactBinding

class ContactAdapter(
    private val contactList: List<Contact>,
    private val onEdit: (Contact) -> Unit,
    private val onDelete: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        with(holder.binding) {
            txtName.text = contact.name
            txtPhone.text = contact.phone

            btnEdit.setOnClickListener { onEdit(contact) }
            btnDelete.setOnClickListener { onDelete(contact) }
        }
    }

    override fun getItemCount(): Int = contactList.size
}
