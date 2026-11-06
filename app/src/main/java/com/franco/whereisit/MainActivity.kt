package com.franco.whereisit

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.franco.whereisit.data.memory.MemoryDataRepository
import com.franco.whereisit.domain.model.User
import com.franco.whereisit.util.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fabPublish = findViewById<ExtendedFloatingActionButton>(R.id.fabPublish_main)
        fabPublish.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(this, ItemFormActivity::class.java)
        })

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav_main)
        Util.setupBottomNav(this, bottomNav, R.id.nav_home)

        askUserName()
    }

    private fun askUserName() {
        val repository = MemoryDataRepository()
        if (repository.getCurrentUser() != null) return

        val input = TextInputEditText(this)
        input.hint = getString(R.string.dialog_name_hint)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_name_title)
            .setMessage(R.string.dialog_name_message)
            .setView(input)
            .setCancelable(false)
            .setPositiveButton(R.string.action_confirm, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val name = input.text.toString().trim()
                if (name.isEmpty()) {
                    input.error = getString(R.string.error_name_required)
                } else {
                    repository.setCurrentUser(
                        User(id = UUID.randomUUID().toString(), name = name)
                    )
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        findViewById<BottomNavigationView>(R.id.bottomNav_main).selectedItemId = R.id.nav_home
    }
}