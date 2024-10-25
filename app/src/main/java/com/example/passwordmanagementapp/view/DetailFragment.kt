package com.example.passwordmanagementapp.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentDetailBinding
import com.example.passwordmanagementapp.util.DetailClickListener
import com.example.passwordmanagementapp.viewModel.DetailViewModel

class DetailFragment : Fragment(), DetailClickListener {

    private lateinit var dataBinding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel

    private var control: String? = null
    private var positionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return  dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            control = DetailFragmentArgs.fromBundle(it).addButtonControl
            positionId = DetailFragmentArgs.fromBundle(it).positionId
        }

        dataBinding.clickListener = this
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        dataBinding.detail = viewModel

        if (control == "addButtonClick"){
            dataBinding.deleteDetailButton.visibility = View.GONE
        }else{
            detailGetData()
        }

        dataBinding.platformNameDetailText.addTextChangedListener {
            it?.let {
                viewModel.detailPlatformName.value = it.toString()
            }
        }

        dataBinding.detailEmailEditText.addTextChangedListener {
            it?.let {
                viewModel.detailEmail.value = it.toString()
            }
        }

        dataBinding.userNameDetailText.addTextChangedListener {
            it?.let {
                viewModel.detailUserName.value = it.toString()
            }
        }

        dataBinding.passwordDetailText.addTextChangedListener {
            it?.let {
                viewModel.detailPassword.value = it.toString()
            }
        }

        dataBinding.webSite.addTextChangedListener {
            it?.let {
                viewModel.detailWebSite.value = it.toString()
            }
        }
    }
    private fun detailGetData() {
        viewModel.getData(positionId.toString())

        viewModel.detailPassList.observe(viewLifecycleOwner){
            it?.let {
                for (i in it){
                    dataBinding.platformNameDetailText.setText(i.platformName)
                    dataBinding.detailEmailEditText.setText(i.email)
                    dataBinding.userNameDetailText.setText(i.userName)
                    dataBinding.passwordDetailText.setText(i.password)
                    dataBinding.webSite.setText(i.webSite)
                }
            }
        }
    }
    override fun detailSaveClickListener(v: View) {
        if(control == "addButtonClick"){
            viewModel.saveData(viewModel.detailPlatformName.value.toString(),viewModel.detailEmail.value.toString(),viewModel.detailUserName.value.toString(),viewModel.detailPassword.value.toString(),
                viewModel.detailWebSite.value.toString(),requireContext())
        }else{
            viewModel.updateData(positionId.toString(),requireContext())
        }
    }

    override fun detailDeleteClickListener(v: View) {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Datayı Sil")
        alertDialog.setMessage("Silmek İstediğinize Emin misiniz?")
        alertDialog.setNegativeButton("Hayır", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }
        })
        alertDialog.setPositiveButton("Evet", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                viewModel.deleteData(positionId.toString(), requireContext(), v)
            }
        })
        alertDialog.create()
        alertDialog.show()
    }

    override fun detailCopyClickListener(v: View) {
        viewModel.copyPassword(requireContext())
    }
}