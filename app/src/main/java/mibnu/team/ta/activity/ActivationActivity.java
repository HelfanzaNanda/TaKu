package mibnu.team.ta.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import mibnu.team.ta.R;
import mibnu.team.ta.models.User;
import mibnu.team.ta.models.UserCapil;
import mibnu.team.ta.webservices.ApiClient;
import mibnu.team.ta.webservices.ApiService;
import mibnu.team.ta.webservices.MyApiService;
import mibnu.team.ta.webservices.WrappedResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationActivity extends AppCompatActivity {
    private MaterialButton btn_activate;
    private TextInputLayout in_nik;
    private TextInputEditText et_nik;
    private ApiService apiService;
    private MyApiService myApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
//        getSupportActionBar().hide();
        setupUI();
        doActivate();
    }

    private void setupUI(){
        btn_activate = findViewById(R.id.btn_activate);
        in_nik = findViewById(R.id.in_nik);
        et_nik = findViewById(R.id.et_nik);
        myApiService = ApiClient.Companion.instanceBackend();
        apiService = ApiClient.Companion.instanceCapil();
    }

    private void doActivate(){
        btn_activate.setOnClickListener(v -> {
            String nik = et_nik.getText().toString().trim();
            if(validate(nik)){
                isLoading(true);
                apiService.activate(ApiClient.DEF_USER_ID, ApiClient.DEF_PASSWORD, nik).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                String r = response.body().string();
                                System.err.println(r);
                                System.err.println(response.body());
                                JSONObject jsonObject = new JSONObject(r);
                                int totalElements = jsonObject.getInt("totalElements");
                                if(totalElements == 0){
                                    JSONObject message = (JSONObject) jsonObject.get("content");
                                    showAlert(message.getString("RESPON") + ". NIK anda tidak terdaftar sebagai warga Kota Tegal.");
                                    isLoading(false);
                                }else {
                                    JSONArray multipleResult = jsonObject.getJSONArray("content");
                                    JSONObject jsonObj = multipleResult.getJSONObject(0);
                                    UserCapil user = new Gson().fromJson(jsonObj.toString(), UserCapil.class);
                                    getFcmToken(user);
                                    //sendToMyBackend(user);
                                    System.err.println(jsonObject.getJSONArray("content"));
                                }
                            } catch (JSONException | IOException e) {
                                toast(e.getMessage());
                                e.printStackTrace();
                                isLoading(false);
                            }
                        }else{
                            isLoading(false);
                            toast("Response is not success");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        toast("Failure "+t.getMessage());
                        isLoading(false);

                    }
                });

            }
        });
    }

    private Boolean validate(String nik){
        setNikError(null);
        if(nik.isEmpty() || nik.length() < 16){
            setNikError("NIK setidaknya memiliki enam belas karakter");
            return false;
        } else {
            return true;
        }
    }

    private void setNikError(String err){ in_nik.setError(err); }

    private void toast(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    private void isLoading(Boolean state){
        btn_activate.setEnabled(!state);
    }

    private void showAlert(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Mengerti", (dialog, id) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sendToMyBackend(UserCapil user, String fcmToken){
        User u = new User();
        u.setNik(user.getNik());
        u.setNama(user.getNamaLengkap());
        u.setKk(user.getNomorKK());
        u.setJk(user.getJenisKelamin());
        u.setKec(user.getNamaKecamatan());
        u.setKota(user.getNamaKabupaten());
        u.setKel(user.getNamaKelurahan());
        u.setRw(user.getRw());
        u.setRt(user.getRt());
        u.setNama_ibu(user.getNamaIbu());
        u.setNama_ayah(user.getNamaAyah());
        u.setTgl_lhr(user.getTanggalLahir());
        u.setTmpt_lhr(user.getTempatLahir());
        u.setAlamat(user.getAlamat());
        u.setFcmToken(fcmToken);
        Gson gson = new Gson();
        isLoading(true);
        System.err.println(gson.toJson(u));
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(u));
        myApiService.registerUser(body).enqueue(new Callback<WrappedResponse<User>>() {
            @Override
            public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                if(response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if(body.getStatus()){
                        showAlert("Aktivasi berhasil. Silakan login dengan NIK dengan password default NIK milik anda.");
                    }else{
                        showAlert("Tidak dapat mengaktivasi akun");
                    }
                }else{
                    showAlert("Gagal saat mengaktivasi akun. Mungkin sudah pernah diaktivasi sebelumnya");
                }
                isLoading(false);
            }

            @Override
            public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                System.err.println(t.getMessage());
                isLoading(false);
                showAlert("Tidak dapat mengaktivasi akun anda. Mungkin sudah pernah diaktivasi sebelumnya.");
            }
        });

    }

    private void getFcmToken(UserCapil user){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               if (task.getResult() != null){
                   String fcmToken = task.getResult().getToken();
                   Toast.makeText(this, fcmToken, Toast.LENGTH_LONG).show();
                   sendToMyBackend(user, fcmToken);
               }else {
                   Toast.makeText(this, "Failed to get firebase token", Toast.LENGTH_SHORT).show();
               }
           }else {
               Toast.makeText(this, "Cannot get firebase token", Toast.LENGTH_SHORT).show();
           }
        });
    }
}
