package com.gaurav.heartone

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.concurrent.timer


/**
 * A simple [Fragment] subclass.
 * Use the [Loading.newInstance] factory method to
 * create an instance of this fragment.
 */
class Loading : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onStart() {
        super.onStart()
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, Playlist())
        val handler = Handler()

        handler.postDelayed({
            fragmentTransaction.commit();
        },3000);
    }
}