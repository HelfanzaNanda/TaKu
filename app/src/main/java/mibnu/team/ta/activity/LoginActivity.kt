package mibnu.team.ta.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import mibnu.team.ta.R
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.viewmodels.UserState
import mibnu.team.ta.viewmodels.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var btnAktif: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        dologin()
        userViewModel.listenUIState().observer(this, Observer { handleState(it) })
        btnAktif =findViewById(R.id.formAktif)
        btnAktif.setOnClickListener { startActivity(Intent(this,
            ActivationActivity::class.java)) }
    }

    private fun dologin(){
        btn_login.setOnClickListener {
            val nik = et_nik.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (userViewModel.validate(nik, password)){
                userViewModel.login(nik, password)
            }

        }
    }

    private fun toast(m : String) = Toast.makeText(this,m,Toast.LENGTH_LONG).show()
    private  fun setErrorNik(err:String?){in_nik.error=err}
    private  fun setErrorPassword(err: String?){in_password.error=err}
    private  fun handleState(it: UserState){
        when(it){
            is UserState.ShowAlert -> showAlert(it.message)
            is UserState.ShowToast -> toast(it.message)
            is UserState.IsLoading -> btn_login.isEnabled = !it.state
            is  UserState.Reset->{
                setErrorNik(null)
                setErrorPassword(null)
            }
            is  UserState.Validate->{
                it.nik?.let { e-> setErrorNik(e) }
                it.password?.let { e->setErrorPassword(e) }
            }
            is UserState.Success ->{
                Utilities.setToken(this@LoginActivity, it.param)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showAlert(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("Mengerti"){
                dialog, which -> dialog.dismiss()
            }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        if (Utilities.getToken(this) != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}
