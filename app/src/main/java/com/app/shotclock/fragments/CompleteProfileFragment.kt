package com.app.shotclock.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.adapters.CompleteProfileImagesAdapter
import com.app.shotclock.databinding.FragmentCompleteProfileBinding
import com.app.shotclock.utils.ImagePickerUtility1
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.longToDate
import com.app.shotclock.utils.setPopUpWindow
import com.google.android.material.button.MaterialButton
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class CompleteProfileFragment : ImagePickerUtility1<FragmentCompleteProfileBinding>() {

    private val myCalendar = Calendar.getInstance()
    lateinit var date: DatePickerDialog.OnDateSetListener
    private var imageList = ArrayList<String>()
    private lateinit var completeProfileImagesAdapter: CompleteProfileImagesAdapter

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            imageList.add(imagePath.toString())
            completeProfileImagesAdapter.notifyDataSetChanged()
        }
    }

    override fun getViewBinding(): FragmentCompleteProfileBinding {
        return FragmentCompleteProfileBinding.inflate(layoutInflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clicksHandle(view)
        completeProfileImageAdapter()
    }

    // complete profile images adapter
    private fun completeProfileImageAdapter() {
        completeProfileImagesAdapter = CompleteProfileImagesAdapter(requireContext(), imageList)
        binding.rvUserImages.adapter = completeProfileImagesAdapter

        completeProfileImagesAdapter.onItemCLickListener = {
            getImage(requireActivity(), 0, false)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clicksHandle(view: View) {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvDOBSelect.setOnClickListener {
            date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.tvDOBSelect.text = longToDate(myCalendar.timeInMillis, "dd/MM/yy")
//                    edtIssuedOnDate.setText(AppUtils.longToDate(myCalendar.timeInMillis))
            }
            datePicker(requireContext())
        }

        binding.etAddBio.setOnClickListener {
            view.setOnTouchListener(View.OnTouchListener { v, event ->
                if (view.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return@OnTouchListener true
                        }
                    }
                }
                false
            })
        }

        binding.btSubmit.setOnClickListener {
            val dialog = Dialog(requireContext())
            with(dialog) {
                setCancelable(false)
                setContentView(R.layout.alert_dialog_submitted)
                val btDone: MaterialButton = findViewById(R.id.btDone)
                btDone.setOnClickListener {
                    findNavController().navigate(R.id.action_completeProfileFragment_to_homeActivity)
                    dismiss()
                }
                show()
            }
        }

        binding.tvGenderSelect.setOnClickListener {
            val list = ArrayList<String>()
            list.add("Male")
            list.add("Female")
            setPopUpWindow(binding.tvGenderSelect, requireContext(), list)
        }
        binding.tvSmokingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList =resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvSmokingSelect, requireContext(), list)
        }
        binding.tvDrinkingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList =resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvDrinkingSelect, requireContext(), list)
        }
        binding.tvPetsSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList =resources.getStringArray(R.array.yesNoList)
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

}