package com.example.passwordmanagementapp.view

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.adapter.ListAdapter
import com.example.passwordmanagementapp.databinding.FragmentListBinding
import com.example.passwordmanagementapp.model.PassModel
import com.example.passwordmanagementapp.util.ListClickListener
import com.example.passwordmanagementapp.viewModel.ListViewModel
import com.google.firebase.auth.FirebaseAuth

class ListFragment : Fragment(), ListClickListener {

    private lateinit var dataBinding: FragmentListBinding
    private lateinit var viewModel: ListViewModel

    private var listAdapter =  ListAdapter(arrayListOf())
    private var passList = ArrayList<PassModel>()

    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return  dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        dataBinding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        listAdapter = ListAdapter(passList)
        dataBinding.recyclerView.adapter = listAdapter
        dataBinding.clickListener = this

        viewModel.getData(requireContext())

        val toolbar: Toolbar = dataBinding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)



        val signOutButton = dataBinding.signOutButton

        signOutButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setTitle("Çıkış Yap")
            alertDialog.setMessage("Çıkış yapmak istediğinize emin misiniz?")
            alertDialog.setNegativeButton("Hayır", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
            alertDialog.setPositiveButton("Evet", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    auth.signOut()
                    val action = ListFragmentDirections.actionListFragmentToLoginFragment()
                    Navigation.findNavController(it).navigate(action)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }
        viewModel.passList.observe(viewLifecycleOwner){
            listAdapter.updatePassList(it)
        }

        val currentUi = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentUi ==  Configuration.UI_MODE_NIGHT_YES){
            dataBinding.imageButton.setImageResource(R.drawable.add)
        }else{
            dataBinding.imageButton.setImageResource(R.drawable.add_black)
        }
    }

    override fun addClickListener(v: View) {
        val action = ListFragmentDirections.actionListFragmentToDetailFragment("addButtonClick", "bos")
        Navigation.findNavController(v).navigate(action)
    }

    override fun goGenerateClickListener(v: View) {
        val action = ListFragmentDirections.actionListFragmentToGenerateFragment()
        Navigation.findNavController(v).navigate(action)
    }

    override fun hashingClickListener(v: View) {
        val action = ListFragmentDirections.actionListFragmentToHashingFragment()
        Navigation.findNavController(v).navigate(action)
    }
}