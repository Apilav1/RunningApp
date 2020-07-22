package com.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.runningapp.R
import com.runningapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.runningapp.other.Constants.KEY_NAME
import com.runningapp.other.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup){

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject //because boolean is primitive type
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()

            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        tvContinue.setOnClickListener {
            val success = writePersonalDataSharedPref()

            if(success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment) //id of the action to perform navigation from setup fragment to run fragment

            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    //returns boolean as an indicator if any data was entered
    private fun writePersonalDataSharedPref(): Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()) return false

        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())//we don't validate here weight because of the setup input type of text
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply() //apply() is asynchronous and .commit() is not

        val toolbarText = "Let's go, $name!"
        requireActivity().tvToolbarTitle.text = toolbarText

        return true
    }
}