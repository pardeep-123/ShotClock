package com.app.shotclock.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.app.shotclock.R
import com.app.shotclock.adapters.EditProfileImagesAdapter
import com.app.shotclock.databinding.FragmentEditProfileBinding
import com.app.shotclock.utils.ImagePickerUtility1
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.longToDate
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList


class EditProfileFragment : ImagePickerUtility1<FragmentEditProfileBinding>() {
    private val myCalendar = Calendar.getInstance()
    lateinit var date: DatePickerDialog.OnDateSetListener
    private var imageResultPath = ""
    private var imageList = ArrayList<String>()
    private lateinit var imagesAdapter: EditProfileImagesAdapter

    override fun getViewBinding(): FragmentEditProfileBinding {
        return FragmentEditProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClicks()

        genderSpinner()
        interestedInSpinner()

    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.etDOB.setOnClickListener {
            date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.etDOB.setText(longToDate(myCalendar.timeInMillis, "dd/MM/yy"))
//                    edtIssuedOnDate.setText(AppUtils.longToDate(myCalendar.timeInMillis))
            }
            datePicker(requireContext())
        }

        // edit profile images adapter
        imagesAdapter = EditProfileImagesAdapter(requireContext(), imageList)
        binding.rvEditProfile.adapter = imagesAdapter
        imagesAdapter.onItemClickListener = {
            getImage(requireActivity(), 1, false)
        }

        binding.btUpdate.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.ivCamera.setOnClickListener {
            getImage(requireActivity(), 0, false)
        }


    }

    private fun datePicker(context: Context) {
        DatePickerDialog(
            context, R.style.DialogTheme,
            date,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONDAY],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    // set Gender spinner
    private fun genderSpinner() {
        val superHero = resources.getStringArray(R.array.selectGender)
//        val superHero = arrayOf<String?>("Batman", "SuperMan", "Flash", "AquaMan", "Shazam")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.items_spinner_list, superHero)
        arrayAdapter.setDropDownViewResource(R.layout.items_spinner_list)
        binding.spGender.adapter = arrayAdapter

        binding.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (parent?.getChildAt(0) as TextView).setTextColor(Color.parseColor("#000000"))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

//                (parent?.getChildAt(0) as TextView).setTextColor(Color.parseColor("#000000"))
            }

        }
    }

    // set Interested in spinner
    private fun interestedInSpinner() {
        val superHero = resources.getStringArray(R.array.selectGender)
//        val superHero = arrayOf<String?>("Batman", "SuperMan", "Flash", "AquaMan", "Shazam")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.items_spinner_list, superHero)
        arrayAdapter.setDropDownViewResource(R.layout.items_spinner_list)
        binding.spInterestedIn?.adapter = arrayAdapter

        binding.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (parent?.getChildAt(0) as TextView).setTextColor(Color.parseColor("#000000"))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

//                (parent?.getChildAt(0) as TextView).setTextColor(Color.parseColor("#000000"))
            }

        }
    }

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            if (code == 0) {
                imageResultPath = imagePath
                Glide.with(context!!).load(imageResultPath).into(binding.rivUser)
            } else {
                imageList.add(imagePath)
                imagesAdapter.notifyDataSetChanged()
            }


        }
    }

}