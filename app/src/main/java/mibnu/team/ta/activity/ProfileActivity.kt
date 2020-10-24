package mibnu.team.ta.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fxn.utility.Constants
import kotlinx.android.synthetic.main.activity_profile.*
import mibnu.team.ta.R
import mibnu.team.ta.models.User
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.viewmodels.UserState
import mibnu.team.ta.viewmodels.UserViewModel


class ProfileActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenUIState().observer(this, Observer { handleUI(it) })
        userViewModel.listenToCurrentUser().observe(this, Observer { attachToUI(it) })
        Utilities.getToken(this)?.let { token -> userViewModel.profile(token) }
        updatePass()
    }

    private fun handleUI(it : UserState){
        when(it){
            is UserState.ShowToast -> toast(it.message)
            is UserState.Sucess -> finish()
        }
    }

    private fun attachToUI(user : User){
        kk.setText(user.kk)
        nik.setText(user.nik)
        name.setText(user.nama)
    }
    private fun updatePass(){
        btn_simpan.setOnClickListener {
            val password = password.text.toString().trim()
            userViewModel.updateProfile(Utilities.getToken(this@ProfileActivity)!!, password)
        }
        btn_logout.setOnClickListener {
            Utilities.clearToken(this@ProfileActivity)
            startActivity(Intent(this@ProfileActivity,LoginActivity::class.java))
            this@ProfileActivity.finish()
        }
    }

    private fun toast(message : String) = Toast.makeText(this@ProfileActivity, message, Toast.LENGTH_SHORT).show()


}
