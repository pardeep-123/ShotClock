package com.app.shotclock.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.adapters.CompleteProfileImagesAdapter
import com.app.shotclock.databinding.FragmentCompleteProfileBinding
import com.app.shotclock.utils.ImagePickerUtility1
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.longToDate
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.collections.ArrayList

class CompleteProfileFragment : ImagePickerUtility1() {

    private val myCalendar = Calendar.getInstance()
    lateinit var date: DatePickerDialog.OnDateSetListener
    private var imageList = ArrayList<String>()
    private lateinit var completeProfileImagesAdapter: CompleteProfileImagesAdapter

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            imageList.add(imagePath)
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

        genderSpinner()
        interestedInSpinner()
        completeProfileImageAdapter()
    }

    private fun completeProfileImageAdapter() {
        // complete profile images adapter
        completeProfileImagesAdapter = CompleteProfileImagesAdapter(imageList)
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
}