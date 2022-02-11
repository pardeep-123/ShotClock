package com.app.shotclock.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.app.shotclock.R
import com.app.shotclock.adapters.EditProfileImagesAdapter
import com.app.shotclock.databinding.FragmentEditProfileBinding
import com.app.shotclock.utils.ImagePickerUtility1
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.longToDate
import com.app.shotclock.utils.setPopUpWindow
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

    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvDOBSelect.setOnClickListener {
            date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.tvDOBSelect.setText(longToDate(myCalendar.timeInMillis, "dd/MM/yy"))
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

        binding.tvGenderSelect.setOnClickListener {
            val list = ArrayList<String>()
            list.add("Male")
            list.add("Female")
            setPopUpWindow(binding.tvGenderSelect, requireContext(), list)
        }
        binding.tvSmokingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvSmokingSelect, requireContext(), list)
        }
        binding.tvDrinkingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvDrinkingSelect, requireContext(), list)
        }
        binding.tvPetsSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvPetsSelect, requireContext(), list)
        }
        binding.tvHeightSelect.setOnClickListener {
            val list = ArrayList<String>()
            val heights = resources.getStringArray(R.array.heights)
            list.addAll(heights)
            setPopUpWindow(binding.tvHeightSelect, requireContext(), list)
        }

        binding.tvQualificatSelect.setOnClickListener {
            val list = ArrayList<String>()
            list.add("High School")
            list.add("Bachelor's Degree")
            list.add("Master's Degree")
            list.add("Doctorate Degree")
            list.add("Other")
            setPopUpWindow(binding.tvQualificatSelect, requireContext(), list)
        }

        binding.tvInterestSelect.setOnClickListener {
            val list = ArrayList<String>()
            val interests = resources.getStringArray(R.array.interests)
            list.addAll(interests)
            setPopUpWindow(binding.tvInterestSelect, requireContext(), list)
        }

        binding.tvSexualOrientationSelect.setOnClickListener {
            val list = ArrayList<String>()
            val sexualOrientation = resources.getStringArray(R.array.sexualOrientation)
            list.addAll(sexualOrientation)
            setPopUpWindow(binding.tvInterestSelect, requireContext(), list)
        }

        binding.tvAstrologicalSignSelect.setOnClickListener {
            val list = ArrayList<String>()
            val astrological = resources.getStringArray(R.array.astrologicalSign)
            list.addAll(astrological)
            setPopUpWindow(binding.tvAstrologicalSignSelect, requireContext(), list)
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