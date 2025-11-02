//package com.example.authapp
//
//import android.app.AlertDialog
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.authapp.data.AppDatabase
//import com.example.authapp.data.Contact
//import com.example.mycontact.R
//import com.example.mycontact.databinding.ActivityMainBinding
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var prefManager: PrefManager
//    private lateinit var db: AppDatabase
//    private lateinit var adapter: ContactAdapter
//
//    private var contacts: MutableList<Contact> = mutableListOf()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        prefManager = PrefManager.getInstance(this)
//        checkLoginStatus()
//
//        val loggedInUsername = prefManager.getUsername()
//        binding.txtUsername.text = "Login sebagai: $loggedInUsername"
//
//        // Inisialisasi database
//        db = AppDatabase.get(this)
//
//        // RecyclerView setup
//        adapter = ContactAdapter(contacts,
//            onEdit = { contact -> showEditDialog(contact) },
//            onDelete = { contact -> deleteContact(contact) }
//        )
//        binding.recyclerContacts.layoutManager = LinearLayoutManager(this)
//        binding.recyclerContacts.adapter = adapter
//
//        // Tombol Tambah
//        binding.btnAdd.setOnClickListener { showAddDialog() }
//
//        // Tombol Logout
//        binding.btnLogout.setOnClickListener {
//            prefManager.setLoggedIn(false)
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
//
//        // Tombol Clear
//        binding.btnClear.setOnClickListener {
//            prefManager.clear()
//            finish()
//        }
//
//        loadContacts()
//    }
//
//    private fun checkLoginStatus() {
//        if (!prefManager.isLoggedIn()) {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
//    }
//
//    private fun loadContacts() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val list = db.contactDao().getAll()
//            runOnUiThread {
//                contacts.clear()
//                contacts.addAll(list)
//                adapter.notifyDataSetChanged()
//            }
//        }
//    }
//
//    private fun showAddDialog() {
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.form_contact, null)
//        val edtName = dialogView.findViewById<EditText>(R.id.edt_name)
//        val edtPhone = dialogView.findViewById<EditText>(R.id.edt_phone)
//
//        AlertDialog.Builder(this)
//            .setTitle("Tambah Kontak")
//            .setView(dialogView)
//            .setPositiveButton("Simpan") { _, _ ->
//                val name = edtName.text.toString()
//                val phone = edtPhone.text.toString()
//                if (name.isNotEmpty() && phone.isNotEmpty()) {
//                    val contact = Contact(name = name, phone = phone)
//                    insertContact(contact)
//                }
//            }
//            .setNegativeButton("Batal", null)
//            .show()
//    }
//
//    private fun showEditDialog(contact: Contact) {
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.form_contact, null)
//        val edtName = dialogView.findViewById<EditText>(R.id.edt_name)
//        val edtPhone = dialogView.findViewById<EditText>(R.id.edt_phone)
//        edtName.setText(contact.name)
//        edtPhone.setText(contact.phone)
//
//        AlertDialog.Builder(this)
//            .setTitle("Edit Kontak")
//            .setView(dialogView)
//            .setPositiveButton("Update") { _, _ ->
//                val updated = contact.copy(
//                    name = edtName.text.toString(),
//                    phone = edtPhone.text.toString()
//                )
//                updateContact(updated)
//            }
//            .setNegativeButton("Batal", null)
//            .show()
//    }
//
//    private fun insertContact(contact: Contact) {
//        CoroutineScope(Dispatchers.IO).launch {
//            db.contactDao().insert(contact)
//            loadContacts()
//        }
//    }
//
//    private fun updateContact(contact: Contact) {
//        CoroutineScope(Dispatchers.IO).launch {
//            db.contactDao().update(contact)
//            loadContacts()
//        }
//    }
//
//    private fun deleteContact(contact: Contact) {
//        CoroutineScope(Dispatchers.IO).launch {
//            db.contactDao().delete(contact)
//            loadContacts()
//        }
//    }
//}

package com.example.authapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authapp.data.AppDatabase
import com.example.authapp.data.Contact
import com.example.mycontact.R
import com.example.mycontact.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private lateinit var db: AppDatabase
    private lateinit var adapter: ContactAdapter

    private var contacts: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        val loggedInUsername = prefManager.getUsername()
        val lastLoginTime = prefManager.getLastLoginTime()
        binding.txtUsername.text = "Login sebagai: $loggedInUsername\nTerakhir login: $lastLoginTime"

        // Inisialisasi database
        db = AppDatabase.get(this)

        // RecyclerView setup
        adapter = ContactAdapter(
            contacts,
            onEdit = { contact -> showEditDialog(contact) },
            onDelete = { contact -> deleteContact(contact) }
        )
        binding.recyclerContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerContacts.adapter = adapter

        // Tombol Tambah
        binding.btnAdd.setOnClickListener { showAddDialog() }

        // Tombol Logout → tidak hapus akun, cuma ubah status login
        binding.btnLogout.setOnClickListener {
            prefManager.setLoggedIn(false)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tombol Clear → reset data kontak di database (bukan hapus akun)
        binding.btnClear.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Semua Kontak?")
                .setMessage("Yakin ingin menghapus semua kontak?")
                .setPositiveButton("Ya") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        db.clearAllTables()
                        loadContacts()
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        loadContacts()
    }

    private fun checkLoginStatus() {
        if (!prefManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = db.contactDao().getAll()
            runOnUiThread {
                contacts.clear()
                contacts.addAll(list)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.form_contact, null)
        val edtName = dialogView.findViewById<EditText>(R.id.edt_name)
        val edtPhone = dialogView.findViewById<EditText>(R.id.edt_phone)

        AlertDialog.Builder(this)
            .setTitle("Tambah Kontak")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val name = edtName.text.toString()
                val phone = edtPhone.text.toString()
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    val contact = Contact(name = name, phone = phone)
                    insertContact(contact)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(contact: Contact) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.form_contact, null)
        val edtName = dialogView.findViewById<EditText>(R.id.edt_name)
        val edtPhone = dialogView.findViewById<EditText>(R.id.edt_phone)
        edtName.setText(contact.name)
        edtPhone.setText(contact.phone)

        AlertDialog.Builder(this)
            .setTitle("Edit Kontak")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updated = contact.copy(
                    name = edtName.text.toString(),
                    phone = edtPhone.text.toString()
                )
                updateContact(updated)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun insertContact(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            db.contactDao().insert(contact)
            loadContacts()
        }
    }

    private fun updateContact(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            db.contactDao().update(contact)
            loadContacts()
        }
    }

    private fun deleteContact(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            db.contactDao().delete(contact)
            loadContacts()
        }
    }
}
